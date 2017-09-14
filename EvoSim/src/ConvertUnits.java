import org.jbox2d.common.Vec2;

public class ConvertUnits {
	private static final float scale = 100; // 100px = 1m
	private static final Vec2 posCam = new Vec2(0,5);
	
	
	
	public static Vec2 coordPixelsToWorld (Vec2 px_pos) {
		Vec2 B2D_pos = px_pos.mul(1/scale);
		return new Vec2(posCam.x + B2D_pos.x, posCam.y - B2D_pos.y);
	}
	
	public static Vec2 coordPixelsToWorld (float px_x, float px_y) {
		return coordPixelsToWorld(new Vec2(px_x, px_y));
	}
	
	public static Vec2 coordPixelsToWorld (double px_x, double px_y) {
		return coordPixelsToWorld((float) px_x, (float) px_y);
	}
	
	
	public static Vec2 coordWorldToPixels (Vec2 B2D_pos) {
		Vec2 px_pos = B2D_pos.add(posCam.negate());
		px_pos.y = -px_pos.y;
		return px_pos.mul(scale);
	}
	
	
	
	
	public static float scalarPixelsToWorld (float px_f) {
		return px_f / scale;
	}

	public static Vec2 scalarPixelsToWorld (Vec2 px_vec) {
		return px_vec.mul(1/scale);
	}

	public static float scalarWorldToPixels (float B2D_f) {
		return B2D_f * scale;
	}

	public static Vec2 scalarWorldToPixels (Vec2 B2D_vec) {
		return B2D_vec.mul(scale);
	}
	
	public static double radToDeg (double rad) {
		return rad * 180 / Math.PI;
	}
	
	public static double degToRad (double deg) {
		return deg * Math.PI / 180;
	}
}
