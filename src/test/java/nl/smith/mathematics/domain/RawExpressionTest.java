package nl.smith.mathematics.domain;

import nl.smith.mathematics.util.RationalNumberUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

class RawExpressionTest {

    @ParameterizedTest
    @NullAndEmptySource
    public void constructor_usingNullOrEmptyArgument(String text) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new RawExpression(text));
        assertEquals("Please specify an expression.", exception.getMessage());
    }

    @Test
    public void getStartPosition_expressionNotStarted() {
        RawExpression rawExpression = new RawExpression("1 + 2");
        Exception exception = assertThrows(IllegalStateException.class, () -> rawExpression.getStartPosition());
        assertEquals(RawExpression.RAW_EXPRESSION_NOT_STARTED, exception.getMessage());
    }

    @Test
    public void getSubExpressions_expressionNotStarted() {
        RawExpression rawExpression = new RawExpression("1 + 2");
        Exception exception = assertThrows(IllegalStateException.class, () -> rawExpression.getSubExpressions());
        assertEquals(RawExpression.RAW_EXPRESSION_NOT_STARTED, exception.getMessage());
    }

    @Test
    public void getLength_expressionNotStarted() {
        RawExpression rawExpression = new RawExpression("1 + 2");
        Exception exception = assertThrows(IllegalStateException.class, () -> rawExpression.getLength());
        assertEquals(RawExpression.RAW_EXPRESSION_NOT_TERMINATED, exception.getMessage());
    }

}