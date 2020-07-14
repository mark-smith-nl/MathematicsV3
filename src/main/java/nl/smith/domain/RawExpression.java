package nl.smith.domain;

import java.util.HashSet;
import java.util.Set;

public class RawExpression {

    private final int startsAt;

    /* Not including. */
    private int endsAt = -1;

    private final StringBuilder value;

    private RawExpression sibling;

    private final Set<RawExpression> subExpressions = new HashSet<>();

    public RawExpression() {
        this(0);
    }

    public RawExpression(int startsAt) {
        this.startsAt = startsAt;
        value = new StringBuilder();
    }

    public void addCharacter(char c) {
        value.append(c);
    }

    public int getStartsAt() {
        return startsAt;
    }

    public int getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(int endsAt) {
        this.endsAt = endsAt;
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
        return value.toString().matches("\\s*") && sibling == null && subExpressions.isEmpty();
    }

}
