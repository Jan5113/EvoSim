package test;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import box2d.B2DBody;
import box2d.B2DMuscle;
import javafx.scene.paint.Color;
import population.Creature;

public class Test {
	private World testWorld;
	
	private ArrayList<B2DBody> worldInstancesList = new ArrayList<B2DBody>();
	private ArrayList<B2DBody> creatureInstancesList = new ArrayList<B2DBody>();
	public ArrayList<B2DMuscle> creatureMusclesList = new ArrayList<B2DMuscle>();
	
	private Creature creature;
	private float lastFitness = 0.0f;
	
	private boolean testing = false;
	private boolean taskDone = true;
	
	private float testTimer = 0;
	
	private static float afterTestLength = 2.0f;
	private float afterTestTime = 10000.0f;
	private final boolean fastCalculation;
	
	private float dtToRun = 0;
	private static float dtStepSize = 0.005f;
	
	private final TestWrapper parentWrapper;
	
	public Test (Vec2 gravity_in, TestWrapper testWrapper, boolean fastCalculation) {
		testWorld = new World(gravity_in);

		B2DBody floor = new B2DBody("floor");
		floor.setUpRect(0.0f, -10.0f, 100.0f, 10.0f, 0.0f, BodyType.STATIC);
		floor.setFill(true);
		floor.setColor(Color.GREENYELLOW);
		floor.createBody(testWorld);
		worldInstancesList.add(floor);
		
		parentWrapper = testWrapper;
		this.fastCalculation = fastCalculation;
	}
	
	public void setCreature (Creature creature_in) {
		creature = creature_in;
		buildCreature();
	}
	
	public Creature getCreature () {
		return creature;
	}
	
	private  void buildCreature () {
		if (creature == null) {System.err.println("No Creature set!"); return;}
		if (testing) {System.err.println("No Creature set!"); return;}
		
		CreatureBuilder.buildCreature(creature, testWorld, creatureInstancesList, creatureMusclesList);
		
//		B2DBody fixture = new B2DBody("fixture");
//		fixture.setUpPoint(creature.fixturePos.getVal());
//		fixture.setColor(Color.RED);
//		creatureInstancesList.add(fixture);
//
//		B2DBody bat = new B2DBody("bat");
//		bat.setUpRect(creature.fixturePos.getVal().add(new Vec2(-creature.length.getSqVal(), 0)), (new Vec2(creature.length.getSqVal(), 0.1f)), 0.0f, BodyType.DYNAMIC);
//		
//		creatureInstancesList.add(bat);
//
//		B2DBody ball = new B2DBody("ball");
//		//ball.setUpCircle(Creature.ballStartPosition, Creature.ballDim, 0.0f, BodyType.DYNAMIC);
//		ball.setUpCircle(new Vec2(0, 10), 0.1f, 0.0f, BodyType.DYNAMIC);
//		ball.setBullet(true);
//		ball.createBody(testWorld);
//		creatureInstancesList.add(ball);
//		
//		for (B2DBody b : creatureInstancesList) {
//			b.createBody(testWorld);
//		}
//		
//		RevoluteJointDef jointDef = new RevoluteJointDef();
//		jointDef.initialize(fixture.getBody(), bat.getBody(), creature.fixturePos.getVal());
//		jointDef.localAnchorB.set(creature.length.getSqVal(), 0);
//		jointDef.enableLimit = true;
//		jointDef.lowerAngle = 0.0f;		
//		creatureJointsList.add((RevoluteJoint) testWorld.createJoint(jointDef));
	}
	
	public void startTest() {
		testing = true;
		taskDone = false;
		
		if (fastCalculation) {
			fastSteps();
		}
	}
	
	public void fastSteps() {
		while (!taskDone) {
			step(dtStepSize, 1);
		}
	}
	
	public void step (float dt, float speed) {
		if (!testing) return;
		dtToRun += (speed * dt);
		
		while (dtToRun >= dtStepSize) {
			dtToRun -= dtStepSize;
			testTimer += dtStepSize;
			testWorld.step(dtStepSize, 10, 10);
			
			
			
			if (testTimer > 20.0f && !taskDone) { //abort TEST
				taskDone = true;
				//testing = false;
				//dtToRun = 0.0f;
				lastFitness = getAveragePosition().x;
				parentWrapper.taskDone(creature, lastFitness);
				afterTestTime = testTimer + afterTestLength;
			}			
			
			if (testTimer > afterTestTime) {
				parentWrapper.pauseDone(creature, lastFitness);
			}
			
		}
	}
	
	public void reset () {
		for (B2DBody b : creatureInstancesList) {
			b.destroy();
		}
		creatureInstancesList.clear();
		for (B2DMuscle m : creatureMusclesList) {
			m.destroyRevJoint();
		}
		creatureMusclesList.clear();
		testTimer = 0.0f;	
		dtToRun = 0.0f;
		testing = false;
		creature = null;
		afterTestTime = 100000.0f;
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
}
