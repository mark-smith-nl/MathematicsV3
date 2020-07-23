package nl.smith.mathematics.domain;

import nl.smith.mathematics.numbertype.RationalNumber;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StackElement<T> {

    private final T value;

    private final StackElementType stackElementType;

    public enum StackElementType {
        UNARY_OPERATOR(Character.class, false, false),
        BINARY_OPERATOR(Character.class, false, false),
        NUMBER(Number.class, true, true),
        COMPOUND_EXPRESSION(ExpressionStack.class, true, true),
        VARIABLE(String.class, true, true);

        private final Class<?> valueType;

        /**
         * Digestion of the StackElement value of this type results in a number
         */
        private final boolean isNumeric;

        /**
         * Flag to indicate that this could be the last element of an expression stack.
         */
        private final boolean isLastElementCandidate;

        StackElementType(Class<?> valueType, boolean isNumeric, boolean isLastElementCandidate) {
            this.valueType = valueType;
            this.isNumeric = isNumeric;
            this.isLastElementCandidate = isLastElementCandidate;
        }

        public Class<?> getValueType() {
            return valueType;
        }

        public boolean isNumeric() {
            return isNumeric;
        }

        public boolean isLastElementCandidate() {
            return isLastElementCandidate;
        }

        public static Set<StackElementType> getNumberTypes() {
            return Stream.of(StackElementType.values()).filter(t -> t.isNumeric).collect(Collectors.toSet());
        }
    }

    public StackElement(T value, StackElementType stackElementType) {
        if (value == null || stackElementType == null) {
            throw new IllegalStateException("Please specify a value and type for the stack element.");
        }

        if (!stackElementType.getValueType().isAssignableFrom(value.getClass())) {
            throw new IllegalStateException(String.format("A stack element of type %s(%s) can not have a value of type %s.", stackElementType, value.getClass()));
        }

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

