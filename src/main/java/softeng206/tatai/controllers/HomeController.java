package softeng206.tatai.controllers;

import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.fxml.FXML;
import softeng206.tatai.Controller;
import softeng206.tatai.GameMode;
import softeng206.tatai.User;

import java.io.IOException;
import java.io.File;

/**
 * The HomeController Class is the controller for homeSelector.fxml.
 * It displays the home screeen and presents the user with their options
 * and triggers the appropriate actions when a Button/StackPane is clicked.
 *  
 * @author Sam Broadhead
 * @author Charlie Rillstone
 */
public class HomeController extends Controller{
	@FXML
	private RadioButton randomSelect,additionSelect,subtractionSelect,multiplicationSelect,divisionSelect;
	@FXML
	private StackPane levelOnePane, statsPane,customGame, favouriteGames,mathRound,badges;
	@FXML
	private Button cancelMath, startMath;
	@FXML
	private ImageView logout, help;
	@FXML
	private ToggleGroup radioGroup;
	@FXML
	private VBox playMathDialog;
	@FXML
	private Pane dialogPane;

	/**
	 * called to initialize {@link HomeController} after its root element
	 * has been completely processed.
	 * 
	 * @throws IOException io exception if encountered
	 */
	@FXML
	void initialize() throws IOException {
		//region initial configuration
		User user = User.getInstance();
		Tooltip logoutTip = new Tooltip("logout");
		Tooltip.install(logout,logoutTip);
		radioGroup = new ToggleGroup();
		radioGroup.getToggles().addAll(randomSelect,additionSelect,subtractionSelect,multiplicationSelect,divisionSelect);
		//endregion initial configuration
		//region navigation tiles
		statsPane.setOnMouseClicked(sceneNavigator::openStats);
		help.setOnMouseClicked(sceneNavigator::openHelp);
		logout.setOnMouseClicked(sceneNavigator::openSplash);
		customGame.setOnMouseClicked(sceneNavigator::openCustomGame);
		favouriteGames.setOnMouseClicked(sceneNavigator::openFavouriteGames);
		badges.setOnMouseClicked(sceneNavigator::openBadges);
		levelOnePane.setOnMouseClicked(e -> sceneNavigator.startLevel(e, GameMode.PRACTICE_MODE.mode()));
		mathRound.setOnMouseClicked(e-> {
			show(true,playMathDialog,dialogPane);
			startMath.setDisable(radioGroup.getSelectedToggle() == null);
			radioGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> startMath.setDisable(false));
			startMath.setOnAction(e2 -> {
				RadioButton selected = (RadioButton) radioGroup.getSelectedToggle();
				sceneNavigator.setMathType(selected.getText());
				show(false,playMathDialog,dialogPane);
				startMath.setDisable(true);
				if (radioGroup.getSelectedToggle() !=null ) {
					radioGroup.getSelectedToggle().setSelected(false);
				}
				sceneNavigator.startLevel(e,GameMode.MATH_MODE.mode());
			});
			cancelMath.setOnAction(e3-> hideDialog());
			dialogPane.setOnMouseClicked(e3-> hideDialog());
		});
		//endregion navigation tiles
	}

	/**
	 * method to hide the math option dialog
	 */
	private void hideDialog() {
		if (radioGroup.getSelectedToggle() !=null ) {
			radioGroup.getSelectedToggle().setSelected(false);
		}
		show(false,playMathDialog,dialogPane);
		startMath.setDisable(true);
	}
}


