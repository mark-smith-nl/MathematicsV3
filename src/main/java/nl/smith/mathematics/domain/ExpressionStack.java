package nl.smith.mathematics.domain;

import java.util.LinkedList;

public class ExpressionStack {

    private final LinkedList<StackElement<?>> stackElements = new LinkedList<>();

    public ExpressionStack(StackElement<?> stackElement) {
        push(stackElement);
    }

    public ExpressionStack push(StackElement<?> stackElement) {
        if (stackElement == null) {
            throw new IllegalArgumentException("Please specify a stack element.");
        }

        if (stackElements.isEmpty()) {
            if (stackElement.getStackElementType() == StackElement.StackElementType.BINARY_OPERATOR) {
                throw new IllegalArgumentException("You can not start an expression stack with a binary operator.");
            }
        } else {
            StackElement.StackElementType previousStackElementType = stackElements.peek().getStackElementType();
            StackElement.StackElementType stackElementType = stackElement.getStackElementType();

            if (previousStackElementType == StackElement.StackElementType.UNARY_OPERATOR && !stackElementType.isNumeric()) {
                throw new IllegalArgumentException("After a unary operator an element was expected that resolves into a number.");
            } else if (previousStackElementType == StackElement.StackElementType.FUNCTION && stackElementType != StackElement.StackElementType.COMPOUND_EXPRESSION) {
                throw new IllegalArgumentException("After a function(name) a compound expression was expected.");
            } else if (previousStackElementType.isNumeric() && stackElementType != StackElement.StackElementType.BINARY_OPERATOR) {
                throw new IllegalArgumentException("After an elements that resolves to a number a binary operator was expected.");
            } else if (previousStackElementType == StackElement.StackElementType.BINARY_OPERATOR && stackElementType == StackElement.StackElementType.BINARY_OPERATOR) {
                throw new IllegalArgumentException("After a binary element another binary operator was not expected.");
            }
        }

        stackElements.push(stackElement);

        return this;
    }

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();

        if (stackElements.isEmpty()) {
            throw new IllegalStateException("Expression stack is empty");
        }

        if (!stackElements.peek().getStackElementType().isLastElementCandidate()) {
            value.append("<Expecting more elements>").append("\n");
        }

        for (StackElement<?> stackElement : stackElements) {
            value.append(String.format("%-20s%s%n", stackElement.getStackElementType(), stackElement.getValue()));
        }

        return value.toString();
    }

}
