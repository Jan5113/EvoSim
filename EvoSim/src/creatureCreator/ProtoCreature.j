package creatureCreator;
import java.io.Serializable;

import org.jbox2d.common.Vec2;
import mutation.MutVal;
import population.Creature;
import population.RootBone;

public class ProtoCreature implements Serializable {
	private static final long serialVersionUID = 1L;
	public RootBone rootBone;
	public MutVal cycleLength = new MutVal(2, 1);
	
	public ProtoCreature() {
	}
	
	public ProtoCreature(boolean initTest) {
		if (initTest) {
			//createHumanoid();
			createTest();
		}
	}

	public void addBone(Vec2 pos1, Vec2 pos2) {
		rootBone = new RootBone(pos1, pos2.add(pos2.negate()));
	}

	public void addBone(int parentID, Vec2 dir) {
		rootBone.addBone(parentID, dir);
	}

	public void addHead(int parentID) {
		rootBone.addHead(parentID, 0.2f);
	} 

	public void deleteAll() {
		
	}
	
	
	public Creature makeCreature (int ID) {
		return new Creature(ID, rootBone, cycleLength);
	}

	/*
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
		boneDefList.get(boneDefList.size() - 1).name = "head";
		addBone(5, 1);
		
		addMuscle(1, 8, 1, -2.0f, 2.0f);
		addMuscle(1, 8, 4, -2.0f, 2.0f);
		addMuscle(2, 1, 2, -2.5f, 0.1f);
		addMuscle(2, 4, 5, -2.5f, 0.1f);
		addMuscle(3, 2, 3, -1.5f, 1.5f);
		addMuscle(3, 5, 6, -1.5f, 1.5f);
		addMuscle(0, 7, 0, -1.6f, -1.5f);
		addMuscle(5, 0, 8, -1.0f, 1.0f);
	}*/
	
	public void createTest() {/*
		addJoint(new Vec2(-1f, 0.1f)); 
		addJoint(new Vec2(-0.5f, 0.6f)); 
		addJoint(new Vec2(0f, 0.1f));
		
		addBone(0, 1);
		addBone(1, 2);
		
		addMuscle(1, 0, 1);*/
	}

	
	
}
