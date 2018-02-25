package utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * A View in the program.
 */
@SuppressWarnings("all")
public abstract class View implements EventHandler<ActionEvent> {
    /**
     * The Singleton instance of MainView that is available to all Views.
     */
    MainView programView = MainView.getInstance();

    /**
     * Sets up this View in the main program.
     */
    public abstract void setupView();

    /**
     * Sets up GUI elements that this view should handle.
     */
    public abstract void setupInputs();

    /**
     * Returns the MainView associated with this View.
     *
     * @return The MainView associated with this View.
     */
    public MainView getProgramView() {
        return programView;
    }

}