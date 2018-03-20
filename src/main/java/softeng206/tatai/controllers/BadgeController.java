package softeng206.tatai.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import javafx.scene.image.ImageView;
import softeng206.tatai.Controller;
import softeng206.tatai.CSVReader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import softeng206.tatai.User;
import javafx.fxml.FXML;

/**
 * .The BadgeController Class is the controller
 * for badgeScreen.fxml. It displays the 9 badges
 * that the user is able to earn, and it triggers
 * a dialog to display when any of the badges are clicked
 *
 * @author Charlie Rillstone
 * @author Sam Broadhead
 */
public class BadgeController extends Controller {
    @FXML
    private ImageView podiumBadge,waterBadge,fireBadge,medalBadge;
    @FXML
    private ImageView trophyBadge,watchBadge,okBadge,mountainBadge,tickBadge,imgDialog,closeDialog;
    @FXML
    private Pane paneDialog;
    @FXML
    private HBox badgeDialog;
    @FXML
    private Label titleDialog,descriptionDialog,logo;
    private ArrayList<String> userBadges;
    private Map<String, ImageView> badgeMap;

    /**
     * called to initialize {@link BadgeController} after its root element has been
     * completely processed.
     */
    @FXML
    void initialize() {
        logo.setOnMouseClicked(sceneNavigator::goHome);
        badgeMap = new HashMap<String, ImageView>() {{
            put("trophyBadge", trophyBadge);
            put("watchBadge", watchBadge);
            put("okBadge", okBadge);
            put("mountainBadge", mountainBadge);
            put("tickBadge", tickBadge);
            put("podiumBadge", podiumBadge);
            put("waterBadge", waterBadge);
            put("fireBadge", fireBadge);
            put("medalBadge", medalBadge);
        }};
        getUserBadges();
        getBadgeData();
        closeDialog.setOnMouseClicked(e->show(false,paneDialog,badgeDialog));
        paneDialog.setOnMouseClicked(e->show(false,paneDialog,badgeDialog));
    }
    /**
     * method is used to read in the badge data from the .badgeInfo.csv file.
     * This includes the name of the badge, the description and the image file
     * location.
     */
    private void getBadgeData() {
        CSVReader csvReader = new CSVReader("./MaoriNumbers/.badgeInfo.csv");
        String split = ",";
        String line = "";
        userBadges = new ArrayList<>();
        try {
            BufferedReader bufferedReader = csvReader.getBufferedReader();
            while ((line = bufferedReader.readLine()) != null) {
                String[] dataOut = line.split(split);
                ImageView badge = badgeMap.get(dataOut[0]);
                badge.setOnMouseClicked(e-> {
                    show(true,paneDialog,badgeDialog);
                    imgDialog.setImage(badge.getImage());
                    titleDialog.setText(dataOut[1]);
                    descriptionDialog.setText(dataOut[2]);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * method used specifically for the reading of badges the current user
     * owns. The user's badges are stored in their respective .badges.csv file.
     * Given they have any badges. Those badges will be shown with full 1.0 opacity
     * to represent an earned badge.
     */
    private void getUserBadges() {
        User user = User.getInstance();
        CSVReader csvReader = new CSVReader(user.getCurrentUser().getBadges().toString());
        String split = ",";
        String line = "";
        userBadges = new ArrayList<>();
        try {
            BufferedReader bufferedReader = csvReader.getBufferedReader();
            while ((line = bufferedReader.readLine()) != null) {
                String[] dataOut = line.split(split);
                userBadges.add(dataOut[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String badge : userBadges) {
            ImageView currentBadge = badgeMap.get(badge);
            currentBadge.setStyle("-fx-opacity: 1.0");
        }
    }
}
