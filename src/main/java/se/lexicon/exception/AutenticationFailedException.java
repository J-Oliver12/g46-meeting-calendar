package se.lexicon.exception;

public class AutenticationFailedException extends Exception {
    public AutenticationFailedException(String message) {
        super(message);
    }

    public AutenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
