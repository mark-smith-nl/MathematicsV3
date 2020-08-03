package nl.smith.mathematics.domain;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.exception.MathematicalFunctionMethodMappingException;
import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static nl.smith.mathematics.util.MathematicalMethodUtil.*;

/**
 * Class to store information about a method annotated with @{@link nl.smith.mathematics.annotation.MathematicalFunction}
 */
public class MathematicalFunctionMethodMapping<N extends Number> {

    /**
     * The (function container) object in which the method resides.
     */
    private final RecursiveFunctionContainer<N, ? extends RecursiveFunctionContainer<N, ?>> container;

    private final String name;

    private final String description;

    private final Method method;

    private final String signature;

    private final int parameterCount;

    private final boolean isVararg;

    private final MathematicalFunction.Type type;

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
        type = annotation.type();

        if (!name.matches(type.getRegex())) {
            throw new IllegalStateException(format("The name '%s' can not be used to reference a method(%s) of type %s.%nThe name should comply to the regular expression '%s'", name, method, type, type.getRegex()));
        }

        if (!type.getParameterCountChecker().test(parameterCount)) {
            throw new IllegalStateException(format("The number of arguments of method '%s' (%s) does not comply to its type of %s.", name, method, type));
        }

        signature = getMathematicalMethodSignature();
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

    public MathematicalFunction.Type getType() {
        return type;
    }

    public String getSignature() {
        return signature;
    }

    public N invokeWithNumbers(N... arguments) {
        Object[] invocationParameters = getInvocationParameters(arguments);
        try {
            // Note: We have to use the sibling container since this is the @Validated container.
            return (N) method.invoke(container.getSibling(), invocationParameters);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) targetException;
                throw new IllegalArgumentException(constraintViolationException.getMessage());
            }
            throw new MathematicalFunctionMethodMappingException(format("Can not invoke method %s.", signature), e, this, arguments);
        } catch (IllegalAccessException e) {
            throw new MathematicalFunctionMethodMappingException(format("Can not invoke method %s.", signature), e, this, arguments);
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
        MathematicalFunctionMethodMapping<N> that = (MathematicalFunctionMethodMapping) o;
        return parameterCount == that.parameterCount &&
                isVararg == that.isVararg &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameterCount, isVararg);
    }

    private String getMathematicalMethodSignature() {
        return name + "(" +
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
        return String.format("%-105s ---> %-20s%-35s(%s)",
                method.getDeclaringClass().getCanonicalName() +"." + getMethodName() ,
                signature,
                type,
                description);
    }
}
