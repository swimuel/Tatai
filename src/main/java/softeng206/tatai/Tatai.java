package softeng206.tatai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The initial class that the application is loaded from,
 * contains the start method for the JavaFX Application and
 * also the main method to launch the Application.
 * 
 * @author Sam Broadhead
 * @author Charlie Rillstone
 */

public class Tatai extends Application{

  /**
	 * start method for the application.
	 * @param primaryStage the stage passed into the method to be used.
	 */
    @Override
    public void start(Stage primaryStage) throws Exception {
        String splashLocation = "/splashScreen.fxml";
        String styleLocation = "/style.css";
        Parent homePage = FXMLLoader.load(getClass().getResource(splashLocation));
        homePage.getStylesheets().add(getClass().getResource(styleLocation).toExternalForm());
        Scene scene = new Scene(homePage,860,750);
        primaryStage.setTitle("Tātai!");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(750);
        primaryStage.show();
	}
	/**
	 * the main method that java needs.
	 * @param args
	 */
	public static void main (String[] args) {
		launch(args);
	}
}
