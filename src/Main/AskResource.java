package io.github.MillenniarSt.MillenniarTexture.Main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.UnZip;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class AskResource implements Initializable, ProgrammePath {
	
	static Stage tempStage = null;
	Stage stage;
	
	@FXML
	private TextField fieldResource;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.stage = tempStage;
		fieldResource.setText("C:/Users/" + System.getProperty("user.name") + "/Downloads/resource_mt-2.0.2.zip");
	}

	public void search() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("ZIP Archive", "*.zip"),
		        new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(this.stage);
		if (selectedFile != null) {
			fieldResource.setText(selectedFile.getPath());
		}
	}
	
	public void download() {
		try {
	        Desktop.getDesktop().browse(new URI("https://cdn.discordapp.com/attachments/1029131762162471014/1090313731138920559/resource_mt-3.0.0.zip"));
	        ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Open URL \"https://cdn.discordapp.com/attachments/1029131762162471014/1090313731138920559/resource_mt-3.0.0.zip\"", ConsoleLine.INFO));
	    } catch (URISyntaxException exc) {
	    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Fail to open URL \"https://cdn.discordapp.com/attachments/1029131762162471014/1090313731138920559/resource_mt-3.0.0.zip\" - URISyntaxException in io.github.MillenniarSt.MillenniarTexture.Settings.resource.download", ConsoleLine.FATAL, exc));
	    } catch (IOException exc) {
	    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Fail to open URL \"https://cdn.discordapp.com/attachments/1029131762162471014/1090313731138920559/resource_mt-3.0.0.zip\" - IOException in io.github.MillenniarSt.MillenniarTexture.Settings.resource.download", ConsoleLine.ERROR, exc));
	    }
	}
	
	public void load() {
		new UnZip(fieldResource.getText().trim(), resource, true).execute(true);
			MSDialog.showInformation("Millenniar Texture - Resource", "The programme resource was installed successfully in:\n" + resource);
			this.stage.close();
			((Main) Main.getApl()).run();
	}
}
