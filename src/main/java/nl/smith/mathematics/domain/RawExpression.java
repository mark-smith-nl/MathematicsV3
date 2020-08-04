package nl.smith.mathematics.domain;

import java.util.*;

public class RawExpression {

    private final String text;

    /* Absolute position, The first character of the expression is text.charAt(startPosition) (including).
     * A startPosition -1 denotes that the first character or a subexpression has not been encountered.*/
    private int startPosition = -1;

    /* Absolute position, The last character of the expression is text.charAt(endPosition - 1) (not including).
     * A endPosition -1 denotes that the character that marks the expressions has ended has not been encountered.*/
    private int endPosition = -1;

    private RawExpression parent;

    private RawExpression sibling;

    private final List<RawExpression> subExpressions = new ArrayList<>();

    public RawExpression(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Please specify an expression.");
        }

        if (text.matches("\\s*")) {
            throw new IllegalArgumentException("An expression can not only contain blank characters.");
        }

        this.text = text;
    }

    private RawExpression(RawExpression rawExpression, boolean isSubExpression) {
        this(rawExpression.text);

        if (isSubExpression) {
            parent = rawExpression;
            rawExpression.subExpressions.add(this);
        } else {
            // The new expression is a sibling.
            rawExpression.sibling = this;
            parent = rawExpression.parent;
        }
    }

    public String getText() {
        return text;
    }

    public boolean isStartPositionSet() {
        return startPosition >= 0;
    }

    public boolean isEndPositionSet() {
        return endPosition >= 0;
    }

    public int getStartPosition() {
        assertStartPositionSet();

        return startPosition;
    }

    public RawExpression startExpressionAtPosition(int startPosition) {
        assertStartPositionNotSet();
        assertEndPositionNotSet();

        if (startPosition < 0 || startPosition >= text.length()) {
            throw new IllegalArgumentException("Can not set start position. Start position ∊ [0, text.length()>.");
        }

        this.startPosition = startPosition;

        return this;
    }

    public int getEndPosition() {
        assertStartPositionSet();
        assertEndPositionSet();

        return endPosition;
    }

    public RawExpression setEndPosition(int endPosition) {
        assertStartPositionSet();
        assertEndPositionNotSet();

        if (endPosition <= 0 || endPosition > text.length()) {
            throw new IllegalArgumentException("Can not set end position. End position ∊ <0, text.length].");
        }

        this.endPosition = endPosition;

        return parent == null ? this : parent;
    }

    /**
     * The method returns the subexpression of the terminated expression.
     * The method throws an error if the expression has not been terminated.
     */
    public List<RawExpression> getSubExpressions() {
        assertStartPositionSet();
        assertEndPositionSet();

        return subExpressions;
    }

    public RawExpression appendSibling(int endPosition) {
        assertStartPositionSet();
        assertEndPositionNotSet();

        this.endPosition = endPosition;

        return new RawExpression(this, false);
    }

    public RawExpression addSubExpression() {
        assertStartPositionSet();
        assertEndPositionNotSet();

        return new RawExpression(this, true);
    }

    /* Note method can be called if the endPosition has not been set. */
    public boolean isBlank(int position) {
        assertStartPositionSet();
        assertEndPositionNotSet();

        return getContent(position).matches("\\s*") && subExpressions.isEmpty();
    }

    public boolean hasSubExpressionsButNoContent(int position) {
        assertStartPositionSet();
        assertEndPositionNotSet();

        return getContent(position).matches("\\s*") && !subExpressions.isEmpty();
    }

    /**
     * The method returns the length of the terminated expression.
     * The method throws an error if the expression has not been terminated.
     */
    public int getLength() {
        assertStartPositionSet();
        assertEndPositionSet();

        return endPosition - startPosition;
    }

    public int getLengthWithSiblings() {
        assertStartPositionSet();
        assertEndPositionSet();

        int endPosition = getLastSibling().getEndPosition();

        return endPosition - startPosition;
    }

    public RawExpression getNthSibling(int n) {
        if (n < 0 || n >= getDimension()) {
            throw new IllegalArgumentException("Can not retrieve Nth sibling. Index should be on the domain [0-dimension>.");
        }

        return getSibling(n);
    }

    public RawExpression getLastSibling() {
        return getSibling(getDimension() - 1);
    }

    private RawExpression getSibling(int n) {
        return n == 0 ? this : sibling.getNthSibling(n - 1);
    }

    /** A raw expression without siblings has a dimension of 1 */
    public int getDimension() {
        assertStartPositionSet();
        assertEndPositionSet();

        if (sibling == null) {
            return 1;
        }

        return 1 + sibling.getDimension();
    }

    @Override
    public String toString() {
        if (!isStartPositionSet() || !isEndPositionSet()) {
            return "Expression is under construction";
        }

        return text.substring(startPosition, endPosition);
    }

    public String toStringWithSiblings() {
        assertStartPositionSet();
        assertEndPositionSet();

        int startPosition = getStartPosition();
        int endPosition = getLastSibling().getEndPosition();

        return text.substring(startPosition, endPosition);
    }

    public String toSimplifiedString() {
        assertStartPositionSet();
        assertEndPositionSet();

        String simplifiedString = text.substring(0, endPosition); // Remove trailing string

        for (int i = subExpressions.size() - 1; i >= 0; i--) {
            RawExpression subExpression = subExpressions.get(i);
            String tail = simplifiedString.substring(subExpression.endPosition + 1);
            String head = simplifiedString.substring(0, subExpression.startPosition - 1);
            simplifiedString = head + "${" + i + "}" + tail;
        }

        simplifiedString = simplifiedString.substring(startPosition);

        return simplifiedString;
    }

    private String getContent(int position) {
        assertStartPositionSet();

        String content = text.substring(0, position); // Remove trailing string

        for (int i = subExpressions.size() - 1; i >= 0; i--) {
            RawExpression subExpression = subExpressions.get(i);
            String tail = content.substring(subExpression.endPosition + 1);
            String head = content.substring(0, subExpression.startPosition - 1);
            content = head + tail;
        }

        return content.substring(startPosition);
    }

    private void assertStartPositionSet() {
        if (!isStartPositionSet()) {
            throw new IllegalStateException("Raw expression has not been started.");
        }
    }

    private void assertStartPositionNotSet() {
        if (isStartPositionSet()) {
            throw new IllegalStateException("Raw expression has already been started.");
        }
    }

    private void assertEndPositionSet() {
        if (!isEndPositionSet()) {
            throw new IllegalStateException("Raw expression has not been terminated.");
        }
    }

    private void assertEndPositionNotSet() {
        if (isEndPositionSet()) {
            throw new IllegalStateException("Raw expression has already been terminated.");
        }
    }

    public <N extends Number> ExpressionStack<N> getExpressionStack(Class<N> numberType, Set<String> unaryOperatorChars, Set<String> binaryOperatorChars, String numberPattern) {
        return null;
    }
}
