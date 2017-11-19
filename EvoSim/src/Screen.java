import org.jbox2d.common.Vec2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Screen extends Canvas {
	private GraphicsContext gc;
	private Color col_background;
	public boolean gridEnabled = true;
	public ConvertUnits camera;
	private boolean infoEnabled = false;
	private String infoString = "";

	public Screen(double xRes, double yRes, float scale_in, Vec2 pos_in) {
		super(xRes, yRes);

		camera = new ConvertUnits((float) scale_in, pos_in, new Vec2((float) xRes, (float) yRes));
		gc = this.getGraphicsContext2D();
		col_background = Color.color(1, 0.8, 0.8);
		clearScreen();
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
	}
	
	public void setScreenSize(int x_in, int y_in) {
		this.setWidth(x_in);
		this.setHeight(y_in);
	}
	
	private void drawGrid() {
		Vec2 startPos = new Vec2 ((float) Math.floor(camera.getPos().x), (float) Math.floor(camera.getPos().y));
		for (float i = -20.0f; i < 20.0f; i++) {
			Color c = Color.GRAY;
			if ((startPos.x + i) % 5 > -0.1f && (startPos.x + i) % 5 < 0.1f) c = Color.RED;
			drawLine(new Vec2(startPos.x + i, 1.0f),new Vec2(startPos.x + i, 0.0f), c);
		}
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
				ConvertUnits.radToDeg(-cube.getAngle()),
				cube.getColor(), cube.getFill());
	}

	private void drawSphere(B2DBody sphere) {
		Vec2 pos = camera.coordWorldToPixels(sphere.getPos());
		float rad = camera.scalarWorldToPixels(sphere.getDim().x);
		float angl = ConvertUnits.radToDeg(-sphere.getAngle());
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
		Vec2 rotated1 = body1_pos.add(ConvertUnits.rotateVec2(body1_loc, body1_rot));
		Vec2 rotated2 = body2_pos.add(ConvertUnits.rotateVec2(body2_loc, body2_rot));
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
		camera.setScale(scale_in);
	}

	public float getScale() {
		return camera.getScale();
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
	
	public boolean infoEabled() {
		return infoEnabled;
	}
	
	private void drawInfo() {
		gc.save();
		gc.setFont(new Font(15));
		gc.setFill(Color.BLACK);
		gc.fillText(infoString, 10, 40);
		gc.restore();
	}
}
