package overlay;

import java.io.Serializable;

/**
 * A ImageTagOverlay. Represents a visual overlay that can be set for a given Tag / Image combination. A ImageTagOverlay
 * consists of two points which are interpolated by a OverlayCanvas as a rectangle to be displayed over an image.
 */
public class ImageTagOverlay implements Serializable {

    /**
     * X coordinate for the first point of this ImageTagOverlay
     */
    private double x1;
    /**
     * Y coordinate for the first point of this ImageTagOverlay
     */
    private double y1;
    /**
     * X coordinate for the second point of this ImageTagOverlay
     */
    private double x2;
    /**
     * Y coordinate for the second point of this ImageTagOverlay
     */
    private double y2;

    /**
     * Set the first point of this ImageTagOverlay
     *
     * @param x X coordinate for the first point of this ImageTagOverlay
     * @param y Y coordinate for the first point of this ImageTagOverlay
     */
    void setPointA(double x, double y) {
        x1 = x;
        y1 = y;
    }

    /**
     * Set the first point of this ImageTagOverlay
     *
     * @param x X coordinate for the second point of this ImageTagOverlay
     * @param y Y coordinate for the second point of this ImageTagOverlay
     */
    void setPointB(double x, double y) {
        x2 = x;
        y2 = y;
    }

    /**
     * Returns the first point of this ImageTagOverlay
     *
     * @return An array containing the X and Y coordinates of the first point of this ImageTagOverlay
     */
    double[] getPointA() {
        return new double[]{x1, y1};
    }

    /**
     * Returns the second point of this ImageTagOverlay
     *
     * @return An array containing the X and Y coordinates of the second point of this ImageTagOverlay
     */
    double[] getPointB() {
        return new double[]{x2, y2};
    }

}
