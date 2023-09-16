package Files;

import java.io.Serializable;
import java.util.ArrayList;

import JavaObject.TextureObject;

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

public class SaveTx implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String type;
	private boolean activated;
	private String problems;
	private ArrayList<TextureObject> sources;
	
	SaveTx(FileTx file) {
		this.type = file.getType();
		this.activated = file.isActivated();
		this.problems = file.getProblems();
		this.sources = file.getSources();
	}
	
	@Override
	public String toString() {
		return this.getClass()+"SaveTx:[type:"+type+",activated:"+activated+",problems:"+problems+",sources:"+sources+"]"; 
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public ArrayList<TextureObject> getSources() {
		return sources;
	}
	public void setSources(ArrayList<TextureObject> sources) {
		this.sources = sources;
	}
}
