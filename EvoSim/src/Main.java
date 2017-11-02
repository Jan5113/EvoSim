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
	private Test test;
	private Vec2 dir = new Vec2(0,0);
	private Vec2 shootDir;
	private Vec2 mousePos;
	private float playBackSpeed = 1.0f;
	private Population pop;
	private Controls bp_control;
	
	public static void main(String[] args) {
		launch (args);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("EvoSim - v"+ version);
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene(root, 1200, 600);
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
		
		bp_control = new Controls();
		
		root.setBottom(bp_control);
		root.setCenter(screen);
		
		
		primaryStage.show();
		
		
		
		primaryStage.heightProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		primaryStage.widthProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		
		pop = new Population(100);
		pop.CreateRandPopulation();
		
		test = new Test(new Vec2(0.0f, -9.81f));
		test.setCreature(pop.getNext());
		test.startTest();
		
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
		if (e.getCode() == KeyCode.W) {
			dir.y = 0;
		}
		if (e.getCode() == KeyCode.A) {
			dir.x = 0;
		}
		if (e.getCode() == KeyCode.S) {
			dir.y = 0;
		}
		if (e.getCode() == KeyCode.D) {
			dir.x = 0;
		}
		if (e.getCode() == KeyCode.S) {
			dir.x = 0;
		}
		dir.normalize();
	}

	private void onKeyScreen(KeyEvent e) {
		if (e.getCode() == KeyCode.W) {
			dir.y += 1;
		}
		if (e.getCode() == KeyCode.A) {
			dir.x += -1;
		}
		if (e.getCode() == KeyCode.S) {
			dir.y += -1;
		}
		if (e.getCode() == KeyCode.D) {
			dir.x += 1;
		}
		if (e.getCode() == KeyCode.Q) {
			screen.setScale(screen.getScale()*0.8f);
		}
		if (e.getCode() == KeyCode.E) {
			screen.setScale(screen.getScale()*1.2f);
		}
		if (e.getCode() == KeyCode.SPACE) {	
			int gen = pop.getGen();
			while (pop.getGen() < gen + 10) {
				manageTest(1.0f);
			}
		}
		if (e.getCode() == KeyCode.J) {
			playBackSpeed /= 2.0f;
			System.out.println("SPEED: " + playBackSpeed);
		}
		if (e.getCode() == KeyCode.L) {
			if (playBackSpeed * 2 > 1500.0f) playBackSpeed = 1024.0f;
			else playBackSpeed *= 2.0f;
			System.out.println("SPEED: " + playBackSpeed);
		}
		dir.normalize();
	}

	private void refreshScreen(double dt) {
		manageTest(dt);
		screen.addPos(dir.mul((float) (dt * 1000/screen.getScale())));
		screen.clearScreen();
		
		for (B2DBody b : test.worldInstancesList) {
			screen.drawBody(b);
		}
		for (B2DBody b : test.creatureInstancesList) {
			screen.drawBody(b);
		}
		
		if (shootDir != null && mousePos != null) {
			screen.drawLine(shootDir, mousePos, Color.RED);
		}
		
		// HANDLE CONTROLS
		while (true) {
			ContolFunc cf = bp_control.getLastInput();
			if (cf == null) break;
			
			switch (cf) {
				case PLAY:
					playBackSpeed = 1.0f;
					System.out.println("PLAY");
					break;
				case PAUSE:
					playBackSpeed = 0.0f;
					System.out.println("PAUSE");
					break;
				case FAST:
					playBackSpeed *= 2.0f;
					System.out.println("SPEED: " + playBackSpeed);
					break;
				case SLOW:
					playBackSpeed *= 0.5f;
					System.out.println("SPEED: " + playBackSpeed);
					break;
				default:
					System.err.println("CONTROL FUNC ENUM ERROR!");
					break;
			}
		}
	}
	
	private void manageTest(double dt) {
		if (test.taskDone) {
		
		pop.setCurrentFitness(test.getLastFitness());
		System.out.println("ID: "+ pop.getCurrent().id + " | Fitness:" + pop.getCurrent().getFitness());
		
			if (pop.getNext() == null) {
				pop.nextGen();
				pop.sortPopulation();
				pop.killPercentage(0.8f);
				pop.mutatePop(0.8f);
				pop.getNext();
			}

			test.reset();
			test.setCreature(pop.getCurrent());
			test.startTest();
		}

		test.step((float) dt, playBackSpeed);
	}
	
	private void onPressedScreen(MouseEvent e) {
		mousePos = screen.camera.coordPixelsToWorld(e.getX(), e.getY());	
		shootDir = screen.camera.coordPixelsToWorld(e.getX(), e.getY());
	}

	private void onClickScreen(MouseEvent e) {
		mousePos = screen.camera.coordPixelsToWorld(e.getX(), e.getY());	
		shootDir = null;
	}
	
	private void onMoveScreen(MouseEvent e) {	
		mousePos = screen.camera.coordPixelsToWorld(e.getX(), e.getY());	
	}
	
	private void onEnteredScreen(MouseEvent e) {
		mousePos = screen.camera.coordPixelsToWorld(e.getX(), e.getY());	
	}

	private void stageResize(Stage s) {
		screen.setScreenSize((int) s.getWidth()-100, (int) s.getHeight()-100); 
	}

}
