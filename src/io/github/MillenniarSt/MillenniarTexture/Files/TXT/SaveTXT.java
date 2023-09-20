package io.github.MillenniarSt.MillenniarTexture.Files.TXT;

/*            //////////////////////////////////
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

import io.github.MillenniarSt.MillenniarTexture.Files.SaveTx;
import io.github.MillenniarSt.MillenniarTexture.Files.TXT.FileTXT;

public class SaveTXT extends SaveTx {
	
	private static final long serialVersionUID = 1L;
	
	private String extension;
	private String text;
	
	protected SaveTXT(FileTXT txt) {
		super(txt);
		this.extension = txt.getExtension();
		this.text = txt.getText();
	}

	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
