package display;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Layout {
	private static Insets insetsDef = new Insets(15, 0, 0, 15);
	private static float HGap = 10;
	private static float VGap = 10;
	private static float prefWidth = 100;

	public static void button(Button btn) {
		btn.getStyleClass().add("standard-button");
		btn.setPrefWidth(prefWidth);

	}
	
	public static void tallButton(Button btn) {
		btn.getStyleClass().add("standard-button");
		btn.getStyleClass().add("large-text");
		btn.setPrefWidth(2 * prefWidth + HGap);
		btn.setMinHeight(50);

	}

	public static void wideButton(Button btn) {
		button(btn);
		btn.setPrefWidth(2 * prefWidth + HGap);
	}
	
	public static void squareButton(Button btn) {
		button(btn);
		btn.setPrefSize(40, 40);
	}
	
	public static void TwoThirdsButton(Button btn) {
		button(btn);
		btn.setPrefWidth(((prefWidth * 2.0f) - HGap)/3.0f);
	}

	public static void gridPane(GridPane gp) {
		gp.setVgap(VGap);
		gp.setHgap(HGap);
	}

	public static void labelTitle(Label lbl) {
		lbl.setStyle("-fx-font-size: 21px;");
	}
	
	public static Color getSkycolor() {
		return Color.web("d3e8f8");
	}
	
	public static Color getSkycolorInactive() {
		return Color.web("bddcf4");
	}
	
	public static void defMargin (Node node) {
		BorderPane.setMargin(node, insetsDef);
		VBox.setMargin(node, insetsDef);
	}
	
	public static void innterTitleMargin (Node node) {
		BorderPane.setMargin(node, new Insets(0,15,15,0));
		VBox.setMargin(node, new Insets(0,15,15,0));
	}
}
