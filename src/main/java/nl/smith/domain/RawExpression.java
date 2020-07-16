package nl.smith.domain;

import java.util.HashSet;
import java.util.Set;

public class RawExpression {

    private boolean initialized;

    /* Including. */
    private int startPosition;

    /* Not including. */
    private int endPosition;

    private final StringBuilder value = new StringBuilder();

    private RawExpression sibling;

    private final Set<RawExpression> subExpressions = new HashSet<>();

    public boolean isInitialized() {
        return initialized;
    }

    public void initializeAtPosition(int startPosition) {
        initialized = true;
        this.startPosition = startPosition;
    }

    public void addCharacter(char c) {
        value.append(c);
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndsPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public Set<RawExpression> getSubExpressions() {
        return subExpressions;
    }

    public RawExpression getSibling() {
        return sibling;
    }

    public void setSibling(RawExpression sibling) {
        this.sibling = sibling;
    }

    public void addSubExpression(RawExpression rawExpression) {
        subExpressions.add(rawExpression);
    }

    public boolean isBlank() {
        return value.toString().matches("\\s*") && subExpressions.isEmpty();
    }

    public boolean hasOneSubExpressionsButNoContent() {
        return value.toString().matches("\\s*") && subExpressions.size() == 1;
    }
}
