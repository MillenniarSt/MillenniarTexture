package ManagmentFile;

import Console.ConsoleLine;
import Console.ConsoleStage;

import java.io.File;

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

public class DeleteFile extends ManagmentFile {

    private File file;
    private boolean withDir;

    private int progress;
    private int count;

    public DeleteFile(String file, boolean withDir) {
        super();
        this.file = new File(file);
        this.withDir = withDir;
    }
    public DeleteFile(File file, boolean withDir) {
        super();
        this.file = file;
        this.withDir = withDir;
    }

    @Override
    protected Integer[] call() throws Exception {
        count = 0;
        progress = 0;
        this.updateValue(voidProgress);
        if(file.exists()) {
            if(file.isDirectory()) {
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Deleting files into a directory \"" + file.getPath() + "\"...", ConsoleLine.DEBUG));
                updateMessage("Individualization");
                count = countFile(file);
                updateValue(new Integer[]{0, count});
                updateProgress(0, count);

                if(deleteDirectory(file) == 0) {
                    updateValue(new Integer[]{count, count});
                    updateProgress(count, count);
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete directory \"" + file.getPath() + "\" successfully", ConsoleLine.INFO));
                } else if(deleteDirectory(file) == 1) {
                    updateValue(new Integer[]{progress, count});
                    updateProgress(count, count);
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Stop deleting directory \"" + file.getPath() + "\" " + progress + " / " + count + " file deleted", ConsoleLine.INFO));
                    return new Integer[]{progress, count};
                } else {
                    updateValue(new Integer[]{count, count});
                    updateProgress(count, count);
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete directory \"" + file.getPath() + "\"", ConsoleLine.FATAL));
                    return voidProgress;
                }

            } else {
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Deleting file \"" + file.getPath() + "\"", ConsoleLine.DEBUG));
                this.updateMessage(file.getPath());
                count = 1;
                if(file.delete()) {
                    this.updateValue(new Integer[]{1, 1});
                    this.updateProgress(1, 1);
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete file \"" + file.getPath() + "\" successfully", ConsoleLine.INFO));
                } else {
                    this.updateProgress(0, 0);
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete file \"" + file.getPath() + "\"", ConsoleLine.ERROR));
                    return voidProgress;
                }
            }
            if(withDir) {
                boolean check = true;
                while(check) {
                    try {
                        File directory = new File(file.getPath().substring(0, file.getPath().lastIndexOf("\\")));
                        check = directory.delete();
                        if(check)
                            ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete directory \"" + directory.getPath() + "\"", ConsoleLine.DEBUG));
                    } catch(Exception exc) {
                        check = false;
                    }
                }
            }
        } else {
            ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to found file \"" + file.getPath() + "\"", ConsoleLine.WARN));
            return voidProgress;
        }
        return new Integer[]{count, count};
    }

    private int deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        for(File file : files) {
            if(!isStop()) {
                updateMessage(file.getPath());
                if (file.isDirectory()) {
                    int i = deleteDirectory(file);
                    if(i == 1)
                        return 1;
                    else if(i == 2)
                        return 2;
                } else {
                    if (file.delete())
                        ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Delete file \"" + file.getPath() + "\" successfully", ConsoleLine.DEBUG));
                    else
                        ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Fail to delete file \"" + file.getPath() + "\"", ConsoleLine.ERROR));
                }
                progress++;
                updateValue(new Integer[]{progress, count});
                updateProgress(progress, count);
            } else {
                return 1;
            }
        }
        updateMessage(directory.getPath());
        if(directory.delete()) {
            ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete directory \"" + directory.getPath() + "\" successfully", ConsoleLine.DEBUG));
        } else {
            ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete directory \"" + directory.getPath() + "\"", ConsoleLine.ERROR));
            return 2;
        }
        return 0;
    }

    @Override
    public boolean execute(boolean dialog) {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();

        if(dialog) {
            DialogProcess process = new DialogProcess(this, "Delete " + (file.isDirectory() ? "File" : "Directory") + "...");
            process.show();
        }

        return this.getValue() != voidProgress;
    }
}
