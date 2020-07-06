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

    @DisplayName("Testing getAnnotatedText with invalid arguments")
    @ParameterizedTest
    @MethodSource("textAndPositions")
    public void getAnnotatedText_preconditionsNotMet(String text, int[] position, Set<Pair<String, String>> expectedConstraintViolations) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> textAnnotationService.getAnnotatedText(text, position));

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        assertEquals(expectedConstraintViolations.size(), constraintViolations.size(), "The number of constraint violations actually thrown is not equal to the specified number.");

        Set<Pair<String, String>> actualConstraintViolations = constraintViolations.stream().map(cv -> new Pair<>(cv.getPropertyPath().toString(), cv.getMessage())).collect(Collectors.toSet());
        assertTrue(actualConstraintViolations.containsAll(expectedConstraintViolations),
                "Constraint path/messages violations thrown are not identical to that which are specified:\nActual constraint path/messages violations thrown:\n" +
                actualConstraintViolations.stream().map(acv -> acv.getKey() + "(" + acv.getValue() +")").collect(Collectors.joining("\n")) +
                "\n\nSpecified constraint path/messages violations:\n" +
                expectedConstraintViolations.stream().map(ecv -> ecv.getKey() + "(" + ecv.getValue() + ")").collect(Collectors.joining("\n")));
    }

    private static Stream<Arguments> textAndPositions() {
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
                Arguments.of("Hello world", null, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.position", "Please specify one or more positions at which the text should be annotated.")
                ))),
                // Empty position array
                Arguments.of("Hello world", new int[0], new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.position", "Please specify one or more positions at which the text should be annotated.")
                ))),
                // String with trailing white space character in line
                Arguments.of("Hello world\t", new int[]{4}, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.<cross-parameter>", "The provided text string contains empty lines or none empty lines with trailing white space characters or positions are not well defined.")
                ))),
                // String with trailing white space character in line
                Arguments.of("Hello world\t", new int[]{4}, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.<cross-parameter>", "The provided text string contains empty lines or none empty lines with trailing white space characters or positions are not well defined.")
                ))),
                // String with trailing white space character in line
                Arguments.of("Hello world", new int[]{-1}, new HashSet<>(Arrays.asList(
                        new Pair<>("getAnnotatedText.<cross-parameter>", "The provided text string contains empty lines or none empty lines with trailing white space characters or positions are not well defined.")
                )))
        );
    }

    @Test
    public void doIt() {
        String text = "Mijn naam is Mark Smith.\n\nIk woon in Geldermalsen.\nIk heb 2 kinderen\n \n\n\n\n\n";

        String[] expected = new String[]{"Mijn naam is Mark Smith.", "", "Ik woon in Geldermalsen.", "Ik heb 2 kinderen"};
        System.out.println('\n');
        System.out.println((int)'\n');
        assertArrayEquals(expected, text.split("\n"));
    }


}