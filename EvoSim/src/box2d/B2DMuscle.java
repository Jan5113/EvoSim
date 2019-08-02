package box2d;
import java.io.Serializable;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import creatureCreator.ProtoMuscle;
import mutation.MutTimer;
import mutation.MutVal;
import population.Creature;
import test.Test;


/**
 * A {@link B2DMuscle} sits on a {@link B2DJoint} and acts as a "rotation"
 * muscle between two {@link B2DBone} instances connected to the
 * {@link B2DJoint}. It moves the bones by going back and forth between two
 * given angles. Two {@link MutTimer} instances determine, when the muscle is
 * which position. The absolute timing is calculated by using the "internal
 * clock" ({@code MutVal cycleLength}) of every {@link Creature} by the
 * {@link Test}
 * <p>
 * The {@link B2DMuscle} movement itself is regulated and their torque
 * ("strength") is limited; the speed is solely determined by the deviation from
 * the desired angle (hence "regulated"). The {@link B2DMuscle} can make up to
 * half a rotation (PI rad). It has a {@code mutation()} code implemented
 * changing the timings and the angles.
 */
public class B2DMuscle implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Specifies the {@link B2DJoint} this {@link B2DMuscle} is located in. It can
	 * only move two {@link B2DBone} instances which are connected (registered) in
	 * the {@link B2DJoint}.
	 */
	private final B2DJoint joint;
	/**
	 * First {@link B2DBone} this {@link B2DMuscle} instance is connected to. The
	 * {@link B2DBone} must be connected to the {@link B2DJoint} {@code joint}.
	 */
	private final B2DBone boneA;
	/**
	 * Second {@link B2DBone} this {@link B2DMuscle} instance is connected to. The
	 * {@link B2DBone} must be connected to the {@link B2DJoint} {@code joint}.
	 */
	private final B2DBone boneB;
	/**
	 * Holds the orientation for {@code boneA}
	 */
	private B2DBoneDir boneADir;
	/**
	 * Holds the orientation for {@code boneB}
	 */
	private B2DBoneDir boneBDir;
	/**
	 * Determines when this {@link B2DMuscle} moves to the first angle ({@code angle})
	 */
	private final MutTimer timer1;
	/**
	 * Determines when this {@link B2DMuscle} moves to the second angle ({@code angle + angleOffset})
	 */
	private final MutTimer timer2;
	/**
	 * Angle (percentage between {@code angleMin} and {@code angleMax}) between {@code boneA}
	 * and {@code boneB} when {@code timer1} is active. Angle in rad between 0 and 1.
	 */
	private final MutVal angle;
	/**
	 * The angle between {@code boneA} and {@code boneB} when {@code timer2} is
	 * active is {@code angle + angleOffset}. {@code angleOffset} is in rad and
	 * between {@code -0.5} and {@code 0.5}. This limits the {@link B2DMuscle} to
	 * only move up to 180°.
	 */
	private final MutVal angleOffset;
	
	private final float angleMin;
	
	private final float angleMax;
	
	/**
	 * Each {@link B2DMuscle} can be given an ID. This is used for identification.
	 */
	private final int id;
	/**
	 * The {@code healthy boolean} is {@code true} when both {@link B2DBone} instances given during
	 * initialisation are actually connected to the {@link B2DJoint} this instance
	 * is referenced to and orientated the correct way.
	 */
	private boolean healthy;
	
	/**
	 * {@code maxTorque} hold the maximum "strength" a {@link B2DMuscle} is able to
	 * apply to the {@link B2DBone}s connected to it.
	 */
	private final float maxTorque;
	
	private final boolean enabled;
	
	/**
	 * Creates a new {@link B2DMuscle} with all parameters given. {@code joint_in}
	 * references the {@link B2DJoint} this instance will connect to.
	 * {@code boneA_in} and {@code boneB_in} reference the two {@link B2DBone}
	 * instances this {@code B2DMuscle} will move. {@code timer1_in} and
	 * {@code timer2_in} are {@link MutTimer} instances, {@code angle_in} and
	 * {@code angleOffset_in} are {@link MutVal} instances and directly set their
	 * corresponding value in this new {@link B2DMuscle}.
	 * <p>
	 * <strong>Note: </strong> Both bones must be connected (referenced) to the
	 * {@link B2DJoint} {@code joint_in}. Calling {@code getHealth()} will return
	 * {@code false} if that's not the case.
	 * 
	 * @param joint_in
	 *            defines the location of the {@link B2DMuscle}
	 * @param boneA_in
	 *            defines the first {@link B2DBone} this {@link B2DMuscle} can move
	 * @param boneB_in
	 *            defines the second {@link B2DBone} this {@link B2DMuscle} can move
	 * @param timer1_in
	 *            defines the time period the {@code B2DMuscle} moves to the first
	 *            angle
	 * @param timer2_in
	 *            defines the time period the {@code B2DMuscle} moves to the second
	 *            angle
	 * @param angle_in
	 *            defines the first angle
	 * @param angleOffset_in
	 *            defines the difference between the first and the second angle
	 *            (maximum difference is PI)
	 * @param id_in
	 *            gives this instance a {@code final} ID
	 */
	public B2DMuscle(B2DJoint joint_in, B2DBone boneA_in, B2DBone boneB_in,
			MutTimer timer1_in, MutTimer timer2_in, MutVal angle_in, MutVal angleOffset_in,
			float angleMin_in, float angleMax_in, float maxTorque_in, boolean enabled_in, int id_in) {
		joint = joint_in;
		boneA = boneA_in;
		boneB = boneB_in;
		timer1 = timer1_in;
		timer2 = timer2_in;
		angle = angle_in;
		angleOffset = angleOffset_in;
		angleMin = angleMin_in;
		angleMax = angleMax_in;
		id = id_in;
		maxTorque = maxTorque_in;
		enabled = enabled_in;
		
		initialiseMuscle();
	}
	
	/**
	 * Creates a new {@link B2DMuscle} instance with new references to
	 * {@link B2DJoint} and {@link B2DBone} instances. {@code joint_in} references
	 * the {@link B2DJoint} this instance will connect to. {@code boneA_in} and
	 * {@code boneB_in} reference the two {@link B2DBone} instances this
	 * {@code B2DMuscle} will move. The other values for the angles and timings are
	 * randomly generated.
	 * <p>
	 * <strong>Note: </strong> Both bones must be connected (referenced) to the
	 * {@link B2DJoint} {@code joint_in}. Calling {@code getHealth()} will return
	 * {@code false} if that's not the case.
	 * 
	 * @param joint_in
	 *            defines the location of the {@link B2DMuscle}
	 * @param boneA_in
	 *            defines the first {@link B2DBone} this {@link B2DMuscle} can move
	 * @param boneB_in
	 *            defines the second {@link B2DBone} this {@link B2DMuscle} can move
	 * @param id_in
	 *            gives this instance a {@code final} ID
	 */
	public B2DMuscle(B2DJoint joint_in, B2DBone boneA_in, B2DBone boneB_in,
			float maxTorque_in, boolean enabled_in, int id_in) {
		joint = joint_in;
		boneA = boneA_in;
		boneB = boneB_in;
		timer1 = new MutTimer();
		timer2 = new MutTimer();
		angle = new MutVal(0, 1);
		angleOffset = new MutVal((float) (-Math.PI), (float) (Math.PI));
		angleMin = (float) (-Math.PI);
		angleMax = (float) (3*Math.PI);
		id = id_in;
		maxTorque = maxTorque_in;
		enabled = enabled_in;
		
		initialiseMuscle();
	}

	public B2DMuscle(B2DJoint joint_in, B2DBone boneA_in, B2DBone boneB_in,
			float minAngle, float maxAngle, float maxTorque_in, boolean enabled_in, int id_in) {
		joint = joint_in;
		boneA = boneA_in;
		boneB = boneB_in;
		timer1 = new MutTimer();
		timer2 = new MutTimer();
		angle = new MutVal(0, 1);
		angleOffset = new MutVal((float) (-Math.PI), (float) (Math.PI));
		angleMin = (float) (minAngle);
		angleMax = (float) (maxAngle);
		id = id_in;
		maxTorque = maxTorque_in;
		enabled = enabled_in;
		
		initialiseMuscle();
	}
	
	public B2DMuscle(ProtoMuscle protM, B2DJoint[] joints, B2DBone[] bones) {
		joint = joints[protM.IDJoint];
		boneA = bones[protM.IDBoneA];
		boneB = bones[protM.IDBoneB];
		timer1 = new MutTimer();
		timer2 = new MutTimer();
		angle = new MutVal(0, 1);
		angleOffset = new MutVal((float) (-Math.PI), (float) (Math.PI));
		angleMin = (float) (protM.angleMin);
		angleMax = (float) (protM.angleMax);
		id = protM.ID;
		maxTorque = protM.torque;
		enabled = protM.enabled;
		
		initialiseMuscle();
	}
	
	/**
	 * Returns a new {@link B2DMuscle} instance with slightly varied values for the
	 * contraction times and angles but with new references. {@code joint_in}
	 * references the {@link B2DJoint} the new instance will connect to.
	 * {@code boneA_in} and {@code boneB_in} reference the two {@link B2DBone}
	 * instances the new {@code B2DMuscle} will move. The other values for the
	 * angles and timings are slightly changed.
	 * <p>
	 * <strong>Note: </strong> Both bones must be connected (referenced) to the
	 * {@link B2DJoint} {@code joint_in}. Calling {@code getHealth()} of the
	 * returned instance will return {@code false} if that's not the case.
	 * 
	 * @param joint_in
	 *            is the new {@link B2DJoint} of the mutated {@link B2DMuscle}
	 * @param boneA_in
	 *            is the new first {@link B2DBone} of the mutated {@link B2DMuscle}
	 * @param boneB_in
	 *            is the new second {@link B2DBone} of the mutated {@link B2DMuscle}
	 * @param gen
	 *            current generation of the main Population
	 * @return a new mutated {@link B2DMuscle} with the new references
	 */
	public B2DMuscle rereferencedMutate(B2DJoint joint_in, B2DBone boneA_in, B2DBone boneB_in, int gen) {
		return new B2DMuscle(joint_in, boneA_in, boneB_in,
				timer1.mutate(gen), timer2.mutate(gen),
				angle.mutate(gen), angleOffset.mutate(gen),
				angleMin, angleMax, maxTorque, enabled, id);
	}

	/**
	 * Returns a new {@link B2DMuscle} instance with identical values for the
	 * contraction times and angles but with new references. {@code joint_in}
	 * references the {@link B2DJoint} the new instance will connect to.
	 * {@code boneA_in} and {@code boneB_in} reference the two {@link B2DBone}
	 * instances the new {@code B2DMuscle} will move. The other values for the
	 * angles and timings are directly copied to the new {@link B2DMuscle} instance.
	 * <p>
	 * <strong>Note: </strong> Both bones must be connected (referenced) to the
	 * {@link B2DJoint} {@code joint_in}. Calling {@code getHealth()} of the
	 * returned instance will return {@code false} if that's not the case.
	 * 
	 * @param joint_in
	 *            is the new {@link B2DJoint} of the cloned {@link B2DMuscle}
	 * @param boneA_in
	 *            is the new first {@link B2DBone} of the cloned {@link B2DMuscle}
	 * @param boneB_in
	 *            is the new second {@link B2DBone} of the cloned {@link B2DMuscle}
	 * @return a cloned {@link B2DMuscle} with new references
	 */
	public B2DMuscle rereferencedClone(B2DJoint joint_in, B2DBone boneA_in, B2DBone boneB_in) {
		return new B2DMuscle(joint_in, boneA_in, boneB_in,
				timer1.clone(), timer2.clone(),
				angle.clone(), angleOffset.clone(),
				angleMin, angleMax, maxTorque, enabled, id);
	}
	
	/**
	 * This method initialises the values for the {@link B2DBone} orientation.
	 * Furthermore it checks, if the {@link B2DBone} instances are actually
	 * connected to the {@link B2DJoint} given during initialisation. A negative
	 * will result in an error message. This method is called only when creating a
	 * new {@link B2DMuscle} instance.
	 */
	private void initialiseMuscle() {
		boolean[] checkBones = {false, false};
		ArrayList<B2DBone> headBones = joint.getRegisteredHeadBones();
		for (int i = 0; i < headBones.size(); i++) {
			if (headBones.get(i) == boneA) {
				checkBones[0] = true;
				boneADir = B2DBoneDir.HEAD;
			} else if (headBones.get(i) == boneB) {
				checkBones[1] = true;
				boneBDir = B2DBoneDir.HEAD;
			}
		}
		
		ArrayList<B2DBone> endBones = joint.getRegisteredEndBones();
		for (int i = 0; i < endBones.size(); i++) {
			if (endBones.get(i) == boneA) {
				checkBones[0] = true;
				boneADir = B2DBoneDir.END;
			} else if (endBones.get(i) == boneB) {
				checkBones[1] = true;
				boneBDir = B2DBoneDir.END;
			}
		}
		
		if (checkBones[0] && checkBones[1]) {
			healthy = true;
		} else {
			System.err.println("Muscle " + id + " not healty!");
			healthy = false;
		}
	}
	
	/**
	 * This method returns {@code true} only if the {@link B2DMuscle} had been
	 * initialised correctly. That being that both {@link B2DBone} instances given
	 * during initialisation are actually connected and registered in the
	 * {@link B2DJoint} given.
	 * <p>
	 * If it returns {@code false} one or both {@link B2DBone} instances are not
	 * connected to the {@link B2DJoint} instance given during initialisation.
	 * 
	 * @return
	 * {@code true} is setup correctly
	 */
	public boolean getHealth() {
		return healthy;
	}
	
	/**
	 * Returns the {@code int} given during initialisation of this {@link B2DMuscle}
	 * instance.
	 * 
	 * @return ID of the {@link B2DMuscle}
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Returns the ID of the {@link B2DJoint} given during initialisation of this
	 * {@link B2DMuscle} instance.
	 * 
	 * @return ID of the referenced {@link B2DJoint}
	 */
	public int getJointID() {
		return joint.getID();
	}
	
	/**
	 * Returns whether this {@link B2DMuscle} is active at the time given.
	 * {@code percentOfCycle} is a {@code float} between {@code 0.0f} and
	 * {@code 1.0} and states the modulo of the {@link Creature}s cycle length and
	 * the time passed. It is calculated by:
	 * <p>
	 * ({@code cycleLength % timePassed)/cycleLength}).
	 * <p>
	 * The {@link MutTimer} values of this {@link B2DMuscle} instance are between
	 * {@code 0.0f} and {@code 1.0} as well. If {@code percentOfCycle} has last
	 * surpassed the first timer the method returns {@code true}, otherwise it
	 * returns {@code false}
	 * 
	 * @param percentOfCycle
	 * "percentage" of the {@link Creature}s "inner clock" {@code cycleLength}
	 * @return
	 * whether this {@link B2DMuscle} is active at the time given
	 * 
	 */
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
		
	/**
	 * Gives the static maximum torque or "strength" of {@link B2DMuscle}
	 * 
	 * @return torque for {@link RevoluteJoint}
	 */
	public float getTorque() {
		return maxTorque;
	}

	/**
	 * Gives an {@link B2DBone}{@code [2]} array with the {@link B2DBone} instances this
	 * {@link B2DMuscle} moves.
	 * <p>
	 * {@code [boneA, boneB]}
	 * 
	 * @return references to the {@link B2DBone}s affected by this {@link B2DMuscle}
	 */
	public B2DBone[] getBones() {
		B2DBone[] bones = {boneA, boneB};
		return bones;
	}
	
	/**
	 * Gives a {@link Vec2} vector of the current position of this {@link B2DMuscle}
	 * instance. It corresponds to its {@link B2DJoint} position.
	 * 
	 * @return the current position vector of the {@link B2DMuscle}
	 * 
	 */
	public Vec2 getPos() {
		return joint.getPos();
	}
	
	/**
	 * Gives an {@link B2DBoneDir}{@code [2]} array with the orientation of the
	 * {@link B2DBone} instances this {@link B2DMuscle} moves.
	 * <p>
	 * {@code [boneDirA, boneDirB]}
	 * 
	 * @return orientation of {@link B2DBone}s referenced in this {@link B2DMuscle}
	 *         instance
	 */
	public B2DBoneDir[] getBoneDirs() {
		B2DBoneDir[] boneDirs = {boneADir, boneBDir};
		return boneDirs;
	}
	
	/**
	 * Gives the angle (in rad) between {@code boneA} and {@code boneB} (CCW) when
	 * {@code timer1} is active.
	 * 
	 * @return angle when {@code isActive()} is {@code true}
	 */
	public float getOffAngle() {
		return angleMin + angle.getVal() * (angleMax - angleMin);
	}
	
	/**
	 * Gives the angle (in rad) between {@code boneA} and {@code boneB} (CCW) when
	 * {@code timer2} is active.
	 * 
	 * @return angle when {@code isActive()} is {@code false}
	 */
	public float getOnAngle() {
		float offAngle = getOffAngle() + angleOffset.getVal();
		if (offAngle < angleMin) return angleMin;
		else if (offAngle > angleMax) return angleMax;
		return offAngle;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
}