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
	
	public Test (Vec2 gravity_in) {
		testWorld = new World(gravity_in);

		B2DBody floor = new B2DBody("floor");
		floor.setUpCuboid(0.0f, -10.0f, 50.0f, 10.0f, 0.0f, BodyType.STATIC);
		floor.setFill(true);
		floor.setColor(Color.GREENYELLOW);
		floor.createBody(testWorld);
		worldInstancesList.add(floor);
		
		setCreature(new Creature(0));
		buildCreature();
	}
	
	public void setCreature (Creature creature_in) {
		creature = creature_in;
	}
	
	public void buildCreature () {
		if (creature == null) {System.err.println("No Creature set!"); return;}
		
		B2DBody fixture = new B2DBody("fixture");
		fixture.setUpPoint(Creature.fixturePosition);
		fixture.setColor(Color.RED);
		creatureInstancesList.add(fixture);

		B2DBody bat = new B2DBody("bat");
		bat.setUpCuboid(Creature.fixturePosition.add(new Vec2(0, -creature.length)), (new Vec2(0.1f, creature.length)), 0.0f, BodyType.DYNAMIC);
		creatureInstancesList.add(bat);

		B2DBody ball = new B2DBody("ball");
		ball.setUpCircle(Creature.ballStartPosition, Creature.ballDim, 0.0f, BodyType.DYNAMIC);
		creatureInstancesList.add(ball);
		
		RevoluteJointDef revJointDef = new RevoluteJointDef();
		revJointDef.initialize(fixture.body, bat.body, creature.fixturePosition);
		revJointDef.localAnchorB.set(0, creature.length);
		
//		RevoluteJoint revJoint = new RevoluteJoint(); 
		
		for (B2DBody b : creatureInstancesList) {
			b.createBody(testWorld);
		}
	}
	
	public void step (float dt) {
		testWorld.step(dt, 10, 10);
	}
}
