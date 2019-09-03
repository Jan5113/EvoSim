package population;

import org.jbox2d.common.Vec2;

public class MuscleInfo {
    public Vec2 anchorA;
    public Vec2 anchorB;
    public float offAngle;
    public float onAngle;
    public MuscleInfo (Vec2 anchorA_in, Vec2 anchorB_in, float offAngle_in, float onAngle_in) {
        anchorA = anchorA_in;
        anchorB = anchorB_in;
        offAngle = offAngle_in;
        onAngle = offAngle_in;
    }
}