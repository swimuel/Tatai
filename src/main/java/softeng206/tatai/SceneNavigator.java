package softeng206.tatai;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.Node;
import softeng206.tatai.controllers.LevelController;

import java.io.IOException;

/**
 * The SceneNavigator class is used for all FXML loading and changing of scenes.
 * A singleton pattern has been used to increase performance wherever possible.
 *
 * @author Charlie Rillstone
 * @author Sam Broadhead
 */
public class SceneNavigator extends Controller {
	private Parent homeParent,statsParent,helpParent,customGameParent,favouriteGamesParent;
	private Parent badgeParent,finalScoreParent,splashParent;
	private static SceneNavigator singleton = new SceneNavigator();
	private String mathType;

	private SceneNavigator() {}

	/**
	 * the singleton getter.
	 *
	 * @return the instance
	 */
	public static SceneNavigator getInstance() {
		return singleton;
	}

	//region fxml loaders
	/**
	 * Pre-loads the FXML files at the splash screen so when the user
	 * passes splash and goes to home, the home, stats and help
	 * page is already loaded. This will make navigation between these pages
	 * much faster.
	 *
	 * @throws IOException the io exception
	 */
	public void preLoad() throws IOException {
		statsParent = FXMLLoader.load(getClass().getResource("/statsScreen.fxml"));
		helpParent = FXMLLoader.load(getClass().getResource("/helpScreen.fxml"));
	}

	/**
	 * Since stats page is already loaded from when the session first started,
	 * give the user completes a quiz, the stats page will need updating
	 * to show their recently added score.
	 * This reload method is called when a quiz is finished, it reloads
	 * the stats page FXML, getting any new data added.
	 *
	 * @throws IOException the io exception
	 */
	public void updateStats() throws IOException{
		statsParent = FXMLLoader.load(getClass().getResource("/statsScreen.fxml"));
	}
	//endregion fxml loaders
	//region scene navigation
	/**
	 * method called when a level is to be started. This allowed the sharing of
	 * a fxml and controller for speech and math rounds.
	 *
	 * @param event    the event which allows for scene changing
	 * @param gameMode the requested game mode
	 */
	public void startLevel(Event event, int gameMode) {
		Level level = Level.getInstance();
		String levelFXML = "/levelScreen.fxml";
		if (gameMode == GameMode.PRACTICE_MODE.mode()) {
			level.setPractice();
		} else if (gameMode == GameMode.MATH_MODE.mode()){
			if (mathType == null) {
				mathType= "random";
			}
			level.setMathGame(mathType);
		} else {
			level.setPractice();
		}
		FXMLLoader levelLoader;
		Parent levelParent = null;
		try {
			levelLoader = new FXMLLoader(getClass().getResource(levelFXML));
			levelLoader.setController(new LevelController());
			levelParent = levelLoader.load();
		} catch (IOException levelLoadError) {
			levelLoadError.printStackTrace();
		}
		navigate(levelParent, event);
	}

	/**
	 * Method called to start a custom level, from the users'
	 * custom level list
	 *
	 * @param event     the event used for scene navigation
	 * @param questions the array of questions which the user has pre-created
	 */
	public void startCustomLevel(Event event, Question[] questions) {
		Level level = Level.getInstance();
		String levelFXML= "/levelScreen.fxml";
		level.setCustomMathGame(questions);
		FXMLLoader levelLoader;
		Parent levelParent = null;
		try {
			levelLoader = new FXMLLoader(getClass().getResource(levelFXML));
			levelLoader.setController(new LevelController());
			levelParent = levelLoader.load();
		} catch (IOException levelLoadError) {
			levelLoadError.printStackTrace();
		}
		navigate(levelParent, event);
	}

	/**
	 * Navigate home
	 *
	 * @param event  the event which is used for scene navigation
	 */
	public void goHome(Event event) {
		if (homeParent == null) {
			try {
				homeParent = FXMLLoader.load(getClass().getResource("/homeSelector.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		navigate(homeParent, event);
	}

	/**
	 * Navigate to custom game page.
	 *
	 * @param event  the event which is used for scene navigation
	 */
	public void openCustomGame(Event event) {
		try {
			customGameParent = FXMLLoader.load(getClass().getResource("/customGameMaker.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		navigate(customGameParent, event);
	}

	/**
	 * Navigate to favourite games.
	 *
	 * @param event  the event which is used for scene navigation
	 */
	public void openFavouriteGames(Event event) {
		try {
			favouriteGamesParent = FXMLLoader.load(getClass().getResource("/favouriteGames.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		navigate(favouriteGamesParent, event);
	}

	/**
	 * Navigate to final score screen.
	 *
	 * @param event  the event which is used for scene navigation
	 */
	public void openFinalScore(Event event) {
		try {
			finalScoreParent = FXMLLoader.load(getClass().getResource("/finalScoreScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		navigate(finalScoreParent, event);
	}

	/**
	 * Navigate to help.
	 *
	 * @param event  the event which is used for scene navigation
	 */
	public void openHelp(Event event) {
		if (helpParent == null) {
			try {
				helpParent = FXMLLoader.load(getClass().getResource("/helpScreen.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		navigate(helpParent, event);
	}

	/**
	 * Navigate to badges.
	 *
	 * @param event  the event which is used for scene navigation
	 */
	public void openBadges(Event event) {
		try {
			badgeParent = FXMLLoader.load(getClass().getResource("/badgeScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		navigate(badgeParent, event);
	}

	/**
	 * Navigate to stats.
	 *
	 * @param event  the event which is used for scene navigation
	 */
	public void openStats(Event event)  {
		try {
			statsParent = FXMLLoader.load(getClass().getResource("/statsScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		navigate(statsParent, event);
	}

	/**
	 * Navigate to splash screen.
	 *
	 * @param event  the event which is used for scene navigation
	 */
	public void openSplash(Event event)  {
		try {
			splashParent = FXMLLoader.load(getClass().getResource("/splashScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		navigate(splashParent, event);
	}

	/**
	 * Navigate handling for all "open" methods above
	 *
	 * @param parent the parent/fxml loader
	 * @param event  the event which is used for scene navigation
	 */
	void navigate(Parent parent, Event event) {
		Scene scene;
		if (parent.getScene() != null) {
			scene = parent.getScene();
		} else {
			parent.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
			scene = new Scene(parent);
		}
      Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
      window.setWidth(window.getWidth());
      window.setHeight(window.getHeight());
      window.setScene(scene);
      window.show();
	}


	/**
	 * Sets the math operation type for the game.
	 *
	 * @param mathType the math operation type
	 */
	public void setMathType(String mathType) {
		this.mathType = mathType;
	}
	//endregion scene navigation
}
