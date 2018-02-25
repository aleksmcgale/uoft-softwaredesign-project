package utils;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A MainView class. The main view of the program.
 */
public class MainView {
    /**
     * The main Pane of the program.
     */
    private Pane mainPane;
    /**
     * The main GridPane of the program.
     */
    private GridPane mainGridPane;
    /**
     * The main Stage of the program.
     */
    private Stage stage;
    /**
     * The Singleton instance of MainView which provides global access to all views in the program.
     */
    private static MainView instance;

    /**
     * Constructs a main view for a specified stage.
     *
     * @param stage Stage for this MainView.
     */
    private MainView(Stage stage) {
        this.stage = stage;
        mainGridPane = new GridPane();
        mainGridPane.setHgap(12);
        mainGridPane.setVgap(12);

        mainPane = new VBox(12);
        mainPane.setPadding(new Insets(12, 12, 12, 12));
        mainPane.getChildren().addAll(mainGridPane);

    }

    /**
     * Returns the grid pane that contains all GUI elements in the main view.
     *
     * @return The grid pane that contains all GUI elements in the main view.
     */
    public GridPane getMainGridPane() {
        return mainGridPane;
    }

    /**
     * Returns the stage associated with this MainView.
     *
     * @return The stage associated with this MainView.
     */
    public Stage getMainStage() {
        return stage;
    }

    /**
     * Constructs a singleton instance for MainView class.
     *
     * @param stage Stage to associate with the MainView singleton instance.
     */
    public static void createInstance(Stage stage) {
        instance = new MainView(stage);
    }

    /**
     * Returns the singleton instance for MainView.
     *
     * @return The singleton instance for MainView.
     */
    public static MainView getInstance() {
        return instance;
    }

    /**
     * Renders the main program view to the screen.
     */
    public void show() {
        stage.setScene(new Scene(mainPane));
        stage.show();
    }


}
