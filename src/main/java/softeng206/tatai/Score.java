package softeng206.tatai;

import javafx.beans.property.SimpleStringProperty;

/**
 * Simple score object to show on the stats page,
 * required so the table will display properly.
 * 
 * @author Sam Broadhead
 */
public class Score {
	public SimpleStringProperty name, difficulty, score;

	/**
	 * The constructor for a score object, each parameter is
	 * a column in the table
	 * @param name the name of the person
	 * @param difficulty what difficulty they were on
	 * @param score their score from the round
	 */
	public Score (String name, String difficulty, String score) {
		this.name = new SimpleStringProperty(name);
		this.difficulty = new SimpleStringProperty(difficulty);
		this.score = new SimpleStringProperty(score);
	}

	/**
	 * These methods are required because of the nature 
	 * of the StatsController and how the observable list 
	 * works. 
	 * 
	 * @return the name of the person
	 */
	public String getName() {
		return name.get();
	}

	/**
	 * method to get the difficulty
	 * 
	 * @return what difficulty they were on
	 */
	public String getDifficulty() {
		return difficulty.get();
	}

	/**
	 * method to get the score
	 * 
	 * @return the users score
	 */
	public String getScore() {
		return score.get();
	}
}