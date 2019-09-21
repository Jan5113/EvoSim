package main;
import org.jbox2d.common.Vec2;

import creatureCreator.CreatorControls;
import creatureCreator.CreatorScreen;
import display.Layout;
import display.PlayBackControls;
import display.PopScreen;
import display.PopScreenControl;
import display.TestScreen;
import fileMgr.FileIO;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import population.Population;
import test.MultiTest;

public class Main extends Application{
	private static String version = "1.1";
	
	private long lastNanoTime;
	private TestScreen mainTestScreen;
	private MultiTest mainMultiTest;
	private CreatorScreen mainCreatorScreen;
	private PlayBackControls bp_testControl;
	private PopScreenControl bp_popControl;
	private CreatorControls bp_creatorControl;
	private VBox bp_pop;
	private BorderPane bp_test;
	private PopScreen popScreen;
	
	private Stage primaryStage;
	private Scene scene;
	public BorderPane root; 
	private Instructions bp_instr;
	private BorderPane bp_evo;
	private boolean showInstr;
	
	private BorderPane bp_showInstr;
	
	private Population pop = new Population(new Vec2(0.0f, -9.81f));
	
	public static void main(String[] args) {
		launch (args);
	}

	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		setup();
	}
	
	private void setup() {
		primaryStage.getIcons().add(new Image("icon.png"));
		primaryStage.setTitle("EvoSim - v"+ version);
		root = new BorderPane();
		scene = new Scene(root, 1200, 700);
		scene.setFill(Color.color(0.8, 0.8, 1));
		Layout.setCSS(scene);
		Layout.rootPadding(root);

		setupMainScreen();
		setupPopScreen();
		setupButton();

		bp_evo = new BorderPane();
		bp_evo.setCenter(bp_test);
		bp_evo.setLeft(bp_pop);
		bp_evo.getChildren().add(bp_showInstr);
		bp_showInstr.setTranslateX(0);
		bp_showInstr.setTranslateY(0);
		bp_showInstr.setMinWidth(210);
		
		bp_instr = new Instructions(this);
		
		primaryStage.heightProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		primaryStage.widthProperty().addListener((obs, old, nev) -> stageResize(primaryStage));
		
		final long startNanoTime = System.nanoTime();
		lastNanoTime = startNanoTime;
	    
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				double dt = (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;

				refreshScreen((float) dt);
			}
		}.start();

		//setBPInstr();
		setBPEvo();
		
		primaryStage.setScene(scene);
		stageResize(primaryStage);
		
		primaryStage.show();
	}
	
	private void setupButton() {
		Button btn_showInstr = new Button("?");
		Layout.squareButton(btn_showInstr);
		Layout.defaultMargin(btn_showInstr);
		btn_showInstr.setOnAction(e -> setBPInstr());
		
		Button btn_save = new Button("Save");
		Layout.twoThirdsButton(btn_save);
		Layout.defaultMargin(btn_save);
		btn_save.setOnAction(e -> FileIO.safePopulation(pop));
		
		Button btn_load = new Button("Load");
		Layout.twoThirdsButton(btn_load);
		Layout.defaultMargin(btn_load);
		btn_load.setOnAction(e -> {
			Population pop_in = FileIO.loadPopulation();
			if (pop_in != null) {
				pop = pop_in;
				pop.initProperty();
				setup();
				setBPEvo();
				System.out.println("Loaded Population");
			} else {
				System.out.println("Couldn't load Population");
			}
		});
		
		bp_showInstr = new BorderPane();
		bp_showInstr.setLeft(btn_save);
		bp_showInstr.setCenter(btn_load);
		bp_showInstr.setRight(btn_showInstr);
	}
	
	public void setBPEvo() {
		root.setCenter(bp_evo);
		showInstr = false;
	}

	public void setBPInstr() {
		root.setCenter(bp_instr);
		showInstr = true;
	}
	
	public void openCreator() {
		((Label) bp_test.getTop()).setText("Creature configurator");
		bp_test.setCenter(mainCreatorScreen);
		bp_test.setBottom(bp_creatorControl);
		
	}
	
	public void closeCreator() {
		((Label) bp_test.getTop()).setText("Creature preview");
		bp_test.setCenter(mainTestScreen);
		bp_test.setBottom(bp_testControl);
	}

	private void setupMainScreen() {
		bp_test = new BorderPane();
		mainTestScreen = new TestScreen(900, 500, 70, new Vec2(0.0f, 3.0f), pop);
		mainTestScreen.setBackgroundCol(Layout.getSkycolor());
		mainTestScreen.setInactiveBackgroundCol(Layout.getSkycolorInactive());
		mainTestScreen.enableInfo();
		mainTestScreen.enableMarkers();
		mainTestScreen.showTimer(true);
		mainTestScreen.showScore(true);
		mainTestScreen.showCurrentFitness(true);
		Layout.defaultMargin(mainTestScreen);
		BorderPane.setAlignment(mainTestScreen, Pos.TOP_LEFT);

		bp_testControl = new PlayBackControls(mainTestScreen);
		
		Label testTitle = new Label("Title");
		Layout.defaultMargin(testTitle);
		Layout.labelTitle(testTitle);
		bp_test.setTop(testTitle);
		
		
		mainCreatorScreen = new CreatorScreen(pop.getLevel(), 900, 500, 70, new Vec2(-0.5f, 1.0f));
		mainCreatorScreen.setBackgroundCol(Layout.getSkycolor());
		mainCreatorScreen.setInactiveBackgroundCol(Layout.getSkycolorInactive());
		mainCreatorScreen.disableInfo();
		mainCreatorScreen.enableGrid();
		mainCreatorScreen.enableMarkers();
		mainCreatorScreen.disableViewLock();
		mainCreatorScreen.camera.setZoom(150f);
		Layout.defaultMargin(mainCreatorScreen);
		BorderPane.setAlignment(mainCreatorScreen, Pos.TOP_LEFT);
		
		bp_creatorControl = new CreatorControls(mainCreatorScreen);
		mainCreatorScreen.setParent(bp_creatorControl);
		closeCreator();
	}

	private void setupPopScreen() {
		bp_pop = new VBox();
		popScreen = new PopScreen(pop, mainTestScreen);
		mainMultiTest = new MultiTest(12, pop);
		bp_popControl = new PopScreenControl(this, mainTestScreen, mainMultiTest, mainCreatorScreen, popScreen, pop);
		Label popTitle = new Label("Population");
		Layout.defaultMargin(popTitle);
		Layout.labelTitle(popTitle);
		
		bp_pop.getChildren().add(popTitle);
		bp_pop.getChildren().add(bp_popControl);
		bp_pop.getChildren().add(popScreen);
	}

	private void refreshScreen(float dt) {
		mainTestScreen.refresh(dt);
		bp_testControl.refresh();
		mainCreatorScreen.refresh();
		popScreen.refresh(dt);
		bp_popControl.refresh();
		
		if (showInstr) bp_instr.refresh(dt);
	}

	private void stageResize(Stage s) {
		mainTestScreen.setScreenSize((int) s.getWidth()-272, (int) s.getHeight()-265); 
		mainCreatorScreen.setScreenSize((int) s.getWidth()-272, (int) s.getHeight()-265); 
		bp_showInstr.setTranslateX((int) s.getWidth() - 243);
	}


}
