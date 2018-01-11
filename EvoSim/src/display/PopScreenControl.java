package display;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import population.Population;
import population.PopulationStatus;
import population.PopulationTask;
import population.TestProgressBar;
import test.MultiTest;
import test.MultiTestStatus;

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
	
	private TestProgressBar testProgressBar = new TestProgressBar();
	
	private ArrayList<PopulationTask> tasks = new ArrayList<PopulationTask>();

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

		Layout.tallButton(btn_singleAction);
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
		
		switch (pop.getPopStat()) {
		case S0_NOTCREATED:
			btn_singleAction.setText("Create Population");
			btn_1G.setDisable(true);
			btn_10G.setDisable(true);
			break;
		case S1_CREATED_MUTATED:
			btn_singleAction.setText("Test Creatures");
			break;
		case S2_TESTING:
			btn_singleAction.setText("Skip Testing");
			break;
		case S3_TESTED:
			btn_singleAction.setText("Sort Population");
			break;
		case S4_SORTED:
			btn_singleAction.setText("Kill Population");
			break;
		case S5_NEWGEN:
			btn_singleAction.setText("Kill OLD");
			break;
		case S6_KILLED:
			btn_singleAction.setText("Reproduce & Mutate");
			break;
		default:
			break;
		}
		
		if (multiTest.getTestStatus() == MultiTestStatus.TESTING || multiTest.getTestStatus() == MultiTestStatus.FIXING) {
			testProgressBar.update(1.0f - ((float) multiTest.getQueue().size() / (float) pop.getPopulationSize()));
		}
		else {
			if (multiTest.getTestStatus() == MultiTestStatus.DONE) {
				System.out.println("Callback");
				multiTest.resetStatus();
				if (tasks.size() != 0 && tasks.get(0) == PopulationTask.SORT) {
					System.out.println("Handling Task " + tasks.get(0).toString());
					tasks.remove(0);
					pop.sortPopulation();
				}
				btn_singleAction.setDisable(false);
				btn_1G.setDisable(false);
				btn_10G.setDisable(false);
				popScreen.resetCenter();
				popScreen.refreshTable();
			}
			
			while (tasks.size() != 0) {
				System.out.println("Handling Task " + tasks.get(0).toString());
				switch (tasks.remove(0)) {
				case CALC_GEN:
				case COMPLETE_GEN:
					popScreen.setProgressBar(testProgressBar);
					multiTest.testWholePop();
					btn_singleAction.setDisable(true);
					btn_1G.setDisable(true);
					btn_10G.setDisable(true);
					return;
				case NEWGEN:
					pop.nextGen();
					break;
				case MUTATE:
					pop.mutatePop();
					break;
				case KILL:
					pop.killPercentage();
					break;
				case SORT:
					pop.sortPopulation();
					break;
				case NEXT_KILL_MUT:
					pop.nextGen();
					pop.killPercentage();
					pop.mutatePop();
					break;
				default:
					break;
				}
				
			}
		}
	}
	
	private void singleAction() {
		System.out.println(pop.getPopStat());
		
		switch (pop.getPopStat()) {
		case S0_NOTCREATED: //CREATE POP
			pop.CreateRandPopulation(100);
			popScreen.refreshTable();
			btn_1G.setDisable(false);
			btn_10G.setDisable(false);
			break;
		case S1_CREATED_MUTATED: //TEST ONE BY ONE
			pop.testing();
			testScreen.manageCommand(ControlFuncTest.TEST_ONE_BY_ONE);
			break;
		case S2_TESTING: //SKIP
			testScreen.manageCommand(ControlFuncTest.STOP);
			testScreen.manageCommand(ControlFuncTest.SPEED1X);
			testScreen.manageCommand(ControlFuncTest.RESETVIEW);
			tasks.add(PopulationTask.CALC_GEN);
			break;
		case S3_TESTED: // SORT
			pop.sortPopulation();
			popScreen.refreshTable();
			break;
		case S4_SORTED:
			pop.nextGen();
			pop.killPercentage();
			popScreen.refreshTable();
			break;
		case S6_KILLED:
			pop.mutatePop();
			popScreen.refreshTable();
			break;
		default:
			break;
		}
	}
	
	private void do1Gen() {
		if (pop.getPopStat() == PopulationStatus.S1_CREATED_MUTATED) {
			tasks.add(PopulationTask.COMPLETE_GEN);
			tasks.add(PopulationTask.SORT);
		}
		if (pop.getPopStat() == PopulationStatus.S3_TESTED) {
			tasks.add(PopulationTask.SORT);
		}
		tasks.add(PopulationTask.NEXT_KILL_MUT);
		tasks.add(PopulationTask.CALC_GEN);
		tasks.add(PopulationTask.SORT);
	}
	
	private void do10Gen() {
		if (pop.getPopStat() == PopulationStatus.S1_CREATED_MUTATED) {
			tasks.add(PopulationTask.COMPLETE_GEN);
		}
		if (pop.getPopStat() == PopulationStatus.S3_TESTED) {
			tasks.add(PopulationTask.SORT);
		}
		for (int i = 0; i < 10; i++) {
			tasks.add(PopulationTask.NEXT_KILL_MUT);
			tasks.add(PopulationTask.CALC_GEN);
			tasks.add(PopulationTask.SORT);
		}
	}

}
