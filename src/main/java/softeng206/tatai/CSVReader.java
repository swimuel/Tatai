package softeng206.tatai;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * the CSVReader class is reponsible for reading and
 * writing to csv files.
 * 
 * @author Sam Broadhead
 * @author Charlie Rillstone
 */
public class CSVReader {
    private final String split = ",";
    private final String nl = "\n";
    private final String line = "";
    private BufferedReader br;
    private String file;
    private String strLevel;
    
    /**
     * simple constructor for a CSVReader
     * @param path the string to the csv file
     */
    public CSVReader(String path) {
        this.file = path;
        br = null;
    }
    
    /**
     * method to get a BufferedReader for the given file of the CSVReader object.
     * 
     * @return a BufferedReader for the supplied csv file
     * @throws IOException if the file is not found
     */
    public BufferedReader getBufferedReader() throws IOException {
        br = new BufferedReader(new FileReader(file));
        return br;
    }
    
    /**
     * A method to update a scores csv with a new score. To be called after
     * the user has successfully finished a round. Appends the data in the
     * agreed on format so it can be read into the stats screen. Each score is on 
     * a new line and the 3 aspects are sorted by commas
     * @param name the name of the user
     * @param level what level they have completed
     * @param score their score from the level
     * @throws IOException if the file is not found
     */
    public void updateCSV(String name, int level, int score) throws IOException {
    	if(level ==1) {
    		strLevel = "practice";
    	}else if(level==2) {
    		strLevel = "random";
    	}else if(level==3) {
    		strLevel = "custom";
    	}else {
    		strLevel ="";
    	}
        FileWriter fileWriter = new FileWriter(file,true);
        fileWriter.append(name);
        fileWriter.append(split);
        fileWriter.append(strLevel);
        fileWriter.append(split);
        fileWriter.append(Integer.toString(score));
        fileWriter.append(nl);
        fileWriter.flush();
        fileWriter.close();
    }
    
    /**
     * The method which will write a new custom game to 
     * the custom games csv. Each custom game is on its own
     * line and each question is separated by a comma.
     * 
     * @param customGame A String array of the custom game, each element of the array is a question
     * @throws IOException if the file is not found
     */
    public void addGame(String[] customGame) throws IOException {
        FileWriter fileWriter = new FileWriter(file,true);
        for (String question : customGame) {
            fileWriter.append(question);
        }
        fileWriter.append(nl);
        fileWriter.flush();
        fileWriter.close();
    }
    
    /**
     * Getter method that will return the name of the custom
     * games.
     * @return an ArrayList of Strings, each string is the name of a custom game.
     * @throws IOException if the file is not found
     */
    public ArrayList<String> getGameNames() throws IOException {
        String empty = "";
        ArrayList<String> names = new ArrayList<>();
        getBufferedReader();
        while ((empty = br.readLine()) != null) {
            String[] dataOut = empty.split(split);
            names.add(dataOut[0]);
        }
        return names;
    }
    
    /**
     * the opposite of addGame, reads in custom games into the appropriate format
     * for the {@link Level} class. The method will split each line (each custom game)
     * into it's individual questions and create an array of {@link Question}.
     * @return a HashMap of all the custom games, key is a String, value
     * is a {@link Question} array.
     * @throws IOException if the file is not found.
     */
	public HashMap<String, Question[]> getGames() throws IOException {
		String empty = "";
		HashMap<String, Question[]> games = new HashMap<>();
		getBufferedReader();
		while ((empty = br.readLine()) != null) {
			String[] gameStr = empty.split(split);
			Question[] questions = new Question[10];
			for (int i=0;i<10; i++) {
				try {
					questions[i] = new Question(gameStr[(3*i)+1]+gameStr[(3*i)+2]+gameStr[(3*i)+3]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			games.put(gameStr[0], questions);
			
		}
    	return games;
	}
}

