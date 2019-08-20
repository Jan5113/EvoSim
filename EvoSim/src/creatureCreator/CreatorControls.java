package creatureCreator;
import display.Layout;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class CreatorControls extends BorderPane {

	
	private final CreatorScreen cretatorScreen;
	GridPane gp_controls = new GridPane();

	// Playback Controls
	private Label lbl_tools = new Label("Tools");

	private Button btn_select = new Button("Select");
	private Button btn_delete = new Button("Delete All");
	private Button btn_joint = new Button("Joint");
	private Button btn_bone = new Button("Bone");
	private Button btn_head = new Button("Head");
	private Button btn_muscle = new Button("Muscle");
	private Button btn_human = new Button("Human");

	private ComboBox<String> cbb_level;
	private String[] levels = {"Flat Level", "Hurdles", "Incline", "Random"};

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
		Layout.button(btn_human);

		this.setTop(lbl_tools);
		gp_controls.add(btn_select, 0, 0);
		gp_controls.add(btn_delete, 0, 1);
		gp_controls.add(btn_joint, 1, 0);
		gp_controls.add(btn_bone, 1, 1);
		gp_controls.add(btn_head, 2, 0);
		gp_controls.add(btn_muscle, 2, 1);
		gp_controls.add(btn_human, 3, 0);

		cbb_level = new ComboBox<String>(FXCollections.observableArrayList(levels));
		gp_controls.add(cbb_level, 4, 0);
		cbb_level.getSelectionModel().selectFirst();
		cbb_level.valueProperty().addListener(e -> {
			cretatorScreen.changeLevel(cbb_level.getValue().toString());
		});
		

		btn_select.setOnAction(e -> {
			enableAllBtns();
			btn_select.setDisable(true);
			cretatorScreen.toolSelect();
		});
		btn_delete.setOnAction(e -> {
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
		btn_human.setOnAction(e -> {
			cretatorScreen.loadHuman();
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
