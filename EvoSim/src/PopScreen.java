import org.jbox2d.common.Vec2;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

public class PopScreen extends ScrollPane{
	private TestScreen[] testScreenArr;
	private Population pop;
	private TilePane tp_screens = new TilePane();
	private final TestScreen mainTestScreen;
	
	public PopScreen(Population pop_in, TestScreen testScreen_in) {
		mainTestScreen = testScreen_in;
		pop = pop_in;
		testScreenArr = new TestScreen[pop.getPopulationSize()];
		for (int i = 0; i < testScreenArr.length; i++) {
			int tileNr = i;
			testScreenArr[i] = new TestScreen(150, 150, 15, new Vec2(-1,4), pop);
			testScreenArr[i].addEventHandler(MouseEvent.MOUSE_ENTERED, e -> onMouseEntered(e, tileNr));
			testScreenArr[i].addEventHandler(MouseEvent.MOUSE_EXITED, e -> onMouseExited(e, tileNr));
			testScreenArr[i].addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onMouseClicked(e, tileNr));
			testScreenArr[i].startSingleTest(i);
			testScreenArr[i].setBackgroundCol(Layout.getSkycolor());
			testScreenArr[i].manageCommand(ControlFuncTest.STOP);
			testScreenArr[i].enableAutoRepeat();
			testScreenArr[i].setFollowOffset(new Vec2(-1, 0));
			testScreenArr[i].enableCompactInfo();
			testScreenArr[i].disableScrollZoom();
			tp_screens.getChildren().add(testScreenArr[i]);
		}
		tp_screens.setVgap(10);
		tp_screens.setHgap(10);
		tp_screens.setPrefColumns(3);
		
		this.setPadding(new Insets(13));
		this.setPrefWidth(520);
		this.setContent(tp_screens);
		this.setStyle("-fx-font-size: 18px;"
				+ "-fx-focus-color: transparent;"
				+ "-fx-background-insets: -1.4, 0, 1, 2;");
	}

	private void onMouseClicked(MouseEvent e, int tileNr) {
		if(e.getButton().equals(MouseButton.PRIMARY)){
            if(e.getClickCount() == 2){
        		mainTestScreen.startSingleTest(tileNr);
            }
        }
	}

	private void onMouseExited(MouseEvent e, int tileNr) {
		testScreenArr[tileNr].manageCommand(ControlFuncTest.STOP);
	}

	private void onMouseEntered(MouseEvent e, int tileNr) {
		testScreenArr[tileNr].manageCommand(ControlFuncTest.START);
	}

	public void refresh(float dt) {
		for (int i = 0; i < testScreenArr.length; i++) {
			testScreenArr[i].refresh(dt);
		}
	}
}
