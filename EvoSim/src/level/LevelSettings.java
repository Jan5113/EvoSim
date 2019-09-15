package level;

import java.io.Serializable;

public class LevelSettings implements Serializable {
    private static final long serialVersionUID = 1L;
    public float incline = 0.3f;
    public float hurdleDist = 2f;
    public float hurdleHeight = 0.2f;
    public float hurdleWidth = 0.2f;
    //public float randDist = 2f;
    //public float randMaxDiff = 2f;
    public float climbWidth = 2f;

    public LevelSettings() {

    }

    public LevelSettings(float incline_in, float hurdleDist_in, float hurdleHeight_in, float hurdleWidth_in, float climbWidth_in) {
        incline = incline_in;
        hurdleDist = hurdleDist_in;
        hurdleHeight = hurdleHeight_in;
        hurdleWidth = hurdleWidth_in;
        climbWidth = climbWidth_in;
    }

    public LevelSettings clone() {
        return new LevelSettings(incline, hurdleDist, hurdleHeight, hurdleWidth, climbWidth);
    }
}