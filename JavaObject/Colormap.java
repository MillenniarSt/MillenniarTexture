package JavaObject;

import java.io.File;

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

public class Colormap extends TextureObject{

	private static final long serialVersionUID = 1L;
	String name;
	String derivation;
	File colormap;
	
	public Colormap(String name, String derivation, File colormap) {
		super(name, derivation);
		this.colormap = colormap;
	}
	
	public String toString(Colormap ne) {
		return  ne.name + "\n" +
				"- " + ne.derivation + "\n" +
				"Colormap" + "\n" +
				"" + ne.colormap.getName();
	}
}
