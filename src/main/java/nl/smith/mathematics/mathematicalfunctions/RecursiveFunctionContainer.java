package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.service.RecursiveValidatedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class RecursiveFunctionContainer<N extends Number, S extends RecursiveFunctionContainer> extends RecursiveValidatedService<S> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecursiveFunctionContainer.class);

    /** The annotation that describes the container. */
    protected final MathematicalFunctionContainer annotation;

    private final Class<N> numberType;

    /** Map of callable mathematical functions using the methods signature as key. */
    private final Map<String, MathematicalMethod> mathematicalFunctions;

    public RecursiveFunctionContainer(Class<? extends RecursiveFunctionContainer> mathematicalFunctionContainerClass) {
        annotation = getAnnotationFromGenericSuperClass(mathematicalFunctionContainerClass);

        numberType = getNumberTypeFromBeanClass();

        mathematicalFunctions = getMathematicalFunctionsFromGenericSuperClass(mathematicalFunctionContainerClass);

        LOGGER.info("Instantiated {}", this.getClass().getCanonicalName());
    }

    private MathematicalFunctionContainer getAnnotationFromGenericSuperClass(Class<? extends RecursiveFunctionContainer> mathematicalFunctionContainerClass) {
        if (mathematicalFunctionContainerClass == null) {
            throw new IllegalStateException(String.format("Please specify an abstract mathematical function container class to extract the %s annotation from.",
                    MathematicalFunctionContainer.class.getCanonicalName()));
        }

        if (mathematicalFunctionContainerClass.getSuperclass() != RecursiveFunctionContainer.class) {
            throw new IllegalStateException(String.format("Incorrect class hierarchy.\nSpecified class %s should directly extend %s.",
                    mathematicalFunctionContainerClass.getCanonicalName(),
                    RecursiveFunctionContainer.class.getCanonicalName()));
        }

        MathematicalFunctionContainer annotation = mathematicalFunctionContainerClass.getAnnotation(MathematicalFunctionContainer.class);
        if (annotation == null) {
            throw new IllegalStateException(String.format("Please annotate your class %s with %s.",
                    mathematicalFunctionContainerClass.getCanonicalName(),
                    MathematicalFunctionContainer.class.getCanonicalName()));
        }

        return annotation;
    }

    /**
     * Protected for test purposes.
     */
    private Class<N> getNumberTypeFromBeanClass() {
        Class<?> functionContainerClazz = this.getClass();

        Type genericInterfaceClazz = functionContainerClazz.getGenericSuperclass();

        return (Class<N>) ((ParameterizedType) genericInterfaceClazz).getActualTypeArguments()[0];
    }

    private Map<String, MathematicalMethod> getMathematicalFunctionsFromGenericSuperClass(Class<? extends RecursiveFunctionContainer> mathematicalFunctionContainerClass) {
        return Stream.of(mathematicalFunctionContainerClass.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> Modifier.isAbstract(m.getModifiers()))
                .filter(m -> m.getAnnotation(MathematicalFunction.class) != null)
                .map(MathematicalMethod::new)
                .collect(Collectors.toMap(MathematicalMethod::getSignature, mm -> mm));
    }

    public Class<N> getNumberType() {
        return numberType;
    }

    public String getName() {
        return annotation.name();
    }

    public String getDescription() {
        return annotation.description();
    }

    public Map<String, MathematicalMethod> getMathematicalFunctions() {
        return mathematicalFunctions;
    }

}
