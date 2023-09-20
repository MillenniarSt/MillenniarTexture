package io.github.MillenniarSt.MillenniarTexture.Settings;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.MillenniarSt.MillenniarTexture.Console.Console;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;

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

public class Settings implements Serializable, ProgrammePath {

	@Serial
	private static final long serialVersionUID = 1L;
	private static HashMap<String, Object> settings = new HashMap<>();
	
	public static boolean verifyObject = true;
	public static boolean verifyTexturePack = true;
	
	public static final String MINECRAFT_DIRECTORY = "minecraftDir";
	public static final String RESOURCE = "resource";
	public static final String TEME = "colorTeme";
	public static final String DERIVATION = "derivations";
	public static final String CONSOLES = "consoles";
	
	@SuppressWarnings("unchecked")
	public static void load() {
		try {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Starting to load settings", ConsoleLine.INFO));
			ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(data + "settings.dat")));
			settings = (HashMap<String, Object>) (HashMap<?, ?>) input.readObject();
			try {
				input.close();
			} catch(IOException e) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to close ObjectInputStream - IOException in io.github.MillenniarSt.MillenniarTexture.Settings.load", ConsoleLine.WARN));
			}
			
			repair();
			
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "io.github.MillenniarSt.MillenniarTexture.Settings load successfully", ConsoleLine.SUCCESS));
		} catch(FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to found settings' file, settings will be default", ConsoleLine.WARN, exc));
			defaultSettings();
		} catch(IOException exc) { 
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to load setiings, settings will be default - IOException in io.github.MillenniarSt.MillenniarTexture.Settings.load", ConsoleLine.FATAL, exc));
			defaultSettings();
		} catch (ClassNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to load setiings, settings will be default - ClassNotFoundException in io.github.MillenniarSt.MillenniarTexture.Settings.load", ConsoleLine.FATAL, exc));
			defaultSettings();
		}
	}
	
	public static void save() {
		try {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Starting to save settings", ConsoleLine.INFO));
			File addDir = new File(data);
			if(!addDir.exists()) {			
				addDir.mkdirs();
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Create directory \"" + addDir.getPath() + "\"", ConsoleLine.INFO));
			}
			ObjectOutputStream texturesVectorSave = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(data + "settings.dat")));
			texturesVectorSave.writeObject(settings);
			try {
				texturesVectorSave.close();
			} catch(IOException e) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to close ObjectOutputStream", ConsoleLine.WARN));
			}
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "io.github.MillenniarSt.MillenniarTexture.Settings save successfully", ConsoleLine.SUCCESS));
		} catch(FileNotFoundException exc) { 
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to found settings' file - FileNotFoundException in io.github.MillenniarSt.MillenniarTexture.Settings.save", ConsoleLine.FATAL, exc));
		} catch(IOException exc) { 
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail save settings - IOException in io.github.MillenniarSt.MillenniarTexture.Settings.save", ConsoleLine.FATAL, exc));
		}
	}
	
	public static void defaultSettings() {
		settings.clear();
		settings.put(MINECRAFT_DIRECTORY, "C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/");
		settings.put(RESOURCE, "C:/Users/" + System.getProperty("user.name") + "/Downloads/resource-Mt1.3.0.zip");
		settings.put(TEME, "Modena-Dark");
		ArrayList<Console> consolesDef = new ArrayList<>();
		consolesDef.add(Main.exception);
		settings.put(CONSOLES, consolesDef);
		settings.put(DERIVATION, new ArrayList<Derivation>());
	}
	
	public static void repair() {
		if(settings.get(MINECRAFT_DIRECTORY) == null || !(settings.get(MINECRAFT_DIRECTORY) instanceof String)) {
			settings.put(MINECRAFT_DIRECTORY, "C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/");
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Set minecraft directory to \"" + settings.get(MINECRAFT_DIRECTORY) + "\" because it is null", ConsoleLine.WARN));
		}
		if(settings.get(RESOURCE) == null || !(settings.get(RESOURCE) instanceof String)) {
			settings.put(RESOURCE, "C:/Users/" + System.getProperty("user.name") + "/Downloads/resource-Mt1.3.0.zip");
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Set resource path to \"" + settings.get(RESOURCE) + "\" because it is null", ConsoleLine.WARN));
		}
		if(settings.get(TEME) == null || !(settings.get(TEME) instanceof String)) {
			settings.put(TEME, "Modena-Dark");
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Set teme to \"" + settings.get(TEME) + "\" because it is null", ConsoleLine.WARN));
		}
		if(settings.get(CONSOLES) == null || !(settings.get(CONSOLES) instanceof ArrayList)) {
			settings.put(CONSOLES, new ArrayList<Console>());
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Set consoles to \"" + settings.get(CONSOLES) + "\" because it is null", ConsoleLine.WARN));
		}
		if(settings.get(DERIVATION) == null || !(settings.get(DERIVATION) instanceof ArrayList)) {
			settings.put(DERIVATION, new ArrayList<Derivation>());
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Set derivations to \"" + settings.get(DERIVATION) + "\" because it is null", ConsoleLine.WARN));
		}
	}
	
	public static void setSetting(String key, Object value) {
		settings.put(key, value);
	}

	public static String getTheme() {
		return "resources/theme/" + Settings.getSetting(Settings.TEME) + ".css";
	}

	public static Object getSetting(String key) {
		return settings.get(key);
	}
}
