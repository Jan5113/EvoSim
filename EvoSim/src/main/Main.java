package main;
import org.jbox2d.common.Vec2;

import display.Layout;
import display.PlayBackControls;
import display.PopScreen;
import display.PopScreenControl;
import display.TestScreen;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import population.Population;
import test.MultiTest;

public class Main extends Application{
	private static String version = "0.1.0";
	
	private long lastNanoTime;
	private TestScreen mainTestScreen;
	private MultiTest mainMultiTest;
	private PlayBackControls bp_testControl;
	private PopScreenControl bp_popControl;
	private VBox bp_pop;
	private BorderPane bp_test;
	private PopScreen popScreen;
	
	private Scene scene;
	public BorderPane root; 
	private Instructions bp_instr;
	private BorderPane bp_evo;
	private boolean showInstr;
	
	private Button btn_showInstr;
	
	private Population pop = new Population(new Vec2(0.0f, -9.81f));
	
	public static void main(String[] args) {
		launch (args);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("EvoSim - v"+ version);
		root = new BorderPane();
		scene = new Scene(root, 1200, 700);
		scene.setFill(Color.color(0.8, 0.8, 1));
		Layout.setCSS(scene);
		Layout.rootPadding(root);

		setupTestScreen();
		setupPopScreen();
		setupButton();

		bp_evo = new BorderPane();
		bp_evo.setCenter(bp_test);
		bp_evo.setLeft(bp_pop);
		bp_evo.setTop(btn_showInstr);
		
		bp_instr = new Instructions(this);
		
		primaryStage.heightProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		primaryStage.widthProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		
		final long startNanoTime = System.nanoTime();
		lastNanoTime = startNanoTime;
	    
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				double dt = (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;

				refreshScreen((float) dt);
			}
		}.start();

		setBPInstr();
		
		primaryStage.setScene(scene);
		stageResize(primaryStage);
		
		primaryStage.show();
	}
	
	private void setupButton() {
		btn_showInstr = new Button("?");
		Layout.squareButton(btn_showInstr);
		Layout.defaultMargin(btn_showInstr);
		BorderPane.setAlignment(btn_showInstr, Pos.TOP_RIGHT);
		btn_showInstr.setOnAction(e -> setBPInstr());
		btn_showInstr.setTranslateY(0);
		btn_showInstr.resize(0, 0);
	}
	
	public void setBPEvo() {
		root.setCenter(bp_evo);
		showInstr = false;
	}

	public void setBPInstr() {
		root.setCenter(bp_instr);
		showInstr = true;
	}

	private void setupTestScreen() {
		bp_test = new BorderPane();
		bp_test.setTranslateY(-45);
		mainTestScreen = new TestScreen(900, 500, 70, new Vec2(0.0f, 3.0f), pop);
		mainTestScreen.setBackgroundCol(Layout.getSkycolor());
		mainTestScreen.setInactiveBackgroundCol(Layout.getSkycolorInactive());
		mainTestScreen.enableInfo();
		mainTestScreen.enableMarkers();
		mainTestScreen.showTimer(true);
		mainTestScreen.showScore(true);
		Layout.defaultMargin(mainTestScreen);
		BorderPane.setAlignment(mainTestScreen, Pos.TOP_LEFT);

		bp_testControl = new PlayBackControls(mainTestScreen);
		
		Label testTitle = new Label("Creature preview");
		Layout.defaultMargin(testTitle);
		Layout.labelTitle(testTitle);
		
		bp_test.setCenter(mainTestScreen);
		bp_test.setBottom(bp_testControl);
		bp_test.setTop(testTitle);
	}

	private void setupPopScreen() {
		bp_pop = new VBox();
		bp_pop.setTranslateY(-45);
		popScreen = new PopScreen(pop, mainTestScreen);
		mainMultiTest = new MultiTest(6, pop);
		bp_popControl = new PopScreenControl(mainTestScreen, mainMultiTest, popScreen, pop);
		Label popTitle = new Label("Population");
		Layout.defaultMargin(popTitle);
		Layout.labelTitle(popTitle);
		
		bp_pop.getChildren().add(popTitle);
		bp_pop.getChildren().add(bp_popControl);
		bp_pop.getChildren().add(popScreen);
	}

	private void refreshScreen(float dt) {
		mainTestScreen.refresh(dt);
		bp_testControl.refresh();
		popScreen.refresh(dt);
		bp_popControl.refresh();
		
		if (showInstr) bp_instr.refresh(dt);
	}

	private void stageResize(Stage s) {
		mainTestScreen.setScreenSize((int) s.getWidth()-267, (int) s.getHeight()-265); 
	}


}
