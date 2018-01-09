package box2d;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import mutation.MutVec2;

public class B2DJoint {
	private final MutVec2 pos;
	private ArrayList<B2DBone> registeredHeadBones = new ArrayList<B2DBone>();
	private ArrayList<B2DBone> registeredEndBones = new ArrayList<B2DBone>();
	private final int id;
	private final Vec2 offset;
	
	public B2DJoint(float[] minXY_maxXY, int id_in) {
		pos = new MutVec2(minXY_maxXY);
		id = id_in;
		
		offset = new Vec2((float) Math.random() / 1000, (float) Math.random() / 1000);
	}
	
	public B2DJoint(MutVec2 mutVec2_in, int id_in) {
		pos = mutVec2_in.clone();
		id = id_in;
		
		offset = new Vec2((float) Math.random() / 1000, (float) Math.random() / 1000);
	}
	
	public B2DJoint mutate(int gen) {
		return new B2DJoint(pos.mutate(gen), id);
	}
	
	public Vec2 getPos() {
		//return pos.getVal();
		return pos.getVal().add(offset);
	}
	
	public void registerHeadBone(B2DBone boneRef_in) {
		registeredHeadBones.add(boneRef_in);
	}
	
	public void registerEndBone(B2DBone boneRef_in) {
		registeredEndBones.add(boneRef_in);
	}
	
	public ArrayList<B2DBone> getRegisteredHeadBones() {
		return registeredHeadBones;
	}
	
	public ArrayList<B2DBone> getRegisteredEndBones() {
		return registeredEndBones;
	}

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
	
	public int getID() {
		return id;
	}
	
	public B2DJoint clone() {
		return new B2DJoint(pos, id);
	}
}
