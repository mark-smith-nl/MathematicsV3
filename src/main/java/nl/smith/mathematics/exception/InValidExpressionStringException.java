package nl.smith.mathematics.exception;

public class InValidExpressionStringException extends RuntimeException {

    private final String simpelMessage;

    private final String annotatedText;

    public InValidExpressionStringException(String simpelMessage, String annotatedText) {
        super(simpelMessage + "\n" + annotatedText);
        this.simpelMessage = simpelMessage;
        this.annotatedText = annotatedText;
    }

    public String getSimpelMessage() {
        return simpelMessage;
    }

    public String getAnnotatedText() {
        return annotatedText;
    }
}
