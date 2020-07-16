package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;

import java.lang.reflect.Method;
import java.util.Objects;

import static nl.smith.mathematics.util.MathematicalMethodUtil.*;

/**
 * Class to store information about a method annotated with @{@link nl.smith.mathematics.annotation.MathematicalFunction}
 */
public class MathematicalFunctionMethodMapping {

    private final RecursiveFunctionContainer<? extends Number, ?> container;

    private final String name;

    private final String description;

    private final Method method;

    private final String signature;

    private final int parameterCount;

    private final boolean isVararg;

    public MathematicalFunctionMethodMapping(RecursiveFunctionContainer<? extends Number, ?> container, Method method) {
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
        signature = getMathematicalMethodSignature(this);
    }

    public RecursiveFunctionContainer<? extends Number, ?> getContainer() {
        return container;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Method getMethod() {
        return method;
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
}
