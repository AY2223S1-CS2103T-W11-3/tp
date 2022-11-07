package seedu.address.logic.parser;

/**
 * Helper class to store the position of a flag and the associated prefix token.
 */
public class FlagAndPosition implements Comparable<FlagAndPosition> {
    private String flag;
    private Prefix token;
    private int position;

    /**
     * Constructor of the class.
     */
    public FlagAndPosition(String flag, Prefix token, int position) {
        this.flag = flag;
        this.token = token;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getFlag() {
        return flag;
    }

    public Prefix getToken() {
        return token;
    }

    @Override
    public int compareTo(FlagAndPosition other) {
        if (this.position == other.position) {
            return 0;
        } else if (this.position < other.position) {
            return -1;
        } else {
            return 1;
        }
    }
}
