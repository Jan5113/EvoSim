package mutation;

import java.util.Random;

public class MutTimer extends MutVal {

	public MutTimer() {
		super(0, 1);
	}

	public MutTimer(float val) {
		
		super(0, 1, val % 1);
	}
	
	public MutTimer(MutVal mutVal_in) {
		super(mutVal_in);
	}
	
	public float getVal() {
		return (Math.abs(super.getVal() % 1));
	}
	
	public MutTimer mutate(int gen) {
		Random r = new Random();
		float rand = r.nextFloat();
		float genMul = (float) Math.pow(0.95f, gen * 0.5f);
		float rng =  0.5f;
		float new_val = super.getVal() + (rand * genMul * rng);
		
		new_val = new_val % 1;
		
		return new MutTimer(new_val);
	}
	
	public MutTimer clone() {
		return new MutTimer(super.clone());
	}

}
