package population;

import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import box2d.B2DBody;
import creatureCreator.PosID;

public class Root implements BoneParent {
    private ArrayList<Muscle> muscleList;
    private ArrayList<Bone> rootChildren = new ArrayList<Bone>();
    private int currentBoneID = 1;
    private Vec2[] boundingBox; 

    public Root(boolean init){ //init test
        if (init) initTest();
    }
    private Root(int currentBoneID_in) {
        currentBoneID = currentBoneID_in;
    }

    public Muscle[] getMuscleList() {
        if (muscleList == null) {
            refreshMuscleList();
        }
        Muscle[] mArray = new Muscle[muscleList.size()];
        for (int i = 0; i < muscleList.size(); i++) {
            mArray[i] = muscleList.get(i);
        }
        return mArray;
    }

    public Vec2[] getBoundingBox() {
        if (boundingBox == null) {
            refreshBoundingBox();
        }
        return boundingBox;
    }

    public ArrayList<PosID> getJointPos() {
		ArrayList<PosID> jointList = new ArrayList<PosID>();
		jointList.add(new PosID(0, new Vec2()));
		for (Bone b : rootChildren) {
			b.getJointPos(jointList, new Vec2());
        }
		return jointList;
	}

    public ArrayList<PosID> getBonePos() {
		ArrayList<PosID> boneList = new ArrayList<PosID>();
		for (Bone b : rootChildren) {
			b.getJointPos(boneList, new Vec2());
        }
		return boneList;
	}

    public ArrayList<PosID> getMusclePos() {
		ArrayList<PosID> musclePosList = new ArrayList<PosID>();
		for (int i = 1; i < rootChildren.size(); i++) {
            rootChildren.get(i).getMusclePos(musclePosList, new Vec2());
		}
		return musclePosList;
	}

    public void buildCreature(World w, ArrayList<B2DBody> creatureInstances_in,
            ArrayList<RevoluteJoint> revoluteJoints_in) {
        Vec2 pos = getBoundingBox()[1].negate();
        //root bone (no muscle)
        B2DBody rootBody = rootChildren.get(0).build(w, creatureInstances_in, revoluteJoints_in,
                rootChildren.get(0).getLocalRoot(), pos);
        //rest
        for (int i = 1; i < rootChildren.size(); i++) {
            rootChildren.get(i).build(w, creatureInstances_in, revoluteJoints_in,
            rootBody, rootChildren.get(0).getLocalRoot(), pos);
		}

    }

    public void addBone(Vec2 dir_head) {
        addBone(0, dir_head);
        muscleList = null;
    }

    public void addBone(int parentID, Vec2 dir) {
        if (parentID == 0) {
            if (currentBoneID == 1) {
                rootChildren.add(new Bone(dir, this, currentBoneID, null));
            } else {
                rootChildren.add(new Bone(dir, this, currentBoneID, new Muscle()));
            }
        } else {
            for (Bone b : rootChildren) {
                b.addBone(parentID, dir, currentBoneID);
            }
        }
        currentBoneID++;
        muscleList = null;
        boundingBox = null;
    }

    public void addHead(int parentID, float size) {
        if (parentID == 0) {
            Bone head = new Bone(new Vec2(0,0), this, currentBoneID, new Muscle());
            head.setBoneType(BoneType.HEAD);
            rootChildren.add(head);
        } else {
            for (Bone b : rootChildren) {
                b.addHead(parentID, size, currentBoneID);
            }
        }
        currentBoneID++;
        muscleList = null;
        boundingBox = null;
    }

    public void refreshMuscleList() {
        muscleList = new ArrayList<Muscle>();
        for (Bone b : rootChildren) {
            b.getMuscles(muscleList);
        }
    }

    public void refreshBoundingBox() {
        boundingBox = new Vec2[]{new Vec2(), new Vec2()};
        for (Bone b : rootChildren) {
            b.getBounds(boundingBox, new Vec2());
        }
    }

    public Root clone() {
        ArrayList<Bone> cloneRootChildren = new ArrayList<Bone>();
        Root clone = new Root(currentBoneID);
        for (Bone b : rootChildren) {
            cloneRootChildren.add(b.clone(clone));
        }
        clone.rootChildren = cloneRootChildren;
        return clone;
    }

    public Root mutate(int gen) {
        ArrayList<Bone> mutateRootChildren = new ArrayList<Bone>();
        Root mutant = new Root(currentBoneID);
        for (Bone b : rootChildren) {
            mutateRootChildren.add(b.mutate(mutant, gen));
        }
        mutant.rootChildren = mutateRootChildren;
        return mutant;
    }

    public Root getNewInit() {
        Root clone = clone();
        for (Bone b : clone.rootChildren) {
            b.newInitMuscle();
        }
        return clone;
    }

    public PosID selectJointNear(Vec2 pos) { // search for head -> for adding new bones
		float dist = 0f;
		PosID id = new PosID(-1, new Vec2());
		for (PosID pid : getJointPos()) {
			float tempDist = 1/(pid.pos.add(pos.negate()).lengthSquared() + 1);
			if (tempDist > dist) {
				dist = tempDist;
				id = pid;
			}
		}		
		return id;
	}

	public PosID selectMuscleNear(Vec2 pos) { // search for root -> for editing muscle
		float dist = 0f;
		PosID ids = new PosID(new int[]{-1}, new Vec2());
		for (PosID pid : getMusclePos()) {
			float tempDist = 1/(pid.pos.add(pos.negate()).lengthSquared() + 1);
			if (tempDist > dist) {
				dist = tempDist;
				ids = pid;
			}
		}
		return ids;
	}

	public PosID selectBoneNear(Vec2 pos) {
		float dist = 0f;
		PosID id = new PosID(-1, new Vec2());
		for (PosID pid : getBonePos()) {
			float tempDist = 1/(pid.pos.add(pos.negate()).lengthSquared() + 1);
			if (tempDist > dist) {
				dist = tempDist;
				id = pid;
			}
		}		
		return id;
	}


    public Vec2 getHeadPosition() {
        return new Vec2();
	}

    private void initTest() {
        addBone(new Vec2(0.5f,1));
        addBone(1, new Vec2(0.5f,-1));
    }
    
}