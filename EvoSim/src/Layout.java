import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Layout {
	private static Insets insetsDef = new Insets(15, 0, 0, 15);
	private static float HGap = 10;
	private static float VGap = 10;
	private static float prefWidth = 80;

	public static void button(Button btn) {
		btn.setStyle("-fx-background-color:" + "#000000," + "linear-gradient(#7ebcea, #2f4b8f),"
				+ "linear-gradient(#426ab7, #263e75)," + "linear-gradient(#395cab, #223768);"
				+ "-fx-background-insets: 0,1,2,3;" + "-fx-background-radius: 0;" + "-fx-padding: 8 4;"
				+ "-fx-text-fill: white;" + "-fx-font-size: 12px;");
		btn.setPrefWidth(prefWidth);

	}

	public static void wideButton(Button btn) {
		button(btn);
		btn.setPrefWidth(2 * prefWidth + HGap);
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
		return Color.color(0.85, 0.90, 0.95);
	}
	
	public static void defMargin (Node node) {
		BorderPane.setMargin(node, insetsDef);
	}
	
	public static void innterTitleMargin (Node node) {
		BorderPane.setMargin(node, new Insets(0,15,15,0));
	}
}
