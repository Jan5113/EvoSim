import org.jbox2d.common.Vec2;

public class B2DCamera {
	private final float zoomStart;
	private float zoom = 100; // 100px = 1m
	private static float maxZoomOut = 20;
	private static float maxZoomIn = 400;
	private Vec2 posCam;
	private final Vec2 posCamStart;
	private Vec2 resCam = new Vec2(500, 500);
	
	private boolean followXEnabled = false;
	private boolean followYEnabled = false;
	private static float followMaxAccel = 60.0f;
	private static float followResistance = 0.8f;
	private Vec2 followSpeed = new Vec2(0,0);
	
	public B2DCamera (float zoom_in, Vec2 pos_in, Vec2 res_in) {
		zoom = zoom_in;
		posCam = pos_in;
		resCam = res_in;
		
		posCamStart = posCam.clone();
		zoomStart = zoom_in;
	}
	
	public Vec2 coordPixelsToWorld (Vec2 px_pos) {
		Vec2 B2D_pos = px_pos.add(resCam.mul(0.5f).negate()).mul(1/zoom);
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
		return px_pos.mul(zoom).add(resCam.mul(0.5f));
	}
	
	static  public Vec2 rotateVec2(Vec2 v, float rad) {
		Vec2 rotated = new Vec2(
						v.x * (float) Math.cos(rad) - v.y * (float) Math.sin(rad),
						v.x * (float) Math.sin(rad) + v.y * (float) Math.cos(rad)
						);
		return rotated;
	}
	
	static public float getRotation(Vec2 v) {
		float atan;
		if (v.x == 0) {
			if (v.y >= 0) atan =  (float) Math.PI*0.5f; //90°
			else atan =  (float) Math.PI*1.5f; //270°
		}
		else if (v.y == 0) {
			if (v.x >= 0) atan =  0; //0°
			else atan =  (float) Math.PI; //180°
		}
		else {
			atan = (float)(Math.atan(v.y/v.x));
			if (v.x > 0 && v.y < 0) atan += (float) 2 * Math.PI; //Q4
			else if (v.x < 0) atan += (float) Math.PI; //Q2 & Q3
		}		
		return atan;
	}
	
	
	
	
	public float scalarPixelsToWorld (float px_f) {
		return px_f / zoom;
	}

	public Vec2 scalarPixelsToWorld (Vec2 px_vec) {
		return px_vec.mul(1/zoom);
	}

	public float scalarWorldToPixels (float B2D_f) {
		return B2D_f * zoom;
	}

	public Vec2 scalarWorldToPixels (Vec2 B2D_vec) {
		return B2D_vec.mul(zoom);
	}
	
	static public float radToDeg (float rad) {
		return rad * 180 / (float) Math.PI;
	}
	
	static public float degToRad (float deg) {
		return deg * (float) Math.PI / 180;
	}
	
	public float getZoom() {
		return zoom;
	}
	
	public void setZoom(float zoom_in) {
		if (zoom_in <= maxZoomOut) zoom_in = maxZoomOut;
		zoom = zoom_in;
	}
	
	public void zoomCenter(float magnification) {
		if ((zoom == maxZoomOut && magnification < 1.0f) || (zoom == maxZoomIn && magnification > 1.0f)) return;
		
		if (magnification * zoom <= maxZoomOut) zoom = maxZoomOut;
		else if (magnification * zoom >= maxZoomIn) zoom = maxZoomIn;
		else zoom *= magnification;
	}
	
	public void zoomInPoint(float magnification, Vec2 B2DCenterPos) {
		if ((zoom == maxZoomOut && magnification < 1.0f) || (zoom == maxZoomIn && magnification > 1.0f)) return;
		
		if (magnification * zoom <= maxZoomOut) zoom = maxZoomOut;
		else if (magnification * zoom >= maxZoomIn) zoom = maxZoomIn;
		else zoom *= magnification;
		
		addPos(B2DCenterPos.sub(posCam).mul((magnification - 1.0f) * (1.0f / magnification)));
		
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
	
	//************************************************
	//*		FOLLOW CAM
	//************************************************
	
	public void enableFollow() {
		followXEnabled = true;
		followYEnabled = true;
	}
	
	public void enableFollowX() {
		followXEnabled = true;
	}
	
	public void enableFollowY() {
		followYEnabled = true;
	}
	
	public void disableFollow() {
		followXEnabled = false;
		followYEnabled = false;
	}
	
	public boolean followXEnabled() {
		return followXEnabled;
	}
	
	public boolean followYEnabled() {
		return followYEnabled;
	}
	
	public void refreshFollow(float dt, float playbackspeed, Vec2 B2D_target) {
		if (B2D_target == null) return;
		followSpeed.addLocal(B2D_target.sub(posCam).mul(followMaxAccel).mul(dt)).mulLocal(followResistance);
		if (followXEnabled) addPos(new Vec2(followSpeed.x * dt * playbackspeed, 0.0f));
		if (followYEnabled) addPos(new Vec2(0.0f, followSpeed.y * dt * playbackspeed));
	}
	
	public void resetPosZoom() {
		resetPos();
		zoom = zoomStart;
		
	}
	
	public void resetPos() {
		followSpeed = new Vec2(0,0);
		posCam = posCamStart.clone();
	}
}
