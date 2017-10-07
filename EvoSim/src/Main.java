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
	byte scaleZahl = 0;
	
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
		
		root.setCenter(screen);
		
		primaryStage.show();
		
		
		
		primaryStage.heightProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		primaryStage.widthProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		
		test = new Test(new Vec2(0.0f, -9.81f));
		
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
			if(scaleZahl <= 5){
			screen.setScale(screen.getScale()*0.8);
			scaleZahl = (byte) (scaleZahl + 1);
		}
		}
		if (e.getCode() == KeyCode.E) {
			if(scaleZahl >= -5){
			screen.setScale(screen.getScale()*1.2);
			scaleZahl = (byte) (scaleZahl - 1);
		}
		}
		if (e.getCode() == KeyCode.SPACE) {	
			for (int i = 0; i < 50; i++) {
//				B2DBody tempB2DBody = new B2DBody();
//				tempB2DBody.setUpCuboid(mousePos, new Vec2(0.1f * (float)Math.random() + 0.05f, 0.1f* (float)Math.random() + 0.05f),
//						(float) (Math.random()*Math.PI), BodyType.DYNAMIC);
//				tempB2DBody.setLinearVelocity(new Vec2((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).mul(1.0f));
//				tempB2DBody.createBody(world);
//				al_bodies.add(tempB2DBody);					
			}
		}
		dir.normalize();
	}

	private void refreshScreen(double dt) {
		test.step((float) dt);
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
		
	}
	
	private void onPressedScreen(MouseEvent e) {
		mousePos = screen.cu.coordPixelsToWorld(e.getX(), e.getY());	
		shootDir = screen.cu.coordPixelsToWorld(e.getX(), e.getY());
	}

	private void onClickScreen(MouseEvent e) {
		mousePos = screen.cu.coordPixelsToWorld(e.getX(), e.getY());	
		//al_cubes.add(new B2DCube(ConvertUnits.coordPixelsToWorld(e.getX(), e.getY()), new Vec2(0.1f, 0.1f), BodyType.DYNAMIC, world));
		
		
//		Vec2 shoot_vel = shootDir.add(screen.cu.coordPixelsToWorld(e.getX(), e.getY()).negate()).mul(5.0f);
//
//		B2DBody tempB2DBody = new B2DBody();
//		tempB2DBody.setUpCircle(shootDir, 0.2f,	(float) (Math.random()*Math.PI*2), BodyType.DYNAMIC);
//		tempB2DBody.setLinearVelocity(new Vec2((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).mul(1.0f));
//		tempB2DBody.setLinearVelocity(shoot_vel);
//		tempB2DBody.setColor(Color.RED);
//		tempB2DBody.createBody(world);
//		al_bodies.add(tempB2DBody);
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
		//System.out.println("stage has been resized");
		//canvas.setHeight(s.getHeight()-100);
		//canvas.setWidth(s.getWidth()-100);
		//gc.setFill(Color.color(1, 0.8, 0.8));
		//gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		screen.setScreenSize((int) s.getWidth()-100, (int) s.getHeight()-100); 
	}

}
