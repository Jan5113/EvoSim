package box2d;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import mutation.MutVec2;

public class B2DJoint {
	private final MutVec2 pos;
	private ArrayList<B2DBone> registeredBones = new ArrayList<B2DBone>();
	private final int id;
	
	public B2DJoint(Vec2 min, Vec2 max, float rng, int id_in) {
		pos = new MutVec2(min, max, rng);
		id = id_in;
	}
	
	public B2DJoint(MutVec2 mutVec2_in, int id_in) {
		pos = mutVec2_in.clone();
		id = id_in;
	}
	
	public B2DJoint mutate() {
		return new B2DJoint(pos.mutate(), id);
	}
	
	public Vec2 getPos() {
		return pos.getVal();
	}
	
	public void registerBone(B2DBone boneRef_in) {
		registeredBones.add(boneRef_in);
	}
	
	public ArrayList<B2DBone> getRegisteredBones() {
		return registeredBones;
	}
	
	public B2DJoint clone() {
		return new B2DJoint(pos, id);
	}
}
