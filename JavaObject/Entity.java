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

public class Entity extends TextureObject{

	private static final long serialVersionUID = 1L;
	String name;
	String derivation;
	ArrayList<File> textures;
	
	protected Entity(String name, String derivation, ArrayList<File> textures) {
		super(name, derivation);
		this.textures = textures;
	}
	
	public String toString(Entity ne) {
		String textureString = "";
		for(File f : ne.textures) {
			textureString = " " + textureString + f.getName() + "\n";
		}
		return  ne.name + "\n" +
				"- " + ne.derivation + "\n" +
				"Textures" + "\n" +
				textureString;
	}
}
