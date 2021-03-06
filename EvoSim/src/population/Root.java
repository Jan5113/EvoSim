package population;

import java.io.Serializable;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import box2d.B2DBody;
import creatureCreator.PosID;
import mutation.MutVec2;

public class Root implements BoneParent, Serializable {
    private static final long serialVersionUID = 1L;
    
    private ArrayList<Bone> rootChildren = new ArrayList<Bone>();
    private transient ArrayList<B2DBody> creatureBodies;
    private transient ArrayList<PosID> creatureJoints;
    private int currentBoneID = 1;
    private Vec2[] boundingBox; 
    private MutationMode mutationMode = MutationMode.M2_ALLOW_NEW_BONES;

    public Root(boolean init){ //init test
        if (init) initTest();
    }
    private Root(int currentBoneID_in, MutationMode mm) {
        currentBoneID = currentBoneID_in;
        mutationMode = mm;
    }

    public Vec2[] getBoundingBox() {
        if (boundingBox == null) {
            refreshBoundingBox();
        }
        return boundingBox;
    }

    public ArrayList<PosID> getJointPos() {
        if (isEmpty()) return null;
        if (creatureJoints == null) {
            creatureJoints = new ArrayList<PosID>();
            creatureJoints.add(new PosID(0, new Vec2()));
            for (Bone b : rootChildren) {
                b.getJointPos(creatureJoints, new Vec2());
            }
        }
		return creatureJoints;
	}

    public ArrayList<PosID> getBonePos() {
        if (isEmpty()) return null;
		ArrayList<PosID> boneList = new ArrayList<PosID>();
		for (Bone b : rootChildren) {
			b.getBonePos(boneList, new Vec2());
        }
		return boneList;
	}

    public Bone getBone(int id) {
        ArrayList<Bone> bone = new ArrayList<Bone>();
        for (Bone b : rootChildren) {
            b.getBone(id, bone);
        }
        if (bone.size() != 0) return bone.get(0);
        else return null;
    }

    public void buildCreature(World w, ArrayList<B2DBody> creatureInstances_in,
            ArrayList<RevoluteJoint> revoluteJoints_in, ArrayList<Muscle> muscles) {
        Vec2 pos = getBoundingBox()[1].negate();
        //root bone (no muscle)
        B2DBody rootBody = rootChildren.get(0).build(w, creatureInstances_in, revoluteJoints_in, muscles,
                rootChildren.get(0).getLocalRoot(), pos);
        //rest
        for (int i = 1; i < rootChildren.size(); i++) {
            rootChildren.get(i).build(w, creatureInstances_in, revoluteJoints_in, muscles,
            rootBody, rootChildren.get(0).getLocalRoot(), pos);
		}

    }

    public ArrayList<B2DBody> getCreatureBodies(Vec2 rootPos) {
        if (creatureBodies == null) {
            creatureBodies = new ArrayList<B2DBody>();
            //root bone (no muscle)
            for (Bone b : rootChildren) {
                b.getCreatureBodies(creatureBodies, rootPos);
            }
        }
        return creatureBodies;
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
        boundingBox = null;
        creatureBodies = null;
        creatureJoints = null;
    }

    public void addHead(int parentID, float size) {
        if (parentID == 0) {
            Bone head = new Bone(new Vec2(1,0), this, currentBoneID, new Muscle());
            head.setBoneType(BoneType.HEAD);
            head.setBoneArg(size);
            rootChildren.add(head);
        } else {
            for (Bone b : rootChildren) {
                b.addHead(parentID, size, currentBoneID);
            }
        }
        currentBoneID++;
        boundingBox = null;
        creatureBodies = null;
        creatureJoints = null;
    }

    public void refreshBoundingBox() {
        boundingBox = new Vec2[]{new Vec2(), new Vec2()};
        for (Bone b : rootChildren) {
            b.getBounds(boundingBox, new Vec2());
        }
    }

    public Root clone() {
        ArrayList<Bone> cloneRootChildren = new ArrayList<Bone>();
        Root clone = new Root(currentBoneID, mutationMode);
        for (Bone b : rootChildren) {
            cloneRootChildren.add(b.clone(clone));
        }
        clone.rootChildren = cloneRootChildren;
        return clone;
    }

    public Root mutate(int gen) {
        ArrayList<Bone> mutateRootChildren = new ArrayList<Bone>();
        Root mutant = new Root(currentBoneID, mutationMode);
        for (Bone b : rootChildren) {
            if (Math.random() < 0.02 && b.getID() != 1 && b.getBoneType() != BoneType.HEAD && mutationMode == MutationMode.M2_ALLOW_NEW_BONES) {
                for (Bone c : b.getChildren()) {
                    mutateRootChildren.add(c.mutate(mutant, gen, mutationMode));
                }
            } else {
                mutateRootChildren.add(b.mutate(mutant, gen, mutationMode));
            }            
        }
		if (Math.random() < 0.02 && mutationMode == MutationMode.M2_ALLOW_NEW_BONES) {
            mutateRootChildren.add(new Bone(new MutVec2(gen).getVal(), this, currentBoneID, new Muscle()));
            currentBoneID ++;
        }

        mutant.rootChildren = mutateRootChildren;
        return mutant;
    }

	public void setMutationMode(MutationMode mm) {
        mutationMode = mm;
        System.out.println(mutationMode.toString());
	}

	public MutationMode getMutationMode() {
		return mutationMode;
	}
    

    public int getIncrCurrentBoneID() {
        currentBoneID++;
        return currentBoneID - 1;
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
        addBone(0, new Vec2(0.5f,1));
        addBone(1, new Vec2(0.5f,-1));
    }


    public boolean isEmpty() {
        return rootChildren.size() == 0;
    }

    public float getCost() {
        float cost = 0;
        for (Bone b : rootChildren) {
            cost += b.getCost();
        }
        return cost;
    }
    
}