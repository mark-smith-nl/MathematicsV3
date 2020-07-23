package nl.smith.mathematics.domain;


import nl.smith.mathematics.numbertype.RationalNumber;

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

        if (!stackElements.peek().getStackElementType().isLastElementCandidate()) {
            value.append("<Expecting more elements>").append("\n");
        }

        for (StackElement<?> stackElement : stackElements) {
            value.append(String.format("%-20s%s%n", stackElement.getStackElementType(), stackElement.getValue()));
        }

        return value.toString();
    }

    public static void main(String[] args) {
        ExpressionStack expressionStack = new ExpressionStack(new StackElement<Character>('-', StackElement.StackElementType.UNARY_OPERATOR));
        System.out.print(expressionStack
                .push(new StackElement<RationalNumber>(new RationalNumber(1, 7), StackElement.StackElementType.NUMBER))
                .push(new StackElement<Character>('*', StackElement.StackElementType.BINARY_OPERATOR))
                .push(new StackElement<Character>('-', StackElement.StackElementType.UNARY_OPERATOR))
                .push(new StackElement<String>("Speed", StackElement.StackElementType.VARIABLE))
                .push(new StackElement<Character>('s', StackElement.StackElementType.VARIABLE)));
        System.out.println(String.format("Mijn naam is %-10s. Ik woon in Geldermalsen.", "Mark"));
    }
}
