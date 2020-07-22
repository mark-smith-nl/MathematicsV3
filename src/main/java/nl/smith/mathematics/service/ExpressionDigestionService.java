package nl.smith.mathematics.service;

import javafx.util.Pair;
import nl.smith.mathematics.annotation.constraint.TextWithoutReservedCharacters;
import nl.smith.mathematics.domain.RawExpression;
import nl.smith.mathematics.annotation.constraint.TextWithoutLinesWithTrailingBlanks;
import nl.smith.mathematics.exception.InValidExpressionStringException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service to interpret a mathematical expression string.
 */
@Service
public class ExpressionDigestionService extends RecursiveValidatedService<ExpressionDigestionService> {

    private static final String SIBLING_BEAN_NAME = "EXPRESSIONDIGESTIONSERVICE";

    private final TextAnnotationService textAnnotationService;

    private static final Set<Pair<Character, Character>> AGGREGATION_TOKEN_PAIRS = new HashSet<>(Arrays.asList(
            new Pair<>('(', ')'),
            new Pair<>('{', '}'),
            new Pair<>('[', ']')));

    private static final char START_SIBLING_CHARACTER = ',';

    private static Set<Character> PREDEFINED_CHARACTERS = new HashSet(Arrays.asList('$', '#', '@', '%'));

    public ExpressionDigestionService(TextAnnotationService textAnnotationService) {
        this.textAnnotationService = textAnnotationService;
    }

    /**
     * Protected for test purposes.
     */
    protected enum CharacterType {
        NORMAL,
        BEGIN_SIBLING,
        BEGIN_SUBEXPRESSION,
        END_SUBEXPRESSION,
        PREDEFINED_CHARACTER
    }

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    @Override
    public ExpressionDigestionService makeSibling() {
        return new ExpressionDigestionService(textAnnotationService);
    }

    /**
     * Protected for test purposes.
     */
    protected RawExpression getRawExpression(@NotBlank(message = "Please specify an expression.") @TextWithoutLinesWithTrailingBlanks @TextWithoutReservedCharacters String text) {
        RawExpression rawExpression = new RawExpression(text); // Note: the expression has not been initialized.
        RawExpression mainRawExpression = rawExpression;
        LinkedList<Pair<Character, Integer>> openeningAggregationTokenStack = new LinkedList<>();

        int position;
        for (position = 0; position < text.length(); position++) {
            char c = text.charAt(position);
            switch (getCharacterType(c)) {
                case NORMAL:
                    if (!rawExpression.isStartPositionSet()) {
                        rawExpression.startExpressionAtPosition(position);
                    }

                    break;
                case PREDEFINED_CHARACTER :
                    String message = String.format("Predefine character found. Please do not use [%s]", PREDEFINED_CHARACTERS.stream().map(String::valueOf).collect(Collectors.joining(", ")));
                    throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(rawExpression.getText(), position));
                case BEGIN_SIBLING:
                    // Beginning a sibling implicates a previous initialized sibling has been set.
                    assertRawExpressionInitializedAndNotEmpty(rawExpression, position);
                    rawExpression = rawExpression.appendSibling(position);

                    break;
                case BEGIN_SUBEXPRESSION:
                    if (!rawExpression.isStartPositionSet()) {
                        rawExpression.startExpressionAtPosition(position);
                    }

                    rawExpression = rawExpression.addSubExpression();

                    openeningAggregationTokenStack.push(new Pair<>(c, position));
                    break;
                case END_SUBEXPRESSION:
                    // Validate proper nesting of aggregation tokens.
                    assertTokenStackIsNotEmptyAndClosedTokenMatchesOpenToken(openeningAggregationTokenStack, text, position, c);
                    // Ending a subexpression implicates that the subexpression was initialized.
                    assertRawExpressionInitializedAndNotEmpty(rawExpression, position);
                    rawExpression = rawExpression.setEndPosition(position);


                    openeningAggregationTokenStack.pop(); // Remove the opening token from the token stack.

                    break;
            }
        }

        assertTokenStackIsEmpty(openeningAggregationTokenStack, text);
        assertRawExpressionInitializedAndNotEmpty(rawExpression, position);
        rawExpression.setEndPosition(position);

        return mainRawExpression;
    }

    /**
     * Protected for test purposes.
     */
    protected static char getMatchingOpenToken(char c) {
        for (Pair<Character, Character> atp : AGGREGATION_TOKEN_PAIRS) {
            if (atp.getValue() == c) {
                return atp.getKey();
            }
        }

        throw new IllegalArgumentException(String.format("Character '%c' is not a close token.", c));
    }

    /**
     * Protected for test purposes.
     */
    protected static char getMatchingCloseToken(char c) {
        for (Pair<Character, Character> atp : AGGREGATION_TOKEN_PAIRS) {
            if (atp.getKey() == c) {
                return atp.getValue();
            }
        }
        throw new IllegalArgumentException(String.format("Character '%c' is not an open token.", c));
    }

    /**
     * Protected for test purposes.
     */
    protected static CharacterType getCharacterType(char c) {
        if (c == START_SIBLING_CHARACTER) {
            return CharacterType.BEGIN_SIBLING;
        }

        for (Pair<Character, Character> atp : AGGREGATION_TOKEN_PAIRS) {
            if (atp.getKey() == c) {
                return CharacterType.BEGIN_SUBEXPRESSION;
            }
            if (atp.getValue() == c) {
                return CharacterType.END_SUBEXPRESSION;
            }
        }

        if(PREDEFINED_CHARACTERS.contains(c)) {
            return CharacterType.PREDEFINED_CHARACTER;
        }

        return CharacterType.NORMAL;
    }

    /**
     * <pre>
     *     Method throws an exception in the following cases:
     *
     *     - The rawExpression has not been initialized
     *     - The rawexpression contains only blank characters
     *
     *     If no exception is thrown the expression is eligible for termination.
     * </pre>
     */
    private void assertRawExpressionInitializedAndNotEmpty(RawExpression rawExpression , int position) {

        if (!rawExpression.isStartPositionSet()) {
            Set<Integer> positions = new HashSet<>(Collections.singletonList(position));
            String message = String.format("Expected an expression before position %d.%nDid you forget to specify the expression?", position);
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(rawExpression.getText(), positions));
        }

        if (rawExpression.isBlank(position)) {
            Set<Integer> positions = new HashSet<>(Arrays.asList(rawExpression.getStartPosition(), position - 1));
            String message = String.format("Blank expression from [%d-%d].%nDid you forget to specify the expression?", rawExpression.getStartPosition(), position - 1);
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(rawExpression.getText(), positions));
        }

        if (rawExpression.hasSubExpressionsButNoContent(position)) {
            Set<Integer> positions = new HashSet<>(Arrays.asList(rawExpression.getStartPosition(), position - 1));
            String message = String.format("Essentially blank expression (contains only subexpressions) from [%d-%d].%nRemove unnecessary aggregation tokens and blank characters.", rawExpression.getStartPosition(), position);
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(rawExpression.getText(), positions));
        }
    }

    private void assertTokenStackIsNotEmptyAndClosedTokenMatchesOpenToken(LinkedList<Pair<Character, Integer>> openingAggregationTokenStack, String text, int currentPosition, char closeToken) {
        Pair<Character, Integer> openingAggregationTokenPair = openingAggregationTokenStack.peek();
        char openingAggregationToken;
        if (openingAggregationTokenPair == null) {
            String message = String.format("Missing matching open token '%c' for '%c' at position %d.%nDid you forget to begin the subexpression?",
                    getMatchingOpenToken(closeToken),
                    closeToken,
                    currentPosition
            );
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(text, currentPosition));
        } else {
            openingAggregationToken = openingAggregationTokenPair.getKey();
        }

        if (getMatchingCloseToken(openingAggregationToken) != closeToken) {
            String message = String.format("Wrong open token '%c' for closing token '%c' at position %d.%nYou should close the subexpression with '%c' instead of '%2$c'.",
                    openingAggregationToken,
                    closeToken,
                    currentPosition,
                    getMatchingCloseToken(openingAggregationToken));
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(text, currentPosition));
        }
    }

    private void assertTokenStackIsEmpty(LinkedList<Pair<Character, Integer>> openeningAggregationTokenStack, String text) {
        Set<Integer> positions = openeningAggregationTokenStack.stream().map(Pair::getValue).collect(Collectors.toSet());

        if (!positions.isEmpty()) {
            String message = String.format("Encountered unmatched open tokens at positions %s.%nDid you forget to close some subexpressions?", positions.stream().sorted().map(Object::toString).collect(Collectors.joining(", ")));
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(text, positions));
        }
    }

}
