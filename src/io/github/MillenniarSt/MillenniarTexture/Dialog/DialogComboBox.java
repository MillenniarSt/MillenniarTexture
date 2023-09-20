package io.github.MillenniarSt.MillenniarTexture.Dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;

import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.Settings.Settings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class DialogComboBox<Item> implements Initializable {
	
	@FXML
	private ComboBox<Item> comboInput;
	
	private ObservableList<Item> items;
	private final HashMap<Item, Image> images;
	
	public DialogComboBox(ObservableList<Item> items) {
		this.items = items;
		this.images = new HashMap<>();
	}
	public DialogComboBox(ObservableList<Item> items, HashMap<Item, Image> images) {
		this.items = items;
		this.images = images;
	}
	
	public Item showComboInput(String title, Item defaultValue) {
        DialogPane dialogPane;
		try {
			FXMLLoader loader = new FXMLLoader(Main.getLayout("Dialog/ComboPopup"));
			loader.setController(this);
			dialogPane = loader.load();
			dialogPane.setHeaderText(title);
			dialogPane.getStylesheets().add(Settings.getTheme());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Millenniar Texture - Combo input");
            ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(Main.getResource("icon"));
            
        	if(defaultValue != null) {
        		comboInput.getSelectionModel().select(defaultValue);
			} else {
        		if(!items.isEmpty())
        			comboInput.getSelectionModel().select(0);
        	}
        	ButtonType button = dialog.showAndWait().orElse(ButtonType.CANCEL);
        	if(button.equals(ButtonType.OK)) {
        		return comboInput.getSelectionModel().getSelectedItem();
        	} else {
        		return null;
        	}
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		return null;
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		comboInput.setItems(items);
        comboInput.setCellFactory(param -> new ListCell<Item>() {
        	@Override
            public void updateItem(Item element, boolean empty) {
            	super.updateItem(element, empty);
            	if (empty) {
            		setText(null);
            		setGraphic(null);
            	} else {
            		setText(element.toString());
            		Image image = images.get(element);
            		if(image != null) {
	            		ImageView imageView = new ImageView(image);
	            		imageView.setFitHeight(16);
	            		imageView.setFitWidth(16);
	            		setGraphic(imageView);
            		}
            	}
        	}
        });
	}
	
	public ObservableList<Item> getItems() {
		return items;
	}
	public void setItems(ObservableList<Item> items) {
		this.items = items;
	}
}
