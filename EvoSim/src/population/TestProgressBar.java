package population;
import java.util.ArrayList;

import display.Layout;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

public class TestProgressBar extends BorderPane{
	private ProgressBar progressBar;
	private Label lbl_progressBarTitle;
	
	private int gensInInitialTask;
	private int popSize;

	public TestProgressBar () {
		progressBar = new ProgressBar(0.5);
		progressBar.setMaxWidth(300);
		BorderPane.setMargin(progressBar, new Insets(5, 0, 0, 15));
		
		lbl_progressBarTitle = new Label("Calculating Generation...");
		BorderPane.setAlignment(lbl_progressBarTitle, Pos.CENTER);
		Layout.defaultMargin(lbl_progressBarTitle);
		
		this.setTop(lbl_progressBarTitle);
		this.setCenter(progressBar);
	}
	
	public void setPopSize(int popSize_in) {
		popSize = popSize_in;
	}

	public void update(int testQueueSize, ArrayList<PopulationTask> tasks) {
		int totalGenToCalc = 0;
		for (PopulationTask pt : tasks) {
			if (pt == PopulationTask.CALC_GEN || pt == PopulationTask.COMPLETE_GEN) {
				totalGenToCalc++;
			}
		}
		float totalCretsToCalc = totalGenToCalc * popSize + testQueueSize;
		
		float totalTaskSize = popSize * gensInInitialTask;
		
		progressBar.setProgress((totalTaskSize - totalCretsToCalc) / totalTaskSize);
		
		lbl_progressBarTitle.setText("Calculating Generation " + (gensInInitialTask - totalGenToCalc) + " of " + gensInInitialTask);
	}

	public void setGens(ArrayList<PopulationTask> tasks) {
		gensInInitialTask = 0;
		for (PopulationTask pt : tasks) {
			if (pt == PopulationTask.CALC_GEN || pt == PopulationTask.COMPLETE_GEN) {
				gensInInitialTask++;
			}
		}
	}

}
