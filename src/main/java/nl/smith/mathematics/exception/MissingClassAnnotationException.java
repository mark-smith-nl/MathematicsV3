package nl.smith.mathematics.exception;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;

public class MissingClassAnnotationException extends IllegalStateException {

    public MissingClassAnnotationException(Class<?> annotation, Class<?> sourceClass) {
        super(String.format("Missing annotation.\n.Please annotate your %s (abstract) subclass mathematical function container with the %s annotation describing the name and context of your mathematical function set.",
                sourceClass.getCanonicalName(), annotation.getCanonicalName()));
    }
}
