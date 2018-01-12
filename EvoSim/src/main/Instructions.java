package main;

import org.jbox2d.common.Vec2;

import display.Layout;
import display.TestScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import population.Creature;

public class Instructions extends BorderPane {
	BorderPane[] pages = new BorderPane[3];
	int currentPage = 0;
	
	private Button btn_next = new Button("Next >");
	private Button btn_prev = new Button("< Back");
	private Button btn_showEvo = new Button("Skip >>");
	
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
		Layout.defMargin(btn_next);
		Layout.defMargin(btn_prev);
		buttons.setLeft(btn_prev);
		buttons.setCenter(btn_next);

		Layout.button(btn_showEvo);
		Layout.defMargin(btn_showEvo);
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
	
	public void updateButtons() {
		if (currentPage == 0) {
			btn_prev.setDisable(true);
			btn_next.setDisable(false);
		}
		else if (currentPage + 1 == pages.length) {
			btn_next.setDisable(true);
			btn_prev.setDisable(false);
		} else {
			btn_prev.setDisable(false);
			btn_next.setDisable(false);
		}
	}
	
	private void setupPages() {
		pages[0] = getPage1();
		pages[1] = getPage2();
		pages[2] = getPage3();
	}

	private static BorderPane getPage1() {
		BorderPane page = new BorderPane();
		page.setCenter(new Label("Hi there! \n Page 1"));
		page.setId("Start");
		return page;
	}

	private static BorderPane getPage2() {
		BorderPane page = new BorderPane();
		page.setCenter(new Label("Hi there! \n Page 2"));
		page.setId("Welcome");
		return page;
	}

	private TestScreen testScreen;
	
	private BorderPane getPage3() {
		BorderPane page = new BorderPane();
		testScreen = new TestScreen(300, 200, 30, new Vec2(0,0));
		testScreen.startSingleTest(new Creature(0));
		testScreen.setBackgroundCol(Layout.getSkycolor());
		testScreen.enableAutoRepeat();
		testScreen.showScore(true);
		testScreen.showTimer(true);
		testScreen.setInactiveBackgroundCol(Layout.getSkycolorInactive());
		page.setCenter(testScreen);
		page.setId("Welcome");
		return page;
	}

	public void refresh(float dt) {
		testScreen.refresh(dt);
	}
	
	
}
