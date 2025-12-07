package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageBrightnessCalculator;
import image.ImagePrepare;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;
import java.io.IOException;

public class AsciiArtAlgorithm {
    private final int resolution;
    private final boolean isReversed;
    private Image image;
    private SubImgCharMatcher subImgCharMatcher;

    public AsciiArtAlgorithm(Image image, int resolution, SubImgCharMatcher charMatcher,
                             boolean isReversed) {
        this.subImgCharMatcher = charMatcher;
        this.image = image;
        this.resolution = resolution;
        this.isReversed = isReversed;

    }

    public char [][] run(){
        ImagePrepare imagePrepare = new ImagePrepare(image);
        Image[][] subImages = imagePrepare.prepareImage(resolution);
        int rows = subImages.length;
        int cols = subImages[0].length;
        char[][] asciiArt = new char[rows][cols];
        for (int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                asciiArt[i][j]=subImgCharMatcher.getCharByImageBrightness
                        (ImageBrightnessCalculator.calculateBrightness(subImages[i][j]));
            }
        }
        return asciiArt;
    }

    public static void main(String[] args) throws IOException {
        HtmlAsciiOutput consoleAsciiOutput = new HtmlAsciiOutput("test.html", "Courier New");
        char [] chars = {'m','o'};
        SubImgCharMatcher charMatcher = new SubImgCharMatcher(chars);
        Image image1 = new Image("C:/Users/97254/IdeaProjects/oopex3/board.jpeg");
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(image1, 2, charMatcher, false);
        char [][] asciiArt = asciiArtAlgorithm.run();
        consoleAsciiOutput.out(asciiArt);
        // Example usage
    }
}
