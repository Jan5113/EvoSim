package mutation;
import java.util.Random;

public class MutVal {
	private final float value;
	private float mutRange;
	private Random normDis = new Random();
	private static float minRangeChange = 0.9f; //between 0 and 1
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
		float mutFactor = (float) normDis.nextGaussian();
		
		if (normDis.nextFloat() < 0.05) { // 5% chance for bigger mutation 
			mutFactor *= bigMutFactor;
		}
				
		float rangeChange = Math.abs(mutFactor) + minRangeChange;
		float diff = mutFactor * mutRange * rangeChange;
		
		mutRange *= 0.999999f;
			
		return new MutVal(value + diff, Math.abs(diff * rangeChange));
	}
	
	public MutVal clone() {
		return new MutVal(value, mutRange);
	}
	
	public float getVal() {
		return value;
	}
	
	public float getSqVal() {
		return Math.abs(value);
	}
	
	public float getMutRange() {
		return mutRange;
	}
	
}
