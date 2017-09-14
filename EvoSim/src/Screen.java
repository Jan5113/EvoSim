import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Screen extends Canvas {
	GraphicsContext gc;

	public Screen(double arg0, double arg1) {
		super(arg0, arg1);
		gc = this.getGraphicsContext2D();
		gc.setFill(Color.color(1, 0.8, 0.8));
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
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

	public void drawRect(double x, double y, double w, double h, Color c, boolean fill) {
		gc.setStroke(c);
		gc.setFill(c);

		if (fill)
			gc.fillRect(x - w, y - h, 2*w, 2*h);
		else
			gc.strokeRect(x - w, y - h, 2*w, 2*h);
		
	}

}
