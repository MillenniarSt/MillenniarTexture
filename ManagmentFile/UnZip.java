package ManagmentFile;

import Console.ConsoleLine;
import Console.ConsoleStage;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnZip extends ManagmentFile {

    private File location;
    private File destination;
    boolean replace;

    boolean isDerivation;

    private static final int BUFFER_SIZE = 4096;

    public UnZip(String location, String destination, boolean replace) {
        super();
        this.location = new File(location);
        this.destination = new File(destination);
        this.replace = replace;
    }
    public UnZip(File location, File destination, boolean replace) {
        super();
        this.location = location;
        this.destination = destination;
        this.replace = replace;
    }
    public UnZip(String location, String version) {
        super();
        this.location = new File(location);
        this.destination = new File(derivations + version);
        this.replace = true;
        this.isDerivation = true;
    }

    @Override
    protected Integer[] call() throws Exception {
        int count = 0;
        if (location.exists() && location.isFile()) {
            ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Unzip file \"" + location.getPath() + "\" to \"" + destination.getPath() + "\"...", ConsoleLine.DEBUG));
            ZipInputStream zis = null;
            try {
                FileInputStream fis = new FileInputStream(location);
                zis = new ZipInputStream(new BufferedInputStream(fis));
                ZipEntry entry;

                count = new ZipFile(location).size();
                int progress = 0;
                updateValue(new Integer[]{0, count});
                updateProgress(0, count);
                while((entry = zis.getNextEntry()) != null) {
                    updateMessage(destination.getPath() + entry.getName());
                    String path = destination.getPath() + entry.getName();
                    File check;
                    try {
                        check = new File(destination.getPath() + "/" + entry.getName().substring(0, entry.getName().lastIndexOf("/")));
                    } catch(StringIndexOutOfBoundsException exc) {
                        check = new File(destination.getPath() + "/");
                    }
                    if(!check.exists()) {
                        check.mkdirs();
                        ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create directory \"" + check.getPath() + "\" succefly", ConsoleLine.DEBUG));
                    }

                    if(!path.endsWith("/")) {
                        FileOutputStream fos = new FileOutputStream(path);
                        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);

                        int cont;
                        byte data[] = new byte[BUFFER_SIZE];
                        while((cont = zis.read(data, 0, BUFFER_SIZE)) != -1)
                            dest.write(data, 0, cont);
                        dest.flush();
                        dest.close();
                    }
                    progress++;
                    updateValue(new Integer[]{progress, count});
                    updateProgress(progress, count);
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create file \"" + path + "\" succefly", ConsoleLine.DEBUG));
                }
            } catch (FileNotFoundException exc) {
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to found zip file \"" + location.getPath() + "\" - FileNotFoundException in ManagmentFile.unzip", ConsoleLine.FATAL, exc));
            } catch (StringIndexOutOfBoundsException exc) {
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to unzip file \"" + location.getPath() + "\" - StringIndexOutOfBoundsException in ManagmentFile.unzip", ConsoleLine.FATAL, exc));
            } catch (IOException exc) {
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to unzip file \"" + location.getPath() + "\" - IOException in ManagmentFile.unzip", ConsoleLine.FATAL, exc));
            } finally {
                try {
                    if(zis != null)
                        zis.close();
                } catch (IOException exc) {
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to close ZipInputStream - IOException in ManagmentFile.unzip", ConsoleLine.WARN, exc));
                }
            }
            ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Unzip file \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" successfully", ConsoleLine.SUCCESS));
        } else {
            ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to found file \"" + location.getPath() + "\" or it is not a file", ConsoleLine.WARN));
            return voidProgress;
        }
        updateValue(new Integer[]{count, count});
        updateProgress(count, count);
        return new Integer[]{count, count};
    }

    @Override
    public boolean execute(boolean dialog) {
        if(destination.exists()) {
            if(replace) {
                DeleteFile deleteFile = new DeleteFile(destination, false);
                if(deleteFile.execute(dialog)) {
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete file \"" + destination.getPath() + "\"", ConsoleLine.DEBUG));
                } else {
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete file \"" + destination.getPath() + "\"", ConsoleLine.ERROR));
                    return false;
                }
            } else {
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "File \"" + destination.getPath() + "\" already exists, unzip finished", ConsoleLine.ERROR));
                return false;
            }
        }

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();

        if(dialog) {
            DialogProcess process = new DialogProcess(this, "Unzip Files...");
            process.show();
        }

        return this.getValue() != voidProgress;
    }
}
