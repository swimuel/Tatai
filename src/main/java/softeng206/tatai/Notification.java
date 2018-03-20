package softeng206.tatai;

import org.controlsfx.control.Notifications;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.Map;

/**
 * The Notification class is used for dealing with the displaying and
 * content displayed on push notifications.
 *
 * @author Charlie Rillstone
 */
class Notification {
	private Map<String,String[]> badgeMap;
	private String badgeKey;

	/**
	 * Instantiates a new Notification.
	 *
	 * @param badgeMap the map of badges and the badge data - description, name etc
	 * @param badgeKey the badge which has been called to display
	 */
	Notification(Map<String, String[]> badgeMap, String badgeKey) {
		this.badgeMap = badgeMap;
		this.badgeKey = badgeKey;
		setNotification();
	}
	/**
	 * sets up the notification to show the data which was received from the map
	 * and key.
	 *
	 * @see Notifications
	 */
	private void setNotification() {
		String[] newUserBadge = badgeMap.get(badgeKey);
		Image badgeImage = new Image(getClass().getResource(newUserBadge[2]).toExternalForm());
		ImageView badgeGraphic = new ImageView(badgeImage);
		badgeGraphic.setFitHeight(60);
		badgeGraphic.setFitWidth(60);
		Notifications badgeNotification = Notifications.create()
				.title("New Badge! " + newUserBadge[0]).text(newUserBadge[1])
				.graphic(badgeGraphic).hideAfter(Duration.seconds(5));
		badgeNotification.show();
	}
}
