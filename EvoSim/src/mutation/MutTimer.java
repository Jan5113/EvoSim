package mutation;

public class MutTimer extends MutVal {

	public MutTimer() {
		super(0, 1, 0.5f);
	}
	
	public MutTimer(float rng) {
		super(0, 1, rng);
	}

	public MutTimer(float val, float rng) {
		super(val, rng);
	}
	
	public MutTimer(MutVal mutVal_in) {
		super(mutVal_in.getVal(), mutVal_in.getMutRange());
	}
	
	public float getVal() {
		return (Math.abs(super.getVal() % 1));
	}
	
	public MutTimer mutate() {
		return new MutTimer(super.mutate());
	}
	
	public MutTimer clone() {
		return new MutTimer(super.clone());
	}

}
