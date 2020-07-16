package nl.smith.mathematics.domain;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RawExpression {

    private final String text;

    private boolean initialized;

    /* Including. */
    private int startPosition;

    /* Not including. */
    private int endPosition = -1;

    private final StringBuilder value = new StringBuilder();

    private RawExpression sibling;

    private final Set<RawExpression> subExpressions = new HashSet<>();

    public RawExpression(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

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

    public boolean hasSubExpressionsButNoContent() {
        return value.toString().matches("\\s*") && !subExpressions.isEmpty();
    }

    public int length() {
        return endPosition - startPosition;
    }

    @Override
    public String toString() {
        if (!initialized) {
            return "Raw expression has not been initialized.";
        }

        return text.substring(startPosition, endPosition);
    }

    public String toSimplifiedString() {
        if (!initialized) {
            return "Raw expression has not been initialized.";
        }

        String simplifiedString = text;
        simplifiedString.substring(endPosition); // Remove trailing string
        if (!subExpressions.isEmpty()) {
            TreeMap<Integer, RawExpression> subExpressionsPositionMap = subExpressions.stream().collect(Collectors.toMap(RawExpression::getStartPosition, Function.identity(), (o1, o2) -> o1, TreeMap::new));

            for (int position : subExpressionsPositionMap.descendingKeySet()) {

                System.out.println("value of " + position + " is " + subExpressionsPositionMap.get(position));

            }
        }

        return text.substring(startPosition, endPosition);
    }



}
