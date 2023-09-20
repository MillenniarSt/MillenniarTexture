package io.github.MillenniarSt.MillenniarTexture.Files;

import java.io.File;
import java.io.Serial;
import java.util.ArrayList;

import io.github.MillenniarSt.MillenniarTexture.Objects.Component.TextureObject;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.CopyFile;
import io.github.MillenniarSt.MillenniarTexture.Menu.ManagmentStage;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
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

	@Serial
	private static final long serialVersionUID = 1L;
	
	private TreeItem<FileTx> location;
	
	public DirectoryTx(String path, SaveTx saved) {
		super(path, saved, new ImageView(Main.getResource("icon/directory")));
	}
	
	public DirectoryTx(String path, boolean activated, TreeItem<FileTx> location) {
		super(path, "directory", activated, new ImageView(Main.getResource("icon/directory")));
		this.location = location;
	}
	
	public DirectoryTx(String path, boolean activated, ArrayList<TextureObject> sources, TreeItem<FileTx> location) {
		super(path, "directory", activated, new ImageView(Main.getResource("icon/directory")), sources);
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
