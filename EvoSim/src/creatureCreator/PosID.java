package creatureCreator;

import org.jbox2d.common.Vec2;

public class PosID{
    public int id;
    public Vec2 pos;
    public Vec2 mPos;

    public PosID(int id_in, Vec2 pos_in) {
        id = id_in;
        pos = pos_in;
    }

    public PosID(int id_in, Vec2 pos_in, Vec2 mPos_in) {
        id = id_in;
        pos = pos_in;
        mPos = mPos_in;
    }
}