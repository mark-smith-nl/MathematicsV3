package nl.smith.mathematics.service;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import org.springframework.core.BridgeMethodResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodSignature {

    private final Method method;

    private final MathematicalFunction annotation;

    private final Class<?> declaringClass;

    private final String name;

    private final Parameter[] parameters;

    public static MethodSignature getMethodSignature(Method method){
        Method targetMethod = null;
        MathematicalFunction annotation = null;
        MethodSignature methodSignature;

        if (method.isBridge()) {
            Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
             annotation = bridgedMethod.getAnnotation(MathematicalFunction.class);

            Class superClazz = method.getDeclaringClass().getSuperclass();
            String name = method.getName();
            while (superClazz != FunctionContainer.class || annotation != null) {
                Object[] objects = Stream.of(method.getParameters()).map(Parameter::getType).collect(Collectors.toList()).toArray();
                Class[] parameterClasses = Arrays.copyOf(objects, objects.length, Class[].class);
                superClazz.getDeclaredMethod(name, parameterClasses);
                //System.out.println(declaredMethod.getAnnotations().length);
            }
            methodSignature = new MethodSignature(bridgedMethod, annotation);

        } else {
            annotation = method.getAnnotation(MathematicalFunction.class);
            targetMethod = method;
            methodSignature = new MethodSignature(method, method.getAnnotation(MathematicalFunction.class));
        }

        if (targetMethod != null && annotation != null) {
            methodSignature = new MethodSignature(targetMethod, annotation);
        }
        return methodSignature;
    }

    public MethodSignature(Method method, MathematicalFunction annotation) {
        this.method = method;
        declaringClass = method.getDeclaringClass();
        name = method.getName();
        parameters = method.getParameters();
        this.annotation = annotation;
    }


    public Method getMethod() {
        return method;
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public String getName() {
        return name;
    }

    public Parameter[] getParameters() {
        return parameters;
    }


    @Override
    public String toString() {
        return String.format("%-20s\t%-30s\t%40s", name, declaringClass.getSimpleName(), parametersAsString(parameters));
    }

    private static String parametersAsString(Parameter[] parameters) {
        String parametersAsString = "";
        for (Parameter parameter: parameters) {
            parametersAsString += parameter.getType().getSimpleName() + " (" + parameter.getName() + ")";
        }

        return parametersAsString;
    }

}
