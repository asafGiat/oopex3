package image;

import java.awt.*;

/**
 * A class responsible for converting an image into sub-images based on resolution.
 */
public class ConvertImageToSubImages {
    private final Image image;

    /**
     * Constructs a ConvertImageToSubImages object.
     *
     * @param image The image to be divided into sub-images
     */
    public ConvertImageToSubImages(Image image) {
        super();
        this.image = image;
    }

    /**
     * Divides the image into sub-images based on the specified resolution.
     * The resolution determines the size of each sub-image (resolution x resolution pixels).
     * The image dimensions must be divisible by the resolution.
     *
     * @param resolution The size of each square sub-image (must be a power of 2)
     * @return A 2D array of Image objects representing the sub-images
     * @throws IllegalArgumentException if resolution is invalid or doesn't divide the image evenly
     */
    public Image[][] divideIntoSubImages(int resolution) {
        validateResolution(resolution);

        int numRows = image.getHeight() / resolution;
        int numCols = image.getWidth() / resolution;

        Image[][] subImages = new Image[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                subImages[row][col] = extractSubImage(row, col, resolution);
            }
        }

        return subImages;
    }

    /**
     * Validates that the resolution is valid for dividing the image.
     *
     * @param resolution The resolution to validate
     * @throws IllegalArgumentException if resolution is invalid
     */
    private void validateResolution(int resolution) {
        if (resolution <= 0) {
            throw new IllegalArgumentException("Resolution must be positive");
        }
        else if (resolution > image.getWidth() || resolution > image.getHeight()) {
            throw new IllegalArgumentException("Resolution cannot be larger than image dimensions");
        }
        else if (image.getWidth() % resolution != 0 || image.getHeight() % resolution != 0) {
            throw new IllegalArgumentException(
                    "Resolution must divide image dimensions evenly. " +
                    "Image dimensions: " + image.getWidth() + "x" + image.getHeight() +
                    ", Resolution: " + resolution);
        }
    }

    /**
     * Extracts a single sub-image from the original image.
     *-
     * @param row The row index of the sub-image
     * @param col The column index of the sub-image
     * @param resolution The size of the sub-image
     * @return An Image object representing the sub-image
     */
    private Image extractSubImage(int row, int col, int resolution) {
        Color[][] subImagePixels = new Color[resolution][resolution];

        int startRow = row * resolution;
        int startCol = col * resolution;

        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                subImagePixels[i][j] = image.getPixel(startRow + i, startCol + j);
            }
        }

        return new Image(subImagePixels, resolution, resolution);
    }
}
