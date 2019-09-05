package population;
import java.io.Serializable;
import java.util.ArrayList;

import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import box2d.B2DBody;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import mutation.MutVal;

public class Creature  implements Serializable, Comparable<Creature>{
	private static final long serialVersionUID = 1L;
	private final Root rootBone;
	
	private final MutVal cycleLength;
	
	private final int id;
	private transient FloatProperty fitness = new SimpleFloatProperty(-1000.0f);
	private float distance = -1000.0f;
	private float fitness_ser = -1000.f;
	private boolean fitnessEvaluated = false;
	
	public Creature(int id_in, Root rb_in) {
		rootBone = rb_in;
		cycleLength = new MutVal(1, 2);
		id = id_in;
	}
	
	public Creature(int id_in, Root rb_in, MutVal cycleLen_in) {
		rootBone = rb_in;
		cycleLength = cycleLen_in.clone();
		id = id_in;
	}
	
	private Creature(int id_in, Root rb_in, MutVal cycleLen_in, float fitness_in, float distance_in, boolean fitnessEval_in) {
		rootBone = rb_in;
		
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
	public Muscle[] getMuscles() {
		return rootBone.getMuscleList();
	}
	

	public Creature mutate(int id, int gen) {
		return new Creature(id, rootBone.mutateCreature(), cycleLength.mutate(gen));
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
		return new Creature(id, rootBone.cloneCreature(), cycleLength.clone(), fitness.get(), distance, fitnessEvaluated);
	}
	
	public void initProperty() {
		fitness = new SimpleFloatProperty(fitness_ser);
	}

	public void buildCreature(World w, ArrayList<B2DBody> creatureInstances_in,
			ArrayList<RevoluteJoint> revoluteJoints_in) {
		rootBone.buildCreature(w, creatureInstances_in, revoluteJoints_in);
	}
}
