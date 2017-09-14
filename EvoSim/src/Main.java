import org.jbox2d.common.Vec2;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{
	private static String version = "0.0";
	GraphicsContext gc;
	private Screen screen;
	
	public static void main(String[] args) {
		launch (args);
		Vec2 v = new Vec2(2,2);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("EvoSim - v"+ version);
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene(root, 600, 600);
		scene.setFill(Color.color(0.8, 0.8, 1));
		
		primaryStage.setScene(scene);
		screen = new Screen(500, 500);	
		screen.setOnMouseClicked(e -> onClickScreen(e));
		
		root.setCenter(screen);
		
		primaryStage.show();
		
		primaryStage.heightProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		primaryStage.widthProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		
		Vec2 v = new Vec2(2, -1);
		v.toString();
	}

	private void onClickScreen(MouseEvent e) {
		screen.drawRect(e.getX(), e.getY(), 30, 30, Color.BLUE, true);
	}

	private void stageResize(Stage s) {
		System.out.println("stage has been resized");
		//canvas.setHeight(s.getHeight()-100);
		//canvas.setWidth(s.getWidth()-100);
		//gc.setFill(Color.color(1, 0.8, 0.8));
		//gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
	}

}
