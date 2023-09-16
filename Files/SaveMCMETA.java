package Files;

import JavaObject.Animation;

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
