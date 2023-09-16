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

public class Variant implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<String> conditions;
	private ArrayList<RandomVariant> random;
	private HashMap<String, Object> other;
	
	private Variant() {
		this.conditions = new ArrayList<>();
		this.random = new ArrayList<>();
		this.other = new HashMap<>();
	}
	Variant(ArrayList<String> conditions) {
		this.conditions = conditions;
		this.random = new ArrayList<>();
		this.other = new HashMap<>();
	}
	public Variant(ArrayList<String> conditions, ArrayList<RandomVariant> random) {
		this.conditions = conditions;
		this.random = random;
		this.other = new HashMap<>();
	}
	public Variant(ArrayList<String> conditions, ArrayList<RandomVariant> random, HashMap<String, Object> other) {
		this.conditions = conditions;
		this.random = random;
		this.other = other;
	}
	
	public String conditionsToString() {
		String output = "";
		for(String condition : conditions) {
			if(!output.equals(""))
				output = output + ",";
			output = output + condition;
		}
		return output;
	}
	
	@Override
	public Variant clone() {
		Variant output = new Variant();
		for(String condition : conditions) {
			output.conditions.add(condition);
		}
		for(String key : other.keySet()) {
			output.other.put(key, other.get(key));
		}
		for(RandomVariant random : random) {
			output.random.add(random.clone());
		}
		return output;
	}
	
	@Override
	public String toString() {
		if(this.conditions.isEmpty())
			return "Always";
		else
			return this.conditionsToString();
	}
	
	public ArrayList<String> getConditions() {
		return conditions;
	}
	public void setConditions(ArrayList<String> conditions) {
		this.conditions = conditions;
	}
	public void setConditions(String string) {
		ArrayList<String> conditions = new ArrayList<>();
		if(!string.trim().equals("")) {
            try {
                while(string.contains(",")) {
                    conditions.add(string.substring(0, string.indexOf(",")));
                    string = string.substring(string.indexOf(",") + 1);
                }
                conditions.add(string);
            } catch(StringIndexOutOfBoundsException exc) { }
        }
		this.conditions = conditions;
	}
	public ArrayList<RandomVariant> getRandom() {
		return random;
	}
	public void setRandom(ArrayList<RandomVariant> random) {
		this.random = random;
	}
	public HashMap<String, Object> getOther() {
		return other;
	}
	public void setOther(HashMap<String, Object> other) {
		this.other = other;
	}
}
