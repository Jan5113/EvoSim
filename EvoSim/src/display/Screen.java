package display;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import box2d.B2DBody;
import box2d.B2DBone;
import box2d.B2DBoneDir;
import box2d.B2DCamera;
import box2d.B2DMuscle;
import box2d.ShapeType;
import creatureCreator.ProtoBone;
import creatureCreator.ProtoJoint;
import creatureCreator.ProtoMuscle;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import population.Creature;
import test.Test;

/**
 * The {@link Screen} class extends the JavaFX {@link Canvas} class contains
 * many methods for rendering a {@link Test} environment. This class supports:
 * <ul>
 * <li>rendering of {@link B2DBody} instances</li>
 * <li>a grid (with distance markers if enabled)</li>
 * <li>a marker for a {@link Creature}'s fitness</li>
 * <li>a camera follow feature</li>
 * <li>drag mouse to adjust framing</li>
 * <li>scroll to zoom</li>
 * <li>information screens to display generation, ID and fitness</li>
 * <li>information screen to display the timer</li>
 * </ul>
 * 
 * <strong> NOTE: </strong> The {@code refresh} method has to be called every frame
 * if the follow feature is enabled.
 *
 */
public class Screen extends Canvas {
	/**
	 * The {@link GraphicsContext} {@code gc} is used to issue draw calls to a Canvas using a buffer. 
	 */
	private GraphicsContext gc;
	/**
	 * {@code col_background} specifies the colour of the background / "sky".
	 * <p>
	 * The default colour is {@code (1, 0.8, 0.8)}
	 */
	private Color col_background = Color.color(0.8, 0.8, 1);
	/**
	 * {@code col_background} specifies the colour of the background / "sky" when
	 * the scene (or the {@link Test}) is inactive.
	 * <p>
	 * The default colour is {@code (0.5, 0.5, 0.7)}
	 */
	private Color col_backgroundInactive = Color.color(0.5, 0.5, 0.7);
	/**
	 * {@code gridEnabled} can be set to {@code true} if a grid should be drawn.
	 */
	private boolean gridEnabled = true;
	/**
	 * {@code camera} keeps track of the current camera position and zoom and
	 * converts Box2D coordinates to pixel coordinates. 
	 * <p>
	 * Default value is {@code true}
	 */
	public B2DCamera camera;
	/**
	 * {@code markersEnabled} can be set to {@code true} to label every fifth metre. 
	 * <p>
	 * Default value is {@code false}
	 */
	private boolean markersEnabled = false;
	/**
	 * {@code infoEnabled} specifies whether to show a given information string in
	 * the top left corner.
	 * <p>
	 * {@code 1} draws normal text <br>
	 * {@code 2} draws smaller text <br>
	 * {@code 0} draws nothing
	 * <p>
	 * Default value is {@code 0}
	 */
	private int infoEnabled = 0;
	/**
	 * Holds the string written in the top left corner of the {@link Screen}.
	 */
	private String infoString = "";
	/**
	 * If {@code scrollZoomEnabled} is {@code true} the 
	 * user can adjust the adjust the framing by dragging and scrolling.
	 */
	private boolean scrollZoomEnabled = true;
	
	/**
	 * If {@code viewLock} is true, the user cannot adjust the framing anymore,
	 * zooming is still enabled
	 */
	private boolean viewLock = true;
	
	/**
	 * Holds the current state of the mouse being dragged on the {@link Screen}
	 */
	private boolean dragging = false;
	/**
	 * Holds the position {@link Vec2} of the current mouse position while dragging.
	 */
	private Vec2 dragMousePos;

	/**
	 * Initialises a new {@link Screen} object and sets the default {@link Canvas}
	 * size and the starting values for the framing. EventListerners are initialised
	 * for UI-interaction.
	 * 
	 * @param xRes
	 *            amount of horizontal pixels
	 * @param yRes
	 *            amount of vertical pixels
	 * @param scale_in
	 *            zoom of the {@link B2DCamera}
	 * @param pos_in
	 *            position of the {@link B2DCamera}
	 */
	public Screen(double xRes, double yRes, float scale_in, Vec2 pos_in) {
		super(xRes, yRes);

		camera = new B2DCamera((float) scale_in, pos_in, new Vec2((float) xRes, (float) yRes));
		gc = this.getGraphicsContext2D();
		clearScreen(true);
		
		this.addEventHandler(ScrollEvent.SCROLL, e -> scrollEvent(e));
		this.addEventHandler(MouseEvent.DRAG_DETECTED, e -> dragCameraStarted(e));
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> dragCamera(e));
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> dragCameraEnded(e));
	}
	
	//************************************************
	//*		SCREEN FUNCTIONS
	//************************************************

	/**
	 * Sets the primary background colour of the {@link Screen}.
	 * <p>
	 * The default colour is {@code (1, 0.8, 0.8)}
	 * 
	 * @param c
	 *            sky colour when active
	 */
	public void setBackgroundCol(Color c) {
		col_background = c;
	}
	
	/**
	 * Sets the secondary background colour of the {@link Screen} when the scene (or
	 * the {@link Test}) is inactive.
	 * <p>
	 * The default colour is {@code (0.5, 0.5, 0.7)}
	 * 
	 * @param c
	 *            sky colour when inactive
	 */
	public void setInactiveBackgroundCol(Color c) {
		col_backgroundInactive = c;
	}
	
	/**
	 * This method clears the whole canvas with the primary background colour or
	 * secondary background colour depending on the {@code active boolean} and draws
	 * the grid, info and/or marker if enabled.
	 *
	 * @param active
	 *            draw secondary background if {@code false}
	 */
	public void clearScreen(boolean active) {
		if (active) gc.setFill(col_background);
		else gc.setFill(col_backgroundInactive);
		
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public void drawInfoNGrind(boolean vertical) {
		if (infoEnabled != 0) drawInfo();
		if (markersEnabled) drawMarkers(vertical);
		if (gridEnabled) drawGrid(vertical);
	}
	
	/**
	 * Resizes this {@link Screen} instance
	 * 
	 * @param x_in
	 *            amount of vertical pixels 
	 * @param y_in
	 *            amount of horizontal pixels
	 */
	public void setScreenSize(int x_in, int y_in) {
		this.setWidth(x_in);
		this.setHeight(y_in);
		camera.setScreenRes(new Vec2(x_in, y_in));
	}
	
	//************************************************
	//*		B2D DRAW FUNCTIONS
	//************************************************

	
	/**
	 * This universal method takes any {@link B2DBody} and draws it to the according
	 * coordinates on the {@link Screen}.
	 * <p>
	 * {@link BodyType}, rotation, colour and fill is all considered.
	 * 
	 * @param body
	 *            is drawn on the {@link Screen}
	 */
	public void drawBody(B2DBody body) {
		if (body.getShapeType() == ShapeType.CIRCLE) {
			drawSphere(body);
		}
		else if (body.getShapeType() == ShapeType.RECT) {
			drawCuboid(body);
		}
		else if (body.getShapeType() == ShapeType.POINT) {
			drawCross(body);
		}
		else if (body.getShapeType() == ShapeType.POLYGON) {
			drawPolygon(body);
		}
	}
	
	/**
	 * This method draws a {@link B2DBody} with the {@link BodyType} {@code CUBOID} to
	 * the according coordinates on the {@link Screen}.
	 * <p>
	 * Rotation, colour and fill is all considered.
	 * 
	 * @param cube
	 *            is drawn on the {@link Screen} as a cuboid
	 */
	private void drawCuboid(B2DBody cube) {
		drawPxRect(camera.coordWorldToPixels(cube.getPos()),
				camera.scalarWorldToPixels(cube.getDim()),
				(float) Math.toDegrees(-cube.getAngle()),
				cube.getColor(), cube.getFill());
	}
	
	/**
	 * This method draws a {@link B2DBody} with the {@link BodyType} {@code CIRCLE}
	 * to the according coordinates on the {@link Screen}. A line from the centre to
	 * a fixed point on the perimeter is added to represent the angle.
	 * <p>
	 * Rotation, colour and fill is all considered.
	 *
	 * @param sphere
	 *            is drawn on the {@link Screen} as a circle
	 */
	private void drawSphere(B2DBody sphere) {
		Vec2 pos = camera.coordWorldToPixels(sphere.getPos());
		float rad = camera.scalarWorldToPixels(sphere.getDim().x);
		float angl = (float) Math.toDegrees(-sphere.getAngle());
		drawPxLineCircle(pos.x, pos.y, rad, angl, sphere.getColor(), sphere.getFill());
	}
	
	/**
	 * This method takes two B2D coordinates as {@link Vec2} vectors and draws a
	 * line in the given colour.
	 * 
	 * @param b2d_pos1
	 *            start point of line
	 * @param b2d_pos2
	 *            end point of line
	 * @param c
	 *            colour of line
	 */
	public void drawLine(Vec2 b2d_pos1, Vec2 b2d_pos2, Color c) {
		b2d_pos1 = camera.coordWorldToPixels(b2d_pos1);
		b2d_pos2 = camera.coordWorldToPixels(b2d_pos2);
		
		gc.setStroke(c);
		gc.strokeLine(b2d_pos1.x, b2d_pos1.y, b2d_pos2.x, b2d_pos2.y);
	}
	
	/**
	 * This method draws a {@link B2DBody} with the {@link BodyType} {@code POINT}
	 * to the according coordinates on the {@link Screen}. It is rendered as an "X"
	 * with the {@link B2DBody} dimensions as length.
	 *
	 * @param point
	 *            is drawn on the {@link Screen} as a cross
	 */
	public void drawCross(B2DBody point) {
		Vec2 dim = point.getDim();
		drawLine (point.getPos().add(dim.negate()), point.getPos().add(dim), point.getColor());
		dim = new Vec2 (dim.x, -dim.y);
		drawLine (point.getPos().add(dim.negate()), point.getPos().add(dim), point.getColor());
	}
	
	/**
	 * This method draws a line between two {@link B2DBone}s. The colour varies form white to 
	 * red, depending on the angle between the Bones.
	 * 
	 * @param muscle
	 * {@link B2DMuscle} to be drawn
	 */
	public void drawMuscle(RevoluteJoint muscle) {
		Vec2 pxpointA;
		Vec2 pxpointB;
		if (((B2DMuscle) muscle.getUserData()).getBoneDirs()[0] == B2DBoneDir.END) {
			pxpointA = camera.coordWorldToPixels(
					muscle.getBodyA().getPosition().add(
							B2DCamera.rotateVec2(muscle.getLocalAnchorA().add(new Vec2(0.3f, 0).negate()),
									muscle.getBodyA().getAngle())));
		} else {
			pxpointA = camera.coordWorldToPixels(
					muscle.getBodyA().getPosition().add(
							B2DCamera.rotateVec2(muscle.getLocalAnchorA().add(new Vec2(-0.3f, 0).negate()),
									muscle.getBodyA().getAngle())));
		}
		if (((B2DMuscle) muscle.getUserData()).getBoneDirs()[1] == B2DBoneDir.HEAD) {
			pxpointB = camera.coordWorldToPixels(
					muscle.getBodyB().getPosition().add(
							B2DCamera.rotateVec2(muscle.getLocalAnchorB().add(new Vec2(-0.3f, 0).negate()),
									muscle.getBodyB().getAngle())));
		} else {
			pxpointB = camera.coordWorldToPixels(
					muscle.getBodyB().getPosition().add(
							B2DCamera.rotateVec2(muscle.getLocalAnchorB().add(new Vec2(0.3f, 0).negate()),
									muscle.getBodyB().getAngle())));
		}
		float angl = muscle.getJointAngle();
		float angl0 = ((B2DMuscle) muscle.getUserData()).getOffAngle();
		float angl1 = ((B2DMuscle) muscle.getUserData()).getOnAngle();
		float col = Math.abs((angl - angl0) / (angl1 - angl0));
		if (col > 1) col = 1;
		if (col < 0) col = 0;
		gc.save();
		gc.setLineWidth(camera.scalarWorldToPixels(0.05f));
		gc.setLineCap(StrokeLineCap.ROUND);
		gc.setStroke(Color.color(1, col, col));
		gc.strokeLine(pxpointA.x, pxpointA.y, pxpointB.x, pxpointB.y);
		gc.restore();
		//drawPxCircle(pxpoint.x, pxpoint.y, camera.scalarWorldToPixels(0.05f), camera.scalarWorldToPixels(0.05f), Color.color(col,0,0), true);
	}
	
	public void drawRect(float posx, float posy, float dimx, float dimy, float rot, Color c, boolean fill) {
		drawPxRect(camera.coordWorldToPixels(new Vec2(posx, posy)),
				camera.scalarWorldToPixels(new Vec2(dimx, dimy)),
				(float) Math.toDegrees(-rot),
				c, fill);
	}

	//************************************************
	//*		PROTO DRAW FUNCTIONS
	//************************************************	

	public void drawProtoJoint(ProtoJoint j, boolean selected) {
		Color c = Color.color(1, 0, 0);
		if (selected) {
			c = Color.ROSYBROWN;
		}
		Vec2 dim = new Vec2(0.1f, 0.1f);
		drawLine (j.pos.add(dim.negate()), j.pos.add(dim), c);
		dim = new Vec2 (dim.x, -dim.y);
		drawLine (j.pos.add(dim.negate()), j.pos.add(dim), c);
//		Vec2 pos = camera.coordWorldToPixels(j.pos);
//		float rad = camera.scalarWorldToPixels(0.05f);
//		float angl = (float) Math.toDegrees(0);
//		drawPxLineCircle(pos.x, pos.y, rad, angl, c, true);
	}

	public void drawProtoBone(ProtoBone b, boolean selected) {
		Color c = Color.DODGERBLUE;
		if (selected) {
			c = Color.BLUE;
		}

		boolean fill = false;
		
		switch (b.shapetype) {
		case RECT:
			drawPxRect(camera.coordWorldToPixels(b.getPos()), camera.scalarWorldToPixels(b.getDim()),
					(float) Math.toDegrees(-b.getAngle()), c, fill);
			break;
		case CIRCLE:
			Vec2 pos = camera.coordWorldToPixels(b.getPos());
			float rad = camera.scalarWorldToPixels(b.shapeArg);
			float angl = (float) Math.toDegrees(0);
			drawPxLineCircle(pos.x, pos.y, rad, angl, c, fill);	
			break;
		default:
			break;
		}
		
		
	}

	public void drawProtoMuscle(ProtoMuscle m) {
		
	}

	public void drawPolygon(B2DBody body) {
		Vec2[] points = body.getPolygon();
		Vec2[] pxPoints = new Vec2[points.length];

		for (int i = 0; i < points.length; i++) {
			pxPoints[i] = camera.coordWorldToPixels(points[i]);
		}
		drawPxPolygon(pxPoints, body.getColor(), body.getFill());
	}

	

	
	//************************************************
	//*		PRIVATE PX DRAW FUNCTIONS
	//************************************************
	
	/**
	 * This methods takes pixel coordinates and draws a circle with a line form the
	 * centre to a fixed point to the perimeter.
	 * 
	 * @param x
	 *            x px-coordinate of the centre
	 * @param y
	 *            y px-coordinate of the centre
	 * @param r
	 *            radius in px
	 * @param deg
	 *            rotation in rad
	 * @param c
	 *            colour of the circle
	 * @param fill
	 *            circle is filled when {@code true}
	 */
	private void drawPxLineCircle(float x, float y, float r, float deg, Color c, boolean fill) {
		gc.save();
		gc.translate(x, y);
		gc.rotate(deg);
		gc.translate(-x, -y);
		drawPxCircle(x, y, r, r, c, fill);
		gc.setStroke(c);
		gc.strokeLine(x, y, x + r, y);
		gc.restore();
	}

	/**
	 * This methods takes pixel coordinates and dimensions and draws a circle to the {@link Screen}
	 * 
	 * @param x
	 *            x px-coordinate of top left corner
	 * @param y
	 *            y px-coordinate of bottom left corner
	 * @param w
	 *            horizontal width in px
	 * @param h
	 *            vertical height in px
	 * @param c
	 *            colour of the circle
	 * @param fill
	 *            circle is filled when {@code true}
	 */
	private void drawPxCircle(float x, float y, float w, float h, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);

		if (fill)
			gc.fillOval(x - w, y - h, 2 * w, 2 * h);
		else
			gc.strokeOval(x - w, y - h, 2 * w, 2 * h);
	}
	
	/**
	 * This methods takes pixel coordinates and draws a circle with a line form the
	 * centre to a fixed point to the perimeter.
	 * 
	 * @param pos
	 *            position {@link Vec2} of the centre
	 * @param dim
	 *            dimension {@link Vec2}
	 * @param deg
	 *            rotation in rad
	 * @param c
	 *            colour of the circle
	 * @param fill
	 *            circle is filled when {@code true}
	 */
	private void drawPxRect(Vec2 pos, Vec2 dim, float deg, Color c, boolean fill) {
		gc.save();
		gc.translate(pos.x, pos.y);
		gc.rotate(deg);
		gc.translate(-pos.x, -pos.y);
		drawPxRect(pos.x, pos.y, dim.x, dim.y, c, fill);
		gc.restore();
	}

	/**
	 * This methods takes pixel coordinates and dimensions and draws a rectangle to the {@link Screen}
	 * 
	 * @param x
	 *            x px-coordinate of top left corner
	 * @param y
	 *            y px-coordinate of bottom left corner
	 * @param w
	 *            horizontal width in px
	 * @param h
	 *            vertical height in px
	 * @param c
	 *            colour of the rectangle
	 * @param fill
	 *            rectangle is filled when {@code true}
	 */
	private void drawPxRect(float x, float y, float w, float h, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);

		if (fill)
			gc.fillRect(x - w, y - h, 2 * w, 2 * h);
		else
			gc.strokeRect(x - w, y - h, 2 * w, 2 * h);

	}

	private void drawPxPolygon(Vec2[] pxPoints, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);
		double xPoints[] = new double[pxPoints.length];
		double yPoints[] = new double[pxPoints.length];
		for (int i = 0; i < pxPoints.length; i++) {
			xPoints[i] = (double) pxPoints[i].x;
			yPoints[i] = (double) pxPoints[i].y;
		}
		if (fill) {
			gc.fillPolygon(xPoints, yPoints, pxPoints.length);
		} else {
			gc.strokePolygon(xPoints, yPoints, pxPoints.length);
		}

	}

//************************************************
//*		CAMERA FUNCTIONS
//************************************************
	
	/**
	 * Sets the zoom of the {@link B2DCamera}. The value given defines how many
	 * screen pixels are equivalent to one metre in the Box2D World, so a smaller
	 * value zooms out and a bigger value zooms into the scene.
	 * 
	 * @param scale_in
	 *            sets the zoom of this {@link B2DCamera} instance
	 */
	public void setScale(float scale_in) {
		camera.setZoom(scale_in);
	}

	/**
	 * Returns the current zoom value of the {@link B2DCamera}. The value
	 * represents the number of screen pixels equivalent to one metre in the Box2D
	 * World, so if zoomed out, this method returns a small value and the other way
	 * around.
	 * 
	 * @return amount of pixels equivalent to one Box2D {@link World} metre
	 */
	public float getScale() {
		return camera.getZoom();
	}

	/**
	 * Takes Box2D {@link World} coordinates as a {@link Vec2} vector and sets the
	 * centre of the {@link B2DCamera}.
	 * 
	 * @param pos_in
	 *            sets the centre position of this {@link B2DCamera} instance
	 */
	public void setPos(Vec2 pos_in) {
		camera.setPos(pos_in);
	}

	/**
	 * Takes Box2D {@link World} {@link Vec2} vector and moves the {@link B2DCamera}
	 * by this vector. It is considering the locked view directions.
	 * 
	 * @param pos_in
	 *            moves the {@link B2DCamera}
	 *            @param viewLock
	 *            locks translation directions
	 */
	public void addPos(Vec2 pos_in, boolean viewLock) {
		camera.addPosLock(pos_in, viewLock);
	}
	
	
	/**
	 * Returns the current position of the {@link B2DCamera}. The coordinates are
	 * given in Box2D coordinates as a {@link Vec2} vector.
	 * 
	 * @return the location of the {@link B2DCamera}
	 */
	public Vec2 getPos() {
		return camera.getPos();
	}
	
//************************************************
//*		INFO SCREEN
//************************************************

	/**
	 * Calling this method will enable an information string being written in the
	 * top left corner of this {@link Screen}. The font size in 15.
	 */
	public void enableInfo() {
		infoEnabled = 1;
	}
	
	/**
	 * Calling this method will disable an information string being written in the
	 * top left corner of this {@link Screen}.
	 */
	public void disableInfo() {
		infoEnabled = 0;
	}
	
	/**
	 * Calling this method will enable an information string being written in the
	 * top left corner of this {@link Screen}. The font size in 12.
	 */
	public void enableCompactInfo() {
		infoEnabled = 2;
	}
	
	/**
	 * This method sets the information string. It is displayed in the top left
	 * corner if enabled by {@code enableInfo()} oder {@code enableCompactInfo()}.
	 * 
	 * @param text
	 *            this string is written in the top left
	 */
	public void setInfoString(String text) {
		infoString = text;
	}
	
	/**
	 * Returns how the information string in the top left corner is displayed:
	 * <p>
	 * {@code 1} normal text <br>
	 * {@code 2} smaller text <br>
	 * {@code 0} disabled
	 * 
	 * @return display mode of the information string
	 */
	public int infoEnabled() {
		return infoEnabled;
	}
	
	/**
	 * This method draws the information string in the top left corner of this
	 * {@link Screen} instance.
	 */
	private void drawInfo() {
		gc.save();
		gc.setFill(Color.BLACK);
		if (infoEnabled == 2) {
			gc.setFont(new Font(12));
			gc.fillText(infoString, 8, 20);
		} else {
			gc.setFont(new Font(15));	
			gc.fillText(infoString, 10, 25);		
		}
		gc.restore();
	}
	
//************************************************
//*		GRID
//************************************************
	
	/**
	 * Calling this method will show the grid along the x-axis. Every metre is
	 * marked and every fifth metre is highlighted. A label can be added by calling
	 * {@code enableMarkers()}.
	 */
	public void enableGrid() {
		gridEnabled = true;
	}
	
	/**
	 * Calling this method will show the grid.
	 */
	public void disableGird() {
		gridEnabled = false;
	}
	
	/**
	 * Returns {@code true} if the grid is enabled.
	 * 
	 * @return  {@code gridEnabled} boolean
	 */
	public boolean gridEnabled() {
		return gridEnabled;
	}
	
	/**
	 * This method draws the grid to the {@link Screen}. Every metre is marked and
	 * every fifth metre is highlighted in red.
	 */
	private void drawGrid(boolean vertical) {
		if (vertical) {
			Vec2 startPos = new Vec2 ((float) Math.floor(camera.getPos().x), (float) Math.floor(camera.getPos().y));
			for (float i = -Math.round(0.55f *(float) getWidth() / camera.getZoom()); i <= Math.round(0.55f *(float) getWidth() / camera.getZoom()); i++) {
				if ((startPos.y + i) % 5 > -0.1f && (startPos.y + i) % 5 < 0.1f) {
					drawLine(new Vec2(2.0f, startPos.y + i),new Vec2(1.0f, startPos.y + i), Color.RED);
				} else {
					drawLine(new Vec2(1.5f, startPos.y + i),new Vec2(1.0f, startPos.y + i), Color.GRAY);
				}
			}
		} else {
			Vec2 startPos = new Vec2 ((float) Math.floor(camera.getPos().x), (float) Math.floor(camera.getPos().y));
			for (float i = -Math.round(0.55f *(float) getWidth() / camera.getZoom()); i <= Math.round(0.55f *(float) getWidth() / camera.getZoom()); i++) {
				if ((startPos.x + i) % 5 > -0.1f && (startPos.x + i) % 5 < 0.1f) {
					drawLine(new Vec2(startPos.x + i, 1.0f),new Vec2(startPos.x + i, 0.0f), Color.RED);
				} else {
					drawLine(new Vec2(startPos.x + i, 0.5f),new Vec2(startPos.x + i, 0.0f), Color.GRAY);
				}
			}
		}
		
	}
	
//************************************************
//*		MARKERS
//************************************************
	
	/**
	 * Calling this method shows a label every five metres with the x-distance from
	 * the origin.
	 */
	public void enableMarkers() {
		markersEnabled = true;
	}
	
	/**
	 * Calling this method disables the distance labels.
	 */
	public void disableMarkers() {
		markersEnabled = false;
	}
	
	/**
	 * Returns {@code true} if the distance labels are enabled.
	 * 
	 * @return  {@code markersEnabled} boolean
	 */
	public boolean markersEnabled() {
		return markersEnabled;
	}
	
	/**
	 * This method draws the distance labels to the {@link Screen}.
	 */
	private void drawMarkers(boolean vertical) {
		if (vertical) {
			Vec2 startPos = new Vec2 (0, (float) Math.floor(camera.getPos().y));
			for (float i = -Math.round(0.55f *(float) getHeight() / camera.getZoom()); i <= Math.round(0.55f *(float) getHeight() / camera.getZoom()); i++) {
				if ((startPos.y + i) % 5 > -0.1f && (startPos.y + i) % 5 < 0.1f) {
					gc.save();
					gc.setFont(new Font(30));
					gc.setFill(Color.RED);
					Vec2 pos = camera.coordWorldToPixels(startPos.add(new Vec2(2.1f, i)));
					gc.fillText((int) (startPos.y + i) + "m", pos.x, pos.y + 10);
					gc.restore();
				}
			}
		} else {
			Vec2 startPos = new Vec2 ((float) Math.floor(camera.getPos().x), 0);
			for (float i = -Math.round(0.55f *(float) getWidth() / camera.getZoom()); i <= Math.round(0.55f *(float) getWidth() / camera.getZoom()); i++) {
				if ((startPos.x + i) % 5 > -0.1f && (startPos.x + i) % 5 < 0.1f) {
					gc.save();
					gc.setFont(new Font(30));
					gc.setFill(Color.RED);
					Vec2 pos = camera.coordWorldToPixels(startPos.add(new Vec2(i, 1.1f)));
					gc.fillText((int) (startPos.x + i) + "m", pos.x - 20, pos.y);
					gc.restore();
				}
			}
		}
	}
	
//************************************************
//*		DRAW SCORE
//************************************************	
	
	/**
	 * This method draws a labelled marker at a given x-coordinate. It is used to
	 * mark a {@link Creature}'s fitness.
	 * 
	 * @param fitness
	 *            x-distance from the origin, fitness
	 */
	public void drawScore(float distance) {
		float halfWidth = 50;
		float top = 70;
		float bottom = 30;
		float tipc = 0f;
		
		Vec2 tip = camera.coordWorldToPixels(new Vec2(distance, tipc));
		
		double[] xPoints = {tip.x - halfWidth, tip.x + halfWidth, tip.x + halfWidth, tip.x, tip.x - halfWidth};
		double[] yPoints = {tip.y - bottom - top, tip.y - bottom - top, tip.y - bottom, tip.y, tip.y - bottom};
		
		gc.save();
		
		gc.setFill(Color.web("#4a9ddb"));
		gc.fillPolygon(xPoints, yPoints, 5);
		
		gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
		
		gc.setFill(Color.RED);
		gc.setFont(new Font(30));
		gc.fillText(Math.round(distance * 10)/10.0f + "m", tip.x, tip.y - bottom - (top/2) , 2 * halfWidth - 10);
		
		gc.restore();
	}
	
//************************************************
//*		DRAW TIMER
//************************************************	
		
	/**
	 * This method draws a timer in the top right corner of the {@link Screen}. It
	 * consists of two values, the time passed {@code time} and the entire duration
	 * of the timer {@code duration}. The times are rounded to one decimal place.
	 * <p>
	 * Layout: <br>
	 * Time: 1.2s / 10.0s
	 * 
	 * @param time
	 *            elapsed time
	 * @param duration
	 *            total duration
	 */
	public void drawTimer(float time, float duration) {
		gc.save();
		time = ((int) (time * 10))/10.0f;
		duration = ((int) (duration * 10))/10.0f;
		if (time >= duration) {
			gc.setFill(Color.RED);
		} else {
			gc.setFill(Color.BLACK);
		}
		gc.setFont(new Font(20));
		gc.setTextAlign(TextAlignment.RIGHT);
		gc.fillText("Time: " + time + "s / "+ duration + "s", this.getWidth() - 25, 30);
		gc.restore();
	}
	
	/**
	 * This method draws a timer with the play-back speed in the top right corner of
	 * the {@link Screen}. It consists of two values, the time passed {@code time}
	 * and the entire duration of the timer {@code duration}. The times are rounded
	 * to one decimal place. The play back speed is added below if is not equal to
	 * {@code 1.0}
	 * <p>
	 * Layout: <br>
	 * Time: 1.2s / 10.0s <br>
	 * 2.0x Speed
	 * 
	 * @param time
	 *            elapsed time
	 * @param duration
	 *            total duration
	 * @param speed
	 *            current play-back speed
	 */
	public void drawTimer(float time, float duration, float speed) {
		gc.save();
		time = ((int) (time * 10))/10.0f;
		duration = ((int) (duration * 10))/10.0f;
		if (time >= duration) {
			gc.setFill(Color.RED);
		} else {
			gc.setFill(Color.BLACK);
		}
		gc.setFont(new Font(20));
		gc.setTextAlign(TextAlignment.RIGHT);
		if (speed == 1.0f) {
			gc.fillText("Time: " + time + "s / "+ duration + "s", this.getWidth() - 25, 30);
		} else {
			gc.fillText("Time: " + time + "s / "+ duration + "s\n" + speed + "x Speed", this.getWidth() - 25, 30);
		}
		gc.restore();
	}

//************************************************
//*		CAMERA NAVIGATION
//************************************************	
	
	/**
	 * This method is called by a ScrollEvent. It handles the zooming-in or
	 * zooming-out with the scroll wheel. One raster zooms in by 20% oder out by
	 * 25%.
	 * <p>
	 * If the view is locked by {@code enableViewLock()}, it takes the target
	 * as centre, if not the mouse acts as the centre of magnification.
	 * 
	 * @param se
	 *            {@link ScrollEvent} for determining direction and location
	 */
	public void scrollEvent(ScrollEvent se) {
		if (!scrollZoomEnabled || se.getDeltaY() == 0.0) return;
		if (se.getDeltaY() > 0 ) {
			camera.zoomInPoint(1.25f, camera.coordPixelsToWorld(se.getX(), se.getY()), viewLock);
		} else {
			camera.zoomInPoint(0.8f, camera.coordPixelsToWorld(se.getX(), se.getY()), viewLock);
		}
	}
	
	/**
	 * Enables the user to manipulate the framing.
	 */
	public void enableScrollZoom() {
		scrollZoomEnabled = true;
	}
	
	/**
	 * Calling this method disables framing by the user.
	 */
	public void disableScrollZoom() {
		scrollZoomEnabled = false;
	}
	
	/**
	 * This method is called by a {@link MouseEvent} when starting a mouse drag. It
	 * initialises the drag move.
	 * 
	 * @param me
	 *            {@link MouseEvent} for location
	 */
	public void dragCameraStarted(MouseEvent me) {
		dragging = true;
		dragMousePos = camera.coordPixelsToWorld(me.getX(), me.getY());
	}
	
	/**
	 * This method is called by a {@link MouseEvent} pressed-dragging the mouse. The
	 * camera follows the movement of the mouse during the this period.
	 * 
	 * @param me
	 *            {@link MouseEvent} for location
	 */
	public void dragCamera(MouseEvent me) {
		if (!dragging) return;
		addPos(dragMousePos.sub(camera.coordPixelsToWorld(me.getX(), me.getY())), viewLock);
	}
	

	/**
	 * This method is called by a {@link MouseEvent} when ending a mouse drag. It
	 * terminates the drag move.
	 * 
	 * @param me
	 *            {@link MouseEvent}
	 */
	public void dragCameraEnded(MouseEvent me) {
		dragging = false;
	}
	
	/**
	 * Camera follow feature
	 * <p>
	 * This method has to be called very frame with a Box2D-World coordinate the
	 * {@link B2DCamera} instance has to follow. Use offset {@link Vec2} to adjust
	 * the framing. The time since the last frame dt is used to calculate the
	 * acceleration and speed of {@link B2DCamera}. To adjust for the faster
	 * movement when fast-forwarding, pass the current {@code playBackSpeed} to this
	 * method. If {@code running} is {@code false}, the {@link B2DCamera} wont
	 * follow.
	 * 
	 * @param dt
	 *            delta time
	 * @param playBackSpeed
	 *            current play-back speed
	 * @param B2D_target
	 *            target coordinate the {@link B2DCamera} should follow
	 * @param B2D_offset
	 *            offset from target
	 * @param running
	 *            {@link TestScreen} is running
	 */
	public void refreshFollow(float dt, float playBackSpeed, Vec2 B2D_target, Vec2 B2D_offset, boolean running) {
		if (B2D_target == null) return;
		if (running && viewLock) camera.refreshFollow(dt, playBackSpeed, B2D_target.add(B2D_offset));
	}
	
	/**
	 * Camera follow feature
	 * <p>
	 * Toggles the directional lock. If {@code true} the {@link B2DCamera} will
	 * follow a given target coordinate and doesn't listen to the user's input.
	 */
	public void toggleViewLock() {
		viewLock = !viewLock;
	}
	
	/**
	 * Camera follow feature
	 * <p>
	 * Enables the directional lock. The {@link B2DCamera} will follow a given
	 * target coordinate and doesn't listen to the user's input.
	 */
	public void enableViewLock() {
		viewLock = true;
	}
	

	public void disableViewLock() {
		viewLock = false;
	}
	
	
	/**
	 * Camera follow feature
	 * <p>
	 * Toggles the directional lock and specifies the target. If {@code running}
	 * is {@code true} the {@link B2DCamera} will follow the given target coordinate
	 * and doesn't listen to the user's input.
	 * 
	 * @param B2D_target
	 *            references the target coordinate
	 * @param running
	 *            {@link TestScreen} is running
	 */
	public void toggleViewLock(Vec2 B2D_target, boolean running) {
		toggleViewLock();
		
		if (viewLock && !running) {
			camera.quickFollow(B2D_target);
		}
	}
	
	/**
	 * Camera follow feature
	 * <p>
	 * Returns {@code true} if the camera follow feature is currently enabled.
	 * 
	 * @return {@code true} when the {@link B2DCamera} is locked to a target
	 */
	public boolean isViewLocked() {
		return viewLock;
	}
	
	/**
	 * Camera follow feature
	 * <p>
	 * Resets the position and zoom of the B2DCamera to the values it had been
	 * initialised with.
	 */
	public void resetView() {
		camera.resetPosZoom();
	}

}
