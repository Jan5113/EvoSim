package fileMgr;
import display.Layout;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SaveAsWindow {
	static Insets ins_standard = new Insets(12, 12, 12, 12);
	static String answer = "";

	public static String display(String title, String message) {
		Stage window = new Stage();
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);
		scene.setFill(Color.color(0.8, 0.8, 1));
		Layout.setCSS(scene);
		
		//window.getIcons().add(new Image("file:icon.png"));
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText(message);
		label.setTextAlignment(TextAlignment.CENTER);
		
		TextField textfield = new TextField("untitled_population");
		BorderPane.setMargin(textfield, ins_standard);

		Button btn_ok = new Button("Save");
		Button btn_cancel = new Button("Cancel");
		Layout.button(btn_cancel);
		Layout.button(btn_ok);
		
		btn_ok.setOnAction(e -> {
			answer = textfield.getText();
			window.close();
		});
		
		btn_cancel.setOnAction(e -> {
			answer = "&cancel&";
			window.close();
		});
		
		window.setOnCloseRequest(e -> {
			answer = "&cancel&";
		});
		
		root.setTop(label);
		root.setCenter(textfield);
		BorderPane.setAlignment(label, Pos.CENTER);
		BorderPane.setMargin(label, ins_standard);
		HBox hb_buttons = new HBox(40);
		hb_buttons.getChildren().addAll(btn_ok, btn_cancel);
		hb_buttons.setAlignment(Pos.CENTER);
		root.setBottom(hb_buttons);
		BorderPane.setMargin(hb_buttons, ins_standard);
		
		window.setScene(scene);
		btn_cancel.requestFocus();
		window.showAndWait();
		
		return answer;
	}

}
