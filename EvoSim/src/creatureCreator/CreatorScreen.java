package creatureCreator;

import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import box2d.B2DBody;
import display.Screen;
import level.Level;
import population.Root;
public class CreatorScreen extends Screen {
	
	private Root creatureBlueprint;
	private Vec2 mouseCoord;
	private CreatorToolMode toolMode;
	private PosID firstSelected;
	private Vec2 rootPos = new Vec2();
	private Level level;

	public CreatorScreen(Level level_in, double xRes, double yRes, float scale_in, Vec2 pos_in) {
		super(xRes, yRes, scale_in, pos_in);
		level = level_in;
		creatureBlueprint = new Root(true);
		toolMode = CreatorToolMode.NONE;
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> mousePressed(e));
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> clickEvent(e));
	}
	
	public Root getBlueprint() {
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
				if (firstSelected == null && creatureBlueprint.isEmpty()) {
					firstSelected = new PosID(0, new Vec2());
					rootPos = clickPos;
				} else if (firstSelected == null) {
					firstSelected = creatureBlueprint.selectJointNear(clickPos.add(rootPos.negate()));
				} else {
					creatureBlueprint.addBone(firstSelected.id,
						clickPos.add(rootPos.add(firstSelected.pos).negate()));
					firstSelected = null;
				}
			break;
			case ADD_HEAD:
				
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
		creatureBlueprint = new Root(false);
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
		drawGrid(level.isVertical());
		drawMarkers(level.isVertical());

		if (!creatureBlueprint.isEmpty()) {
			for (B2DBody b : creatureBlueprint.getCreatureBodies(rootPos)) {
				boolean active = toolMode == CreatorToolMode.SELECT;
				boolean selected = false;
				if (firstSelected != null) selected = firstSelected.id == b.getId();
				drawBody(b, active, selected);
			}
			for (PosID j : creatureBlueprint.getJointPos()) {
				boolean active = (toolMode == CreatorToolMode.ADD_HEAD ||
						toolMode == CreatorToolMode.ADD_BONE);
				boolean selected = false;
				if (firstSelected != null) selected = firstSelected.id == j.id;
				drawJoint(j, active, selected, rootPos);
			}
		} else if ((toolMode == CreatorToolMode.ADD_BONE 
				|| toolMode == CreatorToolMode.ADD_HEAD)
				&& firstSelected != null) {
			drawJoint(firstSelected, true, true, rootPos);
		}

		
	}

	public void changeLevel(String s) {
		System.out.println(s);
	}
}
