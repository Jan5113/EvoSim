import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

public class TestProgressBar extends BorderPane implements Runnable{
	private ProgressBar progressBar;
	private Task t;
	private float progress = 0;

	public TestProgressBar () {
		progressBar = new ProgressBar();
		
		Label lbl_progressBarTitle = new Label("Calculating Generation");
		
		
		this.setTop(lbl_progressBarTitle);
		this.setCenter(progressBar);
	}
	
	public void update(float progress_in) {
		
		progress = progress_in;
	}

	public void start() {
		t = createUpdater();

		progressBar.progressProperty().unbind();
		progressBar.progressProperty().bind(t.progressProperty());
		
		new Thread(t).start();
	}
	
	
	
	public void stop() {
		System.out.println("STOPPED PROGRESSBAR");
	}
	
	private Task createUpdater() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100);
                    updateMessage("update");
                    updateProgress(i + 1, 10);
                }
                return true;
            }
        };
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
