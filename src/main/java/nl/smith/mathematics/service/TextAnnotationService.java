package nl.smith.mathematics.service;

import javafx.util.Pair;
import nl.smith.mathematics.annotation.constraint.ConsistentTextAnnotationParameters;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalAuxiliaryFunctions;
import nl.smith.mathematics.util.ObjectWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Array;
import java.sql.SQLSyntaxErrorException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service to annotate texts.
 */
@Service
public class TextAnnotationService extends RecursiveValidatedService<TextAnnotationService> {

    private final static String SIBLING_BEAN_NAME = "TEXTANNOTATIONSERVICE";

    private char endOfLineCharacter = (char) 182;

    private boolean showEndOfLine = true;

    @Override
    public String getSiblingBeanName() {
        return SIBLING_BEAN_NAME;
    }

    @Bean(SIBLING_BEAN_NAME)
    @Override
    public TextAnnotationService makeSibling() {
        return new TextAnnotationService();
    }

    @ConsistentTextAnnotationParameters()
    public String getAnnotatedText(@NotEmpty(message = "Please specify a string to annotate.") String text,
                                   @NotEmpty(message = "Please specify one or more positions at which the text should be annotated.") int... position) {
        return sibling.getAnnotatedText(text, Arrays.stream(position).boxed().filter(p -> p >= 0).collect(Collectors.toSet()));
    }

    @ConsistentTextAnnotationParameters()
    public String getAnnotatedText(@NotEmpty(message = "Please specify a string to annotate.") String text,
                                   @NotEmpty(message = "Please specify one or more positions at which the text should be annotated.") Set<Integer> positions) {
        List<String> lines = Arrays.asList(text.split("\n")).stream().map(l -> l.concat(String.valueOf(endOfLineCharacter))).collect(Collectors.toList());

        List<String> annotatedTextLines = new ArrayList<>();

        ObjectWrapper<Integer> offSet = new ObjectWrapper<>(0);
        lines.forEach(line -> processLine(line, positions, offSet, annotatedTextLines));
        return String.join("\n", annotatedTextLines);
    }

    private void processLine(String line, Set<Integer> positions, ObjectWrapper<Integer> offSet, List<String> annotatedTextLines) {
        Set<Integer> relevantPositions = positions.stream().filter(p -> p.compareTo(offSet.getValue() + line.length()) < 0).collect(Collectors.toSet());
        positions.removeAll(relevantPositions);
        relevantPositions = relevantPositions.stream().map(p -> p - offSet.getValue()).collect(Collectors.toSet());
        StringBuffer annotationLine = new StringBuffer();
        boolean lineIsAnnotated = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (relevantPositions.contains(i)) {
                annotationLine.append('^');
                annotationLine.append(c == '\t' ? c : "");
                lineIsAnnotated = true;
            } else {
                annotationLine.append(c == '\t' ? c : ' ');
            }
        }


        annotatedTextLines.add(showEndOfLine ? line : line.substring(0, line.length() - 1));
        if (lineIsAnnotated) {
            annotatedTextLines.add(annotationLine.toString());
        }

        offSet.setValue(offSet.getValue() + line.length());
    }

}
