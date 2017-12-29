package box2d;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class B2DBone {
	private final B2DJoint jointA;
	private final B2DJoint jointB;
	private Vec2 pos;
	private float angle;
	private final int id;
	private B2DBody boneBody;
	
	public B2DBone(B2DJoint jointA_in, B2DJoint jointB_in, int id_in) {
		jointA = jointA_in;
		jointB = jointB_in;
		id = id_in;
		initialiseBone();
	}
	
	private void initialiseBone() {
		jointA.registerBone(this);
		jointB.registerBone(this);
		
		pos = jointA.getPos().add(jointB.getPos()).mul(0.5f);
		angle = B2DCamera.getRotation(jointB.getPos().add(jointA.getPos().negate()));
		
		boneBody = new B2DBody("BONE" + id);
		boneBody.setUpRect(pos, new Vec2(jointB.getPos().add(jointA.getPos().negate()).length(), 0.1f), angle, BodyType.DYNAMIC);
	}
	
	public B2DBody getBone() {
		return boneBody;
	}
	
	public B2DBone clone() {
		return new B2DBone(jointA.clone(), jointB.clone(), id);
	}
}
