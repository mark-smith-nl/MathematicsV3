package nl.smith.mathematics.service;

import nl.smith.mathematics.annotation.constraint.ConsistentTextAnnotationParameters;
import nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal.BigDecimalAuxiliaryFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service to annotate texts.
 */
@Service
public class TextAnnotationService extends RecursiveValidatedService<TextAnnotationService> {

    private final static String SIBLING_BEAN_NAME = "TEXTANNOTATIONSERVICE";

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
    public String getAnnotatedText(@NotEmpty(message = "Please specify a string to annotate.") String text, @NotNull(message = "Please specify one or more positions at which the text should be annotated.") int ... position) {
        return sibling.getAnnotatedText(text, Arrays.stream(position).boxed().filter(p -> p >= 0).collect(Collectors.toSet()));
    }

    @ConsistentTextAnnotationParameters()
    public String getAnnotatedText(@NotEmpty(message = "Please specify a string to annotateeeee.") String text,
                                    @NotEmpty(message = "Please specify one or more positions at which the text should be annotated.") Set<Integer> position) {


        return text;
    }


}
