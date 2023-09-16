package Files;

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

public class SavePROPERTIES extends SaveTXT {
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, ArrayList<String>> properties;

	protected SavePROPERTIES(FilePROPERTIES file) {
		super(file);
		if(file.getProperties() != null)
			properties = file.getProperties().getProperties();
	}

	public HashMap<String, ArrayList<String>> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, ArrayList<String>> properties) {
		this.properties = properties;
	}
}
