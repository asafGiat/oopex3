package ascii_art;

/**
 * Exception thrown when a command is invalid or malformed.
 */
public class InvalidCommandException extends Exception {

    public InvalidCommandException(String message) {
        super(message);
    }
}

