package ManagmentFile;

import Console.ConsoleLine;
import Console.ConsoleStage;

import java.io.*;
import java.net.URI;
import java.util.Deque;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileProcess extends ManagmentFile {

    private File location;
    private File destination;
    boolean replace;

    public ZipFileProcess(String location, String destination, boolean replace) {
        super();
        this.location = new File(location);
        this.destination = new File(destination);
        this.replace = replace;
    }
    public ZipFileProcess(File location, File destination, boolean replace) {
        super();
        this.location = location;
        this.destination = destination;
        this.replace = replace;
    }

    @Override
    protected Integer[] call() throws Exception {
        int count = 0;
        if(location.exists() && location.isDirectory()) {
            try {
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Starting to zip from \"" + location.getPath() + "\" to \"" + destination.getPath() + "\"...", ConsoleLine.DEBUG));
                updateMessage("Individualization");
                count = countFile(location);
                updateValue(new Integer[]{0, count});
                updateProgress(0, count);

                URI base = location.toURI();
                Deque<File> queue = new LinkedList<File>();
                queue.push(location);
                OutputStream out = new FileOutputStream(destination);
                Closeable res = out;
                ZipOutputStream zout = new ZipOutputStream(out);
                res = zout;

                int progress = 0;
                File directory;
                while (!queue.isEmpty()) {
                    directory = queue.pop();
                    for (File kid : directory.listFiles()) {
                        if(!isStop()) {
                            updateMessage(kid.getPath());
                            String name = base.relativize(kid.toURI()).getPath();
                            if (kid.isDirectory()) {
                                queue.push(kid);
                                name = name.endsWith("/") ? name : name + "/";
                                zout.putNextEntry(new ZipEntry(name));
                                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create directory \"" + name + "\" successfully", ConsoleLine.DEBUG));
                            } else {
                                zout.putNextEntry(new ZipEntry(name));
                                zipDirectoryInit(kid, zout);
                                zout.closeEntry();
                                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create file \"" + name + "\" successfully", ConsoleLine.DEBUG));
                            }
                            progress++;
                            updateValue(new Integer[]{progress, count});
                            updateProgress(progress, count);
                        } else {
                            res.close();
                            updateValue(new Integer[]{progress, count});
                            ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Stop zipping directory \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" " + progress + " / " + count + " file zipped", ConsoleLine.INFO));
                            return new Integer[]{count, count};
                        }
                    }
                }
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Zip file \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" successfully", ConsoleLine.SUCCESS));
                try {
                    if(res != null)
                        res.close();
                } catch (IOException exc) {
                    ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to close ZipInputStream - IOException in ManagmentFile.zip", ConsoleLine.WARN, exc));
                }
            } catch(IOException exc) {
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to zip directory \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" - IOException in ManagmentFile.zip", ConsoleLine.FATAL, exc));
                return voidProgress;
            } catch(Exception exc) {
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to zip directory \"" + location.getPath() + "\" to \"" + destination.getPath() + "\" - Exception in ManagmentFile.zip", ConsoleLine.FATAL, exc));
                return voidProgress;
            }
        } else {
            ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to found file \"" + location.getPath() + "\" or it is not a directory", ConsoleLine.WARN));
            return voidProgress;
        }
        updateValue(new Integer[]{count, count});
        updateProgress(count, count);
        return new Integer[]{count, count};
    }

    private static void zipDirectoryInit(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            zipDirectory(in, out);
        } finally {
            in.close();
        }
    }
    private static void zipDirectory(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
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
                ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "File \"" + destination.getPath() + "\" already exists, zip finished", ConsoleLine.ERROR));
                return false;
            }
        }

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();

        if(dialog) {
            DialogProcess process = new DialogProcess(this, "Zip Files...");
            process.show();
        }

        return this.getValue() != voidProgress;
    }
}
