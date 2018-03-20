package softeng206.tatai;

/**
 * The enum Score io is for the CSV locations.
 * goal of this enum is to prevent error with adding in the string
 * location every time. Also reduce repetition.
 * 
 * @author Charlie Rillstone
 */
public enum ScoreIO {
    PRACTICE_CSV("./MaoriNumbers/.practicescores.csv"),
    MATH_CSV("./MaoriNumbers/.mathscores.csv");
    private final String location;

    ScoreIO(final String location) {
        this.location = location;
    }

    /**
     * Gets the location of the respective
     * enum
     *
     * @return the location of the CSV
     */
    public String getLocation() {
        return location;
    }
}
