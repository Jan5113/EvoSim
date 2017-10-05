import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class B2DCube {
	
	private Vec2 dim;
	private Body b_cube;
	
	public B2DCube (Vec2 B2D_pos, Vec2 B2D_dim , BodyType bt_cube, World world) {
		SetupCube(B2D_pos, B2D_dim, bt_cube, world);
	}
	public B2DCube (Vec2 B2D_pos, Vec2 B2D_dim , Vec2 B2D_vel, float rad_in, BodyType bt_cube, World world) {
		dim = B2D_dim;
		
		PolygonShape ps_cube = new PolygonShape();
		ps_cube.setAsBox(dim.x, dim.y);
		
		FixtureDef fd_cube = new FixtureDef();
		fd_cube.density = 1f;
		fd_cube.friction = 0.3f;
		fd_cube.restitution = 0.5f;
		fd_cube.shape = ps_cube;
		fd_cube.filter.groupIndex = - 2;
		
		BodyDef bd_cube = new BodyDef();
		bd_cube.type = bt_cube;
		bd_cube.setPosition(B2D_pos);
		bd_cube.setAngle(rad_in);
		bd_cube.setLinearVelocity(B2D_vel);

		b_cube = world.createBody(bd_cube);
		b_cube.createFixture(fd_cube);

		
		
	}
	public B2DCube (float B2D_x, float B2D_y, float B2D_w, float B2D_h, BodyType bt_cube, World world) {
		Vec2 B2D_pos = new Vec2(B2D_x,B2D_y);
		Vec2 B2D_dim = new Vec2(B2D_w, B2D_h);
		SetupCube(B2D_pos, B2D_dim, bt_cube, world);
	}
	
	private void SetupCube(Vec2 B2D_pos, Vec2 B2D_dim , BodyType bt_cube, World world) {
		dim = B2D_dim;
		
		BodyDef bd_cube = new BodyDef();
		bd_cube.type = bt_cube;
		bd_cube.setPosition(B2D_pos);
		
		b_cube = world.createBody(bd_cube);
		
		PolygonShape ps_cube = new PolygonShape();
		ps_cube.setAsBox(dim.x, dim.y);
		
		FixtureDef fd_cube = new FixtureDef();
		fd_cube.shape = ps_cube;
		fd_cube.density = 1f;
		fd_cube.friction = 0.3f;
		fd_cube.restitution = 0.5f;
		fd_cube.filter.groupIndex = 2;
		
		b_cube.createFixture(fd_cube);
	}
	
	public Vec2 getPos() {
		return b_cube.getPosition();
	}
	
	public float getRot() {
		return b_cube.getAngle();
	}	
	
	public Vec2 getDim() {
		return dim;
	}
	
	public Vec2 getVel() {
		return b_cube.getLinearVelocity();
	}
	
	public void setVel(Vec2 vel_in) {
		b_cube.setLinearVelocity(vel_in);
	}
	
	public Body getB2D() {
		return b_cube;
	}
}
