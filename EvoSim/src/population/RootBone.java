package population;

import org.jbox2d.common.Vec2;

import mutation.MutVec2;

public class RootBone extends Bone implements BoneParent {
    private MutVec2 rootPos;

    public RootBone(BoneParent parent_in, int id_in) {
        super(parent_in, id_in);
    }

    @Override
    public Vec2 getPosition() {
        return rootPos.getVal();
	}
    
}