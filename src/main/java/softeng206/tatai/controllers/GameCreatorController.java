package softeng206.tatai.controllers;

import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import softeng206.tatai.*;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.ScriptEngine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.io.File;

/**
 * The GameCreatorController Class is the controller for 
 * customGameMaker.fxml. This is the page where the user will enter
 * in their custom game.
 *  
 * @author Sam Broadhead
 * @author Charlie Rillstone
 */
public class GameCreatorController extends Controller {
	@FXML
	private Label customNameError,eqError1,eqError2,eqError3,eqError4,eqError5,eqError6,eqError7,eqError8,eqError9,eqError10,logo;
	@FXML
	private ImageView nameVerify,verify1,verify2,verify3,verify4,verify5,verify6,verify7,verify8,verify9,verify10;
	@FXML
	private TextField gameNameInput,q1VarA,q2VarA,q3VarA,q4VarA,q5VarA,q6VarA,q7VarA,q8VarA,q9VarA,q10VarA;
	@FXML
	private TextField q1VarB,q2VarB,q3VarB,q4VarB,q5VarB,q6VarB,q7VarB,q8VarB,q9VarB,q10VarB;
	@FXML
	private ToggleGroup q1,q2,q3,q4,q5,q6,q7,q8,q9,q10;
	@FXML
	private Button addGameButton,cancelAddButton;
	private ArrayList<String> gameNames;
	private String[] questions;
	private boolean goodName, goodQuestions;

	/**
	 * called to intialize {@link GameCreatorController} after its root element has
	 * been completely processed. Sets the mouse click actions for Buttons and
	 * a listener for the game name.
	 * @throws IOException
	 */
	@FXML
	void initialize() throws IOException {
		//region initialisation
		User user = User.getInstance();
		questions = new String[11];
		questions[0] = "Custom Game,";
		addGameButton.setDisable(true);
		goodName = false;
		goodQuestions = false;
		BadgeHandler badgeHandler = new BadgeHandler();
		String customGameLocation = user.getCurrentUser().getCustomGames().toString();
		CSVReader csvReader = new CSVReader(customGameLocation);
		File customGameCSV = new File(customGameLocation);
		customGameCSV.createNewFile();
		if (csvReader.getGameNames() != null) {
			gameNames = csvReader.getGameNames();
			questions[0] = "Custom Game "+ gameNames.size() + 1 + ",";
		}
		inputRow(q1VarA,q1VarB,q1,verify1,eqError1,1);
		inputRow(q2VarA,q2VarB,q2,verify2,eqError2,2);
		inputRow(q3VarA,q3VarB,q3,verify3,eqError3,3);
		inputRow(q4VarA,q4VarB,q4,verify4,eqError4,4);
		inputRow(q5VarA,q5VarB,q5,verify5,eqError5,5);
		inputRow(q6VarA,q6VarB,q6,verify6,eqError6,6);
		inputRow(q7VarA,q7VarB,q7,verify7,eqError7,7);
		inputRow(q8VarA,q8VarB,q8,verify8,eqError8,8);
		inputRow(q9VarA,q9VarB,q9,verify9,eqError9,9);
		inputRow(q10VarA,q10VarB,q10,verify10,eqError10,10);
		//endregion initialisation
		//region button action events
		logo.setOnMouseClicked(sceneNavigator::goHome);
		cancelAddButton.setOnAction(sceneNavigator::goHome);
		addGameButton.setOnAction(addGameEvent -> {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Custom game confirmation");
			alert.setHeaderText("This will add a custom level, located in \"favourite games\"");
			alert.setContentText("Add custom game?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				try {
					questions[0] = (!noText(gameNameInput)) ? "Custom Game," : gameNameInput.getText();
					csvReader.addGame(questions);

					badgeHandler.firstCustomBadge();
				} catch (IOException gameToCSVError) {
					gameToCSVError.printStackTrace();
				}
				sceneNavigator.goHome(addGameEvent);
			} else {
				alert.close();
			}
		});
		//endregion button action events
		gameNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!gameNames.contains(newValue) && newValue.matches("^[a-zA-Z0-9_ -]*$") &&
					newValue.length()<16 && !newValue.equals("")) {
				nameVerify.setVisible(true);
				customNameError.setVisible(false);
				goodName = true;
				readyToPlay();
			} else if(gameNames.contains(newValue)){
				customNameError.setText("Name already exists, please choose another one");
				nameVerify.setVisible(false);
				customNameError.setVisible(true);
				goodName = false;
				readyToPlay();
			} else if(!newValue.matches("^[a-zA-Z0-9_ -]*$")) {
				customNameError.setText("Illegal characters, please try again");
				nameVerify.setVisible(false);
				customNameError.setVisible(true);
				goodName = false;
				readyToPlay();
			} else if (newValue.length()>15){
				customNameError.setText("Name too long, please try again");
				nameVerify.setVisible(false);
				customNameError.setVisible(true);
				goodName = false;
				readyToPlay();
			}else {
				nameVerify.setVisible(false);
				customNameError.setVisible(false);
				goodName = false;
				readyToPlay();
			}
		});
	}

	/**
	 * The intermediary method for handling input change of the custom questions.
	 * Used to test the inputs in both orders in case the user doesn't fill out left
	 * to right.
	 * 
	 * @param inputA the left TextField on the screen 
	 * @param inputB the right TextField on the screen
	 * @param mathToggle the entire block of toggles (add/subtract/divide/multiply)
	 * @param verifyTick the tick to verify an answer
	 * @param errorLabel the Label to let the user know what's wrong, if anything
	 * @param index the index position of the question
	 */
	private void inputRow(TextField inputA, TextField inputB, ToggleGroup mathToggle,
						  ImageView verifyTick, Label errorLabel,int index) {
		handleInputChange(inputA,inputB,mathToggle,errorLabel,verifyTick,false, index); //handle for any input order
		handleInputChange(inputB,inputA,mathToggle,errorLabel,verifyTick,true, index); //handle for regular input order
	}
	//region Validation Calculations

	/**
	 * A method to calculate the answer of a string equation.
	 * 
	 * @param equation the string equation to be calculated 
	 * @return a Double, the result of the equation
	 * @throws ScriptException if there is a problem with the JavaScript
	 */
	private Double calculate(String equation) throws ScriptException {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
		return Double.parseDouble(scriptEngine.eval(equation).toString());
	}

	/**
	 * A simple check if the equation has a valid answer.
	 * 
	 * @param answer a Double representing the answer of an equation
	 * @return a Boolean, true if valid, false if not.
	 */
	private Boolean validEquation(Double answer) {
		return (answer % 1 == 0) && (answer > 0 && answer < 100);
	}

	/**
	 * A method to handle the invalid equations, if the question is
	 * valid then it will be written to the array
	 * 
	 * @param input the answer of the equation
	 * @param errorLabel the label where any errors will be displayed
	 * @param tick the image tick, will be displayed if no errors
	 * @param index number what question is being entered
	 * @param equation the equation being checked
	 */
	private void handleErrors(Double input,Label errorLabel, ImageView tick, int index, String equation) {
		Boolean empty = false;
		if (validEquation(input)) {
			tick.setVisible(true);
			errorLabel.setVisible(false);
			questions[index] = equation;
			for (String element : questions) {
				if (element == null) {
					empty = true;
					break;
				}
			}
			if (!empty) {
				goodQuestions = true;
				readyToPlay();
			}
		} else {
			tick.setVisible(false);
			errorLabel.setVisible(true);
			errorLabel.setText("invalid equation!");
			goodQuestions = false;
			readyToPlay();
		}
	}

	/**
	 * Check if the provided TextField has text
	 * @param textField the TextField to be checked
	 * @return a boolean - true if the TextField has no text, false if not
	 */
	private boolean noText(TextField textField) {
		return (textField.textProperty().isNotEmpty().get() && !textField.getText().trim().equals(""));
	}
	//endregion Validation Calculations
	//region change listeners

	/**
	 * The method that handles the change in values for each question. This method checks the 
	 * equation that is supplied to it using a combination of other methods. It will also supply
	 * feedback to the user if the equation they have entered is invalid.
	 * 
	 * @param textField1 the TextField of the first number
	 * @param textField2 the TextField of the second number
	 * @param toggleGroup the entire block of toggles (add/subtract/divide/multiply)
	 * @param errorLabel the Label to let the user know what's wrong, if anything
	 * @param verifyTick the tick to verify an answer
	 * @param normalOrder whether or not the question has been reversed
	 * @param index the index position of the question
	 */
	private void handleInputChange(TextField textField1,TextField textField2, ToggleGroup toggleGroup,
								   Label errorLabel, ImageView verifyTick, boolean normalOrder, int index) {
		toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (noText(textField1) && noText(textField2) && !(newValue==null)) {
				ToggleButton button = (ToggleButton) newValue;
				String eqType = button.getText();
				eqType = (eqType.equals("x")) ? "*" : (eqType.equals("÷")) ? "/" : button.getText();
				try {
					Double result;
					String storedEquation;
					if(normalOrder) {
						result = calculate(textField2.getText() + eqType + textField1.getText());
						storedEquation = ","+textField2.getText() +","+ eqType +","+ textField1.getText();
					} else {
						result = calculate(textField1.getText() + eqType + textField2.getText());
						storedEquation = ","+textField1.getText() +","+ eqType +","+ textField2.getText();
					}
					handleErrors(result, errorLabel, verifyTick, index, storedEquation);
				} catch (ScriptException e) {
					e.printStackTrace();
				}
			}
		});
		textField1.textProperty().addListener((observable, oldValue, newValue) -> {
			if (textField1.getText().length() > 7) {
				//restrict large numbers inspired by @ceklock
				String s = textField1.getText().substring(0, 7);
				textField1.setText(s);
			}
		});
		textField2.textProperty().addListener((observable, oldValue, newValue) -> {
			if (textField2.getText().length() > 7) {
				//restrict large numbers inspired by @ceklock
				String s = textField2.getText().substring(0, 7);
				textField2.setText(s);
			}
		});
		textField1.textProperty().addListener((observable, oldValue, newValue) -> {
			if (noText(textField2) && !newValue.trim().equals("") && toggleGroup.getSelectedToggle() != null) {
				if (textField2.getText().matches("[0-9.]*") && newValue.matches("[0-9.]*")) {
					ToggleButton button = (ToggleButton) toggleGroup.getSelectedToggle();
					String eqType = button.getText();
					eqType = (eqType.equals("x")) ? "*" : (eqType.equals("÷")) ? "/" : button.getText();
					String inputAValue = textField2.getText();
					try {
						Double result;
						String equation;
						String storedEquation;
						if (normalOrder) {
							equation = inputAValue + eqType + newValue;
							result = calculate(equation);
							storedEquation = ","+inputAValue +","+ eqType +","+ newValue;
						} else {
							equation = newValue+ eqType + inputAValue;
							result = calculate(equation);
							storedEquation = ","+newValue +","+ eqType +","+ inputAValue;
						}
						handleErrors(result, errorLabel, verifyTick, index, storedEquation);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
				} else {
					verifyTick.setVisible(false);
					errorLabel.setVisible(true);
					errorLabel.setText("invalid equation!");
				}
			}
		});
	}
	//endregion change listeners
	/**
	 * A simple checker that the conditions of a valid question list have been met.
	 * The game cannot be added until these conditions are met (all questions valid
	 * and name also valid)
	 */
	private void readyToPlay() {
		if(goodName && goodQuestions) {			
			addGameButton.setDisable(false);
		} else {
			addGameButton.setDisable(true);
		}
	}
}