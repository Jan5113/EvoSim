package test;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import box2d.B2DBody;
import box2d.B2DBone;
import box2d.B2DBoneDir;
import box2d.B2DCamera;
import box2d.B2DJoint;
import box2d.B2DMuscle;
import javafx.scene.paint.Color;
import population.Creature;

public class CreatureBuilder {
	public static void buildCreature(Creature c, World w, ArrayList<B2DBody> creatureInstances_in, ArrayList<RevoluteJoint> revoluteJoints_in) {
		
		B2DBone[] bones = c.getBones();
		for (int i = 0; i < bones.length; i++) {
			buildBone(bones[i], creatureInstances_in);
		}
		
		for (B2DBody b : creatureInstances_in) {
			b.createBody(w);
		}
		
		B2DMuscle[] muscles = c.getMuscles();
		for (int i = 0; i < muscles.length; i++) {
			revoluteJoints_in.add((RevoluteJoint) w.createJoint(buildMuscles(muscles[i], creatureInstances_in)));
		}
		
	}
	
	private static void buildBone(B2DBone bone, ArrayList<B2DBody> creatureInstances_in) {
		B2DJoint[] joints = bone.getJoints();
		Vec2 pos = joints[0].getPos().add(joints[1].getPos()).mul(0.5f);
		float angle = B2DCamera.getRotation(joints[1].getPos().add(joints[0].getPos().negate()));
		
		B2DBody boneBody = new B2DBody("BONE" + bone.getID());
		boneBody.setUpRect(pos, new Vec2(bone.getHalfLen(), 0.1f), angle, BodyType.DYNAMIC);
		boneBody.setFill(true);
		boneBody.setColor(Color.DODGERBLUE);
		
		boneBody.setGroupIndex(-1);
		
		creatureInstances_in.add(boneBody);
	}
	
	private static JointDef buildMuscles(B2DMuscle muscle, ArrayList<B2DBody> creatureInstances_in) {
		RevoluteJointDef jointDef = new RevoluteJointDef();
		B2DBone[] bones = muscle.getBones();
		//System.out.println(bones[0].getID() + " " + bones[1].getID());
		jointDef.initialize(getBoneByID(bones[0].getID(), creatureInstances_in).getBody(), getBoneByID(bones[1].getID(), creatureInstances_in).getBody(), muscle.getPos());
		
		if (muscle.getBoneDirs()[0] == B2DBoneDir.HEAD) {
			jointDef.localAnchorA.set(bones[0].getLocalHead());
		} else if (muscle.getBoneDirs()[0] == B2DBoneDir.END){
			jointDef.localAnchorA.set(bones[0].getLocalEnd());			
		} else {
			System.err.println("No Head/End of Bone " + bones[0].getID() + " @Muscle " + muscle.getID());
		}
		
		if (muscle.getBoneDirs()[1] == B2DBoneDir.HEAD) {
			jointDef.localAnchorB.set(bones[1].getLocalHead());
		} else if (muscle.getBoneDirs()[1] == B2DBoneDir.END){
			jointDef.localAnchorB.set(bones[1].getLocalEnd());			
		} else {
			System.err.println("No Head/End of Bone " + bones[1].getID() + " @Muscle " + muscle.getID());
		}
		
		jointDef.maxMotorTorque = muscle.getTorque();
		jointDef.motorSpeed = 0;
		jointDef.collideConnected = false;
		jointDef.enableMotor = true;
		jointDef.userData = muscle;
		
		return jointDef;
	}
	
	private static B2DBody getBoneByID(int id, ArrayList<B2DBody> creatureInstances_in) {
		for (B2DBody b : creatureInstances_in) {
			if (b.getName().equals("BONE" + id)) {
				return b;
			}
		}
		System.err.println("No matching bone found!");
		return null;
	}
	
}
