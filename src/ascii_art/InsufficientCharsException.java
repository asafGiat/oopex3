package ascii_art;

/**
 * Exception thrown when attempting to run ASCII art generation with insufficient characters.
 */
public class InsufficientCharsException extends Exception {

    public InsufficientCharsException(String message) {
        super(message);
    }
}

