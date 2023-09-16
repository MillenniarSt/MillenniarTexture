package JavaObject;

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

public class ModelElement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Float> positionFrom;
	private ArrayList<Float> positionTo;
	private ModelRotation rotation;
	private ArrayList<ModelFace> faces;
	private HashMap<String, Object> other;
	
	public ModelElement() {
		ArrayList<Float> from = new ArrayList<>();
		ArrayList<Float> to = new ArrayList<>();
		for(int i = 1; i <= 3; i++) {
			from.add(0.0f);
			to.add(16.0f);
		}
		this.positionFrom = from;
		this.positionTo = to;
		this.rotation = null;
		ArrayList<ModelFace> faces = new ArrayList<>();
		for(String face : ModelFace.faces) {
			faces.add(new ModelFace(face, face));
		}
 		this.faces = faces;
		this.other = new HashMap<>();
	}
	
	public ModelElement(ArrayList<Float> positionFrom, ArrayList<Float> positionTo, ModelRotation rotation, ArrayList<ModelFace> faces, HashMap<String, Object> other) {
		this.positionFrom = positionFrom;
		this.positionTo = positionTo;
		this.rotation = rotation;
		this.faces = faces;
		this.other = other;
	}
	
	@Override
	public String toString() {
		String from = "";
		String to = "";
		for(Float pos : positionFrom) {
			if(!from.equals(""))
				from = from + "-";
			from = from + pos;
		}
		for(Float pos : positionTo) {
			if(!to.equals(""))
				to = to + "-";
			to = to + pos;
		}
		return from + " : " + to;
	}
	
	@Override
	public ModelElement clone() {
		ModelElement output = new ModelElement();
		output.positionFrom.clear();
		for(float from : positionFrom) {
			output.positionFrom.add(from);
		}
		output.positionTo.clear();
		for(float to : positionTo) {
			output.positionTo.add(to);
		}
		if(rotation != null)
			output.rotation = rotation.clone();
		output.faces.clear();
		for(ModelFace face : faces) {
			output.faces.add(face.clone());
		}
		if(other != null)
			for(String key : other.keySet()) {
				output.other.put(key, other.get(key));
			}
		else
			other = new HashMap<>();
		return output;
	}
	
	public ArrayList<Float> getPositionFrom() {
		return positionFrom;
	}
	public void setPositionFrom(ArrayList<Float> positionFrom) {
		this.positionFrom = positionFrom;
	}
	public ArrayList<Float> getPositionTo() {
		return positionTo;
	}
	public void setPositionTo(ArrayList<Float> positionTo) {
		this.positionTo = positionTo;
	}
	public ArrayList<ModelFace> getFaces() {
		return faces;
	}
	public void setFaces(ArrayList<ModelFace> faces) {
		this.faces = faces;
	}
	public ModelRotation getRotation() {
		return rotation;
	}
	public void setRotation(ModelRotation rotation) {
		this.rotation = rotation;
	}
	public HashMap<String, Object> getOther() {
		return other;
	}
	public void setOther(HashMap<String, Object> other) {
		this.other = other;
	}
}
