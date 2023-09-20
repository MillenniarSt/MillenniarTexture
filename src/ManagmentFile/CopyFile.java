package io.github.MillenniarSt.MillenniarTexture.ManagmentFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;

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
public class CopyFile extends ManagmentFile {

	private File location;
	private File destination;
	private boolean replace;

	private boolean dialog;

	public CopyFile(String location, String destination, boolean replace) {
		super();
		this.location = new File(location);
		this.destination = new File(destination);
		this.replace = replace;
	}
	public CopyFile(File location, File destination, boolean replace) {
		super();
		this.location = location;
		this.destination = destination;
		this.replace = replace;
	}

	@Override
	protected Integer[] call() throws Exception {
		int count = 0;
		this.updateValue(voidProgress);
		
		if(location.exists()) {
			File check = new File(destination.getPath().substring(0, destination.getPath().lastIndexOf("\\")));
			if(!check.exists()) {
				check.mkdirs();
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Create directory \"" + check.getPath() + "\"", ConsoleLine.DEBUG));
			}
			
			try {
				Path locationPath = FileSystems.getDefault().getPath(location.getPath());
				Path destinationPath = FileSystems.getDefault().getPath(destination.getPath());
				if(location.isDirectory()) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Copying directory \"" + location.getPath() + "\" to \"" + destination.getPath() + "\"", ConsoleLine.DEBUG));
					 List<Path> sources = Files.walk(locationPath).collect(Collectors.toList());
					 count = sources.size();
					 for (Path source: sources) {
						 this.updateProgress(sources.indexOf(source), count);
						 this.updateMessage(source.toString());
						 this.updateValue(new Integer[]{sources.indexOf(source), count});
						 if(!isStop()) {
							 Files.copy(source, destinationPath.resolve(locationPath.relativize(source)), StandardCopyOption.REPLACE_EXISTING);
							 ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Copy file \"" + source + "\"", ConsoleLine.DEBUG));
						 } else {
							 ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Stop copying directory \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" " + sources.indexOf(source) + " / " + count + " file copied", ConsoleLine.INFO));
							 return new Integer[]{sources.indexOf(source), count};
						 }
					 }
					 this.updateProgress(count, count);
					 ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Copy directory \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" successfully: " + this.getValue()[0] + " / " + this.getValue()[1] + " file copied", ConsoleLine.INFO));
				} else {
					count = 1;
					this.updateProgress(0, 1);
					this.updateMessage(locationPath.toString());
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Copying file \"" + location.getPath() + "\" to \"" + destination.getPath() + "\"", ConsoleLine.DEBUG));
					Files.copy(locationPath, destinationPath);
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Copy file \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" successfully", ConsoleLine.INFO));
					this.updateProgress(1, 1);
					this.updateValue(new Integer[]{1, 1});
				}
			} catch (NoSuchFileException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to copy file \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" - NoSuchFileException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.copy", ConsoleLine.ERROR, exc));
				this.updateValue(voidProgress);
				return voidProgress;
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to copy file \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.copy", ConsoleLine.ERROR, exc));
				this.updateValue(voidProgress);
				return voidProgress;
			}
		} else {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to found file \"" + location.getPath() + "\"", ConsoleLine.ERROR));
			this.updateValue(voidProgress);
			return voidProgress;
		}
		return new Integer[]{count, count};
	}

	@Override
	public void execute(boolean dialog) {
		if(destination.exists()) {
			if(replace) {
				DeleteFile deleteFile = new DeleteFile(destination, false);
				deleteFile.execute(dialog);
			} else {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "File \"" + destination.getPath() + "\" already exists, copy finished", ConsoleLine.ERROR));
			}
		}

		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();

		this.dialog = dialog;
		if(dialog) {
			DialogProcess process = new DialogProcess(this, "Copy " + (location.isDirectory() ? "File" : "Directory") + "...");
			process.show();
		}
	}
}
