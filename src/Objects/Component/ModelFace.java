package io.github.MillenniarSt.MillenniarTexture.Objects.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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

public class ModelFace implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String[] faces = {"north", "east", "south", "west", "up", "down"};
	
	private String name;
	private ArrayList<Float> uv;
	private int rotation;
	private String texture;
	private int tintindex;
	private String cullface;
	private HashMap<String, Object> other;
	private boolean transparent;
	
	public ModelFace(String name) {
		this.name = name;
		this.uv = new ArrayList<>();
		this.uv.add(0, 0f);
		this.uv.add(1, 0f);
		this.uv.add(2, 16f);
		this.uv.add(3, 16f);
		this.rotation = 0;
		this.texture = "";
		this.tintindex = -1;
		this.cullface = null;
		this.other = new HashMap<>();
	}
	public ModelFace(String name, String cullface) {
		this.name = name;
		this.uv = new ArrayList<>();
		this.uv.add(0, 0f);
		this.uv.add(1, 0f);
		this.uv.add(2, 16f);
		this.uv.add(3, 16f);
		this.rotation = 0;
		this.texture = "";
		this.tintindex = -1;
		this.cullface = cullface;
		this.other = new HashMap<>();
	}
	
	public ModelFace(String name, ArrayList<Float> uv, int rotation, String texture, int tintindex, String cullface) {
		this.name = name;
		this.uv = uv;
		this.rotation = rotation;
		this.texture = texture;
		this.tintindex = tintindex;
		this.cullface = cullface;
		this.other = new HashMap<>();
	}
	
	public ModelFace(String name, ArrayList<Float> uv, int rotation, String texture, int tintindex, String cullface, HashMap<String, Object> other) {
		this.name = name;
		this.uv = uv;
		this.rotation = rotation;
		this.texture = texture;
		this.tintindex = tintindex;
		this.cullface = cullface;
		this.other = other;
	}
	
	@Override
	public ModelFace clone() {
		ModelFace output = new ModelFace(name, cullface);
		output.uv.clear();
		for(float uvF : uv) {
			output.uv.add(uvF);
		}
		output.rotation = rotation;
		output.texture = texture;
		output.tintindex = tintindex;
		output.transparent = transparent;
		for(String key : other.keySet()) {
			output.other.put(key, other.get(key));
		}
		return output;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Float> getUv() {
		return uv;
	}
	public void setUv(ArrayList<Float> uv) {
		this.uv = uv;
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	public String getTexture() {
		return texture;
	}
	public void setTexture(String texture) {
		this.texture = texture;
	}
	public HashMap<String, Object> getOther() {
		return other;
	}
	public void setOther(HashMap<String, Object> other) {
		this.other = other;
	}
	public int getTintindex() {
		return tintindex;
	}
	public void setTintindex(int tintindex) {
		this.tintindex = tintindex;
	}
	public String getCullface() {
		return cullface;
	}
	public void setCullface(String cullface) {
		this.cullface = cullface;
	}
	public boolean isTransparent() {
		return transparent;
	}
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
}
