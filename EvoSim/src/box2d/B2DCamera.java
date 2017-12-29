package box2d;
import org.jbox2d.common.Vec2;

import display.Screen;

/**
 * The {@link B2DCamera} class is used to convert the Box2D-World coordinates to
 * pixel coordinates and the other way around. This class resembles a camera
 * with its own position and magnification, so it can be moved and zoomed like
 * an actual camera.
 * <p>
 * The {@link B2DCamera} also contains a follow-feature to track objects on
 * screen (with speed and acceleration to make the movements smooth)
 *
 */
public class B2DCamera {
	/**
	 * Holds the default zoom value in case of a reset.
	 */
	private final float zoomStart;
	/**
	 * Holds the current zoom value. The value given defines how many screen pixels
	 * are equivalent to one metre in the Box2D World, so a smaller value zooms out
	 * and a bigger value zooms into the scene.
	 */
	private float zoom = 100; // 100px = 1m
	/**
	 * Holds the static value for the maximum the {@link B2DCamera} can zoom out.
	 */
	private static float maxZoomOut = 20;
	/**
	 * Holds the static value for the maximum the {@link B2DCamera} can zoom in.
	 */
	private static float maxZoomIn = 400;
	/**
	 * Holds the current position {@link Vec2} vector of the centre of this
	 * {@link B2DCamera} instance.
	 */
	private Vec2 posCam;
	/**
	 * Holds the default position {@link Vec2} vector in case of a reset.
	 */
	private final Vec2 posCamStart;
	/**
	 * Holds the pixel resolution of the {@link B2DCamera}. It is used to calculate
	 * the centre of the {@link B2DCamera} and has to be updated if the resolution
	 * changes.
	 */
	private Vec2 resCam = new Vec2(500, 500);
	
	/**
	 * Holds the information whether the camera should follow the given coordinate in
	 * the x direction.
	 */
	private boolean followXEnabled = false;
	/**
	 * Holds the information whether the camera should follow the given coordinate in
	 * the y direction.
	 */
	private boolean followYEnabled = false;
	/**
	 * Holds the value for the maximum acceleration the {@link B2DCamera} can
	 * experience. The higher this value, the snappier the movement looks.
	 */
	private static float followMaxAccel = 60.0f;
	/**
	 * Holds the amount of resistance the camera movement experiences. {@code 1.0f}
	 * resembles no friction at all, the lower the value, the more languorous 
	 * the camera gets and the smoother the movement looks.
	 *  
	 */
	private static float followResistance = 0.8f;
	/**
	 * Holds the currents speed {@link Vec2} vector of the {@link B2DCamera}.
	 */
	private Vec2 followSpeed = new Vec2(0,0);
	
	/**
	 * Initialises a new {@link B2DCamera} instance. The {@code zoom_in} value
	 * defines how many screen pixels are equivalent to one metre in the Box2D
	 * World, so a small value zooms out of and a big value zooms into the scene.
	 * The {@code pos_in} {@link Vec2} vector sets the centre position of the
	 * {@link B2DCamera} and the res_in {@link Vec2} vector specifies the size of
	 * the {@link Screen} resolution (in px) this {@link B2DCamera} instance belongs
	 * to.
	 * 
	 * @param zoom_in
	 * specifies the number of screen pixels for one metre
	 * @param pos_in
	 * sets the position of the centre of the {@link B2DCamera}
	 * @param res_in
	 * sets the pixel resolution of the {@link Screen} this instance belongs to
	 */
	public B2DCamera (float zoom_in, Vec2 pos_in, Vec2 res_in) {
		zoom = zoom_in;
		posCam = pos_in;
		resCam = res_in;
		
		posCamStart = posCam.clone();
		zoomStart = zoom_in;
	}
	
	/**
	 * Takes a pixel-coordinates point as a {@link Vec2} vector and converts those
	 * to the correspondent Box2D-World coordinates. The current position and zoom
	 * of this {@link B2DCamera} instance is taken into account by the conversion
	 * calculation.
	 * <p>
	 * <i>Example:</i> Use this method to create a body where a click has been
	 * registered.
	 * 
	 * @param px_pos
	 *            is the pixel-coordinate input vector (Vec2(x, y))
	 * @return the converted Box2D-World coordinates vector
	 */
	public Vec2 coordPixelsToWorld (Vec2 px_pos) {
		Vec2 B2D_pos = px_pos.add(resCam.mul(0.5f).negate()).mul(1/zoom);
		return new Vec2(posCam.x + B2D_pos.x, posCam.y - B2D_pos.y);
	}
	
	/**
	 * Takes the x and y values of the pixel-coordinates and converts those to the
	 * correspondent Box2D-World coordinates. The current position and zoom of this
	 * {@link B2DCamera} instance is taken into account by the conversion
	 * calculation.
	 * <p>
	 * <i>Example:</i> Use this method to create a body where a click has been
	 * registered.
	 * 
	 * @param px_x
	 *            x part of the input pixel-coordinates
	 * @param px_y
	 *            y part of the input pixel-coordinates
	 * @return the converted Box2D-World coordinates vector
	 */
	public Vec2 coordPixelsToWorld (float px_x, float px_y) {
		return coordPixelsToWorld(new Vec2(px_x, px_y));
	}

	/**
	 * Takes the x and y values of the pixel-coordinates and converts those to the
	 * correspondent Box2D-World coordinates. The current position and zoom of this
	 * {@link B2DCamera} instance is taken into account by the conversion
	 * calculation.
	 * <p>
	 * <i>Example:</i> Use this method to create a body where a click has been
	 * registered.
	 * 
	 * @param px_x
	 *            x part of the input pixel-coordinates
	 * @param px_y
	 *            y part of the input pixel-coordinates
	 * @return the converted Box2D-World coordinates vector
	 */
	public Vec2 coordPixelsToWorld (double px_x, double px_y) {
		return coordPixelsToWorld((float) px_x, (float) px_y);
	}
	
	
	/**
	 * Takes a Box2D-World coordinates point as a {@link Vec2} vector and converts
	 * those to the correspondent pixel-coordinates. The current position and zoom
	 * of this {@link B2DCamera} instance is taken into account by the conversion
	 * calculation.
	 * <p>
	 * <i>Example:</i> Use this method to render a {@link B2DBody} on screen
	 * 
	 * @param B2D_pos
	 *            is the Box2D-World coordinate input vector
	 * @return the converted pixel-coordinates vector
	 */
	public Vec2 coordWorldToPixels (Vec2 B2D_pos) {
		Vec2 px_pos = B2D_pos.add(posCam.negate());
		px_pos.y = -px_pos.y;
		return px_pos.mul(zoom).add(resCam.mul(0.5f));
	}
	
	/**
	 * Takes a {@link Vec2} vector and a rotation value (in radiant) and rotates the
	 * vector by the value. This method doesn't modify the input vector, instead it
	 * returns a new {@link Vec2} vector instance.
	 * 
	 * @param v
	 *            input vector to be rotated
	 * @param rad
	 *            rotation amount (in radiant and counterclockwise)
	 * @return a new rotated {@link Vec2} instance
	 */
	static public Vec2 rotateVec2(Vec2 v, float rad) {
		Vec2 rotated = new Vec2(
						v.x * (float) Math.cos(rad) - v.y * (float) Math.sin(rad),
						v.x * (float) Math.sin(rad) + v.y * (float) Math.cos(rad)
						);
		return rotated;
	}
	
	/**
	 * Takes a {@link Vec2} vector and returns its argument (rotation). The return
	 * value is given in radiant counterclockwise starting at the positive x-axis.
	 * Special cases (0°, 90°, 180° and 270°) are considered.
	 * 
	 * @param v
	 *            input vector
	 * @return argument (rotation) of the input vector
	 */
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
	
	/**
	 * Takes a pixel-scalar and returns the corresponding Box2D-World value. The
	 * current zoom of this {@link B2DCamera} instance is taken into account by the
	 * conversion calculation.
	 * <p>
	 * <i>Example:</i> Use this method to move a {@link B2DBody} by a distance
	 * dragged on screen.
	 * 
	 * @param px_f
	 *            input pixel scalar
	 * @return converted Box2D-World scalar
	 */
	public float scalarPixelsToWorld (float px_f) {
		return px_f / zoom;
	}

	/**
	 * Takes two pixel-scalars (as a {@link Vec2} instance) and returns the
	 * corresponding Box2D-World values. The current zoom of this {@link B2DCamera}
	 * instance is taken into account by the conversion calculation.
	 * <p>
	 * <i>Example:</i> Use this method to move a {@link B2DBody} by a x and y
	 * distance dragged on screen.
	 * 
	 * @param px_f
	 *            input pixel scalar pair (Vec2 instance)
	 * @return converted Box2D-World scalar pair (Vec2 instance)
	 */
	public Vec2 scalarPixelsToWorld (Vec2 px_vec) {
		return px_vec.mul(1.0f/zoom);
	}

	/**
	 * Takes a Box2D-World scalar and returns the corresponding pixel value.
	 * The current zoom of this {@link B2DCamera} instance is taken into account by
	 * the conversion calculation.
	 * <p>
	 * <i>Example:</i> Use this method to calculate the pixel radius of a
	 * {@link B2DBody} circle for rendering
	 * 
	 * @param B2D_f
	 *            input Box2D-World scalar
	 * @return converted pixel scalar
	 */
	public float scalarWorldToPixels (float B2D_f) {
		return B2D_f * zoom;
	}

	/**
	 * Takes a Box2D-World scalar pair and returns the corresponding pixel values.
	 * The current zoom of this {@link B2DCamera} instance is taken into account by
	 * the conversion calculation.
	 * <p>
	 * <i>Example:</i> Use this method to calculate the pixel height and width of a
	 * {@link B2DBody} rectangle for rendering
	 * 
	 * @param B2D_vec
	 *            input Box2D-World scalar pair (Vec2 instance)
	 * @return converted pixel scalar pair (Vec2 instance)
	 */
	public Vec2 scalarWorldToPixels (Vec2 B2D_vec) {
		return B2D_vec.mul(zoom);
	}
	
	/**
	 * Returns the current zoom value of this {@link B2DCamera} instance. The value
	 * represents the number of screen pixels equivalent to one metre in the Box2D
	 * World, so if zoomed out, this method returns a small value and the other way
	 * around.
	 * 
	 * @return amount of pixels equivalent to one Box2D World metre
	 */
	public float getZoom() {
		return zoom;
	}
	
	/**
	 * Sets the absolute zoom value. The value given defines how many screen pixels
	 * are equivalent to one metre in the Box2D World, so a smaller value zooms out
	 * and a bigger value zooms into the scene.
	 * 
	 * @param zoom_in
	 *            sets the zoom of this {@link B2DCamera} instance
	 */
	public void setZoom(float zoom_in) {
		zoom = zoom_in;
	}
	
	/**
	 * Zooms into or out of the scene by the {@code magnification} factor. A
	 * {@code magnification} value under {@code 1.0f} zooms out and a
	 * {@code magnification} value over {@code 1.0f} zooms into the scene. The
	 * centre of the zoom is always the centre of the view.
	 * 
	 * @param magnification
	 *            changes the zoom of this {@link B2DCamera} instance by its factor
	 */
	public void zoomCenter(float magnification) {
		if ((zoom == maxZoomOut && magnification < 1.0f) || (zoom == maxZoomIn && magnification > 1.0f)) return;
		
		if (magnification * zoom <= maxZoomOut) zoom = maxZoomOut;
		else if (magnification * zoom >= maxZoomIn) zoom = maxZoomIn;
		else zoom *= magnification;
	}
	
	/**
	 * Zooms into or out of the scene by the {@code magnification} factor given
	 * while keeping the Box2D point given at the exact same location on screen. A
	 * {@code magnification} value under {@code 1.0f} zooms out and a
	 * {@code magnification} value over {@code 1.0f} zooms into the scene. The
	 * coordinate given is the centre of the zoom and is usually set to the cursor's
	 * position. If {@code respectLockDirs} is {@code true} the zoom process only
	 * centres the not-locked directions of this {@link B2DCamera} instance.
	 * 
	 * @param magnification
	 *            changes the zoom of this {@link B2DCamera} instance by its factor
	 * @param B2DCenterPos
	 *            defines the centre of the zoom (in Box2D coordinates)
	 * @param respectLockDirs
	 *            centres the zoom only in the unlocked directions if {@code true}
	 */
	public void zoomInPoint(float magnification, Vec2 B2DCenterPos, boolean respectLockDirs) {
		if ((zoom == maxZoomOut && magnification < 1.0f) || (zoom == maxZoomIn && magnification > 1.0f)) return;
		
		if (magnification * zoom <= maxZoomOut) zoom = maxZoomOut;
		else if (magnification * zoom >= maxZoomIn) zoom = maxZoomIn;
		else zoom *= magnification;
		
		if (respectLockDirs) {
			if (followXEnabled) {
				addPos(new Vec2(0.0f, B2DCenterPos.sub(posCam).mul((magnification - 1.0f) * (1.0f / magnification)).y));
			}
			if (followYEnabled) {
				addPos(new Vec2(B2DCenterPos.sub(posCam).mul((magnification - 1.0f) * (1.0f / magnification)).x, 0.0f));
			}
		} else {
			addPos(B2DCenterPos.sub(posCam).mul((magnification - 1.0f) * (1.0f / magnification)));
		}
		
		
	}
	
	/**
	 * Returns the current position of this {@link B2DCamera} instance. The
	 * coordinates are given in Box2D coordinates as a {@link Vec2} vector.
	 * 
	 * @return the location of this {@code B2DCamera} instance
	 */
	public Vec2 getPos() {
		return posCam;
	}
	
	/**
	 * Takes Box2D World coordinates as a {@link Vec2} vector and sets the centre of
	 * this {@link B2DCamera} instance.
	 * 
	 * @param pos_in
	 *            sets the centre position of this {@code B2DCamera} instance
	 */
	public void setPos(Vec2 pos_in) {
		posCam = pos_in;
	}
	
	/**
	 * Takes Box2D World {@link Vec2} vector and moves this {@link B2DCamera}
	 * instance by this vector.
	 * 
	 * 
	 * @param pos_in
	 *            moves this {@code B2DCamera} instance
	 */
	public void addPos(Vec2 pos_in) {
		posCam.addLocal(pos_in);
	}
	
	/**
	 * Takes a pixel {@link Vec2} vector and sets the screen to a new resolution.
	 * 
	 * @param res_in
	 *            sets the resolution of this {@link B2DCamera} instance
	 */
	public void setScreenRes(Vec2 res_in) {
		resCam = res_in;
	}
	
	//************************************************
	//*		FOLLOW CAM
	//************************************************
	
	/**
	 * Enables the follow feature for both axis. The {@link B2DCamera} tracks a
	 * given coordinate or object in all directions.
	 */
	public void enableFollow() {
		followXEnabled = true;
		followYEnabled = true;
	}

	/**
	 * Enables the follow feature for the x-axis. The {@link B2DCamera} only tracks
	 * a given coordinate or object in the x-direction. This method does not disable
	 * the y-follow.
	 */
	public void enableFollowX() {
		followXEnabled = true;
	}

	/**
	 * Enables the follow feature for the y-axis. The {@link B2DCamera} only tracks
	 * a given coordinate or object in the y-direction. This method does not disable
	 * the x-follow.
	 */
	public void enableFollowY() {
		followYEnabled = true;
	}
	
	/**
	 * Disables the follow feature for both axis. The {@link B2DCamera} does not
	 * track any given coordinate or object.
	 */
	public void disableFollow() {
		followXEnabled = false;
		followYEnabled = false;
	}

	/**
	 * Disables the follow feature for the x-axis. The {@link B2DCamera} does not
	 * track any given coordinate or object in the x-axis. This method does not
	 * affect the y-axis.
	 */
	public boolean followXEnabled() {
		return followXEnabled;
	}

	/**
	 * Disables the follow feature for the y-axis. The {@link B2DCamera} does not
	 * track any given coordinate or object in the y-axis. This method does not
	 * affect the x-axis.
	 */
	public boolean followYEnabled() {
		return followYEnabled;
	}
	
	/**
	 * This method has to be called very frame with a Box2D-World coordinate this
	 * {@link B2DCamera} instance has to follow. The time since the last frame
	 * {@code dt} is used to calculate the acceleration and speed of this
	 * {@code B2DCamera} instance. To adjust for the faster movement when
	 * fast-forwarding, pass the current {@code playBackSpeed} to this method. If
	 * the follow had been restricted to an axis the camera will only move along the
	 * specified axis.
	 * 
	 * @param dt
	 *            delta time, time in-between frames
	 * @param playBackSpeed
	 *            current speed of the simulation
	 * @param B2D_target
	 *            coordinate this {@code B2DCamera} instance has to follow
	 */
	public void refreshFollow(float dt, float playBackSpeed, Vec2 B2D_target) {
		if (B2D_target == null) return;
		followSpeed.addLocal(B2D_target.sub(posCam).mul(followMaxAccel).mul(dt * playBackSpeed)).mulLocal((float) Math.pow(followResistance, playBackSpeed));
		if (followXEnabled) addPos(new Vec2(followSpeed.x * dt * playBackSpeed, 0.0f));
		if (followYEnabled) addPos(new Vec2(0.0f, followSpeed.y * dt * playBackSpeed));
	}
	
	/**
	 * Takes a Box2D World coordinate and moves this {@link B2DCamera} instance
	 * instantly to the target point. If the follow had been restricted to an axis
	 * the camera will only move along the specified axis.
	 * 
	 * @param B2D_target
	 *            coordinate this {@code B2DCamera} instance jumps to
	 */
	public void quickFollow(Vec2 B2D_target) {
		if (B2D_target == null) return;
		followSpeed = new Vec2(0,0);
		if (followXEnabled) addPos(new Vec2(B2D_target.sub(posCam).x, 0.0f));
		if (followYEnabled) addPos(new Vec2(0.0f, B2D_target.sub(posCam).y));		
	}
	

	/**
	 * Resets the position and zoom of this {@link B2DCamera} instance to the values
	 * it had been initialised with.
	 */
	public void resetPosZoom() {
		resetPos();
		zoom = zoomStart;
		
	}

	/**
	 * Resets only the position of this {@link B2DCamera} instance to the
	 * coordinates it had been initialised at.
	 */
	public void resetPos() {
		followSpeed = new Vec2(0,0);
		posCam = posCamStart.clone();
	}

}
