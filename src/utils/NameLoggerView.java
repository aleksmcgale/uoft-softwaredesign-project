package utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;

//**************************************************************************************
// *    Title: (adapted from) <JavaFX Freeze on Desktop.open(file), Desktop.browse(uri)>
// *    Author: Alex K
// *    Date: September 2015
// *    Code version: N/A
// *    Date Accessed: November 2017
// *    Availability: https://stackoverflow.com/questions/23176624/javafx-freeze-on-desktop-openfile-desktop-browseuri
// ***************************************************************************************/

/**
 * A NameLoggerView. The View for a NameLogger.
 */
public class NameLoggerView extends View {
    /**
     * Button which opens the name log.
     */
    private Button nameLoggerViewButton = new Button("Open Name Log");
    /**
     * Grid pane which contains this NameLoggerView's GUI elements.
     */
    private GridPane gridPane = new GridPane();

    /**
     * Constructs a NameLoggerView.
     */
    public NameLoggerView() {
        setupGridPane();
        setupInputs();
        setupView();
    }

    /**
     * Handles and filters the events of GUI elements contained in this NameLoggerView.
     *
     * @param e Event to handle and filter.
     */
    @Override
    public void handle(ActionEvent e) {
        Object eventsource = e.getSource();
        if (eventsource.equals(nameLoggerViewButton)) {
            handleNameLoggerViewButton();
        }
    }

    /**
     * Setup the inputs (GUI elements) that this NameLoggerView will handle.
     */
    public void setupInputs() {
        nameLoggerViewButton.setOnAction(this);
    }

    /**
     * Sets up this NameLoggerView's GridPane
     */
    private void setupGridPane() {
        GridPane.setConstraints(nameLoggerViewButton, 0, 0);
        gridPane.getChildren().add(nameLoggerViewButton);
    }

    /**
     * Sets up this FileManagerView in the main program view.
     */
    public void setupView() {
        GridPane.setConstraints(this.gridPane, 1, 0);
        this.getProgramView().getMainGridPane().getChildren().add(this.gridPane);
    }

    /**
     * Private handler for events from the button that opens the name log.
     */
    private void handleNameLoggerViewButton() {
        //this solution borrowed from stack overflow article mentioned above.
        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(new File("nameLog.txt"));
                } catch (IOException ioe) {
                    System.out.println("Fatal Error: Failed to open name log.");
                }

            }).start();
        }
    }

}
