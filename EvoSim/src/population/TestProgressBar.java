package population;
import display.Layout;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class TestProgressBar extends BorderPane{
	private ProgressBar progressBar;

	public TestProgressBar () {
		progressBar = new ProgressBar(0.5);
		progressBar.setMaxWidth(300);
		BorderPane.setMargin(progressBar, new Insets(5, 0, 0, 15));
		
		Label lbl_progressBarTitle = new Label("Calculating Generation");
		BorderPane.setAlignment(lbl_progressBarTitle, Pos.CENTER);
		Layout.defaultMargin(lbl_progressBarTitle);
		lbl_progressBarTitle.setFont(new Font(16));
		
		this.setTop(lbl_progressBarTitle);
		this.setCenter(progressBar);
	}

	public void update(float progress) {
		progressBar.setProgress(progress);
	}

}
