package creatureCreator;

import display.Layout;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import mutation.MutVec2;
import population.Bone;
import population.Muscle;
import population.MutationMode;

public class CreatorControls extends BorderPane {

	
	private final CreatorScreen cretatorScreen;
	GridPane gp_controls = new GridPane();

	// Playback Controls
	private Label lbl_tools = new Label("Tools");

	private Button btn_select = new Button("Select");
	private Button btn_delete = new Button("Delete All");
	private Button btn_add = new Button("Add Bone");
	private Button btn_head = new Button("Add Head");

	private GridPane gp_creatureProp = new GridPane();
	private Label lbl_creatureProp = new Label("Creature Properties");

	private CheckBox cb_mm_1 = new CheckBox("length mutations");
	private CheckBox cb_mm_2 = new CheckBox("grow/lose bones");
	

	private GridPane gp_boneProp = new GridPane();

	private TextField tf_minLen = new TextField();
	private TextField tf_maxLen = new TextField();
	private TextField tf_minAngl = new TextField();
	private TextField tf_maxAngl = new TextField();

	private Label lbl_boneProp = new Label("Bone Properties");
	private Label lbl_minLen = new Label("min length: ");
	private Label lbl_maxLen = new Label("max length: ");
	private Label lbl_muscleProp = new Label("Muscle Properties");
	private Label lbl_minAngl = new Label("min angle: ");
	private Label lbl_maxAngl = new Label("max angle: ");

	//private Button btn_human = new Button("Human");

	private ChoiceBox<String> cbb_level = new ChoiceBox<String>();
	private String[] levels = {"Flat Level", "Hurdles", "Incline"};
	
	private Bone selectedBone;

	public CreatorControls(CreatorScreen cretatorScreen_in) {
		cretatorScreen = cretatorScreen_in;

		this.setCenter(gp_controls);

		Layout.gridPane(gp_controls);

		Layout.labelTitle(lbl_tools);
		Layout.button(btn_select);
		Layout.button(btn_delete);
		Layout.button(btn_add);
		Layout.button(btn_head);
		//Layout.button(btn_human);

		this.setTop(lbl_tools);
		gp_controls.add(btn_select, 0, 0);
		gp_controls.add(btn_delete, 0, 1);
		gp_controls.add(btn_add, 1, 0);
		gp_controls.add(btn_head, 1, 1);
		//gp_controls.add(btn_human, 3, 0);
		
		Layout.gridPane(gp_creatureProp);
		gp_creatureProp.add(lbl_creatureProp, 0, 0);
		gp_creatureProp.add(cb_mm_1, 0, 1);
		gp_creatureProp.add(cb_mm_2, 0, 2);
		gp_controls.add(gp_creatureProp, 2, 0, 1, 2);
		updateMutMode();

		Layout.gridPane(gp_boneProp);

		gp_boneProp.add(lbl_boneProp, 0, 0, 2, 1);
		gp_boneProp.add(lbl_minLen, 0, 1);
		gp_boneProp.add(lbl_maxLen, 0, 2);
		gp_boneProp.add(tf_minLen, 1, 1);
		gp_boneProp.add(tf_maxLen, 1, 2);
		
		gp_boneProp.add(lbl_muscleProp, 2, 0, 2, 1);
		gp_boneProp.add(lbl_minAngl, 2, 1);
		gp_boneProp.add(lbl_maxAngl, 2, 2);
		gp_boneProp.add(tf_minAngl, 3, 1);
		gp_boneProp.add(tf_maxAngl, 3, 2);

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
			selectedBone = null;
			updateSelection();
		});
		btn_add.setOnAction(e -> {
			enableAllBtns();
			btn_add.setDisable(true);
			cretatorScreen.toolAdd();
		});
		btn_head.setOnAction(e -> {
			enableAllBtns();
			btn_head.setDisable(true);
			cretatorScreen.toolHead();
		});
/*		btn_human.setOnAction(e -> {
			cretatorScreen.loadHuman();
		});*/

		tf_minAngl.setOnAction(e -> {
			Muscle muscle = selectedBone.getMuscle();
			muscle.setMinMaxAngl(Float.parseFloat(tf_minAngl.getText()), Float.parseFloat(tf_maxAngl.getText()));
			updateSelection();
		});

		tf_maxAngl.setOnAction(e -> {
			Muscle muscle = selectedBone.getMuscle();
			muscle.setMinMaxAngl(Float.parseFloat(tf_minAngl.getText()), Float.parseFloat(tf_maxAngl.getText()));
			updateSelection();	
		});

		tf_minLen.setOnAction(e -> {
			MutVec2 headDir = selectedBone.getHeadDirMutVec();
			headDir.setMinMaxLen(Float.parseFloat(tf_minLen.getText()),	Float.parseFloat(tf_maxLen.getText()));
			updateSelection();	
		});

		tf_maxLen.setOnAction(e -> {
			MutVec2 headDir = selectedBone.getHeadDirMutVec();
			headDir.setMinMaxLen(Float.parseFloat(tf_minLen.getText()),	Float.parseFloat(tf_maxLen.getText()));
			updateSelection();	
		});

		cb_mm_1.setOnAction(e -> {
			if (!cb_mm_1.isSelected()) cretatorScreen.getBlueprint().setMutationMode(MutationMode.M0_ONLY_MUSCLE);
			else cretatorScreen.getBlueprint().setMutationMode(MutationMode.M1_POSITION_MUT);
			updateMutMode();
		});

		cb_mm_2.setOnAction(e -> {
			if (!cb_mm_2.isSelected()) cretatorScreen.getBlueprint().setMutationMode(MutationMode.M1_POSITION_MUT);
			else cretatorScreen.getBlueprint().setMutationMode(MutationMode.M2_ALLOW_NEW_BONES);
			updateMutMode();
		});
	}
	
	private void enableAllBtns () {
		btn_select.setDisable(false);
		btn_delete.setDisable(false);
		btn_add.setDisable(false);
		btn_head.setDisable(false);
		selectedBone = null;
		updateSelection();
	}

	public void refresh() {
	}

	public void setSelectedBone(Bone b) {
		selectedBone = b;
		updateSelection();
	}

	public void updateMutMode() {
		switch (cretatorScreen.getBlueprint().getMutationMode()) {
			case M0_ONLY_MUSCLE:
				cb_mm_1.setSelected(false);
				cb_mm_2.setSelected(false);
				cb_mm_2.setDisable(true);
				break;
			case M1_POSITION_MUT:
				cb_mm_1.setSelected(true);
				cb_mm_2.setSelected(false);
				cb_mm_2.setDisable(false);
				break;
			case M2_ALLOW_NEW_BONES:
				cb_mm_1.setSelected(true);
				cb_mm_2.setSelected(true);
				cb_mm_2.setDisable(false);				
				break;
			default:
				break;
		} 
	}

	public void updateSelection() {
		if (selectedBone != null) {
			Muscle muscle = selectedBone.getMuscle();
			MutVec2 headDir = selectedBone.getHeadDirMutVec();

			tf_minLen.setText(Float.toString(headDir.getMinLen()));
			tf_maxLen.setText(Float.toString(headDir.getMaxLen()));
			if (muscle != null) {
				
				tf_minAngl.setDisable(false);
				tf_maxAngl.setDisable(false);
				tf_minAngl.setText(Float.toString(muscle.getMinAngl()));
				tf_maxAngl.setText(Float.toString(muscle.getMaxAngl()));
			} else {
				tf_minAngl.setDisable(true);
				tf_maxAngl.setDisable(true);
				tf_minAngl.setText("-");
				tf_maxAngl.setText("-");
			}
			if (!gp_controls.getChildren().contains(gp_boneProp)) {
				gp_controls.add(gp_boneProp, 3, 0, 1, 2);
			}
		} else {
			System.out.println("rm gp");
			gp_controls.getChildren().remove(gp_boneProp);
		}
	}

}
