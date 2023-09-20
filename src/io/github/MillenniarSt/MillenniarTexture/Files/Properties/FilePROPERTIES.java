package io.github.MillenniarSt.MillenniarTexture.Files.Properties;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.Files.TXT.FileTXT;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.Properties;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.TextureObject;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.Menu.ManagmentStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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

public class FilePROPERTIES extends FileTXT implements Initializable {

	@Serial
	private static final long serialVersionUID = 1L;
	static HashMap<ComponentProperties<?>, FilePROPERTIES> listComp = new HashMap<>();
	
	@FXML
	private Label pathLabel;
	@FXML
	private TextArea textArea;
	@FXML
	private VBox propertiesBox;
	@FXML
	private ToggleButton PropEditor;
	
	private final SavePROPERTIES savedPROPERTIES;
	private Properties properties;
	private ArrayList<ComponentProperties<?>> propertiesArea;
	ComponentProperties<?> selected;
	
	public FilePROPERTIES(String path, SavePROPERTIES saved) {
		super(path, saved, new ImageView(Main.getResource("icon/file_properties")));
		this.savedPROPERTIES = saved;
		properties = new Properties(this, this.getName(), saved.getProperties());
	}
	
	public FilePROPERTIES(String path, boolean activated) {
		super(path, activated, new ImageView(Main.getResource("icon/file_properties")), "properties");
		properties = new Properties(this, this.getName().substring(this.getName().lastIndexOf(".") + 1));
		this.savedPROPERTIES = new SavePROPERTIES(this);
	}
	
	public FilePROPERTIES(String path, boolean activated, ArrayList<TextureObject> sources) {
		super(path, activated, new ImageView(Main.getResource("icon/file_properties")), sources, "properties");
		properties = new Properties(this, this.getName());
		this.savedPROPERTIES = new SavePROPERTIES(this);
	}
	
	@Override
	public void open(ManagmentStage stage) {
		if(this.getTab() == null) {
			try {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("JSON", "Starting to open mcmeta file \"" + this.getPath() + "\"...", ConsoleLine.DEBUG));
					
				this.read();
				properties = new Properties(this, this.getName());
				properties.build(Properties.readPropertiesFile(this));
				propertiesArea = new ArrayList<>();
				for(String key : properties.getProperties().keySet()) {
					ArrayList<String> value = properties.getProperties().get(key);
					if(value.size() == 1) {
						try {
							propertiesArea.add(new ComponentProperties<Color>(key, Color.web(value.get(0)), this));
						} catch(IllegalArgumentException eC) {
							propertiesArea.add(new ComponentProperties<ArrayList<String>>(key, value, this));
						}
					} else {
						propertiesArea.add(new ComponentProperties<ArrayList<String>>(key, value, this));
					}
				}
					
				FXMLLoader loader = new FXMLLoader(Main.getLayout("/Files/Properties/TabPROPERTIES"));
				loader.setController(this);
				this.setTab(loader.load());
				this.getTab().setText(this.getName());
				this.getTab().setGraphic(new ImageView(Main.getResource("icon/file_properties")));
				stage.getFilesTabPane().getTabs().add(this.getTab());
				stage.getFilesTabPane().getSelectionModel().select(this.getTab());
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Open PROPERTIES file \"" + this.getPath() + "\" successfully", ConsoleLine.DEBUG));
			} catch (FileNotFoundException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open PROPERTIES file \"" + this + "\" - FileNotFoundException in FilePROPERTIES.open", ConsoleLine.CRASH, exc));
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open PROPERTIES file \"" + this + "\" - IOException in FilePROPERTIES.open", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open PROPERTIES file \"" + this + "\" - Exception in FilePROPERTIES.open", ConsoleLine.CRASH, exc));
			}
		} else {
			stage.getFilesTabPane().getSelectionModel().select(this.getTab());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pathLabel.setText(this.getPath().substring(buildTexture.length()));
		textArea.setText(this.getText());
		for(ComponentProperties<?> comp : propertiesArea) {
			propertiesBox.getChildren().add(comp.getRoot());
		}
	}
	
	public void addProperty() {
		String choose = MSDialog.showConfirm("New Property", "Choose the type of new property", "Default", "Color", "Cancel");
		if(choose.equals("Default")) {
			ComponentProperties<ArrayList<String>> comp = new ComponentProperties<>("", new ArrayList<String>(), this);
			propertiesArea.add(comp);
			propertiesBox.getChildren().add(comp.getRoot());
		} else if(choose.equals("Color")) {
			ComponentProperties<Color> comp = new ComponentProperties<>("", Color.WHITE, this);
			propertiesArea.add(comp);
			propertiesBox.getChildren().add(comp.getRoot());
		}
	}
	public void removeProperty() {
		if(selected != null) {
			if(MSDialog.showConfirm("Delete Component", "Are you sure to delete this component?", "Yes", "No").equals("Yes")) {
				propertiesArea.remove(selected);
				propertiesBox.getChildren().remove(selected.getRoot());
				selected = null;
			}
		}
	}
	
	@Override
	public void change() {
		this.PropEditor.setSelected(false);
		this.setSave(false);
	}
	
	public void build() {
		try {
			this.properties.build(Properties.readPropertiesFile(this));
			propertiesArea = new ArrayList<>();
			propertiesBox.getChildren().clear();
			for(String key : properties.getProperties().keySet()) {
				ArrayList<String> value = properties.getProperties().get(key);
				if(value.size() == 1) {
					try {
						ComponentProperties<Color> comp = new ComponentProperties<>(key, Color.web(value.get(0)), this);
						propertiesArea.add(comp);
						propertiesBox.getChildren().add(comp.getRoot());
					} catch(IllegalArgumentException eC) {
						ComponentProperties<ArrayList<String>> comp = new ComponentProperties<>(key, value, this);
						propertiesArea.add(comp);
						propertiesBox.getChildren().add(comp.getRoot());
					}
				} else {
					ComponentProperties<ArrayList<String>> comp = new ComponentProperties<>(key, value, this);
					propertiesArea.add(comp);
					propertiesBox.getChildren().add(comp.getRoot());
				}
			}
		} catch (FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to build PROPERTIES file \"" + this + "\" - IOException in FilePROPERTIES.build", ConsoleLine.FATAL, exc));
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to build PROPERTIES file \"" + this + "\" - Exception in FilePROPERTIES.build", ConsoleLine.FATAL, exc));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean save() {
		try {
			if(PropEditor.isSelected()) {
				properties.getProperties().clear();
				for(ComponentProperties<?> comp : propertiesArea) {
					comp.save();
					if(comp.getValue() instanceof ArrayList) {
						properties.getProperties().put(comp.getKey(), (ArrayList<String>) comp.getValue());
					} else {
						ArrayList<String> array = new ArrayList<>();
						array.add(comp.getValueView());
						properties.getProperties().put(comp.getKey(), array);
					}
				}
				this.textArea.setText(properties.toString());
			}
			FileWriter writer = new FileWriter(this);
			writer.write(this.textArea.getText());
			writer.close();
			register();
			this.setSave(true);
			return true;
		} catch(FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to save JSON file \"" + this + "\" - FileNotFoundException in FileJSON.save", ConsoleLine.FATAL, exc));
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to save JSON file \"" + this + "\" - IOException in FileJSON.save", ConsoleLine.FATAL, exc));
		}
		return false;
	}
	@Override
	public void register() {
		savedPROPERTIES.setProperties(properties.getProperties());
		ManagmentStage.getOn().getTexturePack().getObjects().putFile(this.getPath(), this.savedPROPERTIES);
	}
	
	public ToggleButton getPropEditor() {
		return PropEditor;
	}
	public SavePROPERTIES getSavedPROPERTIES() {
		return savedPROPERTIES;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public ArrayList<ComponentProperties<?>> getPropertiesArea() {
		return propertiesArea;
	}
}
