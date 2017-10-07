import org.jbox2d.common.Vec2;

public class Creature {
	public static float[] minMaxLength = {0.2f, 1.0f};
	public static float[] minMaxTime = {0.0f, 0.5f};
	public static Vec2 ballStartPosition = new Vec2(0.0f, 7.0f);
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
	
	public void setFitness(float fitness_in) {
		fitness = fitness_in;
	}
	
	public float getFitness() {
		return fitness;
	}
}
