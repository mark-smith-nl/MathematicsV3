package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.service.MethodAnnotationFinderService;
import nl.smith.mathematics.service.RecursiveValidatedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class FunctionContainer<N extends Number, S extends FunctionContainer> extends RecursiveValidatedService<S> {

    private final static Logger LOGGER = LoggerFactory.getLogger(FunctionContainer.class);

    private Class<N> numberType;

    /**
     * Map of callable mathematical functions using the methods signature as key.
     */
    private Map<String, MathematicalMethod> mathematicalFunctions = new HashMap<>();

    public Class<N> getNumberType() {
        return numberType;
    }

    public abstract String getDescription();

    public FunctionContainer() {
        LOGGER.info("Instantiating {}", this.getClass().getCanonicalName());
    }

    /**
     * Protected for test purposes.
     */
    @PostConstruct
    private void postConstruct() {
       initializeNumberType();
      //  initializeMathematicalFunctions();
    }

    /**
     * Protected for test purposes.
     */
    protected void initializeNumberType() {
        Class<?> functionContainerClazz = this.getClass();

        Type genericInterfaceClazz = functionContainerClazz.getGenericSuperclass();

        numberType = (Class<N>) ((ParameterizedType) genericInterfaceClazz).getActualTypeArguments()[0];
    }

    /**
     *  Check if the MathematicalFunction annotation is present on the concrete class.
     *  If not present is bridge than check parent class
     *  Check return type
     */
    private void initializeMathematicalFunctions() {
        Class<?> functionContainerClazz = this.getClass();
        Class<?> functionContainerSuperClazz = functionContainerClazz.getSuperclass();

        try {
            // @formatter:off
            mathematicalFunctions = Stream.of(functionContainerSuperClazz.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(MathematicalFunction.class))
                    .map(MathematicalMethod::new)
                    .collect(Collectors.toMap(MathematicalMethod::getSignature, Function.identity()));
            // @formatter:on
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("Duplicate key")) {
                throw new IllegalStateException(String.format("\nThere are multiple mathematical functions (i.e. methods annotated with %2$s) found in class %1$s.", functionContainerSuperClazz.getCanonicalName(), MathematicalFunction.class.getCanonicalName()));
            }
        }

    }

    public Map<String, MathematicalMethod> getMathematicalFunctions() {
        return mathematicalFunctions;
    }

}
