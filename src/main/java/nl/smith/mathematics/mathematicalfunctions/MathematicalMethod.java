package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;

import java.lang.reflect.Method;

import static nl.smith.mathematics.util.MathematicalMethodUtil.*;

/**
 * Class to store information about a method annotated with @{@link nl.smith.mathematics.annotation.MathematicalFunction}
 */
public class MathematicalMethod {

    private final String name;

    private final String description;

    private final Method method;

    private final String signature;

    public MathematicalMethod(Method method) {
        checkGenericsEnclosingClass(method.getDeclaringClass());
        checkModifiers(method);
        checkReturnType(method);
        checkArguments(method);

        MathematicalFunction annotation = method.getAnnotation(MathematicalFunction.class);

        name = "".equals(annotation.name()) ? method.getName() : annotation.name();
        description = annotation.description();
        this.method = method;
        signature = getMathematicalMethodSignature(this);
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

    public String getSignature() {
        return signature;
    }

}
