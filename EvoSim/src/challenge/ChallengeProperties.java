package challenge;

import java.io.Serializable;
import java.util.ArrayList;


public class ChallengeProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<ChallengeProperty> properties;

    public ChallengeProperties() {
        properties = new ArrayList<ChallengeProperty>();
    }

    public ArrayList<ChallengeProperty> getProperties() {
        return properties;
    }

    public ChallengeProperty getChallengeProperty(String name) {
        for (ChallengeProperty cp : properties) {
            if (name == cp.name) return cp;
        }
        properties.add(new ChallengeProperty(name));
        return properties.get(properties.size()-1);
    }

    public void updateChallengeProperty(String name, int stars) {
        for (ChallengeProperty cp : properties) {
            if (name == cp.name && stars > cp.starsEarned) {
                cp.starsEarned = stars;
                return;
            }
        }
        properties.add(new ChallengeProperty(name, stars));
    }
}    
