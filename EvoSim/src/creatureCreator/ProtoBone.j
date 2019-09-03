package creatureCreator;
import java.io.Serializable;
/*
import org.jbox2d.common.Vec2;
import box2d.ShapeType;
import population.BoneType;*/

public class ProtoBone implements Serializable {
	private static final long serialVersionUID = 1L;
	/*public final ProtoBone parentBone;
	public final int ID;
	public Vec2 headPos;
	
	public float density = 0.1f;
	public BoneType boneType = BoneType.BONE;
	public float boneArg = 0.2f;
	public String name;
	
	public ProtoBone (Vec2 headpos_in, ProtoBone pb_in, int id_in) {
		parentBone = pb_in;
		ID = id_in;
	}

	public Vec2 getBonePos(ArrayList<BonePosElemnet> boneList) {
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
	}*/

}
