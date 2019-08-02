package mutation;
import java.io.Serializable;

import org.jbox2d.common.Vec2;


public class MutVec2 implements Serializable {
	private static final long serialVersionUID = 1L;
	private float[] defRangeMinXYMaxXY = {-2, 0, 0, 2};
	
	private MutVal x;
	private MutVal y;	
	
	private boolean mutates = true;
	
	private final Vec2 offset;

	public MutVec2(Vec2 val, float[] minXY_maxXY) {
		defRangeMinXYMaxXY = minXY_maxXY;
		x = new MutVal(defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2], val.x);
		y = new MutVal(defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3], val.y);
		
		offset = new Vec2((float) Math.random() * 0.001f, (float) Math.random() * 0.001f);
	}

	public MutVec2(float[] minXY_maxXY) {
		defRangeMinXYMaxXY = minXY_maxXY;
		x = new MutVal(defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2]);
		y = new MutVal(defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3]);
		
		offset = new Vec2((float) Math.random() * 0.001f, (float) Math.random() * 0.001f);
	}
	
	public MutVec2(Vec2 val) {
		x = new MutVal(defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2], val.x);
		y = new MutVal(defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3], val.y);
		
		offset = new Vec2((float) Math.random() * 0.001f, (float) Math.random() * 0.001f);
	}
	
	public MutVec2(Vec2 val, boolean mutates_in) {
		x = new MutVal(defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2], val.x);
		y = new MutVal(defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3], val.y);
		
		offset = new Vec2((float) Math.random() * 0.001f, (float) Math.random() * 0.001f);
		mutates = mutates_in;
	}
	
	public MutVec2() {
		x = new MutVal(defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2]);
		y = new MutVal(defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3]);
		
		offset = new Vec2((float) Math.random() * 0.001f, (float) Math.random() * 0.001f);
	}
	
	public MutVec2(MutVal x_in, MutVal y_in, float[] minXY_maxXY,boolean mutates_in, Vec2 offset_in) {
		x = x_in;
		y = y_in;
		defRangeMinXYMaxXY = minXY_maxXY;
		mutates = mutates_in;
		offset = new Vec2(offset_in);
	}
	
	public MutVec2(MutVal x_in, MutVal y_in, float[] minXY_maxXY, boolean mutates_in) {
		x = x_in;
		y = y_in;
		defRangeMinXYMaxXY = minXY_maxXY;
		mutates = mutates_in;
		offset = new Vec2((float) Math.random() * 0.001f, (float) Math.random() * 0.001f);
	}
	
	public MutVec2 mutate(int gen) {
		if (mutates) {
			return new MutVec2(x.mutate(gen), y.mutate(gen), defRangeMinXYMaxXY, true);			
		} else {
			return new MutVec2(x.clone(), y.clone(), defRangeMinXYMaxXY, false);			
		}
	}
	
	public Vec2 getVal() {
		return new Vec2(x.getVal(), y.getVal()).addLocal(offset);
	}
	
	public MutVec2 clone() {
		return new MutVec2(x.clone(), y.clone(), defRangeMinXYMaxXY, mutates, offset);
	}
	
//	public static float sigmoid(float x, float min, float max, float expRange) {
//		return (float) (1/( 1 + Math.pow(Math.E,(-Math.abs(5/(expRange))*x)))) * (max - min) + min;
//	}
	
}
