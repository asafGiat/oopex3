package image;

/**
 * A class responsible for preparing an image for processing by padding it to
 * power-of-2 dimensions and splitting it into sub-images based on resolution.
 * This class orchestrates the image preparation workflow.
 *
 * @author asaf
 */
public class ImagePrepare {
    private final Image originalImage;
    private Image paddedImage;
    private Image[][] subImages;

    /**
     * Constructs an ImagePrepare object with the specified image.
     *
     * @param image The original image to prepare
     * @throws IllegalArgumentException if image is null
     */
    public ImagePrepare(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        this.originalImage = image;
    }

    /**
     * Prepares the image by padding it to power-of-2 dimensions and splitting
     * it into sub-images based on the specified resolution.
     *
     * @param resolution The resolution for dividing the image into sub-images
     * @return A 2D array of sub-images
     * @throws IllegalArgumentException if resolution is invalid or doesn't divide the padded dimensions
     * evenly
     */
    public Image[][] prepareImage(int resolution) {
        if (resolution <= 0) {
            throw new IllegalArgumentException("Resolution must be positive");
        }
        padImage();
        splitIntoSubImages(resolution);
        return subImages;
    }

    /**
     * Pads the original image to power-of-2 dimensions.
     */
    private void padImage() {
        java.awt.Color[][] paddedPixels = ImagePadder.padToPowerOfTwo(originalImage);
        int paddedHeight = paddedPixels.length;
        int paddedWidth = paddedPixels[0].length;
        this.paddedImage = new Image(paddedPixels, paddedWidth, paddedHeight);
    }

    /**
     * Splits the padded image into sub-images based on the resolution.
     *
     * @param resolution The resolution for dividing the image
     */
    private void splitIntoSubImages(int resolution) {
        ConvertImageToSubImages converter = new ConvertImageToSubImages(paddedImage);
        this.subImages = converter.divideIntoSubImages(resolution);
    }
}
