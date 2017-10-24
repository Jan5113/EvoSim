import java.util.Random;

public class MutVal {
	private final float value;
	private final float mutRange;
	private Random normDis = new Random();
	private static float minRangeChange = 0.9f; //between 0 and 1
	private static float bigMutFactor = 10.0f;
	
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
		float mutFactor = (float) normDis.nextGaussian();
		float diff = mutFactor * mutRange;
		float rangeChange = Math.abs(mutFactor) + minRangeChange;
		
		if (normDis.nextFloat() < 0.05) { // 5% chance for bigger mutation 
			diff *= bigMutFactor;
		}
			
		return new MutVal(value + diff, Math.abs(diff * rangeChange));
	}
	
	public float getVal() {
		return value;
	}
	
	public float getMutRange() {
		return mutRange;
	}
	
}
