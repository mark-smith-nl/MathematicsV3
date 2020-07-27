package nl.smith.mathematics.domain;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpressionStackTest {

    private final ExpressionStack<RationalNumber> expressionStack = new ExpressionStack<>();

    @Mock
    private ExpressionStack<RationalNumber> siblingExpressionStack;
    @Test
    public void initialState() {
        assertEquals(ExpressionStack.State.INITIALIZED, expressionStack.getState());
    }

    @Test
    public void addingMalFormedUnaryOperatorStackElement_usingNullMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addUnaryOperator(null));
        assertEquals("Method must be specified.", actualException.getMessage());
    }

    @Test
    public void addingMalFormedUnaryOperatorStackElement_usingBinaryMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addUnaryOperator(OperatorContainer.getBinaryMethod()));
        assertEquals("Method mapping to a unary operation should have 1 argument.", actualException.getMessage());
    }

    @Test
    public void addingMalFormedBinaryOperatorStackElement_usingNullMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addBinaryOperator(null));
        assertEquals("Method must be specified.", actualException.getMessage());
    }

    @Test
    public void addingMalFormedBinaryOperatorStackElement_usingUnaryMethod() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addBinaryOperator(OperatorContainer.getUnaryMethod()));
        assertEquals("Method mapping to a binary operation should have 2 arguments.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateInitialized() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.setSibling(siblingExpressionStack));
        assertEquals("Can not add an expression stack sibling to the current expression stack with state INITIALIZED.\nThe state of the current expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateAppendable() {
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(RationalNumber.ONE).setSibling(siblingExpressionStack));
        assertEquals("Can not add an expression stack sibling to the current expression stack with state APPENDABLE.\nThe state of the current expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateClosed_siblingStateInitialized() {
        when(siblingExpressionStack.getState()).thenReturn(ExpressionStack.State.INITIALIZED);
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(RationalNumber.ONE).close().setSibling(siblingExpressionStack));
        assertEquals("Can not add an expression stack sibling with state INITIALIZED to the current expression stack.\nThe state of the sibling expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateClosed_siblingStateAppendable() {
        when(siblingExpressionStack.getState()).thenReturn(ExpressionStack.State.APPENDABLE);
        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> expressionStack.addNumber(RationalNumber.ONE).close().setSibling(siblingExpressionStack));
        assertEquals("Can not add an expression stack sibling with state APPENDABLE to the current expression stack.\nThe state of the sibling expression stack should be CLOSED.", actualException.getMessage());
    }

    @Test
    public void setSibling_expressionStackStateClosed_siblingStateClosed() {
        when(siblingExpressionStack.getState()).thenReturn(ExpressionStack.State.CLOSED);
        expressionStack.addNumber(RationalNumber.ONE).close().setSibling(siblingExpressionStack);
        assertSame(siblingExpressionStack, expressionStack.getSibling());
    }

    @Test
    void getDimension() {
        when(siblingExpressionStack.getState()).thenReturn(ExpressionStack.State.CLOSED);
        when(siblingExpressionStack.getDimension()).thenReturn(3);
        expressionStack.addNumber(RationalNumber.ONE).close().setSibling(siblingExpressionStack);

        assertEquals(4, expressionStack.getDimension());
    }

    private static class OperatorContainer {

        public RationalNumber unaryMethod(RationalNumber numberOne) {
            return  numberOne.negate();
        }

        public RationalNumber binaryMethod(RationalNumber numberOne, RationalNumber numberTwo) {
            return numberOne.add(numberTwo);
        }

        public static Method getUnaryMethod() {
            try {
                return OperatorContainer.class.getDeclaredMethod("unaryMethod", RationalNumber.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Missing method.");
            }
        }

        public static Method getBinaryMethod() {
            try {
                return OperatorContainer.class.getDeclaredMethod("binaryMethod", RationalNumber.class, RationalNumber.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Missing method.");
            }
        }

    }

}