import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class Controls extends BorderPane {

	private final TestManager testManager;

	GridPane gp_controls = new GridPane();

	// Playback Controls
	Label lbl_playback = new Label("Playback");
	Button btn_playpause = new Button("Play");
	Button btn_fast = new Button(">>>");
	Button btn_slow = new Button(">");

	// Evo Controls
	Label lbl_evo = new Label("Evolution");
	Button btn_generatePop = new Button("Generate population");
	Button btn_skip1G = new Button("Skip 1 generation");
	Button btn_skip10G = new Button("Skip 10 generations");

	Button btn_best = new Button("Best");
	Button btn_med = new Button("Median");
	Button btn_worst = new Button("Worst");

	public Controls(TestManager testManager_in) {
		testManager = testManager_in;

		this.setCenter(gp_controls);

		layoutGridPane(gp_controls);

		layoutLabelTitle(lbl_playback);
		layoutWideButton(btn_playpause);
		layoutButton(btn_fast);
		layoutButton(btn_slow);

		layoutLabelTitle(lbl_evo);
		layoutWideButton(btn_generatePop);
		layoutWideButton(btn_skip1G);
		layoutWideButton(btn_skip10G);
		
		layoutButton(btn_best);
		layoutButton(btn_med);
		layoutButton(btn_worst);

		gp_controls.add(lbl_evo, 0, 0);
		gp_controls.add(btn_generatePop, 0, 1);

		btn_playpause.setOnAction(e -> testManager.manageCommand(ControlFuncTest.PLAYPAUSE));
		btn_fast.setOnAction(e -> testManager.manageCommand(ControlFuncTest.FAST));
		btn_slow.setOnAction(e -> testManager.manageCommand(ControlFuncTest.SLOW));

		btn_generatePop.setOnAction(e -> generatePopulation());
		btn_skip1G.setOnAction(e -> testManager.manageCommand(ControlFuncTest.SKIP1G));
		btn_skip10G.setOnAction(e -> testManager.manageCommand(ControlFuncTest.SKIP10G));

		btn_best.setOnAction(e -> testManager.manageCommand(ControlFuncTest.SHOWBEST));
		btn_med.setOnAction(e -> testManager.manageCommand(ControlFuncTest.SHOWMID));
		btn_worst.setOnAction(e -> testManager.manageCommand(ControlFuncTest.SHOWWORST));
	}
	
	public void refresh() {
		if (testManager.testIsRunning())
			btn_playpause.setText("Pause");
		else
			btn_playpause.setText("Play");
	}
	
	private void generatePopulation() {
		if (testManager.popIsInit()) {
			System.err.println("Can't call this function! Population already initialised!");
			return;
		}
		testManager.manageCommand(ControlFuncTest.GENERATEPOP);

		gp_controls.getChildren().remove(btn_generatePop);
		gp_controls.getChildren().remove(lbl_evo);

		gp_controls.add(lbl_playback, 0, 0, 2, 1);
		gp_controls.add(btn_playpause, 0, 1, 2, 1);
		gp_controls.add(btn_fast, 1, 2);
		gp_controls.add(btn_slow, 0, 2);

		gp_controls.add(lbl_evo, 2, 0, 2, 1);
		gp_controls.add(btn_skip1G, 2, 1, 2, 1);
		gp_controls.add(btn_skip10G, 2, 2, 2, 1);

		gp_controls.add(btn_best, 4, 1);
		gp_controls.add(btn_med, 5, 1);
		gp_controls.add(btn_worst, 6, 1);
	}

	
	
	
	
	
	
	
	
	
	
	private static Insets insetsDef = new Insets(15, 15, 15, 15);
	private static float HGap = 10;
	private static float VGap = 10;
	private static float prefWidth = 80;

	private void layoutButton(Button btn) {
		btn.setStyle("-fx-background-color:" + "#000000," + "linear-gradient(#7ebcea, #2f4b8f),"
				+ "linear-gradient(#426ab7, #263e75)," + "linear-gradient(#395cab, #223768);"
				+ "-fx-background-insets: 0,1,2,3;" + "-fx-background-radius: 0;" + "-fx-padding: 8 15;"
				+ "-fx-text-fill: white;" + "-fx-font-size: 12px;");
		btn.setPrefWidth(prefWidth);

	}

	private void layoutWideButton(Button btn) {
		layoutButton(btn);
		btn.setPrefWidth(2 * prefWidth + HGap);
	}

	private void layoutGridPane(GridPane gp) {
		gp.setVgap(VGap);
		gp.setHgap(HGap);
		BorderPane.setMargin(gp, insetsDef);
		gp.setStyle("-fx-background-color: #CCCCFF;");
		gp.setPrefHeight(120);
	}

	private void layoutLabelTitle(Label lbl) {
		lbl.setStyle("-fx-font-size: 21px;");
	}

}
