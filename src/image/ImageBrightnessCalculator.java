package image;

import java.awt.*;

/**
 * A utility class for calculating the brightness of an image.
 * Brightness is calculated as the average grayscale value of all pixels.
 *
 * @author asaf
 */
public class ImageBrightnessCalculator {

    private static final double RED_WEIGHT = 0.2126;
    private static final double GREEN_WEIGHT = 0.7152;
    private static final double BLUE_WEIGHT = 0.0722;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ImageBrightnessCalculator() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Calculates the brightness of the given image.
     * The brightness is computed as the average grayscale value of all pixels,
     * where grayscale is calculated using the formula: (R + G + B) / 3.
     *
     * @param image The image whose brightness is to be calculated
     * @return The brightness value as an integer (0-255)
     * @throws IllegalArgumentException if image is null
     */
    public static double calculateBrightness(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        long totalBrightness = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color pixel = image.getPixel(i, j);
                int grayscale = calculateGrayscale(pixel);
                totalBrightness += grayscale;
            }
        }
        return (double) (totalBrightness / ((long) width * height)) /255;
    }

    /**
     * Calculates the grayscale value of a color.
     * Uses the average of RGB components.
     *
     * @param color The color to convert to grayscale
     * @return The grayscale value (0-255)
     */
    private static int calculateGrayscale(Color color) {
        return (int) (color.getRed() * RED_WEIGHT + color.getGreen() * GREEN_WEIGHT  + color.getBlue() * BLUE_WEIGHT);
    }
}
