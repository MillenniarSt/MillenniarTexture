package io.github.MillenniarSt.MillenniarTexture.Objects;

import java.io.File;

import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.DeleteFile;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.Blockstate;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.Model;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.Texture;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.TextureObject;

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

public class Block extends TextureObject implements ProgrammePath {
	
	private static final long serialVersionUID = 1L;
	private Blockstate blockstate;
	private Model item;
	
	public Block(String derivation, String name, Blockstate blockstate, Model item) {
		super(name, derivation);
		this.blockstate = blockstate;
		this.item = item;
	}

	public String toString() {
		String blockstateString = "";
		try {
			this.blockstate.build(Blockstate.readJsonFile(this.blockstate.getFile()));
			blockstateString = blockstate.getValue() + "\n" + blockstate + "\n";
		} catch (Exception exc) {
			blockstateString = " - fail to read \n";
		}
		String modelString = "";
		String textureString = "";
		for(Model model : blockstate.getModelsUD()) {
			try {
				model.build(Model.readJsonFile(model.getFile()));
				modelString = modelString + model.getPath() + "\n" + model + "\n";
				for(Texture texture : model.getTexturesUD()) {
					textureString = textureString + texture + "\n";
				}
			} catch (Exception exc) {
				modelString = modelString + " - fail to read \n";
			}
		}
		return  this.getName() + " - " + this.getDerivation() + "\n" +
				"Blockstate:" + "\n" +
				blockstateString + "\n" +
				"Models:" + "\n" +
				modelString +
				"Textures:" + "\n" +
				textureString;
	}
	
	public void delete() {
		File blockstateFile = this.getBlockstate().getFile();
		new DeleteFile(blockstateFile, true).execute(false);
		
		for(Model model : blockstate.getModelsUD()) {
			File modelFile = model.getFile();
			new DeleteFile(modelFile, true).execute(false);
			for(Texture texture : model.getTexturesUD()) {
				File textureFile = texture.getFile();
				if(textureFile.exists())
					new DeleteFile(textureFile, true).execute(false);
			}
		}
	}
	
	@Override
	public Block clone() {
		return new Block(this.getDerivation(), this.getName(), blockstate.clone(), item.clone());
	}
	
	public Blockstate getBlockstate() {
		return blockstate;
	}
	public void setBlockstate(Blockstate blockstate) {
		this.blockstate = blockstate;
	}
	public Model getItem() {
		return item;
	}
	public void setItem(Model item) {
		this.item = item;
	}
}
