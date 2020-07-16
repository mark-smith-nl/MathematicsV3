package nl.smith.mathematics.service;

import javafx.util.Pair;
import nl.smith.mathematics.exception.InValidExpressionStringException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExpressionDigestionServiceTest {

    private final ExpressionDigestionService expressionDigestionService;

    @Autowired
    public ExpressionDigestionServiceTest(ExpressionDigestionService expressionDigestionService) {
        this.expressionDigestionService = expressionDigestionService;
    }

    @Test
    public void exists() {
        assertNotNull(expressionDigestionService);
    }

    @ParameterizedTest
    @MethodSource("getMatchingOpenToken")
    public void getMatchingOpenToken(char c, char expectedOpenToken, Exception expectedException) {
        if (expectedException == null) {
            assertEquals(expectedOpenToken, ExpressionDigestionService.getMatchingOpenToken(c));
        } else {
            IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> ExpressionDigestionService.getMatchingOpenToken(c));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("getMatchingCloseToken")
    public void getMatchingCloseToken(char c, char expectedCloseToken, Exception expectedException) {
        if (expectedException == null) {
            assertEquals(expectedCloseToken, ExpressionDigestionService.getMatchingCloseToken(c));
        } else {
            IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> ExpressionDigestionService.getMatchingCloseToken(c));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("getCharacterType")
    public void getCharacterType(char c, ExpressionDigestionService.CharacterType expectedCharacterType) {
        assertEquals(expectedCharacterType, ExpressionDigestionService.getCharacterType(c));
    }

    @DisplayName("Testing digest(String) with invalid arguments")
    @ParameterizedTest
    @MethodSource("digest_preconditionsNotMet")
    public void digest_preconditionsNotMet(String text, Set<Pair<String, String>> expectedConstraintViolations) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> expressionDigestionService.digest(text));

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(expectedConstraintViolations.size(), constraintViolations.size(), "The number of constraint violations actually thrown is not equal to the specified number.");

        Set<Pair<String, String>> actualConstraintViolations = constraintViolations.stream().map(cv -> new Pair<>(cv.getPropertyPath().toString(), cv.getMessage())).collect(Collectors.toSet());
        assertTrue(actualConstraintViolations.containsAll(expectedConstraintViolations),
                "Constraint path/messages violations thrown are not identical to that which are specified:\nActual constraint path/messages violations thrown:\n" +
                        actualConstraintViolations.stream().map(acv -> acv.getKey() + "(" + acv.getValue() + ")").collect(Collectors.joining("\n")) +
                        "\n\nexpected constraint path/messages violations:\n" +
                        expectedConstraintViolations.stream().map(ecv -> ecv.getKey() + "(" + ecv.getValue() + ")").collect(Collectors.joining("\n")));
    }

    @DisplayName("Testing digest(String) with valid arguments but invalid use of open/close tokens")
    @ParameterizedTest
    @MethodSource("digest_invalidCloseTag")
    public void digest_invalidCloseTag(String text, InValidExpressionStringException expectedInValidExpressionStringException) {
        InValidExpressionStringException exception = assertThrows(InValidExpressionStringException.class, () -> expressionDigestionService.digest(text));

        assertEquals(expectedInValidExpressionStringException.getSimpelMessage(), exception.getSimpelMessage());
    }

    @DisplayName("Testing digest(String) with valid arguments but with uninitialized/empty/blank sibling or subexpressions")
    @ParameterizedTest
    @MethodSource("digest_uninitializedOrEmptyExpression")
    public void digest_uninitializedOrEmptyExpression(String text, InValidExpressionStringException expectedInValidExpressionStringException) {
        InValidExpressionStringException exception = assertThrows(InValidExpressionStringException.class, () -> expressionDigestionService.digest(text));

        assertEquals(expectedInValidExpressionStringException.getSimpelMessage(), exception.getSimpelMessage());
    }

    private static Stream<Arguments> getMatchingOpenToken() {
        return Stream.of(
                Arguments.of(')', '(', null),
                Arguments.of('}', '{', null),
                Arguments.of(']', '[', null),
                Arguments.of('(', ' ', new IllegalArgumentException("Character '(' is not a close token.")),
                Arguments.of('{', ' ', new IllegalArgumentException("Character '{' is not a close token.")),
                Arguments.of('[', ' ', new IllegalArgumentException("Character '[' is not a close token.")),
                Arguments.of('A', ' ', new IllegalArgumentException("Character 'A' is not a close token."))
        );
    }

    private static Stream<Arguments> getMatchingCloseToken() {
        return Stream.of(
                Arguments.of('(', ')', null),
                Arguments.of('{', '}', null),
                Arguments.of('[', ']', null),
                Arguments.of(')', ' ', new IllegalArgumentException("Character ')' is not an open token.")),
                Arguments.of('}', ' ', new IllegalArgumentException("Character '}' is not an open token.")),
                Arguments.of(']', ' ', new IllegalArgumentException("Character ']' is not an open token.")),
                Arguments.of('A', ' ', new IllegalArgumentException("Character 'A' is not an open token."))
        );
    }

    private static Stream<Arguments> getCharacterType() {
        return Stream.of(
                Arguments.of('(', ExpressionDigestionService.CharacterType.BEGIN_SUBEXPRESSION),
                Arguments.of('{', ExpressionDigestionService.CharacterType.BEGIN_SUBEXPRESSION),
                Arguments.of('[', ExpressionDigestionService.CharacterType.BEGIN_SUBEXPRESSION),
                Arguments.of(')', ExpressionDigestionService.CharacterType.END_SUBEXPRESSION),
                Arguments.of('}', ExpressionDigestionService.CharacterType.END_SUBEXPRESSION),
                Arguments.of(']', ExpressionDigestionService.CharacterType.END_SUBEXPRESSION),
                Arguments.of(',', ExpressionDigestionService.CharacterType.BEGIN_SIBLING),
                Arguments.of('A', ExpressionDigestionService.CharacterType.NORMAL)
        );
    }

    private static Stream<Arguments> digest_preconditionsNotMet() {
        return Stream.of(
                // Null text String
                Arguments.of(null, new HashSet<>(Arrays.asList(
                        new Pair<>("digest.text", "Please specify an expression.")
                ))),
                // Empty text string
                Arguments.of("", new HashSet<>(Arrays.asList(
                        new Pair<>("digest.text", "Please specify an expression.")
                ))),
                Arguments.of("2+3\t", new HashSet<>(Arrays.asList(
                        new Pair<>("digest.text", "The provided text has lines with trailing whitespace characters at position(s): 3.")
                ))),
                Arguments.of("2+3\n+5\t\n", new HashSet<>(Arrays.asList(
                        new Pair<>("digest.text", "The provided text has lines with trailing whitespace characters at position(s): 6.")
                )))
        );
    }

    private static Stream<Arguments> digest_invalidCloseTag() {
        return Stream.of(
                // Unmatched close token at position 6
                Arguments.of("2 + 3 )",
                        new InValidExpressionStringException("Missing matching open token '(' for ')' at position 6.\nDid you forget to begin the subexpression?", "Not specified annotated expression")),
                // Unmatched close token at position 13 (second line)
                Arguments.of("2 + 3 *\n\t\t\t4+9)+8",
                        new InValidExpressionStringException("Missing matching open token '(' for ')' at position 14.\nDid you forget to begin the subexpression?", "Not specified annotated expression")),
                // Wrong close token at position 13
                Arguments.of("2 + 3 * (6 - 2] + 4",
                        new InValidExpressionStringException("Wrong open token '(' for closing token ']' at position 14.\nYou should close the subexpression with ')' instead of ']'.", "Not specified annotated expression")),
                Arguments.of("2 + {3 * [ (6 -2) + 4",
                        new InValidExpressionStringException("Encountered unmatched open tokens at positions 4, 9.\nDid you forget to close some subexpressions?", "Not specified annotated expression"))
        );
    }

    private static Stream<Arguments> digest_uninitializedOrEmptyExpression() {
        return Stream.of(
                // Expected expression before position 8
                Arguments.of("1 + sum(,4+5)", new InValidExpressionStringException("Expected an expression before position 8.\nDid you forget to specify the expression?", "Not specified annotated expression")),
                // Blank sibling expression at position [8-8]
                Arguments.of("1 + sum( ,4+5)", new InValidExpressionStringException("Blank expression from [8-8].\nDid you forget to specify the expression?", "Not specified annotated expression")),
                // Blank sibling expression at position [8-9]
                Arguments.of("1 + sum(  ,4+5)", new InValidExpressionStringException("Blank expression from [8-9].\nDid you forget to specify the expression?", "Not specified annotated expression")),
                // Blank sibling expression (tab) at position [8-8]
                Arguments.of("1 + sum(\t,4+5)", new InValidExpressionStringException("Blank expression from [8-8].\nDid you forget to specify the expression?", "Not specified annotated expression")),
                // Blank sibling expression (two tabs) at position [8-9]
                Arguments.of("1 + sum(\t\t,4+5)", new InValidExpressionStringException("Blank expression from [8-9].\nDid you forget to specify the expression?", "Not specified annotated expression")),
                // Blank sibling expression (<CR> followed by two tabs) at position [8-9]
                Arguments.of("1 + sum(\n\t\t,4+5)", new InValidExpressionStringException("Blank expression from [8-10].\nDid you forget to specify the expression?", "Not specified annotated expression")),
                // Expected expression before position 5
                Arguments.of("1 + ()", new InValidExpressionStringException("Expected an expression before position 5.\nDid you forget to specify the expression?", "Not specified annotated expression")),
                // Blank subexpression at position [9-11]
                Arguments.of("1 + (   {   } )", new InValidExpressionStringException("Blank expression from [9-11].\nDid you forget to specify the expression?", "Not specified annotated expression")),
                // Blank subexpression with one subexpression at position [9-11]
                Arguments.of("1 + (   { 3  } )", new InValidExpressionStringException("Expression is blank and has only one subexpression.\nRemove unnecessary aggregation tokens at position 4 and 15", "Not specified annotated expression"))
        );
    }

}