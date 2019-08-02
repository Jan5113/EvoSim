package creatureCreator;
import java.io.Serializable;

import org.jbox2d.common.Vec2;
import box2d.ShapeType;

public class ProtoBone implements Serializable {
	private static final long serialVersionUID = 1L;
	public final int IDJointA;
	public final int IDJointB;
	public final int ID;
	public final ProtoCreature pc; //Parent Creature
	
	public float density = 0.1f;
	public float width = 0.2f;
	public ShapeType shapetype = ShapeType.RECT;
	public float shapeArg = 0.1f;
	
	public ProtoBone (int jointA, int jointB, int id_in, ProtoCreature pc_in) {
		IDJointA = jointA;
		IDJointB = jointB;
		ID = id_in;
		pc = pc_in;
	}

	public Vec2 getPos() {
		return pc.jointDefList.get(IDJointA).pos.add(pc.jointDefList.get(IDJointB).pos).mul(0.5f);
	}

	public Vec2 getDim() {
		Vec2 dim = pc.jointDefList.get(IDJointB).pos.sub(pc.jointDefList.get(IDJointA).pos);
		dim = new Vec2((float) Math.sqrt(dim.x*dim.x+dim.y*dim.y), width);
		return dim.mul(0.5f);
	}

	public float getAngle() {
		Vec2 dim = pc.jointDefList.get(IDJointB).pos.sub(pc.jointDefList.get(IDJointA).pos);
		return (float) Math.atan((double) dim.y/dim.x);
	}

}
