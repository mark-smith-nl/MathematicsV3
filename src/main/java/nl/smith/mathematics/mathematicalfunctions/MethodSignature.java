package nl.smith.mathematics.mathematicalfunctions;

import nl.smith.mathematics.annotation.MathematicalFunction;

import java.util.Objects;

public class MethodSignature {

    private final MathematicalFunction annotation;

    private final String name;

    private final int argumentCount;

    public MethodSignature(MathematicalFunction annotation, String name, int argumentCount) {
        this.annotation = annotation;
        this.name = name;
        this.argumentCount = argumentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodSignature methodSignature = (MethodSignature) o;
        return argumentCount == methodSignature.argumentCount &&
                Objects.equals(name, methodSignature.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, argumentCount);
    }

    @Override
    public String toString() {
        String arguments = "number";
        if (argumentCount == 0) {
            arguments += "...";
        } else {
            for (int i=1; i<argumentCount; i++) {
                arguments += ", number";
            }
        }
        return String.format("%s\n%s(%s)", annotation.description(), name, arguments);
    }
}
