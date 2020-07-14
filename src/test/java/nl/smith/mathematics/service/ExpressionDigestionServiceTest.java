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

    @DisplayName("Testing digest(String) with invalid arguments")
    @ParameterizedTest
    @MethodSource("invalidInput")
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
    @MethodSource("invalidCloseTag")
    public void digest_invalidCloseTag(String text, InValidExpressionStringException expectedInValidExpressionStringException) {
        InValidExpressionStringException exception = assertThrows(InValidExpressionStringException.class, () -> expressionDigestionService.digest(text));

        assertEquals(expectedInValidExpressionStringException.getSimpelMessage(), exception.getSimpelMessage());
    }

    private static Stream<Arguments> invalidInput() {
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

    private static Stream<Arguments> invalidCloseTag() {
        return Stream.of(
                // Unmatched close token at position 6
                Arguments.of("2 + 3 )",
                        new InValidExpressionStringException("Missing matching open token '(' for ')' at position 6.\nDid you forget to begin the subexpression?", "Not specified annotated expression")),
                // Unmatched close token at position 13 (second line)
                Arguments.of("2 + 3*\n\t\t\t4+9)+8",
                        new InValidExpressionStringException("Missing matching open token '(' for ')' at position 13.\nDid you forget to begin the subexpression?", "Not specified annotated expression")),
                // Wrong close token at position 13
                Arguments.of("2 + 3 * (6 -2] + 4",
                        new InValidExpressionStringException("Wrong open token '(' for closing token ']' at position 13.\nYou should close the subexpression with ')' instead of ']'.", "Not specified annotated expression")),
                Arguments.of("2 + {3 * [ (6 -2) + 4",
                        new InValidExpressionStringException("Encounted unmatched open tokens at positions 4, 9.\nDid you forget to close some subexpressions?", "Not specified annotated expression"))
        );
    }

    private static Stream<Arguments> emptyExpression() {
        return Stream.of(
                // Unmatched close token at position 6
                Arguments.of("2 + 3 )",
                        new InValidExpressionStringException("Missing matching open token '(' for ')' at position 6.\nDid you forget to begin the subexpression?", "Not specified annotated expression")),
                // Unmatched close token at position 13 (second line)
                Arguments.of("2 + 3*\n\t\t\t4+9)+8",
                        new InValidExpressionStringException("Missing matching open token '(' for ')' at position 13.\nDid you forget to begin the subexpression?", "Not specified annotated expression")),
                // Wrong close token at position 13
                Arguments.of("2 + 3 * (6 -2] + 4",
                        new InValidExpressionStringException("Wrong open token '(' for closing token ']' at position 13.\nYou should close the subexpression with ')' instead of ']'.", "Not specified annotated expression")),
                Arguments.of("2 + {3 * [ (6 -2) + 4",
                        new InValidExpressionStringException("Encounted unmatched open tokens at positions 4, 9.\nDid you forget to close some subexpressions?", "Not specified annotated expression"))
        );
    }

}