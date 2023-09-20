package io.github.MillenniarSt.MillenniarTexture.Files.Properties;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

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

public class ComponentProperties<Value> implements Initializable {

	private static final  String SELECTED = "selected";
	private static final String ERROR = "text-field-error";
	
	@FXML
	private TextField keyField;
	@FXML
	private TextField valueField;
	@FXML
	private ColorPicker valuePicker;
	
	private AnchorPane root;
	
	private String key;
	private Value value;
	private String valueView;
	
	@SuppressWarnings("unchecked")
	public ComponentProperties(String key, Value value, FilePROPERTIES source) {
		this.key = key;
		this.value = value;
		if(value instanceof Color) {
			this.valueView = colorToString((Color) value);
		} else if(value instanceof ArrayList) {
			this.valueView = "";
			for(String string : (ArrayList<String>) value) {
				if(!valueView.isEmpty())
					valueView = valueView + " ";
				valueView = valueView + string;
			}
		} else {
			this.valueView = value.toString();
		}
		buildRoot();
		FilePROPERTIES.listComp.put(this, source);
	}
	private void buildRoot() {
		try {
			if(value instanceof Color) {
				FXMLLoader loader = new FXMLLoader(Main.getLayout("Files/Properties/PropertiesComponentColor"));
				loader.setController(this);
				this.root = loader.load();
				checkKey();
			} else {
				FXMLLoader loader = new FXMLLoader(Main.getLayout("Files/Properties/PropertiesComponent"));
				loader.setController(this);
				this.root = loader.load();
				checkKey();
				checkValue();
			}
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to load component's fxml from \"/io.github.MillenniarSt.MillenniarTexture.Files/PropertiesComponent.fxml\" - IOException in FilePROPERTIES.components.load", ConsoleLine.ERROR, exc));
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		keyField.setText(key);
		if(value instanceof Color) {
			valuePicker.setValue((Color) value);
		} else {
			valueField.setText(valueView);
		}
	}
	
	public void select() {
		FilePROPERTIES source = FilePROPERTIES.listComp.get(this);
		if(source.selected != null)
			source.selected.root.getStyleClass().remove(SELECTED);
		source.selected = this;
		this.root.getStyleClass().add(SELECTED);
	}
	
	public void modify() {
		checkValue();
		FilePROPERTIES.listComp.get(this).getPropEditor().setSelected(true);
		FilePROPERTIES.listComp.get(this).setSave(false);
	}
	public void modifyKey() {
		checkKey();
		FilePROPERTIES.listComp.get(this).getPropEditor().setSelected(true);
		FilePROPERTIES.listComp.get(this).setSave(false);
	}
	public void modifyColor() {
		FilePROPERTIES.listComp.get(this).getPropEditor().setSelected(true);
		FilePROPERTIES.listComp.get(this).setSave(false);
	}
	
	private void checkValue() {
		if(this.valueField.getText().isEmpty()) {
			valueField.getStyleClass().add(ERROR);
		} else {
			valueField.getStyleClass().remove(ERROR);
		}
	}
	private void checkKey() {
		if(this.keyField.getText().isEmpty()) {
			keyField.getStyleClass().add(ERROR);
		} else if(sameKey()) {
			keyField.getStyleClass().add(ERROR);
		} else {
			keyField.getStyleClass().remove(ERROR);
		}
	}
	private boolean sameKey() {
		if(FilePROPERTIES.listComp.get(this) != null) {
			for(ComponentProperties<?> comp : FilePROPERTIES.listComp.get(this).getPropertiesArea()) {
				if(this.keyField.getText().equals(comp.keyField.getText()) && this != comp)
					return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	void save() {
		this.key = keyField.getText();
		if(value instanceof Color) {
			this.value = (Value) valuePicker.getValue();
			this.valueView = colorToString(valuePicker.getValue());
		} else if(value instanceof ArrayList) {
			ArrayList<String> array = new ArrayList<>();
			String text = "";
			for(int i = 0; i < valueField.getText().trim().length(); i++) {
				char character = valueField.getText().trim().charAt(i);
				if(character != ' ') {
					text = text + character;
				} else {
					if(!text.trim().isEmpty()) {
						array.add(text);
					}
					text = "";
				}
			}
			if(!text.trim().isEmpty()) {
				array.add(text);
			}
			this.value = (Value) array;
			this.valueView = valueField.getText().trim();
		}
	}
	
	public static String colorToString(Color color) {
		return String.format("%02X%02X%02X", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255)).toLowerCase();
	}
	
	String getKey() {
		return key;
	}
	void setKey(String key) {
		this.key = key;
	}
	Value getValue() {
		return value;
	}
	void setValue(Value value) {
		this.value = value;
	}
	public String getValueView() {
		return valueView;
	}
	public void setValueView(String valueView) {
		this.valueView = valueView;
	}
	public void setValueView(Color color) {
		this.valueView = colorToString(color);
	}
	public void setValueView(ArrayList<String> array) {
		this.valueView = "";
		for(String string : array) {
			if(!valueView.isEmpty())
				valueView = valueView + " ";
			valueView = valueView + string;
		}
	}
	public AnchorPane getRoot() {
		return root;
	}
}
