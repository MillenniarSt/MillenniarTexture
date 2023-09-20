package io.github.MillenniarSt.MillenniarTexture.ManagmentFile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.Settings.Settings;
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
	private final String title;
	
	private final ManagmentFile task;
	
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
			FXMLLoader loader = new FXMLLoader(Main.getLayout("ManagmentFile/Process"));
			loader.setController(this);
			DialogPane dialogPane = loader.load();
			dialogPane.setHeaderText(title);
			dialogPane.getStylesheets().add(Settings.getTheme());

            dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Millenniar Texture - " + title);
            ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(Main.getResource("icon"));

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
