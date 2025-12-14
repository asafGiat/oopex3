package ascii_art;

/**
 * Thrown when a requested resolution value is either not a power of two or
 * falls outside the legal bounds derived from the loaded image dimensions.
 * Used by {@link ProgramRun#setResolution(int)} and surfaced to the Shell for user feedback.
 */
public class ResolutionOutOfBoundsException extends Exception {

    /**
     * Creates a new ResolutionOutOfBoundsException with a descriptive message.
     *
     * @param message details about the violation (e.g., expected range or power-of-two requirement)
     */
    public ResolutionOutOfBoundsException(String message) {
        super(message);
    }
}
