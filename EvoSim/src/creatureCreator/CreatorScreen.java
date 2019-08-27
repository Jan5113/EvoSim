package creatureCreator;

import javafx.scene.input.MouseEvent;
import org.jbox2d.common.Vec2;
import display.Screen;
import javafx.scene.paint.Color;
public class CreatorScreen extends Screen {
	
	private ProtoCreature creatureBlueprint;
	private Vec2 mouseCoord;
	private CreatorToolMode toolMode;
	private int firstSelected = -1;

	public CreatorScreen(double xRes, double yRes, float scale_in, Vec2 pos_in) {
		super(xRes, yRes, scale_in, pos_in);
		creatureBlueprint = new ProtoCreature(true);
		toolMode = CreatorToolMode.NONE;
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> mousePressed(e));
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> clickEvent(e));
	}
	
	public ProtoCreature getBlueprint() {
		return creatureBlueprint;
	}

	private void mousePressed(MouseEvent e) {
		mouseCoord = new Vec2((float) e.getX(), (float) e.getY());
	}

	private void clickEvent(MouseEvent e) {
		if (Math.abs(mouseCoord.x - (float) e.getX()) > 6  ||
		 Math.abs(mouseCoord.y - (float) e.getY()) > 6) return;
		Vec2 clickPos = camera.coordPixelsToWorld(e.getX(), e.getY());
		System.out.println("x: " + clickPos.x + ", y: " + clickPos.y);

		switch (toolMode) {
			case NONE:
			break;
			case ADD_BONE:
				if (firstSelected == -1) {
					firstSelected = creatureBlueprint.selectJointNear(clickPos);
				} else {
					int secondSelected = creatureBlueprint.selectJointNear(clickPos);
					if (firstSelected == secondSelected) break;
					creatureBlueprint.addBone(creatureBlueprint.selectJointNear(clickPos), firstSelected);
					firstSelected = -1;
				}
			break;
			case ADD_JOINT:
				if (clickPos.x > 0f || clickPos.x < -2f || clickPos.y > 2f || clickPos.y < 0f) return;
				creatureBlueprint.addJoint(clickPos);
			break;
			case ADD_HEAD:
				creatureBlueprint.addHead(creatureBlueprint.selectJointNear(clickPos));
			break;
			case ADD_MUSCLE:
				if (firstSelected == -1) {
					firstSelected = creatureBlueprint.selectBoneNear(clickPos);
				} else {
					int secondSelected = creatureBlueprint.selectBoneNear(clickPos);
					if (firstSelected == secondSelected) break;
					int j = creatureBlueprint.getCommonJoint(secondSelected, firstSelected);
					if (j == -1) {
						System.err.println("No common joint found!");
						firstSelected = -1;
					} else {
						creatureBlueprint.addMuscle(j, secondSelected, firstSelected);
						firstSelected = -1;
					}
				}
			break;
			case SELECT:
			break;
			default:
		}
	}

	public void toolSelect() {
		toolMode = CreatorToolMode.SELECT;
		firstSelected = -1;
	}

	public void toolDelete() {
		creatureBlueprint.deleteAll();
		firstSelected = -1;
		refresh();
	}

	public void toolJoint() {
		toolMode = CreatorToolMode.ADD_JOINT;
		firstSelected = -1;
	}

	public void toolBone() {
		toolMode = CreatorToolMode.ADD_BONE;
		firstSelected = -1;
	}

	public void toolHead() {
		toolMode = CreatorToolMode.ADD_HEAD;
		firstSelected = -1;
	}

	public void toolMuscle() {
		toolMode = CreatorToolMode.ADD_MUSCLE;
		firstSelected = -1;
	}

	public void loadHuman() {
		creatureBlueprint.deleteAll();
		creatureBlueprint.createHumanoid();
		refresh();
	}
	
	public void refresh() {
		clearScreen(true);
		drawRect(0, -10, 100, 10, 0, Color.GREENYELLOW, true);
		drawRect(-1, 1, 1, 1, 0, Color.LIGHTBLUE, true);

		for (ProtoJoint j : creatureBlueprint.jointDefList) {
			drawProtoJoint(j, firstSelected == j.ID && toolMode == CreatorToolMode.ADD_BONE);
		}
		for (ProtoBone b : creatureBlueprint.boneDefList) {
			drawProtoBone(b, firstSelected == b.ID && toolMode == CreatorToolMode.ADD_MUSCLE);
		}
		for (ProtoMuscle m : creatureBlueprint.muscleDefList) {
			drawProtoMuscle(m);
		}
	}

	public void changeLevel(String s) {
		System.out.println(s);
	}
}
