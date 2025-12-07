package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

/**
 * A class that coordinates the ASCII art generation process.
 * It manages the image, resolution, character matching, output, and the ASCII art algorithm.
 */
public class ProgramRun {

    private static final int DEFAULT_RESOLUTION = 128;
    private static final char[] DEFAULT_CHARSET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final Image image;
    private int resolution;
    private final SubImgCharMatcher subImgCharMatcher;
    private AsciiOutput asciiOutput;
    private final AsciiArtAlgorithm asciiArtAlgorithm;

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
        this.asciiArtAlgorithm = new AsciiArtAlgorithm();
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
     */
    public void setResolution(int resolution) {
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
     * Runs the ASCII art generation and outputs the result.
     */
    public void run() {
        char[][] asciiArt = asciiArtAlgorithm.run();
        if (asciiArt != null) {
            asciiOutput.out(asciiArt);
        }
    }
}
