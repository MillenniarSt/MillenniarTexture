package Files;

import java.io.File;
import java.util.ArrayList;

import JavaObject.TextureObject;
import Main.Main;
import ManagmentFile.CopyFile;
import Menu.ManagmentStage;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
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

public class DirectoryTx extends FileTx {

	private static final long serialVersionUID = 1L;
	
	private TreeItem<FileTx> location;
	
	public DirectoryTx(String path, SaveTx saved) {
		super(path, saved, new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/directory.png"))));
	}
	
	public DirectoryTx(String path, boolean activated, TreeItem<FileTx> location) {
		super(path, "directory", activated, new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/directory.png"))));
		this.location = location;
	}
	
	public DirectoryTx(String path, boolean activated, ArrayList<TextureObject> sources, TreeItem<FileTx> location) {
		super(path, "directory", activated, new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/directory.png"))), sources);
		this.location = location;
	}
	
	@Override
	public void duplicate(Stage stage) {
		DirectoryChooser fileChooser = new DirectoryChooser();
	    fileChooser.setTitle("Select Directory");
	    File file = fileChooser.showDialog(stage);
		if(file != null) {
			new CopyFile(this.getPath(), file.getPath(), true).execute(true);
		}
	}
	
	@Override
	public void open(ManagmentStage stage) {
		if(this.location.getParent() != null)
			this.location.getParent().setExpanded(true);
		
		MultipleSelectionModel<?> msm = stage.getFilesTree().getSelectionModel();
		int row = stage.getFilesTree().getRow(this.location);
		msm.select(row);
		stage.refreshFiles(this.location);
	}
	
	public TreeItem<FileTx> getLocation() {
		return location;
	}

	public void setLocation(TreeItem<FileTx> location) {
		this.location = location;
	}
}
