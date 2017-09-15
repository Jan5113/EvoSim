import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

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
	
	private World world;
	private long lastNanoTime;
	private static final Vec2 v2_gravity = new Vec2(0, -0);
	private ArrayList<B2DCube> al_cubes = new ArrayList<B2DCube>();
	private Vec2 dir = new Vec2(0,0);
	private Vec2 shootDir;
	private Vec2 mousePos;
	
	public static void main(String[] args) {
		launch (args);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("EvoSim - v"+ version);
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene(root, 600, 600);
		scene.setFill(Color.color(0.8, 0.8, 1));
		
		primaryStage.setScene(scene);
		screen = new Screen(500, 500, 100, new Vec2(0, 2.4f));
		screen.setBackgroundCol(Color.ALICEBLUE);
		screen.setOnMouseReleased(e -> onClickScreen(e));
		screen.setOnMouseMoved(e -> onMoveScreen(e));
		screen.setOnMousePressed(e -> onPressedScreen(e));
		screen.setOnMouseDragged(e -> onEnteredScreen(e));
		scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> onKeyScreen(e));
		scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> offKeyScreen(e));
		
		root.setCenter(screen);
		
		primaryStage.show();
		
		primaryStage.heightProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		primaryStage.widthProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		
		world = new World(v2_gravity);
		al_cubes.add(new B2DCube(0f, 0.2f, 2.0f, 0.1f, BodyType.STATIC, world));	
		
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
			screen.setScale(screen.getScale()*0.8);
		}
		if (e.getCode() == KeyCode.E) {
			screen.setScale(screen.getScale()*1.25);
		}
		if (e.getCode() == KeyCode.SPACE) {	
			for (int i = 0; i < 50; i++) {
				al_cubes.add(new B2DCube(mousePos,
						new Vec2(0.1f * (float)Math.random() + 0.05f, 0.1f* (float)Math.random() + 0.05f),
						new Vec2((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).mul(1.0f), (float) (Math.random()*Math.PI),
						BodyType.DYNAMIC, world));
				
			}
		}
		dir.normalize();
	}

	private void refreshScreen(double dt) {
		world.step((float) dt, 10, 10);
		screen.addPos(dir.mul((float) (dt * 1000/screen.getScale())));
		screen.clearScreen();
		
		for (B2DCube c : al_cubes) {
			screen.drawRect(c, Color.BLUE, false);
		}
		
		if (shootDir != null && mousePos != null) {
			screen.drawLine(shootDir, mousePos, Color.RED);
		}
		
	}
	
	private void onPressedScreen(MouseEvent e) {
		mousePos = screen.cu.coordPixelsToWorld(e.getX(), e.getY());	
		shootDir = screen.cu.coordPixelsToWorld(e.getX(), e.getY());
	}

	private void onClickScreen(MouseEvent e) {
		mousePos = screen.cu.coordPixelsToWorld(e.getX(), e.getY());	
		//al_cubes.add(new B2DCube(ConvertUnits.coordPixelsToWorld(e.getX(), e.getY()), new Vec2(0.1f, 0.1f), BodyType.DYNAMIC, world));
		Vec2 shoot_vel = shootDir.add(screen.cu.coordPixelsToWorld(e.getX(), e.getY()).negate()).mul(5.0f);
		al_cubes.add(new B2DCube(shootDir,
				new Vec2(2f, 2f),
				shoot_vel, (float) (Math.random()*Math.PI),
				BodyType.DYNAMIC, world));
		shootDir = null;
	}
	
	private void onMoveScreen(MouseEvent e) {	
		mousePos = screen.cu.coordPixelsToWorld(e.getX(), e.getY());	
//		al_cubes.add(new B2DCube(screen.cu.coordPixelsToWorld(e.getX(), e.getY()),
//				new Vec2(0.1f * (float)Math.random() + 0.05f, 0.1f* (float)Math.random() + 0.05f),
//				new Vec2((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).mul(10.0f), (float) (Math.random()*Math.PI),
//				BodyType.DYNAMIC, world));
	}
	
	private void onEnteredScreen(MouseEvent e) {
		mousePos = screen.cu.coordPixelsToWorld(e.getX(), e.getY());	
	}

	private void stageResize(Stage s) {
		System.out.println("stage has been resized");
		//canvas.setHeight(s.getHeight()-100);
		//canvas.setWidth(s.getWidth()-100);
		//gc.setFill(Color.color(1, 0.8, 0.8));
		//gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
	}

}
