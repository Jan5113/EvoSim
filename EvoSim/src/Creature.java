import java.util.Random;

import org.jbox2d.common.Vec2;

public class Creature implements Comparable<Creature>{
	public static float[] minMaxLength = {0.2f, 2.0f};
	public static float[] minMaxTime = {0.0f, 1.0f};
	public static Vec2[] minMaxRange = {new Vec2(-5.0f, 1.5f), new Vec2(-0.0f, 6.0f)};
	public static Vec2 ballStartPosition = new Vec2(0.0f, 17.0f);
	public static float ballDim = 0.1f;
	
	public final float length;
	public final float time;
	public final Vec2 fixturePos;
	public final int id;
	
	private float fitness;
	
	private Random rnd = new Random();
	
	public Creature(int id_in) {
		length = minMaxLength[0] + (float) Math.random() * (minMaxLength[1] - minMaxLength[0]);
		time = minMaxTime[0] + (float) Math.random() * (minMaxTime[1] - minMaxTime[0]);
		fixturePos =
				new Vec2 (minMaxRange[0].x + (float) Math.random() * (minMaxRange[1].x - minMaxRange[0].x),
						minMaxRange[0].y + (float) Math.random() * (minMaxRange[1].y - minMaxRange[0].y));

		id = id_in;
	}
	
	public Creature(int id_in, float length, float time, Vec2 pos) {
		this.length = length;
		this.time = time;
		if (pos.x > -0.5f) {
			this.fixturePos = new Vec2 (pos.x, pos.y);
		} else {
			this.fixturePos = pos;			
		}
		id = id_in;
		
	}
	
	public void setFitness(float fitness_in) {
		fitness = fitness_in;
	}
	
	public float getFitness() {
		return fitness;
	}
	
	public Creature mutate(float amount, int id) {
		float newLen = this.length + amount *(float) rnd.nextGaussian();
		float newTime = this.time + amount * (float) rnd.nextGaussian();
		Vec2 newPos = new Vec2(
				this.fixturePos.x + amount * (float) rnd.nextGaussian(),
				this.fixturePos.y + amount * (float) rnd.nextGaussian());
		return new Creature(id, newLen, newTime, newPos);
	}

	public int compareTo(Creature c) {
		if (this.fitness < c.getFitness()) return 1;
		else if (this.fitness > c.getFitness()) return -1;
		return 0;
	}
}
