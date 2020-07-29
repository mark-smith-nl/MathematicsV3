package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.smith.mathematics.util.MathematicalMethodUtil.*;

/**
 * Class to store information about a method annotated with @{@link nl.smith.mathematics.annotation.MathematicalFunction}
 */
public class MathematicalFunctionMethodMapping<N extends Number> {

    /** The (function container) object in which the method resides. */
    private final RecursiveFunctionContainer<N, ? extends RecursiveFunctionContainer<N, ?>> container;

    private final String name;

    private final String description;

    private final Method method;

    private final String signature;

    private final int parameterCount;

    private final boolean isVararg;

    public MathematicalFunctionMethodMapping(RecursiveFunctionContainer<N, ? extends RecursiveFunctionContainer<N, ?>> container, Method method) {
        this.container = container;

        checkGenericsEnclosingClass(method.getDeclaringClass());
        checkModifiers(method);
        checkReturnType(method);
        checkArguments(method);

        MathematicalFunction annotation = method.getAnnotation(MathematicalFunction.class);

        name = "".equals(annotation.name()) ? method.getName() : annotation.name();
        description = annotation.description();
        this.method = method;
        parameterCount = method.getParameterCount();
        isVararg = method.isVarArgs();
        signature = getMathematicalMethodSignature();
    }

    public RecursiveFunctionContainer<N, ? extends RecursiveFunctionContainer<N, ?>> getContainer() {
        return container;
    }

    public String getName() {
        return name;
    }

    public String getMethodName() {
        return method.getName();
    }

    public String getDescription() {
        return description;
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public boolean isVararg() {
        return isVararg;
    }

    public String getSignature() {
        return signature;
    }

    public N invokeWithNumbers(N... arguments){
        Object[] invocationParameters = getInvocationParameters(arguments);
        try {
            // Note: We have to use the sibling container since this is the @Validated container.
            return (N) method.invoke(container.getSibling(), invocationParameters);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @SafeVarargs
    private <N extends Number> Object[] getInvocationParameters(N... arguments) {
        Object[] parameters = new Object[parameterCount];

        if (isVararg) {
            int numberOfExplicitlyDeclaredParameters = parameterCount - 1;

            if (numberOfExplicitlyDeclaredParameters >= 0)
                System.arraycopy(arguments, 0, parameters, 0, numberOfExplicitlyDeclaredParameters);

            int numberOfVarArgParameters = arguments.length - numberOfExplicitlyDeclaredParameters;
            Object varArgParameters = Array.newInstance(container.getNumberType(), numberOfVarArgParameters);
            for (int i = 0; i < arguments.length - numberOfExplicitlyDeclaredParameters; i++) {
                Array.set(varArgParameters, i, arguments[numberOfExplicitlyDeclaredParameters + i]);
            }

            parameters[parameterCount - 1] = varArgParameters;
        } else {
            parameters = arguments;
        }

        return parameters;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MathematicalFunctionMethodMapping that = (MathematicalFunctionMethodMapping) o;
        return parameterCount == that.parameterCount &&
                isVararg == that.isVararg &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameterCount, isVararg);
    }

    private String getMathematicalMethodSignature() {
        return  name + "(" +
                getMathematicalMethodGenericParameterTypesAsString() + ")";
    }

    private String getMathematicalMethodGenericParameterTypesAsString() {
        return Stream.of(method.getGenericParameterTypes()).map(genericParameterType -> {
            if (GenericArrayType.class.isAssignableFrom(genericParameterType.getClass())) {
                return genericParameterType.getTypeName();
            }

            return ((TypeVariable<?>) genericParameterType).getName();
        }).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return method.getDeclaringClass().getCanonicalName() + getMethodName() + "--->" + signature + " (" + description +")";
    }
}
