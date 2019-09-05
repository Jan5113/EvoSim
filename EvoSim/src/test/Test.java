package test;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import box2d.B2DBody;
import level.Level;
import level.LevelStyle;
import population.Creature;
import population.Muscle;

public class Test {
	private World testWorld;
	private Vec2 gravity;
	
	private ArrayList<B2DBody> worldInstancesList = new ArrayList<B2DBody>();
	private ArrayList<B2DBody> creatureInstancesList = new ArrayList<B2DBody>();
	private ArrayList<RevoluteJoint> creatureRevoluteJointsList = new ArrayList<RevoluteJoint>();
	
	private Creature creature;
	private float lastFitness = 0.0f;
	private float lastDistance = 0.0f;
	private float lastRecordTime = 0f;
	
	private boolean testing = false;
	private boolean taskDone = true;
	
	private float testTimer = 0;
	
	private static float testDuration = 15.0f;
	private static float jumpTestDuration = 5f;
	private static float afterTestLength = 2f;
	private float afterTestTime = 10000.0f;
	
	private float dtToRun = 0;
	private static float dtStepSize = 0.005f;
	
	private final TestWrapper parentWrapper;
	private final Level level;
	
	private float cycleLen;
	private Muscle[] muscles;
	
	public Test (Vec2 gravity_in, TestWrapper testWrapper, Level level) {
		gravity = gravity_in;
		this.level = level;
		initWorld();		
		parentWrapper = testWrapper;
	}
	
	private void initWorld() {
		testWorld = new World(gravity);
		for (B2DBody b : level.getLevel()) {
			b.createBody(testWorld);
			worldInstancesList.add(b);
		}
	}
	
	public void setCreature (Creature creature_in) {
		initWorld();		
		creature = creature_in;
		buildCreature();
		cycleLen = creature.getCycleLength();
		muscles = creature.getMuscles();	
		lastDistance = 0f;
		lastFitness = 0f;
		lastRecordTime = 0f;
	}
	
	public Creature getCreature () {
		return creature;
	}
	
	private void buildCreature () {
		if (creature == null) {System.err.println("No Creature set! 1"); return;}
		if (testing) {
			System.err.println("No Creature set! 2");
			return;
		}
		creature.buildCreature(testWorld, creatureInstancesList, creatureRevoluteJointsList);
	}
	
	public void startTest() {
		testing = true;
		taskDone = false;
	}
	
	public void step (float dt, float speed) {
		if (!testing) {
			return;
		}
		dtToRun = dtToRun + (speed * dt);
		int steps = 0;
		
		while (dtToRun >= dtStepSize) {
			dtToRun = dtToRun - dtStepSize;
			testTimer = testTimer + dtStepSize;
			testWorld.step(dtStepSize, 10, 10);
				
			for (int i = 0; i < muscles.length; i++) {
				RevoluteJoint r = creatureRevoluteJointsList.get(i);
				float cycle = testTimer;
				cycle = cycle % cycleLen;
				cycle /= cycleLen;
				
				if (muscles[i].isActivated(cycle)) { //Joint is active
					r.setMotorSpeed((muscles[i].getOnAngle() - r.getJointAngle())*10.f);
				} else { //Joint is inactive
					r.setMotorSpeed((muscles[i].getOffAngle() - r.getJointAngle())*10.f);
				}
			}
			if (level.getLevelStyle() == LevelStyle.CLIMB) {
				float height = getAveragePosition().y;
				if (height > lastDistance) lastDistance = height;
				if (testTimer > testDuration && !taskDone) { //abort TEST
					taskDone = true;
					lastFitness = lastDistance;
					parentWrapper.taskDone(creature, lastFitness, lastDistance);					
					afterTestTime = testTimer + afterTestLength;
				}	
			} else if (level.getLevelStyle() == LevelStyle.JUMP) {
				float height = getAveragePosition().y;
				if (height > lastDistance) {
					lastDistance = height;
					lastRecordTime = testTimer;
				}
				if ((testTimer > lastRecordTime + jumpTestDuration || testTimer > testDuration) && !taskDone) { //abort TEST
					taskDone = true;
					lastFitness = lastDistance;
					parentWrapper.taskDone(creature, lastFitness, lastDistance);					
					afterTestTime = testTimer + afterTestLength;
				}	
			} else {
				B2DBody[] head = getHead();
				if ((testTimer > testDuration || headOnGround(head)) && !taskDone) { //abort TEST
					taskDone = true;
					setFitness(head);
					parentWrapper.taskDone(creature, lastFitness, lastDistance);					
					afterTestTime = testTimer + afterTestLength;
				}		
			}
							
			if (testTimer > afterTestTime) {
				parentWrapper.pauseDone(creature, lastFitness, lastDistance);
			}

			parentWrapper.stepCallback(steps);
			//System.out.println("teststep");
			steps++;
		}
	}
	public void reset() {
		for (RevoluteJoint rj : creatureRevoluteJointsList) {
			RevoluteJoint.destroy(rj);
			rj.destructor();
		}
		creatureRevoluteJointsList.clear();
		for (B2DBody b : creatureInstancesList) {
			b.destroy();
		}
		for (B2DBody b : worldInstancesList) {
			b.destroy();
		}
		worldInstancesList.clear();
		creatureInstancesList.clear();
		testTimer = 0.0f;	
		dtToRun = 0.0f;
		testing = false;
		creature = null;
		afterTestTime = 10000.0f;

		testWorld.clearForces();
		//testWorld.getJointList().destructor();
		
		initWorld();
	}
	
	
	private void setFitness(B2DBody[] head) {	
		float[] headHeight = getHeadHeight(head);	
		lastDistance = getAveragePosition().x;
		float fitness = lastDistance;
		for (float f : headHeight) {
			fitness *= f * f;
		}
		lastFitness = fitness;
	}

	private B2DBody[] getHead() {
		ArrayList<B2DBody> tempHead = new ArrayList<B2DBody>();
		for (B2DBody b: creatureInstancesList) {
			if(b.getName() == "head") {
				tempHead.add(b);
			}
		}
		if (tempHead.size()==0) return null;

		B2DBody[] head = new B2DBody[tempHead.size()];
		for (int i = 0; i < tempHead.size(); i++) {
			head[i] = tempHead.get(i);
		}
		return head;
	}
	
	private float[] getHeadHeight(B2DBody[] head) {
		if (head == null) return new float[] {1f};
		float heighs[] = new float[head.length];
		for (int i = 0; i < head.length; i++) {
			heighs[i] = head[i].getPos().y;
		}
		return heighs;
	}
	
	private boolean headOnGround(B2DBody[] head) {
		if (head == null) return false;
		for (B2DBody h : head) {
			if (h.getPos().y < h.getDim().y + 0.02f) return true;
		} 
		return false;
	}

	public Vec2 getAveragePosition() {
		Vec2 pos = new Vec2(0,0);
		for (B2DBody b: creatureInstancesList) {
			pos.addLocal(b.getPos());
		}
		pos.mulLocal(1.0f/(float) (creatureInstancesList.size()));
		return pos;
	}
	
	public ArrayList<B2DBody> getWorldInstances() {
		return worldInstancesList;
	}
	
	public ArrayList<B2DBody> getCreatureInstances () {
		return creatureInstancesList;
	}
	
	public boolean isTaskDone() {
		return taskDone;
	}
	
	public float getTestTimer() {
		return testTimer;
	}
	
	public float getTestDuration() {
		if (level.getLevelStyle() == LevelStyle.JUMP) return 0;
		return testDuration;
	}

	public ArrayList<RevoluteJoint> getRevoluteJoints() {
		return creatureRevoluteJointsList;
	}

	public boolean getLevelisVertical() {
		return level.isVertical();
	}

	public float getCurrentDistance() {
		return lastDistance;
	}
}