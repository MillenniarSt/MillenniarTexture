package io.github.MillenniarSt.MillenniarTexture.CreateTexture;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.Files.SaveTx;
import io.github.MillenniarSt.MillenniarTexture.Objects.Component.TextureObject;
import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;
import io.github.MillenniarSt.MillenniarTexture.Menu.ManagmentStage;

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

public class Register implements ProgrammePath, Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<TextureObject> list;
	private HashMap<String, SaveTx> files;
	
	public Register(ArrayList<TextureObject> list, HashMap<String, SaveTx> files) {
		this.list = list;
		this.files = files;
	}

	@SuppressWarnings("unchecked")
	public static void loadTp() {
		try {
			ObjectInputStream texturesVector = new ObjectInputStream(new BufferedInputStream(new FileInputStream(data + "textures.dat")));
			TexturePack.textures = (ArrayList<TexturePack>) texturesVector.readObject();
			texturesVector.close();
		} catch(FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Texture pack's file \"" + data + "textures.dat" + "\" not found", ConsoleLine.WARN, exc));
		} catch(IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Fail to read texture pack's file - IOException in Register.reader", ConsoleLine.FATAL, exc));
		} catch (ClassNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Fail to read texture pack's file - ClassNotFoundException in Register.reader", ConsoleLine.FATAL, exc));
		}
	}
	
	public static Register load(TexturePack texturePack) {
		try {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Reading texture pack \"" + texturePack.getName() + "\" objects...", ConsoleLine.INFO));
			ObjectInputStream JavaObjectVector = new ObjectInputStream(new BufferedInputStream(new FileInputStream(data + "textureObjects/" + texturePack.getName() + ".dat")));
			Register output = (Register) JavaObjectVector.readObject();
			JavaObjectVector.close();
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Read texture pack \"" + texturePack.getName() + "\" objects successfully", ConsoleLine.INFO));
			return output;
		} catch(FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Texture pack \"" + texturePack.getName() + "\"'s file objects \"" + data + "textureObjects/" + texturePack.getName() + ".dat" + "\" not found", ConsoleLine.WARN, exc));
		} catch(ClassCastException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Fail to read texture pack \"" + texturePack.getName() + "\" objects - ClassCastException in TexturePackObject.reader", ConsoleLine.FATAL, exc));
		} catch(IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Fail to read texture pack \"" + texturePack.getName() + "\" objects - IOException in TexturePackObject.reader", ConsoleLine.ERROR, exc));
		} catch (ClassNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Fail to read texture pack \"" + texturePack.getName() + "\" objects - ClassNotFoundException in TexturePackObject.reader", ConsoleLine.FATAL, exc));
		}
		return new Register(new ArrayList<TextureObject>(), new HashMap<String, SaveTx>());
	}
	
	public static void save() {
		try {
			File addDir = new File(data);
			if(!addDir.exists()) {			
				addDir.mkdirs();
			}
			ObjectOutputStream texturesVectorSave = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(data + "textures.dat")));
			texturesVectorSave.writeObject(TexturePack.textures);
			texturesVectorSave.close();
		} catch(FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Texture pack's file \"" + data + "textures.dat" + "\" not found", ConsoleLine.ERROR, exc));
		} catch(IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Fail to read texture pack's file - IOException in Register.save", ConsoleLine.FATAL, exc));
		}
	}
	
	public void save(String name) {
		try {
			File addDir = new File(data + "textureObjects");
			if(!addDir.exists()) {
				addDir.mkdirs();
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Create directory \"" + addDir.getPath() + "\"", ConsoleLine.INFO));
			}
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Saving texture pack \"" + name + "\" objects...", ConsoleLine.INFO));
			ObjectOutputStream JavaObjectVectorSave = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(data + "textureObjects/" + name + ".dat")));
			JavaObjectVectorSave.writeObject(this);
			JavaObjectVectorSave.close();
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Save texture pack \"" + name + "\" objects successfully", ConsoleLine.SUCCESS));
		} catch(FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Fail to save texture pack \"" + name + "\" objects because file \"" + data + "textureObjects/" + name + ".dat" + "\" not found", ConsoleLine.FATAL, exc));
		} catch(IOException exc) { 
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("REGISTER", "Fail to save texture pack \"" + name + "\" objects - IOException in TexturePackObject.save", ConsoleLine.ERROR, exc));
		}
	}

	public ArrayList<TextureObject> getList() {
		return list;
	}
	public void putFile(String key, SaveTx file) {
		files.put(key, file);
		this.save(ManagmentStage.getOn().getTexturePack().getName());
	}
	public HashMap<String, SaveTx> getFiles() {
		return files;
	}
}
