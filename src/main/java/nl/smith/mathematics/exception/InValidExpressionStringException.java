package nl.smith.mathematics.exception;

import java.util.Objects;

import static java.lang.System.lineSeparator;

public class InValidExpressionStringException extends RuntimeException {

    private final String simpleMessage;

    private final String annotatedText;

    public InValidExpressionStringException(String simpleMessage, String annotatedText) {
        super(simpleMessage + lineSeparator() + annotatedText);
        this.simpleMessage = simpleMessage;
        this.annotatedText = annotatedText;
    }

    public String getSimpleMessage() {
        return simpleMessage;
    }

    public String getAnnotatedText() {
        return annotatedText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InValidExpressionStringException)) return false;
        InValidExpressionStringException that = (InValidExpressionStringException) o;
        return Objects.equals(simpleMessage, that.simpleMessage) && Objects.equals(annotatedText, that.annotatedText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simpleMessage, annotatedText);
    }
}
