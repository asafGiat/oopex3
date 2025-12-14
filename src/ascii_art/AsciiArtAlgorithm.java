package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageBrightnessCalculator;
import image.ImagePrepare;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;
import java.io.IOException;

/**
 * The core ASCII art generation algorithm. It prepares the input image,
 * computes brightness per sub-image, and selects the best matching character
 * for each tile using a SubImgCharMatcher. Optionally supports reverse mapping
 * of brightness to characters.
 */
public class AsciiArtAlgorithm {
    private final int resolution;
    private final boolean isReversed;
    private Image image;
    private SubImgCharMatcher subImgCharMatcher;

    /**
     * Constructs a new ASCII art algorithm instance.
     *
     * @param image the source {@link image.Image} to convert to ASCII art
     * @param resolution the number of tiles per dimension (must evenly divide padded image)
     * @param charMatcher the matcher that maps brightness values to characters
     * @param isReversed if true, invert brightness mapping (dark→light and vice versa)
     */
    public AsciiArtAlgorithm(Image image, int resolution, SubImgCharMatcher charMatcher,
                             boolean isReversed) {
        this.subImgCharMatcher = charMatcher;
        this.image = image;
        this.resolution = resolution;
        this.isReversed = isReversed;

    }

    /**
     * Executes the algorithm: pads and splits the image into tiles, computes brightness
     * per tile, and selects a character for each position using the provided matcher.
     *
     * @return a 2D character grid representing the ASCII art
     * @throws IllegalArgumentException if resolution is invalid for the prepared image
     */
    public char [][] run(){
        ImagePrepare imagePrepare = new ImagePrepare(image);
        Image[][] subImages = imagePrepare.prepareImage(resolution);
        int rows = subImages.length;
        int cols = subImages[0].length;
        char[][] asciiArt = new char[rows][cols];
        for (int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                if (isReversed){
                    asciiArt[i][j]=subImgCharMatcher.getCharByImageBrightness
                            (1.0 - ImageBrightnessCalculator.calculateBrightness(subImages[i][j]));

                } else {
                    asciiArt[i][j]=subImgCharMatcher.getCharByImageBrightness
                            (ImageBrightnessCalculator.calculateBrightness(subImages[i][j]));
                }
            }
        }
        return asciiArt;
    }
}
