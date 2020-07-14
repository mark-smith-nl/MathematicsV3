package nl.smith.mathematics.service;

import javafx.util.Pair;
import nl.smith.domain.RawExpression;
import nl.smith.mathematics.annotation.constraint.TextWithoutLinesWithTrailingBlanks;
import nl.smith.mathematics.exception.InValidExpressionStringException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service to interpret mathematical expressions.
 */
@Service
public class ExpressionDigestionService extends RecursiveValidatedService<ExpressionDigestionService> {

    private static final String SIBLING_BEAN_NAME = "EXPRESSIONDIGESTIONSERVICE";

    private final TextAnnotationService textAnnotationService;

    private static final Set<Pair<Character, Character>> AGGREGATION_TOKEN_PAIRS = new HashSet<>(Arrays.asList(
            new Pair<Character, Character>('(', ')'),
            new Pair<Character, Character>('{', '}'),
            new Pair<Character, Character>('[', ']')));

    private static final char START_SIBLING_CHARACTER = ',';

    public ExpressionDigestionService(TextAnnotationService textAnnotationService) {
        this.textAnnotationService = textAnnotationService;
    }

    private enum CharacterType {
        NORMAL,
        BEGIN_SIBLING,
        BEGIN_SUBEXPRESSION,
        END_SUBEXPRESSION
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


    public RawExpression digest(@NotBlank(message = "Please specify an expression.") @TextWithoutLinesWithTrailingBlanks String text) {
        RawExpression rawExpression = new RawExpression();
        LinkedList<RawExpression> rawExpressionStack = new LinkedList<>();
        rawExpressionStack.push(rawExpression);
        LinkedList<Pair<Character, Integer>> openeningAggregationTokenStack = new LinkedList<>();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (getCharacterType(c)) {
                case NORMAL:
                    rawExpression.addCharacter(c);
                    break;
                case BEGIN_SIBLING:
                    rawExpression.setEndsAt(i);
                    assertIsNotEmpty(rawExpression, text, i);
                    RawExpression previousRawExpression = rawExpressionStack.pop(); // Remove the previous sibling from the stack.
                    rawExpression = new RawExpression(i + 1); // Note: the expressions starts after START_SIBLING_CHARACTER.
                    logger.debug("Starting sibling at position {}.", rawExpression.getStartsAt());
                    rawExpressionStack.push(rawExpression); // Add the new sibling to the stack.
                    previousRawExpression.setSibling(rawExpression); // Linking the new sibling expression.
                    break;
                case BEGIN_SUBEXPRESSION:
                    openeningAggregationTokenStack.push(new Pair<Character, Integer>(c, i));
                    rawExpression = new RawExpression(i + 1); // Note: the expressions starts after the open token.
                    logger.debug("Starting subexpression at position {}.", rawExpression.getStartsAt());
                    rawExpressionStack.peek().addSubExpression(rawExpression);
                    rawExpressionStack.push(rawExpression); // Add the new (sub) expression) to the stack.
                    break;
                case END_SUBEXPRESSION:
                    logger.debug("Ending subexpression at position {}.", i);
                    rawExpression.setEndsAt(i);
                    assertIsNotEmptyAndProperlyClosed(openeningAggregationTokenStack, text, i, c);
                    assertIsNotEmpty(rawExpression, text, i);

                    openeningAggregationTokenStack.pop(); // Remove the opening token from the token stack.

                    rawExpressionStack.pop(); // Remove the (sub) expression) from the stack.

                    rawExpression = rawExpressionStack.peek(); // Retrieve the parent expression from the stack.
                    break;
            }
        }

        assertIsEmpty(openeningAggregationTokenStack, text);


        rawExpression.setEndsAt(text.length() - 1);

        return rawExpression;

    }

    private static char getMatchingOpenToken(char c) {
        for (Pair<Character, Character> atp : AGGREGATION_TOKEN_PAIRS) {
            if (atp.getValue() == c) {
                return atp.getKey();
            }
        }

        throw new IllegalStateException(String.format("Character '%c' is not a close token.", c));
    }

    private static char getMatchingCloseToken(char c) {
        for (Pair<Character, Character> atp : AGGREGATION_TOKEN_PAIRS) {
            if (atp.getKey() == c) {
                return atp.getValue();
            }
        }
        throw new IllegalStateException(String.format("Character '%c' is not an open token.", c));
    }

    private static CharacterType getCharacterType(char c) {
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
        return CharacterType.NORMAL;
    }

    private void assertIsNotEmpty(RawExpression rawExpression, String text, int currentPosition) {
        if (rawExpression.isBlank()) {
            //TODO fixme
            Set<Integer> positions = new HashSet<>(Arrays.asList(rawExpression.getStartsAt(), currentPosition - 1));
            String message = String.format("Blank expression from %d-%d.%nDid you forget to specify the expression?.", rawExpression.getStartsAt(), currentPosition - 1);
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(text, positions));
        }
    }

    private void assertIsNotEmptyAndProperlyClosed(LinkedList<Pair<Character, Integer>> openingAggregationTokenStack, String text, int currentPosition, char closeToken) {
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

    private void assertIsEmpty(LinkedList<Pair<Character, Integer>> openeningAggregationTokenStack, String text) {
        Set<Integer> positions = openeningAggregationTokenStack.stream().map(Pair::getValue).collect(Collectors.toSet());

        if (!positions.isEmpty()) {
            String message = String.format("Encounted unmatched open tokens at positions %s.%nDid you forget to close some subexpressions?", positions.stream().sorted().map(p -> p.toString()).collect(Collectors.joining(", ")));
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(text, positions));
        }
    }

}
