package main;

import org.jbox2d.common.Vec2;

import display.Layout;
import display.TestScreen;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
	private ScrollPane sp = new ScrollPane();
	
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
		
		sp.setFitToWidth(true);
		sp.getStyleClass().add("scroll-pane");
		Layout.setBackgroundCol(sp);
		BorderPane.setMargin(sp, new Insets(15,0,0,0));
		BorderPane.setAlignment(sp, Pos.TOP_LEFT);
		
		setPage(currentPage);
		this.setCenter(sp);
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

			sp.setContent(pages[currentPage]);
		}
		updateButtons();
	}

	public void prevPage() {
		if (currentPage > 0) {
			currentPage --;
			sp.setContent(pages[currentPage]);
		}
		updateButtons();
	}
	
	public void setPage(int page) {
		if (page >= 0 && page < pages.length) {
			currentPage = page;
			sp.setContent(pages[currentPage]);
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
			titles[i].setStyle("-fx-background-color: transparent;");
		}
		titles[currentPage].setFont(new Font(30));
		titles[currentPage].setStyle("-fx-background-color: #7ebcea;");
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
			titles[i].setMinWidth(210);
			titles[i].setPadding(new Insets(4, 4, 4, 15));
			vb_contents.getChildren().add(titles[i]);
		}
		
		Layout.defaultMargin(vb_contents);
		vb_contents.autosize();
		BorderPane.setAlignment(vb_contents, Pos.CENTER_LEFT);
		vb_contents.setMaxWidth(200);
		
		this.setLeft(vb_contents);
	}

	private static BorderPane getPage1() {
		BorderPane page = new BorderPane();
		page.setId("Home");
		Label[] lbls = new Label[3];
		
		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}
		lbls[0].setText("Projektarbeit von Nils Schlatter und Jan Obermeier, G3E");
		lbls[1].setText("Ob Darwinismus oder Survival of the Fittest, jeder hat schon "
				+ "mal in irgendeiner Form vor der Evolution gehört: Eine komplexe, mehr "
				+ "oder weniger von Zufällen gesteuerte Entwicklung, aus welcher wir "
				+ "entstanden sein sollen. Mit dem Projekt EvoSim haben wir es uns zur Aufgabe "
				+ "gemacht, genau einen solchen Prozess, die Evolution, zu simulieren.");
		
		lbls[2].setText("Auf den folgenden Seiten findet ihr Informationen über die Funktionsweise "
				+ "des Programmes sowohl als auch eine Bedienungsanleitung.");

		VBox vb = new VBox();
		
		for (Label l : lbls) {
			vb.getChildren().add(l);
		}
		
		vb.autosize();
		page.setCenter(vb);
		
		Layout.rootPadding(page);
		
		Label title = new Label("EvoSim - die Evolution im Kleinen");
		Layout.labelBigTitle(title);
		
		page.setTop(title);
		
		return page;
	}

	private static BorderPane getPage2() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[7];
		
		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}
		lbls[0].setText("Die Evolution programmieren, ist das überhaupt möglich?");
		lbls[1].setText("Bevor die Erwartungen allzu sehr steigen, ist zu vorentnehmen, dass es sich "
				+ "bei dieser Simulation nur um ein sehr vereinfachtes Modell handelt. Die "
				+ "Kreaturen, welche sich evolutiv entwickeln sollen, sind lediglich ein paar "
				+ "bewegende Klötzchen. Auf diese wird dann der Trick angewendet:");
		
		lbls[2].setText("genetische Algorithmen");
		Layout.labelTitle(lbls[2]);
		lbls[3].setText("Diese ermöglichen es, die Evolution vereinfacht in Codezeilen zu fassen. "
				+ "Kurz und knapp erklärt, sieht ein solcher Algorithmus etwa so aus:"); 
		lbls[4].setText("1.	komplett zufällige Kreaturen werden erstellt\n" + 
				"2.	Kreaturen werden auf ein bestimmtes Kriterium getestet, ihre Leistung "
				+ "wird in einer Zahl, der «Fitness», wiedergegeben\n" + 
				"3.	Kreaturen mit einer guten Leistung können sich mit kleinen Veränderungen, "
				+ "sogenannten Mutationen, an den Nachkommen fortpflanzen, während die "
				+ "Schlechteren eliminiert werden\n" + 
				"4.	zurück zu Schritt 2");
		lbls[5].setText("That’s it!");
		lbls[6].setText("In Deinem Fall werden:\r\n" + 
				"100 Kreaturen einer (Wurm/Schlangen/Dreibeinigen/Vierbeinigen Gestalt) "
				+ "aber mit zufälligen Bewegungsabläufen werden in einem Zeitrahmen von "
				+ "15s auf ihre Fähigkeit so weit wie möglich zu kommen getestet.");
		VBox vb = new VBox();
		
		for (Label l : lbls) {
			vb.getChildren().add(l);
		}
		
		Label title = new Label("Der grundlegende Algorithmus");
		Layout.labelBigTitle(title);
		
		vb.autosize();
		page.setCenter(vb);
		page.autosize();
		page.setTop(title);
		
		Layout.rootPadding(page);
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
		testScreen.setInactiveBackgroundCol(Layout.getSkycolor());
		page.setCenter(testScreen);
		page.setId("Kreaturen");
		Layout.rootPadding(page);
		return page;
	}

	public void refresh(float dt) {
		testScreen.refresh(dt);
	}
	
	
}
