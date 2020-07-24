package nl.smith.mathematics.domain;

import nl.smith.mathematics.numbertype.RationalNumber;

import java.util.LinkedList;

public class ExpressionStack<N extends Number> {

    private final LinkedList<StackElement<?>> stackElements = new LinkedList<>();

    public ExpressionStack(Object elementValue, StackElement.StackElementType stackElementType) {
        push(StackElement.createStackElement(elementValue, stackElementType));
    }

    public LinkedList<StackElement<?>> getStackElements() {
        return stackElements;
    }

    public ExpressionStack(N elementValue) {
        push(StackElement.createNumberStackElement(elementValue));
    }

    public ExpressionStack add(Object elementValue, StackElement.StackElementType stackElementType) {
        push(StackElement.createStackElement(elementValue, stackElementType));
        return this;
    }

    public ExpressionStack add(N elementValue) {
        push(StackElement.createNumberStackElement(elementValue));
        return this;
    }

    private void push(StackElement<?> stackElement) {
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

            switch (previousStackElementType) {
                case UNARY_OPERATOR:
                    if (!stackElementType.isNumeric()) {
                        throw new IllegalArgumentException("After a unary operator a number, compound expression or variable was expected.");
                    }
                    break;
                case NAME:
                    if (stackElementType != StackElement.StackElementType.BINARY_OPERATOR && stackElementType != StackElement.StackElementType.COMPOUND_EXPRESSION) {
                        throw new IllegalArgumentException("After a name a binary operator or a compound expression was expected.");
                    }
                    break;
                case NUMBER:
                case COMPOUND_EXPRESSION:
                    if (stackElementType != StackElement.StackElementType.BINARY_OPERATOR) {
                        throw new IllegalArgumentException("After a number of compound expression a binary operator was expected.");
                    }
                    break;
            }
        }

        stackElements.push(stackElement);
    }

    public boolean expectingStackElement() {
        if (stackElements.isEmpty()) {
            throw new IllegalStateException("Expression stack is empty");
        }

        return !stackElements.peek().getStackElementType().isLastElementCandidate();
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


    public static void main(String[] args) {
        new ExpressionStack<RationalNumber>(new RationalNumber(1, 5));
        System.out.println("Yes");
    }
}
