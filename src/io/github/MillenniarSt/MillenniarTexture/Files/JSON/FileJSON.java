package io.github.MillenniarSt.MillenniarTexture.Files.JSON;

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
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.Blockstate;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.Component;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.Model;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.TextureObject;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

public class FileJSON extends FileTXT implements Initializable, ProgrammePath {
	@Serial
	private static final long serialVersionUID = 1L;
	
	static HashMap<ComponentJSON<?>, FileJSON> listComp = new HashMap<>();
	
	@FXML
	private Label pathLabel;
	@FXML
	public TreeView<String> componentsTreeView;
	@FXML
	private TextArea textArea;
	@FXML
	private ScrollPane propertiesScroll;
	@FXML
	private VBox propertiesArea;
	@FXML
	private ComboBox<String> typeParentCombo;
	@FXML
	private ToggleButton treeEditor;
	
	private byte idparentMd;
	
	private ComponentJSON<?> rootTree;
	private Component parentMd;
	private final SaveJSON savedJSON;
	
	private final ObservableList<String> listParent = FXCollections.observableArrayList("null", "Model", "Blockstate");
	
	public FileJSON(String path, SaveJSON savedJSON) {
		super(path, savedJSON, new ImageView(Main.getResource("icon/file_json")));
		this.rootTree = new ComponentJSON<>(savedJSON.getRootTree());
		this.idparentMd = savedJSON.getIdParentMd();
		this.savedJSON = savedJSON;
	}
	
	public FileJSON(String path, boolean activated) {
		super(path, activated, new ImageView(Main.getResource("icon/file_json")), "json");
		this.savedJSON = new SaveJSON(this);
	}
	
	public FileJSON(String path, boolean activated, ArrayList<TextureObject> sources) {
		super(path, activated, new ImageView(Main.getResource("icon/file_json")), sources, "json");
		this.savedJSON = new SaveJSON(this);
	}
	
	@Override
	public void open(ManagmentStage stage) {
		if(this.getTab() == null) {
			try {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Starting to open json file \"" + this.getPath() + "\"...", ConsoleLine.DEBUG));
					
				elements = Model.readJsonFile(this);
				index = 0;
					
				if(((char) elements.get(index)) == '{') {
					this.rootTree = new ComponentJSON<>(this.getName(), false);
					index++;
					buildGroup(this.rootTree);
					this.rootTree.setExpanded(true);
					this.setRootTree(this.rootTree);
				}
				
				FXMLLoader loader = new FXMLLoader(Main.getLayout("Files/JSON/TabJSON"));
				loader.setController(this);
				this.setTab(loader.load());
				this.getTab().setText(this.getName());
				this.getTab().setGraphic(new ImageView(Main.getResource("icon/file_json")));
				stage.getFilesTabPane().getTabs().add(this.getTab());
				stage.getFilesTabPane().getSelectionModel().select(this.getTab());
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Open json file \"" + this.getPath() + "\" successfully", ConsoleLine.DEBUG));
			} catch (FileNotFoundException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open JSON file \"" + this + "\" - FileNotFoundException in FileJSON.open", ConsoleLine.CRASH, exc));
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open JSON file \"" + this + "\" - IOException in FileJSON.open", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open JSON file \"" + this + "\" - Exception in FileJSON.open", ConsoleLine.CRASH, exc));
			}
		} else {
			stage.getFilesTabPane().getSelectionModel().select(this.getTab());
		}
	}
	
	public void refreshProperties() {
		try {
			if(componentsTreeView.getSelectionModel().getSelectedItem() != null) {
				ComponentJSON<?> selected = (ComponentJSON<?>) componentsTreeView.getSelectionModel().getSelectedItem();
				propertiesArea.getChildren().clear();
				if(selected.getValues() != null) {
					selected.display(this, null);
				}
				showProperties(selected, null);
			}
		} catch (Exception exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail read and compile JSON file's tree view \"" + this + "\" - Exception in FileJSON.read", ConsoleLine.FATAL, exc));
		}
	}
	
	public void showProperties(ComponentJSON<?> root, ComponentJSON<?> parent) {
		if(parent != null && parent.getChildrenComp() != null)
			parent.getChildrenComp().getChildren().clear();
		
		for(TreeItem<String> item : root.getChildren()) {
			ComponentJSON<?> component = (ComponentJSON<?>) item;
			component.display(this, parent);
			
			if(!component.getChildren().isEmpty()) {
				showProperties(component, component);
			}
		}
	}
	
	private static ArrayList<Object> elements;
	private static int index;
	
	private void buildGroup(ComponentJSON<?> parent) {
		do {
			if(elements.get(index) instanceof String && ((char) elements.get(index + 1)) == ':') {
				ComponentJSON<?> component = null;
				if(elements.get(index + 2) instanceof String) {
					component = new ComponentJSON<String>((String) elements.get(index), (String) elements.get(index), (String) elements.get(index + 2));
					index = index + 4;
				} else if(elements.get(index + 2) instanceof Float) {
					component = new ComponentJSON<Float>((String) elements.get(index), (String) elements.get(index), (Float) elements.get(index + 2));
					index = index + 4;
				} else if(elements.get(index + 2) instanceof Boolean) {
					component = new ComponentJSON<Boolean>((String) elements.get(index), (String) elements.get(index), (Boolean) elements.get(index + 2));
					index = index + 4;
				} else if(((char) elements.get(index + 2)) == '{') {
					component = new ComponentJSON<>((String) elements.get(index), (String) elements.get(index), false);
					index = index + 3;
					buildGroup(component);
					if(((char) elements.get(index)) != '}') {
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail build JSON file's tree group \"" + this + "\" - Exception in FileJSON.build", ConsoleLine.ERROR));
					}
					index = index + 1;
				} else if(((char) elements.get(index + 2)) == '[') {
					component = new ComponentJSON<>((String) elements.get(index), (String) elements.get(index), true);
					index = index + 3;
					buildArray(component);
					if(((char) elements.get(index)) != ']') {
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail build JSON file's tree group \"" + this + "\" - Exception in FileJSON.build", ConsoleLine.ERROR));
					}
					index = index + 1;
				}
				if(component != null)
					parent.getChildren().add(component);
			}
		} while(((char) elements.get(index - 1)) == ',');
	}
	
	private void buildArray(ComponentJSON<?> parent) {
		int count = 1;
		do {
			ComponentJSON<?> component = null;
			if(elements.get(index) instanceof String) {
				component = new ComponentJSON<String>(count + ":", null, (String) elements.get(index));
				index = index + 2;
			} else if(elements.get(index) instanceof Float) {
				component = new ComponentJSON<Float>(count + ":", null, (Float) elements.get(index));
				index = index + 2;
			} else if(elements.get(index) instanceof Boolean) {
				component = new ComponentJSON<Boolean>(count + ":", null, (Boolean) elements.get(index));
				index = index + 2;
			} else if(((char) elements.get(index)) == '{') {
				component = new ComponentJSON<>("Element " + count + ":", false);
				index = index + 1;
				buildGroup(component);
				if(((char) elements.get(index)) != '}') {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail build JSON file's tree array \"" + this + "\" - Exception in FileJSON.build", ConsoleLine.ERROR));
				}
				index = index + 1;
			} else if(((char) elements.get(index)) == '[') {
				component = new ComponentJSON<>("Element " + count, true);
				index = index + 1;
				buildArray(component);
				if(((char) elements.get(index)) != ']') {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail build JSON file's tree group \"" + this + "\" - Exception in FileJSON.build", ConsoleLine.ERROR));	
				}
				index = index + 1;
			}
			if(component != null) {
				parent.getChildren().add(component);
				count++;
			}
		} while(((char) elements.get(index - 1)) == ',');
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pathLabel.setText(this.getPath().substring(buildTexture.length()));
		this.read();
		textArea.setText(this.getText());
		typeParentCombo.setItems(listParent);
		if(idparentMd == 1)
			typeParentCombo.getSelectionModel().select(1);
		else if(idparentMd == 2)
			typeParentCombo.getSelectionModel().select(2);
		else
			typeParentCombo.getSelectionModel().select(0);
		propertiesScroll.setFitToHeight(true);
		propertiesScroll.setFitToWidth(true);
		
		this.componentsTreeView.setRoot(this.rootTree);
	}
	
	public void build() {
		try {
			elements = Model.readJsonFile(this);
			if(typeParentCombo.getSelectionModel().getSelectedItem() == null) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Parent type null of \"" + this + "\" it will be null", ConsoleLine.WARN));
			} else if(typeParentCombo.getSelectionModel().getSelectedItem().equals("Model")) {
				this.setParentMd(new Model(this, this.getName()));
				if(this.parentMd.build(elements)) {
					MSDialog.showInformation("Build successfully", "Complete Model build without error");
				} else {
					MSDialog.showError("Build Fail", "Fail to build Model, check in console");
					ConsoleStage.defaultCns.getStage().show();
				}
			} else if(typeParentCombo.getSelectionModel().getSelectedItem().equals("Blockstate")) {
				this.setParentMd(new Blockstate(this, this.getName()));
				if(this.parentMd.build(elements)) {
					MSDialog.showInformation("Build successfully", "Complete Blockstate build without error");
				} else {
					MSDialog.showError("Build Fail", "Fail to build Blockstate, check in console");
					ConsoleStage.defaultCns.getStage().show();
				}
			}
			index = 0;
			if(((char) elements.get(index)) == '{') {
				this.rootTree = new ComponentJSON<>(this.getName(), false);
				index++;
				buildGroup(this.rootTree);
				this.setRootTree(this.rootTree);
			}
			this.componentsTreeView.setRoot(this.rootTree);
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to build JSON file \"" + this + "\" - IOException in FileJSON.build", ConsoleLine.FATAL, exc));
		}  catch (Exception exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to build JSON file \"" + this + "\" - Exception in FileJSON.build", ConsoleLine.FATAL, exc));
		}
	}
	
	public String writeTree() {
		String output = "{";
		try {
			output = output + readTree(this.rootTree, 1);
			this.rootTree.getSaved().setChildren(this.rootTree.getChildren());
		} catch(Exception exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to read and save JSON file's tree view \"" + this + "\" - Exception in FileJSON.save", ConsoleLine.FATAL, exc));
			return this.textArea.getText();
		}
		return output + "\n}";
	}
	
	public String readTree(ComponentJSON<?> parent, int level) {
		StringBuilder input = new StringBuilder();
		String levelStr = "\n";
		for(int i = 1; i <= level; i++) {
			levelStr = levelStr + "	";
		}
		boolean start = true;
		for(TreeItem<String> item : parent.getChildren()) {
			ComponentJSON<?> component = (ComponentJSON<?>) item;
			if(!start) {
				input.append(",");
			} else {
				start = false;
			}
			if(component.getValues() == null) {
				if(component.getKey() != null) {
					if(component.isArray()) {
						input.append(levelStr).append("\"").append(component.getKeyField().getText()).append("\" : [");
						String content = readTree(component, level + 1);
						if(content.charAt(0) == '	') {
							input.append(content).append(levelStr).append("]");
						} else {
							input.append(content).append("]");
						}
					} else {
						input.append(levelStr).append("\"").append(component.getKeyField().getText()).append("\" : {");
						String content = readTree(component, level + 1);
						input.append(content).append(levelStr).append("}");
					}
				} else {
					if(component.isArray()) {
						input.append(levelStr).append("[");
						String content = readTree(component, level + 1);
						if(content.charAt(0) == '	') {
							input.append(content).append(levelStr).append("]");
						} else {
							input.append(content).append("]");
						}
					} else {
						input.append(levelStr).append("{");
						String content = readTree(component, level + 1);
						input.append(content).append(levelStr).append("}");
					}
				}
				component.getSaved().setChildren(component.getChildren());
			} else if(component.getValues() instanceof String) {
				if(component.getKey() != null) {
					input.append(levelStr).append("\"").append(component.getKeyField().getText()).append("\" : ").append("\"").append(component.getValueField().getText()).append("\"");
				} else {
					input.append(" \"").append(component.getValueField().getText()).append("\"");
				}
				
				component.setValues(component.getValueField().getText());
			} else if(component.getValues() instanceof Float) {
				if(component.getKey() != null) {
					input.append(levelStr).append("\"").append(component.getKeyField().getText()).append("\" : ").append(component.getValueField().getText());
				} else {
					input.append(" ").append(component.getValueField().getText());
				}
				try {
					component.setValues(Float.parseFloat(component.getValueField().getText()));
				} catch(NumberFormatException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to load number \"" + component.getValueField().getText() + "\" the value will be 0 - NumberFormatException in FileJSON.save.number", ConsoleLine.WARN, exc));
					component.setValues(0f);
				}
			} else if(component.getValues() instanceof Boolean) {
				if(component.getKey() != null) {
					input.append(levelStr).append("\"").append(component.getKeyField().getText()).append("\" : ").append(component.getValueCombo().getSelectionModel().getSelectedItem());
				} else {
					input.append(" ").append(component.getValueCombo().getSelectionModel().getSelectedItem());
				}
				component.setValues(component.getValueCombo().getSelectionModel().getSelectedItem());
			}
			if(component.getKeyField() != null) {
				component.setKey(component.getKeyField().getText());
			}
		}
		return input.toString();
	}
	
	@Override
	public boolean save() {
		try {
			if(this.parentMd != null)
				this.idparentMd = this.parentMd.getId();
			if(this.treeEditor.isSelected()) {
				this.textArea.setText(writeTree());
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
		if(this.parentMd != null)
			savedJSON.setIdParentMd(this.parentMd.getId());
		if(this.rootTree != null)
			savedJSON.setRootTree(this.rootTree.getSaved());
		ManagmentStage.getOn().getTexturePack().getObjects().putFile(this.getPath(), this.savedJSON);
	}
	
	public void deleteComp() {
		TreeItem<String> choose = componentsTreeView.getSelectionModel().getSelectedItem();
		if(choose != null && choose.getParent() != null) {
			choose.getParent().getChildren().remove(choose);
			refreshProperties();
		}
	}
	public void addTexture() {
		TreeItem<String> textures = null;
		for(TreeItem<String> item : componentsTreeView.getRoot().getChildren()) {
			if(((ComponentJSON<?>) item).getKeyField().getText().equals("textures")) {
				textures = item;
			}
		}
		if(textures == null) {
			ComponentJSON<?> newTextures = new ComponentJSON<>("textures", "textures", false);
			componentsTreeView.getRoot().getChildren().add(textures);
			textures = newTextures;
		}
		textures.getChildren().add(new ComponentJSON<String>(textures.getChildren().size()+"", textures.getChildren().size()+"", ""));
		refreshProperties();
	}
	@SuppressWarnings("unchecked")
	public void addElement() {
		TreeItem<String> elements = null;
		for(TreeItem<String> item : componentsTreeView.getRoot().getChildren()) {
			if(((ComponentJSON<?>) item).getKeyField().getText().equals("elements")) {
				elements = item;
			}
		}
		if(elements == null) {
			ComponentJSON<?> newElements = new ComponentJSON<>("elements", "elements", true);
			componentsTreeView.getRoot().getChildren().add(newElements);
			elements = newElements;
		}
		ComponentJSON<?> element = new ComponentJSON<>("Element " + elements.getChildren().size(), false);
		ComponentJSON<?> from = new ComponentJSON<>("from", "from", true);
		ComponentJSON<?> to = new ComponentJSON<>("to", "to", true);
		for(byte i = 1; i <= 3; i++) {
			from.getChildren().add(new ComponentJSON<Float>(i + ":", null, 0f));
			to.getChildren().add(new ComponentJSON<Float>(i + ":", null, 16f));
		}
		ComponentJSON<?> faces = new ComponentJSON<>("rotation", "rotation", false);
		ComponentJSON<?> down = new ComponentJSON<>("down", "down", false);
		down.getChildren().add(new ComponentJSON<String>("texture", "texture", "#"));
		ComponentJSON<?> up = new ComponentJSON<>("up", "up", false);
		up.getChildren().add(new ComponentJSON<String>("texture", "texture", "#"));
		ComponentJSON<?> north = new ComponentJSON<>("north", "north", false);
		north.getChildren().add(new ComponentJSON<String>("texture", "texture", "#"));
		ComponentJSON<?> south = new ComponentJSON<>("south", "south", false);
		south.getChildren().add(new ComponentJSON<String>("texture", "texture", "#"));
		ComponentJSON<?> west = new ComponentJSON<>("west", "west", false);
		west.getChildren().add(new ComponentJSON<String>("texture", "texture", "#"));
		ComponentJSON<?> east = new ComponentJSON<>("east", "east", false);
		east.getChildren().add(new ComponentJSON<String>("texture", "texture", "#"));
		faces.getChildren().addAll(down, up, north, south, west, east);
		element.getChildren().addAll(from, to, faces);
		elements.getChildren().add(element);
		refreshProperties();
	}
	@SuppressWarnings("unchecked")
	public void addDisplay() {
		boolean check = true;
		for(TreeItem<String> item : componentsTreeView.getRoot().getChildren()) {
			if(((ComponentJSON<?>) item).getKeyField().getText().equals("display")) {
				ComponentJSON<?> gui = new ComponentJSON<>("gui", "gui", false);
				ComponentJSON<?> ground = new ComponentJSON<>("ground", "ground", false);
				ComponentJSON<?> fixed = new ComponentJSON<>("fixed", "fixed", false);
				ComponentJSON<?> thirdpersonRighthand = new ComponentJSON<>("thirdperson_righthand", "thirdperson_righthand", false);
				ComponentJSON<?> firstpersonRighthand = new ComponentJSON<>("firstperson_righthand", "firstperson_righthand", false);
				ComponentJSON<?> firstpersonLefthand = new ComponentJSON<>("firstperson_lefthand", "firstperson_lefthand", false);
				ComponentJSON<?>[] newItems = {gui, ground, fixed, thirdpersonRighthand, firstpersonRighthand, firstpersonLefthand};
				for(TreeItem<String> comp : item.getChildren()) {
					String key = ((ComponentJSON<?>) comp).getKeyField().getText();
                    switch (key) {
                        case "gui" -> newItems[0] = null;
                        case "ground" -> newItems[1] = null;
                        case "fixed" -> newItems[2] = null;
                        case "thirdperson_righthand" -> newItems[3] = null;
                        case "firstperson_righthand" -> newItems[4] = null;
                        case "firstperson_lefthand" -> newItems[5] = null;
                    }
				}
				for(ComponentJSON<?> component : newItems) {
					if(component != null) {
						ComponentJSON<?> rotation = new ComponentJSON<>("rotation", "rotation", true);
						ComponentJSON<?> translation = new ComponentJSON<>("translation", "translation", true);
						ComponentJSON<?> scale = new ComponentJSON<>("scale", "scale", true);
						for(byte i = 1; i <= 3; i++) {
							rotation.getChildren().add(new ComponentJSON<Float>(i + ":", null, 0f));
							translation.getChildren().add(new ComponentJSON<Float>(i + ":", null, 0f));
							scale.getChildren().add(new ComponentJSON<Float>(i + ":", null, 1f));
						}
						component.getChildren().addAll(rotation, translation, scale);
						item.getChildren().add(component);
					}
				}
				check = false;
			}
		}
		if(check) {
			ComponentJSON<?> display = new ComponentJSON<>("display", "display", false);
			ComponentJSON<?> gui = new ComponentJSON<>("gui", "gui", false);
			ComponentJSON<?> ground = new ComponentJSON<>("ground", "ground", false);
			ComponentJSON<?> fixed = new ComponentJSON<>("fixed", "fixed", false);
			ComponentJSON<?> thirdpersonRighthand = new ComponentJSON<>("thirdperson_righthand", "thirdperson_righthand", false);
			ComponentJSON<?> firstpersonRighthand = new ComponentJSON<>("firstperson_righthand", "firstperson_righthand", false);
			ComponentJSON<?> firstpersonLefthand = new ComponentJSON<>("firstperson_lefthand", "firstperson_lefthand", false);
			display.getChildren().addAll(gui, ground, fixed, thirdpersonRighthand, firstpersonRighthand, firstpersonLefthand);
			for(TreeItem<String> item : display.getChildren()) {
				ComponentJSON<?> rotation = new ComponentJSON<>("rotation", "rotation", true);
				ComponentJSON<?> translation = new ComponentJSON<>("translation", "translation", true);
				ComponentJSON<?> scale = new ComponentJSON<>("scale", "scale", true);
				for(byte i = 1; i <= 3; i++) {
					rotation.getChildren().add(new ComponentJSON<Float>(i + ":", null, 0f));
					translation.getChildren().add(new ComponentJSON<Float>(i + ":", null, 0f));
					scale.getChildren().add(new ComponentJSON<Float>(i + ":", null, 1f));
				}
				item.getChildren().addAll(rotation, translation, scale);
			}
			componentsTreeView.getRoot().getChildren().add(display);
		}
		refreshProperties();
	}
	public void addVariant() {
		TreeItem<String> variants = null;
		for(TreeItem<String> item : componentsTreeView.getRoot().getChildren()) {
			if(((ComponentJSON<?>) item).getKeyField().getText().equals("variants")) {
				variants = item;
			}
		}
		if(variants == null) {
			ComponentJSON<?> newVariants = new ComponentJSON<>("variants", "variants", false);
			componentsTreeView.getRoot().getChildren().add(variants);
			variants = newVariants;
		}
		ComponentJSON<String> variant = new ComponentJSON<String>("", "", false);
		variant.getChildren().add(new ComponentJSON<String>("model", "model", ""));
		variants.getChildren().add(variant);
		refreshProperties();
	}
	
	public void addGroup() {
		ComponentJSON<?> selected = (ComponentJSON<?>) componentsTreeView.getSelectionModel().getSelectedItem();
		if(selected != null) {
			if(selected.getValues() == null && selected.isArray()) {
				selected.getChildren().add(new ComponentJSON<>("Element " + selected.getChildren().size(), false));
				refreshProperties();
			} else if(selected.getValues() == null) {
				selected.getChildren().add(new ComponentJSON<>("Element " + selected.getChildren().size(), "Element " + selected.getChildren().size(), false));
				refreshProperties();
			}
		}
	}
	public void addArray() {
		ComponentJSON<?> selected = (ComponentJSON<?>) componentsTreeView.getSelectionModel().getSelectedItem();
		if(selected != null) {
			if(selected.getValues() == null && selected.isArray()) {
				selected.getChildren().add(new ComponentJSON<>("Element " + selected.getChildren().size(), true));
				refreshProperties();
			} else if(selected.getValues() == null) {
				selected.getChildren().add(new ComponentJSON<>("Element " + selected.getChildren().size(), "Element " + selected.getChildren().size(), true));
				refreshProperties();
			}
		}
	}
	public void addString() {
		ComponentJSON<?> selected = (ComponentJSON<?>) componentsTreeView.getSelectionModel().getSelectedItem();
		if(selected != null) {
			if(selected.getValues() == null && selected.isArray()) {
				selected.getChildren().add(new ComponentJSON<String>("Element " + selected.getChildren().size(), null, ""));
				refreshProperties();
			} else if(selected.getValues() == null) {
				selected.getChildren().add(new ComponentJSON<String>("Element " + selected.getChildren().size(), "Element " + selected.getChildren().size(), ""));
				refreshProperties();
			}
		}
	}
	public void addNumber() {
		ComponentJSON<?> selected = (ComponentJSON<?>) componentsTreeView.getSelectionModel().getSelectedItem();
		if(selected != null) {
			if(selected.getValues() == null && selected.isArray()) {
				selected.getChildren().add(new ComponentJSON<Float>("Element " + selected.getChildren().size(), null, 0f));
				refreshProperties();
			} else if(selected.getValues() == null) {
				selected.getChildren().add(new ComponentJSON<Float>("Element " + selected.getChildren().size(), "Element " + selected.getChildren().size(), 0f));
				refreshProperties();
			}
		}
	}
	public void addBoolean() {
		ComponentJSON<?> selected = (ComponentJSON<?>) componentsTreeView.getSelectionModel().getSelectedItem();
		if(selected != null) {
			if(selected.getValues() == null && selected.isArray()) {
				selected.getChildren().add(new ComponentJSON<Boolean>("Element " + selected.getChildren().size(), null, false));
				refreshProperties();
			} else if(selected.getValues() == null) {
				selected.getChildren().add(new ComponentJSON<Boolean>("Element " + selected.getChildren().size(), "Element " + selected.getChildren().size(), false));
				refreshProperties();
			}
		}
	}
	
	@Override
	public void change() {
		this.setSave(false);
		this.treeEditor.setSelected(false);
	}
	protected VBox getPropertiesArea() {
		return propertiesArea;
	}
	protected Component getParentMd() {
		return parentMd;
	}
	private void setParentMd(Component parentMd) {
		this.parentMd = parentMd;
	}
	protected ComboBox<String> getTypeParentCombo() {
		return typeParentCombo;
	}
	protected ToggleButton getTreeEditor() {
		return this.treeEditor;
	}
	protected ComponentJSON<?> getRootTree() {
		return rootTree;
	}
	private void setRootTree(ComponentJSON<?> rootTree) {
		this.rootTree = rootTree;
	}
}
