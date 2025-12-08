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
     * The resolution determines how many sub-images to create per row/column.
     * For example, resolution=2 creates a 2x2 grid (4 sub-images total).
     *
     * @param resolution The number of sub-images per row/column
     * @return A 2D array of Image objects representing the sub-images
     * @throws IllegalArgumentException if resolution is invalid or doesn't divide the image evenly
     */
    public Image[][] divideIntoSubImages(int resolution) {
        validateResolution(resolution);

        int subImageSize = image.getHeight() / resolution;  // Size of each sub-image in pixels

        Image[][] subImages = new Image[resolution][resolution];

        for (int row = 0; row < resolution; row++) {
            for (int col = 0; col < resolution; col++) {
                subImages[row][col] = extractSubImage(row, col, subImageSize);
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
     *
     * @param row The row index of the sub-image
     * @param col The column index of the sub-image
     * @param subImageSize The size (in pixels) of the sub-image
     * @return An Image object representing the sub-image
     */
    private Image extractSubImage(int row, int col, int subImageSize) {
        Color[][] subImagePixels = new Color[subImageSize][subImageSize];

        int startRow = row * subImageSize;
        int startCol = col * subImageSize;

        for (int i = 0; i < subImageSize; i++) {
            for (int j = 0; j < subImageSize; j++) {
                subImagePixels[i][j] = image.getPixel(startRow + i, startCol + j);
            }
        }

        return new Image(subImagePixels, subImageSize, subImageSize);
    }
}
