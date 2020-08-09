package nl.smith.mathematics.domain;

import nl.smith.mathematics.annotation.MathematicalFunction.Type;
import nl.smith.mathematics.domain.StackElement.*;

import java.util.*;

import static java.lang.String.format;
import static nl.smith.mathematics.domain.ExpressionStack.State.*;

public class ExpressionStack<N extends Number> {

    private static final String OUTPUT_FORMAT = "%-45s%s%s%n";

    private ExpressionStack<N> sibling;

    /**
     * Protected for test purposes.
     */
    protected final LinkedList<StackElement<N, ?>> stackElements = new LinkedList<>();

    /**
     * Protected for test purposes.
     */
    protected enum State {
        DIGESTED("<Digested stack>",
                null,
                NumberStackElement.class),
        DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS("<Digested stack - High priority binary operators>",
                DIGESTED,
                BinaryOperatorStackElement.class,
                NumberStackElement.class),
        DIGESTED_UNARY_OPERATORS("<Digested stack - Unary operators>",
                DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS,
                HighPriorityBinaryOperatorStackElement.class,
                BinaryOperatorStackElement.class,
                NumberStackElement.class),
        DIGESTED_VARIABLES("<Digested stack - Variables>",
                DIGESTED_UNARY_OPERATORS,
                UnaryOperatorStackElement.class,
                HighPriorityBinaryOperatorStackElement.class,
                BinaryOperatorStackElement.class,
                NumberStackElement.class),
        DIGESTED_MATHEMATICAL_FUNCTIONS("<Digested stack - Mathematical functions>",
                DIGESTED_VARIABLES,
                VariableNameStackElement.class,
                UnaryOperatorStackElement.class,
                HighPriorityBinaryOperatorStackElement.class,
                BinaryOperatorStackElement.class,
                NumberStackElement.class),
        DIGESTED_COMPOUND_EXPRESSIONS("<Digested stack - Compound expressions>",
                DIGESTED_MATHEMATICAL_FUNCTIONS,
                MathematicalFunctionStackElement.class,
                VariableNameStackElement.class,
                UnaryOperatorStackElement.class,
                HighPriorityBinaryOperatorStackElement.class,
                BinaryOperatorStackElement.class,
                NumberStackElement.class),
        CLOSED("<Closed stack>",
                DIGESTED_COMPOUND_EXPRESSIONS,
                CompoundExpressionStackElement.class,
                MathematicalFunctionStackElement.class,
                VariableNameStackElement.class,
                UnaryOperatorStackElement.class,
                HighPriorityBinaryOperatorStackElement.class,
                BinaryOperatorStackElement.class,
                NumberStackElement.class),
        APPENDABLE("<Appendable stack>",
                CLOSED,
                CompoundExpressionStackElement.class,
                MathematicalFunctionStackElement.class,
                VariableNameStackElement.class,
                UnaryOperatorStackElement.class,
                HighPriorityBinaryOperatorStackElement.class,
                BinaryOperatorStackElement.class,
                NumberStackElement.class),
        INITIALIZED("<Initialized stack>",
                APPENDABLE);

        private final String description;

        private final State nextState;

        private final Set<Class<? extends StackElement>> allowedStackElementTypes = new HashSet<>();

        @SafeVarargs
        State(String description, State nextState, Class<? extends StackElement> ... allowedStackElementTypes) {
            this.description = description;
            this.nextState = nextState;
            this.allowedStackElementTypes.addAll(Arrays.asList(allowedStackElementTypes));
        }

        public String getDescription() {
            return description;
        }

        public State getNextState() {
            return nextState;
        }

        public Set<Class<? extends StackElement>> getAllowedStackElementTypes() {
            return allowedStackElementTypes;
        }
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

    public ExpressionStack<N> addNumber(N number) {
        return push(new NumberStackElement<>(number));
    }

    public ExpressionStack<N> addCompoundExpression(ExpressionStack<N> expressionStack) {
        if (expressionStack.getState() != State.CLOSED) {
            throw new IllegalStateException(format("Can not add an expression stack with state %s to another expression stack.", expressionStack.getState()));
        }

        return push(new CompoundExpressionStackElement<>(expressionStack));
    }

    public ExpressionStack<N> addMathematicalFunction(MathematicalFunctionMethodMapping<N> methodMapping) {
        return push(new MathematicalFunctionStackElement<>(methodMapping));
    }

    public ExpressionStack<N> addVariableName(String variableName) {
        return push(new VariableNameStackElement<>(variableName));
    }

    public ExpressionStack<N> addUnaryOperator(MathematicalFunctionMethodMapping<N> methodMapping) {
        return push(new UnaryOperatorStackElement<>(methodMapping));
    }

    public ExpressionStack<N> addBinaryOperator(MathematicalFunctionMethodMapping<N> methodMapping) {
        return push(new BinaryOperatorStackElement<>(methodMapping));
    }

    public ExpressionStack<N> addHighPriorityBinaryOperator(MathematicalFunctionMethodMapping<N> methodMapping) {
        return push(new HighPriorityBinaryOperatorStackElement<>(methodMapping));
    }

    private ExpressionStack<N> push(StackElement<N, ?> stackElement) {
        if (!(getState() == State.INITIALIZED || getState() == State.APPENDABLE)) {
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

            StackElement<N, ?> stackElement = stackElements.peek();
            if ((!stackElement.canBeLastStackElement())) {
                throw new IllegalStateException(format("Can not close an expression stack when the last element is of type %s.", stackElement.getClass().getSimpleName()));
            }
        } else {
            throw new IllegalStateException(format("Can not close an expression stack when it is in state %s.", state));
        }

        state = State.CLOSED;

        return this;
    }

    /**
     * Protected for test purposes.
     */
    protected ExpressionStack<N> closeWithState(State requestedState) {
        if (!Arrays.asList(CLOSED,
                DIGESTED_COMPOUND_EXPRESSIONS,
                DIGESTED_MATHEMATICAL_FUNCTIONS,
                DIGESTED_VARIABLES,
                DIGESTED_UNARY_OPERATORS,
                DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS,
                DIGESTED)
                .contains(requestedState)) {
            throw new IllegalArgumentException(format("Can not close expression stack and change state of expression stack from current state %s to requested state %s.", getState(), requestedState));
        }

        close();

        Set<Class<? extends StackElement>> allowedStackElementTypes = requestedState.getAllowedStackElementTypes();
        for (StackElement<N, ?> stackElement : stackElements) {
            if (!allowedStackElementTypes.contains(stackElement.getClass())) {
                throw new IllegalArgumentException(format("Can not change state of expression stack from %s to %s. Stack contains element of type %s which is not allowed in the desired state.", getState(), requestedState, stackElement.getClass().getSimpleName()));
            }
        }
        this.state = requestedState;
        return this;
    }

    /**
     * <pre>
     * The order in which a stack is digested is:
     *
     *     - substitute compound expressions(the stack size does not change, elements of type {@link CompoundExpressionStackElement} are being replaced by {@link NumberStackElement}
     *     - substitute functions (the stack size diminishes by the number of {@link MathematicalFunctionStackElement} elements)
     *     - substitute variables (the stack size does not change, elements of type {@link VariableNameStackElement} are being replaced by {@link NumberStackElement}
     *     - substitute unary operators (the stack size diminishes by the number of {@link UnaryOperatorStackElement} elements)
     *     - substitute high priority binary operators (the stack size diminishes by two times the number of {@link BinaryOperatorStackElement} of type {@link Type#HIGH_PRIORITY_BINARY_OPERATION} elements)
     *     - substitute binary operators (the stack size diminishes by two times the number of {@link BinaryOperatorStackElement} of type {@link Type#BINARY_OPERATION} elements)
     *
     * After the last step the stack should contain one element of type {@link StackElement.NumberStackElement}
     *
     * </pre>
     *
     * @param variables Map of variable names and values
     * @return An expression stack with one element of type {@link NumberStackElement}
     */
    public ExpressionStack<N> digest(Map<String, N> variables) {
        if (getState() != State.CLOSED) {
            throw new IllegalStateException(format("Can not digest an expression stack when it is in state %s.", getState()));
        }

        ExpressionStack<N> digestedExpressionStack = digestCompoundExpressions(variables)
                .digestMathematicalFunctions()
                .digestVariables(variables)
                .digestUnaryOperators()
                .digestHighPriorityBinaryOperators()
                .digestBinaryOperators();

        if (sibling != null) {
            digestedExpressionStack.setSibling(sibling.digest(variables));
        }

        return digestedExpressionStack;
    }

    /**
     * Protected for test purposes.
     */
    protected ExpressionStack<N> digestCompoundExpressions(Map<String, N> variables) {
        State requestedState = DIGESTED_COMPOUND_EXPRESSIONS;
        if (getState().getNextState() != requestedState) {
            throw new IllegalStateException(format("Can not digest (substitute compound expressions) an expression stack when it is in state %s.", getState()));
        }

        ExpressionStack<N> digestedExpressionStack = new ExpressionStack<>();

        for (int i = stackElements.size() - 1; i >= 0; i--) {
            StackElement<N, ?> stackElement = stackElements.get(i);
            if (stackElement instanceof CompoundExpressionStackElement) {
                CompoundExpressionStackElement<N> compoundExpressionStackElement = (CompoundExpressionStackElement<N>) stackElement;
                ExpressionStack<N> subExpressionStack = compoundExpressionStackElement.getValue();
                ExpressionStack<N> digestedSubExpressionStack = subExpressionStack.digest(variables);
                if (digestedSubExpressionStack.stackElements.size() == 1) {
                    StackElement<N, ?> resultStackElement = digestedSubExpressionStack.stackElements.get(0);
                    if (resultStackElement instanceof NumberStackElement) {
                        NumberStackElement<N> numberStackElement = (NumberStackElement<N>) resultStackElement;
                        digestedExpressionStack.addNumber(numberStackElement.getValue());
                    } else {
                        throw new IllegalStateException("Wrong type");
                    }
                } else {
                    throw new IllegalStateException("Wrong size");
                }
            } else {
                digestedExpressionStack.push(stackElement);
            }
        }

        return digestedExpressionStack.closeWithState(requestedState);
    }

    /**
     * Protected for test purposes.
     */
    protected ExpressionStack<N> digestMathematicalFunctions() {
        State requestedState = DIGESTED_MATHEMATICAL_FUNCTIONS;
        if (getState().getNextState() != requestedState) {
            throw new IllegalStateException(format("Can not digest (substitute mathematical functions) an expression stack when it is in state %s.", getState()));
        }

        ExpressionStack<N> digestedExpressionStack = new ExpressionStack<>();

        int i = stackElements.size();
        while (i > 0) {
            StackElement<N, ?> stackElement = stackElements.get(--i);
            if (stackElement instanceof MathematicalFunctionStackElement) {
                MathematicalFunctionStackElement<N> mathematicalFunctionStackElement = (MathematicalFunctionStackElement) stackElement;
                MathematicalFunctionMethodMapping<N> methodMapping = mathematicalFunctionStackElement.getValue();
                stackElement = stackElements.get(--i);
                if (stackElement instanceof NumberStackElement) {
                    NumberStackElement<N> numberStackElement = (NumberStackElement<N>) stackElement;
                    N number = numberStackElement.getValue();
                    N result = methodMapping.invokeWithNumbers(number);
                    digestedExpressionStack.addNumber(result);
                } else {
                    throw new IllegalStateException(format("Expected a stack element of type %s after a unary operator but the type was was %s.",
                            NumberStackElement.class.getSimpleName(),
                            stackElement.getClass().getSimpleName()));
                }
            } else {
                digestedExpressionStack.push(stackElement);
            }
        }

        return digestedExpressionStack.closeWithState(requestedState);
    }

    /**
     * Protected for test purposes.
     */
    protected ExpressionStack<N> digestVariables(Map<String, N> variables) {
        State requestedState = DIGESTED_VARIABLES;
        if (getState().getNextState() != requestedState) {
            throw new IllegalStateException(format("Can not digest (substitute variables) an expression stack when it is in state %s.", getState()));
        }

        ExpressionStack<N> digestedExpressionStack = new ExpressionStack<>();

        Set<String> unknownVariables = new TreeSet<>();
        for (int i = stackElements.size() - 1; i >= 0; i--) {
            StackElement<N, ?> stackElement = stackElements.get(i);
            if (stackElement instanceof VariableNameStackElement) {
                VariableNameStackElement<N> variableNameStackElement = (VariableNameStackElement<N>) stackElement;
                String variableName = variableNameStackElement.getValue();
                N number = variables.get(variableName);
                if (number == null) {
                    unknownVariables.add(variableName);
                } else {
                    digestedExpressionStack.addNumber(number);
                }
            } else {
                digestedExpressionStack.push(stackElement);
            }
        }

        if (!unknownVariables.isEmpty()) {
            throw new IllegalStateException(format("Unknown variable(s) '%s'.", String.join("', '", unknownVariables)));
        }

        return digestedExpressionStack.closeWithState(requestedState);
    }

    /**
     * Protected for test purposes.
     */
    protected ExpressionStack<N> digestUnaryOperators() {
        State requestedState = DIGESTED_UNARY_OPERATORS;
        if (getState().getNextState() != requestedState) {
            throw new IllegalStateException(format("Can not digest (substitute unary operators) an expression stack when it is in state %s.", getState()));
        }

        ExpressionStack<N> digestedExpressionStack = new ExpressionStack<>();

        int i = stackElements.size();
        while (i > 0) {
            StackElement<N, ?> stackElement = stackElements.get(--i);
            if (stackElement instanceof UnaryOperatorStackElement) {
                UnaryOperatorStackElement<N> unaryOperatorStackElement = (UnaryOperatorStackElement) stackElement;
                MathematicalFunctionMethodMapping<N> methodMapping = unaryOperatorStackElement.getValue();
                stackElement = stackElements.get(--i);
                if (stackElement instanceof NumberStackElement) {
                    NumberStackElement<N> numberStackElement = (NumberStackElement<N>) stackElement;
                    N number = numberStackElement.getValue();
                    N result = methodMapping.invokeWithNumbers(number);
                    digestedExpressionStack.addNumber(result);
                } else {
                    throw new IllegalStateException(format("Expected a stack element of type %s after a unary operator but the type was was %s.",
                            NumberStackElement.class.getSimpleName(),
                            stackElement.getClass().getSimpleName()));
                }
            } else {
                digestedExpressionStack.push(stackElement);
            }
        }

        return digestedExpressionStack.closeWithState(requestedState);
    }

    /**
     * Protected for test purposes.
     */
    protected ExpressionStack<N> digestHighPriorityBinaryOperators() {
        return digestBinaryOperators(true);
    }

    /**
     * Protected for test purposes.
     */
    protected ExpressionStack<N> digestBinaryOperators() {
        return digestBinaryOperators(false);
    }

    /**
     * Protected for test purposes.
     */
    private ExpressionStack<N> digestBinaryOperators(boolean highPriority) {
        State requestedState = highPriority ? DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS : DIGESTED;
        if (getState().getNextState() != requestedState) {
            throw new IllegalStateException(format("Can not digest (substitute high priority binary operators) an expression stack when it is in state %s.", getState()));
        }

        ExpressionStack<N> digestedExpressionStack = new ExpressionStack<>();

        int i = stackElements.size();
        NumberStackElement<N> firstNumberStackElement = null;
        NumberStackElement<N> secondNumberStackElement;
        BinaryOperatorStackElement<N> binaryOperatorStackElement = null;

        while (i > 0) {
            StackElement<N, ?> stackElement = stackElements.get(--i);
            if (stackElement instanceof NumberStackElement) {
                if (firstNumberStackElement == null) {
                    firstNumberStackElement = (NumberStackElement<N>) stackElement;
                } else {
                    secondNumberStackElement = (NumberStackElement<N>) stackElement;
                    N result = binaryOperatorStackElement.getValue().invokeWithNumbers(firstNumberStackElement.getValue(), secondNumberStackElement.getValue());
                    firstNumberStackElement = new NumberStackElement<>(result);
                }

            } else if (highPriority) {
                if (stackElement instanceof HighPriorityBinaryOperatorStackElement) {
                    binaryOperatorStackElement = (BinaryOperatorStackElement) stackElement;
                } else {
                    digestedExpressionStack.push(firstNumberStackElement);
                    digestedExpressionStack.push(stackElement); // Push the low priority binary operator on the stack
                    firstNumberStackElement = null;
                }
            } else {
                if (stackElement instanceof BinaryOperatorStackElement) {
                    binaryOperatorStackElement = (BinaryOperatorStackElement) stackElement;
                }
            }
        }

        if (firstNumberStackElement != null) {
            digestedExpressionStack.push(firstNumberStackElement);
        }

        return digestedExpressionStack.closeWithState(requestedState);
    }

    private static boolean isValidStackSequence(Class<?> previousStackElementClass, Class<?> stackElementClass) {
        if (previousStackElementClass == null) {
            return new HashSet<>(Arrays.asList(
                    UnaryOperatorStackElement.class,
                    NumberStackElement.class,
                    MathematicalFunctionStackElement.class,
                    VariableNameStackElement.class,
                    CompoundExpressionStackElement.class)).contains(stackElementClass);
        }

        if (previousStackElementClass == UnaryOperatorStackElement.class) {
            return new HashSet<>(Arrays.asList(
                    NumberStackElement.class,
                    MathematicalFunctionStackElement.class,
                    VariableNameStackElement.class,
                    CompoundExpressionStackElement.class)).contains(stackElementClass);
        }

        if (new HashSet<>(Arrays.asList(
                NumberStackElement.class,
                VariableNameStackElement.class,
                CompoundExpressionStackElement.class)).contains(previousStackElementClass)) {
            return BinaryOperatorStackElement.class.isAssignableFrom(stackElementClass);
        }

        if (previousStackElementClass == MathematicalFunctionStackElement.class) {
            return stackElementClass == CompoundExpressionStackElement.class;
        }

        if (BinaryOperatorStackElement.class.isAssignableFrom(previousStackElementClass)) {
            return new HashSet<>(Arrays.asList(
                    UnaryOperatorStackElement.class,
                    NumberStackElement.class,
                    MathematicalFunctionStackElement.class,
                    VariableNameStackElement.class,
                    CompoundExpressionStackElement.class)).contains(stackElementClass);
        }

        return false;
    }

    public StringBuilder toStringBuilder(int level) {
        StringBuilder value = new StringBuilder();

        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("\t\t");
        }

        value.append(format(OUTPUT_FORMAT, "Size: " + size(), indent, state.getDescription()));

        for (StackElement<N, ?> stackElement : stackElements) {
            Object elementValue = stackElement.getValue();
            if (elementValue instanceof ExpressionStack) {
                value.append(format(OUTPUT_FORMAT, stackElement.getClass().getSimpleName(), indent, "--- <Stack> ---"));
                value.append(((ExpressionStack<?>) elementValue).toStringBuilder(1));
                value.append(format(OUTPUT_FORMAT, stackElement.getClass().getSimpleName(), indent, "--- <Stack> ---"));
            } else {
                value.append(format(OUTPUT_FORMAT, stackElement.getClass().getSimpleName(), indent, stackElement.toString()));
            }
        }

        return value;
    }

    @Override
    public String toString() {
        return toStringBuilder(0).toString();
    }

}
