package softeng206.tatai;

import javafx.scene.Node;

/**
 * .The controller abstract class is used by controllers
 * to share similar functionality.
 *
 * @author Charlie Rillstone
 */
public abstract class Controller {
    protected SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    /**
     * this method is used to show and hide any given of input nodes.
     * usually used for popup dialogs which hold multiple elements.
     *
     * @param show show or hide the elements (show = true)
     * @param args the elements which are being shown or hidden
     */
    protected void show(boolean show, Node... args) {
        for (Node node : args) {
            node.setDisable(!show);
            node.setVisible(show);
        }
    }
}
