package creatureCreator;
import java.io.Serializable;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import box2d.B2DBone;
import box2d.B2DJoint;
import box2d.B2DMuscle;
import box2d.ShapeType;
import mutation.MutVal;
import population.Creature;

public class ProtoCreature implements Serializable {
	private static final long serialVersionUID = 1L;
	public ArrayList<ProtoJoint> jointDefList = new ArrayList<ProtoJoint>();
	public ArrayList<ProtoBone> boneDefList = new ArrayList<ProtoBone>();
	public ArrayList<ProtoMuscle> muscleDefList = new ArrayList<ProtoMuscle>();
	
	public MutVal cycleLength = new MutVal(2, 1);
	
	public ProtoCreature() {
	}
	
	public ProtoCreature(boolean initHumanoid) {
		if (initHumanoid) {
			//createHumanoid();
			createTest();
		}
	}
	
	public void addJoint(Vec2 pos) {
		jointDefList.add(new ProtoJoint(pos, jointDefList.size()));
	}
	
	public void addJoint(Vec2 pos, boolean mutPos) {
		ProtoJoint pj = new ProtoJoint(pos, jointDefList.size());
		pj.mutatingPos = mutPos;
		jointDefList.add(pj);
	}
	
	public void addBone(int j1, int j2) {
		boneDefList.add(new ProtoBone(j1, j2, boneDefList.size(), this));
	}
	
	public void addMuscle(int j, int b1, int b2) {
		muscleDefList.add(new ProtoMuscle(j,b1, b2, muscleDefList.size(), this));
	}
		
	public void addMuscle(int j, int b1, int b2, float anglMin, float anglMax) {
		ProtoMuscle pm = new ProtoMuscle(j,b1, b2, muscleDefList.size(), this);
		pm.angleMin = anglMin;
		pm.angleMax = anglMax;
		muscleDefList.add(pm);
	}
	
	
	public Creature makeCreature (int ID) {
		B2DJoint[] joints = makeJoints();
		B2DBone[] bones = makeBones(joints);
		B2DMuscle[] muscles = makeMuscles(joints, bones);
		
		return new Creature(ID, joints, bones, muscles, cycleLength);
	}

	
	private B2DJoint[] makeJoints() {
		B2DJoint[] joints = new B2DJoint[jointDefList.size()];
		for (ProtoJoint j : jointDefList) {
			joints[j.ID] = new B2DJoint(j);
		}
		return joints;
	}
	
	private B2DBone[] makeBones(B2DJoint[] joints) {
		B2DBone[] bones = new B2DBone[boneDefList.size()];
		for (ProtoBone b : boneDefList) {
			bones[b.ID] = new B2DBone(b, joints);
		}
		return bones;
	}
	
	private B2DMuscle[] makeMuscles(B2DJoint[] joints, B2DBone[] bones) {
		B2DMuscle[] muscles = new B2DMuscle[muscleDefList.size()];
		for (ProtoMuscle m : muscleDefList) {
			muscles[m.ID] = new B2DMuscle(muscleDefList.get(m.ID), joints, bones);
		}
		return muscles;
	}
	
	public void createHumanoid() {
		addJoint(new Vec2(0f, 3.1f), false); //shoulder
		addJoint(new Vec2(0f, 1.6f), false); //hip
		addJoint(new Vec2(0f, 0.85f), false); //knee
		addJoint(new Vec2(0f, 0.1f), false); //ankle
		addJoint(new Vec2(0.5f, 0.1f), false); //toes
		addJoint(new Vec2(0f, 2.35f), false); //stomach
		
		addBone(0, 5);
		addBone(1, 2);
		addBone(2, 3);
		addBone(3, 4);
		addBone(1, 2);
		addBone(2, 3);
		addBone(3, 4);
		addBone(0, 0);
		boneDefList.get(boneDefList.size() - 1).shapetype = ShapeType.CIRCLE;
		boneDefList.get(boneDefList.size() - 1).shapeArg = 0.2f;
		addBone(5, 1);
		
		addMuscle(1, 8, 1, -2.0f, 2.0f);
		addMuscle(1, 8, 4, -2.0f, 2.0f);
		addMuscle(2, 1, 2, -2.5f, 0.1f);
		addMuscle(2, 4, 5, -2.5f, 0.1f);
		addMuscle(3, 2, 3, -1.5f, 1.5f);
		addMuscle(3, 5, 6, -1.5f, 1.5f);
		addMuscle(0, 7, 0, -1.6f, -1.5f);
		addMuscle(5, 0, 8, -1.0f, 1.0f);
	}
	
	public void createTest() {
		addJoint(new Vec2(-1f, 0.1f)); 
		addJoint(new Vec2(-0.5f, 0.6f)); 
		addJoint(new Vec2(0f, 0.1f));
		
		addBone(0, 1);
		addBone(1, 2);
		
		addMuscle(1, 0, 1);
	}
	
}
