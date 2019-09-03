package creatureCreator;

import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import box2d.B2DBody;
import display.Screen;
import level.Level;
import population.RootBone;
public class CreatorScreen extends Screen {
	
	private RootBone creatureBlueprint;
	private Vec2 mouseCoord;
	private CreatorToolMode toolMode;
	private PosID firstSelected;
	private Level level;

	public CreatorScreen(Level level_in, double xRes, double yRes, float scale_in, Vec2 pos_in) {
		super(xRes, yRes, scale_in, pos_in);
		level = level_in;
		creatureBlueprint = new RootBone(0);
		toolMode = CreatorToolMode.NONE;
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> mousePressed(e));
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> clickEvent(e));
	}
	
	public RootBone getBlueprint() {
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
				

				/*
				BONE
				if (firstSelected == -1) {
					firstSelected = creatureBlueprint.selectJointNear(clickPos);
				} else {
					int secondSelected = creatureBlueprint.selectJointNear(clickPos);
					if (firstSelected == secondSelected) break;
					creatureBlueprint.addBone(creatureBlueprint.selectJointNear(clickPos), firstSelected);
					firstSelected = -1;
				}
				JOINT
				if (clickPos.x > 0f || clickPos.x < -2f || clickPos.y > 2f || clickPos.y < 0f) return;
				creatureBlueprint.addJoint(clickPos);
				
				MUSCLE
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
				*/
			break;
			case ADD_HEAD:
				creatureBlueprint.addHead(firstSelected.id, 0.2f);
			break;
			case SELECT:
			break;
			default:
		}
	}

	public void toolSelect() {
		toolMode = CreatorToolMode.SELECT;
		firstSelected = null;
	}

	public void toolDelete() {
		creatureBlueprint = new RootBone();
		firstSelected = null;
		refresh();
	}

	public void toolAdd() {
		toolMode = CreatorToolMode.ADD_BONE;
		firstSelected = null;
	}

	public void toolHead() {
		toolMode = CreatorToolMode.ADD_HEAD;
		firstSelected = null;
	}

	/*public void loadHuman() {
		creatureBlueprint.deleteAll();
		creatureBlueprint.createHumanoid();
		refresh();
	}*/
	
	public void refresh() {
		clearScreen(true);
		ArrayList<B2DBody> levelBodies = level.getLevel();
		for (B2DBody b : levelBodies) {
			drawBody(b);
		}
/*
		for (ProtoJoint j : creatureBlueprint.jointDefList) {
			drawProtoJoint(j, firstSelected == j.ID && toolMode == CreatorToolMode.ADD_BONE);
		}
		for (ProtoMuscle m : creatureBlueprint.muscleDefList) {
			drawProtoMuscle(m);
		}*/
		drawInfoNGrind(level.isVertical());
	}

	public void changeLevel(String s) {
		System.out.println(s);
	}
}
