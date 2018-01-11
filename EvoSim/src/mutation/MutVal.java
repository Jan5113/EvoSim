package mutation;

import java.util.Random;

public class MutVal {
	private final float value;
	private final float min;
	private final float max;
	
	public MutVal(float min_in, float max_in, float value_in) {
		if (min_in > max_in) max_in += (min_in - (min_in = max_in)); // swap
		value = value_in;
		min = min_in;
		max = max_in;
	}
	
	public MutVal(float min_in, float max_in) {
		if (min_in > max_in) max_in += (min_in - (min_in = max_in)); // swap
		min = min_in;
		max = max_in;
		value = (float) Math.random()*(max - min) + min;
	}
	
	public MutVal(MutVal mutVal_in) {
		min = mutVal_in.getMin();
		max = mutVal_in.getMax();
		value = mutVal_in.getVal();
	}
	
	public MutVal mutate(int gen) {
		Random r = new Random();
		float rand = r.nextFloat();
		float genMul = (float) Math.pow(0.95f, gen * 0.5f);
		float rng = (max - min) * 0.5f;
		float new_val = value + (rand * genMul * rng);
		
		if (new_val > max) {
			new_val = max;
		}
		else if (new_val < min) {
			new_val = min;
		}
		
		return new MutVal(min, max, new_val);
	}
	
	public MutVal clone() {
		return new MutVal(min, max, value);
	}
	
	public float getVal() {
		return value;
	}
	
	public float getAbsVal() {
		return Math.abs(value);
	}
	
	public float getMin() {
		return min;
	}
	
	public float getMax() {
		return max;
	}
	
}
