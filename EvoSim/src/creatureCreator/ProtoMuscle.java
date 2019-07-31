package creatureCreator;

public class ProtoMuscle {
	public final int IDBoneA;
	public final int IDBoneB;
	public final int IDJoint;
	public final int ID;
	public final ProtoCreature pc; // Parent Creature
	
	public float torque = 5.0f;
	public float angleMin = (float) (-Math.PI);
	public float angleMax = (float) (3*Math.PI);
	
	public ProtoMuscle(int joint, int boneA, int boneB, int id_in, ProtoCreature pc_in) {
		IDBoneA = boneA;
		IDBoneB = boneB;
		IDJoint = joint;
		ID = id_in;
		pc = pc_in;
	}
	
}
