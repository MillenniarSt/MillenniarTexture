package io.github.MillenniarSt.MillenniarTexture.Main;

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

public interface ProgrammePath {
	
	final String MillenniarTexturePath = "C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\MillenniarTexture\\";
	
	final String buildTexture = MillenniarTexturePath + "TextureWorkspace\\";
	final String data = MillenniarTexturePath + "data\\";
	final String resource = MillenniarTexturePath + "resource\\";
	final String derivations = MillenniarTexturePath + "derivation\\";
	final String defaultFile = resource + "default_file\\";
	final String canche = MillenniarTexturePath + "canche\\";
}
