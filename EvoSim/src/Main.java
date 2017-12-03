import org.jbox2d.common.Vec2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{
	private static String version = "0.1.0";
	
	private long lastNanoTime;
	private TestScreen mainTestScreen;
	private MultiTest mainMultiTest;
	private PlayBackControls bp_control;
	private BorderPane bp_pop;
	private BorderPane bp_test;
	private PopScreen popScreen;
	public BorderPane root;
	
	private Population pop = new Population(new Vec2(0.0f, -9.81f));
	
	public static void main(String[] args) {
		launch (args);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("EvoSim - v"+ version);

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1200, 700);
		scene.setFill(Color.color(0.8, 0.8, 1));
		
		primaryStage.setScene(scene);
		
		scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> onKeyScreen(e));
		scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> offKeyScreen(e));
		
		mainMultiTest = new MultiTest(6, pop);
		
		pop.CreateRandPopulation(100);

		setupTestScreen();
		setupPopScreen();
		
		root.setPadding(new Insets(0, 15, 15, 0));
		root.setCenter(bp_test);
		root.setLeft(bp_pop);
		
		primaryStage.show();
		
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

		stageResize(primaryStage);
	}
	
	private void setupTestScreen() {
		bp_test = new BorderPane();
		mainTestScreen = new TestScreen(900, 500, 70, new Vec2(0.0f, 3.0f), pop);
		mainTestScreen.setBackgroundCol(Layout.getSkycolor());
		mainTestScreen.enableAutoGetNext();
		mainTestScreen.enableInfo();
		mainTestScreen.enableMarkers();
		Layout.defMargin(mainTestScreen);
		BorderPane.setAlignment(mainTestScreen, Pos.TOP_LEFT);

		bp_control = new PlayBackControls(mainTestScreen, mainMultiTest);
		Layout.defMargin(bp_control);
		
		Label testTitle = new Label("Creature preview");
		Layout.defMargin(testTitle);
		Layout.labelTitle(testTitle);
		
		bp_test.setCenter(mainTestScreen);
		bp_test.setBottom(bp_control);
		bp_test.setTop(testTitle);
	}

	private void setupPopScreen() {
		bp_pop = new BorderPane();
		popScreen = new PopScreen(pop, mainTestScreen);
		bp_pop.setCenter(popScreen);
		Layout.defMargin(popScreen);
		Label popTitle = new Label("Population");
		Layout.defMargin(popTitle);
		Layout.labelTitle(popTitle);
		bp_pop.setTop(popTitle);
	}

	private void offKeyScreen(KeyEvent e) {
	}

	private void onKeyScreen(KeyEvent e) {
	}

	private void refreshScreen(float dt) {
		mainTestScreen.refresh(dt);
		bp_control.refresh();
		popScreen.refresh(dt);

	}

	private void stageResize(Stage s) {
		mainTestScreen.setScreenSize((int) s.getWidth()-580, (int) s.getHeight()-250); 
	}

}
