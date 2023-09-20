package io.github.MillenniarSt.MillenniarTexture.CreateTexture;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;

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

public class TexturePack implements Serializable, ProgrammePath {
	
	final static long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private byte version;
	private String image;
	private boolean optifine;
	private Register objects;
	
	public static ArrayList<TexturePack> textures = new ArrayList<>();
	
	public String versionString;

	public TexturePack(String name, String description, byte version, String image) {
		this.name = name;
		this.description = description;
		this.version = version;
		this.versionString = getVersionString();
		this.image = image;
		this.optifine = false;
		textures.add(this);
	}
	
	public TexturePack(String name, String description, String version, String image) {
		this.name = name;
		this.description = description;
		this.setVersionString(version);
		this.versionString = getVersionString();
		this.image = image;
		this.optifine = false;
		textures.add(this);
	}
	
	public TexturePack() {
		this.name = "";
		this.description = "";
		this.version = 9;
		this.image = "";
	}
	
	TexturePack(String name) {
		this.name = name;
	}
	
	public static TexturePack getTexturePack(String name) {
		for(TexturePack texturePack : textures) {
			if(texturePack.name.equals(name))
				return texturePack;
		}
		return null;
	}
	
	public static int versionToInt(String version) {
		if(version.equals("1.6.1 - 1.8.9"))
			return 1;
		if(version.equals("1.9 - 1.10.2"))
			return 2;
		if(version.equals("1.11 - 1.12.2"))
			return 3;
		if(version.equals("1.13 - 1.14.4"))
			return 4;
		if(version.equals("1.15 - 1.16.1"))
			return 5;
		if(version.equals("1.16.2 - 1.16.5"))
			return 6;
		if(version.equals("1.17.x"))
			return 7;
		if(version.equals("1.18.x"))
			return 8;
		if(version.equals("1.19 - 1.19.2"))
			return 9;
		if(version.equals("1.19.3"))
			return 12;
		if(version.equals("1.19.4"))
			return 13;
		if(version.equals("1.20.x"))
			return 15;
		return -1;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getVersionByte() {
		return version;
	}
	
	public void setVersionString(String version) {
		if(version.equals("1.6.1 - 1.8.9"))
			this.version = 1;
		if(version.equals("1.9 - 1.10.2"))
			this.version = 2;
		if(version.equals("1.11 - 1.12.2"))
			this.version = 3;
		if(version.equals("1.13 - 1.14.4"))
			this.version = 4;
		if(version.equals("1.15 - 1.16.1"))
			this.version = 5;
		if(version.equals("1.16.2 - 1.16.5"))
			this.version = 6;
		if(version.equals("1.17.x"))
			this.version = 7;
		if(version.equals("1.18.x"))
			this.version = 8;
		if(version.equals("1.19 -1.19.2"))
			this.version = 9;
		if(version.equals("1.19.3"))
			this.version = 12;
		if(version.equals("1.19.4"))
			this.version = 13;
		if(version.equals("1.20.x"))
			this.version = 15;
	}
	
	public String getVersionString() {
		if(version == 1)
			return "1.6.1 - 1.8.9";
		if(version == 2)
			return "1.9 - 1.10.2";
		if(version == 3)
			return "1.11 - 1.12.2";
		if(version == 4)
			return "1.13 - 1.14.4";
		if(version == 5)
			return "1.15 - 1.16.1";
		if(version == 6)
			return "1.16.2 - 1.16.5";
		if(version == 7)
			return "1.17.x";
		if(version == 8)
			return "1.18.x";
		if(version == 9)
			return "1.19 - 1.19.2";
		if(version == 12)
			return "1.19.3";
		if(version == 13)
			return "1.19.4";
		if(version == 15)
			return "1.20.x";
		return null;
	}
	
	public static String versionToString(float version, byte versionFlext) {
		if(version == 1.6f || version == 1.7f || version == 1.8f)
			return "1.6.1 - 1.8.9";
		else if(version == 1.9f || version == 1.10f)
			return "1.9 - 1.10.2";
		else if(version == 1.11f || version == 1.12f)
			return "1.11 - 1.12.2";
		else if(version == 1.13f || version == 1.14f)
			return "1.13 - 1.14.4";
		else if(version == 1.15f || (version == 1.16f && versionFlext <= 1))
			return "1.15 - 1.16.1";
		else if(version == 1.16f)
			return "1.16.2 - 1.16.5";
		else if(version == 1.17f)
			return "1.17.x";
		else if(version == 1.18f)
			return "1.18.x";
		else if(version == 1.19f && versionFlext <= 2)
			return "1.19 - 1.19.2";
		else if(version == 1.19f && versionFlext == 3)
			return "1.19.3";
		else if(version == 1.19f)
			return "1.19.4";
		else if(version == 1.20f)
			return "1.20.x";
		return null;
	}

	public void setVersion(byte version) {
		this.version = version;
		this.versionString = getVersionString();
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isOptifine() {
		if(new File(buildTexture + this.name + "\\assets\\minecraft\\optifine").exists())
			optifine = true;
		else
			optifine = false;
		return optifine;
	}

	public Register getObjects() {
		return objects;
	}

	public void setObjects(Register objects) {
		this.objects = objects;
	}
}
