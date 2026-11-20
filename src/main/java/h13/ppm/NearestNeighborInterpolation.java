package h13.ppm;

import h13.ppm.image.PPMHeader;
import h13.ppm.image.PPMImage;
import h13.ppm.image.PixelColor;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

/**
 * Class to provide class method that implements nearest neighbor-interpolation of images.
 */
public final class NearestNeighborInterpolation {

    @DoNotTouch
    private NearestNeighborInterpolation() {
    }

    /**
     * Scales an image using nearest neighbor-interpolation given a scale factor.
     * The new image's dimensions are the originals scaled by scale factor (rounded towards 0).
     *
     * @param original    the original image to be scaled
     * @param scaleFactor the factor by which the image's dimensions should be scaled
     * @return a new image scaled by scaleFactor
     */
    public static PPMImage scaleImage(PPMImage original, float scaleFactor) {
        // TODO: H13.1.2
        PixelColor[][] oldPixelData = original.pixelData();
        PPMHeader oldHeader = original.header();
        int newHeight = (int)Math.floor(oldHeader.height()*scaleFactor);
        int newWidth = (int)Math.floor(oldHeader.width()*scaleFactor);
        PixelColor[][] newPixelData = new PixelColor[newHeight][newWidth];

            for(int y=0; y<newHeight; y++){
                int originalY = Math.clamp(Math.round(y/scaleFactor),0,oldHeader.height()-1);
                for(int x=0; x<newWidth; x++) {
                    int originalX = Math.clamp(Math.round(x / scaleFactor), 0, oldHeader.width() - 1);
                    newPixelData[y][x] = oldPixelData[originalY][originalX];

                }

            }


        return new PPMImage(new PPMHeader(newWidth, newHeight), newPixelData);
    }
}
