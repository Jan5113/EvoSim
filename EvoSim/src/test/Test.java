package test;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import box2d.B2DBody;
import box2d.B2DMuscle;
import box2d.ShapeType;
import javafx.scene.paint.Color;
import population.Creature;

public class Test {
	private World testWorld;
	private Vec2 gravity;
	
	private ArrayList<B2DBody> worldInstancesList = new ArrayList<B2DBody>();
	private ArrayList<B2DBody> creatureInstancesList = new ArrayList<B2DBody>();
	private ArrayList<RevoluteJoint> creatureRevoluteJointsList = new ArrayList<RevoluteJoint>();
	
	private Creature creature;
	private float lastFitness = 0.0f;
	private float lastDistance = 0.0f;
	
	private boolean testing = false;
	private boolean taskDone = true;
	
	private float testTimer = 0;
	
	private static float testDuration = 15.0f;
	private static float afterTestLength = 2.0f;
	private float afterTestTime = 10000.0f;
	
	private float dtToRun = 0;
	private static float dtStepSize = 0.005f;
	
	private final TestWrapper parentWrapper;
	
	private float cycleLen;
	private B2DMuscle[] muscles;
	
	public Test (Vec2 gravity_in, TestWrapper testWrapper) {
		gravity = gravity_in;
		
		initWorld();
		
		parentWrapper = testWrapper;
	}
	
	private void initWorld() {
		testWorld = new World(gravity);

		B2DBody floor = new B2DBody("floor");
		floor.setUpRect(0.0f, -10.0f, 100.0f, 10.0f, 0.0f, BodyType.STATIC);
		floor.setFill(true);
		floor.setColor(Color.GREENYELLOW);
		floor.createBody(testWorld);
		worldInstancesList.add(floor);
	}
	
	public void setCreature (Creature creature_in) {
		creature = creature_in;
		buildCreature();
		cycleLen = creature.getCycleLength();
		muscles = creature.getMuscles();	
	}
	
	public Creature getCreature () {
		return creature;
	}
	
	private void buildCreature () {
		if (creature == null) {System.err.println("No Creature set!"); return;}
		if (testing) {System.err.println("No Creature set!"); return;}
		CreatureBuilder.buildCreature(creature, testWorld, creatureInstancesList, creatureRevoluteJointsList);
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
			float headHeight = getHeadHeight();
			if ((testTimer > testDuration || headHeight < 0.3f) && !taskDone) { //abort TEST
				taskDone = true;
				lastDistance = getAveragePosition().x;
				lastFitness = lastDistance * headHeight * headHeight * headHeight;
				parentWrapper.taskDone(creature, lastFitness, lastDistance);					
				afterTestTime = testTimer + afterTestLength;
			}			
			
			if (testTimer > afterTestTime) {
				parentWrapper.pauseDone(creature, lastFitness, lastDistance);
			}

			parentWrapper.stepCallback(steps);
			//System.out.println("teststep");
			steps++;
		}
	}
	
	public void reset () {
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
	
	private float getHeadHeight() {
		for (B2DBody b: creatureInstancesList) {
			if(b.getShapeType() == ShapeType.CIRCLE) {
			return b.getPos().y;
			}
		}
		return 1;
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
		return testDuration;
	}

	public ArrayList<RevoluteJoint> getRevoluteJoints() {
		return creatureRevoluteJointsList;
	}
}
