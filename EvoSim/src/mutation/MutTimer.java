package mutation;

import java.io.Serializable;
import java.util.Random;

public class MutTimer extends MutVal implements Serializable {
	private static final long serialVersionUID = 1L;

	public MutTimer() {
		super(0, 1);
	}

	public MutTimer(float val, int genOffset_in) {
		
		super(0, 1, val % 1, genOffset_in);
	}
	
	public MutTimer(MutVal mutVal_in) {
		super(mutVal_in);
	}
	
	public float getVal() {
		return (Math.abs(super.getVal() % 1));
	}
	
	public MutTimer mutate(int gen) {
		Random r = new Random();
		float rand = (float) r.nextGaussian();
		float genMul = (float) Math.pow(0.9f, (gen - super.getGenOffset()) * 0.5f);
		float rng =  0.5f;
		float new_val = super.getVal() + (rand * genMul * rng);
		
		new_val = new_val % 1;
		
		int newGenOffset = super.getGenOffset();
		if(Math.abs(rand) > 2) {
			newGenOffset = gen / 2;
		}
		
//		if (newGenOffset > 3) {
//			System.out.println(super.getVal() + " " + new_val);
//		}
		
		return new MutTimer(new_val, newGenOffset);
	}
	
	public MutTimer clone() {
		return new MutTimer(super.clone());
	}

}
