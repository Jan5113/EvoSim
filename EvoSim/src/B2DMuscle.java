import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;


public class B2DMuscle {
	private B2DBody fixedAnchor1;
	private B2DBody fixedAnchor2;
	private Vec2 anchorOffset1;
	private Vec2 anchorOffset2;
	private RevoluteJoint revJoint1;
	private RevoluteJoint revJoint2;
	private RevoluteJointDef revJointDef1;
	private RevoluteJointDef revJointDef2;
	private PrismaticJoint muscle;
	private PrismaticJointDef jointDef;
	
	private final float maxLen;
	private final float minLen;
	
	private boolean active = false;
	
	private boolean isCreated = false;
	
	public B2DMuscle(float min, float max) {
		maxLen = max;
		minLen = min;
		setDefaultJointDef();
	}
	
	public B2DMuscle() { //default length
		maxLen = 3.5f;
		minLen = 2.0f;
		setDefaultJointDef();
	}
	
	
	public void setMuscleForce(float force) {
		if (isCreated) {System.err.println("B2DMuscle already created!"); return;}
		jointDef.maxMotorForce = force;
	}
	
	public void setDefaultJointDef () {
		if (isCreated) {System.err.println("B2DMuscle already created!"); return;}
		jointDef = new PrismaticJointDef();
		jointDef.lowerTranslation = 0.0f;
		jointDef.upperTranslation = 100.0f;
		jointDef.enableLimit = true;
		jointDef.maxMotorForce = 10f;
		jointDef.motorSpeed = 0f;
		jointDef.enableMotor = true;
	}
	
	public void setOffset1(Vec2 offset_in) {
		if (isCreated) {System.err.println("B2DMuscle already created!"); return;}
		anchorOffset1 = offset_in;
	}
	
	public void setOffset2(Vec2 offset_in) {
		if (isCreated) {System.err.println("B2DMuscle already created!"); return;}
		anchorOffset2 = offset_in;
	}
	
	public void setOffsets(Vec2 offset1_in, Vec2 offset2_in) {
		if (isCreated) {System.err.println("B2DMuscle already created!"); return;}
		anchorOffset1 = offset1_in;
		anchorOffset2 = offset2_in;
	}
	
	public void Create(B2DBody anchor1, B2DBody anchor2, World world) {		
		if (!anchor1.isCreated() || !anchor2.isCreated()) {System.err.println("Create bodies before creating muscle!"); return;}
		
		Vec2 muscleDir = B2DCamera.rotateVec2(new Vec2(1, 0), B2DCamera.getRotation(anchor2.getPos().sub(anchor1.getPos())));
		fixedAnchor1.setUpPoint(anchor1.getPos());
		fixedAnchor1.setAngle(B2DCamera.getRotation(muscleDir));
		fixedAnchor2.setUpPoint(anchor2.getPos());
		fixedAnchor2.setAngle(B2DCamera.getRotation(muscleDir));
		
		anchorOffset1 = B2DCamera.rotateVec2(anchorOffset1, anchor1.getAngle());
		revJointDef1.initialize(anchor1.getBody(), fixedAnchor1.getBody(), anchor1.getBody().getWorldCenter().add(anchorOffset1));	
		revJoint1 = (RevoluteJoint) world.createJoint(revJointDef1);

		anchorOffset2 = B2DCamera.rotateVec2(anchorOffset2, anchor2.getAngle());
		revJointDef2.initialize(anchor2.getBody(), fixedAnchor2.getBody(), anchor2.getBody().getWorldCenter().add(anchorOffset2));	
		revJoint2 = (RevoluteJoint) world.createJoint(revJointDef2);
		
		jointDef.initialize(fixedAnchor1.getBody(), fixedAnchor2.getBody(), fixedAnchor1.getBody().getWorldCenter(), muscleDir);
		muscle = (PrismaticJoint) world.createJoint(jointDef);
		
		isCreated = true;
	}
	
	public void enable() {
		active = true;
	}
	
	public void disable() {
		active = false;
	}
	
	public void toggle() {
		active = !active;
	}
	
	public void destroy() {
		if (!isCreated) System.err.println("B2DMuscle not created!");
		Joint.destroy(revJoint1);
		Joint.destroy(revJoint2);
		Joint.destroy(muscle);
		fixedAnchor1.destroy();
		fixedAnchor2.destroy();
	}
	
	public void update() {
		muscle.enableMotor(true);
		if (active) {
			muscle.setMotorSpeed((minLen - muscle.getJointTranslation())*10.f);
		} else {
//			joint.setMotorSpeed((maxLen- joint.getJointTranslation())*10.f);
			if ((maxLen < muscle.getJointTranslation())) {
				muscle.setMotorSpeed((maxLen- muscle.getJointTranslation())*10.f);
			} else if (minLen - 0.01f > muscle.getJointTranslation()) {
				muscle.setMotorSpeed((minLen - muscle.getJointTranslation())*10.f);
			} else {
				muscle.enableMotor(false);
			}
			
		}
	}
	
	/*
	
	world = new World(v2_gravity);
	al_cubes.add(new B2DCube(0f, 0.2f, 2.0f, 0.1f, BodyType.STATIC, world));	
	
	B2DCube tempCubeStat = new B2DCube(new Vec2(0f, 4.0f), new Vec2(0.2f, 0.2f), new Vec2(0f, 0f),  (float) 1.2f, BodyType.STATIC, world);
	B2DCube tempCubeDyn = new B2DCube(new Vec2(0f, 4.0f), new Vec2(1f, 0.2f),	new Vec2(0f, 0f), (float) 1.2f, BodyType.DYNAMIC, world);
	PrismaticJointDef jointDef = new PrismaticJointDef();
	Vec2 worldAxis = ConvertUnits.rotateVec2(new Vec2(0.0f, -1.0f), (float) 1.2f);
	jointDef.initialize(tempCubeStat.getB2D(), tempCubeDyn.getB2D(), tempCubeStat.getB2D().getWorldCenter(), worldAxis);
	jointDef.lowerTranslation = 2.0f;
	jointDef.upperTranslation = 100.0f;
	jointDef.enableLimit = true;
	jointDef.maxMotorForce = 100f;
	jointDef.motorSpeed = 0f;
	jointDef.enableMotor = true; 
	//jointDef.localAnchorA.set(0.1f,0.1f);
	//jointDef.localAnchorB.set(-0.1f,0.1f);
	joint = (PrismaticJoint) world.createJoint(jointDef);
	joint.setLimits(0f, 100.0f);
	al_cubes.add(tempCubeStat);
	al_cubes.add(tempCubeDyn);
	
	
	{

		float maxLen = 2.5f;
		float minLen = 1.0f;

		joint.enableMotor(true);
		if (countTime % 2 == 0) {
			joint.setMotorSpeed((minLen - joint.getJointTranslation())*10.f);
		} else {
//			joint.setMotorSpeed((maxLen- joint.getJointTranslation())*10.f);
			if ((maxLen < joint.getJointTranslation())) {
				joint.setMotorSpeed((maxLen- joint.getJointTranslation())*10.f);
			} else if (minLen - 0.01f > joint.getJointTranslation()) {
				joint.setMotorSpeed((minLen - joint.getJointTranslation())*10.f);
			} else {
				joint.enableMotor(false);
			}
			
		}
		System.out.println(joint.getMotorSpeed());
	}
	
	*/
}
