package nl.smith.mathematics.domain;

import nl.smith.mathematics.domain.StackElement.*;
import nl.smith.mathematics.mathematicalfunctions.MathematicalFunctionMethodMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ExpressionStack<N extends Number> {

    private static final String OUTPUT_FORMAT = "%-30s%s%s%n";

    private ExpressionStack<N> sibling;

    /**
     * Protected for test purposes.
     */
    protected final LinkedList<StackElement<?>> stackElements = new LinkedList<>();

    /**
     * Protected for test purposes.
     */
    protected enum State {
        INITIALIZED,
        APPENDABLE,
        CLOSED,
        DIGESTED
    }

    private State state = State.INITIALIZED;

    public State getState() {
        return state;
    }

    /**
     * An expression stack without siblings has a dimension of 1
     */
    public int getDimension() {
        if (sibling == null) {
            return 1;
        }

        return 1 + sibling.getDimension();
    }

    public ExpressionStack<N> getSibling() {
        return sibling;
    }

    public void setSibling(ExpressionStack<N> sibling) {
        if (getState() != State.CLOSED) {
            throw new IllegalStateException(format("Can not add an expression stack sibling to the current expression stack with state %s.%nThe state of the current expression stack should be %s.",
                    getState(),
                    State.CLOSED));
        }
        if (sibling.getState() != State.CLOSED) {
            throw new IllegalStateException(format("Can not add an expression stack sibling with state %s to the current expression stack.%nThe state of the sibling expression stack should be %s.",
                    sibling.getState(),
                    State.CLOSED));
        }
        this.sibling = sibling;
    }

    public int size() {
        return stackElements.size();
    }

    public ExpressionStack<N> addUnaryOperator(MathematicalFunctionMethodMapping<N> methodMapping) {
        return push(new UnaryOperatorStackElement<>(methodMapping));
    }

    public ExpressionStack<N> addBinaryOperator(MathematicalFunctionMethodMapping<N> methodMapping) {
        return push(new BinaryOperatorStackElement<N>(methodMapping));
    }

    public ExpressionStack<N> addVariableName(String variableName) {
        return push(new VariableNameStackElement(variableName));
    }

    public ExpressionStack<N> addFunctionName(MathematicalFunctionMethodMapping<N> methodMapping) {
        return push(new MathematicalFunctionStackElement<>(methodMapping));
    }

    public ExpressionStack<N> addNumber(N number) {
        return push(new NumberStackElement<>(number));
    }

    public ExpressionStack<N> addCompoundExpression(ExpressionStack<N> expressionStack) {
        if (expressionStack.getState() == State.INITIALIZED || expressionStack.getState() == State.APPENDABLE) {
            throw new IllegalStateException(format("Can not add an expression stack with state %s to another expression stack.", expressionStack.state));
        }

        return push(new CompoundExpressionStackElement<>(expressionStack));
    }

    private ExpressionStack<N> push(StackElement<?> stackElement) {
        if (getState() == State.CLOSED || getState() == State.DIGESTED) {
            throw new IllegalStateException(format("Stack elements can not be pushed to an expression stack when it is in state %s.", state));
        }
        Class<?> previousStackElementClass = stackElements.isEmpty() ? null : stackElements.peek().getClass();
        Class<?> stackElementClass = stackElement.getClass();

        if (isValidStackSequence(previousStackElementClass, stackElementClass)) {
            stackElements.push(stackElement);
        }

        state = State.APPENDABLE;

        return this;
    }

    public ExpressionStack<N> close() {
        if (getState() == State.APPENDABLE) {
            if (stackElements.isEmpty()) {
                throw new IllegalStateException(format("An expression stack with state %s should not be empty.", state));
            }
            Class<? extends StackElement> stackElementClass = stackElements.peek().getClass();
            if ((stackElementClass == UnaryOperatorStackElement.class || stackElementClass == BinaryOperatorStackElement.class)) {
                throw new IllegalStateException(format("Can not close an expression stack when the last element is of type %s.", stackElementClass.getSimpleName()));
            }
        } else {
            throw new IllegalStateException(format("Can not close an expression stack when it is in state %s.", state));
        }

        state = State.CLOSED;

        return this;
    }

    public ExpressionStack<N> digest(Map<String, N> variables) {
        if (getState() != State.CLOSED) {
            throw new IllegalStateException(format("Can not digest an expression stack when it is in state %s.", getState()));
        }

        return substituteVariables(variables).
                substituteUnaryOperators(variables);
    }

    private ExpressionStack<N> substituteVariables(Map<String, N> variables) {
        Set<String> unknownVariables = new TreeSet<>();
        for (int i = stackElements.size() - 1; i >= 0; i--) {
            StackElement<?> stackElement = stackElements.get(i);
            if (stackElement instanceof VariableNameStackElement) {
                String variableName = ((VariableNameStackElement) stackElement).getValue();
                N number = variables.get(variableName);
                if (number == null) {
                    unknownVariables.add(variableName);
                } else{
                    stackElements.set(i, new NumberStackElement<>(number));
                }

            }
        }

        if (!unknownVariables.isEmpty()) {
            throw new IllegalStateException(format("Unknown variable(s) '%s'.", unknownVariables.stream().collect(Collectors.joining("', '"))));
        }
        return this;
    }

    private ExpressionStack<N> substituteUnaryOperators(Map<String, N> variables) {
        ExpressionStack<N> digestedExpressionStack = new ExpressionStack<>();

        int i = stackElements.size();
        while (i > 0) {
            StackElement<?> stackElement = stackElements.get(--i);
            if (stackElement instanceof UnaryOperatorStackElement) {
                MathematicalFunctionMethodMapping<N> methodMapping = ((UnaryOperatorStackElement<N>) stackElement).getValue();
                stackElement = stackElements.get(--i);
                if (stackElement instanceof NumberStackElement) {
                    N number = ((NumberStackElement<N>) stackElement).getValue();
                    Object result = methodMapping.invokeWithNumbers(number);
                    if (result instanceof Number) {
                        digestedExpressionStack.addNumber((N) result);
                    } else {
                        throw new IllegalStateException("?");
                    }
                } else {
                    throw new IllegalStateException(format("Expected a stack element of type %s after a unary operatr but the type was was %s.",
                            NumberStackElement.class.getSimpleName(),
                            stackElement.getClass().getSimpleName()));
                }
            } else {
                digestedExpressionStack.push(stackElement);
            }
        }

        return digestedExpressionStack.close();
    }

    private static boolean isValidStackSequence(Class<?> previousStackElementClass, Class<?> stackElementClass) {
        if (previousStackElementClass == null || previousStackElementClass == BinaryOperatorStackElement.class) {
            return stackElementClass != BinaryOperatorStackElement.class;
        }

        if (previousStackElementClass == UnaryOperatorStackElement.class) {
            return stackElementClass != BinaryOperatorStackElement.class && stackElementClass != UnaryOperatorStackElement.class;
        }

        if (Arrays.asList(NumberStackElement.class, CompoundExpressionStackElement.class, VariableNameStackElement.class).contains(previousStackElementClass)) {
            return stackElementClass == BinaryOperatorStackElement.class;
        }

        if (previousStackElementClass == MathematicalFunctionStackElement.class) {
            return stackElementClass == CompoundExpressionStackElement.class;
        }

        return false;
    }

    public StringBuilder toStringBuilder(int level) {
        StringBuilder value = new StringBuilder();

        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("\t\t");
        }

        switch (getState()) {
            case INITIALIZED:
                value.append(format(OUTPUT_FORMAT, "", indent, "<Initialized stack>"));
                break;
            case APPENDABLE:
                value.append(format(OUTPUT_FORMAT, "", indent, "<Appendable stack>"));
                break;
            case CLOSED:
                value.append(format(OUTPUT_FORMAT, "", indent, "<Closed stack>"));
                break;
            case DIGESTED:
                value.append(format(OUTPUT_FORMAT, "", indent, "<Digested stack>"));
                break;
            default:
                throw new IllegalStateException(format("Unknown expression stack state %s.", state));
        }

        for (StackElement<?> stackElement : stackElements) {
            Object elementValue = stackElement.getValue();
            if (elementValue instanceof Method) {
                value.append(format(OUTPUT_FORMAT, stackElement.getClass().getSimpleName(), indent, ((Method) elementValue).getName()));
            } else if (elementValue instanceof ExpressionStack) {
                value.append(format(OUTPUT_FORMAT, stackElement.getClass().getSimpleName(), indent, "--- <Stack> ---"));
                value.append(((ExpressionStack<?>) elementValue).toStringBuilder(1));
                value.append(format(OUTPUT_FORMAT, stackElement.getClass().getSimpleName(), indent, "--- <Stack> ---"));


            } else {
                value.append(format(OUTPUT_FORMAT, stackElement.getClass().getSimpleName(), indent, stackElement.getValue()));
            }
        }

        return value;
    }

    @Override
    public String toString() {
        return toStringBuilder(0).toString();
    }

    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList();

        list.push("Mark");// 4
        list.push("Tom");// 3
        list.push("Frank");// 2
        list.push("Petra");// 1
        list.push("Smith");// 1
        list.push("Karel"); // 0
        System.out.println();
        list.forEach(System.out::println);
        System.out.println();

        int i = list.size() - 1;
        while (i >= 0) {
            if (list.get(i).equals("Petra")) {

            }
        }

    }

}
