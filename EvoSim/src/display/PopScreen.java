package display;

import org.jbox2d.common.Vec2;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import population.Creature;
import population.Population;
import population.PopulationStatus;
import population.TestProgressBar;
import test.MultiTest;

/**
 * The {@link PopScreen} class is a {@link BorderPane} which holds a list of all
 * {@link Creature} instances in a {@link Population}. It displays their ID and
 * fitness. A small preview is showed when hovering over a {@link Creature} in
 * the list; a double click tests is in the main {@link TestScreen}. It takes
 * references of the elements it controls such as the main {@link TestScreen}
 * instance and the {@link Population} it lists.
 * <p>
 * When a {@link MultiTest} calculation is performed, the table can be replaced
 * by a {@link TestProgressBar} and has to be reset after the calculation is
 * done.
 * <p>
 * <strong>NOTE:</strong> The refresh() method has to be called every frame to
 * run the previews of the {@link Creature}s.
 * <p>
 * This class uses the {@link Layout} class for the way of displaying the
 * elements.
 *
 */
public class PopScreen extends BorderPane {
	/**
	 * Holds the reference to the {@link Population} this {@link PopScreen} instance
	 * displays.
	 */
	private Population pop;
	/**
	 * Holds the reference to the {@link TestScreen} which is called to display a
	 * certain {@link Creature}
	 */
	private final TestScreen mainTestScreen;
	/**
	 * Is {@code true} when the mouse is hovering over an element in the list.
	 */
	private boolean mouseHover = false;
	/**
	 * Shows a small preview of a {@link Creature}
	 */
	private TestScreen previewScreen;
	/**
	 * Lists all the {@link Creature} instances from the {@link Population} with
	 * their ID and fitness.
	 */
	private TableView<Creature> tbv_pop = new TableView<Creature>();
	/**
	 * Holds information about which list element the mouse is or was hovering
	 */
	private IntegerProperty selectedIndex = new SimpleIntegerProperty(-1);
	/**
	 * Is {@code true} when the table is visible, {@code false} when a
	 * {@link TestProgressBar} is shown.
	 */
	private boolean isActive = false;

	/**
	 * Initialises a new {@link PopScreen} instance. References to the
	 * {@link Population} and {@link TestScreen} are handed over. The
	 * {@link TableView} is initialised and EventListerner are added for the preview
	 * window.
	 * 
	 * @param pop_in
	 *            reference to the {@link Population} this {@link PopScreen} should
	 *            list
	 * @param testScreen_in
	 *            refernece to the {@link TestScreen} this {@link PopScreen} should
	 *            address to start a test.
	 */
	@SuppressWarnings("unchecked")
	public PopScreen(Population pop_in, TestScreen testScreen_in) {
		mainTestScreen = testScreen_in;
		pop = pop_in;

		Label placeholder = new Label("No Creatures to show\n\nCreate Population first!");
		placeholder.setTextAlignment(TextAlignment.CENTER);
		tbv_pop.setPlaceholder(placeholder);

		tbv_pop.setRowFactory(tableView -> {
			final TableRow<Creature> row = new TableRow<>();
			row.hoverProperty().addListener((observable) -> {

				final Creature cret = row.getItem();
				row.setId("" + cret.getID());
				if (row.isHover() && cret != null) {
					selectedIndex.set(Integer.parseInt(row.getId()));
				} else {
					selectedIndex.set(-1);
				}

			});

			return row;
		});

		selectedIndex.addListener(
				(observable, oldValue, newValue) -> selectedChange(oldValue.intValue(), newValue.intValue()));

		pop.fitnessSetProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				selectedIndex.set(-1);
				tbv_pop.refresh();
			}
		});

		TableColumn<Creature, Integer> tclm_creatureID = new TableColumn<>("ID");
		tclm_creatureID.setPrefWidth(50);
		tclm_creatureID.setCellValueFactory(new PropertyValueFactory<Creature, Integer>("ID"));
		tclm_creatureID.setSortable(false);

		TableColumn<Creature, Float> tclm_creatureFitness = new TableColumn<>("Fitness");
		tclm_creatureFitness.setPrefWidth(100);
		tclm_creatureFitness.setCellValueFactory(new PropertyValueFactory<Creature, Float>("FitnessFloat"));
		tclm_creatureFitness.setSortable(false);

		ObservableList<Creature> tableCreatures = FXCollections.observableArrayList(pop.getArrayList());
		tbv_pop.setItems(tableCreatures);
		tbv_pop.getColumns().addAll(tclm_creatureID, tclm_creatureFitness);

		tbv_pop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onMouseClicked(e));
		tbv_pop.addEventHandler(MouseEvent.ANY, e -> onMouseMoved(e));
		tbv_pop.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			selectedIndex.set(-1);
		});

		// this.setPadding(new Insets(13));
		tbv_pop.setPrefWidth(145);
		tbv_pop.setPrefHeight(10000);
		tbv_pop.getStyleClass().add("tableview");
		this.setCenter(tbv_pop);
		Layout.defaultMargin(tbv_pop);

		isActive = true;
	}

	/**
	 * This method is called once the mouse has entered a list element or when it
	 * left a list element. It manages the initialisation and the destruction of the
	 * preview {@link TestScreen}.
	 * 
	 * @param oldValue
	 *            old list element index the mouse had been hovering over
	 * @param newValue
	 *            new list element index the mouse is hovering over
	 */
	private void selectedChange(int oldValue, int newValue) {

		if (newValue != -1 && oldValue == -1 && isActive) {
			mouseHover = true;
			if (previewScreen == null) {
				previewScreen = new TestScreen(250, 150, 30, new Vec2(-1, 1), pop);
				previewScreen.startSingleTest(pop.getCreatureByID(selectedIndex.get()));
				//previewScreen.startSingleTest(tbv_pop.getItems().get(selectedIndex.get()));
				previewScreen.setBackgroundCol(Layout.getSkycolor());
				previewScreen.enableAutoRepeat();
				previewScreen.enableCompactInfo();
				previewScreen.disableScrollZoom();

				this.getChildren().add(previewScreen);
			}

		}

		if (newValue == -1) {
			mouseHover = false;
			this.getChildren().remove(previewScreen);
			previewScreen = null;
		}
	}

	/**
	 * This method is called when the mouse moves and makes the preview screen
	 * follow the mouse's position.
	 * 
	 * @param e
	 *            is used for the current mouse position
	 */
	private void onMouseMoved(MouseEvent e) {

		if (mouseHover && previewScreen != null) {
			previewScreen.setTranslateX(e.getX() + 17);
			previewScreen.setTranslateY(e.getY() + 17);
		}
	}

	/**
	 * This method is called when the table registers a double-click and calls the
	 * {@link TestScreen} referenced during initialisation to test the selected
	 * {@link Creature}
	 * 
	 * @param e
	 *            is used to detect a double-click
	 */
	private void onMouseClicked(MouseEvent e) {
		if (e.getButton().equals(MouseButton.PRIMARY)) {
			if (e.getClickCount() == 2) {
				if (pop.getPopStat() != PopulationStatus.S2_TESTING) {
					mainTestScreen.enableAutoRepeat();
				}
				mainTestScreen.startSingleTest(tbv_pop.getSelectionModel().getSelectedItem());
			}
		}
	}

	/**
	 * Refreshes the preview screen. This method has to be called <strong> every
	 * </strong> frame to run the previews of a {@link Creature}.
	 * 
	 * @param dt
	 *            delta time: time (in seconds) since last frame
	 */
	public void refresh(float dt) {
//		if (!isActive)
//			return;
		if (previewScreen != null && mouseHover)
			previewScreen.refresh(dt);
	}

	/**
	 * Refreshes the {@link TableView}. This method has to be called every time, a
	 * change was made to values relevant for the list. That is:
	 * <ul>
	 * <li>new fitness values</li>
	 * <li>new {@link Creature}</li>
	 * <li>new order of {@link Creature} instances in the {@link Population}</li>
	 * </ul>
	 */
	public void refreshTable() {
		if (!isActive)
			return;
		ObservableList<Creature> tableCreatures = FXCollections.observableArrayList(pop.getArrayList());
		tbv_pop.setItems(tableCreatures);
		selectedIndex.set(-1);
		tbv_pop.refresh();
	}

	/**
	 * Replaces the {@link TableView} with a {@link TestProgressBar}. This method is
	 * called to show the progress at the top of this {@link BorderPane} when a
	 * {@link MultiTest} calculation is performed. Once the calculation is done, the
	 * bar has to be removed manually by calling {@code removeProgressBar()}
	 * <p>
	 * <strong> NOTE: </strong> As it is now, it temporarily disables the preview
	 * screen due to NullPointer errors otherwise.
	 * 
	 * @param testProgressBar_in
	 *            is added at the top of this instance
	 */
	public void setProgressBar(TestProgressBar testProgressBar_in) {
		isActive = false;
		this.setTop(testProgressBar_in);
	}

	/**
	 * This method removes a {@link TestProgressBar} which had been added with
	 * {@code setProgressBar(bar)}.
	 * 
	 */
	public void removeProgressBar() {
		this.getChildren().remove(this.getTop());
		isActive = true;
	}
}
