package softeng206.tatai.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import softeng206.tatai.*;

/**
 * The FavouriteGamesController Class is the controller
 * for favouriteGames.fxml. It displays all of the favourite
 * games created by the user and allows them to click on a game
 * to start playing.
 * 
 * @author Sam Broadhead
 * @author Charlie Rillstone
 */
public class FavouriteGamesController extends Controller {
	@FXML
	private Label logo;
	@FXML
	private TilePane gamesTileBox;
	private HashMap<String, Question[]> favGames;
	private SceneNavigator sceneNavigator;
	ArrayList<String> games = new ArrayList<>();

	/**
	 * called to intialize {@link FavouriteGamesController} after its root element has been
	 * completely processed
	 * 
	 * @throws IOException io exception if encountered.
	 */
	@FXML
	void initialize() throws IOException {
		Font.loadFont(getClass().getResource("/fonts/theboldfont.ttf").toExternalForm(), 50);
		Font.loadFont(getClass().getResource("/fonts/PierSans-Medium.ttf").toExternalForm(), 32);
		Font.loadFont(getClass().getResource("/fonts/PierSans-Regular.ttf").toExternalForm(), 32);
		User user = User.getInstance();
		sceneNavigator = SceneNavigator.getInstance();
		logo.setOnMouseClicked(sceneNavigator::goHome);
		String customGameLocation = user.getCurrentUser().getCustomGames().toString();
		CSVReader csvReader = new CSVReader(customGameLocation);
		if (csvReader.getGameNames() != null) {
			games = csvReader.getGameNames();
			favGames = csvReader.getGames();
		}
		for (String name : games) {
			StackPane stackPane = makeStack(name);
			gamesTileBox.getChildren().add(stackPane);
		}
	}

	/**
	 * Method to make a button for each custom game that exists.
	 * This includes setting the button up for action to start the game
	 * and label it appropriately
	 * 
	 * @param name the name of the custom game
	 * @return a StackPane for the game provided
	 */
	StackPane makeStack(String name) {
		StackPane stack = new StackPane();
		stack.setPrefSize(200,200);
		stack.getStyleClass().add("favGameTile");
		Label label = new Label(name);
		label.setWrapText(true);
		label.setTextAlignment(TextAlignment.CENTER);
		label.getStyleClass().add("favGame");
		stack.getChildren().add(label);
		stack.setOnMouseClicked(e -> sceneNavigator.startCustomLevel(e,favGames.get(name)));
		return stack;
	}
}