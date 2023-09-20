package io.github.MillenniarSt.MillenniarTexture.Main;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.Iterator;

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

public class ManagmentFile implements Path, ProgrammePath {

	/*public static boolean copy(String location, String destination) {
		if(new File(location).exists()) {
			if(!new File(destination).exists()) {
				File check = new File(destination.substring(0, destination.lastIndexOf("\\")));
				if(!check.exists()) {
					check.mkdirs();
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Create directory \"" + check.getPath() + "\"", ConsoleLine.INFO));
				}
				Path locationPath = FileSystems.getDefault().getPath(location);
				Path destinationPath = FileSystems.getDefault().getPath(destination);
				try {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Copying file \"" + location + "\" to \"" + destination + "\"", ConsoleLine.INFO));
					io.github.MillenniarSt.MillenniarTexture.Files.copy(locationPath, destinationPath);
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Copy file \"" + location + "\" to \"" + destination + "\" successfully", ConsoleLine.SUCCESS));
					return true;
				} catch (NoSuchFileException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to copy file \"" + location + "\" to \"" + destination + "\" - NoSuchFileException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.copy", ConsoleLine.FATAL, exc));
					return false;
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to copy file \"" + location + "\" to \"" + destination + "\" - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.copy", ConsoleLine.ERROR, exc));
					return false;
				}
			} else {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "File \"" + destination + "\" already exists, copy finished", ConsoleLine.WARN));
				return false;
			}
		} else {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to found file \"" + location + "\"", ConsoleLine.ERROR));
			return false;
		}
	}
	public static boolean copyReplace(String location, String destination) {
		if(new File(location).exists()) {
			if(new File(destination).exists()) {
				if(new File(destination).delete())
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete file \"" + destination + "\"", ConsoleLine.INFO));
				else
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete file \"" + destination + "\"", ConsoleLine.WARN));
			}
			File check = new File(destination.substring(0, destination.lastIndexOf("\\")));
			if(!check.exists()) {
				check.mkdirs();
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Create directory \"" + check.getPath() + "\"", ConsoleLine.INFO));
			}
			Path locationPath = FileSystems.getDefault().getPath(location);
			Path destinationPath = FileSystems.getDefault().getPath(destination);
			try {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Copying file \"" + location + "\" to \"" + destination + "\"", ConsoleLine.INFO));
				io.github.MillenniarSt.MillenniarTexture.Files.copy(locationPath, destinationPath);
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Copy file \"" + location + "\" to \"" + destination + "\" successfully", ConsoleLine.SUCCESS));
				return true;
			} catch (NoSuchFileException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to copy file \"" + location + "\" to \"" + destination + "\" - NoSuchFileException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.copy", ConsoleLine.FATAL, exc));
				return false;
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to copy file \"" + location + "\" to \"" + destination + "\" - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.copy", ConsoleLine.ERROR, exc));
				return false;
			}
		} else {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to found file \"" + location + "\"", ConsoleLine.ERROR));
			return false;
		}
	}
	
	public static boolean copyDir(String location, String destination) {
		if(new File(location).exists()) {
			File check = new File(destination.substring(0, destination.lastIndexOf("\\")));
			if(!check.exists()) {
				check.mkdirs();
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Create directory \"" + check.getPath() + "\"", ConsoleLine.INFO));
			}
			
			Path locationPath = FileSystems.getDefault().getPath(location);
			Path destinationPath = FileSystems.getDefault().getPath(destination);
			
			try {
			    List<Path> sources = io.github.MillenniarSt.MillenniarTexture.Files.walk(locationPath).collect(Collectors.toList());
			    for (Path source: sources) {
			        io.github.MillenniarSt.MillenniarTexture.Files.copy(source, destinationPath.resolve(locationPath.relativize(source)), StandardCopyOption.REPLACE_EXISTING);
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "No new directory founds in \"" + source + "\"", ConsoleLine.DEBUG));
			    }
			    return true;
			} catch (NoSuchFileException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to copy directory \"" + location + "\" to \"" + destination + "\" - NoSuchFileException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.copy", ConsoleLine.FATAL, exc));
				return false;
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to copy directory \"" + location + "\" to \"" + destination + "\" - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.copy", ConsoleLine.ERROR, exc));
				return false;
			}
		} else {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to found directory \"" + location + "\"", ConsoleLine.ERROR));
			return false;
		}
	}
	
	public static boolean deleteDirectory(File path) {
		if(path.exists() && path.isDirectory()) {
			String deleted = path.getPath();
			File[] files = path.listFiles();
			try {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Deleteing files into a directory \"" + path.getPath() + "\"...", ConsoleLine.INFO));
				for(int i = 0; i < files.length; i++) {
					deleted = files[i].getPath();
					if(files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						if(files[i].delete())
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Delete file \"" + deleted + "\" successfully", ConsoleLine.DEBUG));
						else
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Fail to delete file \"" + deleted + "\"", ConsoleLine.ERROR));
					}
				}
			} catch(NullPointerException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete directory \"" + deleted + "\" - NullPointerException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.delete.directory", ConsoleLine.FATAL, exc));
			}
			deleted = path.getPath();
			if(path.delete())
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete directory \"" + deleted + "\" successfully", ConsoleLine.SUCCESS));
			else {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete directory \"" + deleted + "\"", ConsoleLine.FATAL));
				return false;
			}
		} else {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Directory \"" + path.getPath() + "\" not found or is not a directory", ConsoleLine.ERROR));
			return false;
		}
		return true;
	}
	
	public static void deleteWhitDir(File path) {
		String deleted = path.getPath();
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Deleteing file \"" + deleted + "\" and its directory empity...", ConsoleLine.INFO));
		if(path.delete()) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete file \"" + deleted + "\" successfully", ConsoleLine.INFO));
			boolean check = true;
			while(check) {
				try {
					File directory = new File(path.getPath().substring(0, path.getPath().lastIndexOf("\\")));
					check = directory.delete();
				} catch(Exception exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete directory \"" + deleted + "\" successfully", ConsoleLine.INFO, exc));
					check = false;
				}
				if(check)
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete directory \"" + deleted + "\" successfully", ConsoleLine.INFO));
			}
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete file \"" + deleted + "\" and its directory empity successfully", ConsoleLine.SUCCESS));
		} else
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete file \"" + deleted + "\"", ConsoleLine.ERROR));
	}
	
	private static final int BUFFER_SIZE = 4096;
	
	public static boolean unzip(String zipPath, String destination) {
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Unzip file \"" + zipPath + "\" to \"" + destination + "\"...", ConsoleLine.INFO));
		  boolean output = false;
		  ZipInputStream zis = null;
		  
	      try {
	         FileInputStream fis = new FileInputStream(zipPath);
	         zis = new ZipInputStream(new BufferedInputStream(fis));
	         ZipEntry entry;

	         while((entry = zis.getNextEntry()) != null) {
	            String path = destination + entry.getName();
	            File check;
	            try {
	            	check = new File(destination + "/" + entry.getName().substring(0, entry.getName().lastIndexOf("/")));
	            } catch(StringIndexOutOfBoundsException exc) {
	            	check = new File(destination + "/");
	            }
	           	if(!check.exists()) {
	           		check.mkdirs();
	           		ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create directory \"" + check.getPath() + "\" successfully", ConsoleLine.DEBUG));
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
	           	ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create file \"" + path + "\" successfully", ConsoleLine.DEBUG));
	            }
	         output = true;
	      } catch (FileNotFoundException exc) {
	    	  ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to found zip file \"" + zipPath + "\" - FileNotFoundException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.unzip", ConsoleLine.FATAL, exc));
	      } catch (StringIndexOutOfBoundsException exc) {
	    	  ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to unzip file \"" + zipPath + "\" - StringIndexOutOfBoundsException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.unzip", ConsoleLine.FATAL, exc));
	      } catch (IOException exc) {
	    	  ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to unzip file \"" + zipPath + "\" - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.unzip", ConsoleLine.FATAL, exc));
	      } finally {
	         try {
	            if(zis != null)
	               zis.close();
	         } catch (IOException exc) {
	        	 ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to close ZipInputStream - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.unzip", ConsoleLine.WARN, exc));
	         }
	      }
	      ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Unzip file \"" + zipPath + "\" to \"" + destination + "\" successfully", ConsoleLine.SUCCESS));
	      return output;
	    }
	
	public static boolean unzipDer(String zipPath, String version) {
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Unzip file .JAR \"" + zipPath + "\" to \"" + derivations + "\"...", ConsoleLine.INFO));
		ZipInputStream zis = null;
		  
	    try {
	    	FileInputStream fis = new FileInputStream(zipPath);
	        zis = new ZipInputStream(new BufferedInputStream(fis));
	        ZipEntry entry;

	        while((entry = zis.getNextEntry()) != null) {
	            String path = derivations + version + "/" + entry.getName();
	            
         		try {
		            if(entry.getName().substring(0, entry.getName().indexOf("/")).equals("assets")) {
		            	path = derivations + version + "/" + entry.getName().substring(7);
		            	
		            	File check = new File(path.substring(0, path.lastIndexOf("/")));
		            	if(!check.exists()) {
		            		check.mkdirs();
		            		ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create directory \"" + check.getPath() + "\" successfully", ConsoleLine.DEBUG));
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
		             		ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create file \"" + path + "\" successfully", ConsoleLine.DEBUG));
		    	        }
		            }
         		} catch(StringIndexOutOfBoundsException exc) { }
	         }
	         
	         ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Unzip file \"" + zipPath + "\" to derivations successfully", ConsoleLine.SUCCESS));
	      } catch (FileNotFoundException exc) {
	    	  ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "File .JAR \"" + zipPath + "\" not found", ConsoleLine.ERROR, exc));
	    	  return false;
	      } catch (IOException exc) {
	    	  ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to unzip file .JAR \"" + zipPath + "\" - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.unzipDerivation", ConsoleLine.FATAL, exc));
	    	  return false;
	      } finally {
	         try {
	            if(zis != null)
	               zis.close();
	         } catch (IOException exc) {
	        	 ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to close ZipInputStream - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.unzipDerivation", ConsoleLine.WARN, exc));
	         }
	      }
	      return true;
	    }
	
	@SuppressWarnings("resource")
	public static void zip(File directory, File zipfile) {
		try {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Starting to zip directory \"" + directory + "\" to \"" + zipfile + "\"...", ConsoleLine.INFO));
			URI base = directory.toURI();
			Deque<File> queue = new LinkedList<File>();
			queue.push(directory);
			OutputStream out = new FileOutputStream(zipfile);
			Closeable res = out;
			ZipOutputStream zout = new ZipOutputStream(out);
			res = zout;
			while (!queue.isEmpty()) {
				directory = queue.pop();
				for (File kid : directory.listFiles()) {
					String name = base.relativize(kid.toURI()).getPath();
					if (kid.isDirectory()) {
						queue.push(kid);
						name = name.endsWith("/") ? name : name + "/";
						zout.putNextEntry(new ZipEntry(name));
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create directory \"" + name + "\" successfully", ConsoleLine.DEBUG));
					} else {
						zout.putNextEntry(new ZipEntry(name));
						copyByDirectory(kid, zout);
						zout.closeEntry();
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", " - Create file \"" + name + "\" successfully", ConsoleLine.DEBUG));
					}
				}
			}
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Zip file \"" + directory + "\" to \"" + zipfile + "\" successfully", ConsoleLine.SUCCESS));
			try {
	            if(res != null)
	               res.close();
	         } catch (IOException exc) {
	        	 ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to close ZipInputStream - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.unzipDerivation", ConsoleLine.WARN, exc));
	         }
		} catch(IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to zip directory \"" + directory + "\" to \"" + zipfile + "\" - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.unzipDerivation", ConsoleLine.FATAL, exc));
		} catch(Exception exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to zip directory \"" + directory + "\" to \"" + zipfile + "\" - Exception in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.unzipDerivation", ConsoleLine.FATAL, exc));
		}
	}
	
	private static void copyByDirectory(File file, OutputStream out) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			copyByDirectory(in, out);
		} finally {
			in.close();
		}
	}
	
	public static void extractByZip(String ziFile, String destination) {
		  ZipInputStream zis = null;
		  
	      try {
	    	  FileInputStream fis = new FileInputStream(ziFile);
	    	  zis = new ZipInputStream(new BufferedInputStream(fis));
	         
	    	  FileOutputStream fos = new FileOutputStream(destination);
	    	  BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);
      	
	    	  int cont;
	    	  byte data[] = new byte[BUFFER_SIZE];
	    	  while((cont = zis.read(data, 0, BUFFER_SIZE)) != -1)
	    		 dest.write(data, 0, cont);

	    	  dest.flush();
	    	  dest.close();
	      } catch (FileNotFoundException exc) {
	      } catch (StringIndexOutOfBoundsException exc) {
	      } catch (IOException exc) {
	    	  ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to extract file \"" + ziFile + "\" - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.extrctByZip", ConsoleLine.FATAL, exc));
	      } finally {
	         try {
	            if(zis != null)
	               zis.close();
	         } catch (IOException exc) {
	        	 ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to close ZipInputStream - IOException in io.github.MillenniarSt.MillenniarTexture.ManagmentFile.extrctByZip", ConsoleLine.WARN, exc));
	         }
	      }
	}

	private static void copyByDirectory(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = in.read(buffer);
			if (readCount < 0) {
				break;
			}
				out.write(buffer, 0, readCount);
		}
	}*/

	@Override
	public File toFile() {
		return null;
	}
	@Override
	public boolean endsWith(String s) {
		return false;
	}
	@Override
	public Iterator<Path> iterator() {
		return null;
	}
	@Override
	public Path resolve(String other) {
		return null;
	}
	@Override
	public Path resolveSibling(Path other) {
		return null;
	}
	@Override
	public Path resolveSibling(String other) {
		return null;
	}
	@Override
	public boolean startsWith(String other) {
		return false;
	}
	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) {
		  return null;
	}
	@Override
	public FileSystem getFileSystem() {
		return null;
	}
	@Override
	public boolean isAbsolute() {
		return false;
	}

	@Override
	public Path getRoot() {
		return null;
	}

	@Override
	public Path getFileName() {
		return null;
	}

	@Override
	public Path getParent() {
		return null;
	}

	@Override
	public int getNameCount() {
		return 0;
	}

	@Override
	public Path getName(int index) {
		return null;
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		return null;
	}

	@Override
	public boolean startsWith(Path other) {
		return false;
	}

	@Override
	public boolean endsWith(Path other) {
		return false;
	}

	@Override
	public Path normalize() {
		return null;
	}

	@Override
	public Path resolve(Path other) {
		return null;
	}

	@Override
	public Path relativize(Path other) {
		return null;
	}

	@Override
	public URI toUri() {
		return null;
	}

	@Override
	public Path toAbsolutePath() {
		return null;
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		return null;
	}

	@Override
	public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
		return null;
	}

	@Override
	public int compareTo(Path other) {
		return 0;
	}
}
