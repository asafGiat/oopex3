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

    private final Image image;
    private int resolution;
    private final SubImgCharMatcher subImgCharMatcher;
    private AsciiOutput asciiOutput;
    private final AsciiArtAlgorithm asciiArtAlgorithm;
    private final Set<Character> currentCharset;

    /**
     * Constructor that initializes the program with an image path.
     * @param imagePath the path to the image file to convert to ASCII art
     * @throws IOException if the image file cannot be read
     */
    public ProgramRun(String imagePath) throws IOException {
        this.image = new Image(imagePath);
        this.resolution = DEFAULT_RESOLUTION;
        this.subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARSET);
        this.asciiOutput = new ConsoleAsciiOutput();
        this.asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution, subImgCharMatcher, false);

        // Initialize the charset tracking
        this.currentCharset = new HashSet<>();
        for (char c : DEFAULT_CHARSET) {
            currentCharset.add(c);
        }
    }

    /**
     * Gets the image object.
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the current resolution.
     * @return the resolution
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Sets the resolution.
     * @param resolution the new resolution
     * @throws ResolutionOutOfBoundsException if resolution is not a power of 2 or out of bounds
     */
    public void setResolution(int resolution) throws ResolutionOutOfBoundsException {
        // Check if resolution is a power of 2
        if (resolution <= 0 || (resolution & (resolution - 1)) != 0) {
            throw new ResolutionOutOfBoundsException("Resolution must be a power of 2");
        }

        // Calculate bounds
        int minResolution = Math.max(1, image.getWidth() / image.getHeight());
        int maxResolution = image.getWidth();

        // Check if resolution is within bounds
        if (resolution < minResolution || resolution > maxResolution) {
            throw new ResolutionOutOfBoundsException(
                "Resolution must be between " + minResolution + " and " + maxResolution);
        }

        this.resolution = resolution;
    }

    /**
     * Gets the SubImgCharMatcher.
     * @return the character matcher
     */
    public SubImgCharMatcher getSubImgCharMatcher() {
        return subImgCharMatcher;
    }

    /**
     * Gets the ASCII output object.
     * @return the ASCII output
     */
    public AsciiOutput getAsciiOutput() {
        return asciiOutput;
    }

    /**
     * Sets the ASCII output object.
     * @param asciiOutput the new ASCII output
     */
    public void setAsciiOutput(AsciiOutput asciiOutput) {
        this.asciiOutput = asciiOutput;
    }

    /**
     * Gets the AsciiArtAlgorithm object.
     * @return the ASCII art algorithm
     */
    public AsciiArtAlgorithm getAsciiArtAlgorithm() {
        return asciiArtAlgorithm;
    }

    /**
     * Gets the current charset.
     * @return a Set containing all characters currently in use
     */
    public Set<Character> getCharset() {
        return new HashSet<>(currentCharset);
    }

    /**
     * Adds a character to the charset and updates the character map accordingly.
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
     * @param c the character to remove
     */
    public void removeChar(char c) {
        if (currentCharset.contains(c)) {
            currentCharset.remove(c);
            subImgCharMatcher.removeChar(c);
        }
    }

    /**
     * Runs the ASCII art generation and outputs the result.
     */
    public void run() {
        char[][] asciiArt = asciiArtAlgorithm.run();
        if (asciiArt != null) {
            asciiOutput.out(asciiArt);
        }
    }
}
