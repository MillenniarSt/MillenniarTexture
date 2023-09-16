package JavaObject;

import java.io.File;
import java.util.ArrayList;

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

public class Item extends TextureObject{
	
	private static final long serialVersionUID = 1L;
	String name;
	String derivation;
	File model;
	ArrayList<File> textures;
	
	public Item(String name, String derivation, File model, ArrayList<File> textures) {
		super(name, derivation);
		this.model = model;
		this.textures = textures;
	}
	
	public String toString(Item ne) {
		String textureString = "";
		for(File f : ne.textures) {
			textureString = " " + textureString + f.getName() + "\n";
		}
		return  ne.name + "\n" +
				"- " + ne.derivation + "\n" +
				"Models" + "\n" +
				" " + ne.model +
				"Textures" + "\n" +
				textureString;
	}
}
