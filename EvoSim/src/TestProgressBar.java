import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

public class TestProgressBar extends BorderPane{
	private ProgressBar progressBar;

	public TestProgressBar () {
		progressBar = new ProgressBar(0.5);
		
		Label lbl_progressBarTitle = new Label("Calculating Generation");
		
		this.setTop(lbl_progressBarTitle);
		this.setCenter(progressBar);
	}

	public void update(float progress) {
		progressBar.setProgress(progress);
	}

}
