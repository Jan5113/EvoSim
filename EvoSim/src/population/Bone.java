package population;

import java.io.Serializable;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import box2d.B2DBody;
import box2d.B2DCamera;
import creatureCreator.PosID;
import javafx.scene.paint.Color;
import mutation.MutVec2;

public class Bone implements BoneParent, Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int boneID;
    private final BoneParent parent;

    private String boneName = "";
    private BoneType boneType = BoneType.BONE;
    private final MutVec2 headDir;
    private final float length;
    private float boneArg = 0.1f;
    private Muscle parentMuscle;

    private ArrayList<Bone> children= new ArrayList<Bone>();

    public Bone(Vec2 headDir_in, BoneParent parent_in, int id_in, Muscle pm) {
        headDir = new MutVec2(headDir_in);
        length = headDir.getVal().length();
        parent = parent_in;
        boneID = id_in;
        parentMuscle = pm;
    }

    private Bone(MutVec2 headDir_in, BoneParent parent_in, int id_in,
    String name_in, float boneArg_in, Muscle parentMuscle_in, BoneType boneType_in) {
        headDir = headDir_in;
        length = headDir.getVal().length();
        parent = parent_in;
        boneID = id_in;
        parentMuscle = parentMuscle_in;
        boneArg = boneArg_in;
        boneName = name_in;
        boneType = boneType_in;
    }

    public Vec2 getHeadDir() {
        return headDir.getVal();
    }

    public MutVec2 getHeadDirMutVec() {
        return headDir;
    }

	public ArrayList<PosID> getJointPos(ArrayList<PosID> jointList, Vec2 parentHeadPos) {
        if (boneType == BoneType.HEAD) return jointList;
		jointList.add(new PosID(boneID, parentHeadPos.add(headDir.getVal())));
		for (Bone b : children) {
			b.getJointPos(jointList, parentHeadPos.add(headDir.getVal()));
		}
		return jointList;
    }

    public ArrayList<PosID> getBonePos(ArrayList<PosID> boneList, Vec2 parentHeadPos) {
        if (boneID == 1) boneList.add(new PosID(boneID, parentHeadPos.add(headDir.getVal().mul(0.5f))));
        else if (boneType == BoneType.HEAD) boneList.add(new PosID(boneID, parentHeadPos));
        else boneList.add(new PosID(boneID, parentHeadPos.add(headDir.getVal().mul(0.5f)), parentHeadPos));
		
		for (Bone pb : children) {
            pb.getBonePos(boneList, parentHeadPos.add(headDir.getVal()));
		}
		return boneList;
	}

    public void getBone(int id, ArrayList<Bone> bone) {
        if (boneID == id) {
            bone.add(this);
            return;
        }
        for (Bone b : children) {
            b.getBone(id, bone);
        }
    }

    public void build(World w, ArrayList<B2DBody> creatureInstances_in, ArrayList<RevoluteJoint> revoluteJoints_in,
    B2DBody parentBody, Vec2 localParentHead, Vec2 parentHeadPos) {
        B2DBody boneBody = buildBone(parentHeadPos);
        creatureInstances_in.add(boneBody);
        boneBody.createBody(w);
        if (parentMuscle != null) {
            revoluteJoints_in.add((RevoluteJoint) w.createJoint(buildMuscle(boneBody, parentBody, localParentHead, parentHeadPos)));
        } else System.err.println("Wrong function for root bone");
        for (Bone b : children) {
            b.build(w, creatureInstances_in, revoluteJoints_in, boneBody, getLocalHead(), parentHeadPos.add(headDir.getVal()));
        }
    }

    public B2DBody build(World w, ArrayList<B2DBody> creatureInstances_in, 
    ArrayList<RevoluteJoint> revoluteJoints_in, Vec2 localParentHead, Vec2 parentHeadPos) {
        B2DBody rootBody = buildBone(parentHeadPos);
        creatureInstances_in.add(rootBody);
        rootBody.createBody(w);        
        for (Bone b : children) {
            b.build(w, creatureInstances_in, revoluteJoints_in, rootBody, getLocalHead(), parentHeadPos.add(headDir.getVal()));
        }
        return rootBody;
    }

    private B2DBody buildBone(Vec2 parentHeadPos) {
        Vec2 pos = parentHeadPos.add(headDir.getVal().mul(0.5f));
		B2DBody boneBody = new B2DBody(getID(), getName());
		if (getBoneType() == BoneType.BONE) {
            float angle = B2DCamera.getRotation(getHeadDir());
			boneBody.setUpRect(pos, new Vec2(0.5f * getBoneLength(), boneArg), angle, BodyType.DYNAMIC);
		} else if (getBoneType() == BoneType.HEAD) {
            boneBody.setUpCircle(parentHeadPos, boneArg, 0, BodyType.DYNAMIC);
            boneBody.setName("head");
        }
        
        boneBody.setFill(true);
		boneBody.setColor(Color.DODGERBLUE);
		boneBody.setGroupIndex(-1);
        return boneBody;
    }

    private JointDef buildMuscle(B2DBody bone, B2DBody parentBody, Vec2 localParentHead, Vec2 parentHeadPos) {
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.initialize(bone.getBody(), parentBody.getBody(), parentHeadPos);
        
        if (boneType == BoneType.HEAD) jointDef.localAnchorA.set(new Vec2());
        else jointDef.localAnchorA.set(getLocalRoot());
		jointDef.localAnchorB.set(localParentHead);
		
		jointDef.maxMotorTorque = parentMuscle.getTorque();
		jointDef.motorSpeed = 0;
		jointDef.collideConnected = false;
		jointDef.enableMotor = parentMuscle.isEnabled();
        jointDef.userData = new MuscleInfo(jointDef.localAnchorA, jointDef.localAnchorB,
        parentMuscle.getOffAngle(), parentMuscle.getOnAngle());
		
		return jointDef;
    }

    public void getCreatureBodies(ArrayList<B2DBody> bodies, Vec2 parentHeadPos) {
        bodies.add(buildBone(parentHeadPos));
        for (Bone b : children) {
            b.getCreatureBodies(bodies, parentHeadPos.add(headDir.getVal()));
        }
    }
    
    public Bone clone(BoneParent newbp) {
        Bone clone;
        if (parentMuscle == null) {// root bone 
            clone = new Bone(headDir.clone(), newbp, boneID, boneName, boneArg, null, boneType);
        } else {
            clone = new Bone(headDir.clone(), newbp, boneID, boneName, boneArg, parentMuscle.clone(), boneType);
        }
        ArrayList<Bone> newChildren = new ArrayList<Bone>();
        for (Bone b : children) {
            newChildren.add(b.clone(clone));
        }
        clone.setChildren(newChildren);
        return clone;
    }
    
    public Bone mutate(BoneParent newbp, int gen, MutationMode mm) {
        if (boneType == BoneType.HEAD) return clone(newbp);
        Bone mutant;
        if (parentMuscle == null) {
            if (mm == MutationMode.M0_ONLY_MUSCLE) {
                mutant = new Bone(headDir.clone(), newbp, boneID, boneName, boneArg, null, boneType);                
            } else {
                mutant = new Bone(headDir.mutate(gen), newbp, boneID, boneName, boneArg, null, boneType);                
            }
        } else {
            if (mm == MutationMode.M0_ONLY_MUSCLE) {
                mutant = new Bone(headDir.clone(), newbp, boneID, boneName, boneArg, parentMuscle.mutate(gen), boneType);                
            } else {
                mutant = new Bone(headDir.mutate(gen), newbp, boneID, boneName, boneArg, parentMuscle.mutate(gen), boneType);                
            }

        }
        ArrayList<Bone> newChildren = new ArrayList<Bone>();
        for (Bone b : children) {
            if (Math.random() < 0.02 && b.getBoneType() != BoneType.HEAD) {
                for (Bone c : b.getChildren()) {
                    newChildren.add(c.mutate(mutant, gen, mm));
                }
            } else {
                newChildren.add(b.mutate(mutant, gen, mm));
            }
        }
		if (mm == MutationMode.M2_ALLOW_NEW_BONES && Math.random() < 0.02 && getBoneType() != BoneType.HEAD) {
            newChildren.add(new Bone(new MutVec2(gen).getVal(), this, parent.getIncrCurrentBoneID(), new Muscle()));
        }

        mutant.setChildren(newChildren);
        return mutant;
    }

    public int getIncrCurrentBoneID() {
        return parent.getIncrCurrentBoneID();
    }

    public Vec2 getLocalHead() {
        return new Vec2(0.5f * length, 0);
    }

    public Vec2 getLocalRoot() {
        return new Vec2(-0.5f * length, 0);
    }

    public Vec2 getHeadPosition() {
        return parent.getHeadPosition().add(headDir.getVal());
    }

    public Vec2 getBonePosition() {
        return parent.getHeadPosition().add(headDir.getVal().mul(0.5f));
    }

    public void addBone(int parentID, Vec2 dir, int newID) {
        if (parentID == getID()) {
            children.add(new Bone(dir, this, newID, new Muscle()));
        } else {
            for (Bone b : children) {
                b.addBone(parentID, dir, newID);
            }
        }
    }

    public void addHead(int parentID, float size, int newID) {
        if (parentID == getID()) {
            Bone head = new Bone(new Vec2(1,0), this, newID, new Muscle());
            head.setBoneType(BoneType.HEAD);
            head.setBoneArg(size);
            children.add(head);
        } else {
            for (Bone b : children) {
                b.addHead(parentID, size, newID);
            }
        }
    }

    public void setName(String name) {
        boneName = name;
    }

    public String getName() {
        return boneName;
    }

    public int getID() {
        return boneID;
    }

    public void setBoneType(BoneType bt) {
        boneType = bt;
    }

    public BoneType getBoneType() {
        return boneType;
    }

    public void setBoneArg(float arg) {
        boneArg = arg;
    }

    public float getBoneArg() {
        return boneArg;
    }

    public float getBoneLength() {
        return length;
    }

    public void getMuscles(ArrayList<Muscle> muscleList) {
        if (parentMuscle != null) muscleList.add(parentMuscle);
        for (Bone b : children) {
            b.getMuscles(muscleList);
        }
    }

    public Muscle getMuscle() {
        return parentMuscle;
    }

    public void getBounds(Vec2[] boundingBox, Vec2 parentHeadPos) {
        Vec2 pos = parentHeadPos.add(headDir.getVal());
        if (boundingBox[0].x > pos.x) boundingBox[0].x = pos.x;
        if (boundingBox[1].x < pos.x) boundingBox[1].x = pos.x;
        if (boundingBox[0].y < pos.y) boundingBox[0].y = pos.y;
        if (boundingBox[1].y > pos.y) boundingBox[1].y = pos.y;
        for (Bone b : children) {
            b.getBounds(boundingBox, pos);
        }
    }

    public void newInitMuscle() {
        if (parentMuscle != null) parentMuscle.newInit();
        for (Bone b : children) {
            b.newInitMuscle();
        }

    }

    public ArrayList<Bone> getChildren() {
        return children;
    }

    private void setChildren(ArrayList<Bone> newChildren) {
        children = newChildren;
    }
}