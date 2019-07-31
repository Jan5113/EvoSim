package creatureCreator;

import org.jbox2d.common.Vec2;
import display.Screen;
import javafx.scene.paint.Color;
public class CreatorScreen extends Screen {
	
	private ProtoCreature creatureBlueprint;

	public CreatorScreen(double xRes, double yRes, float scale_in, Vec2 pos_in) {
		super(xRes, yRes, scale_in, pos_in);
		creatureBlueprint = new ProtoCreature(true);
	}
	
	public ProtoCreature getBlueprint() {
		return creatureBlueprint;
	}

	public void toolSelect() {
		// TODO Auto-generated method stub
		
	}

	public void toolDelete() {
		
	}

	public void toolJoint() {
		
	}

	public void toolBone() {
		
	}

	public void toolHead() {
		
	}

	public void toolMuscle() {
		
	}
	
	public void refresh() {
		clearScreen(true);
		drawRect(0, -10, 100, 10, 0, Color.GREENYELLOW, true);
		drawRect(-1, 1, 1, 1, 0, Color.LIGHTBLUE, true);

		for (ProtoJoint j : creatureBlueprint.jointDefList) {
			drawProtoJoint(j);
		}
		for (ProtoBone b : creatureBlueprint.boneDefList) {
			drawProtoBone(b);
		}
		for (ProtoMuscle m : creatureBlueprint.muscleDefList) {
			drawProtoMuscle(m);
		}
	}
}
