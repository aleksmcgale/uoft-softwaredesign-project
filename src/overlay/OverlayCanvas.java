package overlay;

import image.ImageFile;
import image.ImageManager;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * An OverlayCanvas. Displays ImageTagOverlays on top of images.
 */
public class OverlayCanvas implements EventHandler<MouseEvent> {
    /**
     * Canvas upon which to display ImageTagOverlays
     */
    private Canvas canvas = new Canvas();
    /**
     * The last created ImageTagOverlay for this OverlayCanvas.
     */
    private ImageTagOverlay lastCreated;
    /**
     * The currently selected ImageFile in this OverlayCanvas' parent ImageManagerView.
     */
    private ImageFile currentlySelectedImageFile;
    /**
     * Tag's to be associated with the ImageTagOverlay currently in creation (lastCreated).
     */
    private ArrayList<String> tagsToCreateFor;
    /**
     * The ImageTagOverlays currently being displayed by this OverlayCanvas.
     */
    private ArrayList<ImageTagOverlay> tagOverlays = new ArrayList<>();
    /**
     * Whether or not this OverlayCanvas is currently in the process of creating an ImageTagOverlay.
     */
    private boolean creatingOverlay = false;
    /**
     * The parent ImageManager of this OverlayCanvas.
     */
    private ImageManager imageManager;

    /**
     * Constructs a new OverlayCanvas for a given parent ImageManager.
     *
     * @param im ImageManager for which this OverlayCanvas will be a child.
     */
    public OverlayCanvas(ImageManager im) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
        imageManager = im;
    }

    /**
     * Resets this OverlayCanvas. Stops any creation of ImageTagOverlays and de-associates an ImageFile from this
     * OverlayCanvas.
     */
    private void reset() {
        lastCreated = null;
        currentlySelectedImageFile = null;
        tagsToCreateFor = null;
        creatingOverlay = false;
    }

    /**
     * Updates this OverlayCanvas for a specified ImageView.
     *
     * @param imageView ImageView for which to update this OverlayCanvas.
     */
    public void setImageToOverlay(ImageView imageView) {
        this.reset();
        Bounds b = imageView.getBoundsInParent();
        double canvasWidth = b.getWidth();
        double canvasHeight = b.getHeight();
        canvas.setHeight(canvasHeight);
        canvas.setWidth(canvasWidth);
    }

    /**
     * Sets the ImageTagOverlays to display for a given ImageFile and given collection of user selected tag names.
     *
     * @param imageFile    ImageFile for which to display ImageTagOverlays.
     * @param selectedTags Tags for which to display ImageTagOverlays.
     */
    public void setOverlaysForImageAndTags(ImageFile imageFile, ArrayList<String> selectedTags) {
        tagOverlays.clear();
        tagOverlays.addAll(imageFile.getOverlaysForTags(selectedTags));
        drawTagOverlays();
    }

    /**
     * Returns the JavaFx canvas associated with this OverlayCanvas.
     *
     * @return The JavaFx canvas associated with this OverlayCanvas.
     */
    public Canvas getCanvas() {
        return this.canvas;
    }

    /**
     * Starts the process of creating a new ImageTagOverlay for a specified ImageFile and a specified collection of
     * Tag names.
     *
     * @param imageFileToCreateFor The ImageFile for which to create the new ImageTagOverlay.
     * @param tagsToCreateFor      The Tags for which to create the new ImageTagOverlay.
     */
    public void startCreateOverlay(ImageFile imageFileToCreateFor, ArrayList<String> tagsToCreateFor) {
        currentlySelectedImageFile = imageFileToCreateFor;
        this.tagsToCreateFor = tagsToCreateFor;
        creatingOverlay = true;
    }

    /**
     * Finishes the process of creating a new ImageTagOverlay.
     */
    private void finishCreateOverlay() {
        imageManager.addOverlayForImageTags(currentlySelectedImageFile, tagsToCreateFor, lastCreated);
        creatingOverlay = false;
    }

    /**
     * Draws a red rectangle to this OverlayCanvas' JavaFx canvas given two specified points.
     *
     * @param x1 The x coordinate of the first point (always between 0-1).
     * @param y1 The y coordinate of the first point (always between 0-1).
     * @param x2 The x coordinate of the second point (always between 0-1).
     * @param y2 The y coordinate of the second point (always between 0-1).
     */
    private void drawRectFromPoints(double x1, double y1, double x2, double y2) {
        // get the full pixel width of the canvas
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        // get the pixel coordinates of a rectangle from the normalized points.
        double[] x_Points = new double[]{x1 * w, x1 * w, x2 * w, x2 * w};
        double[] y_Points = new double[]{y1 * h, y2 * h, y2 * h, y1 * h};
        // draw a rectangle
        gc.strokePolygon(x_Points, y_Points, 4);
    }

    /**
     * Draws all the ImageTagOverlays currently stored in this OverlayCanvas.
     */
    @SuppressWarnings("all")
    private void drawTagOverlays() {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, w, h);

        for (int i = 0; i < tagOverlays.size(); i++) {
            ImageTagOverlay t = tagOverlays.get(i);
            gc.setStroke(Color.RED);
            // draw a rectangle using the (normalized) points stored in the ImageTagOverlay
            try {
                drawRectFromPoints(t.getPointA()[0], t.getPointA()[1], t.getPointB()[0], t.getPointB()[1]);
            } catch (NullPointerException npe) {
            }
        }
    }

    /**
     * Handles and filters mouse events on this CanvasOverlay.
     *
     * @param e MouseEvent to handle.
     */
    public void handle(MouseEvent e) {
        if (creatingOverlay) {
            if (e.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                handleMouseDragged(e);
            } else if (e.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                handleMousePressed(e);
            } else if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                handleMouseReleased(e);
            }
        }
    }

    /**
     * Private handler for when the mouse is dragged on the OverlayCanvas.
     *
     * @param e MouseEvent to handle.
     */
    private void handleMouseDragged(MouseEvent e) {
        // get the normalized (0-1) mouse position on this OverlayCanvas
        double[] normalizedMousePos = getMousePosNormalized(e);
        //sets the second point of the overlay currently in creation
        lastCreated.setPointB(normalizedMousePos[0], normalizedMousePos[1]);
        drawTagOverlays();
    }

    /**
     * Private handler for when the mouse is pressed on the OverlayCanvas.
     *
     * @param e MouseEvent to handle.
     */
    private void handleMousePressed(MouseEvent e) {
        // get the normalized (0-1,0-1) mouse position on this OverlayCanvas.
        double[] normalizedMousePos = getMousePosNormalized(e);
        // start the creation of a new ImageTagOverlay
        lastCreated = new ImageTagOverlay();
        tagOverlays.add(lastCreated);
        // set the first point of the new ImageTagOverlay
        lastCreated.setPointA(normalizedMousePos[0], normalizedMousePos[1]);
    }

    /**
     * Private handler for when the mouse is released on the OverlayCanvas.
     *
     * @param e MouseEvent to handle.
     */
    private void handleMouseReleased(MouseEvent e) {
        // get the normalized (0-1,0-1) mouse  position on this OverlayCanvas
        double[] normalizedMousePos = getMousePosNormalized(e);
        // set the second point of the ImageTagOverlay currently being created.
        lastCreated.setPointB(normalizedMousePos[0], normalizedMousePos[1]);
        drawTagOverlays();
        // finish the creation: a released event signals the end of a mouse {click - drag - release} sequence that
        // creates a rectangle on the canvas.
        finishCreateOverlay();
    }

    /**
     * For a given MouseEvent, returns the mouse position on the canvas as a normalized coordinate
     * value (x : 0-1, y : 0-1).
     *
     * @param e The MouseEvent for which to compute the normalized position.
     * @return The normalized position of the mouse on the canvas.
     */
    private double[] getMousePosNormalized(MouseEvent e) {
        double x_normalized = e.getX() / canvas.getWidth();
        double y_normalized = e.getY() / canvas.getHeight();
        return new double[]{x_normalized, y_normalized};
    }

}
