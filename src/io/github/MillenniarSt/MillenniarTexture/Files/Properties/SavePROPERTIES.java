package io.github.MillenniarSt.MillenniarTexture.Files.Properties;

import io.github.MillenniarSt.MillenniarTexture.Files.TXT.SaveTXT;

import java.io.Serial;
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
	@Serial
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
