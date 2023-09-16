package Console;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import Main.ProgrammePath;
import Settings.Settings;

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

public class Console implements ProgrammePath, Serializable {

	private static final long serialVersionUID = 2L;
	private static HashMap<String, ConsoleStage> consoleStages = new HashMap<>();
	
	private String title;
	private LocalDateTime lastTime;
	private ArrayList<ConsoleLine> register;
	
	public final String id;
	
	private boolean mesDebug;
	private boolean mesSuccess;
	private boolean mesInfo;
	private boolean mesWarn;
	private boolean mesError;
	private boolean mesFatal;
	private boolean mesException;
	
	@SuppressWarnings("unchecked")
	public Console(String title, LocalDateTime lastTime, ArrayList<ConsoleLine> register, String id) {
		this.id = id;
		
		this.title = title;
		this.lastTime = lastTime;
		this.register = register;

		this.mesDebug = false;
		this.mesSuccess = true;
		this.mesInfo = true;
		this.mesWarn = true;
		this.mesError = true;
		this.mesFatal = true;
		this.mesException = false;
		
		if(Settings.getSetting(Settings.CONSOLES) != null)
			((ArrayList<Console>) Settings.getSetting(Settings.CONSOLES)).add(this);
	}
	
	@SuppressWarnings("unchecked")
	public Console(String title, LocalDateTime lastTime, ArrayList<ConsoleLine> register) {
		this.id = UUID.randomUUID().toString();
		
		this.title = title;
		this.lastTime = lastTime;
		this.register = register;

		this.mesDebug = false;
		this.mesSuccess = true;
		this.mesInfo = true;
		this.mesWarn = true;
		this.mesError = true;
		this.mesFatal = true;
		this.mesException = false;
		
		if(Settings.getSetting(Settings.CONSOLES) != null)
			((ArrayList<Console>) Settings.getSetting(Settings.CONSOLES)).add(this);
	}
	
	public static Console load(String path) {
		Console console = null;
		try {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Starting to load console", ConsoleLine.INFO));
			ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
			console = (Console) input.readObject();
			try {
				input.close();
			} catch(IOException e) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to close ObjectInputStream", ConsoleLine.WARN));
			}
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Console load succefly", ConsoleLine.SUCCESS));
		} catch(FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to found console file", ConsoleLine.WARN, exc));
		} catch(IOException exc) { 
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to load console - IOException in Console.load", ConsoleLine.FATAL, exc));
		} catch (ClassNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to load console - ClassNotFoundException in Console.load", ConsoleLine.FATAL, exc));
		}
		return console;
	}
	
	public static void save(String path, Console console) {
		try {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Starting to save console", ConsoleLine.INFO));
			File addDir = new File(path.substring(0, path.lastIndexOf("\\")));
			if(!addDir.exists()) {			
				addDir.mkdirs();
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Create directory \"" + addDir.getPath() + "\"", ConsoleLine.INFO));
			}
			ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
			output.writeObject(console);
			try {
				output.close();
			} catch(IOException e) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to close ObjectOutputStream", ConsoleLine.WARN));
			}
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Console save succefly", ConsoleLine.SUCCESS));
		} catch(FileNotFoundException exc) { 
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail to found console file - FileNotFoundException in Console.save", ConsoleLine.FATAL, exc));
		} catch(NotSerializableException exc) {
			System.out.println("non serirializable");
			exc.printStackTrace();
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail save console - NotSerializableException in Console.save", ConsoleLine.FATAL, exc));
		} catch(IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTING", "Fail save console - IOException in Console.save", ConsoleLine.FATAL, exc));
		}
	}
	
	public boolean equals(Console cns) {
		if(this.id.equals(cns.id))
			return true;
		else 
			return false;
	}
	
	public void addConsoleStage(ConsoleStage stageCns) {
		consoleStages.put(this.id, stageCns);
	}
	
	public ConsoleStage getConsoleStage() {
		ConsoleStage output = consoleStages.get(this.id);
		if(output != null)
			return output;
		else
			return null;
	}
	
	public void copyOpt(Console cns) {
		this.title = cns.title;
		this.mesDebug = cns.mesDebug;
		this.mesSuccess = cns.mesSuccess;
		this.mesInfo = cns.mesInfo;
		this.mesWarn = cns.mesWarn;
		this.mesError = cns.mesError;
		this.mesFatal = cns.mesFatal;
	}
	
	@Override
	public String toString() {
		return this.getTitle();
	}

	public LocalDateTime getLastTime() {
		return lastTime;
	}

	public void setLastTime(LocalDateTime lastTime) {
		this.lastTime = lastTime;
	}

	public ArrayList<ConsoleLine> getRegister() {
		return register;
	}

	public void setRegister(ArrayList<ConsoleLine> register) {
		this.register = register;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isMesSuccess() {
		return mesSuccess;
	}

	public void setMesSuccess(boolean mesSuccess) {
		this.mesSuccess = mesSuccess;
	}

	public boolean isMesInfo() {
		return mesInfo;
	}

	public void setMesInfo(boolean mesInfo) {
		this.mesInfo = mesInfo;
	}

	public boolean isMesWarn() {
		return mesWarn;
	}

	public void setMesWarn(boolean mesWarn) {
		this.mesWarn = mesWarn;
	}

	public boolean isMesError() {
		return mesError;
	}

	public void setMesError(boolean mesError) {
		this.mesError = mesError;
	}

	public boolean isMesFatal() {
		return mesFatal;
	}

	public void setMesFatal(boolean mesFatal) {
		this.mesFatal = mesFatal;
	}

	public boolean isMesDebug() {
		return mesDebug;
	}

	public void setMesDebug(boolean mesDebug) {
		this.mesDebug = mesDebug;
	}

	public boolean isMesException() {
		return mesException;
	}

	public void setMesException(boolean mesException) {
		this.mesException = mesException;
	}
}
