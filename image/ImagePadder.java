package image;

import java.awt.*;

/**
 * A utility class for padding images to dimensions that are powers of 2.
 * This class provides functionality to pad images with white pixels to ensure
 * both width and height are powers of 2, which is useful for various image
 * processing algorithms.
 *
 * @author asaf
 */
public class ImagePadder {

    private static final Color PADDING_COLOR = Color.WHITE;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ImagePadder() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Pads the given image to the nearest power of 2 dimensions.
     * The original image is centered in the padded result, with white pixels
     * filling the extra space.
     *
     * @param image The image to pad
     * @return A Color array representing the padded image with dimensions
     *         that are powers of 2
     * @throws IllegalArgumentException if image is null
     */
    public static Color[][] padToPowerOfTwo(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int paddedWidth = nextPowerOfTwo(originalWidth);
        int paddedHeight = nextPowerOfTwo(originalHeight);

        return createPaddedArray(image, originalWidth, originalHeight,
                                paddedWidth, paddedHeight);
    }

    /**
     * Calculates the next power of 2 greater than or equal to the given number.
     *
     * @param n The input number
     * @return The smallest power of 2 that is >= n
     */
    private static int nextPowerOfTwo(int n) {
        if (n <= 0) {
            return 1;
        }
        // If n is already a power of 2, return it
        if ((n & (n - 1)) == 0) {
            return n;
        }
        // Find the next power of 2
        int power = 1;
        while (power < n) {
            power *= 2;
        }
        return power;
    }

    /**
     * Creates a padded array with the original image centered and surrounded
     * by white pixels.
     *
     * @param image The original image
     * @param originalWidth The width of the original image
     * @param originalHeight The height of the original image
     * @param paddedWidth The target padded width
     * @param paddedHeight The target padded height
     * @return A Color array with the padded image
     */
    private static Color[][] createPaddedArray(Image image, int originalWidth,
                                               int originalHeight, int paddedWidth,
                                               int paddedHeight) {
        Color[][] paddedArray = new Color[paddedHeight][paddedWidth];

        // Calculate offsets to center the image
        int rowOffset = (paddedHeight - originalHeight) / 2;
        int colOffset = (paddedWidth - originalWidth) / 2;

        // Fill the entire array with padding color
        for (int i = 0; i < paddedHeight; i++) {
            for (int j = 0; j < paddedWidth; j++) {
                paddedArray[i][j] = PADDING_COLOR;
            }
        }

        // Copy the original image to the center
        for (int i = 0; i < originalHeight; i++) {
            for (int j = 0; j < originalWidth; j++) {
                paddedArray[i + rowOffset][j + colOffset] = image.getPixel(i, j);
            }
        }

        return paddedArray;
    }
}

