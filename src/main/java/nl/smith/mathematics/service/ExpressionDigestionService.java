package nl.smith.mathematics.service;

import nl.smith.mathematics.annotation.constraint.TextWithoutReservedCharacters;
import nl.smith.mathematics.domain.ExpressionStack;
import nl.smith.mathematics.domain.MathematicalFunctionMethodMapping;
import nl.smith.mathematics.domain.RawExpression;
import nl.smith.mathematics.annotation.constraint.TextWithoutLinesWithTrailingBlanks;
import nl.smith.mathematics.exception.InValidExpressionStringException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Service to interpret a mathematical expression string.
 */
@Service
public class ExpressionDigestionService extends RecursiveValidatedService<ExpressionDigestionService> {

    private static final String SIBLING_BEAN_NAME = "EXPRESSION_DIGESTION_SERVICE";

    private final TextAnnotationService textAnnotationService;

    private final MethodRunnerService methodRunnerService;

    private static final Set<SimpleEntry<Character, Character>> AGGREGATION_TOKEN_PAIRS = new HashSet<>(Arrays.asList(
            new SimpleEntry<>('(', ')'),
            new SimpleEntry<>('{', '}')));

    private static final char START_SIBLING_CHARACTER = ',';

    public ExpressionDigestionService(TextAnnotationService textAnnotationService, MethodRunnerService methodRunnerService) {
        this.textAnnotationService = textAnnotationService;
        this.methodRunnerService = methodRunnerService;
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
        return new ExpressionDigestionService(textAnnotationService, methodRunnerService);
    }

    public <N extends Number> N getResult(@NotNull Class<N> numberType, String text, Map<String, N> variables) {
        RawExpression rawExpression = sibling.getRawExpression(text);

        Set<String> unaryOperatorChars = methodRunnerService.getUnaryArithmeticMethodsForNumberType(numberType).stream().map(MathematicalFunctionMethodMapping::getName).collect(Collectors.toSet());
        Set<String> binaryOperatorChars = methodRunnerService.getBinaryArithmeticMethodsForNumberType(numberType).stream().map(MathematicalFunctionMethodMapping::getName).collect(Collectors.toSet());
        String numberPattern = null;
        ExpressionStack<N> expressionStack = rawExpression.getExpressionStack(numberType, unaryOperatorChars, binaryOperatorChars, numberPattern);
        ExpressionStack<N> digest = expressionStack.digest(variables);
        return null;
    }

    /**
     * Method to build a raw expression tree.
     * Protected for test purposes.
     */
    protected RawExpression getRawExpression(@NotBlank(message = "Please specify an expression.") @TextWithoutLinesWithTrailingBlanks @TextWithoutReservedCharacters String text) {
        RawExpression rawExpression = new RawExpression(text); // Note: the expression has not been initialized.
        RawExpression mainRawExpression = rawExpression;
        LinkedList<SimpleEntry<Character, Integer>> openingAggregationTokenStack = new LinkedList<>();

        int position;
        for (position = 0; position < text.length(); position++) {
            char c = text.charAt(position);
            switch (getCharacterType(c)) {
                case NORMAL:
                    if (!rawExpression.isStartPositionSet()) {
                        rawExpression.startExpressionAtPosition(position);
                    }

                    break;
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

                    openingAggregationTokenStack.push(new SimpleEntry<>(c, position));
                    break;
                case END_SUBEXPRESSION:
                    // Validate proper nesting of aggregation tokens.
                    assertTokenStackIsNotEmptyAndClosedTokenMatchesOpenToken(openingAggregationTokenStack, text, position, c);
                    // Ending a subexpression implicates that the subexpression was initialized.
                    assertRawExpressionInitializedAndNotEmpty(rawExpression, position);
                    rawExpression = rawExpression.setEndPosition(position);


                    openingAggregationTokenStack.pop(); // Remove the opening token from the token stack.

                    break;
            }
        }

        assertTokenStackIsEmpty(openingAggregationTokenStack, text);
        assertRawExpressionInitializedAndNotEmpty(rawExpression, position);
        rawExpression.setEndPosition(position);

        return mainRawExpression;
    }

    /**
     * Protected for test purposes.
     */
    protected static char getMatchingOpenToken(char c) {
        for (SimpleEntry<Character, Character> atp : AGGREGATION_TOKEN_PAIRS) {
            if (atp.getValue() == c) {
                return atp.getKey();
            }
        }

        throw new IllegalArgumentException(format("Character '%c' is not a close token.", c));
    }

    /**
     * Protected for test purposes.
     */
    protected static char getMatchingCloseToken(char c) {
        for (SimpleEntry<Character, Character> atp : AGGREGATION_TOKEN_PAIRS) {
            if (atp.getKey() == c) {
                return atp.getValue();
            }
        }
        throw new IllegalArgumentException(format("Character '%c' is not an open token.", c));
    }

    /**
     * Protected for test purposes.
     */
    protected static CharacterType getCharacterType(char c) {
        if (c == START_SIBLING_CHARACTER) {
            return CharacterType.BEGIN_SIBLING;
        }

        for (SimpleEntry<Character, Character> atp : AGGREGATION_TOKEN_PAIRS) {
            if (atp.getKey() == c) {
                return CharacterType.BEGIN_SUBEXPRESSION;
            }
            if (atp.getValue() == c) {
                return CharacterType.END_SUBEXPRESSION;
            }
        }

        return CharacterType.NORMAL;
    }

    /**
     * <pre>
     *     Method throws an exception in the following cases:
     *
     *     - The raw expression has not been initialized
     *     - The raw expression contains only blank characters
     *
     *     If no exception is thrown the expression is eligible for termination.
     * </pre>
     */
    private void assertRawExpressionInitializedAndNotEmpty(RawExpression rawExpression , int position) {

        if (!rawExpression.isStartPositionSet()) {
            Set<Integer> positions = new HashSet<>(Collections.singletonList(position));
            String message = format("Expected an expression before position %d.%nDid you forget to specify the expression?", position);
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(rawExpression.getText(), positions));
        }

        if (rawExpression.isBlank(position)) {
            Set<Integer> positions = new HashSet<>(Arrays.asList(rawExpression.getStartPosition(), position - 1));
            String message = format("Blank expression from [%d-%d].%nDid you forget to specify the expression?",
                    rawExpression.getStartPosition(),
                    position - 1);
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(rawExpression.getText(), positions));
        }

        if (rawExpression.hasSubExpressionsButNoContent(position)) {
            Set<Integer> positions = new HashSet<>(Arrays.asList(rawExpression.getStartPosition(), position - 1));
            String message = format("Essentially blank expression (contains only subexpressions) from [%d-%d].%nRemove unnecessary aggregation tokens and blank characters.", rawExpression.getStartPosition(), position);
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(rawExpression.getText(), positions));
        }
    }

    private void assertTokenStackIsNotEmptyAndClosedTokenMatchesOpenToken(LinkedList<SimpleEntry<Character, Integer>> openingAggregationTokenStack, String text, int currentPosition, char closeToken) {
        SimpleEntry<Character, Integer> openingAggregationTokenPair = openingAggregationTokenStack.peek();
        char openingAggregationToken;
        if (openingAggregationTokenPair == null) {

            String message = format("Missing matching open token '%c' for '%c' at position %d.%nDid you forget to begin the subexpression?",
                    getMatchingOpenToken(closeToken),
                    closeToken,
                    currentPosition
            );
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(text, currentPosition));
        } else {
            openingAggregationToken = openingAggregationTokenPair.getKey();
        }

        if (getMatchingCloseToken(openingAggregationToken) != closeToken) {
            String message = format("Wrong open token '%c' for closing token '%c' at position %d.%nYou should close the subexpression with '%c' instead of '%2$c'.",
                    openingAggregationToken,
                    closeToken,
                    currentPosition,
                    getMatchingCloseToken(openingAggregationToken));
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(text, currentPosition));
        }
    }

    private void assertTokenStackIsEmpty(LinkedList<SimpleEntry<Character, Integer>> openingAggregationTokenStack, String text) {
        Set<Integer> positions = openingAggregationTokenStack.stream().map(SimpleEntry::getValue).collect(Collectors.toSet());

        if (!positions.isEmpty()) {
            String message = format("Encountered unmatched open tokens at positions %s.%nDid you forget to close some subexpressions?", positions.stream().sorted().map(Object::toString).collect(Collectors.joining(", ")));
            throw new InValidExpressionStringException(message, textAnnotationService.getAnnotatedText(text, positions));
        }
    }

}
