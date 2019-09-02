package population;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import mutation.MutVec2;

public class Bone implements BoneParent{
    private final int boneID;
    private final BoneParent parent;

    private String boneName = "";
    private BoneType boneType = BoneType.BONE;
    private MutVec2 headDir;
    private float boneArg;
    private Muscle parentMuscle;

    private ArrayList<Bone> children;

    public Bone(BoneParent parent_in, int id_in) {
        parent = parent_in;
        boneID = id_in;
    }

    public Vec2 getHeadDir() {
        return headDir.getVal();
    }


    public Vec2 getPosition() {
        return parent.getPosition().add(headDir.getVal());
    }

    public void setName(String name) {
        boneName = name;
    }

    public String getName() {
        return boneName;
    }

    public ArrayList<Muscle> getMuscles(ArrayList<Muscle> muscleList) {
        for (Bone b : children) {
            b.getMuscles(muscleList);
        }
        muscleList.add(parentMuscle);
        return muscleList;
    }
}