import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class Controls extends BorderPane{
	private ArrayList<ContolFunc> inputStack = new ArrayList<ContolFunc>();
	
	GridPane gp_controls = new GridPane();
	Button btn_play = new Button("Play");
	Button btn_pause = new Button("Pause");
	Button btn_fast = new Button(">>>");
	Button btn_slow = new Button(">");
	
	Insets insetsDef = new Insets(6, 6, 6, 6);
	
	public Controls() {
		this.setCenter(gp_controls);
		
		GridPane.setMargin(btn_play, insetsDef);
		GridPane.setMargin(btn_pause, insetsDef);
		GridPane.setMargin(btn_fast, insetsDef);
		GridPane.setMargin(btn_slow, insetsDef);
		
		gp_controls.add(btn_play, 0, 0);
		gp_controls.add(btn_pause, 1, 0);
		gp_controls.add(btn_fast, 0, 1);
		gp_controls.add(btn_slow, 1, 1);
		
		btn_play.setOnAction(e -> inputStack.add(ContolFunc.PLAY));
		btn_pause.setOnAction(e -> inputStack.add(ContolFunc.PAUSE));
		btn_fast.setOnAction(e -> inputStack.add(ContolFunc.FAST));
		btn_slow.setOnAction(e -> inputStack.add(ContolFunc.SLOW));
	}
	
	public ContolFunc getLastInput() {
		if (inputStack.size() == 0) return null;
		return inputStack.remove(0);
	}
	
	public int getStackSize() {
		return inputStack.size();
	}
	

}
