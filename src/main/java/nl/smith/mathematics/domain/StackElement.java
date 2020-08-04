package nl.smith.mathematics.domain;

import nl.smith.mathematics.numbertype.RationalNumber;

/** T
 * <pre>
 *
 * The element in a stack can be one of the following types and is an instance of a subclass of {@link StackElement}
 *
 *     - Unary operator {@link UnaryOperatorStackElement}
 *     - Binary operator {@link BinaryOperatorStackElement}
 *     - Variable {@link VariableNameStackElement}
 *     - Compound expression {@link CompoundExpressionStackElement}
 *     - Number {@link NumberStackElement}
 *
 *     In the construction of a stack element the number type has to be specified.
 *     The type the stack element contains is specified in the subclass declaration.
 *
 * </pre>
 *
 * @param <N> The number type
 * @param <T> The value type
 */
public abstract class StackElement<N, T> {

    private final T value;

    protected abstract void assertIsValid(T value);

    public StackElement(T value) {
        this.value = getValidValue(value);
    }

    private T getValidValue(T value) {
        assertIsValid(value);

        return value;
    }

    public T getValue() {
        return value;
    }

    public static class UnaryOperatorStackElement<N extends Number> extends StackElement<N, MathematicalFunctionMethodMapping<N>> {

        public UnaryOperatorStackElement(MathematicalFunctionMethodMapping<N> value) {
            super(value);
        }

        @Override
        protected void assertIsValid(MathematicalFunctionMethodMapping<N> value) {
            if (value == null) {
                throw new IllegalStateException("A method must be specified.");
            }

            if (value.getParameterCount() != 1) {
                throw new IllegalStateException("Method mapping to a unary operation should have 1 argument.");
            }
        }
    }

    public static class BinaryOperatorStackElement<N extends Number> extends StackElement<N, MathematicalFunctionMethodMapping<N>> {

        public BinaryOperatorStackElement(MathematicalFunctionMethodMapping<N> value) {
            super(value);
        }

        @Override
        protected void assertIsValid(MathematicalFunctionMethodMapping<N> value) {
            if (value == null) {
                throw new IllegalStateException("A method must be specified.");
            }

            if (value.getParameterCount() != 2) {
                throw new IllegalStateException("Method mapping to a binary operation should have 2 arguments.");
            }
        }
    }

    public static class MathematicalFunctionStackElement<N extends Number> extends StackElement<N, MathematicalFunctionMethodMapping<N>> {

        public MathematicalFunctionStackElement(MathematicalFunctionMethodMapping<N> value) {
            super(value);
        }

        @Override
        protected void assertIsValid(MathematicalFunctionMethodMapping<N> value) {
            if (value == null) {
                throw new IllegalStateException("E");
            }

            if (value.getParameterCount() < 1) {
                throw new IllegalStateException("F");
            }
        }
    }

    public static class VariableNameStackElement<N extends Number> extends StackElement<N, String> {

        public VariableNameStackElement(String value) {
            super(value);
        }

        @Override
        protected void assertIsValid(String value) {
            if (value == null) {
                throw new IllegalStateException("G");
            }

            //TODO
        }
    }

    public static class NumberStackElement<N extends Number> extends StackElement<N, N> {

        public NumberStackElement(N value) {
            super(value);
        }

        @Override
        protected void assertIsValid(N value) {
            if (value == null) {
                throw new IllegalStateException("G");
            }
        }
    }

    public static class CompoundExpressionStackElement<N extends Number> extends StackElement<N, ExpressionStack<N>> {

        public CompoundExpressionStackElement(ExpressionStack<N> value) {
            super(value);
        }

        @Override
        protected void assertIsValid(ExpressionStack<N> value) {
            if (value == null) {
                throw new IllegalStateException("I");
            }

            if (value.getState() != ExpressionStack.State.CLOSED){
                throw new IllegalStateException("J");
            }
        }
    }

}

