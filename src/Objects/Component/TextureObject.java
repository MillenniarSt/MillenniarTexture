package io.github.MillenniarSt.MillenniarTexture.Objects.Component;

import java.io.Serializable;
import java.util.ArrayList;

import io.github.MillenniarSt.MillenniarTexture.Objects.Block;
import io.github.MillenniarSt.MillenniarTexture.Objects.ObjectTx;

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

public class TextureObject implements Serializable {
	
	public static ArrayList<TextureObject> textureJavaObjects = new ArrayList<>();
	final static long serialVersionUID = 1L;
	
	private String name;
	private String derivation;
	private boolean activated;
	private String problems;
	
	protected TextureObject(String name, String derivation) {
		this.setName(name);
		this.setDerivation(derivation);
		this.activated = true;
		this.problems = "";
		textureJavaObjects.add(this);
	}
	public TextureObject(ObjectTx object) {
		this.name = object.getName();
		this.derivation = object.getDerivation();
		this.activated = object.isActivated();
		this.problems = object.getProblems();
	}
	
	public void delete() { }
	
	public boolean equals(TextureObject obj) {
		if(this.name.equals(obj.name) && this.derivation.equals(obj.derivation)) {
			if(this instanceof Block && obj instanceof Block)
				return true;
		}
		return false;
	}
	
	public static TextureObject getTextureObject(String name, String derivation, String type) {
		for(TextureObject obj : textureJavaObjects) {
			if(obj.name.equals(name) && obj.derivation.equals(derivation)) {
				if(obj instanceof Block && type.equals("block"))
					return obj;
			}
		}
		return null;
	}
	
	public String toString() {
		return this.name;
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
		if(this instanceof Block)
			return "Block";
		return null;
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
}