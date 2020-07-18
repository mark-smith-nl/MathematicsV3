package nl.smith.mathematics.domain;

import java.util.*;

public class RawExpression {

    private final String text;

    /* Absolute position, The first character of the expression is text.charAt(startPosition) (including).
    * A startPosition -1 denotes that the first character has not been encountered.*/
    private int startPosition = -1;

    /* Absolute position, The last character of the expression is text.charAt(endPosition - 1) (not including).
     * A endPosition -1 denotes that the character that marks the expressions has ended has not been encountered.*/
    private int endPosition = -1;

    private final StringBuilder value = new StringBuilder();

    private RawExpression sibling;

    private final List<RawExpression> subExpressions = new ArrayList<>();

    public static  final String RAW_EXPRESSION_NOT_STARTED = "Raw expression has not been started. First character has not been encountered.";

    public static  final String RAW_EXPRESSION_NOT_TERMINATED = "Raw expression has not terminated. A character denoting the end of the expression has not been encountered.";

    public RawExpression(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Please specify an expression.");
        }
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isNotStarted() {
        return startPosition == -1;
    }

    public boolean isNotTerminated() {
        return endPosition == -1;
    }

    public void addCharacter(char c) {
        if (isNotStarted()) {
            throw new IllegalStateException(RAW_EXPRESSION_NOT_STARTED);
        }

        value.append(c);
    }

    public int getStartPosition() {
        if (isNotStarted()) {
            throw new IllegalStateException(RAW_EXPRESSION_NOT_STARTED);
        }

        return startPosition;
    }

    public void startExpressionAtPosition(int startPosition) {
        if (startPosition < 0 || startPosition >= text.length()) {
            throw new IllegalArgumentException(String.format("Can not set start position. It can not be negative and must be smaller than the length of the original string [0, %d>.", text.length()));
        }

        if (!isNotStarted()) {
            throw new IllegalArgumentException("Start position is already set.");
        }

        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        if (isNotTerminated()) {
            throw new IllegalStateException(RAW_EXPRESSION_NOT_TERMINATED);
        }

        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        if (isNotStarted()) {
            throw new IllegalStateException(RAW_EXPRESSION_NOT_STARTED);
        }

        if (!isNotTerminated()) {
            throw new IllegalArgumentException("Expression is already terminated.");
        }

        this.endPosition = endPosition;
    }

    public List<RawExpression> getSubExpressions() {
        if (isNotStarted()) {
            throw new IllegalStateException(RAW_EXPRESSION_NOT_STARTED);
        }

        return subExpressions;
    }

    public void setSibling(RawExpression sibling) {
        if (isNotTerminated()) {
            throw new IllegalStateException(RAW_EXPRESSION_NOT_TERMINATED);
        }

        this.sibling = sibling;
    }

    public void addSubExpression(RawExpression rawExpression) {
        if (isNotStarted()) {
            throw new IllegalStateException(RAW_EXPRESSION_NOT_STARTED);
        }

        subExpressions.add(rawExpression);
    }


    public boolean isBlank() {
        return value.toString().matches("\\s*") && subExpressions.isEmpty();
    }

    public boolean hasSubExpressionsButNoContent() {
        return value.toString().matches("\\s*") && !subExpressions.isEmpty();
    }

    public int getLength() {
        if (isNotTerminated()) {
            throw new IllegalStateException(RAW_EXPRESSION_NOT_TERMINATED);
        }

        return endPosition - startPosition;
    }

    public int getLengthWithSiblings() {
        if (isNotTerminated()) {
            throw new IllegalStateException(RAW_EXPRESSION_NOT_TERMINATED);
        }

        return endPosition - startPosition + (sibling == null ? 0 : 1 + sibling.getLengthWithSiblings());
    }

    public RawExpression getNthSibling(int n) {
        if (n < 0 || n >= getDimension()) {
            throw new IllegalArgumentException(String.format("Can not retrieve %dth sibling. Index should be on the domain [0-%d>.", n, getDimension()));
        }

        return getSibling(n);
    }

    private RawExpression getSibling(int n) {
        return n == 0 ?  this : sibling.getNthSibling(n -1);

    }
    public int getNumberOfSiblings() {
        return sibling == null ? 0 : 1 +sibling.getNumberOfSiblings();
    }

    public int getDimension() {
        return 1 + getNumberOfSiblings();
    }
    @Override
    public String toString() {
        if (isNotTerminated()) {
            return RAW_EXPRESSION_NOT_TERMINATED;
        }

        return text.substring(startPosition, endPosition);
    }

    public String toStringWithSiblings() {
        if (isNotTerminated()) {
            return RAW_EXPRESSION_NOT_TERMINATED;
        }

        return text.substring(startPosition, endPosition) + (sibling == null ? "" : ("," + sibling.toStringWithSiblings())) ;
    }

    public String toSimplifiedString() {
        if (isNotTerminated()) {
            return RAW_EXPRESSION_NOT_TERMINATED;
        }

        String simplifiedString = text;
        simplifiedString = simplifiedString.substring(0, endPosition); // Remove trailing string

        for (int i = subExpressions.size() - 1; i >= 0; i--) {
            RawExpression subExpression = subExpressions.get(i);
            String tail = simplifiedString.substring(subExpression.endPosition + 1);
            String head = simplifiedString.substring(0, subExpression.startPosition - 1);
            simplifiedString = head + "$" + i + tail;
        }

        simplifiedString = simplifiedString.substring(startPosition);

        return simplifiedString;
    }

}
