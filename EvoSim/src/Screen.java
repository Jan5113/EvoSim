import org.jbox2d.common.Vec2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Screen extends Canvas {
	GraphicsContext gc;
	Color col_background;

	public Screen(double arg0, double arg1) {
		super(arg0, arg1);
		
		gc = this.getGraphicsContext2D();
		col_background = Color.color(1, 0.8, 0.8);
		clearScreen();
	}

	public void drawCircle(double x, double y, double w, double h, double deg, Color c, boolean fill) { 
	    gc.save();
	    gc.translate(x, y);
	    gc.rotate(deg);
	    gc.translate(-x, -y);
		drawCircle(x, y, w, h, c, fill);
	    gc.restore();
	}

	public void drawCircle(double x, double y, double w, double h, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);

		if (fill)
			gc.fillOval(x - w, y - h, 2*w, 2*h);
		else
			gc.strokeOval(x - w, y - h, 2*w, 2*h);
	}

	public void drawRect(double x, double y, double w, double h, double deg, Color c, boolean fill) {
	    gc.save();
	    gc.translate(x, y);
	    gc.rotate(deg);
	    gc.translate(-x, -y);
		drawRect(x, y, w, h, c, fill);
	    gc.restore();
	}

	public void drawRect(Vec2 pos, Vec2 dim, double deg, Color c, boolean fill) {
	    gc.save();
	    gc.translate(pos.x, pos.y);
	    gc.rotate(deg);
	    gc.translate(-pos.x, -pos.y);
		drawRect(pos.x, pos.y, dim.x, dim.y, c, fill);
	    gc.restore();
	}

	public void drawRect(double x, double y, double w, double h, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);

		if (fill)
			gc.fillRect(x - w, y - h, 2*w, 2*h);
		else
			gc.strokeRect(x - w, y - h, 2*w, 2*h);
		
	}
	
	public void drawRect(B2DCube b2d_cube, Color c, boolean fill) {
		drawRect(ConvertUnits.coordWorldToPixels(b2d_cube.getPos()), ConvertUnits.scalarWorldToPixels(b2d_cube.getDim()), ConvertUnits.radToDeg(-b2d_cube.getRot()), c, fill);
	}
	
	public void clearScreen () {		
		gc.setFill(col_background);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

}
