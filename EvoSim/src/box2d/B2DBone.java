package box2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * A {@link B2DBone} connects two {@link B2DJoint} instances with a rigid
 * {@link B2DBody}. {@link B2DBone}s have an orientation, therefore a head- and
 * an end-{@link B2DJoint} determined by the order during initialisation. This
 * is later necessary when a {@link B2DMuscle} has to connect the ends of two
 * {@link B2DBone}s the right way.
 * <p>
 * This {@code class} is <strong>not</strong> involved in the calculations of a
 * {@link World}, it is only providing the starting values and properties of a
 * bone.
 *
 */
public class B2DBone {
	/**
	 * Holds a reference to the joint this {@link B2DBone} is connected to with its head.
	 */
	private final B2DJoint headJoint;
	/**
	 * Holds a reference to the joint this {@link B2DBone} is connected to with its end.
	 */
	private final B2DJoint endJoint;
	/**
	 * Each {@link B2DBone} can be given an ID. This is used for identification of
	 * this {@link B2DBone} instance when creating {@link B2DMuscle} instances.
	 */
	private final int id;
	/**
	 * Holds the between the head- and tail-{@link B2DJoint} for display purposes.
	 */
	private final float halfLength;
	
	/**
	 * Creates a new {@link B2DBone} instance connecting the {@link B2DJoint} given.
	 * 
	 * @param jointA_in
	 * {@code final} reference to the {@link B2DJoint} where the {@link B2DBone} begins
	 * @param jointB_in
	 * {@code final} reference to the {@link B2DJoint} where the {@link B2DBone} ends
	 * @param id_in
	 * Gives this instance a {@code final} name.
	 */
	public B2DBone(B2DJoint jointA_in, B2DJoint jointB_in, int id_in) {
		headJoint = jointA_in;
		endJoint = jointB_in;
		id = id_in;
		
		halfLength = (endJoint.getPos().add(headJoint.getPos().negate()).length()) * 0.5f;

		headJoint.registerHeadBone(this);
		endJoint.registerEndBone(this);
	}
	
	/**
	 * Returns an array[2] with the reference to the head-{@link B2DJoint} at
	 * {@code [0]} and the end-{@link B2DJoint} at {@code [1]}
	 * 
	 * @return gives a {@link B2DJoint}{@code[2]} with head- and end-
	 *         {@link B2DJoint} of this {@link B2DBone} instance.
	 */
	public B2DJoint[] getJoints() {
		B2DJoint[] joints = {headJoint, endJoint};
		return joints;
	}
	
//	public B2DBone clone() {
//		return new B2DBone(headJoint.clone(), endJoint.clone(), id);
//	}
	
	/**
	 * Creates and returns a clone of this {@link B2DBone} instance with new
	 * references to head- and end-{@link B2DJoint}s
	 * 
	 * @param jointA_in
	 * {@code final} reference to the {@link B2DJoint} where the {@link B2DBone} begins
	 * @param jointB_in
	 * {@code final} reference to the {@link B2DJoint} where the {@link B2DBone} ends
	 * @return
	 * a clone of this {@link B2DBone} instance with replaced {@link B2DJoint} references
	 */
	public B2DBone rereferencedClone(B2DJoint jointA_in, B2DJoint jointB_in) {
		return new B2DBone(jointA_in, jointB_in, id);
	}
	
	/**
	 * Returns the {@code int} given during initialisation of this {@link B2DBone}
	 * instance.
	 * 
	 * @return ID of the {@link B2DBone}
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Gives the total length between the head- and end-{@link B2DJoint} divided by
	 * 2 of this {@link B2DBone} instance.
	 * 
	 * @return {@code length * 0.5f} of this {@link B2DBone}
	 */
	public float getHalfLen() {
		return halfLength;
	}
	
	/**
	 * Gives a {@link Vec2} vector for the local coordinates of the head-{@link B2DJoint} of this {@link B2DBone} instance.
	 * 
	 * @return the location to the head-{@link B2DJoint}
	 */
	public Vec2 getLocalHead() {
		return new Vec2(-halfLength, 0);
	}
	
	/**
	 * Gives a {@link Vec2} vector for the local coordinates of the end-{@link B2DJoint} of this {@link B2DBone} instance.
	 * 
	 * @return the location to the end-{@link B2DJoint}
	 */
	public Vec2 getLocalEnd() {
		return new Vec2(halfLength, 0);
	}
}
