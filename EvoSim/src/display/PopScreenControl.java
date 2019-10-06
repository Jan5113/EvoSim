package display;

import java.util.ArrayList;

import creatureCreator.CreatorScreen;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import main.Main;
import population.Population;
import population.PopulationStatus;
import population.PopulationTask;
import population.TestProgressBar;
import test.MultiTest;
import test.MultiTestStatus;

/**
 * The {@link PopScreenControl} class is a {@link BorderPane} which holds all
 * the buttons for the control of {@link PopScreen} and {@link Population}. It
 * takes references of the elements it controls such as the {@link PopScreen}
 * instance or {@link MultiTest} instances. It handles the {@link Population}
 * and with it the mutation procedure. Clicking a button adds a
 * {@link PopulationTask} to a task queue and is then executed.
 * <p>
 * <strong>NOTE:</strong> The refresh() method has to be called every frame to
 * ensure the correct labelling of some "dynamic" buttons and the execution of
 * the {@link PopulationTask}s.
 * <p>
 * This class uses the {@link Layout} class for the way of displaying the
 * elements.
 *
 */
public class PopScreenControl extends BorderPane{

	private final Main main;
	private final MultiTest multiTest;
	private final TestScreen testScreen;
	private final CreatorScreen creatorScreen;
	private final Population pop;
	private final PopScreen popScreen;
	private TestProgressBar testProgressBar;
	private ArrayList<PopulationTask> tasks = new ArrayList<PopulationTask>();
	private GridPane gp_controls = new GridPane();

	// Playback Controls
	private Button btn_singleAction = new Button("Test");
	private Button btn_1G = new Button("1 Gen");
	private Button btn_10G = new Button("10 Gens");
	
	/**
	 * Initialises the new {@link PopScreenControl} instance with references to the
	 * elements it controls. The layout is initialised and EventListeners are added.
	 * 
	 * @param testScreen_in
	 *            reference to the {@link TestScreen} instance this
	 *            {@link PopScreenControl} should have control over.
	 * @param multiTest_in
	 *            reference to the {@link MultiTest} instance this
	 *            {@link PopScreenControl} should have control over.
	 * @param popScreen_in
	 *            reference to the {@link PopScreen} instance this
	 *            {@link PopScreenControl} should have control over.
	 * @param pop_in
	 *            reference to the main {@link Population} this
	 *            {@link PopScreenControl} controls.
	 */
	public PopScreenControl(Main main_in, TestScreen testScreen_in, MultiTest multiTest_in, 
			CreatorScreen creatorScreen_in, PopScreen popScreen_in, Population pop_in) {
		multiTest = multiTest_in;
		testScreen = testScreen_in;
		pop = pop_in;
		popScreen = popScreen_in;
		main = main_in;
		creatorScreen = creatorScreen_in;
		
		testProgressBar = new TestProgressBar();

		this.setCenter(gp_controls);

		Layout.gridPane(gp_controls);

		Layout.tallwideButton(btn_singleAction);
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
	 * Handles the labelling of "dynamic" buttons and execution of
	 * {@link PopulationTask}s from the {@code tasks} queue. All the tasks currently
	 * in the queue are executed (in the order they were added) and then removed
	 * from the queue.
	 * <p>
	 * If a calculation using {@link MultiTest} is running, no more tasks are being
	 * executed.
	 * <p>
	 * If a calculation using {@link MultiTest} is done, the {@link TestProgressBar}
	 * is removed automatically from the {@link PopScreen} referenced during
	 * initialisation.
	 */
	public void refresh() {
		
		switch (pop.getPopStat()) {
		case S000_NOBLUEPRINT:
			btn_singleAction.setText("Create Creature");
			btn_1G.setDisable(true);
			btn_10G.setDisable(true);
			break;
		case S00_CREATOR:
			btn_singleAction.setText("Save Creature");
			btn_1G.setDisable(true);
			btn_10G.setDisable(true);
			break;
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
			testProgressBar.update(multiTest.getQueue().size(), tasks);
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
				btn_1G.setText("1 Gen");
				btn_10G.setText("10 Gen");
				popScreen.removeProgressBar();
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
//					btn_1G.setDisable(true);
//					btn_10G.setDisable(true);
					btn_1G.setText("+ 1 Gen");
					btn_10G.setText("+ 10 Gen");
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
	
	/**
	 * This method is called, when the multi-purpose button had been pressed. It
	 * automatically looks for the next step the {@link Population} has to go
	 * through and executes the code right away or adds it to the {@code task}
	 * queue.
	 */
	private void singleAction() {		
		switch (pop.getPopStat()) {
		case S00_CREATOR: //SAVE CREATURE
			main.closeCreator();
			pop.saveCreature(creatorScreen.getBlueprint());
			testScreen.setTestLevel(pop.getLevel());
			break;
		case S0_NOTCREATED: //CREATE POP
			pop.CreateRandPopulation(100);
			testProgressBar.setPopSize(pop.getPopulationSize());
			popScreen.refreshTable();
			btn_1G.setDisable(false);
			btn_10G.setDisable(false);
			break;
		case S1_CREATED_MUTATED: //TEST ONE BY ONE
			pop.testing();
			testScreen.testOneByOne();;
			break;
		case S2_TESTING: //SKIP
			testScreen.stop();
			testScreen.resetSpeed();
			testScreen.resetView();
			tasks.add(PopulationTask.CALC_GEN);
			testProgressBar.setGens(tasks);
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
	
	public void disableMultiButton() {
		btn_singleAction.setDisable(true);
	}
	
	public void enableMultiButton() {
		btn_singleAction.setDisable(false);
	}
	
	/**
	 * This method is called when one whole generation should be calculated.
	 * {@link PopulationTask} commands are added to the {@code tasks} queue to
	 * calculate an entire generation. The current generation is completed and the
	 * commands for mutations, sorting etc. is added in the correct order to the
	 * queue.
	 * <p>
	 * The {@link TestProgressBar} is shown by executing this method.
	 */
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
		testProgressBar.setGens(tasks);
	}
	
	/**
	 * This method is called when 10 whole generations should be calculated.
	 * {@link PopulationTask} commands are added to the {@code tasks} queue to
	 * calculate 10 entire generations. The current generation is completed and the
	 * commands for mutations, sorting etc. is added in the correct order to the
	 * queue.
	 * <p>
	 * The {@link TestProgressBar} is shown by executing this method.
	 */
	private void do10Gen() {
		if (pop.getPopStat() == PopulationStatus.S1_CREATED_MUTATED) {
			tasks.add(PopulationTask.COMPLETE_GEN);
			tasks.add(PopulationTask.SORT);
		}
		if (pop.getPopStat() == PopulationStatus.S3_TESTED) {
			tasks.add(PopulationTask.SORT);
		}
		for (int i = 0; i < 10; i++) {
			tasks.add(PopulationTask.NEXT_KILL_MUT);
			tasks.add(PopulationTask.CALC_GEN);
			tasks.add(PopulationTask.SORT);
		}
		testProgressBar.setGens(tasks);
	}

}
