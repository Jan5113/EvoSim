package display;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import test.Test;

/**
 * The {@link Layout} class contains static functions for styling JavaFX
 * elements. An external CSS-File can also be applied to the main {@link Scene}
 * for some extra fanciness.
 * <p>
 * Generally, every <strong>visible</strong> screen element is given the
 * standard {@link Insets} by calling {@code defaultMargin(screenElement)}.
 * The {@code root} {@link BorderPane} of the {@link Scene} is given a padding with
 * {@code rootPadding(rootElement)}.This ensures consistent spacings between
	 * all screen elements and the window.
 * <p>
 * The CSS-File is saved externally in {@code EvoSim/src/style.css}
 */
public class Layout {
	/**
	 * Holds the default gap size (in {@code px}) between screen elements.
	 */
	private static float gap = 15;
	/**
	 * Holds the default horizontal gap size (in {@code px}) between {@link GridPane} elements.
	 */
	private static float HGap = 10;
	/**
	 * Holds the default vertical gap size (in {@code px}) between {@link GridPane} elements.
	 */
	private static float VGap = 10;
	/**
	 * Holds the default width (in {@code px}) of screen elements.
	 */
	private static float prefWidth = 100;

	/**
	 * Sets the CSS styling for the given {@link Button} and sets its width to the
	 * preferred element width.
	 * <p>
	 * <strong>NOTE: </strong> {@code defaultInsets()} are not being applied!
	 * 
	 * @param btn
	 *            is set to default width and styling
	 */
	public static void button(Button btn) {
		btn.getStyleClass().add("standard-button");
		btn.setMinWidth(prefWidth);
	}
	
	/**
	 * Sets the CSS styling for the given {@link Button} and sets its width to the
	 * to double the preferred element width and it's height to {@code 50px}. The
	 * horizontal gap for {@link GridPane} elements is considered. Text size is also
	 * increased.
	 * <p>
	 * <strong>NOTE: </strong> {@code defaultInsets()} are not being applied!
	 * 
	 * @param btn
	 *            is set to double width and extra height with button styling,
	 *            font-size is increased
	 */
	public static void tallwideButton(Button btn) {
		btn.getStyleClass().add("standard-button");
		btn.getStyleClass().add("large-text");
		btn.setPrefWidth(2 * prefWidth + HGap);
		btn.setMinHeight(50);

	}
	
	/**
	 * Sets the CSS styling for the given {@link Button} and sets its width to the
	 * to double the preferred element width. The horizontal gap for
	 * {@link GridPane} elements is considered.
	 * <p>
	 * <strong>NOTE: </strong> {@code defaultInsets()} are not being applied!
	 * 
	 * @param btn
	 *            is set to double width with button styling
	 */
	public static void wideButton(Button btn) {
		button(btn);
		btn.setPrefWidth(2 * prefWidth + HGap);
	}
	
	/**
	 * Sets the CSS styling for the given {@link Button} and sets its dimensions to
	 * {@code 32px} by {@code 32px}. The font-size is increased to only fit one
	 * character into the element. (Use-case e.g.: help button)
	 * <p>
	 * <strong>NOTE: </strong> {@code defaultInsets()} are not being applied!
	 * 
	 * @param btn
	 *            is set a small square with button styling and increased font-size
	 */
	public static void squareButton(Button btn) {
		btn.getStyleClass().add("standard-button");
		btn.getStyleClass().add("nopadding-button");
		btn.getStyleClass().add("xl-text");
		btn.setMinSize(32, 32);
	}
	
	/**
	 * Sets the CSS styling for the given {@link Button} and sets its width to the
	 * to 2/3 of the preferred element width. The horizontal gap for
	 * {@link GridPane} elements is considered.
	 * <p>
	 * <strong>NOTE: </strong> {@code defaultInsets()} are not being applied!
	 * 
	 * @param btn
	 *            is set to 2/3 width with button styling
	 */
	public static void TwoThirdsButton(Button btn) {
		button(btn);
		btn.setPrefWidth(((prefWidth * 2.0f) - HGap)/3.0f);
	}

	/**
	 * Sets the default layout and spacings for {@link GridPane} instances.
	 * <p>
	 * <strong>NOTE: </strong> {@code defaultInsets()} are applied!
	 * 
	 * @param gp
	 *            is set to the default layout
	 */
	public static void gridPane(GridPane gp) {
		gp.setVgap(VGap);
		gp.setHgap(HGap);
		defaultMargin(gp);
	}

	/**
	 * Increases the font-size of the {@link Label} given to make a title.
	 * <p>
	 * <strong>NOTE: </strong> {@code defaultInsets()} are applied!
	 * 
	 * @param lbl
	 *            is given a title-layout
	 * 
	 */
	public static void labelTitle(Label lbl) {
		lbl.setStyle("-fx-font-size: 21px;");
		defaultMargin(lbl);
	}
	
	/**
	 * Gives the default background {@link Color} of a {@link TestScreen}
	 * 
	 * @return {@link Color} of the sky when {@link TestScreen} is running
	 */
	public static Color getSkycolor() {
		return Color.web("d3e8f8");
	}

	/**
	 * Gives the default background {@link Color} of a {@link TestScreen} when it is
	 * inactive / {@link Test} is over.
	 * 
	 * @return {@link Color} of the sky when {@link TestScreen} is not running
	 */
	public static Color getSkycolorInactive() {
		return Color.web("bddcf4");
	}
	
	/**
	 * Sets the default margins for every <strong>visible </strong> screen element.
	 * <p>
	 * Generally, every <strong>visible</strong> screen element is given the
	 * standard {@link Insets} by calling this method. The {@code root} {@link Node}
	 * of the {@link Scene} is given a padding with
	 * {@code rootPadding(rootElement)}. This ensures consistent spacings between
	 * all screen elements and the window.
	 * 
	 * @param node
	 *            is given the standard margins
	 */
	public static void defaultMargin (Node node) {
		BorderPane.setMargin(node, new Insets(gap, 0, 0, gap));
		VBox.setMargin(node, new Insets(gap, 0, 0, gap));
		HBox.setMargin(node, new Insets(gap, 0, 0, gap));
	}
	
	/**
	 * Sets the padding for the {@code root} {@link BorderPane} of the {@link Scene}. If
	 * all other <strong>visible </strong> screen elements are given the
	 * {@code defaultMargin()} this will ensures consistent spacings between all
	 * screen elements and the window.
	 * 
	 * @param bp
	 * 			{@code root} {@link BorderPane} of the main {@link Scene}
	 */
	public static void rootPadding(BorderPane bp) {
		bp.setPadding(new Insets(0, gap, gap, 0));
	}
	
	/**
	 * Gets the CSS style sheet for a scene.
	 * 
	 * @param s
	 *            is given a CSS style sheet
	 */
	public static void setCSS(Scene s) {
		s.getStylesheets().add("style.css");
	}

	/**
	 * This method sets a default layout for a {@link Label}
	 * <p>
	 * <strong>NOTE: </strong> {@code defaultInsets()} are applied!
	 * 
	 * @param l
	 *            {@link Label} in
	 */
	public static void instrLabel(Label l) {
		defaultMargin(l);
		l.setWrapText(true);
		l.setStyle("-fx-font-size: 16px;");
		
	}

	public static void labelBigTitle(Label l) {
		l.setStyle("-fx-font-size: 30px;");
		Layout.defaultMargin(l);
		l.autosize();
	}

	public static void setBackgroundCol(Node n) {
		n.setStyle("-fx-background-color: d3e8f8;");
	}

	public static void italics(Label label) {
		label.setStyle("-fx-font-style: italic;");
	}
	
	public static void setSize(Label l, int px) {
		l.setStyle("-fx-font-size: " + px + "px;");
	}
	
	public static void setBold(Label l) {
		l.setStyle("-fx-font-weight: bold");
	}
}
