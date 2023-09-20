package io.github.MillenniarSt.MillenniarTexture.Files.MCMETA;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.CreateTexture.CreateTexture;
import io.github.MillenniarSt.MillenniarTexture.Files.TXT.FileTXT;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.Animation;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.TextureObject;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;
import io.github.MillenniarSt.MillenniarTexture.Menu.ManagmentStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class FileMCMETA extends FileTXT implements Initializable, ProgrammePath {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@FXML
	private Label pathLabel;
	@FXML
	private TextArea textArea;
	@FXML
	private ComboBox<Boolean> interpolaseCombo;
	private final ObservableList<Boolean> listInterpolase = FXCollections.observableArrayList(true, false);
	@FXML
	private TextField frameTimeField;
	
	private boolean propertiesEditor;
	private static final String ERROR = "text-field-error";
	
	private Animation animation;
	private final SaveMCMETA savedMCMETA;
	
	public FileMCMETA(String path, SaveMCMETA savedMCMETA) {
		super(path, savedMCMETA, new ImageView(Main.getResource("icon/file_mcmeta")));
		this.animation = savedMCMETA.getAnimation();
		this.savedMCMETA = savedMCMETA;
	}
	
	public FileMCMETA(String path, boolean activated) {
		super(path, activated, new ImageView(Main.getResource("icon/file_mcmeta")), "properties");
		this.savedMCMETA = new SaveMCMETA(this);
	}
	
	public FileMCMETA(String path, boolean activated, ArrayList<TextureObject> sources) {
		super(path, activated, new ImageView(Main.getResource("icon/file_mcmeta")), sources, "properties");
		this.savedMCMETA = new SaveMCMETA(this);
	}
	
	@Override
	public void open(ManagmentStage stage) {
		if(this.getTab() == null) {
			try {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Starting to open mcmeta file \"" + this.getPath() + "\"...", ConsoleLine.DEBUG));
					
				if(this.getName().equals("pack.mcmeta") && !this.getPath().substring(buildTexture.length(), this.getPath().lastIndexOf("\\")).contains("\\")) {
					CreateTexture.loadTexturePack(ManagmentStage.getOn().getTexturePack(), true);
				} else {
					animation = new Animation(this, this.getName());
					animation.build(Animation.readJsonFile(this));
					
					this.read();
					
					FXMLLoader loader = new FXMLLoader(Main.getLayout("Files/MCMETA/TabMCMETA"));
					loader.setController(this);
					this.setTab(loader.load());
					this.getTab().setText(this.getName());
					this.getTab().setGraphic(new ImageView(Main.getResource("icon/file_mcmeta")));
					stage.getFilesTabPane().getTabs().add(this.getTab());
					stage.getFilesTabPane().getSelectionModel().select(this.getTab());
				}
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Open MCMETA file \"" + this.getPath() + "\" successfully", ConsoleLine.DEBUG));
			} catch (FileNotFoundException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open MCMETA file \"" + this + "\" - FileNotFoundException in FileMCMETA.open", ConsoleLine.CRASH, exc));
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open MCMETA file \"" + this + "\" - IOException in FileMCMETA.open", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open MCMETA file \"" + this + "\" - Exception in FileMCMETA.open", ConsoleLine.CRASH, exc));
			}
		} else {
			stage.getFilesTabPane().getSelectionModel().select(this.getTab());
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pathLabel.setText(this.getPath().substring(buildTexture.length()));
		
		interpolaseCombo.setItems(listInterpolase);
		interpolaseCombo.setValue(this.animation.isInterpolate());
		frameTimeField.setText("" + this.animation.getFrameTime());
		
		textArea.setText(this.getText());
		
		this.propertiesEditor = false;
	}
	
	private String writeProperties() {
		return this.animation.toString();
	}
	
	@Override
	public boolean save() {
		try {
			if(this.propertiesEditor) {
				this.animation.setInterpolate(interpolaseCombo.getValue());
				try {
					this.animation.setFrameTime(Integer.parseInt(frameTimeField.getText()));
				} catch(NumberFormatException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to save frame time \"" + frameTimeField.getText() + "\" - the value will be 0", ConsoleLine.FATAL, exc));
					this.animation.setFrameTime(0);
				}
				this.textArea.setText(writeProperties());
			}
			FileWriter writer = new FileWriter(this);
			writer.write(this.textArea.getText());
			writer.close();
			this.setSave(true);
			return true;
		} catch(FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to save JSON file \"" + this + "\" - FileNotFoundException in FileJSON.save", ConsoleLine.FATAL, exc));
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to save JSON file \"" + this + "\" - IOException in FileJSON.save", ConsoleLine.FATAL, exc));
		}
		return false;
	}

	public void checkBoolean() {
		this.setSave(false);
	}
	public void checkInt() {
		try {
			Float.parseFloat(this.frameTimeField.getText());
			this.frameTimeField.getStyleClass().remove(ERROR);
		} catch(NumberFormatException exc) {
			this.frameTimeField.getStyleClass().add(ERROR);
		}
		this.setSave(false);
	}
	public void changeEditor() {
		this.propertiesEditor = !this.propertiesEditor;
	}
	
	public Animation getAnimation() {
		return animation;
	}
	public void setAnimation(Animation animation) {
		this.animation = animation;
		savedMCMETA.setAnimation(this.animation);
	}
}
