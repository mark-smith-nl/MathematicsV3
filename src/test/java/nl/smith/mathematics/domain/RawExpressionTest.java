package nl.smith.mathematics.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RawExpressionTest {

    @ParameterizedTest
    @NullAndEmptySource
    public void constructor_usingNullOrEmptyArgument(String text) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new RawExpression(text));
        assertEquals("Please specify an expression.", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("constructor_usingBlankArgument")
    public void constructor_usingNullOrBlankArgument(String text) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new RawExpression(text));
        assertEquals("An expression can not only contain blank characters.", exception.getMessage());
    }

    @Test
    public void getStartPosition_expressionNotStarted() {
        RawExpression rawExpression = new RawExpression("1 + 2");
        Exception exception = assertThrows(IllegalStateException.class, rawExpression::getStartPosition);
        assertEquals("Raw expression has not been started.", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("startExpressionAtPosition")
    public void startExpressionAtPosition(int startPosition, Exception expectedException) {
        RawExpression rawExpression = new RawExpression("1 + 2");
        if (expectedException == null) {
            rawExpression.startExpressionAtPosition(startPosition);
        } else {
            IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> rawExpression.startExpressionAtPosition(startPosition));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("startExpressionAtPosition_startPositionAlreadySet")
    public void startExpressionAtPosition_startPositionAlreadySet(int startPosition, Exception expectedException) {
        RawExpression rawExpression = new RawExpression("1 + 2");
        rawExpression.startExpressionAtPosition(0);

        IllegalStateException actualException = assertThrows(IllegalStateException.class, () -> rawExpression.startExpressionAtPosition(startPosition));
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    public void getSubExpressions_expressionNotStarted() {
        RawExpression rawExpression = new RawExpression("1 + 2");
        Exception exception = assertThrows(IllegalStateException.class, rawExpression::getSubExpressions);
        assertEquals("Raw expression has not been started.", exception.getMessage());
    }

    @Test
    public void getSubExpressions_expressionNotTerminated() {
        RawExpression rawExpression = new RawExpression("1 + 2");
        rawExpression.startExpressionAtPosition(0);
        Exception exception = assertThrows(IllegalStateException.class, rawExpression::getSubExpressions);
        assertEquals("Raw expression has not been terminated.", exception.getMessage());
    }

    @Test
    public void getSubExpressions() {
        RawExpression rawExpression = new RawExpression("1 + 2 * (4 - 8) / (1 + 2)");
        rawExpression.startExpressionAtPosition(0)
                .addSubExpression()
                .startExpressionAtPosition(9)
                .setEndPosition(14)
                .addSubExpression()
                .startExpressionAtPosition(19)
                .setEndPosition(24);

        rawExpression.setEndPosition(25);

        List<RawExpression> subExpressions = rawExpression.getSubExpressions();
        assertEquals(2, subExpressions.size());
        rawExpression = subExpressions.get(0);
        assertEquals("4 - 8", rawExpression.toString());
        assertEquals(9, rawExpression.getStartPosition());
        assertEquals(14, rawExpression.getEndPosition());
        rawExpression = subExpressions.get(1);
        assertEquals("1 + 2", subExpressions.get(1).toString());
        assertEquals(19, rawExpression.getStartPosition());
        assertEquals(24, rawExpression.getEndPosition());
    }


    @Test
    public void addSubExpressions_expressionTerminated() {
        RawExpression rawExpression = new RawExpression("1 + 2");

        rawExpression.startExpressionAtPosition(0)
                .setEndPosition(5);

        Exception exception = assertThrows(IllegalStateException.class, rawExpression::addSubExpression);
        assertEquals("Raw expression has already been terminated.", exception.getMessage());
    }

    @Test
    public void appendSibling_expressionNotStarted() {
        RawExpression rawExpression = new RawExpression("1 + 2, 5 - 3");

        Exception exception = assertThrows(IllegalStateException.class, () -> rawExpression.appendSibling(5));
        assertEquals("Raw expression has not been started.", exception.getMessage());
    }

    @Test
    public void appendSibling_expressionTerminatedWithSiblingAlreadySet() {
        RawExpression rawExpression = new RawExpression("1 + 2, 5 - 3");

        rawExpression.startExpressionAtPosition(0)
                .appendSibling(5)
                .startExpressionAtPosition(6)
                .setEndPosition(12);


        Exception exception = assertThrows(IllegalStateException.class, () -> rawExpression.appendSibling(4));
        assertEquals("Raw expression has already been terminated.", exception.getMessage());
    }

    @Test
    public void appendSibling() {
        RawExpression rawExpression = new RawExpression("1 + 2, 5 - 3");

        rawExpression.startExpressionAtPosition(0)
                .appendSibling(5)
                .startExpressionAtPosition(6)
                .setEndPosition(12);
    }

    @Test
    public void getDimension_siblingNotStarted() {
        RawExpression rawExpression = new RawExpression("1 + 2, 5 - 3");

        rawExpression.startExpressionAtPosition(0)
                .appendSibling(5);

        Exception exception = assertThrows(IllegalStateException.class, rawExpression::getDimension);
        assertEquals("Raw expression has not been started.", exception.getMessage());
    }

    @Test
    public void getDimension_siblingNotTerminated() {
        RawExpression rawExpression = new RawExpression("1 + 2, 5 - 3");

        rawExpression.startExpressionAtPosition(0)
                .appendSibling(5)
                .startExpressionAtPosition(6);

        Exception exception = assertThrows(IllegalStateException.class, rawExpression::getDimension);
        assertEquals("Raw expression has not been terminated.", exception.getMessage());
    }

    @Test
    public void getDimension() {
        RawExpression rawExpression = new RawExpression("1 + 2, 5 - 3");

        rawExpression.startExpressionAtPosition(0)
                .appendSibling(5)
                .startExpressionAtPosition(6)
                .setEndPosition(12);

        assertEquals(2, rawExpression.getDimension());
    }

    @Test
    public void getLength_expressionNotStarted() {
        RawExpression rawExpression = new RawExpression("1 + 2");

        Exception exception = assertThrows(IllegalStateException.class, rawExpression::getLength);
        assertEquals("Raw expression has not been started.", exception.getMessage());
    }

    @Test
    public void getLength_expressionNotTerminated() {
        RawExpression rawExpression = new RawExpression("1 + 2");
        rawExpression.startExpressionAtPosition(0);
        Exception exception = assertThrows(IllegalStateException.class, rawExpression::getLength);
        assertEquals("Raw expression has not been terminated.", exception.getMessage());
    }

    @Test
    public void getLength() {
        RawExpression rawExpression = new RawExpression("1 + 2");
        rawExpression.startExpressionAtPosition(0)
                .setEndPosition(5);

        assertEquals(5, rawExpression.getLength());
    }

    @Test
    public void getLength_expressionWithUnterminatedSibling() {
        RawExpression rawExpression = new RawExpression("1 + 2");
        rawExpression.startExpressionAtPosition(0)
                .appendSibling(5);

        assertEquals(5, rawExpression.getLength());
    }

    @Test
    public void getLengthWithSiblings() {
        RawExpression rawExpression = new RawExpression("1 + 2, 3 + 4");
        rawExpression.startExpressionAtPosition(0)
                .appendSibling(5)
                .startExpressionAtPosition(6)
                .setEndPosition(12);

        assertEquals(12, rawExpression.getLengthWithSiblings());
    }

    @Test
    public void getLengthWithSiblings_siblingNotTerminated() {
        RawExpression rawExpression = new RawExpression("1 + 2, 3 + 4");
        rawExpression.startExpressionAtPosition(0)
                .appendSibling(5)
                .startExpressionAtPosition(6);

        Exception exception = assertThrows(IllegalStateException.class, rawExpression::getLengthWithSiblings);
        assertEquals("Raw expression has not been terminated.", exception.getMessage());
    }

    @Test
    public void doIt() {
        RawExpression rawExpression = new RawExpression("(1 + 2) * (1 + 3) * {6 - 4 / (2 - 4)}, 1 + sum(1, 2, 3) + 4, 3 + 4, 2 * average(6 * 7)");
        //                                               0123456789012345678901234567890123456789012345678901234567890123456789012345678901235467890
        rawExpression.startExpressionAtPosition(0)
                .addSubExpression()
                .startExpressionAtPosition(1)
                .setEndPosition(6)
                .addSubExpression()
                .startExpressionAtPosition(11)
                .setEndPosition(16)
                .addSubExpression()
                .startExpressionAtPosition(21)
                .addSubExpression()
                .startExpressionAtPosition(30)
                .setEndPosition(35)
                .setEndPosition(36)
                .appendSibling(37)
                .startExpressionAtPosition(38)
                .addSubExpression()
                .startExpressionAtPosition(47)
                .appendSibling(48)
                .startExpressionAtPosition(49)
                .appendSibling(51)
                .startExpressionAtPosition(52)
                .setEndPosition(54)
                .appendSibling(59)
                .startExpressionAtPosition(60)
                .appendSibling(66)
                .startExpressionAtPosition(67)
                .addSubExpression()
                .startExpressionAtPosition(70)
                .setEndPosition(85)
                .setEndPosition(86);

        System.out.println(rawExpression.getDimension());

        for (int i = 0; i < rawExpression.getDimension(); i++) {
                System.out.println(rawExpression.getNthSibling(i).toString());
                System.out.println(rawExpression.getNthSibling(i).toSimplifiedString());
                System.out.println();
        }
        System.out.println(rawExpression.getText());
        System.out.println(rawExpression);
        System.out.println(rawExpression.toSimplifiedString());
    }

    private static Stream<Arguments> constructor_usingBlankArgument() {
        return Stream.of(
                Arguments.of(" "),
                Arguments.of("\t"),
                Arguments.of("\t "),
                Arguments.of("\n"),
                Arguments.of("\n"),
                Arguments.of("\n\t")
        );
    }

    private static Stream<Arguments> startExpressionAtPosition() {
        return Stream.of(
                Arguments.of(-1, new IllegalStateException("Can not set start position. Start position ∊ [0, text.length()>.")),
                Arguments.of(0, null),
                Arguments.of(4, null),
                Arguments.of(5, new IllegalStateException("Can not set start position. Start position ∊ [0, text.length()>.")),
                Arguments.of(100, new IllegalStateException("Can not set start position. Start position ∊ [0, text.length()>."))
        );
    }

    private static Stream<Arguments> startExpressionAtPosition_startPositionAlreadySet() {
        return Stream.of(
                Arguments.of(-1, new IllegalStateException("Raw expression has already been started.")),
                Arguments.of(0, new IllegalStateException("Raw expression has already been started.")),
                Arguments.of(4, new IllegalStateException("Raw expression has already been started.")),
                Arguments.of(5, new IllegalStateException("Raw expression has already been started.")),
                Arguments.of(100, new IllegalStateException("Raw expression has already been started.")),
                Arguments.of(0, new IllegalStateException("Raw expression has already been started."))
        );
    }
}