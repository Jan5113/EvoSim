package mutation;
import org.jbox2d.common.Vec2;

public class MutVec2{
	private float[] defRangeMinXYMaxXY = {-2, 0, 0, 2};
	
	private MutVal x;
	private MutVal y;	

	public MutVec2(Vec2 val, float[] minXY_maxXY) {
		defRangeMinXYMaxXY = minXY_maxXY;
		x = new MutVal(defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2], val.x);
		y = new MutVal(defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3], val.y);
	}

	public MutVec2(float[] minXY_maxXY) {
		defRangeMinXYMaxXY = minXY_maxXY;
		x = new MutVal(defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2]);
		y = new MutVal(defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3]);
	}
	
	public MutVec2(Vec2 val) {
		x = new MutVal(defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2], val.x);
		y = new MutVal(defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3], val.y);
	}
	
	public MutVec2() {
		x = new MutVal(defRangeMinXYMaxXY[0], defRangeMinXYMaxXY[2]);
		y = new MutVal(defRangeMinXYMaxXY[1], defRangeMinXYMaxXY[3]);
	}
	
	public MutVec2(MutVal x_in, MutVal y_in, float[] minXY_maxXY) {
		x = x_in;
		y = y_in;
	}
	
	public MutVec2 mutate(int gen) {
		return new MutVec2(x.mutate(gen), y.mutate(gen), defRangeMinXYMaxXY);
	}
	
	public Vec2 getVal() {
		return new Vec2(x.getVal(), y.getVal());
	}
	
	public MutVec2 clone() {
		return new MutVec2(x.clone(), y.clone(), defRangeMinXYMaxXY);
	}
	
//	public static float sigmoid(float x, float min, float max, float expRange) {
//		return (float) (1/( 1 + Math.pow(Math.E,(-Math.abs(5/(expRange))*x)))) * (max - min) + min;
//	}
	
}
