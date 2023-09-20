package io.github.MillenniarSt.MillenniarTexture.Objects.Component;

import java.io.File;
import java.io.Serializable;

public class Texture extends Component implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final byte id = 4;
	
	public static final String[] categories = {"block", "item", "entity", "gui", "painting", "colormap", "effect", "environment", "font", "map", "misc", "mob_effect", "models", "particle"};
	
	private String category;
	private String name;

	private Animation aniation;
	
	public Texture(File file, String path) {
		super(file, path);
		if(getPath().contains("\\")) {
			this.name = getPath().substring(getPath().indexOf("\\") + 1);
			this.category = getPath().substring(0, getPath().indexOf("\\"));
		} else if(getPath().contains("/")) {
			this.name = getPath().substring(getPath().indexOf("/") + 1);
			this.category = getPath().substring(0, getPath().indexOf("/"));
		} else {
			this.category = "";
			this.name = getPath();
		}
	}
	public Texture(File file, String category, String name) {
		super(file, category + "/" + name);
		this.category = category;
		this.name = name;
	}
	public Texture(File file, String category, String name, String derivation) {
		super(file, derivation, category + "/" + name);
		this.category = category;
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
		if(category.equals(""))
			this.setPath(name);
		else
			this.setPath(category + "/" + name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		if(category.equals(""))
			this.setPath(name);
		else
			this.setPath(category + "/" + name);
	}
	public Animation getAniation() {
		return aniation;
	}
	public void setAniation(Animation aniation) {
		this.aniation = aniation;
	}
}
