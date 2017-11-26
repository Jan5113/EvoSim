import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import javafx.scene.paint.Color;


/**
 * The {@code B2DBody} class contains methods and templates for an easy setup
 * process of a {@link Body} instance and scanning methods to get its
 * properties.
 * <p>
 * During setup you can define all its physical properties such as its shape,
 * density and friction as well as its location and rotation.
 * <p>
 * Once the {@code B2DBody} is created in a {@link World} the setup settings are
 * locked while the scanning methods for its current location, rotation and
 * other properties are unlocked.
 * <p>
 * For more information about the Box2D physics engine read the official
 * <a href="http://box2d.org/manual.pdf">Box2D manual</a> or consult the
 * <a href="http://www.learn-cocos2d.com/api-ref/1.0/Box2D/html/">Box2D API</a>
 *
 */
public class B2DBody {
	
	/**
	 * The {@link Body} instance which is setup by the {@link B2DBody} class. All
	 * the properties are save in this instance with the help of {@link BodyDef},
	 * {@link FixtureDef} and other sub-classes. This instance is then initialised
	 * in a {@link World} in which its physical processes are calculated.
	 */
	private Body body;
	/**
	 * The {@link BodyDef} is used to setup the {@link B2DBody} class. Properties
	 * such as rotation and location of the {@code Body} as well as the
	 * {@link BodyType} (static, dynamic,...) are specified inhere.
	 */
	private BodyDef bodyDef;
	/**
	 * The {@code fixtureDef} if used to setup the {@link B2DBody} class.
	 * Properties such as density and friction of the {@code Body} are specified
	 * inhere.
	 */
	private FixtureDef fixtureDef;

	/**
	 * The {@link PolygonShape} is used to setup a rectangle or an polygon. It defines
	 * the shape of the {@link Body} as a set of vertices.
	 */
	private PolygonShape polygonShape;
	/**
	 * The {@link CircleShape} is used to setup a circle. It defines the size of the
	 * {@link Body} with a radius given.
	 */
	private CircleShape circleShape;
	//private ChainShape chainShape;
	//private EdgeShape edgeShape;
	
	/**
	 * The {@code dimensions} {@link Vec2} contains the values for the dimensions of
	 * the {@link B2DBody} for rendering purposes. Due to the lack of a
	 * scanning-method in the {@code Box2D} physics engine those values have to be
	 * saved separately.
	 */
	private Vec2 dimensions;
	/**
	 * {@code shapeType} holds the information about what shape this
	 * {@link B2DBody} is as an enum {@link ShapeType}.
	 */
	private ShapeType shapeType;

	/**
	 * {@code drawColor} specifies the {@link Color} in which the {@link B2DBody} is
	 * rendered. The colour is applied to both the frame and the filling (if
	 * enabled).
	 */
	private Color drawColor;
	/**
	 * {@code drawFill} specifies whether the {@link B2DBody} is rendered filled or as a frame.
	 */
	private boolean drawFill;
	
	/**
	 * {@code isCreated} holds the status of the {@link B2DBody}. Its default value
	 * is {@code false}. That enables all the methods for setting up a
	 * {@code B2DBody}. Once the {@code B2DBody} is created with
	 * {@code createBody()} {@code isCreated} is set to {@code false}. This disables
	 * all the value-setting methods but therefore enables scanning methods. As by
	 * now it is not possible to switch back to the setup-mode.
	 */
	private boolean isCreated = false;
	
	/**
	 * {@code defaultBodyType} defines the default {@link BodyType} of a
	 * {@link B2DBody} instance. This value can be overwritten with
	 * {@code setBodyType()}.
	 */
	static private BodyType defaultBodyType = BodyType.DYNAMIC;
	/**
	 * Each {@link B2DBody} can be given an identification string. This can be used
	 * for later recognition of a particular {@link B2DBody} instance.
	 */
	private final String name; 

	//************************************************
	//*		SET DEFAULT VALUES
	//************************************************
	
	/**
	 * Initialises a new {@code B2DBody} and sets the default values. Takes a string
	 * for identification.
	 * 
	 * @param name_in
	 *            Gives this instance a {@code final} name.
	 */
	public B2DBody(String name_in) {
		setDefalutValues();
		name = name_in;
	}
	
	/**
	 * Sets all properties available to a reasonable default value to avoid
	 * {@code NullPointerException}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance
	 * has been created with {@code createBody()}</i>
	 */
	public void setDefalutValues() {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		setDefaultBodyDef();
		setDefaultPolygon();
		setDefaultFixture();
		setDefaultPaint();
	}
	
	/**
	 * Sets {@code bodyDef} to default values. Location, rotation, {@link BodyType}
	 * and velocity are set.
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance
	 * has been created with {@code createBody()}</i>
	 */
	public void setDefaultBodyDef() {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef = new BodyDef();
		bodyDef.type = defaultBodyType;
		bodyDef.setPosition(new Vec2(0,0));
		bodyDef.setAngle(0);
		bodyDef.setLinearVelocity(new Vec2(0,0));
	}
	
	/**
	 * Gives {@code polygonShape} a default cube shape with radius 0.1m.
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance
	 * has been created with {@code createBody()}</i>
	 */
	public void setDefaultPolygon() {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		polygonShape = new PolygonShape();
		dimensions = new Vec2(0.1f, 0.1f);
		polygonShape.setAsBox(dimensions.x, dimensions.y);
	}
	
	/**
	 * Sets {@code fixtureDef} to default values. Density, friction and restitution
	 * of the {@link B2DBody} are set.
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance
	 * has been created with {@code createBody()}</i>
	 */
	public void setDefaultFixture() {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef = new FixtureDef();
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution = 0.5f;
		//fixtureDef.filter.groupIndex = 0;
	}
	
	/**
	 * Sets the default rendering {@link Color} to blue.
	 */
	public void setDefaultPaint() {
		drawColor = Color.BLUE;
		drawFill = false;
	}
	
	//************************************************
	//*		SET BODY DEF, SHAPE & FIXTURE DEF
	//************************************************
	
	/**
	 * Takes properties such as location, rotation, {@link BodyType} and velocity in
	 * form of a {@link BodyDef} and applies those values to the {@link B2DBody}.
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance
	 * has been created with {@code createBody()}</i>
	 * 
	 * @param bodyDef_in
	 *            Specifies the {@link BodyDef} for the {@link B2DBody}
	 */
	public void setBodyDef(BodyDef bodyDef_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef = bodyDef_in;
	}
	
	
	/**
	 * Takes the shape of {@code polygonShape_in} and applies it to the
	 * {@link B2DBody}. Dimensions have to be calculated beforehand to match the
	 * {@link PolygonShape} provided with it.
	 * <p>
	 * <strong>NOTE:</strong> This type of shape has not been implemented in the
	 * render engine yet, so using this method might lead to display errors!
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance
	 * has been created with {@code createBody()}</i>
	 * 
	 * @param polygonShape_in
	 *            Sets the vertex-shape if the {@link B2DBody}
	 * @param dimensions_in
	 *            Sets the total size of the shape
	 */
	public void setPolygonShape(PolygonShape polygonShape_in, Vec2 dimensions_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		polygonShape = polygonShape_in;
		dimensions = dimensions_in;
		shapeType = ShapeType.POLYGON;
	}
	
	/**
	 * Takes a {@code dimensions} {@link Vec2} and creates a rectangle shape. The
	 * {@code x} and the {@code y} parts of {@code dimensions} specify
	 * <strong>half</strong> of the rectangles width and height (in metres).
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance
	 * has been created with {@code createBody()}</i>
	 * 
	 * @param dimensions_in
	 *            Sets the radii of the rectangle (in metres)
	 */
	public void setRectShape(Vec2 dimensions_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		polygonShape = new PolygonShape();
		polygonShape.setAsBox(dimensions_in.x, dimensions_in.y);
		dimensions = dimensions_in;
		shapeType = ShapeType.RECT;
	}	
	
	/**
	 * Takes a {@code float} and creates a circle/ball shape. The radius of this
	 * {@link B2DBody} instance is set to {@code radius_in} (in metres).
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param radius_in
	 *            Sets the radius of the circle/ball.
	 */
	public void setCircleShape(float radius_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		circleShape = new CircleShape();
		circleShape.setRadius(radius_in);
		dimensions = new Vec2 (radius_in, radius_in);
		shapeType = ShapeType.CIRCLE;
	}
	
//	public void setChainShape() {
//		
//	}	
//	
//	public void setEdgeShape() {
//		
//	}
	
	/**
	 * Takes properties such as density, friction and restitution in form of a
	 * {@link FixtureDef} and applies those values to this {@link B2DBody} instance.
	 * <p>
	 * <strong>NOTE:</strong> The {@code Shape} included in {@code fixtureDef_in}
	 * will be overwritten with local {@code Shape}! Use {@code setPolygonShape()}
	 * or {@code setCircleShape()} to avoid this problem.
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param fixtureDef_in
	 *            Specifies the {@link FixtureDef} for the {@link B2DBody}
	 */
	public void setFixture(FixtureDef fixtureDef_in) { 
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef = fixtureDef_in;
	}
	

	
	//************************************************
	//*		SET SPECIFIC VALUES
	//************************************************
	
	/**
	 * Specifies what type this {@link B2DBody} instance is. Takes a
	 * {@link BodyType} enum: <blockquote>
	 * <ul>
	 * <li>{@code DYNAMIC}: Body is affected by gravity; falls and collides</li>
	 * <li>{@code STATIC}: Body is not affected by physics but collides with other
	 * (KINEMATIC) bodies</li>
	 * <li>{@code KINETIC}: Body is not affected by physics but it's movement can be
	 * controlled and it collides with other (KINEMATIC) bodies.
	 * </ul>
	 * </blockquote>
	 * <p>
	 * Default value: {@code DYNAMIC}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param bodyType_in
	 *            Specifies the {@link BodyType} of the {@link B2DBody}
	 */
	public void setBodyType(BodyType bodyType_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.type = bodyType_in;
	}
	
	//		BODY DEF
	
	/**
	 * Takes a {@link Vec2} and sets the location of this {@link B2DBody}
	 * instance.The coordinates are given in metres.
	 * <p>
	 * Default value: {@code 0m, 0m}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param pos_in
	 *            sets the position of the {@link B2DBody}
	 */
	public void setPosition(Vec2 pos_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setPosition(pos_in);
	}

	/**
	 * Takes a {@code float} and sets the rotation of this {@link B2DBody} instance.
	 * The rotation is given in radiant counterclockwise around the centre point.
	 * <p>
	 * Default value: {@code 0 rad}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param rad_in
	 *            sets the rotation of the {@link B2DBody} in radiant
	 */
	public void setAngle(float rad_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setAngle(rad_in);
	}
	
	/**
	 * Takes a {@link Vec2} and sets the velocity of this {@link B2DBody} instance.
	 * The velocity is given in metres/second.
	 * <p>
	 * Default value: {@code 0 m/s, 0m/s}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param vel_in
	 *            sets the velocity of the {@link B2DBody}
	 * 
	 */
	public void setLinearVelocity (Vec2 vel_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setLinearVelocity(vel_in);
	}
	
	/**
	 * Takes a {@code float} and sets the angular velocity of this {@link B2DBody}
	 * instance. The velocity is given in radiant/second counterclockwise.
	 * <p>
	 * Default value: {@code 0.0 rad/s}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param vel_in
	 *            sets the angular velocity of the {@link B2DBody}
	 * 
	 */
	public void setAngularVelocity (float vel_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setAngularVelocity(vel_in);
	}
	
	/**
	 * Takes a {@code boolean} and sets the {@code bulletProperty} of this
	 * {@link B2DBody} instance to the given value. It increases number of
	 * iterations when calculating its movements and prevents those bodies from
	 * passing through one another when set to {@code true}. This is recommended for
	 * fast-moving objects.
	 * <p>
	 * Default value: {@code false}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param bullet_in
	 *            sets the {@code bulletProperty} of the {@link B2DBody}
	 */
	public void setBullet (boolean bullet_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		bodyDef.setBullet(bullet_in);
	}
	
	//		FIXTURE DEF
	
	/**
	 * Takes a {@code float} and sets the density of this {@link B2DBody} instance.
	 * A higher value makes the {@code B2DBody} heavier. The overall weight is
	 * calculated by multiplying the density with the area of the shape of the
	 * {@code B2DBody}.
	 * <p>
	 * Default value: {@code 1.0f}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param dens_in
	 *            sets the {@code density} of the {@link B2DBody}
	 */
	public void setDensity(float dens_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef.density = dens_in;
	}
	
	/**
	 * Takes a {@code float} and sets the {@code friction} of this {@link B2DBody}
	 * instance to the value given. A higher value increases the fiction between
	 * this {@code B2DBody} and its collision partner.
	 * <p>
	 * Default value: {@code 0.3f}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param fric_in
	 *            sets the {@code friction} of the {@link B2DBody}
	 */
	public void setFriction(float fric_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef.friction = fric_in;
	}

	/**
	 * Takes a {@code float} and sets the resitiution of this {@link B2DBody}
	 * instance. By giving a higher value the bounciness of the {@code B2DBody}
	 * increases. For perfectly elastic collisions let this value be
	 * {@code 0.9875f}. Values below this are partial-elastic collisions, values
	 * above this make the collision "over-elastic".
	 * <p>
	 * Default value: {@code 0.5f}
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param rest_in
	 *            value between 0.0f and 1.0f defines the bounciness of the {@link B2DBody}
	 */
	public void setRestitution(float rest_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef.restitution = rest_in;
	}
	
	/**
	 * Takes an {@code int} and assigns this {@link B2DBody} instance to a group.
	 * Only instances with the same group index are interacting with each other.
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param ind_in
	 *            assigns this {@link B2DBody} instance to a group.
	 */
	public void setGroupIndex(int ind_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		fixtureDef.filter.groupIndex = ind_in;
	}
	

	//************************************************
	//*		SHAPE TEMPLATES
	//************************************************

	/**
	 * Simple setup-method for a rectangle. Takes all the information necessary and
	 * handles all the trouble for you. The position and dimensions are handed over
	 * as individual {@code float} values, {@code x} part followed by the {@code y}
	 * part. All the values can still be modified with the set-value-methods. <br>
	 * {@code dim_x} and {@code dim_y} specify <strong>half</strong> of the
	 * rectangles width and height (in metres).
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param pos_x
	 *            sets the {@code x} position of the {@link B2DBody} rectangle
	 * @param pos_y
	 *            sets the {@code y} position of the {@link B2DBody} rectangle
	 * @param dim_x
	 *            sets half the height of the {@link B2DBody} rectangle
	 * @param dim_y
	 *            sets half the width of the {@link B2DBody} rectangle
	 * @param rad_in
	 *            sets the rotation of the {@link B2DBody}
	 * @param bodyType_in
	 *            specifies the {@link BodyType} of the {@link B2DBody}
	 */
	public void setUpRect(float pos_x, float pos_y, float dim_x, float dim_y, float rad_in, BodyType bodyType_in) {
		setUpRect(new Vec2(pos_x, pos_y), new Vec2(dim_x, dim_y), rad_in, bodyType_in);
	}
	
	/**
	 * Simple setup-method for a rectangle. Takes all the information necessary and
	 * handles all the trouble for you. The position and dimensions are handed over
	 * as {@link Vec2} vectors. All the values can still be modified with the
	 * set-value-methods. <br>
	 * The {@code x} and the {@code y} parts of {@code dim_in} specify
	 * <strong>half</strong> of the rectangles width and height (in metres).
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param pos_in
	 *            sets the centre position of the {@link B2DBody} rectangle
	 * @param dim_in
	 *            sets the radii of the {@link B2DBody} rectangle
	 * @param rad_in
	 *            sets the rotation of the {@link B2DBody} in radiant
	 * @param bodyType_in
	 *            specifies the {@link BodyType} of the {@link B2DBody}
	 */
	public void setUpRect(Vec2 pos_in, Vec2 dim_in, float rad_in, BodyType bodyType_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		setPosition(pos_in);
		setBodyType(bodyType_in);
		setAngle(rad_in);
		setRectShape(dim_in);
	}
	
	/**
	 * Simple setup-method for a circle. Takes all the information necessary and
	 * handles all the trouble for you. The position is handed over as individual
	 * {@code float} values, {@code x} part followed by the {@code y} part. All the
	 * values can still be modified with the set-value-methods.
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param pos_x
	 *            sets the {@code x} position of the {@link B2DBody} circle
	 * @param pos_y
	 *            sets the {@code y} position of the {@link B2DBody} circle
	 * @param dim
	 *            sets the radius of the {@link B2DBody} circle
	 * @param rad_in
	 *            sets the rotation of the {@link B2DBody}
	 * @param bodyType_in
	 *            specifies the {@link BodyType} of the {@link B2DBody}
	 */
	public void setUpCircle(float pos_x, float pos_y, float dim, float rad_in, BodyType bodyType_in) {
		setUpCircle(new Vec2(pos_x, pos_y), dim, rad_in, bodyType_in);
	}
	
	/**
	 * Simple setup-method for a circle. Takes all the information necessary and
	 * handles all the trouble for you. The position is handed over as a
	 * {@link Vec2} vector. All the values can still be modified with the
	 * set-value-methods.
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param pos_in
	 *            sets the centre position of the {@link B2DBody} circle
	 * @param dim_in
	 *            sets the radius of the {@link B2DBody} circle
	 * @param rad_in
	 *            sets the rotation of the {@link B2DBody}
	 * @param bodyType_in
	 *            specifies the {@link BodyType} of the {@link B2DBody}
	 */
	public void setUpCircle(Vec2 pos_in, float dim_in, float rad_in, BodyType bodyType_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		setPosition(pos_in);
		setBodyType(bodyType_in);
		setAngle(rad_in);
		setCircleShape(dim_in);
	}
	
	/**
	 * Simple setup for only a point without a body. Takes only a position
	 * {@link Vec2} vector. Default {@link BodyType} is {@code STATIC}, so it can be
	 * used as an anchor.
	 * <p>
	 * <i>This method can only be accessed before this {@code B2DBody} instance has
	 * been created with {@code createBody()}</i>
	 * 
	 * @param pos_in
	 *            sets the position of the {@link B2DBody} point
	 */
	public void setUpPoint(Vec2 pos_in) {
		if (isCreated) {System.err.println("B2DBody already created!"); return;}
		setPosition(pos_in);
		shapeType = ShapeType.POINT;
		setBodyType(BodyType.STATIC);
	}

	//************************************************
	//*		SET DRAW PORPERTIES
	//************************************************	
	
	/**
	 * Takes a {@link Color} and makes it the rendering colour for this
	 * {@link B2DBody} instance. The colour is applied to both the frame and the
	 * filling. This property is only used by the render-class.
	 * <p>
	 * Default value: {@code BLUE}
	 * 
	 * @param c_in
	 *            sets the rendering colour for the {@link B2DBody}
	 */
	public void setColor(Color c_in) {
		drawColor = c_in;
	}
	
	/**
	 * Takes a {@code boolean} which tells the renderer-class, whether to fill this
	 * {@link B2DBody} instance when rendering.
	 * <p>
	 * Default value: {@code false}
	 * 
	 * @param fill_in
	 *            sets the fill-property of the {@link B2DBody}
	 */
	public void setFill(boolean fill_in) {
		drawFill = fill_in;
	}
	
	
	
	
	//************************************************
	//*		CREATE BODY (locks set-functions)
	//************************************************	
	
	/**
	 * Initialises this {@link B2DBody} with the previously set values in the given
	 * {@link World}. Once a had been created, it is <strong>not</strong> possible
	 * to use the setup-methods make any changes. The {@code B2DBody} instance is
	 * now being moved according to the physics in the {@link World}.
	 * Scanner-methods are enabled by calling this method to get position and
	 * rotation values of the {@code B2DBody} instance.
	 * <p>
	 * <strong>Make sure you have set all values before calling this
	 * method!</strong>
	 * 
	 * @param world
	 *            reference to the {@link World} in which this {@link B2DBody}
	 *            instance should be created in.
	 */
	public void createBody(World world) {
		if (shapeType == ShapeType.POLYGON || shapeType == ShapeType.RECT) {
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
		fixtureDef.setUserData(name);
		body.createFixture(fixtureDef);
		isCreated = true;
	}
	
	
	/**
	 * Removes all references to this {@link B2DBody} instance from the {@link World} it had been created in.
	 */
	public void destroy() {
		if (!isCreated) {System.err.println("B2DBody not created!"); return;}
		body.getWorld().destroyBody(body);
	}
	

	//************************************************
	//*		GET VALUES
	//************************************************	
	
	/**
	 * Gives the status of this {@link B2DBody} instance. If {@code true} the
	 * {@code B2DBody} had already been created in a {@link World} and is (probably)
	 * being calculated, scanning-methods are enabled- When {@code false} the
	 * properties of this {@code B2DBody} instance can still be modified and it has
	 * yet to created with {@code createBody()}
	 * 
	 * @return {@link B2DBody} is initialised in a {@link World}
	 */
	public boolean isCreated() {
		return isCreated;
	}
	
	/**
	 * Gives a {@link Vec2} vector of the current position of this
	 * {@link B2DBody} instance.
	 * 
	 * @return the current position vector of the {@link B2DBody}
	 * 
	 */
	public Vec2 getPos() {
		if (!isCreated) {
			return bodyDef.getPosition();
		}
		return body.getPosition();
	}
	
	/**
	 * Gives a {@link Vec2} vector of the dimensions of this {@link B2DBody}
	 * instance.
	 * 
	 * @return the dimension vector of the {@link B2DBody}
	 */
	public Vec2 getDim() {
		if (!isCreated) {System.err.println("B2DBody not created!"); return null;}
		return dimensions;
	}
	
	/**
	 * Gives the {@code float} of the current rotation of this {@link B2DBody}
	 * instance. The rotation is given in radiant counterclockwise.
	 * 
	 * @return the current rotation of the {@link B2DBody}
	 */
	public float getAngle() {
		if (!isCreated) {System.err.println("B2DBody not created!"); return 0.0f;}
		return body.getAngle();
	}
	
	/**
	 * Gives the {@link ShapeType} enum of this {@link B2DBody} instance. It used
	 * for rendering purposes.
	 * 
	 * @return the {@link ShapeType} of this {@link B2DBody}
	 */
	public ShapeType getShapeType () {
		if (!isCreated) {System.err.println("B2DBody not created!"); return null;}
		return shapeType;
	}
	
	/**
	 * Gives the {@link Color} value of this {@link B2DBody} instance. It used
	 * for rendering purposes.
	 * 
	 * @return the {@link Color} of this {@link B2DBody}
	 */
	public Color getColor() {
		return drawColor;
	}
	
	/**
	 * Gives a reference of the {@link Body} of this {@link B2DBody} instance. Only
	 * use this method if you are aware of what you are doing.
	 * 
	 * @return
	 * a reference of the {@link Body} instance of the {@link B2DBody}
	 */
	public Body getBody() {
		return body;
	}
	
	/**
	 * Gives the {@code drawFill} of this {@link B2DBody} instance. It returns
	 * {@code true} when the {@link B2DBody} should be rendered with a filling.
	 * 
	 * @return {@code drawFill boolean} 
	 */
	public boolean getFill() {
		return drawFill;
	}
	
	/**
	 * Returns the {@link String} given during initialisation of this
	 * {@link B2DBody} instance.
	 * 
	 * @return name of the {@link B2DBody}
	 */
	public String getName() {
		return name;
	}
	
//	Add in future:
//		- extractValues() / "unCreate()": Takes current Values from body, enables editing (destroy() old body)

}
