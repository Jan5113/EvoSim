import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class PlayBackControls extends BorderPane {

	private final TestScreen testScreen;
	private final MultiTest multiTest;

	GridPane gp_controls = new GridPane();

	// Playback Controls
	Label lbl_playback = new Label("Playback");
	Button btn_playpause = new Button("Play");
	Button btn_fast = new Button(">>>");
	Button btn_slow = new Button(">");
	Button btn_1x = new Button("1x");
	Button btn_toggleView = new Button("Unlock View");
	Button btn_resetView = new Button("Reset View");
	Button btn_test = new Button("TESTMULTICORE");

	public PlayBackControls(TestScreen testScreen_in, MultiTest multiTest_in) {
		testScreen = testScreen_in;
		multiTest = multiTest_in;

		this.setCenter(gp_controls);

		Layout.gridPane(gp_controls);

		Layout.labelTitle(lbl_playback);
		Layout.wideButton(btn_playpause);
		Layout.TwoThirdsButton(btn_fast);
		Layout.TwoThirdsButton(btn_slow);
		Layout.TwoThirdsButton(btn_1x);
		Layout.button(btn_toggleView);
		Layout.button(btn_resetView);
		
		Layout.button(btn_test);
		
		gp_controls.add(lbl_playback, 0, 0, 3, 1);
		gp_controls.add(btn_playpause, 0, 1, 3, 1);
		gp_controls.add(btn_fast, 2, 2);
		gp_controls.add(btn_slow, 0, 2);
		gp_controls.add(btn_1x, 1,2);
		gp_controls.add(btn_toggleView, 3,1);
		gp_controls.add(btn_resetView, 3,2);
		
		gp_controls.add(btn_test, 4, 1);
		

		btn_playpause.setOnAction(e -> testScreen.manageCommand(ControlFuncTest.PLAYPAUSE));
		btn_fast.setOnAction(e -> testScreen.manageCommand(ControlFuncTest.FAST));
		btn_slow.setOnAction(e -> testScreen.manageCommand(ControlFuncTest.SLOW));
		btn_1x.setOnAction(e -> testScreen.manageCommand(ControlFuncTest.SPEED1X));
		btn_toggleView.setOnAction(e -> testScreen.manageCommand(ControlFuncTest.TOGGLEVIEW));
		btn_resetView.setOnAction(e -> testScreen.manageCommand(ControlFuncTest.RESETVIEW));
		btn_test.setOnAction(e -> testMultiCore());
	}
	
	public void refresh() {
		if (testScreen.isTestRunning())
			btn_playpause.setText("Pause");
		else
			btn_playpause.setText("Play");

		if (testScreen.isViewLocked())
			btn_toggleView.setText("Unlock View");
		else
			btn_toggleView.setText("Lock View");
	}
	
	public void testMultiCore() {
		multiTest.addAllCreaturesToQueue();
		
		multiTest.startThreads();
		multiTest.addAllCreaturesToQueue();
		
		multiTest.startThreads();
		multiTest.addAllCreaturesToQueue();
		
		multiTest.startThreads();
		multiTest.addAllCreaturesToQueue();
		
		multiTest.startThreads();
		multiTest.addAllCreaturesToQueue();
		
		multiTest.startThreads();
	}

}
