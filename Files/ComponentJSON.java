package Files;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import Console.ConsoleLine;
import Console.ConsoleStage;
import Main.Main;
import Main.ProgrammePath;
import Menu.ManagmentStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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

public class ComponentJSON<Value> extends TreeItem<String> implements Initializable, ProgrammePath {

	private String view;
	
	private String key;
	private Value values;
	private boolean error;
	private boolean array;
	
	private AnchorPane root;
	
	@FXML
	private Label titleLabel;
	@FXML
	private ImageView keyCheck;
	@FXML
	private ImageView valueCheck;
	@FXML
	private TextField keyField;
	@FXML
	private VBox childrenComp;
	@FXML
	private TextField valueField;
	@FXML
	private ComboBox<Boolean> valueCombo;
	private ObservableList<Boolean> listBoolean = FXCollections.observableArrayList(true, false);
	
	private SaveComponentJSON<Value> saved;
	
	private HashMap<Node, String> errors = new HashMap<>();
	private HashMap<Node, String> warns = new HashMap<>();
	
	private static final String ERROR = "text-field-error";
	private static final String WARN = "text-field-warn";

	public ComponentJSON(SaveComponentJSON<Value> saved) {
		super(saved.getView());
		this.view = saved.getView();
		this.values = saved.getValues();
		this.error = saved.isError();
		this.array = saved.isArray();
		if(values == null) {
			if(key == null)
				buildGr();
			else
				buildGrKey();
		} else {
			buildItem();
		}
		this.saved = saved;
	}
	
	public ComponentJSON(String view, boolean array) {
		super(view);
		this.view = view;
		this.array = array;
		buildGr();
		this.saved = new SaveComponentJSON<>(this);
	}
	private void buildGr() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Files/ComponentGroupJSON.fxml"));
			loader.setController(this);
			this.root = loader.load();
			this.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/directory.png"))));
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to load component's fxml from \"/Files/ComponentGroupJSON.fxml\" - IOException in FileJSON.components.load", ConsoleLine.ERROR, exc));
			this.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/not_found.png"))));
		}
	}
	
	public ComponentJSON(String view, String key, boolean array) {
		super(view);
		this.view = view;
		this.key = key;
		this.array = array;
		buildGrKey();
		this.saved = new SaveComponentJSON<>(this);
	}
	private void buildGrKey() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Files/ComponentKeyGroupJSON.fxml"));
			loader.setController(this);
			this.root = loader.load();
			this.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/directory.png"))));
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to load component's fxml from \"/Files/ComponentKeyGroupJSON.fxml\" - IOException in FileJSON.components.load", ConsoleLine.ERROR, exc));
			this.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/not_found.png"))));
		}
	}

	public ComponentJSON(String view, String key, Value values) {
		super(view);
		this.view = view;
		this.key = key;
		this.values = values;
		buildItem();
		this.saved = new SaveComponentJSON<>(this);
	}
	private void buildItem() {
		try {
			FXMLLoader loader = null;
			if(this.values instanceof String) {
				if(this.key != null)
					loader = new FXMLLoader(getClass().getResource("/Files/ComponentKeyStringJSON.fxml"));
				else
					loader = new FXMLLoader(getClass().getResource("/Files/ComponentStringJSON.fxml"));
				this.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/text.png"))));
			} else if(this.values instanceof Float) {
				if(this.key != null)
					loader = new FXMLLoader(getClass().getResource("/Files/ComponentKeyFloatJSON.fxml"));
				else
					loader = new FXMLLoader(getClass().getResource("/Files/ComponentFloatJSON.fxml"));
				this.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/number.png"))));
			} else if(this.values instanceof Boolean) {
				if(this.key != null)
					loader = new FXMLLoader(getClass().getResource("/Files/ComponentKeyBooleanJSON.fxml"));
				else
					loader = new FXMLLoader(getClass().getResource("/Files/ComponentBooleanJSON.fxml"));
				this.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/boolean.png"))));
			}
			if(loader != null) {
				loader.setController(this);
				this.root = loader.load();
			}
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to load component's fxml - IOException in FileJSON.components.load", ConsoleLine.ERROR, exc));
			this.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/not_found.png"))));
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		titleLabel.setText(this.view);
		if(this.key != null) {
			keyField.setText(this.key);
		}
		
		if(this.values instanceof String) {
			valueField.setText((String) values);
		} else if(this.values instanceof Float) {
			valueField.setText(((Float) values).toString());
		} else if(this.values instanceof Boolean) {
			valueCombo.setItems(listBoolean);
			valueCombo.setValue((Boolean) values);
			this.setError(false);
			this.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/icon/ok_icon.png")));
		}
	}

	public void display(FileJSON source, ComponentJSON<?> parent) {
		if(this.root != null) {
			if(parent != null) {
				parent.childrenComp.getChildren().add(this.root);
				parent.childrenComp.getChildren().add(new Separator());
			} else {
				source.getPropertiesArea().getChildren().add(this.root);
				source.getPropertiesArea().getChildren().add(new Separator());
			}
			FileJSON.listComp.put(this, source);
			if(this.key != null) {
				checkKey();
			}
			if(this.values instanceof String) {
				checkString();
			} else if(this.values instanceof Float) {
				checkFloat();
			}
		}
	}
	
	public void checkKey() {
		FileJSON parent = FileJSON.listComp.get(this);
		if(this.keyField.getText().equals("")) {
			this.keyCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/null_text.png")));
			this.setError(true);
			addError(keyField, "Key null");
			ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
			while(parentTemp != null) {
				parentTemp.setError(true);
				parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/null_text.png")));
				parentTemp = (ComponentJSON<?>) parentTemp.getParent();
			}
		} else if(this.isSameKey()) {
			this.keyCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/same_text.png")));
			this.setError(true);
			addError(keyField, "Key same");
			ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
			while(parentTemp != null) {
				parentTemp.setError(true);
				parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/same_text.png")));
				parentTemp = (ComponentJSON<?>) parentTemp.getParent();
			}
		} else {
			this.keyCheck.setImage(new Image(getClass().getResourceAsStream("/resource/icon/ok_icon.png")));
			this.setError(false);
			removeError(keyField);
			
			this.titleLabel.setText(this.keyField.getText());
			this.setView(this.keyField.getText());
			
			ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
			while(parentTemp != null) {
				boolean check = true;
				for(TreeItem<String> item : parentTemp.getChildren()) {
					ComponentJSON<?> component = (ComponentJSON<?>) item;
					if(component.error) {
						check = false;
					}
				}
				if(check) {
					parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/icon/ok_icon.png")));
					parentTemp.setError(false);
				}
				parentTemp = (ComponentJSON<?>) parentTemp.getParent();
			}
		}
		parent.setSave(false);
		parent.getTreeEditor().setSelected(true);
	}
	
	private boolean isSameKey() {
		if(this.getParent() != null) {
			for(TreeItem<String> item : this.getParent().getChildren()) {
				if(((ComponentJSON<?>) item).keyField != null && ((ComponentJSON<?>) item).keyField.isEditable() && ((ComponentJSON<?>) item).keyField.getText().equals(this.keyField.getText()) && this != item) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	public void checkString() {
		String value = this.valueField.getText();
		FileJSON parent = FileJSON.listComp.get(this);
		int tempDer = new String(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "\\assets\\").length();
		String derivation;
		if(value.contains(":")) {
			derivation = value.substring(0, value.indexOf(":"));
			value = value.substring(value.indexOf(":") + 1);
		} else {
			derivation = parent.getPath().substring(tempDer, tempDer + parent.getPath().substring(tempDer).indexOf("\\"));
		}

		String toSearch = null;
		boolean ok = true;
		if(value.equals("")) {
			this.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/null_text.png")));
			this.setError(true);
			addError(valueField, "Value null");
			ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
			while(parentTemp != null) {
				parentTemp.setError(true);
				parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/null_text.png")));
				parentTemp = (ComponentJSON<?>) parentTemp.getParent();
			}
			ok = false;
		} else if(parent.getTypeParentCombo().getSelectionModel().getSelectedItem().equals("Model")) {
			if(this.keyField != null) {
				String key = this.keyField.getText();
				if(key.equals("parent")) {
					toSearch = derivation + "\\models\\" + value + ".json";
				} else if(key.equals("axis")) {
					if(!value.equals("x") && !value.equals("y") && !value.equals("z")) {
						this.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/wrong_text.png")));
						this.setError(true);
						addError(valueField, "Value axis not valid");
						ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
						while(parentTemp != null) {
							parentTemp.setError(true);
							parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/wrong_text.png")));
							parentTemp = (ComponentJSON<?>) parentTemp.getParent();
						}
						ok = false;
					}
				} else if(key.equals("texture")) {
					if(value.charAt(0) == '#') {
						boolean check = true;
						for(TreeItem<String> item : parent.componentsTreeView.getRoot().getChildren()) {
							ComponentJSON<?> component = (ComponentJSON<?>) item;
							if(component.keyField.getText().equals("textures")) {
								for(TreeItem<String> texture : component.getChildren()) {
									if(((ComponentJSON<?>) texture).keyField.getText().equals(value.substring(1)))
										check = false;
								}
							}
						}
						if(check) {
							this.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/wrong_text.png")));
							this.setError(true);
							addError(valueField, "Value texture not valid");
							ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
							while(parentTemp != null) {
								parentTemp.setError(true);
								parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/wrong_text.png")));
								parentTemp = (ComponentJSON<?>) parentTemp.getParent();
							}
							ok = false;
						}
					} else {
						toSearch = derivation + "\\textures\\" + value + ".png";
					}
				}
			}
			if(((ComponentJSON<?>) this.getParent()).keyField != null) {
				if(((ComponentJSON<?>) this.getParent()).keyField.getText().equals("textures")) {
					toSearch = derivation + "\\textures\\" + value + ".png";
				}
			}
		} else if(parent.getTypeParentCombo().getSelectionModel().getSelectedItem().equals("Blockstate")) {
			if(this.keyField != null) {
				if(this.keyField.getText().equals("model")) {
					toSearch = derivation + "\\models\\" + value + ".json";
				}
			}
		}
		if(toSearch != null) {
			if(!new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "\\assets\\" + toSearch).exists()) {
				if(!new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString()+ "\\" + derivation).exists()) {
					this.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/derivation_not_found.png")));
					this.setError(true);
					addWarn(valueField, "Texture derivation not found");
					ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
					while(parentTemp != null) {
						boolean check = true;
						for(TreeItem<String> item : parentTemp.getChildren()) {
							ComponentJSON<?> component = (ComponentJSON<?>) item;
							if(component.error) {
								check = false;
							}
						}
						if(check) {
							parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/icon/derivation_not_found.png")));
							parentTemp.setError(false);
						}
						parentTemp = (ComponentJSON<?>) parentTemp.getParent();
					}
					ok = false;
				} else if(!new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + toSearch).exists()) {
					this.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/file_not_found.png")));
					this.setError(true);
					addError(valueField, "Texture not found");
					ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
					while(parentTemp != null) {
						parentTemp.setError(true);
						parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/file_not_found.png")));
						parentTemp = (ComponentJSON<?>) parentTemp.getParent();
					}
					ok = false;
				}
			}
		}
		if(ok) {
			this.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/icon/ok_icon.png")));
			this.setError(false);
			removeError(valueField);
			ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
			while(parentTemp != null) {
				boolean check = true;
				for(TreeItem<String> item : parentTemp.getChildren()) {
					ComponentJSON<?> component = (ComponentJSON<?>) item;
					if(component.error) {
						check = false;
					}
				}
				if(check) {
					parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/icon/ok_icon.png")));
					parentTemp.setError(false);
				}
				parentTemp = (ComponentJSON<?>) parentTemp.getParent();
			}
		}
		parent.setSave(false);
		parent.getTreeEditor().setSelected(true);
	}
	
	public void checkFloat() {
		FileJSON parent = FileJSON.listComp.get(this);
		try {
			float value = Float.parseFloat(this.valueField.getText());
			if(this.valueField.getText().charAt(this.valueField.getText().length() - 1) == '.') {
				throw new NumberFormatException();
			}
			if(parent.getTypeParentCombo().getSelectionModel().getSelectedItem().equals("Model")) {
				if(((ComponentJSON<?>) this.getParent()).keyField != null) {
					String key = ((ComponentJSON<?>) this.getParent()).keyField.getText();
					if(key.equals("from") || key.equals("to") || key.equals("origin")) {
						if(value < -16 || value > 32)
							throw new NumberFormatException();
					} else if(key.equals("rotation")) {
						if(value < -180 || value > 180)
							throw new NumberFormatException();
					} else if(key.equals("translation")) {
						if(value < -80 || value > 80)
							throw new NumberFormatException();
					} else if(key.equals("scale")) {
						if(value < 0 || value > 4)
							throw new NumberFormatException();
					} 
				}
			} else if(parent.getTypeParentCombo().getSelectionModel().getSelectedItem().equals("Blockstate")) {
				if(this.keyField != null) {
					if(this.keyField.getText().equals("x") || this.keyField.getText().equals("y") || this.keyField.getText().equals("z")) {
						if(value % 90 != 0)
							throw new NumberFormatException();
					}
				}
			}
			
			this.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/icon/ok_icon.png")));
			this.setError(false);
			removeError(valueField);
			ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
			while(parentTemp != null) {
				boolean check = true;
				for(TreeItem<String> item : parentTemp.getChildren()) {
					ComponentJSON<?> component = (ComponentJSON<?>) item;
					if(component.error) {
						check = false;
					}
				}
				if(check) {
					parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/icon/ok_icon.png")));
					parentTemp.setError(false);
				}
				parentTemp = (ComponentJSON<?>) parentTemp.getParent();
			}
		} catch(NumberFormatException exc) {
			this.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/wrong_text.png")));
			this.setError(true);
			addError(valueField, "Value [number] not valid");
			ComponentJSON<?> parentTemp = (ComponentJSON<?>) this.getParent();
			while(parentTemp != null) {
				parentTemp.setError(true);
				parentTemp.valueCheck.setImage(new Image(getClass().getResourceAsStream("/resource/exception/wrong_text.png")));
				parentTemp = (ComponentJSON<?>) parentTemp.getParent();
			}
		}
		parent.setSave(false);
		parent.getTreeEditor().setSelected(true);
	}
	
	public void checkBoolean() {
		FileJSON parent = FileJSON.listComp.get(this);
		parent.setSave(false);
		parent.getTreeEditor().setSelected(true);
	}
	
	private void addError(Node node, String view) {
		if(node instanceof TextField) {
			if(!node.getStyleClass().contains(ERROR)) {
				node.getStyleClass().add(ERROR);
				node.getStyleClass().remove(WARN);
				errors.put((TextField) node, view);
				warns.remove((TextField) node);
			}
		}
	}
	private void addWarn(Node node, String view) {
		if(node instanceof TextField) {
			if(!node.getStyleClass().contains(WARN) && !node.getStyleClass().contains(ERROR)) {
				node.getStyleClass().add(WARN);
				warns.put((TextField) node, view);
			}
		}
	}
	private void removeError(Node node) {
		if(node instanceof TextField) {
			node.getStyleClass().remove(ERROR);
			node.getStyleClass().remove(WARN);
			errors.remove((TextField) node);
			warns.remove((TextField) node);
		}
	}
	
	@Override
	public String toString() {
		return this.view;
	}
	
	VBox getChildrenComp() {
		return childrenComp;
	}
	boolean isError() {
		return error;
	}
	private void setError(boolean error) {
		this.error = error;
		this.saved.setError(this.error);
	}
	String getView() {
		return view;
	}
	private void setView(String view) {
		this.view = view;
		this.saved.setView(this.view);
	}
	String getKey() {
		return key;
	}
	void setKey(String key) {
		this.key = key;
		this.saved.setKey(this.key);
	}
	Value getValues() {
		return values;
	}
	void setValues(Value values) {
		this.values = values;
		this.saved.setValues(this.values);
	}
	@SuppressWarnings("unchecked")
	void setValues(String values) {
		this.values = (Value) values;
		this.saved.setValues(this.values);
	}
	@SuppressWarnings("unchecked")
	void setValues(Float values) {
		this.values = (Value) values;
		this.saved.setValues(this.values);
	}
	@SuppressWarnings("unchecked")
	void setValues(Boolean values) {
		this.values = (Value) values;
		this.saved.setValues(this.values);
	}
	AnchorPane getRoot() {
		return root;
	}
	TextField getKeyField() {
		return keyField;
	}
	boolean isArray() {
		return array;
	}	
	Label getTitleLabel() {
		return titleLabel;
	}
	TextField getValueField() {
		return valueField;
	}
	ComboBox<Boolean> getValueCombo() {
		return valueCombo;
	}
	public SaveComponentJSON<Value> getSaved() {
		return saved;
	}
}
