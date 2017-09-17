import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class B2DSphere {
	
	private float rad;
	private Body b_sphere;
	
	public B2DSphere (Vec2 B2D_pos, float B2D_rad, BodyType bt_sphere, World world) {
		SetupSphere(B2D_pos, B2D_rad, bt_sphere, world);
	}
	public B2DSphere (Vec2 B2D_pos, float B2D_rad, Vec2 B2D_vel, float rad_in, BodyType bt_sphere, World world) {
		rad = B2D_rad;
		
		BodyDef bd_sphere = new BodyDef();
		bd_sphere.type = bt_sphere;
		bd_sphere.setPosition(B2D_pos);
		bd_sphere.setAngle(rad_in);
		bd_sphere.setLinearVelocity(B2D_vel);
		bd_sphere.bullet = true;
		
		b_sphere = world.createBody(bd_sphere);
		
		CircleShape ps_sphere = new CircleShape();
		ps_sphere.setRadius(rad);
		
		FixtureDef fd_sphere = new FixtureDef();
		fd_sphere.shape = ps_sphere;
		fd_sphere.density = 1f;
		fd_sphere.friction = 0.3f;
		fd_sphere.restitution = 0.5f;
		
		b_sphere.createFixture(fd_sphere);
	}
	public B2DSphere (float B2D_x, float B2D_y, float B2D_rad, BodyType bt_sphere, World world) {
		Vec2 B2D_pos = new Vec2(B2D_x,B2D_y);
		SetupSphere(B2D_pos, B2D_rad, bt_sphere, world);
	}
	
	private void SetupSphere(Vec2 B2D_pos, float B2D_rad , BodyType bt_sphere, World world) {
		rad = B2D_rad;
		
		BodyDef bd_sphere = new BodyDef();
		bd_sphere.type = bt_sphere;
		bd_sphere.setPosition(B2D_pos);
		
		b_sphere = world.createBody(bd_sphere);
		
		CircleShape ps_sphere = new CircleShape();
		ps_sphere.setRadius(rad);
		
		FixtureDef fd_sphere = new FixtureDef();
		fd_sphere.shape = ps_sphere;
		fd_sphere.density = 1f;
		fd_sphere.friction = 0.3f;
		fd_sphere.restitution = 0.5f;
		
		b_sphere.createFixture(fd_sphere);
	}
	
	public Vec2 getPos() {
		return b_sphere.getPosition();
	}
	
	public double getRot() {
		return b_sphere.getAngle();
	}	
	
	public float getRadius() {
		return rad;
	}
	
	public Vec2 getVel() {
		return b_sphere.getLinearVelocity();
	}
	
	public void setVel(Vec2 vel_in) {
		b_sphere.setLinearVelocity(vel_in);
	}
}
