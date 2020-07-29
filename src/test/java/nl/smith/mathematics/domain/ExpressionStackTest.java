package nl.smith.mathematics.domain;

import nl.smith.mathematics.mathematicalfunctions.MathematicalFunctionMethodMapping;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static nl.smith.mathematics.numbertype.RationalNumber.ONE;
import static nl.smith.mathematics.numbertype.RationalNumber.TEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpressionStackTest {

    private final ExpressionStack<RationalNumber> expressionStack = new ExpressionStack<>();

    @Mock
    private ExpressionStack<RationalNumber> siblingExpressionStack;

    @Mock
    private MathematicalFunctionMethodMapping negateMethodMapping;

    @Mock
    private MathematicalFunctionMethodMapping addMethodMapping;

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
        when(siblingExpressionStack.getState()).thenReturn(ExpressionStack.State.CLOSED);
        expressionStack.addNumber(ONE).close().setSibling(siblingExpressionStack);
        assertSame(siblingExpressionStack, expressionStack.getSibling());
    }

    @Test
    public void getDimension() {
        when(siblingExpressionStack.getState()).thenReturn(ExpressionStack.State.CLOSED);
        when(siblingExpressionStack.getDimension()).thenReturn(3);
        expressionStack.addNumber(ONE).close().setSibling(siblingExpressionStack);

        assertEquals(4, expressionStack.getDimension());
    }

    @ParameterizedTest
    @MethodSource("digest_illegalState")
    public void digest_illegalState(ExpressionStack<RationalNumber> expressionStack, Exception expectedException) {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.digest(Collections.EMPTY_MAP));
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    public void digest() {
        when(addMethodMapping.getParameterCount()).thenReturn(2);
        expressionStack.addNumber(ONE)
                .addBinaryOperator(addMethodMapping)
                .addNumber(TEN)
                .close();

        expressionStack.digest(Collections.emptyMap());
    }

    @Test
    public void digest_unknownVariable() {
        when(negateMethodMapping.getParameterCount()).thenReturn(1);
        when(addMethodMapping.getParameterCount()).thenReturn(2);
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addVariableName("A")
                .addBinaryOperator(addMethodMapping)
                .addUnaryOperator(negateMethodMapping)
                .addVariableName("B")
                .close()
                .digest(Collections.emptyMap()));
        assertEquals("Unknown variable 'A'.", actualException.getMessage());
    }

   // @Test
    public void digest_variables() {
        when(negateMethodMapping.getParameterCount()).thenReturn(1);
        when(addMethodMapping.getParameterCount()).thenReturn(2);
        expressionStack.addVariableName("A")
                .addBinaryOperator(addMethodMapping)
                .addUnaryOperator(negateMethodMapping)
                .addVariableName("B")
                .close()
                .digest(Map.of("A", ONE, "B", TEN));

        System.out.println(expressionStack);
    }

    private static Stream<Arguments> digest_illegalState() {
        return Stream.of(
                Arguments.of(new ExpressionStack<RationalNumber>(), new IllegalArgumentException("Can not digest an expression stack when it is in state INITIALIZED.")),
                Arguments.of(new ExpressionStack<RationalNumber>().addNumber(ONE), new IllegalArgumentException("Can not digest an expression stack when it is in state APPENDABLE."))
        );
    }

}