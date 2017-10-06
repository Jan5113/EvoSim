import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
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
	
	public Test (Vec2 gravity_in) {
		testWorld = new World(gravity_in);

		B2DBody floor = new B2DBody("floor");
		floor.setUpCuboid(0.0f, -10.0f, 50.0f, 10.0f, 0.0f, BodyType.STATIC);
		floor.setFill(true);
		floor.setColor(Color.GREENYELLOW);
		floor.createBody(testWorld);
		worldInstancesList.add(floor);
		
		setCreature(new Creature(0));
	}
	
	public void setCreature (Creature creature_in) {
		creature = creature_in;
		buildCreature();
	}
	
	private  void buildCreature () {
		if (creature == null) {System.err.println("No Creature set!"); return;}
		
		B2DBody fixture = new B2DBody("fixture");
		fixture.setUpPoint(Creature.fixturePosition);
		fixture.setColor(Color.RED);
		creatureInstancesList.add(fixture);

		B2DBody bat = new B2DBody("bat");
		bat.setUpCuboid(Creature.fixturePosition.add(new Vec2(-creature.length, 0)), (new Vec2(creature.length, 0.1f)), 0.0f, BodyType.DYNAMIC);
		creatureInstancesList.add(bat);

		B2DBody ball = new B2DBody("ball");
		ball.setUpCircle(Creature.ballStartPosition, Creature.ballDim, 0.0f, BodyType.DYNAMIC);
		creatureInstancesList.add(ball);
		
		for (B2DBody b : creatureInstancesList) {
			b.createBody(testWorld);
		}
		
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.initialize(fixture.body, bat.body, Creature.fixturePosition);
		jointDef.localAnchorB.set(creature.length, 0);
		
		RevoluteJoint revJoint =  (RevoluteJoint) testWorld.createJoint(jointDef);
		
		creatureJointsList.add(revJoint);
	}
	
	public void step (float dt) {
		testWorld.step(dt, 10, 10);
	}
}
