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
	private Button btn_add = new Button("Add Bone");
	private Button btn_head = new Button("Add Head");
	//private Button btn_human = new Button("Human");

	private ComboBox<String> cbb_level;
	private String[] levels = {"Flat Level", "Hurdles", "Incline", "Random"};

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
	}
	
	private void enableAllBtns () {
		btn_select.setDisable(false);
		btn_delete.setDisable(false);
		btn_add.setDisable(false);
		btn_head.setDisable(false);
	}

	public void refresh() {
	}

}
