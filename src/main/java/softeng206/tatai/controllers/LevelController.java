package softeng206.tatai.controllers;

import javafx.animation.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.concurrent.Service;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

import javafx.concurrent.Task;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.geometry.Insets;

import org.controlsfx.control.PopOver;
import softeng206.tatai.*;

import java.io.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Random;

/**
 * The LevelController Class is the controller for levelScreen.fxml.
 * This is the page where the user answers questions.
 * 
 * @author Sam Broadhead
 * @author Charlie Rillstone
 *
 */
public class LevelController extends Controller{
	@FXML
	private Circle indicator1,indicator2,indicator3,indicator4,indicator5;
	@FXML
	private Circle indicator6,indicator7,indicator8,indicator9,indicator10;
	@FXML
	private ImageView recordImage,playImage,nextImage;
	@FXML
	private Label numberTested,correctAnswerLabel,logo;
	@FXML
	private Rectangle recordingView;
	@FXML
	private Button playButton,nextButton,recordButton;
	@FXML
	private StackPane dialogPane,questionRectangle;

	private String finalAns, blue,red, orange;
	private HashMap<Integer, Double> scorePerQuestion;
	private Question[] questions = new Question[10];
	private HashMap<Integer, String> numberMap;
	private int questionIndex;
	private MediaPlayer mediaPlayer;
	private Boolean secondAttempt,correct;
	private Circle[] indicators;
	private Media media;
	private Level level;

	private final String cardStyle = "-fx-background-radius:6;" +
			"-fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.1),3,0.0,0,3);";
	private Image microphone,stop,finish;

	/**
	 * called to initialize {@link LevelController} after its root element has
	 * been completely processed. Some simple file loading and button actions
	 * are set here.
	 * @throws IOException
	 */
	@FXML
	void initialize() throws IOException {
		//region Initial Configuration
		level = Level.getInstance();
		questions = level.getQuestions();
		indicators = new Circle[]{indicator1,indicator2,indicator3,indicator4,indicator5,
				indicator6,indicator7,indicator8,indicator9,indicator10};
		Font.loadFont(getClass().getResource("/fonts/theboldfont.ttf").toExternalForm(), 50);
		microphone = new Image(getClass().getResource("/icons/mic.png").toExternalForm());
		stop = new Image(getClass().getResource("/icons/stop.png").toExternalForm());
		finish = new Image(getClass().getResource("/icons/finish.png").toExternalForm());

		MaoriNumbers maoriNumbers = new MaoriNumbers();
		numberMap = maoriNumbers.getNumbers();
		scorePerQuestion = new HashMap<>();
		secondAttempt = false;
		orange = "#FFC107;";
		blue = "#43ACFF";
		red = "#E53935;";
		questionIndex = 0;
		setQuestionDisplay();
		indicators[questionIndex].setStyle("-fx-fill: "+ blue +";");

		//endregion Initial Configuration
		recordButton.setOnAction(e -> recordClick());
		logo.setOnMouseClicked(this::homeConfirm);
		playButton.setOnAction(event -> {
			playButton.setDisable(true);
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
			mediaPlayer.setOnEndOfMedia(() -> {
				mediaPlayer.dispose();
				playButton.setDisable(false);
			});
		});
	}
	//region Record and voice recognition logic

	/**
	 * The method that handles the UI response when
	 * the user hits the record button and also initiates
	 * the record.
	 */
	@FXML
	private void recordClick() {
		nextButton.setDisable(true);
		questionRectangle.setStyle("-fx-background-color: white;" + cardStyle);
		numberTested.setStyle("-fx-text-fill: #37474f;");
		recordButton.setDisable(true);
		recordButton.setStyle("-fx-background-color: #FE5981");
		recordImage.setImage(stop);
		playButton.setDisable(true);
		recordingView.setWidth(0);
		recordingView.setVisible(true);
		showRecordInfo();
		Timeline viewRecord = showRecordInfo();
		viewRecord.play();
		try {
			record();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//endregion Record and voice recognition logic

	//region Record transition effects

	/**
	 * The method that handles the UI view when recording
	 * @return a Timeline of KeyFrames for the recording event
	 */
	private Timeline showRecordInfo() {
		Timeline recordViewTimeline = new Timeline();
		recordViewTimeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(recordingView.visibleProperty(), true)),
				new KeyFrame(Duration.ZERO, new KeyValue(recordingView.widthProperty(), 0)),
				new KeyFrame(Duration.seconds(3), new KeyValue(recordingView.widthProperty(), 411)),
				new KeyFrame(Duration.millis(500)));
		return recordViewTimeline;
	}
	//endregion Record transition effects

	//region Display of user result
	/**
	 * The method called when the result is correct.
	 * Will set the appropriate colours and also enable
	 * some buttons such as next and playback.
	 */
	private void correctUserInput() {
		correct = true;
		recordImage.setImage(microphone);
		recordingView.setVisible(false);
		recordButton.setDisable(true);
		playButton.setDisable(false);
		indicators[questionIndex].setStyle((secondAttempt) ? "-fx-fill: " + orange : "-fx-fill: #1DE97E");
		questionRectangle.setStyle("-fx-background-color: #1DE97E; "+ cardStyle);
		numberTested.setStyle("-fx-text-fill: white");
		flashResult();
		nextButton.setDisable(false);
		nextButton.setOnAction(checkLast() ? this::handleCorrectFinish : this::handleCorrectNext);
	}

	/**
	 * The method called when the result is incorrect.
	 * Will set the appropriate colours and also enable
	 * some buttons such as retry, next and playback. If this
	 * is their second then the answer will be shown.
	 */
	private void incorrectUserInput() {
		correct = false;
		shakeTransition(numberTested);
		recordingView.setVisible(false);
		recordImage.setImage(microphone);
		playButton.setDisable(false);
		indicators[questionIndex].setStyle("-fx-fill: #FE5981");
		questionRectangle.setStyle("-fx-background-color: #E53935; " + cardStyle);
		numberTested.setStyle("-fx-text-fill: white");
		nextButton.setDisable(false);
		flashResult();
		if (secondAttempt) {
			checkLast();
			recordButton.setDisable(true);
			secondAttempt=true;
			Label correctAnswer = new Label("Answer is: " + numberMap.get(questions[questionIndex].getScore()));
			correctAnswer.setStyle("-fx-font-size: 14; -fx-font-family: sans-serif");
			correctAnswer.setPadding(new Insets(0, 10, 0, 10));
			PopOver answerPrompt = new PopOver(correctAnswer);
			answerPrompt.show(numberTested);
			answerPrompt.setDetachable(false);
		} else {
			secondAttempt=true;
			recordButton.setDisable(false);
		}
		nextButton.setOnAction(checkLast() ? this::handleCorrectFinish : this::handleCorrectNext);
	}
	//endregion Display of user result

	/**
	 * Iterate through the test, display the next question
	 * and fill in the indicators, also check if the quiz is
	 * finished.
	 */
	private void iterateTest()  {
		questionIndex++;
		recordButton.setDisable(false);
		if (questionIndex < 10) {
			setQuestionDisplay();
			indicators[questionIndex].setStyle("-fx-fill: " + blue);
			secondAttempt = false;
		} else {
			level.finishQuiz(scorePerQuestion);
			recordButton.setDisable(true);
		}
	}

	/**
	 * Makes bash calls and attempts to run a series of commands
	 * to record speech and analysis it using HTK (see GoSpeech for more details
	 * on the bash stuff). Done in a background process. Once finished the answer will
	 * be checked against the map of correct answers.
	 * @throws IOException if there is a problem with the ProcessBuilder
	 */
	private void record() throws IOException {
		ProcessBuilder ffRecord = new ProcessBuilder("bash", "-c", "./MaoriNumbers/GoSpeech");
		Process recordProcess = ffRecord.start();
		Service<Void> bgThread = new Service<Void>() {
			protected Task<Void> createTask() {
				return new Task<Void>() {
					protected Void call() {
						try {
							recordProcess.waitFor();
						} catch (InterruptedException recordError) {
							recordError.printStackTrace();
						}
						return null;
					}
				};
			}
		};
		bgThread.setOnSucceeded(event1 -> {
			File audio = new File("./MaoriNumbers/.sound.wav");
			media = new Media(audio.toURI().toString());
			try {
				getFinalAns();
			} catch (IOException e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (numberMap.get(questions[questionIndex].getScore()).equals(finalAns)) {
				correctUserInput();
			} else {
				incorrectUserInput();
			}
		});
		bgThread.restart();
	}

	/**
	 * read the final answer from the machine learning file
	 * generated by HTK, split the text and get the final answer
	 * in a format that can be compared to the number map.
	 * @throws IOException if the file is not found
	 */
	private void getFinalAns() throws IOException {
		FileReader fr = new FileReader("./MaoriNumbers/.recout.mlf");
		BufferedReader br = new BufferedReader(fr);
		StringBuilder words = new StringBuilder();
		String sCurrentLine;
		finalAns = "";
		while ((sCurrentLine = br.readLine())!= null) {
			words.append(sCurrentLine).append(" ");
		}
		String regexStr = Pattern.quote("sil ")+"(.*?)"+ Pattern.quote(" sil");
		Pattern pattern = Pattern.compile(regexStr);
		Matcher matcher = pattern.matcher(words.toString());
		while (matcher.find()) {
			finalAns = matcher.group(1);
		}
		br.close();
		fr.close();
	}

	/*for testing*/
	/**
	 * USE FOR TESTING ONLY, ignores the recording and randomly picks
	 * if the "answer" was correct or incorrect. Used when our Maori
	 * wasn't that great.	
	 */
	private void getFakeFinalAns() {
		Random random = new Random();
		if (random.nextBoolean()) {
			correctUserInput();
		} else {
			incorrectUserInput();
		}
	}

	/**
	 * Set the question, depending on its string length,
	 * this method will dynamically change size.
	 */
	private void setQuestionDisplay() {
		int length = questions[questionIndex].display().length();
		numberTested.getStyleClass().removeAll(
				"currentTestSmall",
				"currentTestMedium");
		numberTested.getStyleClass().add((length > 6) ?
				"currentTestSmall" : (length > 4) ?
						"currentTestMedium" : "currentTest");
		numberTested.setText(questions[questionIndex].display());
	}

	/**
	 * Reset the card style after the result has been flashed 
	 * on the screen for 3 seconds.
	 */
	private void flashResult() {
		PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
		visiblePause.setOnFinished(event ->{
			questionRectangle.setStyle("-fx-background-color: white; " + cardStyle);
			numberTested.setStyle("-fx-text-fill: #37474F");
		});
		visiblePause.play();
	}
	/**
	 * Shake the node provided. Used when the answer is incorrect.
	 * @param node the Node to be shaken.
	 */
	private void shakeTransition(Node node) {
		Timeline shakeTimeline = new Timeline();
		shakeTimeline.getKeyFrames().addAll(
				new KeyFrame(Duration.millis(0), new KeyValue(node.translateXProperty(), 0)),
				new KeyFrame(Duration.millis(100), new KeyValue(node.translateXProperty(), -10)),
				new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), 10)),
				new KeyFrame(Duration.millis(300), new KeyValue(node.translateXProperty(), -10)),
				new KeyFrame(Duration.millis(400), new KeyValue(node.translateXProperty(), 10)),
				new KeyFrame(Duration.millis(500), new KeyValue(node.translateXProperty(), -10)),
				new KeyFrame(Duration.millis(600), new KeyValue(node.translateXProperty(), 10)),
				new KeyFrame(Duration.millis(700), new KeyValue(node.translateXProperty(), -10)),
				new KeyFrame(Duration.millis(800), new KeyValue(node.translateXProperty(), 10)),
				new KeyFrame(Duration.millis(900), new KeyValue(node.translateXProperty(), -10)),
				new KeyFrame(Duration.millis(1000), new KeyValue(node.translateXProperty(), 0))
				);
		shakeTimeline.play();
	}

	/**
	 * check if the current question is the last question
	 * @return true if it is the last, false if not.
	 */
	private boolean checkLast() {
		if (questionIndex == 9) {
			nextImage.setImage(finish);
			return true;
		}
		return false;
	}

	/**
	 * Confirm that the user wants to go home upon clicking the 
	 * home button. Show them an alert letting them know their progress will
	 * be lost.
	 * @param e the mouse event, use to navigate back home
	 */
	private void homeConfirm(MouseEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Go to home page");
		alert.setHeaderText("You will lose your current game progress");
		alert.setContentText("Proceed?");
		alert.getDialogPane().setMaxSize(300,350);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			sceneNavigator.goHome(e);
		}
	}

	/**
	 * reset the look of the question to default.
	 */
	private void defaultLook() {
		questionRectangle.setStyle("-fx-background-color: white;" + cardStyle);
		numberTested.setStyle("-fx-text-fill: #37474f;");
		playButton.setDisable(true);
		nextButton.setDisable(true);
	}

	/**
	 * called when a question is finished, reset the look, write the score
	 * and continue to next question
	 * @param e
	 */
	private void handleCorrectNext(ActionEvent e) {
		defaultLook();
		putScore(correct);
		iterateTest();
		recordButton.setDisable(false);
	}

	/**
	 * called when the entire quiz is finished,
	 * change scene to the final score screen.
	 * @param e
	 */
	private void handleCorrectFinish(ActionEvent e) {
		putScore(correct);
		iterateTest();
		sceneNavigator.openFinalScore(e);
	}

	/**
	 * write the score to the score map, 1 point for correct,
	 * 0.5 points for correct on second attempt and no points
	 * for incorrect twice.
	 * @param correct
	 */
	private void putScore(boolean correct) {
		if (secondAttempt && correct) {
			scorePerQuestion.put(questionIndex + 1, 0.5);
		} else if (correct){
			scorePerQuestion.put(questionIndex + 1, 1.0);
		} else {
			scorePerQuestion.put(questionIndex + 1, 0.0);
		}
	}
	//endregion Level Configuration
}
