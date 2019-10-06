package creatureCreator;

import challenge.Challenge;
import display.Layout;
import display.PopScreenControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import level.Level;
import level.LevelSettings;
import level.LevelStyle;
import mutation.MutVec2;
import population.Bone;
import population.BoneType;
import population.Muscle;
import population.MutationMode;

public class CreatorControls extends BorderPane {

	
	private final CreatorScreen creatorScreen;
	private PopScreenControl popScreenCtrl;
	private Challenge challenge;
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

	private GridPane gp_levelProp = new GridPane();
	private Label lbl_levelProp = new Label("Level Properties");

	private ChoiceBox<LevelStyle> cb_level = new ChoiceBox<LevelStyle>();
	private ObservableList<LevelStyle> levelStyles = FXCollections.observableArrayList(
		LevelStyle.FLAT,
		LevelStyle.INCLINE,
		LevelStyle.HURDLES,
		LevelStyle.JUMP,
		LevelStyle.CLIMB
	);
	private TextField tf_incline = new TextField();
	private TextField tf_climbWidth = new TextField();
	private TextField tf_hurdleDist = new TextField();
	private TextField tf_hurdleHeight = new TextField();
	private TextField tf_hurdleWidth = new TextField();

	private Label lbl_incline = new Label("incline");
	private Label lbl_climbWidth = new Label("climb width: ");
	private Label lbl_hurdleDist = new Label("hurdle dist: ");
	private Label lbl_hurdleHeight = new Label("hurdle height: ");
	private Label lbl_hurdleWidth = new Label("hurdle width: ");

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
	
	private Bone selectedBone;

	public CreatorControls(CreatorScreen creatorScreen_in) {
		creatorScreen = creatorScreen_in;

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

		Layout.gridPane(gp_levelProp);
		gp_levelProp.add(lbl_levelProp, 0, 0);
		gp_levelProp.add(cb_level, 0, 1);
		gp_levelProp.add(lbl_incline, 1, 0);
		gp_levelProp.add(lbl_climbWidth, 1, 1);
		gp_levelProp.add(tf_incline, 2, 0);
		gp_levelProp.add(tf_climbWidth, 2, 1);
		gp_levelProp.add(lbl_hurdleDist, 3, 0);
		gp_levelProp.add(lbl_hurdleHeight, 3, 1);
		gp_levelProp.add(tf_hurdleDist, 4, 0);
		gp_levelProp.add(tf_hurdleHeight, 4, 1);
		gp_levelProp.add(lbl_hurdleWidth, 5, 0);
		gp_levelProp.add(tf_hurdleWidth, 6, 0);
		tf_incline.setPrefWidth(50);
		tf_climbWidth.setPrefWidth(50);
		tf_hurdleDist.setPrefWidth(50);
		tf_hurdleHeight.setPrefWidth(50);
		tf_hurdleWidth.setPrefWidth(50);
		
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

		cb_level.setItems(levelStyles);
		cb_level.valueProperty().addListener(e -> {
			creatorScreen.changeLevel(cb_level.getValue());
		});
		updateLevel();
		

		btn_select.setOnAction(e -> {
			enableAllBtns();
			btn_select.setDisable(true);
			creatorScreen.toolSelect();
		});
		btn_delete.setOnAction(e -> {
			creatorScreen.toolDelete();
			selectedBone = null;
			updateSelection();
		});
		btn_add.setOnAction(e -> {
			enableAllBtns();
			btn_add.setDisable(true);
			creatorScreen.toolAdd();
		});
		btn_head.setOnAction(e -> {
			enableAllBtns();
			btn_head.setDisable(true);
			creatorScreen.toolHead();
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
			if (!cb_mm_1.isSelected()) creatorScreen.getBlueprint().setMutationMode(MutationMode.M0_ONLY_MUSCLE);
			else creatorScreen.getBlueprint().setMutationMode(MutationMode.M1_POSITION_MUT);
			updateMutMode();
		});

		cb_mm_2.setOnAction(e -> {
			if (!cb_mm_2.isSelected()) creatorScreen.getBlueprint().setMutationMode(MutationMode.M1_POSITION_MUT);
			else creatorScreen.getBlueprint().setMutationMode(MutationMode.M2_ALLOW_NEW_BONES);
			updateMutMode();
		});

		

		tf_incline.setOnAction(e -> {
			Level level = creatorScreen.getLevel();
			level.getLevelSettings().incline = Float.parseFloat(tf_incline.getText());
			updateLevel();	
		});
		tf_climbWidth.setOnAction(e -> {
			Level level = creatorScreen.getLevel();
			level.getLevelSettings().climbWidth = Float.parseFloat(tf_climbWidth.getText());
			updateLevel();	
		});
		tf_hurdleDist.setOnAction(e -> {
			Level level = creatorScreen.getLevel();
			level.getLevelSettings().hurdleDist = Float.parseFloat(tf_hurdleDist.getText());
			updateLevel();	
		});
		tf_hurdleHeight.setOnAction(e -> {
			Level level = creatorScreen.getLevel();
			level.getLevelSettings().hurdleHeight = Float.parseFloat(tf_hurdleHeight.getText());
			updateLevel();	
		});
		tf_hurdleWidth.setOnAction(e -> {
			Level level = creatorScreen.getLevel();
			level.getLevelSettings().hurdleWidth = Float.parseFloat(tf_hurdleWidth.getText());
			updateLevel();	
		});
	}
	
	public void setPopScreenControl(PopScreenControl popscrnctrl) {
		popScreenCtrl = popscrnctrl;
	}
	
	public void setChallenge(Challenge chal_in) {
		challenge = chal_in;
		updateChallengeDisplay();
	}
	
	public void updateChallengeDisplay() {
		if (challenge == null) {
			lbl_tools.setText("Tools");
			updateMutMode();
			updateSelection();
			toggleFields(true);
		} else {
			float cost = creatorScreen.getCurrentCost();
			lbl_tools.setText("Tools                      Challenge: " + challenge.challengeName + " (cost: " +
						Double.toString(Math.floor((double) cost)) + "/" + challenge.maxCost +")");
			creatorScreen.getBlueprint().setMutationMode(MutationMode.M0_ONLY_MUSCLE);
			updateMutMode();
			updateSelection();
			toggleFields(false);
			if (cost > challenge.maxCost) {
				popScreenCtrl.disableMultiButton();
			} else {
				popScreenCtrl.enableMultiButton();
			}
		}
		
	}
	
	private void toggleFields(boolean flag) {
		cb_mm_1.setDisable(!flag);
		cb_mm_2.setDisable(!flag);

		cb_level.setDisable(!flag);
		tf_incline.setDisable(!flag);
		tf_climbWidth.setDisable(!flag);
		tf_hurdleDist.setDisable(!flag);
		tf_hurdleHeight.setDisable(!flag);
		tf_hurdleWidth.setDisable(!flag);
	}
	
	private void enableAllBtns () {
		btn_select.setDisable(false);
		btn_delete.setDisable(false);
		btn_add.setDisable(false);
		btn_head.setDisable(false);
		selectedBone = null;
		updateSelection();
		deselectLevel();
	}

	public void refresh() {
	}

	public void setSelectedBone(Bone b) {
		selectedBone = b;
		updateSelection();
	}

	public void selectLevel() {
		gp_controls.getChildren().remove(gp_boneProp);
		if (!gp_controls.getChildren().contains(gp_levelProp)) {
			gp_controls.add(gp_levelProp, 3, 0, 1, 2);
		}
	}

	public void deselectLevel() {
		gp_controls.getChildren().remove(gp_levelProp);
	}

	public void updateMutMode() {
		switch (creatorScreen.getBlueprint().getMutationMode()) {
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
			if (challenge != null && muscle != null && selectedBone.getBoneType() != BoneType.HEAD) {
				tf_minAngl.setDisable(false);
				tf_maxAngl.setDisable(false);
				tf_minLen.setDisable(true);
				tf_maxLen.setDisable(true);
				tf_minLen.setText("-");
				tf_maxLen.setText("-");
				tf_minAngl.setText(Float.toString(muscle.getMinAngl()));
				tf_maxAngl.setText(Float.toString(muscle.getMaxAngl()));
			} else if (muscle == null) {
				tf_minAngl.setDisable(true);
				tf_maxAngl.setDisable(true);
				tf_minLen.setDisable(challenge!=null);
				tf_maxLen.setDisable(challenge!=null);
				tf_minAngl.setText("-");
				tf_maxAngl.setText("-");
				if (challenge!=null) {
					tf_minLen.setText("-");
					tf_maxLen.setText("-");
				}
			} else if (selectedBone.getBoneType() == BoneType.HEAD) {
				tf_minAngl.setDisable(true);
				tf_maxAngl.setDisable(true);
				tf_minLen.setDisable(true);
				tf_maxLen.setDisable(true);
				tf_minLen.setText("-");
				tf_maxLen.setText("-");
				tf_minAngl.setText("-");
				tf_maxAngl.setText("-");
			} else {
				tf_minAngl.setDisable(false);
				tf_maxAngl.setDisable(false);
				tf_minLen.setDisable(false);
				tf_maxLen.setDisable(false);
				tf_minAngl.setText(Float.toString(muscle.getMinAngl()));
				tf_maxAngl.setText(Float.toString(muscle.getMaxAngl()));
			}
			if (!gp_controls.getChildren().contains(gp_boneProp)) {
				gp_controls.add(gp_boneProp, 3, 0, 1, 2);
			}
			deselectLevel();
		} else {
			gp_controls.getChildren().remove(gp_boneProp);
		}
	}

	public void updateLevel() {
		LevelSettings levelSettings = creatorScreen.getLevel().getLevelSettings();
		cb_level.setValue(creatorScreen.getLevel().getLevelStyle());
		tf_incline.setText(Float.toString(levelSettings.incline));
		tf_climbWidth.setText(Float.toString(levelSettings.climbWidth));
		tf_hurdleDist.setText(Float.toString(levelSettings.hurdleDist));
		tf_hurdleHeight.setText(Float.toString(levelSettings.hurdleHeight));
		tf_hurdleWidth.setText(Float.toString(levelSettings.hurdleWidth));
	}

}
