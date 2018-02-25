package main;


import image.ImageManager;
import image.ImageManagerView;
import javafx.application.Application;
import javafx.stage.Stage;
import tag.TagManager;
import tag.TagManagerView;
import utils.*;

//**************************************************************************************
// *    Title: (adatped from) <JavaFx Documentation - Hello World, JavaFx Style>
// *    Author: Gail Chappell
// *    Date: September 2013
// *    Code version: N/A
// *    https://docs.oracle.com/javafx/2/get_started/hello_world.htm
// *
// ***************************************************************************************/

public class Main extends Application {


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    @SuppressWarnings("unused")
    public void start(final Stage stage) {

        stage.setTitle("ImageTagger Phase2 --- Group 0577");

        MainView.createInstance(stage);
        MainView programView = MainView.getInstance();

        NameLogger nameLogger = NameLogger.getInstance();

        ConfigurationManager configurationManager = new ConfigurationManager();
        FileManager fileManager = new FileManager();
        ImageManager imageManager = new ImageManager("serializedimages.ser");
        TagManager tagManager = new TagManager("serializedtags.ser");

        imageManager.setTagManager(tagManager);
        fileManager.setImageManager(imageManager);

        configurationManager.setTagManager(tagManager);
        configurationManager.setImageManager(imageManager);


        FileManagerView fileManagerView = new FileManagerView(fileManager);
        ImageManagerView imageManagerView = new ImageManagerView(imageManager);
        TagManagerView tagManagerView = new TagManagerView(tagManager);
        NameLoggerView nameLoggerView = new NameLoggerView();

        imageManagerView.setSiblingTagManagerView(tagManagerView);

        programView.show();
    }


}
