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

public class ModelRotation implements Serializable {
	private static final long serialVersionUID = 1L;

	private float angle;
	private String axis;
	private ArrayList<Float> origin;
	private HashMap<String, Object> other;
	
	public ModelRotation() {
		this.origin = new ArrayList<>();
		origin.add(0, 0f);
		origin.add(1, 0f);
		origin.add(2, 0f);
		this.other = new HashMap<String, Object>();
	}
	
	public ModelRotation(float angle, String axis, ArrayList<Float> origin, HashMap<String, Object> other) {
		this.angle = angle;
		this.axis = axis;
		this.origin = origin;
		this.other = other;
	}
	
	@Override
	public ModelRotation clone() {
		ModelRotation output = new ModelRotation();
		output.angle = angle;
		output.axis = axis;
		output.origin.clear();
		for(float value : origin) {
			output.origin.add(value);
		}
		for(String key : other.keySet()) {
			output.other.put(key, other.get(key));
		}
		return output;
	}
	
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	public String getAxis() {
		return axis;
	}
	public void setAxis(String axis) {
		this.axis = axis;
	}
	public ArrayList<Float> getOrigin() {
		return origin;
	}
	public void setOrigin(ArrayList<Float> origin) {
		this.origin = origin;
	}
	public HashMap<String, Object> getOther() {
		return other;
	}
	public void setOther(HashMap<String, Object> other) {
		this.other = other;
	}
}
