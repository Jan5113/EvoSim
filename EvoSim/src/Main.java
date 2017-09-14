import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{
	private static String version = "0.0";
	GraphicsContext gc;
	private Screen screen;
	
	private World world;
	private long lastNanoTime;
	private static final Vec2 v2_gravity = new Vec2(0, -10);
	private ArrayList<B2DCube> al_cubes = new ArrayList<B2DCube>();
	
	public static void main(String[] args) {
		launch (args);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("EvoSim - v"+ version);
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene(root, 600, 600);
		scene.setFill(Color.color(0.8, 0.8, 1));
		
		primaryStage.setScene(scene);
		screen = new Screen(500, 500);	
		screen.setOnMouseClicked(e -> onClickScreen(e));
		screen.setOnMouseMoved(e -> onPressedScreen(e));
		
		root.setCenter(screen);
		
		primaryStage.show();
		
		primaryStage.heightProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		primaryStage.widthProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		
		world = new World(v2_gravity);
		al_cubes.add(new B2DCube(2.5f, 0.5f, 2.0f, 0.1f, BodyType.STATIC, world));	
		
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
	
	private void refreshScreen(double dt) {
		screen.clearScreen();
		world.step((float) dt, 10, 10);
		
		for (B2DCube c : al_cubes) {
			screen.drawRect(c, Color.BLUE, false);
		}
	}

	private void onClickScreen(MouseEvent e) {
		//al_cubes.add(new B2DCube(ConvertUnits.coordPixelsToWorld(e.getX(), e.getY()), new Vec2(0.1f, 0.1f), BodyType.DYNAMIC, world));
	}
	
	private void onPressedScreen(MouseEvent e) {
		al_cubes.add(new B2DCube(ConvertUnits.coordPixelsToWorld(e.getX(), e.getY()),
				new Vec2(0.1f * (float)Math.random() + 0.05f, 0.1f* (float)Math.random() + 0.05f),
				BodyType.DYNAMIC, world));
	}

	private void stageResize(Stage s) {
		System.out.println("stage has been resized");
		//canvas.setHeight(s.getHeight()-100);
		//canvas.setWidth(s.getWidth()-100);
		//gc.setFill(Color.color(1, 0.8, 0.8));
		//gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
	}

}
