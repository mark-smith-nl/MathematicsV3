package nl.smith.mathematics.domain;

import nl.smith.mathematics.configuration.constant.RationalNumberOutputType;
import nl.smith.mathematics.configuration.constant.RationalNumberOutputType.Type;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static nl.smith.mathematics.domain.ExpressionStack.State.CLOSED;
import static nl.smith.mathematics.numbertype.RationalNumber.ONE;
import static nl.smith.mathematics.numbertype.RationalNumber.TEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpressionStackTest {

    private final ExpressionStack<RationalNumber> expressionStack = new ExpressionStack<>();

    @Mock
    private ExpressionStack<RationalNumber> siblingExpressionStack;

    @Mock
    private MathematicalFunctionMethodMapping<RationalNumber> negateMethodMapping;

    @Mock
    private MathematicalFunctionMethodMapping<RationalNumber> addMethodMapping;

    @BeforeEach
    public void init() {
        RationalNumberOutputType.set(Type.COMPONENTS);
    }

    @Test
    public void initialState() {
        assertEquals(ExpressionStack.State.INITIALIZED, expressionStack.getState());
    }

    @Test
    public void addingMalFormedUnaryOperatorStackElement_usingNullMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addUnaryOperator(null));
        assertEquals("A method must be specified.", actualException.getMessage());
    }

    @Test
    public void addingMalFormedUnaryOperatorStackElement_usingBinaryMethod() {
        when(negateMethodMapping.getParameterCount()).thenReturn(2);
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addUnaryOperator(negateMethodMapping));
        assertEquals("Method mapping to a unary operation should have 1 argument.", actualException.getMessage());
    }

    @Test
    public void addingMalFormedBinaryOperatorStackElement_usingNullMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addBinaryOperator(null));
        assertEquals("A method must be specified.", actualException.getMessage());
    }

    @Test
    public void addingMalFormedBinaryOperatorStackElement_usingUnaryMethod() {
        when(addMethodMapping.getParameterCount()).thenReturn(1);
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(ONE).addBinaryOperator(addMethodMapping));
        assertEquals("Method mapping to a binary operation should have 2 arguments.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateInitialized() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.setSibling(siblingExpressionStack));
        assertEquals("Can not add an expression stack sibling to the current expression stack with state INITIALIZED.\nThe state of the current expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateAppendable() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(ONE).setSibling(siblingExpressionStack));
        assertEquals("Can not add an expression stack sibling to the current expression stack with state APPENDABLE.\nThe state of the current expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateClosed_siblingStateInitialized() {
        when(siblingExpressionStack.getState()).thenReturn(ExpressionStack.State.INITIALIZED);
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(ONE).close().setSibling(siblingExpressionStack));
        assertEquals("Can not add an expression stack sibling with state INITIALIZED to the current expression stack.\nThe state of the sibling expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateClosed_siblingStateAppendable() {
        when(siblingExpressionStack.getState()).thenReturn(ExpressionStack.State.APPENDABLE);
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(ONE).close().setSibling(siblingExpressionStack));
        assertEquals("Can not add an expression stack sibling with state APPENDABLE to the current expression stack.\nThe state of the sibling expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateClosed_siblingStateClosed() {
        when(siblingExpressionStack.getState()).thenReturn(CLOSED);
        expressionStack.addNumber(ONE).close().setSibling(siblingExpressionStack);
        assertSame(siblingExpressionStack, expressionStack.getSibling());
    }

    @Test
    public void getDimension() {
        when(siblingExpressionStack.getState()).thenReturn(CLOSED);
        when(siblingExpressionStack.getDimension()).thenReturn(3);
        expressionStack.addNumber(ONE).close().setSibling(siblingExpressionStack);

        assertEquals(4, expressionStack.getDimension());
    }

    @ParameterizedTest
    @MethodSource("digest_illegalState")
    public void digest_illegalState(ExpressionStack<RationalNumber> expressionStack, Exception expectedException) {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.digest((Collections.emptyMap())));
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    public void digest() {
        when(addMethodMapping.getParameterCount()).thenReturn(2);
        // Expression = 1 + 10
        expressionStack.addNumber(ONE)
                .addBinaryOperator(addMethodMapping)
                .addNumber(TEN)
                .close();

        expressionStack.digest(Collections.emptyMap());
    }

    @Test
    public void digest_unknownVariable() {
        when(addMethodMapping.getParameterCount()).thenReturn(2);
        // Expression = A + Z + B + B
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack
                .addVariableName("A")
                .addBinaryOperator(addMethodMapping)
                .addVariableName("Z")
                .addBinaryOperator(addMethodMapping)
                .addVariableName("B")
                .addBinaryOperator(addMethodMapping)
                .addVariableName("B")
                .close()
                .digest(Collections.emptyMap()));
        assertEquals("Unknown variable(s) 'A', 'B', 'Z'.", actualException.getMessage());
    }

    @Test
    public void digest_knownVariable() {
        when(addMethodMapping.getParameterCount()).thenReturn(2);
        // Expression = A + Z + B + B
        ExpressionStack<RationalNumber> digestedExpressionStack = expressionStack.addVariableName("A") // index: 6
                .addBinaryOperator(addMethodMapping) // index: 5
                .addVariableName("Z") // index: 4
                .addBinaryOperator(addMethodMapping) // index: 3
                .addVariableName("B") // index: 2
                .addBinaryOperator(addMethodMapping) // index: 1
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

    @Test
    public void digest_unaryOperators() {
        RationalNumber rationalNumberOneDivideBySeven = new RationalNumber(1, 7);
        RationalNumber rationalNumberTwoDivideByThirteen = new RationalNumber(2, 13);

        when(negateMethodMapping.getParameterCount()).thenReturn(1);
        when(negateMethodMapping.invokeWithNumbers(eq(rationalNumberOneDivideBySeven))).thenReturn(rationalNumberOneDivideBySeven.negate());
        when(negateMethodMapping.invokeWithNumbers(eq(rationalNumberTwoDivideByThirteen))).thenReturn(rationalNumberTwoDivideByThirteen.negate());
        when(addMethodMapping.getParameterCount()).thenReturn(2);
        ExpressionStack<RationalNumber> digestedExpressionStack = expressionStack.addUnaryOperator(negateMethodMapping) // index: 4
                .addNumber(rationalNumberOneDivideBySeven) // index: 3
                .addBinaryOperator(addMethodMapping) // index: 2
                .addUnaryOperator(negateMethodMapping) // index: 1
                .addNumber(rationalNumberTwoDivideByThirteen) // index: 0
                .close()
                .digest(Collections.emptyMap());

        assertEquals(5, expressionStack.size());

        assertNotSame(expressionStack, digestedExpressionStack);
        assertEquals(3, digestedExpressionStack.size());
        assertEquals(CLOSED, digestedExpressionStack.getState());

        assertEquals(rationalNumberTwoDivideByThirteen.negate(), digestedExpressionStack.stackElements.get(0).getValue());
        assertEquals(rationalNumberOneDivideBySeven.negate(), digestedExpressionStack.stackElements.get(2).getValue());
    }

    private static Stream<Arguments> digest_illegalState() {
        return Stream.of(
                Arguments.of(new ExpressionStack<RationalNumber>(), new IllegalArgumentException("Can not digest an expression stack when it is in state INITIALIZED.")),
                Arguments.of(new ExpressionStack<RationalNumber>().addNumber(ONE), new IllegalArgumentException("Can not digest an expression stack when it is in state APPENDABLE."))
        );
    }

}