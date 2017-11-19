import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class PlayBackControls extends BorderPane {

	private final TestScreen testManager;

	GridPane gp_controls = new GridPane();

	// Playback Controls
	Label lbl_playback = new Label("Playback");
	Button btn_playpause = new Button("Play");
	Button btn_fast = new Button(">>>");
	Button btn_slow = new Button(">");

	// Evo Controls

	public PlayBackControls(TestScreen testManager_in) {
		testManager = testManager_in;

		this.setCenter(gp_controls);

		Layout.gridPane(gp_controls);

		Layout.labelTitle(lbl_playback);
		Layout.wideButton(btn_playpause);
		Layout.button(btn_fast);
		Layout.button(btn_slow);
		
		gp_controls.add(lbl_playback, 0, 0, 2, 1);
		gp_controls.add(btn_playpause, 0, 1, 2, 1);
		gp_controls.add(btn_fast, 1, 2);
		gp_controls.add(btn_slow, 0, 2);

		btn_playpause.setOnAction(e -> testManager.manageCommand(ControlFuncTest.PLAYPAUSE));
		btn_fast.setOnAction(e -> testManager.manageCommand(ControlFuncTest.FAST));
		btn_slow.setOnAction(e -> testManager.manageCommand(ControlFuncTest.SLOW));
	}
	
	public void refresh() {
		if (testManager.testIsRunning())
			btn_playpause.setText("Pause");
		else
			btn_playpause.setText("Play");
	}

}
