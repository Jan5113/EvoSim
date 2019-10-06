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
        	System.out.println(" | " + name == cp.name);
            if (name == cp.name) {
            	System.out.println("Recall challenge " + name);
            	return cp;
            }
        }
        properties.add(new ChallengeProperty(name));
        System.out.println("Created new property for challenge " + name);
        return properties.get(properties.size()-1);
    }

    public boolean updateChallengeProperty(Challenge cha, int stars) {
        for (ChallengeProperty cp : properties) {
            if (cha.challengeName == cp.name) {
            	if  (stars > cp.starsEarned) {
            		System.out.println("Updating stars from " + cp.starsEarned + " to " + stars);
            		cp.starsEarned = stars;
            		return true;
            	} else {
            		System.out.println("No update (saved: "+ cp.starsEarned + ", new: " + stars + ")");
            		return false;
            	}
            }
        }
        properties.add(new ChallengeProperty(cha.challengeName, stars));
        return true;
    }
}    
