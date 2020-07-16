package nl.smith.mathematics.exception;

public class InValidExpressionStringException extends RuntimeException {

    private final String simpleMessage;

    private final String annotatedText;

    public InValidExpressionStringException(String simpleMessage, String annotatedText) {
        super(simpleMessage + "\n" + annotatedText);
        this.simpleMessage = simpleMessage;
        this.annotatedText = annotatedText;
    }

    public String getSimpleMessage() {
        return simpleMessage;
    }

    public String getAnnotatedText() {
        return annotatedText;
    }
}
