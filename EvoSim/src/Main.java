import org.jbox2d.common.Vec2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{
	private static String version = "0.0.1";
	GraphicsContext gc;
	
	private long lastNanoTime;
	private TestScreen mainTestScreen;
	private PlayBackControls bp_control;
	
	private Population pop = new Population();
	
	public static void main(String[] args) {
		launch (args);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("EvoSim - v"+ version);
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene(root, 1200, 700);
		scene.setFill(Color.color(0.8, 0.8, 1));
		
		primaryStage.setScene(scene);
		mainTestScreen = new TestScreen(new Vec2(0.0f, -9.81f), 1100, 500, 70, new Vec2(0.0f, 3.0f), pop);
		mainTestScreen.setBackgroundCol(Color.ALICEBLUE);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> onKeyScreen(e));
		scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> offKeyScreen(e));
		
		
		bp_control = new PlayBackControls(mainTestScreen);
		mainTestScreen.enableAutoGetNext();
		mainTestScreen.enableInfo();
		mainTestScreen.enableMarkers();
		
		root.setBottom(bp_control);
		root.setCenter(mainTestScreen);
		
		
		primaryStage.show();
		
		pop.CreateRandPopulation(100);
		
		primaryStage.heightProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		primaryStage.widthProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		
		final long startNanoTime = System.nanoTime();
		lastNanoTime = startNanoTime;
	    
		new AnimationTimer()
	      {
	          public void handle(long currentNanoTime)
	          {
	              double dt = (currentNanoTime - lastNanoTime) / 1000000000.0;
	              lastNanoTime = currentNanoTime;
	              
	        	  refreshScreen((float) dt);
	          }
	      }.start();
	      
	}

	private void offKeyScreen(KeyEvent e) {
	}

	private void onKeyScreen(KeyEvent e) {
		if (e.getCode() == KeyCode.Q) {
			mainTestScreen.setScale(mainTestScreen.getScale()*0.8f);
		}
		if (e.getCode() == KeyCode.E) {
			mainTestScreen.setScale(mainTestScreen.getScale()*1.2f);
		}
	}

	private void refreshScreen(float dt) {
		mainTestScreen.refresh(dt);
		bp_control.refresh();

	}

	private void stageResize(Stage s) {
		mainTestScreen.setScreenSize((int) s.getWidth()-100, (int) s.getHeight()-200); 
	}

}
