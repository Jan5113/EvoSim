package creatureCreator;
import display.Layout;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class CreatorControls extends BorderPane {

	
	private final CreatorScreen cretatorScreen;
	GridPane gp_controls = new GridPane();

	// Playback Controls
	private Label lbl_tools = new Label("Tools");
	private Button btn_select = new Button("Select");
	private Button btn_delete = new Button("Delete");
	private Button btn_joint = new Button("Joint");
	private Button btn_bone = new Button("Bone");
	private Button btn_head = new Button("Head");
	private Button btn_muscle = new Button("Muscle");
	
	public CreatorControls(CreatorScreen cretatorScreen_in) {
		cretatorScreen = cretatorScreen_in;

		this.setCenter(gp_controls);

		Layout.gridPane(gp_controls);

		Layout.labelTitle(lbl_tools);
		Layout.button(btn_select);
		Layout.button(btn_delete);
		Layout.button(btn_joint);
		Layout.button(btn_bone);
		Layout.button(btn_head);
		Layout.button(btn_muscle);

		this.setTop(lbl_tools);
		gp_controls.add(btn_select, 0, 0);
		gp_controls.add(btn_delete, 0, 1);
		gp_controls.add(btn_joint, 1, 0);
		gp_controls.add(btn_bone, 1, 1);
		gp_controls.add(btn_head, 2, 0);
		gp_controls.add(btn_muscle, 2, 1);

		btn_select.setOnAction(e -> {
			enableAllBtns();
			btn_select.setDisable(true);
			cretatorScreen.toolSelect();
		});
		btn_delete.setOnAction(e -> {
			enableAllBtns();
			btn_delete.setDisable(true);
			cretatorScreen.toolDelete();
		});
		btn_joint.setOnAction(e -> {
			enableAllBtns();
			btn_joint.setDisable(true);
			cretatorScreen.toolJoint();
		});
		btn_bone.setOnAction(e -> {
			enableAllBtns();
			btn_bone.setDisable(true);
			cretatorScreen.toolBone();
		});
		btn_head.setOnAction(e -> {
			enableAllBtns();
			btn_head.setDisable(true);
			cretatorScreen.toolHead();
		});
		btn_muscle.setOnAction(e -> {
			enableAllBtns();
			btn_muscle.setDisable(true);
			cretatorScreen.toolMuscle();
		});
	}
	
	private void enableAllBtns () {
		btn_select.setDisable(false);
		btn_delete.setDisable(false);
		btn_joint.setDisable(false);
		btn_bone.setDisable(false);
		btn_head.setDisable(false);
		btn_muscle.setDisable(false);
	}

	public void refresh() {
	}

}
