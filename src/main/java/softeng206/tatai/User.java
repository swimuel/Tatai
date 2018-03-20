package softeng206.tatai;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

/**
 * The user class is used for storing of the current user and
 * also used for creating new users. A user has a name, a badge file
 * and a custom game creation file.
 *
 * @author Charlie Rillstone
 */
public class User {
	private static User singleton = new User();
	private Map<String,User> users = new HashMap<>();
	private File userRoot = new File("./MaoriNumbers/UserData");
	private File badges,customGames;
	private String userName;

	/**
	 * Instantiates a new User. This constructor is used when the
	 * username is provided.
	 *
	 * @param name the name of the user
	 */
	public User(String name) {
		this.userName = name;
		File newUser = new File(userRoot.toString()+"/"+name);
		badges = new File(newUser.toString()+"/.badges.csv");
		customGames = new File(newUser.toString()+"/.customGames.csv");
		if (!newUser.exists()) {
			createUserFiles(newUser);
		}
	}

	/**
	 * Instantiates a new User for the singleton pattern.
	 */
	public User() {
		if (userRoot.listFiles(File::isDirectory) != null) {
			for (File directory : userRoot.listFiles(File::isDirectory)) {
				User newUser = new User(directory.getName());
				users.put(directory.getName(),newUser);
			}
		}
	}
	/**
	 * creates the files for the user, will not create if they exist already
	 */
	private void createUserFiles(File userFile) {
		userFile.mkdir();
		try {
			badges.createNewFile();
			customGames.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method used for changing the user. The singleton object should then
	 * change so the other classes are aware of the current user which is
	 * logged in.
	 *
	 * @param name the name of the user which is logging in.
	 */
	public void changeUser(String name) {
		this.userName=name;
	}

	/**
	 * Gets current user logged in
	 *
	 * @return the current user logged in
	 */
	public User getCurrentUser() {
		return users.get(userName);
	}

	/**
	 * Gets user name of the logged in user
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Get singleton instance of {@link User}
	 *
	 * @return the singleton user object
	 */
	public static User getInstance(){
		return singleton;
	}

	/**
	 * Gets the map of all users
	 *
	 * @return the users
	 */
	public Map<String, User> getUsers() {
		return users;
	}

	/**
	 * Gets the badges file of the user
	 *
	 * @return the badges file
	 */
	public File getBadges() {
		return this.badges;
	}

	/**
	 * Gets the custom games file of the current user.
	 *
	 * @return the custom games file
	 */
	public File getCustomGames() {
		return this.customGames;
	}
}
