package io.github.MillenniarSt.MillenniarTexture.Files.PNG;

import io.github.MillenniarSt.MillenniarTexture.Files.PNG.FilePNG;
import io.github.MillenniarSt.MillenniarTexture.Files.PNG.FilePNG.Pixel;
import io.github.MillenniarSt.MillenniarTexture.Files.SaveTx;

import java.io.Serial;

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

public class SavePNG extends SaveTx {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private Pixel[] [] pixels;
	
	SavePNG(FilePNG png) {
		super(png);
		this.pixels = png.getPixels();
	}
	
	public Pixel[][] getPixels() {
		return pixels;
	}
	public void setPixels(Pixel[][] pixels) {
		this.pixels = pixels;
	}
}
