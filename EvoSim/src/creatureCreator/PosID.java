package creatureCreator;

import org.jbox2d.common.Vec2;

public class PosID{
    public int id;
    public int[] ids;
    public Vec2 pos;

    public PosID(int id_in, Vec2 pos_in) {
        id = id_in;
        pos = pos_in;
    }

    public PosID(int[] ids_in, Vec2 pos_in) {
        ids = ids_in;
        pos = pos_in;
    }
}