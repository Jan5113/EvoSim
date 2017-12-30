package box2d;

import org.jbox2d.common.Vec2;

public class B2DBone {
	private final B2DJoint headJoint;
	private final B2DJoint endJoint;
	private final int id;
	private final float halfLength;
	
	public B2DBone(B2DJoint jointA_in, B2DJoint jointB_in, int id_in) {
		headJoint = jointA_in;
		endJoint = jointB_in;
		id = id_in;
		
		halfLength = (endJoint.getPos().add(headJoint.getPos().negate()).length()) * 0.5f;

		headJoint.registerHeadBone(this);
		endJoint.registerEndBone(this);
	}
	
	public B2DJoint[] getJoints() {
		B2DJoint[] joints = {headJoint, endJoint};
		return joints;
	}
	
//	public B2DBone clone() {
//		return new B2DBone(headJoint.clone(), endJoint.clone(), id);
//	}
	
	public B2DBone rereferencedClone(B2DJoint jointA_in, B2DJoint jointB_in) {
		return new B2DBone(jointA_in, jointB_in, id);
	}

	public int getID() {
		return id;
	}
	
	public float getHalfLen() {
		return halfLength;
	}
	
	public Vec2 getLocalHead() {
		return new Vec2(-halfLength, 0);
	}
	
	public Vec2 getLocalEnd() {
		return new Vec2(halfLength, 0);
	}
}
