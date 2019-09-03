package creatureCreator;

import java.io.Serializable;

import org.jbox2d.common.Vec2;

public class ProtoJoint implements Serializable {

	private static final long serialVersionUID = 1L;
	public Vec2 pos;
	public boolean mutatingPos = true;
	public final int ID;
	
	public ProtoJoint (float x, float y, int id_in) {
		pos = new Vec2(x,y);
		ID = id_in;
	}
	
	public ProtoJoint (Vec2 pos_in, int id_in) {
		pos = pos_in;
		ID = id_in;
	}
	
	public ProtoJoint (Vec2 pos_in, boolean mutatingPos_in, int id_in) {
		pos = pos_in;
		mutatingPos = mutatingPos_in;
		ID = id_in;
	}
}
