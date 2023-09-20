package io.github.MillenniarSt.MillenniarTexture.Files.PNG;

import java.net.URL;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Files.PNG.FilePNG.Pixel;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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

public class ResizePNG implements Initializable {

	private static final ObservableList<Float> listResize = FXCollections.observableArrayList(2.0f, 3.0f, 4.0f, 5.0f, 8.0f, 16.0f);
	
	@FXML
	private RadioButton numbOpt;
	@FXML
	private TextField xSize;
	@FXML
	private TextField ySize;
	@FXML
	private RadioButton extOpt;
	@FXML
	private ComboBox<Float> resizeCombo;
	
	private final FilePNG subject;
	private final Stage stage;
	
	protected ResizePNG(FilePNG subject, Stage stage) {
		this.subject =subject;
		this.stage = stage;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ToggleGroup group = new ToggleGroup();
		numbOpt.setToggleGroup(group);
		extOpt.setToggleGroup(group);
		xSize.setText(subject.getPixels().length + "");
		ySize.setText(subject.getPixels()[0].length + "");
		resizeCombo.setItems(listResize);
		resizeCombo.getSelectionModel().select(2.0f);
	}

	public void applyResize() {
		try {
			if(numbOpt.isSelected()) {
				Pixel[][] pixels = new Pixel[Integer.parseInt(xSize.getText())][Integer.parseInt(ySize.getText())];
				for(int i = 0; i < pixels.length; i++) {
					for(int j = 0; j < pixels[0].length; j++) {
						try {
							pixels[i][j] = subject.getPixels()[i][j];
						} catch(ArrayIndexOutOfBoundsException exc) {
							Pixel newPixel = new Pixel(i, j);
							pixels[i][j] = newPixel;
							newPixel.setOnMousePressed(subject);
						}
					}
				}
				subject.setPixels(pixels);
				subject.refreshPixels();
				subject.changeZoom();
			} else if(extOpt.isSelected()) {
				Pixel[][] pixels = new Pixel[(int) (subject.getPixels().length * resizeCombo.getSelectionModel().getSelectedItem())]
						[(int) (subject.getPixels()[0].length * resizeCombo.getSelectionModel().getSelectedItem())];
				int i = 0;
				for(Pixel[] row : subject.getPixels()) {
					for(int a = 1; a <= resizeCombo.getSelectionModel().getSelectedItem(); a++) {
						int j = 0;
						for(Pixel pixel : row) {
							for(int b = 1; b <= resizeCombo.getSelectionModel().getSelectedItem(); b++) {
								Pixel newPixel =  new Pixel(pixel.getColor(), i, j);
								pixels[i][j] = newPixel;
								newPixel.setOnMousePressed(subject);
								j++;
							}
						}
						i++;
					}
				}
				subject.setPixels(pixels);
				subject.refreshPixels();
				subject.changeZoom();
			}
			subject.setSave(false);
			stage.close();
		} catch(NumberFormatException exc) {
			MSDialog.showError("Resize Fail", "s");
		}
	}
}
