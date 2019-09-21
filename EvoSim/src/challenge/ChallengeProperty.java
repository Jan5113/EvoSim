package challenge;

import java.io.Serializable;

public class ChallengeProperty implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String name;
    public int starsEarned;

    public ChallengeProperty (String name) {
        this.name = name;
        starsEarned = 0;
    }

    public ChallengeProperty (String name, int stars) {
        this.name = name;
        starsEarned = stars;
    }
}
