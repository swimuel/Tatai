package softeng206.tatai.controllers;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import softeng206.tatai.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The StatsController class is the controller for  
 * statsScreen.fxml. This is the page the user sees when they click
 * the player stats button from the home screen.
 * 
 * @author Sam Broadhead
 * @author Charlie Rillstone
 *
 */
public class StatsController extends Controller{
	@FXML
	private TableView<Score> table;
	@FXML
    private Button speechButton,mathButton;
	@FXML
    private Label firstPlace, secondPlace, thirdPlace,logo,highScoreLabel,scoreHistoryLabel;

	TableColumn<Score, String> name, difficulty, score;
	private HashMap<String, Integer> names;

	/**
	 * called to initialize {@link StatsController} after its root element has been
	 * completely processed. Contains mouse click actions for the elements such as
	 * the speech and math buttons as well as setting up the TableColumns
	 * 
	 * @throws IOException the io exception from any CSV operations
	 */
	@FXML
    void initialize() throws IOException {
      	logo.setOnMouseClicked(sceneNavigator::goHome);
	    mathButton.setOnMouseClicked(e -> {
	    	mathButton.setDisable(true);
	    	speechButton.setDisable(false);
			highScoreLabel.setText("math high scores");
			scoreHistoryLabel.setText("math score history");
	    	table.setItems(readCSV(ScoreIO.MATH_CSV.getLocation()));
	    });
	    speechButton.setOnMouseClicked(e -> {
	    	mathButton.setDisable(false);
	    	speechButton.setDisable(true);
	    	highScoreLabel.setText("speech high scores");
	    	scoreHistoryLabel.setText("speech score history");
	    	table.setItems(readCSV(ScoreIO.PRACTICE_CSV.getLocation()));
	    });
        //set up columns
        name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setPrefWidth(200);
        name.setMinWidth(10);
        name.setMaxWidth(500);
        
        difficulty = new TableColumn<>("Difficulty");
        difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        difficulty.setPrefWidth(200);
        difficulty.setMinWidth(10);  
        difficulty.setMaxWidth(500); 
        
        score = new TableColumn<>("Score (%)");
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        score.setPrefWidth(200);
        score.setMinWidth(10);  
        score.setMaxWidth(500); 
        
        table.getColumns().addAll(name, difficulty, score);
        table.setItems(readCSV(ScoreIO.MATH_CSV.getLocation()));
    }
	
	/**
	 * This is what allows us to show 2 separate tables on one single
	 * screen. By calling this method the TableView will get an updated
	 * observable ArrayList. This method also does some basic sorting to
	 * get the top 3 unique names and their respective scores for the top 3 badges.
	 *  
	 * @param scoresLocation the string location of the scores CSV
	 * @return a new list of scores for the TableView
	 */
	private ObservableList<Score> readCSV(String scoresLocation){
    	names = new HashMap<>();
    	CSVReader reader = new CSVReader(scoresLocation);
    	ObservableList<Score> scores = FXCollections.observableArrayList();
    	String split = ",";
    	String line = "";
    	try {
    		BufferedReader bufferedReader = reader.getBufferedReader();
    		while ((line = bufferedReader.readLine()) != null) {
    			String[] dataOut = line.split(split);
    			scores.add(new Score(dataOut[0], dataOut[1], dataOut[2]));
    			int parsedInt = Integer.parseInt(dataOut[2]);
    			if (names.containsKey(dataOut[0])) {
    				if(names.get(dataOut[0])<parsedInt) {
    					names.put(dataOut[0], parsedInt);
    				}
    			}else {
    				names.put(dataOut[0], parsedInt);
    			}
    		}
    		String[] topNames = new String[names.size()];
    		int[] topScores = new int[names.size()];
    		int i = 0;
    		for(Map.Entry<String,Integer> entry : names.entrySet()) {
    			String key = entry.getKey();
    			Integer value = entry.getValue(); 
    			topNames[i] = key;
    			topScores[i] = value;
    			i++;
    		}
    		for (int j=0; j<topScores.length; j++) {
    			for(int k=1; k<topScores.length-j; k++) {
    				if (topScores[k-1]<topScores[k]) {
    					int temp = topScores[k-1];
    					String tmp = topNames[k-1];
    					topScores[k-1] = topScores[k];
    					topNames[k-1] = topNames[k];
    					topScores[k] = temp;
    					topNames[k] = tmp;
    				}
    			}
    		}
    		if(names.size()>2) {
    			firstPlace.setText(topNames[0]+" "+topScores[0]+"%");
    			secondPlace.setText(topNames[1]+" "+topScores[1]+"%");
    			thirdPlace.setText(topNames[2]+" "+topScores[2]+"%");    			
    		} else if (names.size()>1) {
    			firstPlace.setText(topNames[0]+" "+topScores[0]+"%");
    			secondPlace.setText(topNames[1]+" "+topScores[1]+"%");
    			thirdPlace.setText("");
    		} else if (names.size()>0) {
    			firstPlace.setText(topNames[0]+" "+topScores[0]+"%");
    			secondPlace.setText("");
    			thirdPlace.setText("");
    		} else {
    			firstPlace.setText("");
    			secondPlace.setText("");
    			thirdPlace.setText("");
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return scores;
    }
}
