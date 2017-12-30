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
import population.Creature;

public class CreatureBuilder {
	public static void buildCreature(Creature c, World w, ArrayList<B2DBody> creatureInstances_in, ArrayList<B2DMuscle> creatureMuscles_in) {
		
		B2DBone[] bones = c.getBones();
		for (int i = 0; i < bones.length; i++) {
			buildBone(bones[i], creatureInstances_in);
		}
		
		for (B2DBody b : creatureInstances_in) {
			b.createBody(w);
		}
		
		B2DMuscle[] muscles = c.getMuscles();
		for (int i = 0; i < muscles.length; i++) {
			muscles[i].setRevJoint((RevoluteJoint) w.createJoint(buildMuscles(muscles[i], creatureInstances_in)));
			creatureMuscles_in.add(muscles[i]);
		}
		
	}
	
	private static void buildBone(B2DBone bone, ArrayList<B2DBody> creatureInstances_in) {
		B2DJoint[] joints = bone.getJoints();
		Vec2 pos = joints[0].getPos().add(joints[1].getPos()).mul(0.5f);
		float angle = B2DCamera.getRotation(joints[1].getPos().add(joints[0].getPos().negate()));
		
		B2DBody boneBody = new B2DBody("BONE" + bone.getID());
		boneBody.setUpRect(pos, new Vec2(bone.getHalfLen(), 0.1f), angle, BodyType.DYNAMIC);
		
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
		jointDef.motorSpeed = muscle.getSpeed();
		jointDef.enableLimit = true;
		jointDef.lowerAngle = -2;
		jointDef.upperAngle = 2;
		jointDef.collideConnected = false;
		
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
