import org.jbox2d.common.Vec2;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Creature implements Comparable<Creature>{
	public static float[] minMaxLength = {0.2f, 2.0f};
	public static float[] minMaxTime = {0.0f, 1.0f};
	public static Vec2[] minMaxRange = {new Vec2(-5.0f, 1.5f), new Vec2(-0.0f, 6.0f)};
	public static Vec2 ballStartPosition = new Vec2(0.0f, 17.0f);
	public static float ballDim = 0.1f;
	public static Vec2 posBat = new Vec2(-1.0f, 4.0f);
	
	public final MutVal length;
	public final MutVal time;
	public final MutVec2 fixturePos;
	private final int id;
	
	private FloatProperty fitness = new SimpleFloatProperty(-1000.0f);
	private boolean fitnessEvaluated = false;

	
	public Creature(int id_in) {
		length = new MutVal(minMaxLength[0], minMaxLength[1], 0.5f);
		time = new MutVal(minMaxTime[0], minMaxTime[1], 0.5f);
		fixturePos = new MutVec2(minMaxRange[0], minMaxRange[1], 0.5f);
//		fixturePos = new MutVec2(posBat, 2);

		id = id_in;
	}
	
	public Creature(int id_in, MutVal length, MutVal time, MutVec2 pos) {
		this.length = length;
		this.time = time;
		this.fixturePos = pos;
		id = id_in;
		
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
	
	public Creature mutate(int id) {
		return new Creature(id, length.mutate(), time.mutate(), fixturePos.mutate());
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
}
