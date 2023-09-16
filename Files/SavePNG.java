package Files;

import Files.FilePNG.Pixel;

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
