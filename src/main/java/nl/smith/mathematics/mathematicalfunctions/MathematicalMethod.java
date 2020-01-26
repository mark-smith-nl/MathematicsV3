package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class MathematicalMethod {

    private final String name;

    private final String description;

    private final String signature;

    private final Method method;

    public MathematicalMethod(Method method) {
        MathematicalFunction annotation = method.getAnnotation(MathematicalFunction.class);

        // Check modifiers: The method should be a non static abstract method (i.e. it should be implemented elsewhere.
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers) || !Modifier.isAbstract(modifiers)) {
            throw new IllegalStateException(String.format("\"The annotation %1$s is misplaced on the method:\\n%3$s.%2$s(...).\\nPlease see the documentation of the annotation %1$s.",
                    annotation.getClass().getCanonicalName(), method.getName(), method.getDeclaringClass().getCanonicalName()));
        }

        name = method.getName();
        description = annotation.description();
        signature = "Not implemented";
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
