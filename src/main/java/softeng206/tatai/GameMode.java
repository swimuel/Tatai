package softeng206.tatai;

/**
 * The enum Game mode for the three respective game types:
 * practice, normal math mode and custom for users' custom games
 * 
 * @author Charlie Rillstone
 */
public enum GameMode {
    PRACTICE_MODE(1),
    MATH_MODE(2),
    CUSTOM_MODE(3);
    private final int gameMode;

    GameMode(final int gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * get the game mode int for the enum
     *
     * @return the game mode integer
     */
    public int mode() {
        return gameMode;
    }
}
