package population;
import box2d.B2DBone;
import box2d.B2DJoint;
import box2d.B2DMuscle;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import mutation.MutTimer;
import mutation.MutVal;
import mutation.MutVec2;

public class Creature implements Comparable<Creature>{
	private final B2DJoint[] joints;
	private final B2DBone[] bones;
	private final B2DMuscle[] muscles;
	
	private final MutVal cycleLength;
	
	private final int id;
	private FloatProperty fitness = new SimpleFloatProperty(-1000.0f);
	private boolean fitnessEvaluated = false;

	
	public Creature(int id_in) {
		joints = createDefJoints();
		bones = createDefBones();
		muscles = createDefMuscles();
		
		cycleLength = new MutVal(1, 10, 1);
		
		id = id_in;
	}
	
	public Creature(int id_in, B2DJoint[] joints_in, B2DBone[] bones_in, B2DMuscle[] muscles_in, MutVal cycleLen_in) {
		joints = joints_in;
		bones = bones_in;
		muscles = muscles_in;
		
		cycleLength = cycleLen_in.clone();
		
		id = id_in;
	}
	
	private B2DJoint[] createDefJoints() {
		B2DJoint[] joints_def = new B2DJoint[4];
		joints_def[0] = new B2DJoint(new MutVec2(1), 0);
		joints_def[1] = new B2DJoint(new MutVec2(1), 1);
		joints_def[2] = new B2DJoint(new MutVec2(1), 2);
		joints_def[3] = new B2DJoint(new MutVec2(1), 3);
		return joints_def;
	}

	private B2DBone[] createDefBones() {
		B2DBone[] bones_def = new B2DBone[3];
		bones_def[0] = new B2DBone(joints[0], joints[1], 0);
		bones_def[1] = new B2DBone(joints[1], joints[2], 1);
		bones_def[2] = new B2DBone(joints[2], joints[3], 2);
		return bones_def;
	}

	private B2DMuscle[] createDefMuscles() {
		B2DMuscle[] muscle_def = new B2DMuscle[2];
		muscle_def[0] = new B2DMuscle(joints[1], bones[0], bones[1], new MutTimer(0.2f), new MutTimer(0.7f), new MutVal(2.0f, 1.0f), 0);
		muscle_def[1] = new B2DMuscle(joints[2], bones[1], bones[2], new MutTimer(0.2f), new MutTimer(0.7f), new MutVal(2.0f, 1.0f), 1);
		return muscle_def;
	}
	
	public void setFitness(float fitness_in) {
		if (fitnessEvaluated) {
			System.err.println("Can't overwrite fitness!");
			return;
		}
		fitness.set(fitness_in);
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
			return (float) Math.floor(fitness.get()*100)/100;
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
	

	public Creature mutate(int id) {
		B2DJoint[] temp_joints = new B2DJoint[joints.length];
		B2DBone[] temp_bones = new B2DBone[bones.length];
		B2DMuscle[] temp_muscles = new B2DMuscle[muscles.length];
		
		for (int i = 0; i < temp_joints.length; i++) {
			temp_joints[i] = joints[i].mutate();
		}
		for (int i = 0; i < temp_bones.length; i++) {
			int[] jointIDs = {bones[i].getJoints()[0].getID(), bones[i].getJoints()[1].getID()};
			temp_bones[i] = bones[i].rereferencedClone(temp_joints[jointIDs[0]], temp_joints[jointIDs[1]]);
		}
		for (int i = 0; i < temp_muscles.length; i++) {
			int jointID = muscles[i].getJointID();
			int[] boneIDs = {muscles[i].getBones()[0].getID(), muscles[i].getBones()[1].getID()};
			temp_muscles[i] = muscles[i].rereferencedMutate(temp_joints[jointID], temp_bones[boneIDs[0]], temp_bones[boneIDs[1]]);
		}
		
		return new Creature(id, temp_joints, temp_bones, temp_muscles, cycleLength.mutate());
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
}
