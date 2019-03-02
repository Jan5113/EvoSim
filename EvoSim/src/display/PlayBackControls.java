package display;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import test.MultiTest;

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
	 * {@link PlayBackControls} instance has control over.
	 */
	private final TestScreen testScreen;

	/**
	 * {@code gp_controls} is the {@link GridPane} which holds all the buttons and
	 * arranges them accordingly.
	 */
	private GridPane gp_controls = new GridPane();

	// Playback Controls
	private Label lbl_playback = new Label("Playback");
	private Button btn_playpause = new Button("Play");
	private Button btn_fast = new Button(">>>");
	private Button btn_slow = new Button(">");
	private Button btn_1x = new Button("1x");
	private Button btn_toggleView = new Button("Unlock View");
	private Button btn_resetView = new Button("Reset View");
	private Button btn_toggleMuscles = new Button("Show Muscles");
	
	/**
	 * Initialises the new {@link PlayBackControls} instance with references to the
	 * elements it controls.
	 * 
	 * @param testScreen_in
	 *            reference to the {@link TestScreen} instance this
	 *            {@link PlayBackControls} should have control over.
	 */
	public PlayBackControls(TestScreen testScreen_in) {
		testScreen = testScreen_in;

		this.setCenter(gp_controls);

		Layout.gridPane(gp_controls);

		Layout.labelTitle(lbl_playback);
		Layout.wideButton(btn_playpause);
		Layout.twoThirdsButton(btn_fast);
		Layout.twoThirdsButton(btn_slow);
		Layout.twoThirdsButton(btn_1x);
		Layout.button(btn_toggleView);
		Layout.button(btn_resetView);
		Layout.button(btn_toggleMuscles);

		this.setTop(lbl_playback);
		gp_controls.add(btn_playpause, 0, 0, 3, 1);
		gp_controls.add(btn_fast, 2, 1);
		gp_controls.add(btn_slow, 0, 1);
		gp_controls.add(btn_1x, 1, 1);
		gp_controls.add(btn_toggleView, 3, 0);
		gp_controls.add(btn_resetView, 3, 1);
		gp_controls.add(btn_toggleMuscles, 4, 0);

		btn_playpause.setOnAction(e -> testScreen.playPause());
		btn_fast.setOnAction(e -> testScreen.fast());
		btn_slow.setOnAction(e -> testScreen.slow());
		btn_1x.setOnAction(e -> testScreen.resetSpeed());
		btn_toggleView.setOnAction(e -> testScreen.toggleView());
		btn_resetView.setOnAction(e -> testScreen.resetView());
		btn_toggleMuscles.setOnAction(e -> testScreen.showMuscles());
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
		
		if (testScreen.musclesEnabled())
			btn_toggleMuscles.setText("Hide Muscles");
		else
			btn_toggleMuscles.setText("Show Muscles");
	}

}
