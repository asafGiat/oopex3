package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that coordinates the ASCII art generation process.
 * It manages the image, resolution, character matching, output, and the ASCII art algorithm.
 */
public class ProgramRun {

    private static final int DEFAULT_RESOLUTION = 2;
    private static final char[] DEFAULT_CHARSET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    // Output types
    private static final String OUTPUT_CONSOLE = "console";
    private static final String OUTPUT_HTML = "html";

    // HTML output settings
    private static final String HTML_OUTPUT_FILE = "out.html";
    private static final String HTML_FONT = "Courier New";

    // Numbers
    private static final int MIN_CHARSET_SIZE = 2;
    private static final int POWER_OF_TWO_MASK_ADJUSTMENT = 1;
    private static final int MIN_RESOLUTION_FALLBACK = 1;
    private static final int ZERO = 0;

    // Error messages
    private static final String MSG_RESOLUTION_POWER_OF_TWO = "Resolution must be a power of 2";
    private static final String MSG_RESOLUTION_BOUNDS_PREFIX = "Resolution must be between ";
    private static final String MSG_RESOLUTION_BOUNDS_SEPARATOR = " and ";
    private static final String MSG_INSUFFICIENT_CHARSET = "Cannot generate ASCII art with fewer than 2 " +
            "characters in the charset";
    private static final String MSG_INVALID_OUTPUT_TYPE = "";

    private final Image image;
    private int resolution;
    private final SubImgCharMatcher subImgCharMatcher;
    private AsciiOutput asciiOutput;
    private final Set<Character> currentCharset;
    private boolean isReversed;

    /**
     * Constructor that initializes the program with an image path.
     *
     * @param imagePath the path to the image file to convert to ASCII art
     * @throws IOException if the image file cannot be read
     */
    public ProgramRun(String imagePath) throws IOException {
        this.image = new Image(imagePath);
        this.resolution = DEFAULT_RESOLUTION;
        this.subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARSET);
        this.asciiOutput = new ConsoleAsciiOutput();
        this.isReversed = false;

        // Initialize the charset tracking
        this.currentCharset = new HashSet<>();
        for (char c : DEFAULT_CHARSET) {
            currentCharset.add(c);
        }
    }

    /**
     * Gets the image object.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the current resolution.
     *
     * @return the resolution
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Sets the resolution.
     *
     * @param resolution the new resolution
     * @throws ResolutionOutOfBoundsException if resolution is not a power of 2 or out of bounds
     */
    public void setResolution(int resolution) throws ResolutionOutOfBoundsException {
        // Check if resolution is a power of 2
        if (resolution <= ZERO || (resolution & (resolution - POWER_OF_TWO_MASK_ADJUSTMENT)) != ZERO) {
            throw new ResolutionOutOfBoundsException(MSG_RESOLUTION_POWER_OF_TWO);
        }

        // Calculate bounds
        int minResolution = Math.max(MIN_RESOLUTION_FALLBACK, image.getWidth() / image.getHeight());
        int maxResolution = image.getWidth();

        // Check if resolution is within bounds
        if (resolution < minResolution || resolution > maxResolution) {
            throw new ResolutionOutOfBoundsException(
                    MSG_RESOLUTION_BOUNDS_PREFIX + minResolution + MSG_RESOLUTION_BOUNDS_SEPARATOR +
                            maxResolution);
        }

        this.resolution = resolution;
    }

    /**
     * Gets the SubImgCharMatcher.
     *
     * @return the character matcher
     */
    public SubImgCharMatcher getSubImgCharMatcher() {
        return subImgCharMatcher;
    }

    /**
     * Gets the ASCII output object.
     *
     * @return the ASCII output
     */
    public AsciiOutput getAsciiOutput() {
        return asciiOutput;
    }


    /**
     * Sets the ASCII output type based on the output type string.
     *
     * @param outputType "console" for console output or "html" for HTML output
     * @throws InvalidCommandException if the output type is invalid
     */
    public void setAsciiOutput(String outputType) throws InvalidCommandException {
        switch (outputType.toLowerCase()) {
            case OUTPUT_CONSOLE:
                this.asciiOutput = new ConsoleAsciiOutput();
                return;
            case OUTPUT_HTML:
                this.asciiOutput = new ascii_output.HtmlAsciiOutput(HTML_OUTPUT_FILE, HTML_FONT);
                return;
            default:
                throw new InvalidCommandException(MSG_INVALID_OUTPUT_TYPE);
        }
    }

    /**
     * Gets the current charset.
     *
     * @return a Set containing all characters currently in use
     */
    public Set<Character> getCharset() {
        return new HashSet<>(currentCharset);
    }

    /**
     * Adds a character to the charset and updates the character map accordingly.
     *
     * @param c the character to add
     */
    public void addChar(char c) {
        if (!currentCharset.contains(c)) {
            currentCharset.add(c);
            subImgCharMatcher.addChar(c);
        }
    }

    /**
     * Removes a character from the charset and updates the character map accordingly.
     *
     * @param c the character to remove
     */
    public void removeChar(char c) {
        if (currentCharset.contains(c)) {
            currentCharset.remove(c);
            subImgCharMatcher.removeChar(c);
        }
    }

    /**
     * Toggles the reverse state.
     */
    public void toggleReverse() {
        this.isReversed = !this.isReversed;
    }

    /**
     * Gets the current reverse state.
     *
     * @return true if reversed, false otherwise
     */
    public boolean isReversed() {
        return isReversed;
    }

    /**
     * Runs the ASCII art generation and outputs the result.
     *
     * @throws InsufficientCharsException if the charset has fewer than 2 characters
     */
    public void run() throws InsufficientCharsException {
        if (currentCharset.size() < MIN_CHARSET_SIZE) {
            throw new InsufficientCharsException(MSG_INSUFFICIENT_CHARSET);
        }

        // Build the algorithm locally with current settings
        AsciiArtAlgorithm algorithm = new AsciiArtAlgorithm(image, resolution, subImgCharMatcher, isReversed);
        char[][] asciiArt = algorithm.run();

        if (asciiArt != null) {
            asciiOutput.out(asciiArt);
        }
    }
}
