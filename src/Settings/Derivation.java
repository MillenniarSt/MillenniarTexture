package io.github.MillenniarSt.MillenniarTexture.Settings;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;

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

public class Derivation implements Serializable, ProgrammePath {
	
	@Serial
	private static final long serialVersionUID = 2L;
	private String name;
	private final String code;
	private String version;
	private final File source;
	
	public Derivation(String name, String code, String version, File source) {
		this.name = name;
		this.code = code;
		this.version = version;
		this.source = source;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public File getSource() {
		return source;
	}
}
