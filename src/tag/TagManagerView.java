package tag;

import image.ImageManagerView;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import utils.View;


//**************************************************************************************
// *    Title: (adatped from) <JavaFx Documentation - Using JavaFxControls>
// *    Author: Alla Redko
// *    Date: September 2013
// *    Code version: N/A
// *    Availability:
// *    https://docs.oracle.com/javafx/2/ui_controls/button.htm
// *    https://docs.oracle.com/javafx/2/ui_controls/text-field.htm
// *
// ***************************************************************************************/

import java.util.ArrayList;

/**
 * TagManagerView. The View for a TagManager.
 */
public class TagManagerView extends View {
    /**
     * TagListView of this TagManagerView. Displays a list of the currently managed Tags from a TagManager
     */
    private TagListView tagListView = new TagListView();
    /**
     * TagManager (controller) associated with this TagManagerView (view)
     */
    private TagManager tagManager;
    /**
     * Button to delete currently selected Tag in program view.
     */
    private Button deleteTagButton = new Button("Delete Tag");

    private Label label = new Label("Create Tag");
    /**
     * TextField to create a Tag for a specified text input.
     */
    private TextField textField = new TextField();
    /**
     * Containing HBox for TextField.
     */
    private HBox hb = new HBox();

    /**
     * GridPane containing this TagManagerView's GUI elements
     */
    private GridPane gridPane = new GridPane();

    /**
     * Collaborator (sibling) imageManagerView of this TagManagerView.
     */
    private ImageManagerView imageManagerView;

    /**
     * Constructs a TagManagerView (view) for a given TagManager (controller)
     *
     * @param tagManager TagManager to be associated with this TagManagerView.
     */
    public TagManagerView(TagManager tagManager) {
        this.tagManager = tagManager;
        tagManager.setView(this);
        setupGridPane();
        setupInputs();
        setupView();
    }

    /**
     * Filters and handles events from all GUI elements associated with this TagManagerView
     *
     * @param e ActionEvent to filter and handle.
     */
    @Override
    public void handle(ActionEvent e) {
        Object eventsource = e.getSource();

        // event from textField
        if (eventsource.equals(textField)) {
            handleCreateTagField();
        }

        // event from deleteTagButton
        else if (eventsource.equals(deleteTagButton)) {
            handleDeleteTagButton();
        }
    }

    /**
     * Setup this TagManagerView's GridPane.
     */
    private void setupGridPane() {
        GridPane.setConstraints(label, 0, 0);
        GridPane.setConstraints(deleteTagButton, 0, 1);
        GridPane.setConstraints(tagListView.getListView(), 0, 2);

        textField.setPromptText("Press Enter to Create a New Tag");
        textField.getText();

        hb.getChildren().addAll(label, textField);
        hb.setSpacing(12);

        gridPane.setHgap(12);
        gridPane.setVgap(12);
        gridPane.getChildren().addAll(hb, deleteTagButton, tagListView.getListView());
    }

    /**
     * Setup the inputs (GUI elements) that this TagManagerView will handle.
     */
    @Override
    public void setupInputs() {
        deleteTagButton.setOnAction(this);
        textField.setOnAction(this);
    }

    /**
     * Sets the list of Tags to display in the program view.
     *
     * @param tagArrayList List of Tags to display in the program view.
     */
    void updateTagList(ArrayList<Tag> tagArrayList) {
        tagListView.setItems(tagArrayList);
    }

    /**
     * Get the Tag that is currently selected in the program view.
     *
     * @return The Tag that is currently selected in the program view.
     */
    @SuppressWarnings("unused")
    @Deprecated
    public Tag getCurrentlySelectedTag() {
        if (tagListView.getCurrentlySelectedTag() != null) {
            return tagListView.getCurrentlySelectedTag();
        } else {
            return null;
        }
    }

    /**
     * Returns a list of Tag names that are currently selected in the program view.
     *
     * @return A list of Tag names that are currently selected in the program view.
     */
    public ArrayList<String> getCurrentlySelectedTags() {
        return tagListView.getCurrentlySelectedTags();
    }

    /**
     * Set the sibling (collaborator) ImageManagerView for this TagManagerView
     *
     * @param imv The ImageManagerView to associate with this TagManagerView
     */
    public void setSiblingImageManagerView(ImageManagerView imv) {
        this.imageManagerView = imv;
    }

    /**
     * Sets up this TagManagerView in the main program view.
     */
    public void setupView() {
        GridPane.setConstraints(this.gridPane, 1, 1);
        this.getProgramView().getMainGridPane().getChildren().add(this.gridPane);
    }

    /**
     * Private handler for handling when the createTagField receives input from the user.
     */
    private void handleCreateTagField() {
        tagManager.addTag(textField.getText());
        textField.clear();
    }

    /**
     * Private handler for handling when the deleteTagButton is pressed by the user.
     */
    private void handleDeleteTagButton() {
        ArrayList<String> tagsToDelete = tagListView.getCurrentlySelectedTags();
        if (!tagsToDelete.isEmpty()) {
            tagManager.removeTags(tagsToDelete);
            imageManagerView.updateCurrentlySelectedView();
        }
    }

}
