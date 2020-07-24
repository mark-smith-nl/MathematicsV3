package nl.smith.mathematics.domain;

import javax.validation.constraints.NotNull;

public class StackElement<EVT> {

    private final EVT value;

    public final StackElementType stackElementType;

    public enum StackElementType {
        UNARY_OPERATOR(false, false),
        BINARY_OPERATOR(false, false),
        NUMBER(true, true),
        COMPOUND_EXPRESSION(true, true),
        NAME(true, true);

        /**
         * Digestion of the StackElement value of this type results in a number
         */
        private final boolean isNumeric;

        /**
         * Flag to indicate that this could be the last element of an expression stack.
         */
        private final boolean lastElementCandidate;

        StackElementType(boolean isNumeric, boolean lastElementCandidate) {
            this.isNumeric = isNumeric;
            this.lastElementCandidate = lastElementCandidate;
        }

        public boolean isNumeric() {
            return isNumeric;
        }

        public boolean isLastElementCandidate() {
            return lastElementCandidate;
        }

    }

    private StackElement(EVT value, StackElementType stackElementType) {
        this.value = value;
        this.stackElementType = stackElementType;
    }

    public EVT getValue() {
        return value;
    }

    public StackElementType getStackElementType() {
        return stackElementType;
    }


    public static StackElement<?> createStackElement(@NotNull Object elementValue, @NotNull StackElement.StackElementType stackElementType) {
        switch (stackElementType) {
            case UNARY_OPERATOR:
                if (elementValue instanceof Character) {
                    return new StackElement<>((Character) elementValue, StackElement.StackElementType.UNARY_OPERATOR);
                }
                break;
            case NAME:
                if (elementValue instanceof String) {
                    return new StackElement<>((String) elementValue, StackElement.StackElementType.NAME);
                }
                break;
            case BINARY_OPERATOR:
                if (elementValue instanceof Character) {
                    return new StackElement<>((Character) elementValue, StackElement.StackElementType.BINARY_OPERATOR);
                }
                break;
            case COMPOUND_EXPRESSION:
                if (elementValue instanceof ExpressionStack) {
                    return new StackElement<>((ExpressionStack<?>) elementValue, StackElementType.COMPOUND_EXPRESSION);
                }
                break;
            case NUMBER:
                if (elementValue instanceof Number) {
                    throw new IllegalArgumentException("Wrong method: Please use createNumberStackElement(@NotNull N elementValue) instead of createStackElement(@NotNull Object elementValue, @NotNull StackElement.StackElementType stackElementType).");
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Can not create stack element(%s, %s).%nUnknown element type.", elementValue, stackElementType));
        }

        throw new IllegalArgumentException(String.format("Can not create stack element(%s, %s).%nElement type and element value do not match.", elementValue, stackElementType));
    }

    public static <N extends Number> StackElement<N> createNumberStackElement(@NotNull N elementValue) {
        return new StackElement<>(elementValue, StackElement.StackElementType.NUMBER);
    }

}

