package population;

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

public class Bone implements BoneParent{
    private final int boneID;
    private final BoneParent parent;

    private String boneName = "";
    private BoneType boneType = BoneType.BONE;
    private final MutVec2 headDir;
    private final float length;
    private float boneArg = 0.2f;
    private Muscle parentMuscle;

    private ArrayList<Bone> children= new ArrayList<Bone>();

    public Bone(Vec2 headDir_in, BoneParent parent_in, int id_in, Muscle pm) {
        headDir = new MutVec2(headDir_in);
        length = headDir.getVal().length();
        parent = parent_in;
        boneID = id_in;
        parentMuscle = pm;
    }

    public Vec2 getHeadDir() {
        return headDir.getVal();
    }

	public ArrayList<PosID> getJointPos(ArrayList<PosID> jointList, Vec2 parentHeadPos) {
		jointList.add(new PosID(boneID, parentHeadPos.add(headDir.getVal())));
		for (Bone b : children) {
			b.getJointPos(jointList, parentHeadPos.add(headDir.getVal()));
		}
		return jointList;
    }

    public ArrayList<PosID> getBonePos(ArrayList<PosID> boneList, Vec2 parentHeadPos) {
		boneList.add(new PosID(boneID, parentHeadPos.add(headDir.getVal().mul(0.5f))));
		for (Bone pb : children) {
            pb.getBonePos(boneList, parentHeadPos.add(headDir.getVal()));
		}
		return boneList;
	}

    public ArrayList<PosID> getMusclePos(ArrayList<PosID> musclePosList, Vec2 parentHeadPos) {
        int[] ids = new int[children.size()];
        for (int i = 0; i < children.size(); i++) {
            ids[i] = children.get(i).getID();
        }
		musclePosList.add(new PosID(ids, parentHeadPos.add(headDir.getVal())));
		for (Bone b : children) {
            b.getMusclePos(musclePosList, parentHeadPos.add(headDir.getVal()));
		}
		return musclePosList;
	}


    public void build(World w, ArrayList<B2DBody> creatureInstances_in, 
            ArrayList<RevoluteJoint> revoluteJoints_in, B2DBody parentBody, Vec2 localParentHead) {
        B2DBody boneBody = buildBone();
        creatureInstances_in.add(boneBody);
        boneBody.createBody(w);
        if (parentMuscle != null) {
            revoluteJoints_in.add((RevoluteJoint) w.createJoint(buildMuscle(boneBody, parentBody, localParentHead)));
        } else System.err.println("Wrong function for root bone");
        for (Bone b : children) {
            b.build(w, creatureInstances_in, revoluteJoints_in, boneBody, getLocalHead());
        }
    }

    public B2DBody build(World w, ArrayList<B2DBody> creatureInstances_in, 
    ArrayList<RevoluteJoint> revoluteJoints_in, Vec2 localParentHead) {
        B2DBody rootBody = buildBone();
        creatureInstances_in.add(rootBody);
        rootBody.createBody(w);        
        for (Bone b : children) {
            b.build(w, creatureInstances_in, revoluteJoints_in, rootBody, getLocalHead());
        }
        return rootBody;
    }

    public B2DBody buildBone() {
        Vec2 pos = getBonePosition();
		B2DBody boneBody = new B2DBody(getID(), getName());
		if (getBoneType() == BoneType.BONE) {
            float angle = B2DCamera.getRotation(getHeadDir());
			boneBody.setUpRect(pos, new Vec2(0.5f * getBoneLength(), getBoneArg()), angle, BodyType.DYNAMIC);
		} else if (getBoneType() == BoneType.HEAD) {
            boneBody.setUpCircle(pos, getBoneArg(), 0, BodyType.DYNAMIC);
            boneBody.setName("head");
        }
        
        boneBody.setFill(true);
		boneBody.setColor(Color.DODGERBLUE);
		boneBody.setGroupIndex(-1);
        return boneBody;
    }

    private JointDef buildMuscle(B2DBody bone, B2DBody parentBody, Vec2 localParentHead) {
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.initialize(bone.getBody(), parentBody.getBody(), parent.getHeadPosition());
		
		jointDef.localAnchorA.set(getLocalRoot());
		jointDef.localAnchorB.set(localParentHead);
		
		jointDef.maxMotorTorque = parentMuscle.getTorque();
		jointDef.motorSpeed = 0;
		jointDef.collideConnected = false;
		jointDef.enableMotor = parentMuscle.isEnabled();
        jointDef.userData = new MuscleInfo(jointDef.localAnchorA, jointDef.localAnchorB,
        parentMuscle.getOffAngle(), parentMuscle.getOnAngle());
		
		return jointDef;
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
            Bone head = new Bone(new Vec2(0,0), this, newID, new Muscle());
            head.setBoneType(BoneType.HEAD);
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

    public ArrayList<Muscle> getMuscles(ArrayList<Muscle> muscleList) {
        if (parentMuscle != null) muscleList.add(parentMuscle);
        for (Bone b : children) {
            b.getMuscles(muscleList);
        }
        return muscleList;
    }

    public ArrayList<Bone> getChildren() {
        return children;
    }
}