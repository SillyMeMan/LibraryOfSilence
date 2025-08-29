package net.vinh.library_of_silence.exception;

public class InvalidContextException extends RuntimeException {
    public InvalidContextException() {
        super("Invalid IDamageContext implementation instance used");
    }

    public InvalidContextException(String message) {
        super(message);
    }
}
