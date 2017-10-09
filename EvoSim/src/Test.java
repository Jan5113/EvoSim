import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import javafx.scene.paint.Color;

public class Test {
	private World testWorld;
	public ArrayList<B2DBody> worldInstancesList = new ArrayList<B2DBody>();
	public ArrayList<B2DBody> creatureInstancesList = new ArrayList<B2DBody>();
	public ArrayList<RevoluteJoint> creatureJointsList = new ArrayList<RevoluteJoint>();
	private Creature creature;
	private float testTimer = 0;
	private boolean testing = false;
	
	private float dtToRun = 0;
	private static float dtStepSize = 0.005f;
	
	private Population pop;
	
	public Test (Vec2 gravity_in) {
		testWorld = new World(gravity_in);

		B2DBody floor = new B2DBody("floor");
		floor.setUpCuboid(0.0f, -10.0f, 100.0f, 10.0f, 0.0f, BodyType.STATIC);
		floor.setFill(true);
		floor.setColor(Color.GREENYELLOW);
		floor.createBody(testWorld);
		worldInstancesList.add(floor);
		
		pop = new Population(100);
		pop.CreateRandPopulation();
		
		setCreature(pop.getNext());
	}
	
	public void setCreature (Creature creature_in) {
		creature = creature_in;
		buildCreature();
		
		startTest();
	}
	
	private  void buildCreature () {
		if (creature == null) {System.err.println("No Creature set!"); return;}
		if (testing) {System.err.println("No Creature set!"); return;}
		
		B2DBody fixture = new B2DBody("fixture");
		fixture.setUpPoint(creature.fixturePos);
		fixture.setColor(Color.RED);
		creatureInstancesList.add(fixture);

		B2DBody bat = new B2DBody("bat");
		bat.setUpCuboid(creature.fixturePos.add(new Vec2(-creature.length, 0)), (new Vec2(creature.length, 0.1f)), 0.0f, BodyType.DYNAMIC);
		
		creatureInstancesList.add(bat);

		B2DBody ball = new B2DBody("ball");
		ball.setUpCircle(Creature.ballStartPosition, Creature.ballDim, 0.0f, BodyType.DYNAMIC);
		ball.setBullet(true);
		creatureInstancesList.add(ball);
		
		for (B2DBody b : creatureInstancesList) {
			b.createBody(testWorld);
		}
		
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.initialize(fixture.body, bat.body, creature.fixturePos);
		jointDef.localAnchorB.set(creature.length, 0);
		jointDef.enableLimit = true;
		jointDef.lowerAngle = 0.0f;		
		creatureJointsList.add((RevoluteJoint) testWorld.createJoint(jointDef));
	}
	
	public void startTest() {
		testing = true;
	}
	
	public void step (float dt, float speed) {
		if (!testing) return;
		dtToRun += (speed * dt); 
		
		while (dtToRun >= dtStepSize) {
			dtToRun -= dtStepSize;
			testTimer += dtStepSize;
			testWorld.step(dtStepSize, 10, 10);
			
			if (testTimer > creature.time) {
				creatureJointsList.get(0).enableLimit(false);
			}
			
			for (ContactEdge ce = creatureInstancesList.get(2).body.getContactList(); ce != null; ce = ce.next) {
				Contact c = ce.contact;
				
				if (c.isTouching()) {
					if (c.m_fixtureA.m_userData == worldInstancesList.get(0).body.getFixtureList().m_userData) {
						testing = false;
						creature.setFitness(creatureInstancesList.get(2).getPos().x);
						System.out.println("ID: "+ creature.id + " | Fitness:" + creature.getFitness());
						reset();
					}
				}
			}
		}
	}
	
	public void reset () {
		for (int i = 0; i < creatureInstancesList.size(); i++) {
			creatureInstancesList.get(i).destroy();
		}
		Joint.destroy(creatureJointsList.get(0));
		creatureInstancesList.clear();
		creatureJointsList.clear();
		testTimer = 0.0f;
		
		
		Creature tempCret = pop.getNext();
		if (tempCret == null) {
			pop.nextGen();
			pop.sortPopulation();
			pop.killPercentage(0.8f);
			pop.mutatePop(0.8f);
			setCreature(pop.getNext());
		} else {
			setCreature(tempCret);
		}
		
	}
}
