package fileMgr;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;

import display.Layout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import population.Population;

public class OpenWindow {
	private static Insets ins_standard = new Insets(12, 12, 12, 12);
	private static TableView<File> tbl_files;
	private static int selectedIndex = 0;
	private static File[] files;
	private static Population pop_load;
	private static Button btn_open;

	@SuppressWarnings("unchecked")
	public static Population display(File[] files) {
		OpenWindow.files = files;
		
		Stage window = new Stage();
		
		btn_open = new Button("Load");
		Button btn_cancel = new Button("Cancel");
		HBox hb_btns = new HBox();
		hb_btns.getChildren().add(btn_cancel);
		hb_btns.getChildren().add(btn_open);
		Layout.button(btn_cancel);
		Layout.button(btn_open);
		Layout.defaultMargin(btn_cancel);
		Layout.defaultMargin(btn_open);
		hb_btns.setAlignment(Pos.CENTER_RIGHT);
		
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Load Population");

		BorderPane layout = new BorderPane();

		tbl_files = new TableView<File>();
		layout.setCenter(tbl_files);
		layout.setBottom(hb_btns);

		BorderPane.setAlignment(tbl_files, Pos.CENTER);
		BorderPane.setMargin(tbl_files, ins_standard);

		BorderPane.setAlignment(hb_btns, Pos.BOTTOM_RIGHT);
		BorderPane.setMargin(hb_btns, ins_standard);

		btn_open.setOnAction(e -> {
			open();
			window.close();
		});
		btn_open.setDisable(true);
		
		btn_cancel.setOnAction(e -> {
			pop_load = null;
			window.close();
		});

		tbl_files.getSelectionModel().selectedItemProperty().addListener((obs_val, old_val, new_val) -> changeSelection());
		tbl_files.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                open();
		    			window.close();
		            }
		        }
		    }
		});

		Scene scene = new Scene(layout);;
		scene.setFill(Color.color(0.8, 0.8, 1));
		Layout.setCSS(scene);

		window.setScene(scene);
		
		Arrays.sort(files);
		String[] fileNames = new String[files.length];
		
		for (int i = 0; i < files.length; i++) {
			fileNames[i] = files[i].getName();
		}
		
		TableColumn<File, String> tclm_setName = new TableColumn<>("File Name");
		tclm_setName.setPrefWidth(300);
		tclm_setName.setCellValueFactory(new PropertyValueFactory<File, String>("Name"));
		
		ObservableList<File> tableSets = FXCollections.observableArrayList(files);

		tbl_files.setItems(tableSets);
		tbl_files.getColumns().addAll(tclm_setName);
		
		btn_cancel.requestFocus();
		window.showAndWait();

		return pop_load;
	}

	private static void open() {
		File f = files[selectedIndex];
		
		ObjectInputStream input;
		FileInputStream fis;
		Population inputPop = null;
		
		try {
			fis = new FileInputStream(f);
			input = new ObjectInputStream(fis);
			
			inputPop = (Population) input.readObject();
			fis.close();
			input.close();
		} catch (Exception e) {
			System.err.println("Couldn't read File: " + e.getMessage());
			e.printStackTrace();
		}
		
		pop_load = inputPop;
	}

	private static void changeSelection() {
		if (!tbl_files.getSelectionModel().isEmpty()){
			selectedIndex = tbl_files.getSelectionModel().getSelectedIndex();
			btn_open.setDisable(false);
		} else {
			btn_open.setDisable(true);
		}
	}
}