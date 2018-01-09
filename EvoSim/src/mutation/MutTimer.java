package mutation;

import population.Population;

public class MutTimer extends MutVal {

	public MutTimer() {
		super(0, 10);
	}

	public MutTimer(float val, Population pop) {
		
		super(-5, 5, val % 1);
	}
	
	public MutTimer(MutVal mutVal_in) {
		super(mutVal_in);
	}
	
	public float getVal() {
		return (Math.abs(super.getVal() % 1));
	}
	
	public MutTimer mutate(int gen) {
		return new MutTimer(super.mutate(gen));
	}
	
	public MutTimer clone() {
		return new MutTimer(super.clone());
	}

}
