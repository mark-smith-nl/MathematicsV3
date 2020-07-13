package nl.smith.mathematics.service;

import javafx.util.Pair;
import nl.smith.domain.RawExpression;
import nl.smith.mathematics.annotation.constraint.TextWithoutLinesWithTrailingBlanks;
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

    public RawExpression digest(@NotBlank @TextWithoutLinesWithTrailingBlanks String text) {
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
                    assertIsNotEmptyAndProperlyClosed(openeningAggregationTokenStack, text, i, c);
                    assert rawExpression != null;
                    assertIsNotEmpty(rawExpression, text, i);

                    openeningAggregationTokenStack.pop(); // Remove the opening token from the token stack.
                    rawExpression.setEndsAt(i);
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
        if (rawExpression.isEmpty()) {
            Set<Integer> positions = new HashSet<>(Arrays.asList(rawExpression.getStartsAt(), currentPosition));
            String message = String.format("Blank expression.%nDid you forget to specify the expression?%n%s",
                    textAnnotationService.getAnnotatedText(text, positions));
            throw new IllegalArgumentException(message);
        }
    }

    private void assertIsNotEmptyAndProperlyClosed(LinkedList<Pair<Character, Integer>> openingAggregationTokenStack, String text, int currentPosition, char currentCharacter) {
        Pair<Character, Integer> openingAggregationTokenPair = openingAggregationTokenStack.peek();
        char openingAggregationToken;
        int openingAggregationTokenPosition;
        if (openingAggregationTokenPair == null) {
            String message = String.format("Missing matching open token '%c' for '%c'.%nnDid you forget to begin the subexpression?%n%s",
                    getMatchingOpenToken(currentCharacter),
                    currentCharacter,
                    textAnnotationService.getAnnotatedText(text, currentPosition));
            throw new IllegalArgumentException(message);
        } else {
            openingAggregationToken = openingAggregationTokenPair.getKey();
            openingAggregationTokenPosition = openingAggregationTokenPair.getValue();
        }

        if (getMatchingCloseToken(openingAggregationToken) != currentCharacter) {
            String message = String.format("Wrong open token '%c' for closing token'%c'.%nYou should close the subexpression with '%c' instead of '%2$c'%n%s",
                    openingAggregationToken,
                    currentCharacter,
                    getMatchingCloseToken(openingAggregationToken),
                    textAnnotationService.getAnnotatedText(text, openingAggregationTokenPosition, currentPosition));
            throw new IllegalArgumentException(message);
        }
    }

    private void assertIsEmpty(LinkedList<Pair<Character, Integer>> openeningAggregationTokenStack, String text) {
        Set<Integer> positions = openeningAggregationTokenStack.stream().map(Pair::getValue).collect(Collectors.toSet());
        if (!positions.isEmpty()) {
            String message = String.format("Encounted unmatched open tokens.%nDid you forget to close some subexpressions?%n%s",
                    textAnnotationService.getAnnotatedText(text, positions));
            throw new IllegalArgumentException(message);
        }
    }

}
