package mutation;
import java.util.Random;

import org.jbox2d.common.Vec2;

public class MutVec2{
	private static float[] defRangeMinXYMaxXY = {-2, 0, 0, 2};
	private final MutVal diffLen;
	private final MutVal diffDir;
	
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
	
	public MutVec2(float rng) {
		diffLen = new MutVal(rng * rand.nextFloat(), rng);
		diffDir = new MutVal((float) (rand.nextFloat() * Math.PI * 2), rng);
		
		x = (float) Math.random() * (defRangeMinXYMaxXY[2] - defRangeMinXYMaxXY[0]) + defRangeMinXYMaxXY[0];
		y = (float) Math.random() * (defRangeMinXYMaxXY[3] - defRangeMinXYMaxXY[1]) + defRangeMinXYMaxXY[1];
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
		return new MutVec2(
				sigmoid((float) (x + Math.cos(tempDir.getVal()) * tempLen.getSqVal()), defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2], 10),
				sigmoid((float) (y + Math.sin(tempDir.getVal()) * tempLen.getSqVal()), defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3], 10), tempDir, tempLen);
	}
	
	public Vec2 getVal() {
		return new Vec2(x, y);
	}
	
	public MutVec2 clone() {
		return new MutVec2(x, y, diffDir.clone(), diffLen.clone());
	}
	
	public static float sigmoid(float x, float min, float max, float expRange) {
		return (float) (1/( 1 + Math.pow(Math.E,(-Math.abs(5/(expRange))*x)))) * (max - min) + min;
	}
	
}
