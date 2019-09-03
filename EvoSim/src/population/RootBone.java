package population;

import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import box2d.B2DBody;
import mutation.MutVec2;

public class RootBone extends Bone implements BoneParent {
    private MutVec2 rootPos;
    private ArrayList<Muscle> muscleList;

    private ArrayList<Bone> rootChildren;

    public RootBone(Vec2 headDir_in, int id_in) {
        super(headDir_in, null, id_in);
    }

    public Muscle[] getMuscleList() {
        if (muscleList == null) {
            refreshMuscleList();
        }
        Muscle[] mArray = new Muscle[muscleList.size()];
        for (int i = 0; i < muscleList.size(); i++) {
            mArray[i] = muscleList.get(i);
        }
        return mArray;
    }

    public void buildCreature(World w, ArrayList<B2DBody> creatureInstances_in, ArrayList<RevoluteJoint> revoluteJoints_in) {
        B2DBody boneBody = buildBone();
        creatureInstances_in.add(boneBody);
        boneBody.createBody(w);
        for (Bone b : rootChildren) {
            b.build(w, creatureInstances_in, revoluteJoints_in, boneBody, getLocalRoot());
        }
        for (Bone b : getChildren()) {
            b.build(w, creatureInstances_in, revoluteJoints_in, boneBody, getLocalHead());
        }

    }

    public void refreshMuscleList() {
        ArrayList<Muscle> muscleList = new ArrayList<Muscle>();
        for (Bone b : rootChildren) {
            b.getMuscles(muscleList);
        }
        getMuscles(muscleList);
    }

    public Vec2 getHeadPosition() {
        return rootPos.getVal().add(getHeadDir());
	}

    public Vec2 getBonePosition() {
        return rootPos.getVal().add(getHeadDir().mul(0.5f));
    }

    public RootBone cloneCreature() {
        return null;
    }

    public RootBone mutateCreature() {
        return null;
    }
    
}