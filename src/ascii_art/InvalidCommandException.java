package ascii_art;

/**
 * Thrown when a user command or argument does not match the expected format.
 * Used in the shell parsing utilities and in {@link ProgramRun#setAsciiOutput(String)}
 * for unsupported output types. The Shell catches this and presents a clear
 * message to the user without terminating the session.
 */
public class InvalidCommandException extends Exception {
    /**
     * Creates a new InvalidCommandException with a descriptive message.
     *
     * @param message details about the invalid input
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
