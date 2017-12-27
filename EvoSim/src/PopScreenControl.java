import javafx.scene.control.Button;
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
public class PopScreenControl extends BorderPane{

	/**
	 * {@code multiTest} is a reference to the {@link MultiTest} instance this
	 * {@link PopScreenControl} instance has control over.
	 */
	private final MultiTest multiTest;
	
	/**
	 * {@code testScreen} is a reference to the {@link TestScreen} instance this
	 * {@link PopScreenControl} instance has control over. This class communicates
	 * with the {@code TestScreen} via {@link ControlFuncTest} enums.
	 */
	private final TestScreen testScreen;
	
	private final Population pop;
	
	private final PopScreen popScreen;

	/**
	 * {@code gp_controls} is the {@link GridPane} which holds all the buttons and
	 * arranges them accordingly.
	 */
	GridPane gp_controls = new GridPane();

	// Playback Controls
	Button btn_singleAction = new Button("Test");
	Button btn_1G = new Button("1 Gen");
	Button btn_10G = new Button("10 Gens");
	
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
	public PopScreenControl(TestScreen testScreen_in, MultiTest multiTest_in, PopScreen popScreen_in, Population pop_in) {
		multiTest = multiTest_in;
		testScreen = testScreen_in;
		pop = pop_in;
		popScreen = popScreen_in;

		this.setCenter(gp_controls);

		Layout.gridPane(gp_controls);

		Layout.wideButton(btn_singleAction);
		Layout.button(btn_1G);
		Layout.button(btn_10G);
		
		gp_controls.add(btn_singleAction, 0, 0, 2, 1);
		gp_controls.add(btn_1G, 0, 1);
		gp_controls.add(btn_10G, 1, 1);

		btn_singleAction.setOnAction(e -> singleAction());
		btn_1G.setOnAction(e -> do1Gen());
		btn_10G.setOnAction(e -> do10Gen());
	}

	/**
	 * Handles the labelling of "dynamic" buttons. Those are buttons which change
	 * their labelling depending on the status of the elements this
	 * {@link PlayBackControls} instance has control over.
	 */
	public void refresh() {
		
	}
	
	private void singleAction() {
		if (pop.getPopStat() == PopulationStatus.S0_NOTCREATED) { //CREATE POP
			pop.CreateRandPopulation(100);
		}
		if (pop.getPopStat() == PopulationStatus.S1_CREATED_MUTATED) { //TEST ONE BY ONE
			pop.testing();
			testScreen.manageCommand(ControlFuncTest.TEST_ONE_BY_ONE);
		}
		if (pop.getPopStat() == PopulationStatus.S2_TESTING) { //SKIP
			multiTest.addAllCreaturesToQueue();
		}
		else {
			pop.autoNextStep();
		}
	}
	
	private void do1Gen() {
		popScreen.setActive(false);
		if (pop.getPopStat() == PopulationStatus.S1_CREATED_MUTATED) {
			multiTest.testWholePop();
		}
		if (pop.getPopStat() == PopulationStatus.S3_TESTED) {
			pop.sortPopulation();
		}
		pop.nextGen();
		pop.killPercentage();
		pop.mutatePop();
		multiTest.testWholePop();
		pop.sortPopulation();
		
		popScreen.setActive(true);
		popScreen.refreshTable();
	}
	
	private void do10Gen() {
		for (int i = 0; i < 10; i++) {
			do1Gen();
		}
	}

}
