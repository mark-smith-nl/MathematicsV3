package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;

import java.lang.reflect.Method;

import static nl.smith.mathematics.util.MathematicalMethodUtil.*;

/** Class to store information of a method annotated with @{@link nl.smith.mathematics.annotation.MathematicalFunction}*/
public class MathematicalMethod {

    private final String name;

    private final String description;

    private final String signature;

    private final Method method;

    public MathematicalMethod(Method method) {
        checkModifiers(method);

        checkReturnType(method);

        MathematicalFunction annotation = method.getAnnotation(MathematicalFunction.class);

        name = "".equals(annotation.name()) ? method.getName() : annotation.name();
        description = annotation.description();
        signature = getSignatureFromMethod(method);
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSignature() {
        return signature;
    }

    public Method getMethod() {
        return method;
    }
}
