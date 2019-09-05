package population;

import mutation.MutTimer;
import mutation.MutVal;

public class Muscle {
    private MutTimer timer1 = new MutTimer();
	private MutTimer timer2 = new MutTimer();
	private MutVal angle = new MutVal(0, 1);
	private MutVal angleOffset = new MutVal((float) (-Math.PI), (float) (Math.PI));
	
	private float maxTorque = 5.0f;
	private float angleMin = (float) (-Math.PI);
	private float angleMax = (float) (3*Math.PI);
	private boolean enabled = true;

    public Muscle() {
	}
	
    public Muscle(boolean enabled_in) {
		enabled = enabled_in;
    }

    private Muscle(MutTimer timer1_in, MutTimer timer2_in, MutVal angle_in, MutVal angleOffset_in,
			float angleMin_in, float angleMax_in, float maxTorque_in, boolean enabled_in) {
		timer1 = timer1_in;
		timer2 = timer2_in;
		angle = angle_in;
		angleOffset = angleOffset_in;
		angleMin = angleMin_in;
		angleMax = angleMax_in;
		maxTorque = maxTorque_in;
		enabled = enabled_in;
	}

	public Muscle clone() {
		return new Muscle(timer1.clone(), timer2.clone(), angle.clone(), angleOffset.clone(), 
		angleMin, angleMax, maxTorque, enabled);
	}

	public Muscle mutate(int gen) {
		return new Muscle(timer1.mutate(gen), timer2.mutate(gen), angle.mutate(gen), angleOffset.mutate(gen), 
		angleMin, angleMax, maxTorque, enabled);
	}

	public void newInit() {
		timer1 = new MutTimer();
		timer2 = new MutTimer();
		angle = new MutVal(0, 1);
		angleOffset = new MutVal((float) (-Math.PI), (float) (Math.PI));
	}


    public float getTorque() {
        return maxTorque;
    }

    public boolean isEnabled() {
        return enabled;
    }
    
	public boolean isActivated(float percentOfCycle) {
		if (timer1.getVal() < timer2.getVal()) {
			if (percentOfCycle > timer1.getVal() && percentOfCycle < timer2.getVal()) {
				return true;
			} else {
				return false;
			}
		} else {
			if (percentOfCycle < timer1.getVal() && percentOfCycle > timer2.getVal()) {
				return false;
			} else {
				return true;
			}
		}
    }

    public float getOffAngle() {
		return angleMin + angle.getVal() * (angleMax - angleMin);
	}
	
	public float getOnAngle() {
		float offAngle = getOffAngle() + angleOffset.getVal();
		if (offAngle < angleMin) return angleMin;
		else if (offAngle > angleMax) return angleMax;
		return offAngle;
	}

}