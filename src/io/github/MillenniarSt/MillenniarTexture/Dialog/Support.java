package io.github.MillenniarSt.MillenniarTexture.Dialog;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;
import io.github.MillenniarSt.MillenniarTexture.Settings.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/*           //////////////////////////////////
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

public class Support implements Initializable, ProgrammePath {

	private static final Support supportDialog = new Support();

	private static final Stage support = new Stage();
	public static Scene supDialog;

	@FXML
	ImageView imageDiscord;
	@FXML
	ImageView imageWebSite;

	public static void show() {
		DialogPane dialogPane;
		try {
			FXMLLoader loader = new FXMLLoader(Main.getLayout("Dialog/Support"));
			loader.setController(supportDialog);
			dialogPane = loader.load();
			dialogPane.getStylesheets().add(Settings.getTheme());

			Dialog<ButtonType> dialog = new Dialog<>();
			dialog.setDialogPane(dialogPane);
			dialog.setTitle("Millenniar Texture - Combo input");
			((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(Main.getResource("icon"));

			dialog.showAndWait();
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		imageDiscord.setImage(Main.getResource("image/discord"));
		imageWebSite.setImage(Main.getResource("image/web_site"));
	}
	
	public void goDiscord() {
		try {
	        Desktop.getDesktop().browse(new URI("https://discord.gg/prCjNU7d2X"));
	        ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Open URL \"https://discord.gg/prCjNU7d2X\"", ConsoleLine.INFO));
	    } catch (URISyntaxException exc) {
	    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Fail to open URL \"https://discord.gg/prCjNU7d2X\" - URISyntaxException in Support.go", ConsoleLine.FATAL, exc));
	    } catch (IOException exc) {
	    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Fail to open URL \"https://discord.gg/prCjNU7d2X\" - IOException in Support.go", ConsoleLine.ERROR, exc));
	    }
	}

	public void goWebSite() {
		try {
	        Desktop.getDesktop().browse(new URI("https://millenniar-studios.webnode.it"));
	        ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Open URL \"https://millenniar-studios.webnode.it\"", ConsoleLine.INFO));
	    } catch (URISyntaxException exc) {
	    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Fail to open URL \"https://millenniar-studios.webnode.it\" - URISyntaxException in Support.go", ConsoleLine.FATAL, exc));
	    } catch (IOException exc) {
	    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Fail to open URL \"https://millenniar-studios.webnode.it\" - IOException in Support.go", ConsoleLine.ERROR, exc));
	    }
	}
	
	public void close() {
		support.close();
	}

	public static Stage getSupport() {
		return support;
	}
}
