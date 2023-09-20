package io.github.MillenniarSt.MillenniarTexture.Files.TXT;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.Files.FileTx;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.TextureObject;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
import io.github.MillenniarSt.MillenniarTexture.Menu.ManagmentStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

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

public class FileTXT extends FileTx implements Initializable, ProgrammePath {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@FXML
	private Label pathLabel;
	@FXML
	private TextArea textArea;
	
	private String extension;
	private String text;
	private final SaveTXT savedTXT;
	
	public FileTXT(String path, SaveTXT savedTXT) {
		super(path, savedTXT, new ImageView(Main.getResource("icon/file_txt")));
		this.extension = savedTXT.getExtension();
		this.text = savedTXT.getText();
		this.savedTXT = savedTXT;
	}
	
	public FileTXT(String path, SaveTXT savedTXT, ImageView icon) {
		super(path, savedTXT, icon);
		this.extension = savedTXT.getExtension();
		this.text = savedTXT.getText();
		this.savedTXT = savedTXT;
	}
	
	public FileTXT(String path, boolean activated) {
		super(path, "text", activated, new ImageView(Main.getResource("icon/file_txt")));
		this.extension = "txt";
		this.savedTXT = new SaveTXT(this);
	}
	
	public FileTXT(String path, boolean activated, ArrayList<TextureObject> sources) {
		super(path, "text", activated, new ImageView(Main.getResource("icon/file_txt")), sources);
		this.extension = "txt";
		this.savedTXT = new SaveTXT(this);
	}
	
	public FileTXT(String path, boolean activated, ImageView icon, String extension) {
		super(path, "text", activated, icon);
		this.extension = extension;
		this.savedTXT = new SaveTXT(this);
	}
	
	public FileTXT(String path, boolean activated, ImageView icon, ArrayList<TextureObject> sources, String extension) {
		super(path, "text", activated, icon, sources);
		this.extension = extension;
		this.savedTXT = new SaveTXT(this);
	}
	
	@Override
	public void open(ManagmentStage stage) {
		if(this.getTab() == null) {
			try {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Starting to open txt file \"" + this.getPath() + "\"...", ConsoleLine.INFO));
				
				read();
				
				FXMLLoader loader = new FXMLLoader(Main.getLayout("/Files/TXT/TabTXT"));
				loader.setController(this);
				this.setTab(loader.load());
				this.getTab().setText(this.getName());
				this.getTab().setGraphic(new ImageView(Main.getResource("icon/file_txt")));
				stage.getFilesTabPane().getTabs().add(this.getTab());
				stage.getFilesTabPane().getSelectionModel().select(this.getTab());
			} catch (FileNotFoundException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open TXT file \"" + this + "\" - FileNotFoundException in FileTXT.open", ConsoleLine.CRASH, exc));
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open TXT file \"" + this + "\" - IOException in FileTXT.open", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open TXT file \"" + this + "\" - Exception in FileTXT.open", ConsoleLine.CRASH, exc));
			}
		} else {
			stage.getFilesTabPane().getSelectionModel().select(this.getTab());
		}
	}
	
	@Override
	public void read() {
		String output = "";
		try {
			FileReader reader = new FileReader(this);
			int next = reader.read();
			while(next != -1) {
				output = output + (char) next;
				next = reader.read();
			}
			try {
				reader.close();
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to close file reader - IOException in FileTXT.read.stop", ConsoleLine.WARN, exc));
			}
		} catch (FileNotFoundException exc) {
			output = "Fail to read text file \"" + this + "\"";
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to read file \"" + this + "\" - FileNotFoundException in FileTXT.read", ConsoleLine.ERROR, exc));
		} catch (IOException exc) {
			output = "Fail to read text file \"" + this + "\"";
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to read file \"" + this + "\" - IOException in FileTXT.read", ConsoleLine.ERROR, exc));
		}
		this.text = output;
	}
	
	public void change() {
		this.setSave(false);
	}
	
	@Override
	public boolean save() {
		try {
			FileWriter writer = new FileWriter(this);
			writer.write(this.textArea.getText());
			writer.close();
			this.register();
			this.setSave(true);
			return true;
		} catch(FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to save JSON file \"" + this + "\" the value will be 0 - FileNotFoundException in FileJSON.save", ConsoleLine.FATAL, exc));
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to save JSON file \"" + this + "\" the value will be 0 - IOException in FileJSON.save", ConsoleLine.FATAL, exc));
		}
		return false;
	}
	@Override
	public void register() {
		ManagmentStage.getOn().getTexturePack().getObjects().putFile(this.getPath(), this.savedTXT);
	}
	
	public void close() {
		if(!this.isSave()) {
			String confirm = MSDialog.showConfirm("Save changes", "Do you want to save the changes", "Yes", "No", "Cancel");
			if(confirm.equals("Yes")) {
				this.save();
				this.getTab().getTabPane().getTabs().remove(this.getTab());
				this.setSave(true);
				this.setTab(null);
			} else if(confirm.equals("No")) {
				this.getTab().getTabPane().getTabs().remove(this.getTab());
				this.setSave(true);
				this.setTab(null);
			}
		} else {
			this.getTab().getTabPane().getTabs().remove(this.getTab());
			this.setSave(true);
			this.setTab(null);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		textArea.setText(text);
		pathLabel.setText(this.getPath().substring(buildTexture.length()));
	}
	
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
		savedTXT.setExtension(this.extension);
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		savedTXT.setText(this.text);
	}
}
