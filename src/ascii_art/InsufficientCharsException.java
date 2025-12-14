package ascii_art;

/**
 * Thrown when attempting to generate ASCII art with an insufficient charset size.
 * The application requires at least two distinct characters to produce meaningful output.
 */
public class InsufficientCharsException extends Exception {

    /**
     * Creates a new InsufficientCharsException with a descriptive message.
     *
     * @param message details about the cause (e.g., current charset size)
     */
    public InsufficientCharsException(String message) {
        super(message);
    }
}
