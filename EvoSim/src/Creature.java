import org.jbox2d.common.Vec2;

public class Creature implements Comparable<Creature>{
	public static float[] minMaxLength = {0.2f, 2.0f};
	public static float[] minMaxTime = {0.0f, 1.0f};
	public static Vec2 ballStartPosition = new Vec2(0.0f, 17.0f);
	public static Vec2 fixturePosition = new Vec2(-0.5f, 2.0f);
	public static float ballDim = 0.1f;
	
	public final float length;
	public final float time;
	public final int id;
	
	private float fitness;
	
	public Creature(int id_in) {
		length = minMaxLength[0] + (float) Math.random() * (minMaxLength[1] - minMaxLength[0]);
		time = minMaxTime[0] + (float) Math.random() * (minMaxTime[1] - minMaxTime[0]);
		id = id_in;
	}
	
	public Creature(int id_in, float length, float time) {
		this.length = length;
		this.time = time;
		id = id_in;
	}
	
	public void setFitness(float fitness_in) {
		fitness = fitness_in;
	}
	
	public float getFitness() {
		return fitness;
	}
	
	public Creature mutate(float amount, int id) {
		float newLen = this.length * (1 + amount * (2 * (float) Math.random() - 1.0f));
		float newTime = this.time * (1 + amount * (2 * (float) Math.random() - 1.0f));
		return new Creature(id, newLen, newTime);
	}

	public int compareTo(Creature c) {
		if (this.fitness < c.getFitness()) return 1;
		else if (this.fitness > c.getFitness()) return -1;
		return 0;
	}
}
