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

public class PopScreen extends BorderPane {
	private Population pop;
	private final TestScreen mainTestScreen;
	private boolean mouseHover = false;
	private TestScreen previewScreen;
	private TableView<Creature> tbv_pop = new TableView<Creature>();
	private IntegerProperty selectedIndex = new SimpleIntegerProperty(-1);
	private boolean isActive = false;

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

		pop.fitnessSetProperty().addListener(new ChangeListener() {
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
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
		
		isActive = true;
	}

	private void selectedChange(int oldValue, int newValue) {

		if (newValue != -1 && oldValue == -1) {
			mouseHover = true;
			if (previewScreen != null) {

			} else {
				previewScreen = new TestScreen(250, 150, 30, new Vec2(-1, 1), pop);
				previewScreen.startSingleTest(pop.getCreatureByID(selectedIndex.get()));
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

	private void onMouseMoved(MouseEvent e) {

		if (mouseHover && previewScreen != null) {
			previewScreen.setTranslateX(e.getX() + 2);
			previewScreen.setTranslateY(e.getY() + 2);
		}
	}

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

	public void refresh(float dt) {
		if (!isActive) return;
		if (previewScreen != null && mouseHover)
			previewScreen.refresh(dt);
	}
	
	public void refreshTable() {
		if (!isActive) return;
		ObservableList<Creature> tableCreatures = FXCollections.observableArrayList(pop.getArrayList());
		tbv_pop.setItems(tableCreatures);
		tbv_pop.refresh();
	}
	
	public void setActive(boolean active) {
		isActive = active;
	}

	public void setProgressBar(TestProgressBar testProgressBar_in) {
		isActive = false;
		this.setCenter(testProgressBar_in);
	}
	
	public void resetCenter() {
		this.setCenter(tbv_pop);
		isActive = true;
	}
}
