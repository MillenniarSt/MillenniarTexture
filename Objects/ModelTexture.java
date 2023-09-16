package Objects;

import java.io.File;

import JavaObject.Texture;
import javafx.scene.image.Image;
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
public class ModelTexture extends Texture {
	private static final long serialVersionUID = 1L;
	
	private String modelId;
	private ImageView image;
	
	public ModelTexture(File file, String path) {
		super(file, path);
		this.image = new ImageView();
	}
	public ModelTexture(File file, String path, String modelId) {
		super(file, path);
		this.modelId = modelId;
	}
	public ModelTexture(File file, String path, String modelId, Image image) {
		super(file, path);
		this.modelId = modelId;
		this.image = new ImageView(image);
		this.image.setFitHeight(16);
		this.image.setFitWidth(16);
	}
	public ModelTexture(File file, String category, String name, String modelId, Image image) {
		super(file, category, name);
		this.modelId = modelId;
		this.image = new ImageView(image);
		this.image.setFitHeight(16);
		this.image.setFitWidth(16);
	}
	
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public ImageView getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image.setImage(image);
	}
}
