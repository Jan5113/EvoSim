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
import javafx.scene.paint.Color;
import mutation.MutVec2;

public class Bone implements BoneParent{
    private final int boneID;
    private final BoneParent parent;

    private String boneName = "";
    private BoneType boneType = BoneType.BONE;
    private final MutVec2 headDir;
    private final float length;
    private float boneArg;
    private Muscle parentMuscle;

    private ArrayList<Bone> children;

    public Bone(Vec2 headDir_in, BoneParent parent_in, int id_in) {
        headDir = new MutVec2(headDir_in);
        length = headDir.getVal().length();
        parent = parent_in;
        boneID = id_in;
    }

    public Vec2 getHeadDir() {
        return headDir.getVal();
    }

    public void build(World w, ArrayList<B2DBody> creatureInstances_in, 
    ArrayList<RevoluteJoint> revoluteJoints_in, B2DBody parentBody, Vec2 localParentHead) {
        B2DBody boneBody = buildBone();
        creatureInstances_in.add(boneBody);
        boneBody.createBody(w);
        revoluteJoints_in.add((RevoluteJoint) w.createJoint(buildMuscle(boneBody, parentBody, localParentHead)));
        for (Bone b : children) {
            b.build(w, creatureInstances_in, revoluteJoints_in, boneBody, localParentHead);
        }
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
		jointDef.userData = parentMuscle;
		
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

    public void setName(String name) {
        boneName = name;
    }

    public String getName() {
        return boneName;
    }

    public int getID() {
        return boneID;
    }

    public BoneType getBoneType() {
        return boneType;
    }

    public float getBoneArg() {
        return boneArg;
    }

    public float getBoneLength() {
        return length;
    }

    public ArrayList<Muscle> getMuscles(ArrayList<Muscle> muscleList) {
        for (Bone b : children) {
            b.getMuscles(muscleList);
        }
        muscleList.add(parentMuscle);
        return muscleList;
    }

    public ArrayList<Bone> getChildren() {
        return children;
    }
}