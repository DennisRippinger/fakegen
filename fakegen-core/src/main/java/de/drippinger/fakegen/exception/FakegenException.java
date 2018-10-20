package de.drippinger.fakegen.exception;

/**
 * @author Dennis Rippinger
 */
public class FakegenException extends RuntimeException {

    public FakegenException(String message) {
        super(message);
    }

    public FakegenException(String message, Throwable cause) {
        super(message, cause);
    }

}
