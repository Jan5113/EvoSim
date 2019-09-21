package challenge;

import java.io.Serializable;

import level.Level;
import population.Creature;
import population.Population;

public class Challenge implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String challengeName;
    private final Creature challenger;
    public final Level challengerLevel;

    public final float maxCost;
    public final int genBronze;
    public final int genSilver;
    public final int genGold;
    public transient boolean init = false;

    public Challenge(Population pop_in, String name, float maxCost_in,
            int gen_bron, int gen_silv, int gen_gold ) {
        Creature c = pop_in.getArrayList().get(0);
        c.initProperty();
        challengeName = name;
        challenger = c.clone();
        challengerLevel = pop_in.getLevel();
        maxCost = maxCost_in;
        genBronze = gen_bron;
        genSilver = gen_silv;
        genGold = gen_gold;
    }

    public Creature getChallenger() {
        if (!init) challenger.initProperty();
        return challenger;
    }

    public int gen(int rank) {
        switch (rank) {
            case 0:
                return genBronze;
            case 1:
                return genSilver;
            case 2:
                return genGold;
            default:
                return 0;
        }
    }
}