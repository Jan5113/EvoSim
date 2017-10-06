import org.jbox2d.common.Vec2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Screen extends Canvas {
	private GraphicsContext gc;
	private Color col_background;
	public ConvertUnits cu;

	public Screen(double arg0, double arg1, double scale_in, Vec2 pos_in) {
		super(arg0, arg1);

		cu = new ConvertUnits((float) scale_in, pos_in, new Vec2((float) arg0, (float) arg1));
		gc = this.getGraphicsContext2D();
		col_background = Color.color(1, 0.8, 0.8);
		clearScreen();
	}

	public void setBackgroundCol(Color c) {
		col_background = c;
	}

	@SuppressWarnings("unused")
	private void drawPxCircle(double x, double y, double w, double h, double deg, Color c, boolean fill) {
		gc.save();
		gc.translate(x, y);
		gc.rotate(deg);
		gc.translate(-x, -y);
		drawPxCircle(x, y, w, h, c, fill);
		gc.restore();
	}
	
	private void drawPxLineCircle(double x, double y, double r, double deg, Color c, boolean fill) {
		gc.save();
		gc.translate(x, y);
		gc.rotate(deg);
		gc.translate(-x, -y);
		drawPxCircle(x, y, r, r, c, fill);
		gc.setStroke(c);
		gc.strokeLine(x, y, x + r, y);
		gc.restore();
	}

	private void drawPxCircle(double x, double y, double w, double h, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);

		if (fill)
			gc.fillOval(x - w, y - h, 2 * w, 2 * h);
		else
			gc.strokeOval(x - w, y - h, 2 * w, 2 * h);
	}

	@SuppressWarnings("unused")
	private void drawPxRect(double x, double y, double w, double h, double deg, Color c, boolean fill) {
		gc.save();
		gc.translate(x, y);
		gc.rotate(deg);
		gc.translate(-x, -y);
		drawPxRect(x, y, w, h, c, fill);
		gc.restore();
	}

	private void drawPxRect(Vec2 pos, Vec2 dim, double deg, Color c, boolean fill) {
		gc.save();
		gc.translate(pos.x, pos.y);
		gc.rotate(deg);
		gc.translate(-pos.x, -pos.y);
		drawPxRect(pos.x, pos.y, dim.x, dim.y, c, fill);
		gc.restore();
	}

	private void drawPxRect(double x, double y, double w, double h, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);

		if (fill)
			gc.fillRect(x - w, y - h, 2 * w, 2 * h);
		else
			gc.strokeRect(x - w, y - h, 2 * w, 2 * h);

	}
	
	public void drawBody(B2DBody body) {
		if (body.getShapeType() == ShapeType.CIRCLE) {
			drawSphere(body);
		}
		else if (body.getShapeType() == ShapeType.CUBOID) {
			drawCuboid(body);
		}
	}
	
	private void drawCuboid(B2DBody cube) {
		drawPxRect(cu.coordWorldToPixels(cube.getPos()),
				cu.scalarWorldToPixels(cube.getDim()),
				ConvertUnits.radToDeg(-cube.getAngle()),
				cube.getColor(), cube.getFill());
	}

	private void drawSphere(B2DBody sphere) {
		Vec2 pos = cu.coordWorldToPixels(sphere.getPos());
		float rad = cu.scalarWorldToPixels(sphere.getDim().x);
		float angl = ConvertUnits.radToDeg(-sphere.getAngle());
		drawPxLineCircle(pos.x, pos.y, rad, angl, sphere.getColor(), sphere.getFill());
	}
	
	public void drawLine(Vec2 b2d_pos1, Vec2 b2d_pos2, Color c) {
		b2d_pos1 = cu.coordWorldToPixels(b2d_pos1);
		b2d_pos2 = cu.coordWorldToPixels(b2d_pos2);
		
		gc.setStroke(c);
		gc.strokeLine(b2d_pos1.x, b2d_pos1.y, b2d_pos2.x, b2d_pos2.y);
	}
	

	
	public void drawLocalLine(Vec2 body1_pos, Vec2 body1_loc, float body1_rot, Vec2 body2_pos, Vec2 body2_loc, float body2_rot, Color c) {
		Vec2 rotated1 = body1_pos.add(ConvertUnits.rotateVec2(body1_loc, body1_rot));
		Vec2 rotated2 = body2_pos.add(ConvertUnits.rotateVec2(body2_loc, body2_rot));
		drawLine(rotated1, rotated2, c);
	}

	public void clearScreen() {
		gc.setFill(col_background);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public void setScale(double scale_in) {
		cu.setScale(scale_in);
	}

	public double getScale() {
		return cu.getScale();
	}

	public void setPos(Vec2 pos_in) {
		cu.setPos(pos_in);
	}

	public void addPos(Vec2 pos_in) {
		cu.addPos(pos_in);
	}

	public Vec2 getPos() {
		return cu.getPos();
	}

}
