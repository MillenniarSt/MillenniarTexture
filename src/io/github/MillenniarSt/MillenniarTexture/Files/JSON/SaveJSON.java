package io.github.MillenniarSt.MillenniarTexture.Files.JSON;

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

import io.github.MillenniarSt.MillenniarTexture.Files.TXT.SaveTXT;

import java.io.Serial;

public class SaveJSON extends SaveTXT {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private SaveComponentJSON<?> rootTree;
	private byte idparentMd;
	
	SaveJSON(FileJSON json) {
		super(json);
		if(json.getRootTree() != null)
			this.rootTree = json.getRootTree().getSaved();
		if(json.getParentMd() != null)
			this.idparentMd = json.getParentMd().getId();
	}
	
	@Override
	public String toString() {
		return this.getClass()+"SaveTx:[type:"+getType()+",activated:"+isActivated()+",problems:"+getProblems()+",sources:"+getSources()+
				",id_parent:"+idparentMd+",root_tree:"+this.rootTree+"]"; 
	}

	public SaveComponentJSON<?> getRootTree() {
		return rootTree;
	}
	protected void setRootTree(SaveComponentJSON<?> rootTree) {
		this.rootTree = rootTree;
	}
	public byte getIdParentMd() {
		return idparentMd;
	}
	protected void setIdParentMd(byte idparentMd) {
		this.idparentMd = idparentMd;
	}
}

