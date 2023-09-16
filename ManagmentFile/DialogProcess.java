package ManagmentFile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Main.Main;
import Settings.Settings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*            //////////////////////////////////
*           //								    //
*           //    //          //                //
*           //    ////      ////    //////      //
*           //	  //  //  //  //  //            //
*           //	  //    //    //  //            //
*           //	  //          //    //////      //
*           //    //          //          //	//
*           //    //          //          //    //
*           //    //          //  ////////      //
*           //                                  //
*             /////// Millenniar Studios ///////
*/
class DialogProcess implements Initializable {

	private Dialog<ButtonType> dialog;
	private String title;
	
	private ManagmentFile task;
	
	@FXML
	private Label message;
	@FXML
	private Label state;
	@FXML
	private ProgressBar progress;
	
	DialogProcess(ManagmentFile task, String title) {
		this.task = task;
		this.title = title;
	}

	void show() {
		try {
			FXMLLoader loader = new FXMLLoader(Main.getApl().getClass().getResource("/ManagmentFile/Process.fxml"));
			loader.setController(this);
			DialogPane dialogPane = loader.load();
			dialogPane.setHeaderText(title);
			dialogPane.getStylesheets().add("resource/teme/teme" + Settings.getSetting(Settings.TEME) + ".css");

            dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Millenniar Texture - " + title);
            ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Main.getApl().getClass().getResourceAsStream("/icon.png")));

        	ButtonType button = dialog.showAndWait().orElse(ButtonType.CANCEL);
        	if(button.equals(ButtonType.CANCEL)) {
        		task.stop();
        	}
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		message.setText(task.getMessage() != null ? task.getMessage() : "Waiting...");
		progress.setProgress(task.getProgress());
		state.setText(task.getValue() != null ? task.getValue()[0] + " / " + task.getValue()[1] : "0 / 0");
		
		task.progressProperty().addListener(e -> {
			progress.setProgress(task.getProgress());
			if(task.getProgress() >= 1)
				dialog.close();
		});
		task.messageProperty().addListener(e -> {
			if(task.getMessage().contains("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\"))
				message.setText(task.getMessage().substring(("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\").length()));
			else if(task.getMessage().contains("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\"))
				message.setText(task.getMessage().substring(("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\").length()));
			else
				message.setText(task.getMessage());
		});
		task.valueProperty().addListener(e -> {
			state.setText(task.getValue()[0] + " / " + task.getValue()[1]);
		});
	}
}
