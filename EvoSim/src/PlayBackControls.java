import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * The {@link PlayBackControls} class is a {@link BorderPane} which holds all
 * the buttons. It takes references of the elements it controls such as
 * {@link TestScreen} instances or {@link MultiTest} instances. All the commands
 * are then handed over to the correspondent reference.
 * <p>
 * <strong>NOTE:</strong> The refresh() method has to be called every frame to
 * ensure the correct labelling of some "dynamic" buttons.
 * <p>
 * This class uses the {@link Layout} class for the way of displaying the
 * elements.
 *
 */
public class PlayBackControls extends BorderPane {

	/**
	 * {@code testScreen} is a reference to the {@link TestScreen} instance this
	 * {@link PlayBackControls} instance has control over. This class communicates
	 * with the {@code TestScreen} via {@link ControlFuncTest} enums.
	 */
	private final TestScreen testScreen;
	/**
	 * {@code multiTest} is a reference to the {@link MultiTest} instance this
	 * {@link PlayBackControls} instance has control over.
	 */
	private final MultiTest multiTest;

	/**
	 * {@code gp_controls} is the {@link GridPane} which holds all the buttons
	 * and arranges them accordingly.
	 */
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

	/**
	 * Initialises the new {@link PlayBackControls} instance with references to the
	 * elements it controls.
	 * 
	 * @param testScreen_in
	 *            reference to the {@link TestScreen} instance this
	 *            {@link PlayBackControls} should have control over.
	 * @param multiTest_in
	 *            reference to the {@link MultiTest} instance this
	 *            {@link PlayBackControls} should have control over.
	 */
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
	
	/**
	 * Handles the labelling of "dynamic" buttons. Those are buttons which change
	 * their labelling depending on the status of the elements this
	 * {@link PlayBackControls} instance has control over.
	 */
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
		}

}
