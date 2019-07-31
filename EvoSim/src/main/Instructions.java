package main;

import org.jbox2d.common.Vec2;

import creatureCreator.ProtoCreature;
import display.Layout;
import display.TestScreen;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import population.Creature;

public class Instructions extends BorderPane {
	private BorderPane[] pages = new BorderPane[12];
	private Label[] titles = new Label[12];
	private int currentPage = 0;

	private Button btn_next = new Button("Next >");
	private Button btn_prev = new Button("< Back");
	private Button btn_showEvo = new Button("Skip >>");

	private VBox vb_contents = new VBox(10);
	private ScrollPane sp = new ScrollPane();
	private BorderPane mainBP = new BorderPane();

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
		BorderPane.setMargin(sp, new Insets(15, 0, 0, 0));
		BorderPane.setAlignment(sp, Pos.TOP_LEFT);

		setPage(currentPage);
		this.setCenter(sp);
		mainBP.setMaxWidth(800);
		BorderPane.setAlignment(mainBP, Pos.TOP_CENTER);
		BorderPane tempBP = new BorderPane(mainBP);
		sp.setContent(tempBP);
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
		btn_showEvo.setOnAction(e -> {
			btn_showEvo.setText("Close");
			main.setBPEvo();
		});
		buttons.setRight(btn_showEvo);

		this.setBottom(buttons);
	}

	public void nextPage() {
		if (currentPage + 1 < pages.length) {
			currentPage++;
			mainBP.setCenter(pages[currentPage]);
		}
		updateButtons();
	}

	public void prevPage() {
		if (currentPage > 0) {
			currentPage--;
			mainBP.setCenter(pages[currentPage]);
		}
		updateButtons();
	}

	public void setPage(int page) {
		if (page >= 0 && page < pages.length) {
			currentPage = page;
			mainBP.setCenter(pages[currentPage]);
		}
		updateButtons();
	}

	public void updateButtons() {
		if (currentPage == 0) {
			btn_prev.setVisible(false);
			btn_next.setVisible(true);
		} else if (currentPage + 1 == pages.length) {
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
		titles[currentPage].setFont(new Font(26));
		titles[currentPage].setStyle("-fx-background-color: #7ebcea;");
	}

	private void setupPages() {
		pages[0] = getPage1();
		pages[1] = getPage2();
		pages[2] = getPage3();
		pages[3] = getPage4();
		pages[4] = getPage5();
		pages[5] = getPage6();
		pages[6] = getPage7();
		pages[7] = getPage8();
		pages[8] = getPage9();
		pages[9] = getPage10();
		pages[10] = getPage11();
		pages[11] = getPage12();

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
		Layout.italics(lbls[0]);
		lbls[1].setText("\nOb Darwinismus oder Survival of the Fittest, jeder hat schon "
				+ "mal in irgendeiner Form vor der Evolution gehört: Eine komplexe, mehr "
				+ "oder weniger von Zufällen gesteuerte Entwicklung, aus welcher die Natur, wie wir "
				+ "sie kennen, entstanden sein soll. Mit dem Projekt EvoSim haben wir es uns zur Aufgabe "
				+ "gemacht, genau einen solchen Prozess, die Evolution, zu simulieren.");

		lbls[2].setText("Auf den folgenden Seiten findet ihr Informationen über die Funktionsweise "
				+ "des Programmes sowohl als auch eine Bedienungsanleitung.");

		ImageView img_evolution = new ImageView();
		try {
			img_evolution = new ImageView(new Image("evolution.png"));
			HBox.setMargin(img_evolution, new Insets(40, 0, 0, 15));
			img_evolution.setFitWidth(600);
			img_evolution.setPreserveRatio(true);
		} catch (Exception e) {
			System.err.println("File IO Error! No image found!");
		}

		HBox hb_evo = new HBox(img_evolution);
		hb_evo.setAlignment(Pos.CENTER);

		VBox vb = new VBox(lbls[0], lbls[1], lbls[2], hb_evo);

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

		Label title = new Label("Der grundlegende Algorithmus");
		Layout.labelBigTitle(title);

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
		lbls[4].setText("1.	komplett zufällige Kreaturen werden erstellt\n"
				+ "2.	Kreaturen werden auf ein bestimmtes Kriterium getestet, ihre Leistung "
				+ "wird in einer Zahl, der «Fitness», wiedergegeben\n"
				+ "3.	Kreaturen mit einer guten Leistung können sich mit kleinen Veränderungen, "
				+ "sogenannten Mutationen, an den Nachkommen fortpflanzen, während die "
				+ "Schlechteren eliminiert werden\n" + "4.	zurück zu Schritt 2");
		lbls[5].setText("That’s it!");
		lbls[6].setText("In unserem Fall werden: 100 Kreaturen mit einer"
				+ "(Wurm/Schlangen/Dreibeinigen/Vierbeinigen Gestalt) " + "werden in einem Zeitrahmen von "
				+ "15s auf ihre Fähigkeit getestet, so weit wie möglich zu kommen.");
		lbls[6].setText("In unserem Fall werden: 100 Kreaturen mit einer " + "dreigliedrigen Gestalt "
				+ "werden in einem Zeitrahmen von "
				+ "15s auf ihre Fähigkeit getestet, so weit wie möglich zu kommen.");
		Layout.setBold(lbls[6]);
		VBox vb = new VBox();

		for (Label l : lbls) {
			vb.getChildren().add(l);
		}

		vb.autosize();
		page.setCenter(vb);
		page.autosize();
		page.setTop(title);

		Layout.rootPadding(page);
		page.setId("Algorithmus");
		return page;
	}

	private TestScreen[] testScreen = new TestScreen[4];

	private ProtoCreature pc = new ProtoCreature(true);
	private Creature tutorialCret = pc.makeCreature(0);

	private BorderPane getPage3() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[4];
		Label title = new Label("Die Kreatur");
		Layout.labelBigTitle(title);
		testScreen[0] = new TestScreen(400, 200, 60, new Vec2(0, 0.5f));
		testScreen[0].enableInfo();
		testScreen[0].setInfoString("Eine zufällig generierte Kreatur:");
		testScreen[0].disableScrollZoom();

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		lbls[0].setText("Eine Kreatur in EvoSim sieht folgendermassen aus:");

		testScreen[0].startSingleTest(tutorialCret);
		testScreen[0].setBackgroundCol(Layout.getSkycolor());
		testScreen[0].setInactiveBackgroundCol(Layout.getSkycolor());

		Layout.defaultMargin(testScreen[0]);

		lbls[1].setText("Der Körper einer Kreatur wird durch Knochen gebildet, welche durch "
				+ "rotierende Gelenke miteinander verbunden sind. Der Winkel des Gelenks kann "
				+ "direkt beeinflusst werden.");
		lbls[2].setText("Jedes Gelenk hat nun zwei Winkel, dessen Werte von Gelenk zu Gelenk "
				+ "verschieden sind, hier in rot und weiss. Das Gelenk versucht, die verbundenen " + "Knochen zwischen "
				+ "diesen beiden Werten hin und her zu bewegen – es arbeitet (fast) wie ein Muskel. "
				+ "Zu beachten ist bei der Darstellung der Muskeln, dass sich nicht die Länge ändert, "
				+ "sondern wie erwäht der Winkel zwischen den Knochenelementen vorgegeben wird.");
		lbls[3].setText("Wann das Gelenk welchen Winkel einnimmt, wird von der internen Uhr "
				+ "der Kreatur bestimmt. Diese gibt den Takt vor und läuft je nach Kreatur "
				+ "schneller oder langsamer. Zwei Zeiten – wiederum spezifisch für jedes Gelenk "
				+ "und variabel – bestimmen den genauen Zeitpunkt, wann welcher Winkel eingenommen "
				+ "wird. Durch den zyklischen Ablauf der Uhr entsteht somit ein sich wiederholender "
				+ "Bewegungsablauf.");

		VBox vb = new VBox();

		vb.getChildren().addAll(lbls[0], testScreen[0], lbls[1], lbls[2], lbls[3]);

		page.setTop(title);
		page.setCenter(vb);
		page.setId("Kreaturen");
		Layout.rootPadding(page);
		return page;
	}

	private BorderPane getPage4() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[3];
		Label title = new Label("Die Mutation");
		page.setId("Mutation");
		Layout.labelBigTitle(title);

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		for (int i = 1; i < 4; i++) {
			testScreen[i] = new TestScreen(245, 200, 60, new Vec2(0, 0.5f));
			testScreen[i].setBackgroundCol(Layout.getSkycolor());
			testScreen[i].setInactiveBackgroundCol(Layout.getSkycolor());
			testScreen[i].showMuscles(true);
			testScreen[i].enableInfo();
			testScreen[i].disableScrollZoom();

			Layout.defaultMargin(testScreen[i]);
		}

		testScreen[1].startSingleTest(tutorialCret);
		testScreen[1].setInfoString("Die Mutterkreatur...");
		testScreen[2].startSingleTest(tutorialCret.mutate(1, 60));
		testScreen[2].setInfoString("...eine Variation davon...");
		testScreen[3].startSingleTest(tutorialCret.mutate(2, 60));
		testScreen[3].setInfoString("...und noch eine Variation");

		lbls[0].setText("Ein wichtiger Bestandteil von diesem genetischen Algorithmus ist "
				+ "natürlich die Veränderung der Kreaturen – die Mutation. Was bedeutet das in "
				+ "für unsere vereinfachten Kreaturen?");
		lbls[1].setText("Grundsätzlich werden alle Parameter, welche im vorherigen Kapitel "
				+ "als «variabel» deklariert worden sind, bei der Erstellung vom Nachwuchs "
				+ "nach dem Zufallsprinzip verändert. Das sind namentlich die Winkel sowie "
				+ "die Zeitintervalle der Gelenke, aber auch die Zyklusdauer der internen Uhr " + "einer Kreatur.");
		lbls[2].setText("Um am schnellsten die optimale Parameterkombination zu erraten, sind "
				+ "die Mutationen anfangs relativ gross, um in einem möglichst grossen Bereich "
				+ "nach diesem Optimum suchen zu können. Je forstgeschrittener die Kreaturen "
				+ "über die Laufzeit werden, umso mehr wir dieser Suchbereich eingeschränkt, "
				+ "bis es gegen Ende der Entwicklung nur noch um die Perfektion der Parameter " + "geht.");

		VBox vb = new VBox();

		HBox hb = new HBox(testScreen[1], testScreen[2], testScreen[3]);

		vb.getChildren().addAll(lbls[0], lbls[1], hb, lbls[2]);

		page.setTop(title);
		page.setCenter(vb);
		Layout.rootPadding(page);
		return page;
	}

	private static BorderPane getPage5() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[2];
		Label title = new Label("Bedienungsanleitung");
		page.setId("Bedienung");
		Layout.labelBigTitle(title);
		// testScreen = new TestScreen(400, 200, 60, new Vec2(0,0.5f));

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		lbls[0].setText("Auf den folgenden Seiten wird die Bedienung von EvoSim erklärt. "
				+ "Es empfiehlt sich, die Schritte parallel zum Lesen selber im Programm " + "auszuführen.");

		lbls[1].setText("Dieses Fenster lässt sich mit dem Button unten rechts "
				+ "schliessen und ist jederzeit unter dem «?»-Button oben rechts wieder " + "abrufbar.");

		Button tutBtn = new Button("?");
		tutBtn.setMouseTransparent(true);
		Layout.squareButton(tutBtn);
		Layout.defaultMargin(tutBtn);

		ImageView evoSim_UI = new ImageView();
		try {
			evoSim_UI = new ImageView(new Image("user_interface.png"));
			VBox.setMargin(evoSim_UI, new Insets(40, 0, 0, 15));
			evoSim_UI.setFitWidth(600);
			evoSim_UI.setPreserveRatio(true);
		} catch (Exception e) {
			System.err.println("File IO Error! No image found!");
		}

		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);

		HBox hb = new HBox(tutBtn, lbls[1]);
		hb.setAlignment(Pos.CENTER);

		vb.getChildren().addAll(lbls[0], hb, evoSim_UI);

		page.setTop(title);
		page.setCenter(vb);
		Layout.rootPadding(page);
		return page;
	}

	private static BorderPane getPage6() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[12];
		Button[] btns = new Button[7];
		Label title = new Label("Bedienelemente");
		page.setId("Interface");
		Layout.labelBigTitle(title);
		// testScreen = new TestScreen(400, 200, 60, new Vec2(0,0.5f));

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		lbls[0].setText("Das Interface in EvoSim ist in zwei Hauptteile unterteilt:");
		lbls[1].setText("Population - Übersicht von allen Kreaturen");
		lbls[2].setText("Creature Preview - das genaue Betrachten einer einzigen Kreatur");
		lbls[3].setText("Unter Population werden alle Kreaturen der aktuellen Generation mit ihrer ID "
				+ "und Fitness aufgelistet. Eine Kreatur kann durch Doppelklick angezeigt werden. Die "
				+ "Buttons führen die zum genetischen Algorithmus gehörenden Befehle aus. Deren genaue "
				+ "Funktionsweise wird auf den folgenden Seiten erläutert.\n");
		lbls[4].setText("Die Vorschau zeigt Details von der zurzeit ausgewählten Kreatur an "
				+ "und animiert ihre Bewegung. Die Buttons steuern die Wiedergabe/Darstellung der Kreatur:");
		lbls[5].setText("startet / pausiert die Wiedergabe");
		lbls[6].setText("halbiert/verdoppelt die Wiedergabegeschwindigkeit");
		lbls[7].setText("setzt die Geschwindigkeit zurück");
		lbls[8].setText("Bildausschnitt folgt der Kreatur");
		lbls[9].setText("Bildausschnitt kann mittels Ziehen der Maus und Scrollen angepasst werden");
		lbls[10].setText("setzt den Bildausschnitt wieder zurück");
		lbls[11].setText("zeigt die Gelenke der Kreatur und deren Aktivität");

		Layout.labelTitle(lbls[1]);
		Layout.labelTitle(lbls[2]);

		for (int i = 0; i < btns.length; i++) {
			btns[i] = new Button();
			Layout.fixedButton(btns[i]);
			btns[i].setMouseTransparent(true);
		}

		btns[0].setText("Play / Pause");
		btns[1].setText("> / >>>");
		btns[2].setText("1x");
		btns[3].setText("Lock View");
		btns[4].setText("Unlock View");
		btns[5].setText("Reset View");
		btns[6].setText("Show Joints");

		ImageView evoSim_UI = new ImageView();
		try {
			evoSim_UI = new ImageView(new Image("user_interface2.png"));
			VBox.setMargin(evoSim_UI, new Insets(15, 0, 0, 15));
			evoSim_UI.setFitWidth(775);
			evoSim_UI.setPreserveRatio(true);
		} catch (Exception e) {
			System.err.println("File IO Error! No image found!");
		}

		GridPane buttons = new GridPane();
		Layout.gridPane(buttons);
		buttons.add(btns[0], 0, 0);
		buttons.add(btns[1], 0, 1);
		buttons.add(btns[2], 0, 2);
		buttons.add(btns[3], 0, 3);
		buttons.add(btns[4], 0, 4);
		buttons.add(btns[5], 0, 5);
		buttons.add(btns[6], 0, 6);

		buttons.add(lbls[5], 1, 0);
		buttons.add(lbls[6], 1, 1);
		buttons.add(lbls[7], 1, 2);
		buttons.add(lbls[8], 1, 3);
		buttons.add(lbls[9], 1, 4);
		buttons.add(lbls[10], 1, 5);
		buttons.add(lbls[11], 1, 6);

		VBox root = new VBox(lbls[0], evoSim_UI, lbls[1], lbls[3], lbls[2], lbls[4], buttons);

		page.setTop(title);
		page.setCenter(root);
		Layout.rootPadding(page);
		return page;
	}

	private static BorderPane getPage7() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[3];
		Label title = new Label("Die ersten Kreaturen erstellen");
		page.setId("1. Schöpfung");
		Layout.labelBigTitle(title);

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		lbls[0].setText("All diese Steuerungsbefehle nützen einem jedoch nichts, wenn es nichts "
				+ "anzuzeigen gibt. Zu Beginn gibt es nämlich noch keine Kreaturen, die Population "
				+ "ist leer. Das Drücken des «Create Population»-Buttons generiert nun 100 "
				+ "Kreaturen mit einem komplett zufälligen Bewegungsmuster.");
		lbls[1].setText("Das Generieren dieser Kreaturen entspricht dem Entstehen der ersten "
				+ "Einzeller in der Ursuppe vor 3.7 Milliarden Jahren. Gleich wie sich aus diesen "
				+ "Einzellern alles Leben, das wir heute kennen, entwickelt hat, werden sich auch "
				+ "diese ersten Kreaturen zu komplexeren Kreaturen weiterentwickeln.");
		lbls[2].setText("Klickt eine Kreatur in der Liste an, um diese gross anzuzeigen und "
				+ "spielt etwas mit den Bedienelementen.");

		Button createBtn = new Button("Create Population");
		createBtn.setMouseTransparent(true);
		Layout.wideButton(createBtn);
		Layout.defaultMargin(createBtn);

		HBox hb_cret = new HBox(createBtn);
		hb_cret.setAlignment(Pos.CENTER);

		Layout.italics(lbls[1]);

		VBox vb = new VBox();

		vb.getChildren().addAll(lbls[0], hb_cret, lbls[1], lbls[2]);

		page.setTop(title);
		page.setCenter(vb);
		Layout.rootPadding(page);
		return page;
	}

	private static BorderPane getPage8() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[4];
		Button[] btns = new Button[3];
		Label title = new Label("Die Kreaturen testen");
		page.setId("2. Prüfung");
		Layout.labelBigTitle(title);

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		lbls[0].setText("Nun ist es die Aufgabe, alle Kreaturen auf ihre «Fähigkeiten» zu testen. «Start "
				+ "Test» wird nun eine Kreatur nach der anderen testen. In der Zeitspanne von 15 Sekunden "
				+ "müssen sie versuchen, so weit wie möglich zu kommen – egal wie. Bei abgelaufener Zeit "
				+ "wird die Distanz, welche die Kreatur zurückgelegt hat, als «Fitness» eingetragen – je "
				+ "grösser desto besser.");
		lbls[1].setText("In der Biologie entspricht dies dem Überleben der Organismen in der Natur, mit "
				+ "allen Fressfeinden, Konkurrenten und Artgenossen. Je besser ein Organismus an seine "
				+ "Umwelt angepasst ist, umso höher sind dessen Überlebenschancen. All diese Aspekte sind "
				+ "hier auf eine Zahl kondensiert: Die «Fitness» entspricht genau dieser Angepasstheit der "
				+ "Kreatur an ihre «Umwelt».");
		lbls[2].setText("Da wahrscheinlich keiner allen Kreaturen zuschauen will, wie die sich kaum vom "
				+ "Fleck bewegen, kann man die Animation selbstverständlich beschleunigen mit «>>>».");
		lbls[3].setText("Für die ganz Ungeduldigen unter uns kann der ganze Vorgang mit dem Klicken von "
				+ "«Skip Test» übersprungen werden.");

		Layout.italics(lbls[1]);

		for (int i = 0; i < btns.length; i++) {
			btns[i] = new Button();
			Layout.fixedButton(btns[i]);
			btns[i].setMouseTransparent(true);
			Layout.defaultMargin(btns[i]);
		}

		btns[0].setText("Test Creatures");
		btns[1].setText(">>>");
		btns[2].setText("Skip Test");

		Layout.wideButton(btns[0]);

		HBox hb_ff = new HBox(btns[1], lbls[2]);
		HBox hb_skip = new HBox(btns[2], lbls[3]);
		hb_ff.setAlignment(Pos.CENTER);
		hb_skip.setAlignment(Pos.CENTER);

		VBox vb = new VBox();
		vb.setAlignment(Pos.TOP_CENTER);
		vb.getChildren().addAll(lbls[0], btns[0], lbls[1], hb_ff, hb_skip);

		page.setTop(title);
		page.setCenter(vb);
		Layout.rootPadding(page);
		return page;
	}

	private static BorderPane getPage9() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[3];
		Button[] btns = new Button[2];
		Label title = new Label("Die Kreaturen ordnen und dezimieren");
		page.setId("3. Entscheidung");
		Layout.labelBigTitle(title);

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		lbls[0].setText("Die «Fitness» aller Kreaturen ist nun bekannt, jetzt müssen diese mit «Sort» "
				+ "nach ihrer «Fitness» geordnet werden.");
		lbls[1].setText("Mit «Kill» kommt «Survival of the Fittest» nun wortwörtlich zum Zug: Da nur "
				+ "die besten 20% weiterkommen, werden alle andern Kreaturen wohl oder übel dezimiert.");
		lbls[2].setText("In der Natur entspricht dies dem Aussterben der schlecht an die Umwelt angepassten "
				+ "Organismen beziehungsweise dem Überleben der effizienteren, stärkeren oder schnelleren. "
				+ "Die komplette «natürliche Selektion» geschieht in diesem Schritt mit zwei Mausklicks.");

		Layout.italics(lbls[2]);

		for (int i = 0; i < btns.length; i++) {
			btns[i] = new Button();
			Layout.fixedButton(btns[i]);
			btns[i].setMouseTransparent(true);
			Layout.defaultMargin(btns[i]);
		}

		btns[0].setText("Sort");
		btns[1].setText("Kill");

		HBox hb_sort = new HBox(btns[0], lbls[0]);
		HBox hb_kill = new HBox(btns[1], lbls[1]);
		hb_sort.setAlignment(Pos.CENTER);
		hb_kill.setAlignment(Pos.CENTER);

		VBox vb = new VBox();
		vb.setAlignment(Pos.TOP_CENTER);
		vb.getChildren().addAll(hb_sort, hb_kill, lbls[2]);

		page.setTop(title);
		page.setCenter(vb);
		Layout.rootPadding(page);
		return page;
	}

	private static BorderPane getPage10() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[4];
		Button[] btns = new Button[2];
		Label title = new Label("Fortpflanzen und Mutieren");
		page.setId("4. Belohnung");
		Layout.labelBigTitle(title);

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		lbls[0].setText("Da die Population wieder Platz bietet, ist es Zeit für den Nachwuchs. Wie im "
				+ "Kapitel «Mutation» erwähnt können nun die verbleibenden Kreaturen Nachwuchs zeugen, "
				+ "welche kleine Abweichungen vor der Mutterkreatur aufweisen – die Mutationen. Aus "
				+ "technischen Gründen beschränkt sich EvoSim auf die ungeschlechtliche Fortpflanzung.");
		lbls[1].setText("Bis vor etwa 700 Millionen Jahren war dies die einzige Form der Reproduktion. "
				+ "Auch heute ist diese Art der Fortpflanzung zu beobachten, zum Beispiel an der "
				+ "Zellteilung der Bakterien.\n"
				+ "Die Mutationen werden im Fall der ungeschlechtlichen Fortpflanzung durch kleinste "
				+ "Veränderungen am Gen hervorgerrufen.");
		lbls[2].setText("Die Population ist wieder komplett und der Prozess beginnt wieder von vorn. Die "
				+ "Schritte 2-4 bilden den Ablauf einer Generation, ab jetzt gilt nur noch «rince and "
				+ "repeat», eine einzelne Generation ist unbedeutend, schliesslich hat sich die Natur auch "
				+ "4.5 Milliarden Jahre Zeit gelassen und hat ein ähnliches Spiel etwas öfter durchgemacht.");
		lbls[3].setText("Um den manuellen Prozess einer Generation zu überspringen, kann man mit diesen Buttons "
				+ "die entsprechende Anzahl Generationen im Schnelldurchlauf berechnen.");

		Layout.italics(lbls[1]);

		for (int i = 0; i < btns.length; i++) {
			btns[i] = new Button();
			Layout.fixedButton(btns[i]);
			btns[i].setMouseTransparent(true);
			Layout.defaultMargin(btns[i]);
		}

		btns[0].setText("1 Gen");
		btns[1].setText("10 Gens");

		VBox vb_btns = new VBox(btns[0], btns[1]);
		HBox hb_btns = new HBox(vb_btns, lbls[3]);
		hb_btns.setAlignment(Pos.CENTER);

		VBox vb = new VBox();
		vb.setAlignment(Pos.TOP_CENTER);
		vb.getChildren().addAll(lbls[0], lbls[1], lbls[2], hb_btns);

		page.setTop(title);
		page.setCenter(vb);
		Layout.rootPadding(page);
		return page;
	}

	private static BorderPane getPage11() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[5];
		Button[] btns = new Button[1];
		Label title = new Label("Wie haben sich die Kreaturen entwickelt?");
		page.setId("5. Beobachtung");
		Layout.labelBigTitle(title);

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		lbls[0].setText("Wenn die Entwicklung einen Wert erreicht hat, bei dem die Entwicklung stagniert, "
				+ "ist das der Punkt, wo Ihr euch selber kurz Gedanken machen solltet. Wie haben sich "
				+ "Eure Kreaturen entwickelt? Was haben Sie sich \"überlegt\"? Denn jedes Mal findet "
				+ "die Evolution einen anderen Weg zum Ziel!");
		lbls[1].setText("Wählt die beste Kreatur der Population mit Doppelklick aus und lasst die Wiedergabe "
				+ "langsamer laufen. Was könnt ihr beobachten?");
		lbls[2].setText("Wie übertragen die Kreaturen die Kraft auf den Boden? \n" + "Wie verankern sie sich? \n"
				+ "Wie lösen sie sich wieder vom Boden? \n"
				+ "Haben sich Schwungarme oder andere Bewegunsmuster entwickelt?");
		lbls[3].setText("Das war es auch schon von EvoSim! Wir hoffen, dass es euch einen Einblick in "
				+ "die Prinzipien der Evolution und den genetischen Algorithmen gegeben hat. Bei Fragen oder "
				+ "Interesse stehen wir Euch natürlich gerne zur Verfügung.");
		lbls[4].setText("Nils Schlatter und Jan Obermeier, G3E");

		Layout.italics(lbls[2]);
		Layout.italics(lbls[4]);

		ImageView img_evolution = new ImageView();
		try {
			img_evolution = new ImageView(new Image("evolution.png"));
			HBox.setMargin(img_evolution, new Insets(40, 0, 0, 40));
			img_evolution.setFitWidth(200);
			img_evolution.setPreserveRatio(true);
		} catch (Exception e) {
			System.err.println("File IO Error! No image found!");
		}

		HBox hb_evo = new HBox(img_evolution);
		hb_evo.setAlignment(Pos.CENTER);

		for (int i = 0; i < btns.length; i++) {
			btns[i] = new Button();
			Layout.fixedButton(btns[i]);
			btns[i].setMouseTransparent(true);
			Layout.defaultMargin(btns[i]);
		}

		btns[0].setText(">");

		HBox hb_btns = new HBox(btns[0], lbls[1]);
		hb_btns.setAlignment(Pos.CENTER);

		VBox vb = new VBox();
		vb.getChildren().addAll(lbls[0], hb_btns, lbls[2], hb_evo, lbls[3], lbls[4]);

		page.setTop(title);
		page.setCenter(vb);
		Layout.rootPadding(page);
		return page;
	}

	private static BorderPane getPage12() {
		BorderPane page = new BorderPane();
		Label[] lbls = new Label[4];
		Label title = new Label("Über den Sinn und Unsinn dieses Projekts");
		page.setId("About");
		Layout.labelBigTitle(title);

		for (int i = 0; i < lbls.length; i++) {
			lbls[i] = new Label();
			Layout.instrLabel(lbls[i]);
		}

		lbls[0].setText("EvoSim haben wir im Rahmen des Projektunterrichts zum Thema «Bewegung» "
				+ "konzipiert, programmiert und gestaltet.");
		lbls[1].setText("Falls Ihr selber einen Blick in den Code werfen wollt, könnt Ihr diesen "
				+ "unter https://github.com/Jan5113/EvoSim.git abrufen.");
		lbls[2].setText("Fun-Fact:\nEvoSim enthält 7238 Zeilen Code in insgesamt 33 Java-Klassen");
		lbls[3].setText("Projektarbeit von Nils Schlatter und Jan Obermeier, 2019");

		Layout.italics(lbls[2]);

		ImageView img_evolution = new ImageView();
		try {
			img_evolution = new ImageView(new Image("evolution2.png"));
			HBox.setMargin(img_evolution, new Insets(50, 0, 0, 15));
			img_evolution.setFitWidth(700);
			img_evolution.setPreserveRatio(true);
		} catch (Exception e) {
			System.err.println("File IO Error! No image found!");
		}

		HBox hb_evo = new HBox(img_evolution);
		hb_evo.setAlignment(Pos.CENTER);

		VBox vb = new VBox();
		vb.getChildren().addAll(lbls[0], lbls[1], lbls[2], lbls[3], hb_evo);

		page.setTop(title);
		page.setCenter(vb);
		Layout.rootPadding(page);
		return page;
	}

	public void refresh(float dt) {
		if (currentPage == 2) { // KREATUR
			testScreen[0].refresh(dt);
		}
		if (currentPage == 3) { // MUTATION
			testScreen[1].refresh(dt);
			testScreen[2].refresh(dt);
			testScreen[3].refresh(dt);
		}
	}

}
