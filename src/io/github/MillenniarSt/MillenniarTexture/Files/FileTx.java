package io.github.MillenniarSt.MillenniarTexture.Files;

import java.io.File;
import java.io.Serial;
import java.util.ArrayList;

import io.github.MillenniarSt.MillenniarTexture.Objects.Component.TextureObject;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.CopyFile;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.DeleteFile;
import io.github.MillenniarSt.MillenniarTexture.Menu.ManagmentStage;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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

public class FileTx extends File {
	
	@Serial
	private static final long serialVersionUID = 0L;
	private final SaveTx saved;
	
	private String type;
	private Boolean activated;
	private ImageView icon;
	private String problems;
	private ArrayList<TextureObject> sources;
	
	private Tab tab;
	private boolean save;
	
	public String NULL_TEXT = "null_text";
	public String FILE_NOT_FOUND = "file_not_found";
	public String DERIVATION_NOT_FOUND = "derivation_not_found";
	public String NO_NUMBER = "no_number";
	
	public FileTx(String path, SaveTx saved, ImageView icon) {
		super(path);
		this.saved = saved;
		if(icon != null)
			this.icon = icon;
		else
			this.icon = new ImageView(Main.getResource("icon/not_found"));
		this.type = saved.getType();
		this.activated = saved.isActivated();
		this.problems = saved.getProblems();
		this.sources = saved.getSources();
	}
	
	public FileTx(String path, String type, boolean activated, ImageView icon) {
		super(path);
		this.type = type;
		this.activated = activated;
		if(icon != null)
			this.icon = icon;
		else
			this.icon = new ImageView(Main.getResource("icon/not_found"));
		this.sources = new ArrayList<TextureObject>();
		this.problems = "Null";
		this.save = true;
		this.saved = new SaveTx(this);
	}
	
	public FileTx(String path, String type, boolean activated, ImageView icon, ArrayList<TextureObject> sources) {
		super(path);
		this.type = type;
		this.activated = activated;
		if(icon != null)
			this.icon = icon;
		else
			this.icon = new ImageView(Main.getResource("icon/not_found"));
		this.sources = sources;
		this.problems = "Null";
		this.save = true;
		this.saved = new SaveTx(this);
	}
	
	public boolean save() {
		ManagmentStage.getOn().getTexturePack().getObjects().putFile(this.getPath(), this.saved);
		this.save = true;
		return true;
	}
	
	public void register() {
		ManagmentStage.getOn().getTexturePack().getObjects().putFile(this.getPath(), this.saved);
	}
	
	public boolean deleteTx() {
		new DeleteFile(this, true).execute(true);
		return true;
	}
	
	public void duplicate(Stage stage) {
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save");
	    fileChooser.getExtensionFilters().addAll(
	    		new ExtensionFilter(this.type + " files", "*." + this.type),
	    		new ExtensionFilter("All io.github.MillenniarSt.MillenniarTexture.Files", "*.*"));
	    File file = fileChooser.showSaveDialog(stage);
		if(file != null) {
			new CopyFile(this.getPath(), file.getPath(), true).execute(true);
		}
	}
	
	public void open(ManagmentStage stage) {
		MSDialog.showError("Fail Open", "No editor found for this file type");
	}

	public void read() {
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
		this.saved.setType(this.type);
	}
	public Boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
		this.saved.setActivated(this.activated);
	}
	public ImageView getIcon() {
		return icon;
	}
	public void setIcon(ImageView icon) {
		this.icon = icon;
	}
	public ArrayList<TextureObject> getSources() {
		return sources;
	}
	public void setSources(ArrayList<TextureObject> sources) {
		this.sources = sources;
		this.saved.setSources(this.sources);
	}
	public String getProblems() {
		return problems;
	}
	public void setProblems(String problems) {
		this.problems = problems;
		this.saved.setProblems(this.problems);
	}
	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}
	public boolean isSave() {
		return save;
	}
	public void setSave(boolean save) {
		this.save = save;
		if(tab != null) {
			if(save) {
				tab.setText(this.getName());
			} else {
				tab.setText("*" + this.getName());
			}
		}
	}
}
