package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.annotation.MathematicalFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.Signature;
import java.util.*;
import java.util.stream.Collectors;

@Validated
@MathematicalFunctions(name = "Unnamed functionset", description = "There is no description of this set of mathematical functions")
public abstract class AbstractFunctionContainer<T extends Number> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractFunctionContainer.class);

    private Class<T> numberType;

    private Map<MethodSignature, Method> callableMathematicalFunctions = new HashMap<>();

    public Class<T> getNumberType() {
        return numberType;
    }

    @PostConstruct
    private void postConstruct() {
        setNumberType();
        setCallableMathematicalFunctions();
    }

    private void setNumberType() {
        Class<?> functionContainerClazz = this.getClass();

        if (functionContainerClazz.getAnnotation(MathematicalFunctions.class) == AbstractFunctionContainer.class.getAnnotation(MathematicalFunctions.class)) {
            LOGGER.warn("Please annotate your {} subclass with the {} annotation describing the name and context of your mathematical function set.",
                    AbstractFunctionContainer.class.getCanonicalName(), MathematicalFunctions.class.getCanonicalName());
        }

        Type genericInterfaceClazz = functionContainerClazz.getGenericSuperclass();

        numberType = (Class<T>) ((ParameterizedType) genericInterfaceClazz).getActualTypeArguments()[0];
    }

    private void setCallableMathematicalFunctions() {
        Class<?> functionContainerClazz = this.getClass();
        Class<?> functionContainerSuperClazz = functionContainerClazz.getSuperclass();

        // @formatter:off
        Set<Method> mathematicalFunctions = Arrays.stream(functionContainerSuperClazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(MathematicalFunction.class))
                // .filter(m -> Modifier.isAbstract(m.getModifiers()))
                //.filter(m -> m.getReturnType() == numberType)
                //.filter(m -> m.getParameterCount() > 0)
                .collect(Collectors.toSet());
        // @formatter:on

        mathematicalFunctions.forEach(m -> {
            callableMathematicalFunctions.put(new MethodSignature(m.getAnnotation(MathematicalFunction.class), m.getName(), m.getParameterCount()), m);
        });

    }

    public Map<MethodSignature, Method> getCallableMathematicalFunctions() {
        return callableMathematicalFunctions;
    }
}
