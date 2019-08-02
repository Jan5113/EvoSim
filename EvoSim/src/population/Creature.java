package population;
import java.io.Serializable;

import org.jbox2d.common.Vec2;

import box2d.B2DBone;
import box2d.B2DJoint;
import box2d.B2DMuscle;
import box2d.ShapeType;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import mutation.MutVal;
import mutation.MutVec2;

public class Creature  implements Serializable, Comparable<Creature>{
	private static final long serialVersionUID = 1L;
	private final B2DJoint[] joints;
	private final B2DBone[] bones;
	private final B2DMuscle[] muscles;
	
	private final MutVal cycleLength;
	
	private final int id;
	private transient FloatProperty fitness = new SimpleFloatProperty(-1000.0f);
	private float distance = -1000.0f;
	private float fitness_ser = -1000.f;
	private boolean fitnessEvaluated = false;
	
	public Creature(int id_in) {
		//joints = createDefJoints();
		//bones = createDefBones();
		//muscles = createDefMuscles();

		joints = createHumanoidJoints();
		bones = createHumanoidBones();
		muscles = createHumanoidMuscles();

//		joints = createMonoHumanoidJoints();
//		bones = createMonoHumanoidBones();
//		muscles = createMonoHumanoidMuscles();


//		joints = createTestJoints();
//		bones = createTestBones();
//		muscles = createTestMuscles();
//		
		cycleLength = new MutVal(2, 1);
		
		id = id_in;
	}
	
	public Creature(int id_in, B2DJoint[] joints_in, B2DBone[] bones_in, B2DMuscle[] muscles_in, MutVal cycleLen_in) {
		joints = joints_in;
		bones = bones_in;
		muscles = muscles_in;
		
		cycleLength = cycleLen_in.clone();
		
		id = id_in;
	}
	
	private Creature(int id_in, B2DJoint[] joints_in, B2DBone[] bones_in, B2DMuscle[] muscles_in, MutVal cycleLen_in, float fitness_in, float distance_in, boolean fitnessEval_in) {
		joints = joints_in;
		bones = bones_in;
		muscles = muscles_in;
		
		cycleLength = cycleLen_in.clone();
		
		id = id_in;
		fitness.set(fitness_in);
		fitness_ser = fitness_in;
		distance = distance_in;
		fitnessEvaluated = fitnessEval_in;
	}

	@SuppressWarnings("unused")
	private B2DJoint[] createDefJoints() {
		B2DJoint[] joints_def = new B2DJoint[6];
		joints_def[0] = new B2DJoint(new MutVec2(new Vec2(-1.6f, 0.1f)), 0);
		joints_def[1] = new B2DJoint(new MutVec2(new Vec2(-1.6f, 0.9f)), 1);
		joints_def[2] = new B2DJoint(new MutVec2(new Vec2(-0.8f, 0.9f)), 2);
		joints_def[3] = new B2DJoint(new MutVec2(new Vec2(-0.8f, 0.1f)), 3);
		joints_def[4] = new B2DJoint(new MutVec2(new Vec2(0.0f, 0.9f)), 4);
		joints_def[5] = new B2DJoint(new MutVec2(new Vec2(0.0f, 0.1f)), 5);
		return joints_def;
	}

	@SuppressWarnings("unused")
	private B2DBone[] createDefBones() {
		B2DBone[] bones_def = new B2DBone[6];
		bones_def[0] = new B2DBone(joints[0], joints[1], 0, 0.1f);
		bones_def[1] = new B2DBone(joints[1], joints[2], 1, 0.1f);
		bones_def[2] = new B2DBone(joints[2], joints[3], 2, 0.1f);
		bones_def[3] = new B2DBone(joints[2], joints[4], 3, 0.1f);
		bones_def[4] = new B2DBone(joints[4], joints[5], 4, 0.1f);
		bones_def[5] = new B2DBone(joints[3], joints[0], 5, 0.1f);
		return bones_def;
	}

	@SuppressWarnings("unused")
	private B2DMuscle[] createDefMuscles() {
		B2DMuscle[] muscle_def = new B2DMuscle[5];
		muscle_def[0] = new B2DMuscle(joints[1], bones[0], bones[1], 0);
		muscle_def[1] = new B2DMuscle(joints[2], bones[1], bones[2], 1);
		muscle_def[2] = new B2DMuscle(joints[2], bones[2], bones[3], 2);
		muscle_def[3] = new B2DMuscle(joints[4], bones[3], bones[4], 3);
		muscle_def[4] = new B2DMuscle(joints[3], bones[2], bones[5], 4);
		return muscle_def;
	}
	
	@SuppressWarnings("unused")
	private B2DJoint[] createHumanoidJoints() {
		B2DJoint[] joints_def = new B2DJoint[6];
		joints_def[0] = new B2DJoint(new MutVec2(new Vec2(0f, 3.1f)), 0); //shoulder
		joints_def[1] = new B2DJoint(new MutVec2(new Vec2(0f, 1.6f)), 1); //hip
		joints_def[2] = new B2DJoint(new MutVec2(new Vec2(0f, 0.85f)), 2); //knee
		joints_def[3] = new B2DJoint(new MutVec2(new Vec2(0f, 0.1f)), 3); //ankle
		joints_def[4] = new B2DJoint(new MutVec2(new Vec2(0.5f, 0.1f)), 4); //toes
		joints_def[5] = new B2DJoint(new MutVec2(new Vec2(0f, 2.35f)), 5); //stomach
		return joints_def;
	}

	@SuppressWarnings("unused")
	private B2DBone[] createHumanoidBones() {
		B2DBone[] bones_def = new B2DBone[9];
		bones_def[0] = new B2DBone(joints[0], joints[5], 0, 0.1f); //torso
		bones_def[1] = new B2DBone(joints[1], joints[2], 1, 0.1f); //thigh l
		bones_def[2] = new B2DBone(joints[2], joints[3], 2, 0.1f); //shank l
		bones_def[3] = new B2DBone(joints[3], joints[4], 3, 0.1f); //foot l
		bones_def[4] = new B2DBone(joints[1], joints[2], 4, 0.1f); //thigh r
		bones_def[5] = new B2DBone(joints[2], joints[3], 5, 0.1f); //shank r
		bones_def[6] = new B2DBone(joints[3], joints[4], 6, 0.1f); //foot r
		bones_def[7] = new B2DBone(joints[0], joints[0], 7, ShapeType.CIRCLE ,0.2f); //head
		bones_def[8] = new B2DBone(joints[5], joints[1], 8, 0.1f); //lower torso
		return bones_def;
	}

	@SuppressWarnings("unused")
	private B2DMuscle[] createHumanoidMuscles() {
		B2DMuscle[] muscle_def = new B2DMuscle[8];
		muscle_def[0] = new B2DMuscle(joints[1], bones[8], bones[1], -2.0f, 2.0f, 0); //hip l
		muscle_def[1] = new B2DMuscle(joints[1], bones[8], bones[4], -2.0f, 2.0f, 1); //hip r
		muscle_def[2] = new B2DMuscle(joints[2], bones[1], bones[2], -2.5f, 0.1f, 2); //knee l
		muscle_def[3] = new B2DMuscle(joints[2], bones[4], bones[5], -2.5f, 0.1f, 3); //knee r
		muscle_def[4] = new B2DMuscle(joints[3], bones[2], bones[3], -1.5f, 1.5f, 4); //ankle l
		muscle_def[5] = new B2DMuscle(joints[3], bones[5], bones[6], -1.5f, 1.5f, 5); //ankle r
		muscle_def[6] = new B2DMuscle(joints[0], bones[7], bones[0], -1.6f, -1.5f, 6); //head
		muscle_def[7] = new B2DMuscle(joints[5], bones[0], bones[8], -1.0f, 1.0f, 7); //stomach
		return muscle_def;
	}


	@SuppressWarnings("unused")
	private B2DJoint[] createMonoHumanoidJoints() {
		B2DJoint[] joints_def = new B2DJoint[6];
		joints_def[0] = new B2DJoint(new MutVec2(new Vec2(0f, 3.1f)), 0); //shoulder
		joints_def[1] = new B2DJoint(new MutVec2(new Vec2(0f, 1.6f)), 1); //hip
		joints_def[2] = new B2DJoint(new MutVec2(new Vec2(0f, 0.85f)), 2); //knee
		joints_def[3] = new B2DJoint(new MutVec2(new Vec2(0f, 0.1f)), 3); //ankle
		joints_def[4] = new B2DJoint(new MutVec2(new Vec2(0.5f, 0.1f)), 4); //toes
		joints_def[5] = new B2DJoint(new MutVec2(new Vec2(0f, 2.35f)), 5); //stomach
		return joints_def;
	}

	@SuppressWarnings("unused")
	private B2DBone[] createMonoHumanoidBones() {
		B2DBone[] bones_def = new B2DBone[6];
		bones_def[0] = new B2DBone(joints[0], joints[5], 0, 0.1f); //torso
		bones_def[1] = new B2DBone(joints[1], joints[2], 1, 0.1f); //thigh l
		bones_def[2] = new B2DBone(joints[2], joints[3], 2, 0.1f); //shank l
		bones_def[3] = new B2DBone(joints[3], joints[4], 3, 0.1f); //foot l
		bones_def[4] = new B2DBone(joints[0], joints[0], 4, ShapeType.CIRCLE ,0.2f); //head
		bones_def[5] = new B2DBone(joints[5], joints[1], 5, 0.1f); //lower torso
		return bones_def;
	}

	@SuppressWarnings("unused")
	private B2DMuscle[] createMonoHumanoidMuscles() {
		B2DMuscle[] muscle_def = new B2DMuscle[5];
		muscle_def[0] = new B2DMuscle(joints[1], bones[5], bones[1], -2.0f, 2.0f, 0); //hip l
		muscle_def[1] = new B2DMuscle(joints[2], bones[1], bones[2], -2.5f, 0.1f, 1); //knee l
		muscle_def[2] = new B2DMuscle(joints[3], bones[2], bones[3], -1.5f, 1.5f, 2); //ankle l
		muscle_def[3] = new B2DMuscle(joints[0], bones[4], bones[0], -1.6f, -1.5f, 3); //head
		muscle_def[4] = new B2DMuscle(joints[5], bones[0], bones[5], -1.0f, 1.0f, 4); //stomach
		return muscle_def;
	}

	@SuppressWarnings("unused")
	private B2DJoint[] createTestJoints() {
		B2DJoint[] joints_def = new B2DJoint[3];
		joints_def[0] = new B2DJoint(new MutVec2(new Vec2(0f, 4f)), 0);
		joints_def[1] = new B2DJoint(new MutVec2(new Vec2(0f, 3.5f)), 1);
		joints_def[2] = new B2DJoint(new MutVec2(new Vec2(0f, 3f)), 2); 
		return joints_def;
	}

	@SuppressWarnings("unused")
	private B2DBone[] createTestBones() {
		B2DBone[] bones_def = new B2DBone[2];
		bones_def[0] = new B2DBone(joints[0], joints[1], 0, 0.1f);
		bones_def[1] = new B2DBone(joints[1], joints[2], 1, 0.1f);
		return bones_def;
	}

	@SuppressWarnings("unused")
	private B2DMuscle[] createTestMuscles() {
		B2DMuscle[] muscle_def = new B2DMuscle[1];
		muscle_def[0] = new B2DMuscle(joints[1], bones[0], bones[1], -2.0f, 2.0f, 0);
		return muscle_def;
	}
	
	public void setFitness(float fitness_in, float distance_in) {
		if (fitnessEvaluated) {
			System.err.println("Can't overwrite fitness!");
			return;
		}
		fitness.set(fitness_in);
		fitness_ser = fitness_in;
		distance = distance_in;
		fitnessEvaluated = true;
	}
	
	public boolean fitnessEvaulated() {
		return fitnessEvaluated;
	}
	
	public float getFitness() {
		return fitness.get();
	}
	
	public Float getFitnessFloat() {
		if (fitnessEvaluated) {
			return (float) Math.floor(fitness.get()*1000)/1000;
			//return fitness.get();
		} else {
			return null;
		}
	}
	
	public float getDistance() {
		return distance;
	}
	
	public Float getDistnaceFloat() {
		if (fitnessEvaluated) {
			return (float) Math.floor(distance*1000)/1000;
			//return fitness.get();
		} else {
			return null;
		}
	}
	
	public B2DJoint[] getJoints() {
		return joints;
	}
	
	public B2DBone[] getBones() {
		return bones;
	}
	
	public B2DMuscle[] getMuscles() {
		return muscles;
	}
	

	public Creature mutate(int id, int gen) {
		B2DJoint[] temp_joints = new B2DJoint[joints.length];
		B2DBone[] temp_bones = new B2DBone[bones.length];
		B2DMuscle[] temp_muscles = new B2DMuscle[muscles.length];
		
		for (int i = 0; i < temp_joints.length; i++) {
			//temp_joints[i] = joints[i].mutate(gen);
			temp_joints[i] = joints[i].clone();
		}
		for (int i = 0; i < temp_bones.length; i++) {
			int[] jointIDs = {bones[i].getJoints()[0].getID(), bones[i].getJoints()[1].getID()};
			temp_bones[i] = bones[i].rereferencedClone(temp_joints[jointIDs[0]], temp_joints[jointIDs[1]]);
		}
		for (int i = 0; i < temp_muscles.length; i++) {
			int jointID = muscles[i].getJointID();
			int[] boneIDs = {muscles[i].getBones()[0].getID(), muscles[i].getBones()[1].getID()};
			temp_muscles[i] = muscles[i].rereferencedMutate(temp_joints[jointID], temp_bones[boneIDs[0]], temp_bones[boneIDs[1]], gen);
		}
		
		return new Creature(id, temp_joints, temp_bones, temp_muscles, cycleLength.mutate(gen));
	}

	public int compareTo(Creature c) {
		if (this.fitness.get() < c.getFitness()) return 1;
		else if (this.fitness.get() > c.getFitness()) return -1;
		return 0;
	}
	
	public int getID() {
		return id;
	}
	
	public FloatProperty fitnessProperty() {
		return fitness;
	}
	
	public float getCycleLength() {
		return cycleLength.getAbsVal();
	}
	
	public Creature clone() {
		B2DJoint[] temp_joints = new B2DJoint[joints.length];
		B2DBone[] temp_bones = new B2DBone[bones.length];
		B2DMuscle[] temp_muscles = new B2DMuscle[muscles.length];
		
		for (int i = 0; i < temp_joints.length; i++) {
			temp_joints[i] = joints[i].clone();
		}
		for (int i = 0; i < temp_bones.length; i++) {
			int[] jointIDs = {bones[i].getJoints()[0].getID(), bones[i].getJoints()[1].getID()};
			temp_bones[i] = bones[i].rereferencedClone(temp_joints[jointIDs[0]], temp_joints[jointIDs[1]]);
		}
		for (int i = 0; i < temp_muscles.length; i++) {
			int jointID = muscles[i].getJointID();
			int[] boneIDs = {muscles[i].getBones()[0].getID(), muscles[i].getBones()[1].getID()};
			temp_muscles[i] = muscles[i].rereferencedClone(temp_joints[jointID], temp_bones[boneIDs[0]], temp_bones[boneIDs[1]]);
		}
		
		return new Creature(id, temp_joints, temp_bones, temp_muscles, cycleLength.clone(), fitness.get(), distance, fitnessEvaluated);
	}
	
	public void initProperty() {
		fitness = new SimpleFloatProperty(fitness_ser);
	}
}
