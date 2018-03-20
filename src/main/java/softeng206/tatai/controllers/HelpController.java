package softeng206.tatai.controllers;

import javafx.scene.layout.StackPane;
import javafx.scene.control.TextArea;
import softeng206.tatai.Controller;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * .The HelpController Class is the controller
 * for helpScreen.fxml. This is where the user can get help
 * for specific help items. Each help item is a separate
 * StackPane, where clicking an item, brings up a dialog
 * with the respective help text.
 *
 * @author Charlie Rillstone
 */
public class HelpController extends Controller {

    @FXML
    private StackPane helpPane1,helpPane2,helpPane3,helpPane4;
    @FXML
    private StackPane helpPane5,helpPane6,helpPane7;
    @FXML
    private Label helpBoxTitle,logo;
    @FXML
    private Button dismissHelpBox;
    @FXML
    private TextArea helpBoxText;
    @FXML
    private Pane helpPane;
    @FXML
    private VBox helpBox;

    /**
     * called to initialize {@link HelpController} after its root element has been
     * completely processed. Contains Mouse Click actions for each help
     * pane. Including the text which is displayed on the dialog for each
     * mouse click action.
     */
    @FXML
    void initialize() throws IOException {
        logo.setOnMouseClicked(sceneNavigator::goHome);
        helpPane1.setOnMouseClicked(e->{
            show(true,helpPane,helpBox);
            helpBoxTitle.setText("How to start a new game");
            helpBoxText.setText("Navigate to the home page by clicking the \"TATAI!\" logo atop of the window\n\n" +
                    "Click the \"play math round\" tile located on the top left of the window\n\n" +
                    "Select the type of math problems you wish to solve and click \"start\"!\n\n" +
                    "You will be prompted with math problems, solve them by pressing the record button and speaking the answer in Maori.");
        });
        helpPane2.setOnMouseClicked(e->{
            show(true,helpPane,helpBox);
            helpBoxTitle.setText("How to practice your speech");
            helpBoxText.setText("Navigate to the home page by clicking the \"TATAI!\" logo atop of the window\n\n" +
                    "Click the \"practice speech\" tile located on the top row of menu items\n\n" +
                    "You will be prompted with numbers, answer them by pressing the record button and speaking the answer in Maori.");
        });
        helpPane3.setOnMouseClicked(e->{
            show(true,helpPane,helpBox);
            helpBoxTitle.setText("How to make a custom game");
            helpBoxText.setText("Navigate to the home page by clicking the \"TATAI!\" logo atop of the window\n\n" +
                    "Click the \"custom\" tile located on the on the second row of menu items\n\n" +
                    "type the name of your custom level in the provided text field box\n\n" +
                    "fill in the 10 questions by adding in two valid numbers, and select your preferred math operation");
        });
        helpPane4.setOnMouseClicked(e->{
            show(true,helpPane,helpBox);
            helpBoxTitle.setText("How to play a custom game");
            helpBoxText.setText("Navigate to the home page by clicking the \"TATAI!\" logo atop of the window\n\n" +
                    "Click the \"favourite games\" tile located on the on the top row of menu items\n\n" +
                    "select the game you wish to play, by pressing play adjacent your chosen custom game.\n\n" +
                    "answer the questions and have fun!");
        });
        helpPane5.setOnMouseClicked(e->{
            show(true,helpPane,helpBox);
            helpBoxTitle.setText("How to view your statistics");
            helpBoxText.setText("Navigate to the home page by clicking the \"TATAI!\" logo atop of the window\n\n" +
                    "Click the \"player stats\" tile located on the on the second row of menu items\n\n" +
                    "choose whether you want to view practice stats or math stats.\n\n" +
                    "you'll be presented with the top 3 scores and a table of all previous scores.");
        });
        helpPane6.setOnMouseClicked(e->{
            show(true,helpPane,helpBox);
            helpBoxTitle.setText("How to earn badges");
            helpBoxText.setText("Earn badges by playing more and more games.\n\n" +
                    "These games can be math games or just practice rounds.\n\n" +
                    "after you have finished a round, you will be presented with your score, if you have earned a badge, a notification will show");
        });
        helpPane7.setOnMouseClicked(e->{
            show(true,helpPane,helpBox);
            helpBoxTitle.setText("How to view your badges");
            helpBoxText.setText("Navigate to the home page by clicking the \"TATAI!\" logo atop of the window\n\n" +
                    "Click the \"your badges\" tile located on the last row of tiles.\n\n" +
                    "you will be presented by a list of all badges. Badges you have not earned will be greyed out.\n\n" +
                    "click any badge to view more info on that badge and how to earn it! ");
        });
        dismissHelpBox.setOnAction(e->show(false,helpPane,helpBox));
        helpPane.setOnMouseClicked(e->show(false,helpPane,helpBox));
    }
}