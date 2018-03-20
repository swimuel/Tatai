package softeng206.tatai.controllers;

import javafx.beans.value.ObservableValue;
import javafx.animation.FadeTransition;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import softeng206.tatai.BadgeHandler;
import softeng206.tatai.Controller;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import softeng206.tatai.ScoreIO;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import softeng206.tatai.User;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.fxml.FXML;

import java.io.IOException;
import java.io.File;

/**
 * .The SplashController Class is the controller
 * for splashScreen.fxml. The first page the user sees when the program
 * is started. Also where the user logs in/changes user.
 *
 * @author Charlie Rillstone
 * @author Sam Broadhead
 */
public class SplashController extends Controller{
	@FXML
	private Button startButton, newUserButton,submitUserAdd,cancelUserAdd;
	@FXML
	private Label logoTop,logoBottom, loginPrompt, errorLabel;
	@FXML
	private ChoiceBox<String> userChoice;
	@FXML
	private TextField nameInput;
	@FXML
	private Pane dialogPane;
	@FXML
	private VBox dialogBox;
	private User user;

	/**
	 * called to initialize {@link SplashController} after its root element has been
	 * completely processed. Contains Mouse Click actions for the elements
	 * such as the log in dropdown menu.
	 *
	 * @throws IOException the io exception
	 */
	@FXML
	void initialize() throws IOException {
		//region element initialise
		user = User.getInstance();
		File practiceScores = new File(ScoreIO.PRACTICE_CSV.getLocation());
		File mathScores = new File(ScoreIO.MATH_CSV.getLocation());
		practiceScores.createNewFile();
		mathScores.createNewFile();
		sceneNavigator.preLoad();
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), loginPrompt);
		fadeTransition.setCycleCount(Animation.INDEFINITE);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.3);
		fadeTransition.play();
		//endregion element initialise
		//region user management
		String addNewUser = "New User";
		userChoice.getItems().add(addNewUser);
		if (user.getUsers()!= null) {
			for (String key : user.getUsers().keySet()) {
				userChoice.getItems().add(key);
			}
		}
		fadeIn(logoTop,logoBottom,startButton);
		userChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals(addNewUser)) {
				show(true,newUserButton);
				startButton.setDisable(true);
			} else {
				show(false,newUserButton);
				startButton.setDisable(false);
			}
		});
		nameInput.textProperty().addListener(this::changed);
		//endregion user management
		//region Action handling
		startButton.setOnAction(event -> {
			user.changeUser(userChoice.getValue());
			sceneNavigator.goHome(event);
		});
		newUserButton.setOnAction(e-> show(true,dialogBox,dialogPane));
		cancelUserAdd.setOnAction(e-> show(false,dialogBox,dialogPane));
		submitUserAdd.setOnAction(this::submitUser);
		//endregion Action handling
	}
	/**
	 * method used to fade in the elements when the splash screen is loaded
	 *
	 * @param args the elements which are being faded in
	 */
	private void fadeIn(Node... args) {
		for (Node node : args) {
			FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), node);
			fadeTransition.setFromValue(0.0);
			fadeTransition.setToValue(1.0);
			fadeTransition.setCycleCount(1);
			fadeTransition.play();
		}
	}
	/**
	 * event handle for when a new user is submitted
	 *
	 * @param e the action event
	 */
	private void submitUser(ActionEvent e) {
		User newUser = new User(nameInput.getText());
		user.getUsers().put(nameInput.getText(), newUser);
		user.changeUser(nameInput.getText());
		sceneNavigator.goHome(e);
		BadgeHandler badgeHandler = new BadgeHandler();
		badgeHandler.newUserBadge();
	}
	/**
	 * Change listener for listening to the new user input.
	 * Error prevention so users do not add duplicate users or
	 * empty user names etc.
	 *
	 * @param observable observable value
	 * @param oldValue inputField's old value
	 * @param newValue inputField's new value
	 * @see javafx.beans.Observable
	 */
	private void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (!user.getUsers().containsKey(newValue) && newValue.matches("^[a-zA-Z0-9_-]*$") &&
				newValue.length()<11 && !newValue.equals("")) {
			submitUserAdd.setDisable(false);
			errorLabel.setVisible(false);
		} else if(user.getUsers().containsKey(newValue)){
			errorLabel.setText("Name already exists, please choose another one");
			submitUserAdd.setDisable(true);
			errorLabel.setVisible(true);
		} else if(!newValue.matches("^[a-zA-Z0-9_-]*$")) {
			errorLabel.setText("Illegal characters, please try again");
			submitUserAdd.setDisable(true);
			errorLabel.setVisible(true);
		} else if (newValue.length()>10){
			errorLabel.setText("Name too long, please try again");
			submitUserAdd.setDisable(true);
			errorLabel.setVisible(true);
		}else{
			errorLabel.setVisible(false);
		}
	}
}

