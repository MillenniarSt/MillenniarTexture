package JavaObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

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

public class ComponentTx implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private File file;
	private String derivation;
	private String path;
	
	public ComponentTx(File file, String value) {
		this.file = file;
		if(value.contains(":")) {
			this.derivation = value.substring(0, value.indexOf(":"));
			this.path = value.substring(value.indexOf(":") +1);
		} else {
			this.path = value;
		}
	}
	public ComponentTx(File file, String derivation, String path) {
		this.file = file;
		if(derivation != null)
			if(derivation.equals(""))
				this.derivation = null;
			else
				this.derivation = derivation;
		else
			this.derivation = null;
		this.path = path;
	}

	public byte getId() {
		if(this instanceof Animation)
			return Animation.id;
		else if(this instanceof Model)
			return Model.id;
		else if(this instanceof Blockstate)
			return Blockstate.id;
		else if(this instanceof Properties)
			return Properties.id;
		else if(this instanceof Texture)
			return Texture.id;
		return (byte) -1;
	}
	
	public boolean build(ArrayList<Object> elements) {
		return false;
	}
	
	@Override
	public String toString() {
		return getValue();
	}
	
	public boolean equals(ComponentTx component) {
		return this.getValue().equals(component.getValue());
	}
	public boolean equals(ComponentTx component, String derivationDef) {
		return this.getValue(derivationDef).equals(component.getValue(derivationDef));
	}
	
	public String getValue() {
		if(derivation != null) {
			if(!derivation.equals(""))
				return derivation + ":" + path;
		}
		return path;
	}
	public String getValue(String derivationDef) {
		String derivation;
		if(this.derivation == null)
			derivation = derivationDef;
		else if(this.derivation.equals(""))
			derivation = derivationDef;
		else
			derivation = this.derivation;
		return derivation + ":" + path;
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDerivation() {
		return derivation;
	}
	public String getDerivationNotNull() {
		if(derivation == null)
			return "";
		return derivation;
	}
	public void setDerivation(String derivation) {
		if(derivation != null)
			if(derivation.equals(""))
				this.derivation = null;
			else
				this.derivation = derivation;
		else
			this.derivation = derivation;
	}
}
