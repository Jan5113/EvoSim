package box2d;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import mutation.MutVec2;

/**
 * The {@link B2DJoint} class is comparable to a position vector with
 * {@link B2DBone} connected to it. It has a {@code mutation()} code implemented.
 *
 */
public class B2DJoint {
	/**
	 * Holds the position of this {@link B2DJoint} as a {@link Vec2} vector.
	 */
	private final MutVec2 pos;
	/**
	 * Holds all references to {@link B2DBone} instances which are connected to this
	 * {@link B2DJoint} with their {@code head}.
	 */
	private ArrayList<B2DBone> registeredHeadBones = new ArrayList<B2DBone>();
	/**
	 * Holds all references to {@link B2DBone} instances which are connected to this
	 * {@link B2DJoint} with their end.
	 */
	private ArrayList<B2DBone> registeredEndBones = new ArrayList<B2DBone>();
	/**
	 * Each {@link B2DJoint} can be given an ID. This is used for identification of
	 * this {@link B2DJoint} instance when creating {@link B2DMuscle} instances.
	 */
	private final int id;
	/**
	 * A small offset from the default range to prevent clusters on the edges and in
	 * the corners.
	 */
	private final Vec2 offset;
	
	/**
	 * Creates a new {@link B2DJoint} within the given bounds {@code minXY_maxXY} randomly.
	 * <p>
	 * The coordinates are given as follows: {@code [left, bottom, right, top]}
	 * 
	 * @param minXY_maxXY
	 * bounding box, wherein the new {@link B2DJoint} will be created.
	 * @param id_in
	 * gives this instance a {@code final} ID.
	 */
	public B2DJoint(float[] minXY_maxXY, int id_in) {
		pos = new MutVec2(minXY_maxXY);
		id = id_in;
		
		offset = new Vec2((float) Math.random() / 1000, (float) Math.random() / 1000);
	}
	
	/**
	 * Creates a new {@link B2DJoint} from a position given as a {@link MutVec2}. 
	 * 
	 * @param mutVec2_in
	 * position of this {@link B2DJoint} instance
	 * @param id_in
	 * gives this instance a {@code final} ID
	 */
	public B2DJoint(MutVec2 mutVec2_in, int id_in) {
		pos = mutVec2_in.clone();
		id = id_in;
		
		offset = new Vec2((float) Math.random() / 1000, (float) Math.random() / 1000);
	}
	
	/**
	 * Takes the {@code int} current generation and returns a slightly varied
	 * version of this {@link B2DJoint} instance.
	 * 
	 * @param gen
	 *            current generation of the main Population.
	 * @return a mutated version of itself
	 */
	public B2DJoint mutate(int gen) {
		return new B2DJoint(pos.mutate(gen), id);
	}
	
	/**
	 * Gives a {@link Vec2} vector of the current position of this
	 * {@link B2DBone} instance.
	 * 
	 * @return the current position vector of the {@link B2DBone}
	 * 
	 */
	public Vec2 getPos() {
		//return pos.getVal();
		return pos.getVal().add(offset);
	}
	
	/**
	 * Registers the {@link B2DBone} given as one which originates from this
	 * {@link B2DJoint} instance.
	 * 
	 * @param boneRef_in
	 *            reference to a {@link B2DBone} with its {@code head} in this
	 *            {@link B2DJoint}
	 */
	public void registerHeadBone(B2DBone boneRef_in) {
		registeredHeadBones.add(boneRef_in);
	}
	
	/**
	 * Registers the {@link B2DBone} given as one which ends in this
	 * {@link B2DJoint} instance.
	 * 
	 * @param boneRef_in
	 *            reference to a {@link B2DBone} with its end in this
	 *            {@link B2DJoint}
	 */
	public void registerEndBone(B2DBone boneRef_in) {
		registeredEndBones.add(boneRef_in);
	}
	
	/**
	 * Gives an {@link ArrayList} with references to all the {@link B2DBone}
	 * instances, which originate from this {@link B2DJoint} instance.
	 * 
	 * @return
	 * contains all {@link B2DBone}s with their {@code head} in this {@link B2DJoint}
	 */
	public ArrayList<B2DBone> getRegisteredHeadBones() {
		return registeredHeadBones;
	}
	
	/**
	 * Gives an {@link ArrayList} with references to all the {@link B2DBone}
	 * instances, which end in this {@link B2DJoint} instance.
	 * 
	 * @return
	 * contains all {@link B2DBone}s with their {@code end} in this {@link B2DJoint}
	 */
	public ArrayList<B2DBone> getRegisteredEndBones() {
		return registeredEndBones;
	}

	/**
	 * Gives an {@link ArrayList} with references to all the {@link B2DBone}
	 * instances, which originate from or end in this {@link B2DJoint} instance.
	 * 
	 * @return
	 * contains all {@link B2DBone} connected to this {@link B2DJoint}
	 */
	public ArrayList<B2DBone> getAllRegisteredBones() {
		ArrayList<B2DBone> allBones = new ArrayList<B2DBone>();
		for (B2DBone b : registeredEndBones) {
			allBones.add(b);
		}
		for (B2DBone b : registeredHeadBones) {
			allBones.add(b);			
		}
		return allBones;
	}
	
	/**
	 * Returns the {@code int} given during initialisation of this {@link B2DJoint}
	 * instance.
	 * 
	 * @return ID of the {@link B2DJoint}
	 */
	public int getID() {
		return id;
	}
	
	public B2DJoint clone() {
		return new B2DJoint(pos, id);
	}
}
