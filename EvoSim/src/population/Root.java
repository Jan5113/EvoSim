package population;

import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import box2d.B2DBody;
import creatureCreator.PosID;
import mutation.MutVec2;

public class Root implements BoneParent {
    private MutVec2 rootPos;
    private ArrayList<Muscle> muscleList;
    private ArrayList<Bone> rootChildren = new ArrayList<Bone>();
    private int currentBoneID = 1;

    public Root() {
    }

    public Root(int init){ //init test
        switch (init) {
            case 0:
                initTest();
                break;
        
            default:
                break;
        }
    }
    private Root(MutVec2 rootPos_in, int currentBoneID_in) {
        rootPos = rootPos_in;
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

    public ArrayList<PosID> getJointPos() {
		ArrayList<PosID> jointList = new ArrayList<PosID>();
		jointList.add(new PosID(0, rootPos.getVal()));
		for (Bone b : rootChildren) {
			b.getJointPos(jointList, rootPos.getVal());
        }
		return jointList;
	}

    public ArrayList<PosID> getBonePos() {
		ArrayList<PosID> boneList = new ArrayList<PosID>();
		for (Bone b : rootChildren) {
			b.getJointPos(boneList, rootPos.getVal());
        }
		return boneList;
	}

    public ArrayList<PosID> getMusclePos() {
		ArrayList<PosID> musclePosList = new ArrayList<PosID>();
		for (int i = 1; i < rootChildren.size(); i++) {
            rootChildren.get(i).getMusclePos(musclePosList, rootPos.getVal());
		}
		return musclePosList;
	}

    public void buildCreature(World w, ArrayList<B2DBody> creatureInstances_in, ArrayList<RevoluteJoint> revoluteJoints_in) {
        
        B2DBody rootBody = rootChildren.get(0).build(w, creatureInstances_in, revoluteJoints_in,
                rootChildren.get(0).getLocalRoot());
        
        for (int i = 1; i < rootChildren.size(); i++) {
            rootChildren.get(i).build(w, creatureInstances_in, revoluteJoints_in,
            rootBody, rootChildren.get(0).getLocalRoot());
		}

    }

    public void addBone(Vec2 pos_root, Vec2 pos_head) {
        rootPos = new MutVec2(pos_root);
        addBone(0, pos_head.add(pos_root.negate()));
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
    }

    public void refreshMuscleList() {
        muscleList = new ArrayList<Muscle>();
        for (Bone b : rootChildren) {
            b.getMuscles(muscleList);
        }
    }

    public Root clone() {
        ArrayList<Bone> cloneRootChildren = new ArrayList<Bone>();
        Root clone = new Root(rootPos.clone(), currentBoneID);
        for (Bone b : rootChildren) {
            cloneRootChildren.add(b.clone(clone));
        }
        clone.rootChildren = cloneRootChildren;
        return clone;
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
        return rootPos.getVal();
	}

    public Root cloneCreature() {
        return null;
    }

    public Root mutateCreature() {
        return null;
    }

    private void initTest() {
        addBone(new Vec2(-1,0), new Vec2(-0.5f,1));
        addBone(1, new Vec2(0.5f,-1));
    }
    
}