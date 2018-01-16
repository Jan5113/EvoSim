package main;

import org.jbox2d.common.Vec2;

import display.Layout;
import display.TestScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import population.Creature;

public class Instructions extends BorderPane {
	BorderPane[] pages = new BorderPane[3];
	Label[] titles = new Label[3];
	int currentPage = 0;
	
	private Button btn_next = new Button("Next >");
	private Button btn_prev = new Button("< Back");
	private Button btn_showEvo = new Button("Skip >>");
	
	private VBox vb_contents = new VBox(10);
	
	private final Main main;
	
	public Instructions(Main main_in) {
		super();
		setupPages();
		setupButtons();
		this.setCenter(pages[currentPage]);
		updateButtons();
		
		main = main_in;
		
		btn_next.setOnAction(e -> nextPage());
		btn_prev.setOnAction(e -> prevPage());
	}
	
	private void setupButtons() {
		BorderPane buttons = new BorderPane();
		Layout.button(btn_next);
		Layout.button(btn_prev);
		Layout.defaultMargin(btn_next);
		Layout.defaultMargin(btn_prev);
		buttons.setLeft(btn_prev);
		buttons.setCenter(btn_next);

		Layout.button(btn_showEvo);
		Layout.defaultMargin(btn_showEvo);
		BorderPane.setAlignment(btn_next, Pos.CENTER_RIGHT);
		btn_showEvo.setOnAction(e -> {btn_showEvo.setText("Close"); main.setBPEvo();});
		buttons.setRight(btn_showEvo);

		this.setBottom(buttons);
	}

	public void nextPage() {
		if (currentPage + 1 < pages.length) {
			currentPage ++;
			this.setCenter(pages[currentPage]);
		}
		updateButtons();
	}

	public void prevPage() {
		if (currentPage > 0) {
			currentPage --;
			this.setCenter(pages[currentPage]);
		}
		updateButtons();
	}
	
	public void setPage(int page) {
		if (page >= 0 && page < pages.length) {
			currentPage = page;
			this.setCenter(pages[currentPage]);
		}
		updateButtons();
	}
	
	public void updateButtons() {
		if (currentPage == 0) {
			btn_prev.setVisible(false);
			btn_next.setVisible(true);
		}
		else if (currentPage + 1 == pages.length) {
			btn_next.setVisible(false);
			btn_prev.setVisible(true);
		} else {
			btn_prev.setVisible(true);
			btn_next.setVisible(true);
		}
		for (int i = 0; i < pages.length; i++) {
			titles[i].setFont(new Font(16));
		}
		titles[currentPage].setFont(new Font(30));
	}
	
	private void setupPages() {
		pages[0] = getPage1();
		pages[1] = getPage2();
		pages[2] = getPage3();
		
		for (int i = 0; i < pages.length; i++) {
			titles[i] = new Label(pages[i].getId());
			titles[i].setFont(new Font(20));
			titles[i].setId(i + "");
			titles[i].setOnMouseClicked(e -> setPage(Integer.parseInt(((Label) e.getSource()).getId())));
			vb_contents.getChildren().add(titles[i]);
		}
		
		Layout.defaultMargin(vb_contents);
		vb_contents.autosize();
		BorderPane.setAlignment(vb_contents, Pos.CENTER_LEFT);
		vb_contents.setPrefWidth(200);
		
		this.setLeft(vb_contents);
	}

	private static BorderPane getPage1() {
		BorderPane page = new BorderPane();
		page.setId("EvoSim");
		Label lbl = new Label(
				"Ob Darwinismus oder Survival of the Fittest, jeder hat schon mal in irgendeiner "
				+ "Form vor der Evolution gehört: Eine komplexe, mehr oder weniger von Zufällen "
				+ "gesteuerte Entwicklung, aus welcher wir entstanden sein sollen.\n\nMit dem Projekt "
				+ "EvoSim haben wir es uns zur Aufgabe gemacht, genau einen solchen Prozess, die "
				+ "Evolution, zu simulieren.");
		lbl.setWrapText(true);
		lbl.setMaxWidth(600);
		lbl.setFont(new Font(24));
		Layout.defaultMargin(lbl);
		page.setCenter(lbl);
		return page;
	}

	private static BorderPane getPage2() {
		BorderPane page = new BorderPane();
		page.setCenter(new Label("Hi there! \n Page 2"));
		page.setId("Algorithmus");
		return page;
	}

	private TestScreen testScreen;
	
	private BorderPane getPage3() {
		BorderPane page = new BorderPane();
		testScreen = new TestScreen(300, 200, 30, new Vec2(0,0));
		testScreen.startSingleTest(new Creature(0));
		testScreen.setBackgroundCol(Layout.getSkycolor());
		//testScreen.enableAutoRepeat();
		testScreen.showScore(true);
		testScreen.showTimer(true);
		testScreen.setInactiveBackgroundCol(Layout.getSkycolorInactive());
		page.setCenter(testScreen);
		page.setId("Kreaturen");
		return page;
	}

	public void refresh(float dt) {
		testScreen.refresh(dt);
	}
	
	
}
