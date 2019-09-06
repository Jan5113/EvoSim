package mutation;
import java.io.Serializable;

import org.jbox2d.common.Vec2;


public class MutVec2 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private MutVal x;
	private MutVal y;	
	private float minLength;
	private float maxLength;
		
	public MutVec2(Vec2 val) {
		minLength = 0.1f;
		maxLength = val.mul(1.5f).length();
		x = new MutVal(-maxLength, maxLength, val.x);
		y = new MutVal(-maxLength, maxLength, val.y);
		float lenSq = x.getVal()*x.getVal()+y.getVal()*y.getVal();
		if (lenSq < minLength*minLength) {
			float f =  (float) (minLength/(Math.sqrt((double) lenSq)));
			x = x.mul(f);
			y = y.mul(f);	
		}
	}
	
	public MutVec2(int genOffset) {
		minLength = 0.1f;
		maxLength = 1f;
		x = new MutVal(-maxLength, maxLength, genOffset);
		y = new MutVal(-maxLength, maxLength, genOffset);
		float lenSq = x.getVal()*x.getVal()+y.getVal()*y.getVal();
		if (lenSq < minLength*minLength) {
			float f =  (float) (minLength/(Math.sqrt((double) lenSq)));
			x = x.mul(f);
			y = y.mul(f);	
		}
	}
	
	public MutVec2(MutVal x_in, MutVal y_in, float minlen, float maxlen) {
		x = x_in;
		y = y_in;
		minLength = minlen;
		maxLength = maxlen;
	}

	public MutVec2 mutate(int gen) {
		MutVal x_new = x.mutate(gen);
		MutVal y_new = y.mutate(gen);
		float lenSq = x_new.getVal()*x_new.getVal()+y_new.getVal()*y_new.getVal();
		if (lenSq < minLength*minLength) {
			float f =  (float) (minLength/(Math.sqrt((double) lenSq)));
			return new MutVec2(x_new.mul(f), y_new.mul(f), minLength, maxLength);	
		} else if (lenSq > maxLength*maxLength) {
			float f =  (float) (maxLength/(Math.sqrt((double) lenSq)));
			return new MutVec2(x_new.mul(f), y_new.mul(f), minLength, maxLength);	
		}
		return new MutVec2(x_new, y_new, minLength, maxLength);	
	}
	
	public Vec2 getVal() {
		return new Vec2(x.getVal(), y.getVal());
	}
	
	public MutVec2 clone() {
		return new MutVec2(x.clone(), y.clone(), minLength, maxLength);
	}
	
//	public static float sigmoid(float x, float min, float max, float expRange) {
//		return (float) (1/( 1 + Math.pow(Math.E,(-Math.abs(5/(expRange))*x)))) * (max - min) + min;
//	}
	
}
