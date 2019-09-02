package population;
import java.io.Serializable;

import box2d.B2DBone;
import box2d.B2DJoint;
import box2d.B2DMuscle;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import mutation.MutVal;

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
			temp_joints[i] = joints[i].mutate(gen);
			//temp_joints[i] = joints[i].clone();
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
