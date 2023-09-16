package JavaObject;

import java.io.Serializable;
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

public class RandomVariant implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Model model;
	private int weigh;
	private int x;
	private int y;
	private int z;
	private HashMap<String, Object> other;
	
	RandomVariant() {
		this.model = null;
		this.weigh = 1;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.other = new HashMap<>();
	}
	public RandomVariant(Model model, int weight, int x, int y, int z) {
		this.model = model;
		this.weigh = weight;
		this.x = x;
		this.y = y;
		this.z = z;
		this.other = new HashMap<>();
	}
	public RandomVariant(Model model, int weight, int x, int y, int z, HashMap<String, Object> other) {
		this.model = model;
		this.weigh = weight;
		this.x = x;
		this.y = y;
		this.z = z;
		this.other = other;
	}
	
	@Override
	public RandomVariant clone() {
		RandomVariant output = new RandomVariant(model.clone(), weigh, x, y, z);
		for(String key : other.keySet()) {
			output.other.put(key, other.get(key));
		}
		return output;
	}
	
	@Override
	public String toString() {
		if(model == null)
			return "No model";
		else
			return this.model.getValue();
	}
	
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public int getWeight() {
		return weigh;
	}
	public void setWeigh(int weigh) {
		this.weigh = weigh;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}

	public HashMap<String, Object> getOther() {
		return other;
	}

	public void setOther(HashMap<String, Object> other) {
		this.other = other;
	}
}
