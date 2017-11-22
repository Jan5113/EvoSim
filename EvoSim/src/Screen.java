import org.jbox2d.common.Vec2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Screen extends Canvas {
	private GraphicsContext gc;
	private Color col_background;
	private boolean gridEnabled = true;
	public B2DCamera camera;
	private boolean markersEnabled = false;
	private boolean infoEnabled = false;
	private String infoString = "";
	
	private boolean viewLock = true;
	
	private boolean dragging = false;
	private Vec2 dragMousePos;

	public Screen(double xRes, double yRes, float scale_in, Vec2 pos_in) {
		super(xRes, yRes);

		camera = new B2DCamera((float) scale_in, pos_in, new Vec2((float) xRes, (float) yRes));
		gc = this.getGraphicsContext2D();
		col_background = Color.color(1, 0.8, 0.8);
		clearScreen();
		
		this.addEventHandler(ScrollEvent.SCROLL, e -> scrollEvent(e));
		this.addEventHandler(MouseEvent.DRAG_DETECTED, e -> dragCameraStarted(e));
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> dragCamera(e));
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> dragCameraEnded(e));
	}
	
	//************************************************
	//*		SCREEN FUNCTIONS
	//************************************************

	public void setBackgroundCol(Color c) {
		col_background = c;
	}

	public void clearScreen() {
		gc.setFill(col_background);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (gridEnabled) drawGrid();
		if (infoEnabled) drawInfo();
		if (markersEnabled) drawMarkers();
	}
	
	public void setScreenSize(int x_in, int y_in) {
		this.setWidth(x_in);
		this.setHeight(y_in);
		camera.setScreenRes(new Vec2(x_in, y_in));
	}
	
	//************************************************
	//*		B2D DRAW FUNCTIONS
	//************************************************

	
	public void drawBody(B2DBody body) {
		if (body.getShapeType() == ShapeType.CIRCLE) {
			drawSphere(body);
		}
		else if (body.getShapeType() == ShapeType.CUBOID) {
			drawCuboid(body);
		}
		else if (body.getShapeType() == ShapeType.POINT) {
			drawCross(body);
		}
	}
	
	private void drawCuboid(B2DBody cube) {
		drawPxRect(camera.coordWorldToPixels(cube.getPos()),
				camera.scalarWorldToPixels(cube.getDim()),
				B2DCamera.radToDeg(-cube.getAngle()),
				cube.getColor(), cube.getFill());
	}

	private void drawSphere(B2DBody sphere) {
		Vec2 pos = camera.coordWorldToPixels(sphere.getPos());
		float rad = camera.scalarWorldToPixels(sphere.getDim().x);
		float angl = B2DCamera.radToDeg(-sphere.getAngle());
		drawPxLineCircle(pos.x, pos.y, rad, angl, sphere.getColor(), sphere.getFill());
	}
	
	public void drawLine(Vec2 b2d_pos1, Vec2 b2d_pos2, Color c) {
		b2d_pos1 = camera.coordWorldToPixels(b2d_pos1);
		b2d_pos2 = camera.coordWorldToPixels(b2d_pos2);
		
		gc.setStroke(c);
		gc.strokeLine(b2d_pos1.x, b2d_pos1.y, b2d_pos2.x, b2d_pos2.y);
	}
	
	public void drawCross(B2DBody cross) {
		Vec2 dim = cross.getDim();
		drawLine (cross.getPos().add(dim.negate()), cross.getPos().add(dim), cross.getColor());
		dim = new Vec2 (dim.x, -dim.y);
		drawLine (cross.getPos().add(dim.negate()), cross.getPos().add(dim), cross.getColor());
	}
	
	public void drawLocalLine(Vec2 body1_pos, Vec2 body1_loc, float body1_rot, Vec2 body2_pos, Vec2 body2_loc, float body2_rot, Color c) {
		Vec2 rotated1 = body1_pos.add(B2DCamera.rotateVec2(body1_loc, body1_rot));
		Vec2 rotated2 = body2_pos.add(B2DCamera.rotateVec2(body2_loc, body2_rot));
		drawLine(rotated1, rotated2, c);
	}
	
	//************************************************
	//*		PRIVATE PX DRAW FUNCTIONS
	//************************************************
	
	@SuppressWarnings("unused")
	private void drawPxCircle(float x, float y, float w, float h, float deg, Color c, boolean fill) {
		gc.save();
		gc.translate(x, y);
		gc.rotate(deg);
		gc.translate(-x, -y);
		drawPxCircle(x, y, w, h, c, fill);
		gc.restore();
	}
	
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

	private void drawPxCircle(float x, float y, float w, float h, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);

		if (fill)
			gc.fillOval(x - w, y - h, 2 * w, 2 * h);
		else
			gc.strokeOval(x - w, y - h, 2 * w, 2 * h);
	}

	@SuppressWarnings("unused")
	private void drawPxRect(float x, float y, float w, float h, float deg, Color c, boolean fill) {
		gc.save();
		gc.translate(x, y);
		gc.rotate(deg);
		gc.translate(-x, -y);
		drawPxRect(x, y, w, h, c, fill);
		gc.restore();
	}

	private void drawPxRect(Vec2 pos, Vec2 dim, float deg, Color c, boolean fill) {
		gc.save();
		gc.translate(pos.x, pos.y);
		gc.rotate(deg);
		gc.translate(-pos.x, -pos.y);
		drawPxRect(pos.x, pos.y, dim.x, dim.y, c, fill);
		gc.restore();
	}

	private void drawPxRect(float x, float y, float w, float h, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);

		if (fill)
			gc.fillRect(x - w, y - h, 2 * w, 2 * h);
		else
			gc.strokeRect(x - w, y - h, 2 * w, 2 * h);

	}
	
//************************************************
//*		CAMERA FUNCTIONS
//************************************************
	
	public void setScale(float scale_in) {
		camera.setZoom(scale_in);
	}

	public float getScale() {
		return camera.getZoom();
	}

	public void setPos(Vec2 pos_in) {
		camera.setPos(pos_in);
	}

	public void addPos(Vec2 pos_in) {
		camera.addPos(pos_in);
	}

	public Vec2 getPos() {
		return camera.getPos();
	}
	
//************************************************
//*		INFO SCREEN
//************************************************

	public void enableInfo() {
		infoEnabled = true;
	}
	
	public void disableInfo() {
		infoEnabled = false;
	}
	
	public void setInfoString(String text) {
		infoString = text;
	}
	
	public boolean infoEnabled() {
		return infoEnabled;
	}
	
	private void drawInfo() {
		gc.save();
		gc.setFont(new Font(15));
		gc.setFill(Color.BLACK);
		gc.fillText(infoString, 10, 25);
		gc.restore();
	}
	
//************************************************
//*		GRID
//************************************************
	
	public void enableGrid() {
		markersEnabled = true;
	}
	
	public void disableGird() {
		infoEnabled = false;
	}
	
	public boolean gridEnabled() {
		return infoEnabled;
	}
	
	private void drawGrid() {
		Vec2 startPos = new Vec2 ((float) Math.floor(camera.getPos().x), (float) Math.floor(camera.getPos().y));
		for (float i = -Math.round(0.55f *(float) getWidth() / camera.getZoom()); i <= Math.round(0.55f *(float) getWidth() / camera.getZoom()); i++) {
			if ((startPos.x + i) % 5 > -0.1f && (startPos.x + i) % 5 < 0.1f) {
				drawLine(new Vec2(startPos.x + i, 1.0f),new Vec2(startPos.x + i, 0.0f), Color.RED);
			} else {
				drawLine(new Vec2(startPos.x + i, 0.5f),new Vec2(startPos.x + i, 0.0f), Color.GRAY);
				
			}
		}
	}
	
//************************************************
//*		MARKERS
//************************************************
	
	public void enableMarkers() {
		markersEnabled = true;
	}
	
	public void disableMarkers() {
		infoEnabled = false;
	}
	
	public boolean markersEnabled() {
		return infoEnabled;
	}
	
	private void drawMarkers() {
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

//************************************************
//*		CAMERA NAVIGATION
//************************************************	
	
	public void scrollEvent(ScrollEvent se) {
		if (se.getDeltaY() > 0 ) {
			camera.zoomInPoint(1.25f, camera.coordPixelsToWorld(se.getX(), se.getY()), viewLock);
		} else {
			camera.zoomInPoint(0.8f, camera.coordPixelsToWorld(se.getX(), se.getY()), viewLock);
		}
	}
	
	public void dragCameraStarted(MouseEvent me) {
		dragging = true;
		viewLock = false;
		dragMousePos = camera.coordPixelsToWorld(me.getX(), me.getY());
	}
	
	public void dragCamera(MouseEvent me) {
		if (!dragging) return;
		addPos(dragMousePos.sub(camera.coordPixelsToWorld(me.getX(), me.getY())));
	}
	
	public void dragCameraEnded(MouseEvent me) {
		dragging = false;
	}
	
	
	public void refreshFollow(float dt, float playBackSpeed, Vec2 B2D_target, boolean running) {
		if (running && viewLock) camera.refreshFollow(dt, playBackSpeed, B2D_target);
	}
	
	public void toggleViewLock() {
		viewLock = !viewLock;
	}
	
	public void toggleViewLock(Vec2 B2D_target, boolean running) {
		toggleViewLock();
		
		if (viewLock && !running) {
			camera.quickFollow(B2D_target);
		}
	}
	
	public boolean isViewLocked() {
		return viewLock;
	}
	
	public void resetView() {
		camera.resetPosZoom();
	}

}
