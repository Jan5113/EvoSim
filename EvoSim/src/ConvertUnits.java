import org.jbox2d.common.Vec2;

public class ConvertUnits {
	private float scale = 100; // 100px = 1m
	private static float maxScale = 20;
	private Vec2 posCam = new Vec2(0,5);
	private Vec2 resCam = new Vec2(500, 500);
	
	public ConvertUnits (float scale_in, Vec2 pos_in, Vec2 res_in) {
		scale = scale_in;
		posCam = pos_in;
		resCam = res_in;
	}
	
	public Vec2 coordPixelsToWorld (Vec2 px_pos) {
		Vec2 B2D_pos = px_pos.add(resCam.mul(0.5f).negate()).mul(1/scale);
		return new Vec2(posCam.x + B2D_pos.x, posCam.y - B2D_pos.y);
	}
	
	public Vec2 coordPixelsToWorld (float px_x, float px_y) {
		return coordPixelsToWorld(new Vec2(px_x, px_y));
	}
	
	public Vec2 coordPixelsToWorld (double px_x, double px_y) {
		return coordPixelsToWorld((float) px_x, (float) px_y);
	}
	
	
	public Vec2 coordWorldToPixels (Vec2 B2D_pos) {
		Vec2 px_pos = B2D_pos.add(posCam.negate());
		px_pos.y = -px_pos.y;
		return px_pos.mul(scale).add(resCam.mul(0.5f));
	}
	
	static  public Vec2 rotateVec2(Vec2 v, float rad) {
		Vec2 rotated = new Vec2(
						v.x * (float) Math.cos(rad) - v.y * (float) Math.sin(rad),
						v.x * (float) Math.sin(rad) + v.y * (float) Math.cos(rad)
						);
		return rotated;
	}
	
	
	
	
	public float scalarPixelsToWorld (float px_f) {
		return px_f / scale;
	}

	public Vec2 scalarPixelsToWorld (Vec2 px_vec) {
		return px_vec.mul(1/scale);
	}

	public float scalarWorldToPixels (float B2D_f) {
		return B2D_f * scale;
	}

	public Vec2 scalarWorldToPixels (Vec2 B2D_vec) {
		return B2D_vec.mul(scale);
	}
	
	static public float radToDeg (float rad) {
		return rad * 180 / (float) Math.PI;
	}
	
	static public float degToRad (float deg) {
		return deg * (float) Math.PI / 180;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale_in) {
		if (scale_in >= maxScale)
		scale = scale_in;
	}
	
	public Vec2 getPos() {
		return posCam;
	}
	
	public void setPos(Vec2 pos_in) {
		posCam = pos_in;
	}
	
	public void addPos(Vec2 pos_in) {
		posCam.addLocal(pos_in);
	}
	
	public void setScreenRes(Vec2 res_in) {
		resCam = res_in;
	}
}
