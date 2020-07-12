package nl.smith.mathematics.service;

import javafx.util.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TextAnnotationServiceTest {

    private final TextAnnotationService textAnnotationService;

    @Autowired
    public TextAnnotationServiceTest(TextAnnotationService textAnnotationService) {
        this.textAnnotationService = textAnnotationService;
    }

    @Test
    public void exists() {
        assertNotNull(textAnnotationService);
    }

    @DisplayName("Testing getAnnotatedText(String, int[]) with invalid arguments")
    @ParameterizedTest
    @MethodSource("invalidTextAndPositionsUsingIntegerArray")
    public void getAnnotatedText_usingIntegerArray_preconditionsNotMet(String text, int[] position, Set<Pair<String, String>> expectedConstraintViolations) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> textAnnotationService.getAnnotatedText(text, position));

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(expectedConstraintViolations.size(), constraintViolations.size(), "The number of constraint violations actually thrown is not equal to the specified number.");

        Set<Pair<String, String>> actualConstraintViolations = constraintViolations.stream().map(cv -> new Pair<>(cv.getPropertyPath().toString(), cv.getMessage())).collect(Collectors.toSet());
        assertTrue(actualConstraintViolations.containsAll(expectedConstraintViolations),
                "Constraint path/messages violations thrown are not identical to that which are specified:\nActual constraint path/messages violations thrown:\n" +
                        actualConstraintViolations.stream().map(acv -> acv.getKey() + "(" + acv.getValue() + ")").collect(Collectors.joining("\n")) +
                        "\n\nexpected constraint path/messages violations:\n" +
                        expectedConstraintViolations.stream().map(ecv -> ecv.getKey() + "(" + ecv.getValue() + ")").collect(Collectors.joining("\n")));
    }

    @DisplayName("Testing getAnnotatedText(String, Set<Integer>) with invalid arguments")
    @ParameterizedTest
    @MethodSource("invalidTextAndPositionsUsingIntegerSet")
    public void getAnnotatedText_usingIntegerSet_preconditionsNotMet(String text, Set<Integer> positions, Set<Pair<String, String>> expectedConstraintViolations) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> textAnnotationService.getAnnotatedText(text, positions));

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(expectedConstraintViolations.size(), constraintViolations.size(), "The number of constraint violations actually thrown is not equal to the specified number.");

        Set<Pair<String, String>> actualConstraintViolations = constraintViolations.stream().map(cv -> new Pair<>(cv.getPropertyPath().toString(), cv.getMessage())).collect(Collectors.toSet());
        assertTrue(actualConstraintViolations.containsAll(expectedConstraintViolations),
                "Constraint path/messages violations thrown are not identical to that which are specified:\nActual constraint path/messages violations thrown:\n" +
                        actualConstraintViolations.stream().map(acv -> acv.getKey() + "(" + acv.getValue() + ")").collect(Collectors.joining("\n")) +
                        "\n\nExpected constraint path/messages violations:\n" +
                        expectedConstraintViolations.stream().map(ecv -> ecv.getKey() + "(" + ecv.getValue() + ")").collect(Collectors.joining("\n")));
    }

    private static Stream<Arguments> invalidTextAndPositionsUsingIntegerArray() {
        return Stream.of(
                // Null text String and null position array
                Arguments.of(null, null, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.text", "Please specify a string to annotate."),
                        new Pair<>("getAnnotatedText.position", "Please specify one or more positions at which the text should be annotated.")
                ))),
                // Empty text string and null position array
                Arguments.of("", null, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.text", "Please specify a string to annotate."),
                        new Pair<>("getAnnotatedText.position", "Please specify one or more positions at which the text should be annotated.")
                ))),
                //Null position array
                Arguments.of("Hello world1", null, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.position", "Please specify one or more positions at which the text should be annotated.")
                ))),
                // Empty position array
                Arguments.of("Hello world2", new int[0], new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.position", "Please specify one or more positions at which the text should be annotated.")
                ))),
                // String with trailing white space character in line
                Arguments.of("Hello world3\t", new int[]{4}, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.text", "The provided text has lines with trailing whitespace characters at position(s): 12.")
                ))),
                // String with trailing multiple newlines
                Arguments.of("Hello world4\n\n", new int[]{4}, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.text", "The provided text has lines with trailing whitespace characters at position(s): 12.")
                ))),
                // Valid String, negative position to be annotated
                Arguments.of("Hello world5", new int[]{-5, -10}, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.positions[].<iterable element>", "Negative positions (-5) are not allowed."),
                        new Pair<>("getAnnotatedText.positions[].<iterable element>", "Negative positions (-10) are not allowed.")
                ))),
                // Valid String, position to be annotated out of range
                Arguments.of("Hello world6", new int[]{100, 33}, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.positions", "Supplied positions contain values(33, 100) larger than or equal to the size of the provided string (12).")
                )))
        );
    }

    private static Stream<Arguments> invalidTextAndPositionsUsingIntegerSet() {
        return Stream.of(
                // Null text String and null position array
                Arguments.of(null, null, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.text", "Please specify a string to annotate."),
                        new Pair<>("getAnnotatedText.positions", "Please specify one or more positions at which the text should be annotated.")
                ))),
                // Empty text string and null position array
                Arguments.of("", null, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.text", "Please specify a string to annotate."),
                        new Pair<>("getAnnotatedText.positions", "Please specify one or more positions at which the text should be annotated.")
                ))),
                //Null position array
                Arguments.of("Hello world1", null, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.positions", "Please specify one or more positions at which the text should be annotated.")
                ))),
                // Empty position set
                Arguments.of("Hello world2", new HashSet<Integer>(), new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.positions", "Please specify one or more positions at which the text should be annotated.")
                ))),
                // String with trailing white space character in line
                Arguments.of("Hello world3\t", new HashSet(Arrays.asList(4)), new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.text", "The provided text has lines with trailing whitespace characters at position(s): 12.")
                ))),
                // String with trailing multiple newlines
                Arguments.of("Hello world4\n\n", new HashSet(Arrays.asList(4)), new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.text", "The provided text has lines with trailing whitespace characters at position(s): 12.")
                ))),
                // Valid String, negative position to be annotated
                Arguments.of("Hello world5", new HashSet(Arrays.asList(-5, -10)), new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.positions[].<iterable element>", "Negative positions (-5) are not allowed."),
                        new Pair<>("getAnnotatedText.positions[].<iterable element>", "Negative positions (-10) are not allowed.")
                ))),
                // Valid String, position to be annotated out of range
                Arguments.of("Hello world6", new HashSet(Arrays.asList(100, 33)), new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.positions", "Supplied positions contain values(33, 100) larger than or equal to the size of the provided string (12).")
                )))
        );
    }

}