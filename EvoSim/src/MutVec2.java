import org.jbox2d.common.Vec2;

public class MutVec2{
	private MutVal x;
	private MutVal y;

	public MutVec2(Vec2 val, float rng) {
		x = new MutVal(val.x, rng);
		y = new MutVal(val.y, rng);
	}
	
	public MutVec2(Vec2 min, Vec2 max, float rng) {
		if (min.y > max.y) max.y += (min.y - (min.y = max.y)); // swap
		if (min.x > max.x) max.x += (min.x - (min.x = max.x));
		x = new MutVal(min.x, max.x, rng);
		y = new MutVal(min.y, max.y, rng);
	}
	
	public MutVec2(MutVal x_in, MutVal y_in) {
		x = x_in;
		y = y_in;
	}
	
	public MutVec2 mutate() {
		return new MutVec2(x.mutate(), y.mutate());
	}
	
	public Vec2 getVal() {
		return new Vec2(x.getVal(), y.getVal());
	}
	
	public Vec2 getMutRange() {
		return new Vec2(x.getMutRange(), y.getMutRange());
	}
	
}
