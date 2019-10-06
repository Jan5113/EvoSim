package fileMgr;

import display.Layout;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import population.Population;

public class FileMgrTest extends Application {
	
	BorderPane root;
	private Scene scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new BorderPane();
		scene = new Scene(root, 1200, 700);
		scene.setFill(Color.color(0.8, 0.8, 1));
		Layout.setCSS(scene);
		Layout.rootPadding(root);
		
		Button btn_save = new Button("Save");
		Layout.button(btn_save);
		root.setCenter(btn_save);
		
		btn_save.setOnAction(e -> {
			FileIO.safePopulation(new Population(null, null));
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
