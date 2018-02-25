package image;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import overlay.OverlayCanvas;
import tag.HasTagListView;
import tag.TagListView;
import tag.TagManagerView;
import utils.StringListView;
import utils.View;

import java.io.File;
import java.util.ArrayList;

//**************************************************************************************
// *    Title: (adapted from) <JavaFx Documentation - Using JavaFxControls - Button>
// *    Author: Alla Redko
// *    Date: September 2013
// *    Date Accessed : November 2017
// *    Code version: N/A
// *    Availability: https://docs.oracle.com/javafx/2/ui_controls/button.htm
// *
// ***************************************************************************************/

/**
 * ImageManagerView. The View for an ImageManager.
 */
public class ImageManagerView extends View implements HasTagListView {
    /**
     * ImageListView of this ImageManagerView. Displays a list of the currently managed ImageFiles from an ImageManager
     */
    private ImageListView directoryThumbnails = new ImageListView(this, 35);
    /**
     * The currently selected ImageFile in the program View.
     */
    private ImageFile selectedImageFile;
    /**
     * The ImageView for the currently selected ImageFile in the program View.
     */
    private ImageView selectedImageView = new ImageView();
    /**
     * Button to open the containing directory of currently selected ImageFile in the program View.
     */
    private Button openFileButton = new Button("Open Containing Directory...");
    /**
     * Button to move containing directory of currently selected ImageFile in the program View.
     */
    private Button moveFileButton = new Button("Move File...");
    /**
     * Button to add tag to currently selected ImageFile in the program view.
     */
    private Button addTagButton = new Button("Tag Image...");
    /**
     * Button to remove tag to currently selected ImageFile in the program view.
     */
    private Button removeTagButton = new Button("Remove tag...");
    /**
     * Button to revert currently selected ImageFile in the program view to an old name.
     */
    private Button revertToOldNameButton = new Button("Revert to Old Name...");

    private Label imageTagLabel = new Label("Tags on this Image:");
    private Label nameHistoryLabel = new Label("Image Name History:");

    private ScrollPane imageFilePathLabelContainer = new ScrollPane();
    private Label imageFilePathLabel = new Label(" ");


    /**
     * ImageManager (controller) associated with this ImageManagerView (view)
     */
    private ImageManager imageManager;
    /**
     * Collaborator TagManagerView (view) associated with this ImageManagerView (view)
     */
    private TagManagerView tagManagerView;
    /**
     * View for the taglist associated with the currently selected ImageFile in the program view.
     */
    private TagListView imageTags = new TagListView();
    /**
     * View for the list of historical names associated with the currently selected ImageFile in the program view.
     */
    private StringListView nameHistoryOfSelectedImageView = new StringListView();
    /**
     * GridPane containing this ImageManagerView GUI elements
     */
    private GridPane gridPane = new GridPane();
    /**
     * Stage to display this ImageManagerView's open dialog GUI elements.
     */
    private Stage stage;
    /**
     * Canvas subview upon which to display an ImageTagOverlay for a given Tag/Image selection in the program view.
     */
    private OverlayCanvas overlayCanvas;
    /**
     * Button to add an ImageTagOverlay for a given Tag/Image selection in the program view.
     */
    private Button addOverlayButton = new Button("Set Overlay for this Tag/Image Combination");
    /**
     * Button to remove an ImageTagOverlay for a given Tag/Image selection in the program view.
     */
    private Button removeOverlayButton = new Button("Remove Overlay");

    /**
     * Constructs an ImageManagerView for a given ImageManager and Stage.
     *
     * @param imageManager ImageManager to be associated with this ImageManagerView.
     */
    public ImageManagerView(ImageManager imageManager) {
        this.imageManager = imageManager;
        imageManager.setView(this);
        selectedImageView.setFitWidth(400);
        selectedImageView.setFitHeight(300);
        selectedImageView.setPreserveRatio(true);
        overlayCanvas = new OverlayCanvas(this.imageManager);
        setupGridPane();
        setupInputs();
        setupView();

        this.stage = getProgramView().getMainStage();

        imageFilePathLabel.setStyle("-fx-background-color: white");
        imageFilePathLabel.setMinWidth(300);
        imageFilePathLabelContainer.setContent(imageFilePathLabel);
        imageFilePathLabelContainer.setPrefWidth(300);

        imageTags.setParent(this);
    }

    /**
     * Sets the sibling (collaborator) TagManagerView associated with this ImageManagerView.
     *
     * @param tmv TagManagerView to associate with this ImageManagerView.
     */
    public void setSiblingTagManagerView(TagManagerView tmv) {
        tagManagerView = tmv;
        tmv.setSiblingImageManagerView(this);
    }

    /**
     * Sets the ImageFiles that this ImageManagerView should display.
     *
     * @param imageFilesToView List of ImageFiles that this ImageManagerView should display.
     */
    void setImageFilesToView(ArrayList<ImageFile> imageFilesToView) {
        selectedImageFile = null;
        selectedImageView.setImage(null);
        directoryThumbnails.clearList();
        directoryThumbnails.setItems(imageFilesToView);
    }

    /**
     * Sets the ImageFile that this ImageManagerView should display as selected.
     *
     * @param imageFile ImageFile to set as selected in this ImageManagerView.
     */
    void setSelectedImage(ImageFile imageFile) {
        selectedImageFile = imageFile;

        if (hasSelectedImageFile()) {
            // create a new image  to display
            Image image = new Image(selectedImageFile.getImageFilePath().toURI().toString());
            // 1. update the image view for this manager
            selectedImageView.setImage(image);
            // 2. update the tag list view for the selected image file
            imageTags.setItems(selectedImageFile.getTagList());
            // 3. update the name history for the selected image file
            nameHistoryOfSelectedImageView.setItems(selectedImageFile.getNameHistory());
            imageFilePathLabel.setText(selectedImageFile.getImageFilePath().toString());
            overlayCanvas.setImageToOverlay(selectedImageView);
            overlayCanvas.setOverlaysForImageAndTags(selectedImageFile, imageTags.getCurrentlySelectedTags());
        }
    }

    /**
     * Filters and handles events from all GUI elements associated with this ImageManagerView
     *
     * @param e ActionEvent to filter and handle.
     */
    @Override
    @SuppressWarnings("unused")
    public void handle(ActionEvent e) {
        Object eventSource = e.getSource();
        //event from tagButton
        if (eventSource.equals(addTagButton)) {
            handleAddTagButton();
        }
        // event from removeTagButton
        else if (eventSource.equals(removeTagButton)) {
            handleRemoveTagButton();
        }
        // event from openFileButton
        else if (eventSource.equals(openFileButton)) {
            handleOpenFileButton();
        }
        // event from moveFileButton
        else if (eventSource.equals(moveFileButton)) {
            handleMoveFileButton();
        }
        // event from revertToOldNameButton
        else if (eventSource.equals(revertToOldNameButton)) {
            handleRevertToOldNameButton();
        }
        // event from addOverlayButton
        else if (eventSource.equals(addOverlayButton)) {
            handleAddOverlayButton();
        }
        // event from removeOverlayButton
        else if (eventSource.equals(removeOverlayButton)) {
            handleRemoveOverlayButton();
        }

    }

    /**
     * Setup this ImageManagerView's GridPane.
     */
    private void setupGridPane() {
        // setup positioning
        GridPane.setConstraints(selectedImageView, 1, 0);
        GridPane.setConstraints(directoryThumbnails.getListView(), 0, 0);
        GridPane.setConstraints(imageTagLabel, 0, 3);
        GridPane.setConstraints(openFileButton, 0, 1);
        GridPane.setConstraints(moveFileButton, 0, 2);
        GridPane.setConstraints(imageTags.getListView(), 0, 4);
        GridPane.setConstraints(addTagButton, 0, 5);
        GridPane.setConstraints(removeTagButton, 0, 6);
        GridPane.setConstraints(nameHistoryOfSelectedImageView.getListView(), 1, 4);
        GridPane.setConstraints(nameHistoryLabel, 1, 3);
        GridPane.setConstraints(revertToOldNameButton, 1, 5);
        GridPane.setConstraints(imageFilePathLabelContainer, 1, 1);
        GridPane.setConstraints(overlayCanvas.getCanvas(), 1, 0);
        GridPane.setConstraints(addOverlayButton, 1, 2);
        GridPane.setConstraints(removeOverlayButton, 2, 2);

        gridPane.setHgap(12);
        gridPane.setVgap(12);

        // add to gridPane
        gridPane.getChildren().addAll(
                directoryThumbnails.getListView(),
                selectedImageView,
                imageTagLabel,
                openFileButton,
                moveFileButton,
                imageTags.getListView(),
                addTagButton,
                removeTagButton,
                nameHistoryOfSelectedImageView.getListView(),
                nameHistoryLabel,
                revertToOldNameButton,
                imageFilePathLabelContainer,
                overlayCanvas.getCanvas(),
                addOverlayButton,
                removeOverlayButton
        );

    }

    /**
     * Setup the inputs (GUI elements) that this ImageManagerView will handle.
     */
    public void setupInputs() {
        openFileButton.setOnAction(this);
        moveFileButton.setOnAction(this);
        addTagButton.setOnAction(this);
        removeTagButton.setOnAction(this);
        revertToOldNameButton.setOnAction(this);
        addOverlayButton.setOnAction(this);
        removeOverlayButton.setOnAction(this);
    }

    /**
     * Updates the view information about the currently selected ImageFile in the program view.
     */
    public void updateCurrentlySelectedView() {
        if (hasSelectedImageFile()) {
            imageTags.setItems(selectedImageFile.getTagList());
            nameHistoryOfSelectedImageView.setItems(selectedImageFile.getNameHistory());
            imageFilePathLabel.setText(selectedImageFile.getImageFilePath().toString());
            overlayCanvas.setOverlaysForImageAndTags(selectedImageFile, imageTags.getCurrentlySelectedTags());
        }
    }

    /**
     * Sets up the view for this ImageManagerView.
     */
    public void setupView() {
        GridPane.setConstraints(this.gridPane, 0, 1);
        this.getProgramView().getMainGridPane().getChildren().add(this.gridPane);
    }

    /**
     * Method called from TagListView contained in this object.
     */
    @Override
    public void updateFromTagListView() {
        updateTagOverlays();
    }

    /**
     * Updates the view that displays tag overlays.
     */
    void updateTagOverlays() {
        overlayCanvas.setOverlaysForImageAndTags(selectedImageFile, imageTags.getCurrentlySelectedTags());
    }

    /**
     * Private handler for when the add tag button is pressed.
     */
    private void handleAddTagButton() {
        ArrayList<String> tagsToAdd = tagManagerView.getCurrentlySelectedTags();
        if (!tagsToAdd.isEmpty() && hasSelectedImageFile()) {
            imageManager.tagImage(selectedImageFile, tagsToAdd);
            updateCurrentlySelectedView();
        }

    }

    /**
     * Private handler for when the move file button is pressed.
     */
    private void handleMoveFileButton() {
        if (hasSelectedImageFile()) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(selectedImageFile.getImageFilePath().getParent()));
            File fileToMoveTo = directoryChooser.showDialog(stage);
            imageManager.moveImageFile(selectedImageFile, fileToMoveTo);
        }
    }

    /**
     * Private handler for when the remove tag button is pressed.
     */
    private void handleRemoveTagButton() {
        ArrayList<String> tagsToRemove = imageTags.getCurrentlySelectedTags();
        if (hasCurrentlySelectedTags() && hasSelectedImageFile()) {
            imageManager.removeTagsFromImage(selectedImageFile, tagsToRemove);
            updateCurrentlySelectedView();

        }
    }

    /**
     * Private handler for when the open file button is pressed.
     */
    private void handleOpenFileButton() {
        if (hasSelectedImageFile()) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(selectedImageFile.getImageFilePath().getParent()));
            directoryChooser.showDialog(stage);
        }
    }

    /**
     * Private handler for when the revert to old name button is pressed.
     */
    private void handleRevertToOldNameButton() {
        String nameToRevertTo = nameHistoryOfSelectedImageView.getSelected();
        if (hasSelectedImageFile()) {
            imageManager.setImageToOldName(selectedImageFile, nameToRevertTo);
            updateCurrentlySelectedView();
        }
    }

    /**
     * Private handler for when the add overlay button is pressed.
     */
    private void handleAddOverlayButton() {
        if (hasSelectedImageFile() && hasCurrentlySelectedTags()) {
            overlayCanvas.startCreateOverlay(selectedImageFile, imageTags.getCurrentlySelectedTags());
        }
    }

    /**
     * Private handler for when the remove overlay button is pressed.
     */
    private void handleRemoveOverlayButton() {
        if (hasSelectedImageFile() && hasCurrentlySelectedTags()) {
            imageManager.removeOverlaysForImageTags(selectedImageFile, imageTags.getCurrentlySelectedTags());
        }
    }

    /**
     * Returns true iff there is a currently selected image in the program view.
     *
     * @return Whether or not there is a currently selected image in the program view.
     */
    private boolean hasSelectedImageFile() {
        return (selectedImageFile != null);
    }

    /**
     * Returns true iff the tag list for the currently selected image contains user-selected tags.
     *
     * @return Whether or not the user has selected a tag from the tag list of the currently selected image.
     */
    private boolean hasCurrentlySelectedTags() {
        return !imageTags.getCurrentlySelectedTags().isEmpty();
    }

}
