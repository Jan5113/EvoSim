import org.jbox2d.common.Vec2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{
	private static String version = "0.0.1";
	GraphicsContext gc;
	private Screen screen;
	
	private long lastNanoTime;
	private TestManager tests;
	private Controls bp_control;
	
	public static void main(String[] args) {
		launch (args);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("EvoSim - v"+ version);
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene(root, 1200, 700);
		scene.setFill(Color.color(0.8, 0.8, 1));
		
		primaryStage.setScene(scene);
		screen = new Screen(1100, 500, 100, new Vec2(3.0f, 2.0f));
		screen.setBackgroundCol(Color.ALICEBLUE);
		screen.setOnMouseReleased(e -> onClickScreen(e));
		screen.setOnMouseMoved(e -> onMoveScreen(e));
		screen.setOnMousePressed(e -> onPressedScreen(e));
		screen.setOnMouseDragged(e -> onEnteredScreen(e));
		scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> onKeyScreen(e));
		scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> offKeyScreen(e));
		
		tests = new TestManager(new Vec2(0.0f, -9.81f));
		
		bp_control = new Controls(tests);
		
		root.setBottom(bp_control);
		root.setCenter(screen);
		
		
		primaryStage.show();
		
		
		
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
	              
	        	  refreshScreen(dt);
	          }
	      }.start();
	      
	}

	private void offKeyScreen(KeyEvent e) {
	}

	private void onKeyScreen(KeyEvent e) {
		if (e.getCode() == KeyCode.Q) {
			screen.setScale(screen.getScale()*0.8f);
		}
		if (e.getCode() == KeyCode.E) {
			screen.setScale(screen.getScale()*1.2f);
		}
	}

	private void refreshScreen(double dt) {
		tests.manageTest(dt);
		bp_control.refresh();
		screen.clearScreen();
		
		for (B2DBody b : tests.getWorldInstances()) {
			screen.drawBody(b);
		}
		for (B2DBody b : tests.getCreatureInstances()) {
			screen.drawBody(b);
		}

	}
	
	
	private void onPressedScreen(MouseEvent e) {
	}

	private void onClickScreen(MouseEvent e) {
	}
	
	private void onMoveScreen(MouseEvent e) {
	}
	
	private void onEnteredScreen(MouseEvent e) {
	}

	private void stageResize(Stage s) {
		screen.setScreenSize((int) s.getWidth()-100, (int) s.getHeight()-200); 
	}

}
