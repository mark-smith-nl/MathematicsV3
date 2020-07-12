package nl.smith.mathematics.service;

import nl.smith.mathematics.annotation.constraint.CharacterPositionsInRange;
import nl.smith.mathematics.annotation.constraint.LineWithoutTrailingBlanks;
import nl.smith.mathematics.annotation.constraint.TextWithoutLinesWithTrailingBlanks;
import nl.smith.mathematics.util.ObjectWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.validation.constraints.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service to annotate texts.
 */
@Service
public class TextAnnotationService extends RecursiveValidatedService<TextAnnotationService> {

    private final static String SIBLING_BEAN_NAME = "TEXTANNOTATIONSERVICE";

    private char endOfLineCharacter = (char) 182;

    private boolean showEndOfLine = true;

    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_RED = "\u001B[31m";

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    @Override
    public TextAnnotationService makeSibling() {
        return new TextAnnotationService();
    }

    public String getAnnotatedText(@NotBlank(message = "Please specify a string to annotate.") @TextWithoutLinesWithTrailingBlanks String text,
                                   @NotEmpty(message = "Please specify one or more positions at which the text should be annotated.") int... position) {
        return sibling.getAnnotatedText(text, Arrays.stream(position).boxed().collect(Collectors.toSet()));
    }

    @CharacterPositionsInRange()
    public String getAnnotatedText(@NotBlank(message = "Please specify a string to annotate.") @TextWithoutLinesWithTrailingBlanks String text,
                                   @NotEmpty(message = "Please specify one or more positions at which the text should be annotated.") Set<@NotNull @Min(value = 0, message = "Negative positions (${validatedValue}) are not allowed.") Integer> positions) {
        return sibling.getAnnotatedText(split(text), positions);
    }

    //TODO Create tests
    /**
     * Protected for validation purposes. Note: private functions will never be validated when called by a sibling service.
     */
    protected String getAnnotatedText(List<@NotBlank(message = "Line element can not be blank.") @LineWithoutTrailingBlanks String> lines, Set<Integer> positions) {
        List<String> annotatedTextLines = new ArrayList<>();
        List<String> linesWithEndOfLineCharacter = lines.stream().map(l -> l.concat(String.valueOf(endOfLineCharacter))).collect(Collectors.toList());
        ObjectWrapper<Integer> offSet = new ObjectWrapper<>(0);
        linesWithEndOfLineCharacter.forEach(line -> processLine(line, positions, offSet, annotatedTextLines));
        return String.join("\n", annotatedTextLines);
    }

    private static List<String> split(String text) {
        List<StringBuilder> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        lines.add(line);
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                line = new StringBuilder();
                lines.add(line);
            } else {
                line.append(text.charAt(i));
            }
        }

        if (lines.get(lines.size() - 1).length() == 0) {
            lines.remove(lines.size() - 1);
        }

        return lines.stream().map(StringBuilder::toString).collect(Collectors.toList());
    }

    private void processLine(String line, Set<Integer> positions, ObjectWrapper<Integer> offSet, List<String> annotatedTextLines) {
        Set<Integer> relevantPositions = positions.stream().filter(p -> p.compareTo(offSet.getValue() + line.length()) < 0).collect(Collectors.toSet());
        positions.removeAll(relevantPositions);
        relevantPositions = relevantPositions.stream().map(p -> p - offSet.getValue()).collect(Collectors.toSet());
        StringBuffer annotationLine = new StringBuffer();
        boolean lineIsAnnotated = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (relevantPositions.contains(i) && c != '\t') {
                annotationLine.append('^');
                annotationLine.append("");
                lineIsAnnotated = true;
            } else {
                annotationLine.append(c == '\t' ? c : ' ');
            }
        }

        annotatedTextLines.add(showEndOfLine ? line : line.substring(0, line.length() - 1));
        if (lineIsAnnotated) {
            annotatedTextLines.add(ANSI_RED + annotationLine.toString() + ANSI_RESET);
        }

        offSet.setValue(offSet.getValue() + line.length());
    }

    public static void main(String[] args) {

        String input = "\tgd\ngjghgjgjgj\nfffff";

        System.out.println(input.matches("(.*\\S\n)*(.*\\S\n?)"));
    }
}
