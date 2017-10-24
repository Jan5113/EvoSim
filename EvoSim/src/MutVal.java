import java.util.Random;

public class MutVal {
	private final float value;
	private final float mutRange;
	private Random normDis = new Random();
	private static float mutFactor = 2.0f;
	private static float bigMutFactor = 5.0f;
	
	public MutVal(float min, float max, float rng) {
		if (min > max) max += (min - (min = max)); // swap
		value = (float) Math.random() * (max - min) + min;
		mutRange = rng;
	}
	
	public MutVal(float val, float rng) {
		value = val;
		mutRange = rng;
	}
	
	public MutVal mutate() {
		float diff = (float) normDis.nextGaussian() * mutRange;
		
		if (normDis.nextFloat() < 0.05) { // 5% chance for bigger mutation 
			diff *= bigMutFactor;
		}
			
		return new MutVal(value + diff, diff * mutFactor);
	}
	
	public float getVal() {
		return value;
	}
	
	public float getMutRange() {
		return mutRange;
	}
	
}
