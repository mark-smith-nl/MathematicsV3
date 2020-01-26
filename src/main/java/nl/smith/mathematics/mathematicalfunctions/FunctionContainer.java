package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
//@MathematicalFunctions(name = "Unnamed functionset", description = "There is no description of this set of mathematical functions")
public abstract class FunctionContainer<T extends Number> {

    private final static Logger LOGGER = LoggerFactory.getLogger(FunctionContainer.class);

    private Class<T> numberType;

    /** Map of callable mathematical functions using the methods signature as key. */
    private Map<String, MathematicalMethod> mathematicalFunctions = new HashMap<>();

    public Class<T> getNumberType() {
        return numberType;
    }

    /** Protected for test purposes. */
    @PostConstruct
    private void postConstruct() {
        checkAnnotation();
        initializeNumberType();
        initializeMathematicalFunctions();
    }

    /** Protected for test purposes. */
    protected void checkAnnotation() {
        Class<?> functionContainerClazz = this.getClass();
        Class<?> functionContainerSuperClazz = functionContainerClazz.getSuperclass();

        if (functionContainerClazz.isAnnotationPresent(MathematicalFunctions.class)) {
           // throw new MissingClassAnnotationException(FunctionContainer.class, MathematicalFunctions.class.getCanonicalName()));
        }

        if (!functionContainerSuperClazz.isAnnotationPresent(MathematicalFunctions.class)) {
            // throw new MissingClassAnnotationException(FunctionContainer.class, MathematicalFunctions.class.getCanonicalName()));
        }
      /*  if (functionContainerClazz.getAnnotation(MathematicalFunctions.class) == FunctionContainer.class.getAnnotation(MathematicalFunctions.class)) {
            throw new IllegalStateException(String.format("Please annotate your %s (abstract) subclass mathematical function container with the %s annotation describing the name and context of your mathematical function set.",
                    FunctionContainer.class.getCanonicalName(), MathematicalFunctions.class.getCanonicalName()));
        }*/
    }

    /** Protected for test purposes. */
    protected void initializeNumberType() {
        Class<?> functionContainerClazz = this.getClass();

        Type genericInterfaceClazz = functionContainerClazz.getGenericSuperclass();

        numberType = (Class<T>) ((ParameterizedType) genericInterfaceClazz).getActualTypeArguments()[0];
    }

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
