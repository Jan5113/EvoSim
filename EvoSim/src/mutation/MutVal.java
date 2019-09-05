package mutation;

import java.io.Serializable;
import java.util.Random;

public class MutVal implements Serializable {
	private static final long serialVersionUID = 1L;
	private final float value;
	private final float min;
	private final float max;
	
	private int genOffset = 0;
	
	public MutVal(float min_in, float max_in, float value_in, int genOffset_in) {
		if (min_in > max_in) max_in += (min_in - (min_in = max_in)); // swap
		value = value_in;
		min = min_in;
		max = max_in;
		genOffset = genOffset_in;
	}

	public MutVal(float min_in, float max_in, float value_in) {
		if (min_in > max_in) max_in += (min_in - (min_in = max_in)); // swap
		value = value_in;
		min = min_in;
		max = max_in;
		genOffset = 0;
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

	public MutVal mul(float f) {
		return new MutVal(min, max, value*f, genOffset);
	}
	
	public MutVal mutate(int gen) {
		Random r = new Random();
		float rand = (float) r.nextGaussian();
		float genMul = (float) Math.pow(0.9f, (gen - genOffset) * 0.5f);
		float rng = (max - min) * 0.5f;
		float new_val = value + (rand * genMul * rng);
		
		if (new_val > max) {
			new_val = max;
		}
		else if (new_val < min) {
			new_val = min;
		}

		int newGenOffset = genOffset - 1;
		if(Math.abs(rand) > 2) {
			newGenOffset = gen / 2;
		}
		
		if (newGenOffset < 0) newGenOffset = 0;
		
		return new MutVal(min, max, new_val, newGenOffset);
	}
	
	public int getGenOffset() {
		return genOffset;
	}
	
	public MutVal clone() {
		return new MutVal(min, max, value, genOffset);
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
