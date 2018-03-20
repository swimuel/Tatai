package softeng206.tatai.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import softeng206.tatai.*;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * .The FinalScoreController Class is the controller
 * for finalScoreScreen.fxml. It is the page the user visits
 * immediately after completion of a round. It displays their
 * final score in %. Their score is then sent to the respective
 * score csv for persistent storage.
 *
 * @author Charlie Rillstone
 * @author Sam Broadhead
 */
public class FinalScoreController extends Controller {
    @FXML
    private Button homeButton;
    @FXML
    private Label scoreLabel;

    private CSVReader csvReader;
    private Animation countUp;
    private int gameType;
    private int counter;
    private int score;

    /**
     * called to initialize {@link FinalScoreController} after its root element has been
     * completely processed.
     */
    @FXML
    void initialize() throws IOException {
        //region initialisation
        Level level = Level.getInstance();
        User user = User.getInstance();
        gameType = level.getGameMode();
        score = level.getFinalScore();
        counter=0;
        scoreLabel.setText(counter + "%");
        countAnimation();
        csvReader = (gameType == GameMode.PRACTICE_MODE.mode()) ?
                new CSVReader(ScoreIO.PRACTICE_CSV.getLocation()) :
                new CSVReader(ScoreIO.MATH_CSV.getLocation());
        new BadgeHandler(gameType, score);
        //endregion initialisation

        //region button actions
        homeButton.setOnAction(e->{
            try {
                csvReader.updateCSV(user.getCurrentUser().getUserName(),gameType,score);
                sceneNavigator.goHome(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        //endregion button actions
    }
    /**
     * Animation method which gives the effect of the final
     * displayed score counting up, from zero to the score
     * the user received.
     */
    private void countAnimation() {
        countUp = new Timeline(
                new KeyFrame(Duration.millis(20), e -> showScore()));
        countUp.setCycleCount(Timeline.INDEFINITE);
        countUp.play();
    }
    /**
     * Method called by countAnimation method, used to check
     * that the score displayed on the counter is not larger
     * than the score received by the user. animation stops
     * when the counter display is the same as the user score.
     */
    private void showScore() {
        if (counter < score) {
            counter++;
            scoreLabel.setText(counter + "%");
        } else {
            countUp.stop();
        }
    }
}
