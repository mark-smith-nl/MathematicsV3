package nl.smith.mathematics.domain;

import nl.smith.mathematics.annotation.MathematicalFunction.Type;

import static java.lang.String.format;

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

    public abstract boolean canBeLastStackElement();

    public T getValue() {
        return value;
    }

    public static abstract class MethodMappingStackElement<N extends Number> extends StackElement<N, MathematicalFunctionMethodMapping<N>> {

        public MethodMappingStackElement(MathematicalFunctionMethodMapping<N> value) {
            super(value);
        }

        @Override
        protected void assertIsValid(MathematicalFunctionMethodMapping<N> value) {
            if (value == null) {
                throw new IllegalStateException(format("A method must be specified when instantiating %s.", this.getClass().getSimpleName()));
            }

            if (value.getType() != getRequiredType()) {
                throw new IllegalStateException(format("A %s must wrap a %s of type %s. Type specified %s.", this.getClass().getSimpleName(), MathematicalFunctionMethodMapping.class.getSimpleName(), getRequiredType(), value.getType()));
            }
        }

        protected abstract Type getRequiredType();

        @Override
        public boolean canBeLastStackElement() {
            return false;
        }
    }

    public static class UnaryOperatorStackElement<N extends Number> extends MethodMappingStackElement<N> {

        public UnaryOperatorStackElement(MathematicalFunctionMethodMapping<N> value) {
            super(value);
        }

        protected Type getRequiredType() {
            return Type.UNARY_OPERATION;
        }

        @Override
        public String toString() {
            return getValue().getName();
        }
    }

    public static class BinaryOperatorStackElement<N extends Number> extends MethodMappingStackElement<N> {

        public BinaryOperatorStackElement(MathematicalFunctionMethodMapping<N> value) {
            super(value);
        }

        protected Type getRequiredType() {
            return Type.BINARY_OPERATION;
        }

        @Override
        public String toString() {
            return getValue().getName();
        }
    }

    public static class HighPriorityBinaryOperatorStackElement<N extends Number> extends BinaryOperatorStackElement<N> {

        public HighPriorityBinaryOperatorStackElement(MathematicalFunctionMethodMapping<N> value) {
            super(value);
        }

        protected Type getRequiredType() {
            return Type.HIGH_PRIORITY_BINARY_OPERATION;
        }
    }

    public static class MathematicalFunctionStackElement<N extends Number> extends MethodMappingStackElement<N> {

        public MathematicalFunctionStackElement(MathematicalFunctionMethodMapping<N> value) {
            super(value);
        }

        @Override
        protected Type getRequiredType() {
            return Type.FUNCTION;
        }

        @Override
        public String toString() {
            return getValue().getName();
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

        @Override
        public boolean canBeLastStackElement() {
            return true;
        }

        @Override
        public String toString() {
            return getValue();
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

        @Override
        public boolean canBeLastStackElement() {
            return true;
        }

        @Override
        public String toString() {
            return getValue().toString();
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

        @Override
        public boolean canBeLastStackElement() {
            return true;
        }

        @Override
        public String toString() {
            return String.valueOf(getValue().size());
        }
    }

}

