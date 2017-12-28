import java.util.Random;

import org.jbox2d.common.Vec2;

public class MutVec2{
	private MutVal diffLen;
	private MutVal diffDir;
	
	private float x;
	private float y;
	
	private Random rand = new Random();
	

	public MutVec2(Vec2 val, float rng) {
		diffLen = new MutVal(rng * rand.nextFloat(), rng);
		diffDir = new MutVal((float) (rand.nextFloat() * Math.PI * 2), rng);
		x = val.x;
		y = val.y;
	}
	
	public MutVec2(Vec2 min, Vec2 max, float rng) {
		if (min.y > max.y) max.y += (min.y - (min.y = max.y)); // swap
		if (min.x > max.x) max.x += (min.x - (min.x = max.x));
		diffLen = new MutVal(rng * rand.nextFloat(), rng);
		diffDir = new MutVal((float) (rand.nextFloat() * Math.PI * 2), rng);
		x = (float) Math.random() * (max.x - min.x) + min.x;
		y = (float) Math.random() * (max.y - min.y) + min.y;
	}
	
	public MutVec2(float x_in, float y_in, MutVal dir_in, MutVal len_in) {
		x = x_in;
		y = y_in;
		diffDir = dir_in.clone();
		diffLen = len_in.clone();
	}
	
	public MutVec2 mutate() {
		MutVal tempDir = diffDir.mutate();
		MutVal tempLen = diffLen.mutate();
		return new MutVec2(x + (float) (Math.cos(tempDir.getVal()) * tempLen.getSqVal()), y + (float) (Math.sin(tempDir.getVal()) * tempLen.getSqVal()), tempDir, tempLen);
	}
	
	public Vec2 getVal() {
		return new Vec2(x, y);
	}
	
}
