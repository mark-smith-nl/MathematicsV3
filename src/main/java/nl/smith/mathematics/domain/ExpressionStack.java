package nl.smith.mathematics.domain;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import static nl.smith.mathematics.domain.StackElement.StackElementType.*;

public class ExpressionStack<N extends Number> {

    private static final String OUTPUT_FORMAT = "%-30s%s%s%n";

    private ExpressionStack<N> sibling;

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

    /** An expression stack without siblings has a dimension of 1 */
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
        if (state != State.CLOSED) {
            throw new IllegalStateException(String.format("Can not add an expression stack sibling to the current expression stack with state %s.%nThe state of the current expression stack should be %s.",
                    state ,
                    State.CLOSED));
        }
        if (sibling.getState() != State.CLOSED) {
            throw new IllegalStateException(String.format("Can not add an expression stack sibling with state %s to the current expression stack.%nThe state of the sibling expression stack should be %s.",
                    sibling.getState() ,
                    State.CLOSED));
        }
        this.sibling = sibling;
    }

    private final LinkedList<StackElement<?>> stackElements = new LinkedList<>();

    public ExpressionStack<N> addUnaryOperator(Method method) {
        if (method == null) {
            throw new IllegalStateException("Method must be specified.");
        }

        if (method.getParameterCount() != 1) {
            throw new IllegalStateException("Method mapping to a unary operation should have 1 argument.");
        }

        return push(new StackElement<>(method, StackElement.StackElementType.UNARY_OPERATOR));
    }

    public ExpressionStack<N> addBinaryOperator(Method method) {
        if (method == null) {
            throw new IllegalStateException("Method must be specified.");
        }

        if (method.getParameterCount() != 2) {
            throw new IllegalStateException("Method mapping to a binary operation should have 2 arguments.");
        }

        return push(new StackElement<>(method, StackElement.StackElementType.BINARY_OPERATOR));
    }

    public ExpressionStack<N> addVariableName(String variableName) {
        if (variableName == null) {
            throw new IllegalStateException("****");
        }

        return push(new StackElement<>(variableName, StackElement.StackElementType.VARIABLE_NAME));
    }

    public ExpressionStack<N> addFunctionName(Method method) {
        if (method == null || method.getParameterCount() == 0) {
            throw new IllegalStateException("Method must be specified and a method mapping to a function operation should have arguments.");
        }

        return push(new StackElement<>(method, StackElement.StackElementType.FUNCTION_NAME));
    }

    public ExpressionStack<N> addNumber(N number) {
        if (number == null) {
            throw new IllegalStateException("****");
        }

        return push(new StackElement<>(number, StackElement.StackElementType.NUMBER));
    }

    public ExpressionStack<N> addCompoundExpression(ExpressionStack<N> expressionStack) {
        if (expressionStack.state == State.INITIALIZED || expressionStack.state == State.APPENDABLE) {
            throw new IllegalStateException(String.format("Can not add an expression stack with state %s to another expression stack.", expressionStack.state));
        }

        StackElement<ExpressionStack<N>> expressionStackStackElement = new StackElement<>(expressionStack, COMPOUND_EXPRESSION);
        return push(expressionStackStackElement);
    }

    private Set<ExpressionStack<?>> getCompoundExpressionStackElements() {
        return stackElements.stream()
                .filter(se -> se.getStackElementType() == COMPOUND_EXPRESSION)
                .map(se -> {
                    Object value = se.getValue();
                    if (value instanceof ExpressionStack) {
                        return (ExpressionStack<?>) value;
                    } else {
                        throw new IllegalStateException("");
                    }

                })
                .collect(Collectors.toSet());
    }

    private ExpressionStack<N> push(StackElement<?> stackElement) {
        if (state == State.CLOSED || state == State.DIGESTED) {
            throw new IllegalStateException(String.format("Stack elements can not be pushed to an expression stack when it is in state %s.", state));
        }

        StackElement.StackElementType previousStackElementType = stackElements.isEmpty() ? null : stackElements.peek().getStackElementType();
        StackElement.StackElementType stackElementType = stackElement.getStackElementType();

        if (isValidStackSequence(previousStackElementType, stackElementType)) {
            stackElements.push(stackElement);
        } else {
            throw new IllegalStateException("Illegal stack sequence");
        }

        state = State.APPENDABLE;

        return this;
    }

    public ExpressionStack<N> close() {
        if (state == State.APPENDABLE) {
            if (stackElements.isEmpty()) {
                throw new IllegalStateException(String.format("An expression stack with state %s should not be empty.", state));
            }
            StackElement.StackElementType stackElementType = stackElements.peek().getStackElementType();
            if ((stackElementType == UNARY_OPERATOR || stackElementType == BINARY_OPERATOR)) {
                throw new IllegalStateException("asd");
            }
        } else {
            throw new IllegalStateException(String.format("You can not close an expression stack when it is in state %s.", state));
        }

        state = State.CLOSED;

        return this;
    }

    private static boolean isValidStackSequence(StackElement.StackElementType previousStackElementType, StackElement.StackElementType stackElementType) {
        if (previousStackElementType == null) {
            return Arrays.asList(UNARY_OPERATOR, NUMBER, COMPOUND_EXPRESSION, VARIABLE_NAME, FUNCTION_NAME).contains(stackElementType);
        }

        switch (previousStackElementType) {
            case UNARY_OPERATOR:
                return Arrays.asList(NUMBER, COMPOUND_EXPRESSION, VARIABLE_NAME, FUNCTION_NAME).contains(stackElementType);
            case NUMBER:
            case COMPOUND_EXPRESSION:
            case VARIABLE_NAME:
                return BINARY_OPERATOR == stackElementType;
            case FUNCTION_NAME:
                return COMPOUND_EXPRESSION == stackElementType;
            case BINARY_OPERATOR:
                return COMPOUND_EXPRESSION != stackElementType;
            default:
                throw new IllegalStateException(String.format("Unknown enum value: %s", previousStackElementType));
        }
    }

    public StringBuilder toString(int level) {
        StringBuilder value = new StringBuilder();

        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("\t\t");
        }

        switch (state) {
            case INITIALIZED:
                value.append(String.format(OUTPUT_FORMAT, "", indent, "<Initialized stack>"));
                break;
            case APPENDABLE:
                value.append(String.format(OUTPUT_FORMAT, "", indent, "<Appendable stack>"));
                break;
            case CLOSED:
                value.append(String.format(OUTPUT_FORMAT, "", indent, "<Closed stack>"));
                break;
            case DIGESTED:
                value.append(String.format(OUTPUT_FORMAT, "", indent, "<Digested stack>"));
                break;
            default:
                throw new IllegalStateException(String.format("Unknown expression stack state %s.", state));
        }

        for (StackElement<?> stackElement : stackElements) {
            Object elementValue = stackElement.getValue();
            if (elementValue instanceof Method) {
                value.append(String.format(OUTPUT_FORMAT, stackElement.getStackElementType(), indent, ((Method) elementValue).getName()));
            } else if (elementValue instanceof ExpressionStack) {
                value.append(String.format(OUTPUT_FORMAT, stackElement.getStackElementType(), indent, "--- <Stack> ---"));
                value.append(((ExpressionStack<?>) elementValue).toString(1));
                value.append(String.format(OUTPUT_FORMAT, stackElement.getStackElementType(), indent, "--- <Stack> ---"));


            } else {
                value.append(String.format(OUTPUT_FORMAT, stackElement.getStackElementType(), indent, stackElement.getValue()));
            }
        }

        return value;
    }

    @Override
    public String toString() {
        return toString(0).toString();
    }
}
