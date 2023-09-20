package io.github.MillenniarSt.MillenniarTexture.Files.MCMETA;

import io.github.MillenniarSt.MillenniarTexture.Files.TXT.SaveTXT;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.Animation;

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

public class SaveMCMETA extends SaveTXT {
	private static final long serialVersionUID = 1L;
	
	private Animation animation;
	
	SaveMCMETA(FileMCMETA mcmeta) {
		super(mcmeta);
		this.animation = mcmeta.getAnimation();
	}

	public Animation getAnimation() {
		return animation;
	}
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
}
