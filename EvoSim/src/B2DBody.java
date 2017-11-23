import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import javafx.scene.paint.Color;


public class B2DBody {
	private Body body;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	
	private PolygonShape polygonShape;
	private CircleShape circleShape;
	//private ChainShape chainShape;
	//private EdgeShape edgeShape;
	
	private Vec2 dimensions;
	private ShapeType shapeType;

	private Color drawColor;
	private boolean drawFill;
	
	private boolean isCreated = false;
	
	static private BodyType defaultBodyType = BodyType.DYNAMIC;
	public final String id; 

	//************************************************
	//*		SET DEFAULT VALUES
	//************************************************
	
	public B2DBody(String id_in) {
		setDefalutValues();
		id = id_in;
	}
	
	public void setDefalutValues() {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		setDefaultBodyDef();
		setDefaultPolygon();
		setDefaultFixture();
		setDefaultPaint();
	}
	
	public void setDefaultBodyDef() {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef = new BodyDef();
		bodyDef.type = defaultBodyType;
		bodyDef.setPosition(new Vec2(0,0));
		bodyDef.setAngle(0);
		bodyDef.setLinearVelocity(new Vec2(0,0));
	}
	
	public void setDefaultPolygon() {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		polygonShape = new PolygonShape();
		dimensions = new Vec2(0.1f, 0.1f);
		polygonShape.setAsBox(dimensions.x, dimensions.y);
	}
	
	public void setDefaultFixture() {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef = new FixtureDef();
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution = 0.5f;
		//fixtureDef.filter.groupIndex = 0;
	}
	
	public void setDefaultPaint() {
		drawColor = Color.BLUE;
		drawFill = false;
	}
	
	//************************************************
	//*		SET BODY DEF, SHAPE & FIXTURE DEF
	//************************************************
	
	public void setBodyDef(BodyDef bodyDef_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef = bodyDef_in;
	}
	
	public void setPolygonShape(PolygonShape polygonShape_in, Vec2 dimensions_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		polygonShape = polygonShape_in;
		dimensions = dimensions_in;
		shapeType = ShapeType.POLYGON;
	}
	
	public void setCuboidShape(Vec2 dimensions_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		polygonShape = new PolygonShape();
		polygonShape.setAsBox(dimensions_in.x, dimensions_in.y);
		dimensions = dimensions_in;
		shapeType = ShapeType.CUBOID;
	}	
	
	public void setCircleShape(float dimension_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		circleShape = new CircleShape();
		circleShape.setRadius(dimension_in);
		dimensions = new Vec2 (dimension_in, dimension_in);
		shapeType = ShapeType.CIRCLE;
	}
	
//	public void setChainShape() {
//		
//	}	
//	
//	public void setEdgeShape() {
//		
//	}
	
	public void setFixture(FixtureDef fixtureDef_in) { // WARNING! Shape included in fixtureDef_in will be overwritten with local PolygonShape
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef = fixtureDef_in;
	}
	

	
	//************************************************
	//*		SET SINGLE VALUES
	//************************************************
	
	public void setBodyType(BodyType bodyType_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.type = bodyType_in;
	}
	
	//		BODY DEF
	
	public void setPosition(Vec2 pos_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setPosition(pos_in);
	}
	
	public void setAngle(float rad_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setAngle(rad_in);
	}
	
	public void setLinearVelocity (Vec2 vel_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setLinearVelocity(vel_in);
	}
	
	public void setAngularVelocity (float vel_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setAngularVelocity(vel_in);
	}
	
	public void setBullet (boolean in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setBullet(in);
	}
	
	//		FIXTURE DEF
	
	public void setDensity(float dens_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef.density = dens_in;
	}
	
	public void setFriction(float fric_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef.friction = fric_in;
	}

	public void setRestitution(float rest_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef.restitution = rest_in;
	}
	
	public void setGroupIndex(int ind_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef.filter.groupIndex = ind_in;
	}
	

	//************************************************
	//*		SHAPE TEMPLATES
	//************************************************
	
	public void setUpCuboid(float pos_x, float pos_y, float dim_x, float dim_y, float rad_in, BodyType bodyType_in) {
		setUpCuboid(new Vec2(pos_x, pos_y), new Vec2(dim_x, dim_y), rad_in, bodyType_in);
	}
	
	public void setUpCuboid(Vec2 pos_in, Vec2 dim_in, float rad_in, BodyType bodyType_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		setPosition(pos_in);
		setBodyType(bodyType_in);
		setAngle(rad_in);
		setCuboidShape(dim_in);
	}
	
	public void setUpCircle(float pos_x, float pos_y, float dim, float rad_in, BodyType bodyType_in) {
		setUpCircle(new Vec2(pos_x, pos_y), dim, rad_in, bodyType_in);
	}
	
	public void setUpCircle(Vec2 pos_in, float dim_in, float rad_in, BodyType bodyType_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		setPosition(pos_in);
		setBodyType(bodyType_in);
		setAngle(rad_in);
		setCircleShape(dim_in);
	}
	
	public void setUpPoint(Vec2 pos_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		setPosition(pos_in);
		shapeType = ShapeType.POINT;
		setBodyType(BodyType.STATIC);
	}

	//************************************************
	//*		SET DRAW PORPERTIES
	//************************************************	
	
	public void setColor(Color c_in) {
		drawColor = c_in;
	}
	
	public void setFill(boolean fill_in) {
		drawFill = fill_in;
	}
	
	
	
	
	//************************************************
	//*		CREATE BODY (locks set-functions)
	//************************************************	
	
	public void createBody(World world) {
		if (shapeType == ShapeType.POLYGON || shapeType == ShapeType.CUBOID) {
			fixtureDef.shape = polygonShape;			
		}
		else if (shapeType == ShapeType.CIRCLE) {
			fixtureDef.shape = circleShape;			
		}
		else if (shapeType == ShapeType.POINT) {
			fixtureDef.shape = new PolygonShape();
		}
//		else if (shapeType == ShapeType.CHAIN) {
//			fixtureDef.shape = chainShape;			
//		}
//		else if (shapeType == ShapeType.EDGE) {
//			fixtureDef.shape = edgeShape;			
//		}
		body = world.createBody(bodyDef);
		fixtureDef.setUserData(id);
		body.createFixture(fixtureDef);
		isCreated = true;
	}
	
	
	public void destroy() {
		body.getWorld().destroyBody(body);
	}
	

	//************************************************
	//*		GET VALUES
	//************************************************	
	
	public boolean isCreated() {
		return isCreated;
	}
	
	public Vec2 getPos() {
		if (!isCreated) {
			return bodyDef.getPosition();
		}
		return body.getPosition();
	}
	
	public Vec2 getDim() {
		if (!isCreated) {System.err.println("B2DBody not created!"); return null;}
		return dimensions;
	}
	
	public float getAngle() {
		if (!isCreated) {System.err.println("B2DBody not created!"); return 0.0f;}
		return body.getAngle();
	}
	
	public ShapeType getShapeType () {
		if (!isCreated) {System.err.println("B2DBody not created!"); return null;}
		return shapeType;
	}
	
	public Color getColor() {
		return drawColor;
	}
	
	public Body getBody() {
		return body;
	}
	
	public boolean getFill() {
		return drawFill;
	}
	
//	Add in future:
//		- extractValues() / "unCreate()": Takes current Values from body, enables editing (destroy() old body)

}
