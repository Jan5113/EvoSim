package population;

import creatureCreator.ProtoMuscle;
import mutation.MutTimer;
import mutation.MutVal;

public class Muscle {
    private final MutTimer timer1;
	private final MutTimer timer2;
    private final MutVal angle;
    private final MutVal angleOffset;
	private final float angleMin;
	private final float angleMax;
	private final int id;
	private final float maxTorque;
	private final boolean enabled;
    public Muscle(ProtoMuscle protM) {
        timer1 = new MutTimer();
		timer2 = new MutTimer();
		angle = new MutVal(0, 1);
		angleOffset = new MutVal((float) (-Math.PI), (float) (Math.PI));
		angleMin = (float) (protM.angleMin);
		angleMax = (float) (protM.angleMax);
		id = protM.ID;
		maxTorque = protM.torque;
		enabled = protM.enabled;
    }

    public Muscle(MutTimer timer1_in, MutTimer timer2_in, MutVal angle_in, MutVal angleOffset_in,
			float angleMin_in, float angleMax_in, float maxTorque_in, boolean enabled_in, int id_in) {
		timer1 = timer1_in;
		timer2 = timer2_in;
		angle = angle_in;
		angleOffset = angleOffset_in;
		angleMin = angleMin_in;
		angleMax = angleMax_in;
		id = id_in;
		maxTorque = maxTorque_in;
		enabled = enabled_in;
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