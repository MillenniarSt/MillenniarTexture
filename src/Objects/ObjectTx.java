package io.github.MillenniarSt.MillenniarTexture.Objects;

import io.github.MillenniarSt.MillenniarTexture.Objects.Component.TextureObject;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import javafx.scene.control.Tab;
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

public class ObjectTx {

	private Tab tab;
	private ImageView icon;
	private TextureObject object;
	private boolean save;
	
	private String name;
	private String derivation;
	private final String type;
	private Boolean activated;
	private String problems;
	
	public ObjectTx(TextureObject object, String type, ImageView icon) {
		this.object = object;
		this.type = type;
		this.name = object.getName();
		this.derivation = object.getDerivation();
		this.activated = object.isActivated();
		this.problems = object.getProblems();
		if(icon != null)
			this.icon = icon;
		else
			this.icon = new ImageView(Main.getResource("icon/not_found"));
		this.save = true;
	}
	public ObjectTx(String name, String derivation, String type, ImageView icon) {
		this.name = name;
		this.derivation = derivation;
		this.type = type;
		this.activated = true;
		this.problems = "";
		if(icon != null)
			this.icon = icon;
		else
			this.icon = new ImageView(Main.getResource("icon/not_found"));
		this.save = true;
		this.object = new TextureObject(this);
	}


	public void open() {
	}
	
	public void save() {
	}
	
	public void close() {
		if(!this.isSave()) {
			String confirm = MSDialog.showConfirm("Save changes", "Do you want to save the changes", "Yes", "No", "Cancel");
			if(confirm.equals("Yes")) {
				this.save();
				this.getTab().getTabPane().getTabs().remove(this.getTab());
				this.setSave(true);
				this.setTab(null);
			} else if(confirm.equals("No")) {
				this.getTab().getTabPane().getTabs().remove(this.getTab());
				this.setSave(true);
				this.setTab(null);
			}
		} else {
			this.getTab().getTabPane().getTabs().remove(this.getTab());
			this.setSave(true);
			this.setTab(null);
		}
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}
	public TextureObject getObject() {
		return object;
	}
	public void setObject(TextureObject object) {
		this.object = object;
	}
	public boolean isSave() {
		return save;
	}
	public void setSave(boolean save) {
		this.save = save;
		if(tab != null)
			if(save)
				this.tab.setText(this.name);
			else
				this.tab.setText("*" + this.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDerivation() {
		return derivation;
	}

	public void setDerivation(String derivation) {
		this.derivation = derivation;
	}
	public String getType() {
		return type;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	public String getProblems() {
		return problems;
	}
	public void setProblems(String problems) {
		this.problems = problems;
	}
	public ImageView getIcon() {
		return icon;
	}
	public void setIcon(ImageView icon) {
		this.icon = icon;
	}
}
