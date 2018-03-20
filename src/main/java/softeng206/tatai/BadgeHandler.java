package softeng206.tatai;

import java.io.BufferedReader;
import java.util.Collections;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.io.File;

/**
 * .The BadgeHandler Class handle all badge logic. That is
 * providing users with badges given they make an achievement.
 * a badgeHandler object is usually created after a user completes
 * something (i.e. finish a game). The handler then takes in parameters
 * e.g. their score, and checks if they have earned a badge or not.
 *
 * @author Charlie Rillstone
 * @author Sam Broadhead
 */
public class BadgeHandler {

	private final Map<String, String[]> badgeMap = new HashMap<>();
	private HashSet<String> nameSet = new HashSet<>();
	private ArrayList<String> practiceScores;
	private ArrayList<Integer> practiceIntScores= new ArrayList<>();
	private ArrayList<String> mathScores;
	private ArrayList<Integer> mathIntScores= new ArrayList<>();
	private ArrayList<String> userBadges;
	private User user;
	private int score;

	/**
	 * Instantiates a new Badge handler. This BadgeHandler constructor is used in game
	 * controllers, as the gameType and score is passed in. The constructor will
	 * receive all scores from over time for later comparing with the current score provided.
	 *
	 * @param gameType the type of game the user played
	 * @param score    the score the user got from that game
	 */
	public BadgeHandler(int gameType, int score) {
		this.user = User.getInstance().getCurrentUser();
		this.score = score;
		userBadges = parseCSV(user.getBadges().toString(),0);
		if (badgeMap == null || badgeMap.size() < 2) getBadgeInfo();

		ArrayList<String> names;
		if (gameType == GameMode.PRACTICE_MODE.mode()) {
			practiceScores = parseCSV(ScoreIO.PRACTICE_CSV.getLocation(),2);
			for(String scr : practiceScores) {
				practiceIntScores.add(Integer.parseInt(scr));
			}
			Collections.sort(practiceIntScores);
			Collections.reverse(practiceIntScores);
			names = parseCSV(ScoreIO.PRACTICE_CSV.getLocation(),0);
			nameSet.addAll(names);
			practiceBadge();
		} else {
			mathScores = parseCSV(ScoreIO.MATH_CSV.getLocation(),2);
			for(String scr: mathScores) {
				mathIntScores.add(Integer.parseInt(scr));
			}
			Collections.sort(mathIntScores);
			Collections.reverse(mathIntScores);
			names = parseCSV(ScoreIO.MATH_CSV.getLocation(),0);
			nameSet.addAll(names);
			mathBadge();
		}
	}

	/**
	 * Instantiates a new Badge handler. This BadgeHandler constructor is used for user badges
	 * (e.g. new user badge and game creation badge). This BadgeHandler object can then be used to
	 * call specific badge methods (e.g. {@link BadgeHandler#firstCustomBadge()})
	 */
	public BadgeHandler() {
		this.user = User.getInstance().getCurrentUser();
		if (badgeMap == null || badgeMap.size() < 2) getBadgeInfo();
		userBadges = new ArrayList<>();
		userBadges = parseCSV(user.getBadges().toString(),0);
	}
	/**
	 * method used for handling practice badges given to users
	 */
	private void practiceBadge() {
		if (!userBadges.contains("waterBadge")) {
			new Notification(badgeMap,"waterBadge");
			try {
				addBadge(this.user.getBadges(), "waterBadge");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(nameSet.size()>3) { //must be at least 3 other plays so there should be 4 unique names        	
			if (score >= practiceIntScores.get(0) && !userBadges.contains("medalBadge")) {
				new Notification(badgeMap,"medalBadge");
				try {
					addBadge(this.user.getBadges(), "medalBadge");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(practiceScores.size()>4) { //must be at least 5 scores present        	
			if (score >= practiceIntScores.get(2) && !userBadges.contains("mountainBadge")) {
				new Notification(badgeMap,"mountainBadge");
				try {
					addBadge(this.user.getBadges(), "mountainBadge");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (score == 100 && !userBadges.contains("watchBadge")) {
			new Notification(badgeMap,"watchBadge");
			try {
				addBadge(this.user.getBadges(), "watchBadge");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * method used for handling math badges given to users
	 */
	private void mathBadge() {
		if(mathScores.size()>4) { //must be at least 5 scores present        	
			if (score >= mathIntScores.get(2) && !userBadges.contains("podiumBadge")) {
				new Notification(badgeMap,"podiumBadge");
				try {
					addBadge(this.user.getBadges(), "podiumBadge");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(nameSet.size()>3) { //must be at least 3 other plays so there should be 4 unique names        	
			if (score >= mathIntScores.get(0) && !userBadges.contains("trophyBadge")) {
				new Notification(badgeMap,"trophyBadge");
				try {
					addBadge(this.user.getBadges(), "trophyBadge");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (score == 100 && !userBadges.contains("tickBadge")) {
			new Notification(badgeMap,"tickBadge");
			try {
				addBadge(this.user.getBadges(), "tickBadge");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * method used for giving users the new user badge. Called after you create
	 * a new user in the {@link softeng206.tatai.controllers.SplashController}
	 */
	public void newUserBadge() {
		new Notification(badgeMap,"okBadge");
		try {
			addBadge(this.user.getBadges(), "okBadge");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method used for giving users the badge for their first custom game made.
	 * Called after you create your first custom in {@link softeng206.tatai.controllers.GameCreatorController}
	 */
	public void firstCustomBadge() {
		if (!userBadges.contains("fireBadge")) {
			new Notification(badgeMap, "fireBadge");
			try {
				addBadge(this.user.getBadges(), "fireBadge");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * method used for parsing the CSVs which hold all of the practice and math scores.
	 * the data is then later compared for checking if a badge should be rewarded.
	 */
	private ArrayList<String> parseCSV(String location, int position) {
		String split = ",";
		String line = "";
		CSVReader csvReader = new CSVReader(location);
		ArrayList<String> parsedData = new ArrayList<>();
		try {
			BufferedReader bufferedReader = csvReader.getBufferedReader();
			while ((line = bufferedReader.readLine()) != null) {
				String[] dataOut = line.split(split);
				parsedData.add(dataOut[position]);
			}
			return parsedData;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * gets the list of badges a particular user has. Restricting a badge to be awarded more than once.
	 */
	private void getBadgeInfo() {
		String split = ",";
		String line = "";
		CSVReader badgeReader = new CSVReader("./MaoriNumbers/.badgeInfo.csv");
		try {
			BufferedReader bufferedPractice = badgeReader.getBufferedReader();
			while ((line = bufferedPractice.readLine()) != null) {
				String[] dataOut = line.split(split);
				badgeMap.put(dataOut[0], new String[]{dataOut[1], dataOut[3], dataOut[4]});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a badge to the users' list of badges
	 *
	 * @param file  the file which holds the users badges
	 * @param badge the badge they have earned
	 * @throws IOException the io exception
	 */
	private void addBadge(File file, String badge) throws IOException {
		FileWriter fileWriter = new FileWriter(file,true);
		fileWriter.append(badge);
		fileWriter.append("\n");
		fileWriter.flush();
		fileWriter.close();
	}
}
