package nl.smith.mathematics.domain;

public abstract class StackElement<T> {

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

    public static class UnaryOperatorStackElement<N extends Number> extends StackElement<MathematicalFunctionMethodMapping<N>> {

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

    public static class BinaryOperatorStackElement<N extends Number> extends StackElement<MathematicalFunctionMethodMapping<N>> {

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

    public static class MathematicalFunctionStackElement<N extends Number> extends StackElement<MathematicalFunctionMethodMapping<N>> {

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

    public static class VariableNameStackElement extends StackElement<String> {

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

    public static class NumberStackElement<N extends Number> extends StackElement<N> {

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

    public static class CompoundExpressionStackElement<N extends Number> extends StackElement<ExpressionStack<N>> {

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

