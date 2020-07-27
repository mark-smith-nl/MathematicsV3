package nl.smith.mathematics.domain;

public class StackElement<T> {

    private final T value;

    public final StackElementType stackElementType;

    public enum StackElementType {
        UNARY_OPERATOR,
        NUMBER,
        COMPOUND_EXPRESSION,
        VARIABLE_NAME,
        FUNCTION_NAME,
        BINARY_OPERATOR
    }

    public StackElement(T value, StackElementType stackElementType) {
        this.value = value;
        this.stackElementType = stackElementType;
    }

    public T getValue() {
        return value;
    }

    public StackElementType getStackElementType() {
        return stackElementType;
    }

}

