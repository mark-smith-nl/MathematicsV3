package nl.smith.mathematics.domain;

import nl.smith.mathematics.annotation.MathematicalFunction;
import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.RationalNumberOutputType.Type;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static nl.smith.mathematics.annotation.MathematicalFunction.Type.*;
import static nl.smith.mathematics.domain.ExpressionStack.State.*;
import static nl.smith.mathematics.numbertype.RationalNumber.ONE;
import static nl.smith.mathematics.numbertype.RationalNumber.TEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpressionStackTest {

    private final ExpressionStack<RationalNumber> expressionStack = new ExpressionStack<>();

    @BeforeEach
    public void init() {
        RationalNumberOutputType.set(Type.COMPONENTS);
    }

    @Test
    public void initialState() {
        assertEquals(INITIALIZED, expressionStack.getState());
    }

    @Test
    public void addingMalFormedUnaryOperatorStackElement_usingNullMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addUnaryOperator(null));
        assertEquals("A method must be specified when instantiating UnaryOperatorStackElement.", actualException.getMessage());
    }

    @Test
    public void addingMalFormedUnaryOperatorStackElement_usingBinaryMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addUnaryOperator(getMethodMapping(2, BINARY_OPERATION)));
        assertEquals("A UnaryOperatorStackElement must wrap a MathematicalFunctionMethodMapping of type UNARY_OPERATION. Type specified BINARY_OPERATION.", actualException.getMessage());
    }

    @Test
    public void addingMalFormedBinaryOperatorStackElement_usingNullMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addBinaryOperator(null));
        assertEquals("A method must be specified when instantiating BinaryOperatorStackElement.", actualException.getMessage());
    }

    @Test
    public void addingMalFormedBinaryOperatorStackElement_usingUnaryMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(ONE).addBinaryOperator(getMethodMapping(1, UNARY_OPERATION)));
        assertEquals("A BinaryOperatorStackElement must wrap a MathematicalFunctionMethodMapping of type BINARY_OPERATION. Type specified UNARY_OPERATION.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateInitialized() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.setSibling(getExpressionStack(CLOSED)));
        assertEquals("Can not add an expression stack sibling to the current expression stack with state INITIALIZED.\nThe state of the current expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateAppendable() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(ONE).setSibling(getExpressionStack(CLOSED)));
        assertEquals("Can not add an expression stack sibling to the current expression stack with state APPENDABLE.\nThe state of the current expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateClosed_siblingStateInitialized() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(ONE).close().setSibling(getExpressionStack(INITIALIZED)));
        assertEquals("Can not add an expression stack sibling with state INITIALIZED to the current expression stack.\nThe state of the sibling expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateClosed_siblingStateAppendable() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(ONE).close().setSibling(getExpressionStack(APPENDABLE)));
        assertEquals("Can not add an expression stack sibling with state APPENDABLE to the current expression stack.\nThe state of the sibling expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateClosed_siblingStateClosed() {
        ExpressionStack<RationalNumber> siblingExpressionStack = getExpressionStack(CLOSED);
        expressionStack.addNumber(ONE).close().setSibling(siblingExpressionStack);
        assertSame(siblingExpressionStack, expressionStack.getSibling());
    }

    @Test
    public void getDimension() {
        expressionStack.addNumber(ONE).close().setSibling(getExpressionStack(CLOSED, 3));

        assertEquals(4, expressionStack.getDimension());
    }

    @ParameterizedTest
    @MethodSource("closeWithState")
    public void closeWithState(ExpressionStack<RationalNumber> expressionStack, ExpressionStack.State requestedState, Exception expectedException) {
        if (expectedException == null) {
            expressionStack.closeWithState(requestedState);
            assertEquals(requestedState, expressionStack.getState());
        } else {
            IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> expressionStack.closeWithState(requestedState));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("digest_illegalState")
    public void digest_illegalState(ExpressionStack<RationalNumber> expressionStack, Exception expectedException) {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.digest((Collections.emptyMap())));
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    //@ParameterizedTest
    //@MethodSource("digestBinaryOperators")
    public void digestBinaryOperators(ExpressionStack<RationalNumber> expressionStack, RationalNumber expectedValue, Exception expectedException) {
        if (expectedException == null) {
            expressionStack.digestBinaryOperators();
            assertEquals(DIGESTED, expressionStack.getState());
            assertEquals(1, expressionStack.size());
            assertEquals(RationalNumber.class, expressionStack.stackElements.get(0).getClass());
        } else {
            //IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> expressionStack.closeWithState(requestedState));
            //assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.digestCompoundExpressions((Collections.emptyMap())));
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    //@ParameterizedTest
    //@MethodSource("digestCompoundExpressions")
    public void digestCompoundExpressions(ExpressionStack<RationalNumber> expressionStack, RationalNumber expectedValue, Exception expectedException) {
        if (expectedException == null) {
            expressionStack.digestBinaryOperators();
            assertEquals(DIGESTED_COMPOUND_EXPRESSIONS, expressionStack.getState());
        } else {
          //  IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> expressionStack.closeWithState(requestedState));
          //  assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.digestCompoundExpressions((Collections.emptyMap())));
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    //@Test
    public void digest() {
        // Expression = 1 + 10
        expressionStack.addNumber(ONE)
                .addBinaryOperator(getMethodMapping(2, BINARY_OPERATION))
                .addNumber(TEN)
                .close();

        expressionStack.digest(Collections.emptyMap());
    }

    @Test
    public void digest_unknownVariable() {
        // Expression = A + Z + B + B
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack
                .addVariableName("A")
                .addBinaryOperator(getMethodMapping(2, BINARY_OPERATION))
                .addVariableName("Z")
                .addBinaryOperator(getMethodMapping(2, BINARY_OPERATION))
                .addVariableName("B")
                .addBinaryOperator(getMethodMapping(2, BINARY_OPERATION))
                .addVariableName("B")
                .close()
                .digest(Collections.emptyMap()));
        assertEquals("Unknown variable(s) 'A', 'B', 'Z'.", actualException.getMessage());
    }

    //@Test
    public void digest_knownVariable() {
        // Expression = A + Z + B + B ===> 1/3 + 1/7 + 2/3 + 2/3
        ExpressionStack<RationalNumber> digestedExpressionStack = expressionStack.addVariableName("A") // index: 6
                .addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)) // index: 5
                .addVariableName("Z") // index: 4
                .addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)) // index: 3
                .addVariableName("B") // index: 2
                .addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)) // index: 1
                .addVariableName("B") // index: 0
                .close()
                .digest(Map.of("A", new RationalNumber(1, 3), "B", new RationalNumber(2, 3), "Z", new RationalNumber(1, 7)));

        assertEquals(7, expressionStack.size());

        assertNotSame(expressionStack, digestedExpressionStack);
        assertEquals(7, digestedExpressionStack.size());
        assertEquals(CLOSED, digestedExpressionStack.getState());

        StackElement<RationalNumber, ?> stackElement = digestedExpressionStack.stackElements.get(0);
        assertEquals(StackElement.NumberStackElement.class, stackElement.getClass());
        assertEquals(new RationalNumber(2, 3), stackElement.getValue());
        stackElement = digestedExpressionStack.stackElements.get(2);
        assertEquals(StackElement.NumberStackElement.class, stackElement.getClass());
        assertEquals(new RationalNumber(2, 3), stackElement.getValue());
        stackElement = digestedExpressionStack.stackElements.get(4);
        assertEquals(StackElement.NumberStackElement.class, stackElement.getClass());
        assertEquals(new RationalNumber(1, 7), stackElement.getValue());
        stackElement = digestedExpressionStack.stackElements.get(6);
        assertEquals(StackElement.NumberStackElement.class, stackElement.getClass());
        assertEquals(new RationalNumber(1, 3), stackElement.getValue());
    }

    //@Test
    public void digest_unaryOperators() {
        // Expression = - 1/7 + - 2/13
        ExpressionStack<RationalNumber> digestedExpressionStack = expressionStack.addUnaryOperator(getMethodMapping(1, UNARY_OPERATION, new RationalNumber(-1, 7), new RationalNumber(1, 7))) // index: 4
                .addNumber(new RationalNumber(1, 7)) // index: 3
                .addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)) // index: 2
                .addUnaryOperator(getMethodMapping(1, UNARY_OPERATION, new RationalNumber(-2, 13), new RationalNumber(2, 13))) // index: 1
                .addNumber(new RationalNumber(2, 13)) // index: 0
                .close()
                .digest(Collections.emptyMap());

        assertEquals(5, expressionStack.size());

        assertNotSame(expressionStack, digestedExpressionStack);
        assertEquals(3, digestedExpressionStack.size());
        assertEquals(CLOSED, digestedExpressionStack.getState());

        assertEquals(new RationalNumber(-2, 13), digestedExpressionStack.stackElements.get(0).getValue());
        assertEquals(new RationalNumber(-1, 7), digestedExpressionStack.stackElements.get(2).getValue());
    }

    private static Stream<Arguments> digest_illegalState() {
        return Stream.of(
                of(new ExpressionStack<RationalNumber>(), new IllegalArgumentException("Can not digest an expression stack when it is in state INITIALIZED.")),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE), new IllegalArgumentException("Can not digest an expression stack when it is in state APPENDABLE."))
        );
    }

    private static Stream<Arguments> digestBinaryOperators() {
        return Stream.of(
                of(new ExpressionStack<RationalNumber>()
                        .addNumber(ONE)
                        .addBinaryOperator(getMethodMapping(2, BINARY_OPERATION, new RationalNumber(11), ONE, TEN))
                        .addNumber(TEN).closeWithState(DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS),
                        new RationalNumber(11),
                        null)

        );
    }

    private static MathematicalFunctionMethodMapping<RationalNumber> getMethodMapping(int parameterCount, MathematicalFunction.Type type) {
        return getMethodMapping(parameterCount, type, null, null);
    }

    /**
     * Method to return a MathematicalFunctionMethodMapping mock with the desired behaviour.
     * All mocked methods are being invoked to prevent an {@link org.mockito.exceptions.misusing.UnnecessaryStubbingException}
     *
     * @param parameterCount The number of parameters the method should receive
     * @param expectedValue  The return value of the method
     * @param arguments      The methods arguments
     * @return The MathematicalFunctionMethodMapping mock
     */
    private static MathematicalFunctionMethodMapping<RationalNumber> getMethodMapping(int parameterCount, MathematicalFunction.Type type, RationalNumber expectedValue, RationalNumber... arguments) {
        MathematicalFunctionMethodMapping<RationalNumber> methodMapping = mock(MathematicalFunctionMethodMapping.class);

        when(methodMapping.getParameterCount()).thenReturn(parameterCount);
        methodMapping.getParameterCount();

        when(methodMapping.invokeWithNumbers(arguments)).thenReturn(expectedValue);
        methodMapping.invokeWithNumbers(arguments);

        when(methodMapping.getType()).thenReturn(type);

        methodMapping.getType();

        return methodMapping;
    }

    /**
     * Method to return a ExpressionStack mock with the desired state.
     * The dimension is 1.
     * The digested return value is {@link RationalNumber#ZERO}
     * All mocked methods are being invoked to prevent an {@link org.mockito.exceptions.misusing.UnnecessaryStubbingException}
     *
     * @param state     The state of the stack
     * @return The MathematicalFunctionMethodMapping mock
     */
    private static ExpressionStack<RationalNumber> getExpressionStack(ExpressionStack.State state) {
        return getExpressionStack(state, 1);
    }

    /**
     * Method to return a ExpressionStack mock with the desired state an dimension.
     * The digested return value is  {@link RationalNumber#ZERO}
     * All mocked methods are being invoked to prevent an {@link org.mockito.exceptions.misusing.UnnecessaryStubbingException}
     *
     * @param state     The state of the stack
     * @return The MathematicalFunctionMethodMapping mock
     */
    private static ExpressionStack<RationalNumber> getExpressionStack(ExpressionStack.State state, int dimension) {
        return getExpressionStack(state, dimension, RationalNumber.ZERO);
    }

    /**
     * Method to return a ExpressionStack mock with the desired state, dimension and return value.
     * All mocked methods are being invoked to prevent an {@link org.mockito.exceptions.misusing.UnnecessaryStubbingException}
     *
     * @param state     The state of the stack
     * @param dimension The dimension of the stack
     * @param digestedValue  The value when the stack is digested. This value is wrapped in an expression stack
     * @return The MathematicalFunctionMethodMapping mock
     */
    private static ExpressionStack<RationalNumber> getExpressionStack(ExpressionStack.State state, int dimension, RationalNumber digestedValue) {
        return getExpressionStack(state, dimension, digestedValue, Collections.emptyMap());
    }

    /**
     * Method to return a ExpressionStack mock with the desired state, dimension and return value.
     * All mocked methods are being invoked to prevent an {@link org.mockito.exceptions.misusing.UnnecessaryStubbingException}
     *
     * @param state     The state of the stack
     * @param dimension The dimension of the stack
     * @param digestedValue  The value when the stack is digested. This value is wrapped in an expression stack
     * @param variables  The map of vaiables used to digest the expression stack
     * @return The MathematicalFunctionMethodMapping mock
     */
    private static ExpressionStack<RationalNumber> getExpressionStack(ExpressionStack.State state, int dimension, RationalNumber digestedValue, Map<String, RationalNumber> variables) {
        ExpressionStack<RationalNumber> expressionStack = mock(ExpressionStack.class);

        when(expressionStack.getState()).thenReturn(state);
        expressionStack.getState();

        when(expressionStack.getDimension()).thenReturn(dimension);
        expressionStack.getDimension();

        ExpressionStack<RationalNumber> digestedExpressionStack = new ExpressionStack<RationalNumber>().addNumber(digestedValue).close();

        when(expressionStack.digest(variables)).thenReturn(digestedExpressionStack);
        ExpressionStack<RationalNumber> digest = expressionStack.digest(variables);

        return expressionStack;
    }

    private static Stream<Arguments> closeWithState() {
        return Stream.of(
                // of(new ExpressionStack<RationalNumber>().addCompoundExpression("x"), CLOSED, null),
                of(new ExpressionStack<RationalNumber>().addCompoundExpression(getExpressionStack(CLOSED)), INITIALIZED, new IllegalStateException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state INITIALIZED.")),
                of(new ExpressionStack<RationalNumber>().addCompoundExpression(getExpressionStack(CLOSED)), APPENDABLE, new IllegalStateException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state APPENDABLE.")),
                of(new ExpressionStack<RationalNumber>().addCompoundExpression(getExpressionStack(CLOSED)), CLOSED, null),
                of(new ExpressionStack<RationalNumber>().addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_COMPOUND_EXPRESSIONS, new IllegalStateException("Can not change state of expression stack from CLOSED to DIGESTED_COMPOUND_EXPRESSIONS. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_MATHEMATICAL_FUNCTIONS, new IllegalStateException("Can not change state of expression stack from CLOSED to DIGESTED_MATHEMATICAL_FUNCTIONS. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_VARIABLES, new IllegalStateException("Can not change state of expression stack from CLOSED to DIGESTED_VARIABLES. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_UNARY_OPERATORS, new IllegalStateException("Can not change state of expression stack from CLOSED to DIGESTED_UNARY_OPERATORS. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS, new IllegalStateException("Can not change state of expression stack from CLOSED to DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED, new IllegalStateException("Can not change state of expression stack from CLOSED to DIGESTED. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),

                of(new ExpressionStack<RationalNumber>().addMathematicalFunction(getMethodMapping(1, FUNCTION)).addCompoundExpression(getExpressionStack(CLOSED)), INITIALIZED, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state INITIALIZED.")),
                of(new ExpressionStack<RationalNumber>().addMathematicalFunction(getMethodMapping(1, FUNCTION)).addCompoundExpression(getExpressionStack(CLOSED)), APPENDABLE, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state APPENDABLE.")),
                of(new ExpressionStack<RationalNumber>().addMathematicalFunction(getMethodMapping(1, FUNCTION)).addCompoundExpression(getExpressionStack(CLOSED)), CLOSED, null),
                of(new ExpressionStack<RationalNumber>().addMathematicalFunction(getMethodMapping(1, FUNCTION)).addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_COMPOUND_EXPRESSIONS, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_COMPOUND_EXPRESSIONS. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addMathematicalFunction(getMethodMapping(1, FUNCTION)).addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_MATHEMATICAL_FUNCTIONS, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_MATHEMATICAL_FUNCTIONS. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addMathematicalFunction(getMethodMapping(1, FUNCTION)).addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_VARIABLES, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_VARIABLES. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addMathematicalFunction(getMethodMapping(1, FUNCTION)).addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_UNARY_OPERATORS, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_UNARY_OPERATORS. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addMathematicalFunction(getMethodMapping(1, FUNCTION)).addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addMathematicalFunction(getMethodMapping(1, FUNCTION)).addCompoundExpression(getExpressionStack(CLOSED)), DIGESTED, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED. Stack contains element of type CompoundExpressionStackElement which is not allowed in the desired state.")),

                of(new ExpressionStack<RationalNumber>().addVariableName("x"), INITIALIZED, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state INITIALIZED.")),
                of(new ExpressionStack<RationalNumber>().addVariableName("x"), APPENDABLE, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state APPENDABLE.")),
                of(new ExpressionStack<RationalNumber>().addVariableName("x"), CLOSED, null),
                of(new ExpressionStack<RationalNumber>().addVariableName("x"), DIGESTED_COMPOUND_EXPRESSIONS, null),
                of(new ExpressionStack<RationalNumber>().addVariableName("x"), DIGESTED_MATHEMATICAL_FUNCTIONS, null),
                of(new ExpressionStack<RationalNumber>().addVariableName("x"), DIGESTED_VARIABLES, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_VARIABLES. Stack contains element of type VariableNameStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addVariableName("x"), DIGESTED_UNARY_OPERATORS, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_UNARY_OPERATORS. Stack contains element of type VariableNameStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addVariableName("x"), DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS. Stack contains element of type VariableNameStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addVariableName("x"), DIGESTED, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED. Stack contains element of type VariableNameStackElement which is not allowed in the desired state.")),

                of(new ExpressionStack<RationalNumber>().addUnaryOperator(getMethodMapping(1, UNARY_OPERATION)).addNumber(ONE), INITIALIZED, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state INITIALIZED.")),
                of(new ExpressionStack<RationalNumber>().addUnaryOperator(getMethodMapping(1, UNARY_OPERATION)).addNumber(ONE), APPENDABLE, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state APPENDABLE.")),
                of(new ExpressionStack<RationalNumber>().addUnaryOperator(getMethodMapping(1, UNARY_OPERATION)).addNumber(ONE), CLOSED, null),
                of(new ExpressionStack<RationalNumber>().addUnaryOperator(getMethodMapping(1, UNARY_OPERATION)).addNumber(ONE), DIGESTED_COMPOUND_EXPRESSIONS, null),
                of(new ExpressionStack<RationalNumber>().addUnaryOperator(getMethodMapping(1, UNARY_OPERATION)).addNumber(ONE), DIGESTED_MATHEMATICAL_FUNCTIONS, null),
                of(new ExpressionStack<RationalNumber>().addUnaryOperator(getMethodMapping(1, UNARY_OPERATION)).addNumber(ONE), DIGESTED_VARIABLES, null),
                of(new ExpressionStack<RationalNumber>().addUnaryOperator(getMethodMapping(1, UNARY_OPERATION)).addNumber(ONE), DIGESTED_UNARY_OPERATORS, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_UNARY_OPERATORS. Stack contains element of type UnaryOperatorStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addUnaryOperator(getMethodMapping(1, UNARY_OPERATION)).addNumber(ONE), DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS. Stack contains element of type UnaryOperatorStackElement which is not allowed in the desired state.")),
                of(new ExpressionStack<RationalNumber>().addUnaryOperator(getMethodMapping(1, UNARY_OPERATION)).addNumber(ONE), DIGESTED, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED. Stack contains element of type UnaryOperatorStackElement which is not allowed in the desired state.")),

                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), INITIALIZED, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state INITIALIZED.")),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), APPENDABLE, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state APPENDABLE.")),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), CLOSED, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_COMPOUND_EXPRESSIONS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_MATHEMATICAL_FUNCTIONS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_VARIABLES, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_UNARY_OPERATORS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED. Stack contains element of type BinaryOperatorStackElement which is not allowed in the desired state.")),

                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), INITIALIZED, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state INITIALIZED.")),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), APPENDABLE, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state APPENDABLE.")),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), CLOSED, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_COMPOUND_EXPRESSIONS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_MATHEMATICAL_FUNCTIONS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_VARIABLES, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_UNARY_OPERATORS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE).addBinaryOperator(getMethodMapping(2, BINARY_OPERATION)).addNumber(ONE), DIGESTED, new IllegalArgumentException("Can not change state of expression stack from CLOSED to DIGESTED. Stack contains element of type BinaryOperatorStackElement which is not allowed in the desired state.")),

                of(new ExpressionStack<RationalNumber>().addNumber(ONE), INITIALIZED, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state INITIALIZED.")),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE), APPENDABLE, new IllegalArgumentException("Can not close expression stack and change state of expression stack from current state APPENDABLE to requested state APPENDABLE.")),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE), CLOSED, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE), DIGESTED_COMPOUND_EXPRESSIONS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE), DIGESTED_MATHEMATICAL_FUNCTIONS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE), DIGESTED_VARIABLES, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE), DIGESTED_UNARY_OPERATORS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE), DIGESTED_HIGH_PRIORITY_BINARY_OPERATORS, null),
                of(new ExpressionStack<RationalNumber>().addNumber(ONE), DIGESTED, null)
        );
    }
}