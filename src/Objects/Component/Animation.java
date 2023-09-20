package io.github.MillenniarSt.MillenniarTexture.Objects.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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

public class Animation extends Component implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final byte id = 0;
	
	private int frameTime;
	private boolean interpolate;
	private HashMap<String, Object> other;
	
	public Animation(File file, String name, int frameTime, boolean interpolate, HashMap<String, Object> other) {
		super(file, name);
		this.frameTime = frameTime;
		this.interpolate = interpolate;
		this.other = other;
	}
	
	public Animation(File file, String name) {
		super(file, name);
	}
	
	public static ArrayList<Object> readJsonFile(File mcmeta) throws FileNotFoundException, IOException {
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("ANIMATION", "Starting to read animation file \"" + mcmeta.getPath() + "\"...",
				ConsoleLine.INFO));
		ArrayList<Object> output = new ArrayList<>();
		char[] noReadChar = {' ', '\n'};
		char[] specialReadChar = {':', ',', '{', '}', '[', ']'};
		char[] number = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		FileReader reader = new FileReader(mcmeta);
			
		int nextInt = reader.read();
		char next;
		while(nextInt != -1) {
			next = (char) nextInt;
			if(containChar(specialReadChar, next)) {
				output.add(next);
				nextInt = reader.read();
			} else if(containChar(number, next)) {
				String ne = "" + next;
				nextInt = reader.read();
				while(containChar(number, (char) nextInt) && nextInt != -1) {
					ne = ne + (char) nextInt;
					nextInt = reader.read();
				}
				output.add(Integer.parseInt(ne));
			} else if(next == '"') {
				String ne = "";
				do {
					next = (char) reader.read();
					if(!containChar(noReadChar, next) && next != '"' && nextInt != -1) ne = ne + next;
				} while(next != '"' && nextInt != -1);
				output.add(ne);
				nextInt = reader.read();
			} else if(next == 't') {
				if((char) reader.read() == 'r')
					if((char) reader.read() == 'u')
						if((char) reader.read() == 'e')
							output.add((Boolean) true);
				nextInt = reader.read();
			} else if(next == 'f') {
				if((char) reader.read() == 'a')
					if((char) reader.read() == 'l')
						if((char) reader.read() == 's')
							if((char) reader.read() == 'e')
								output.add((Boolean) false);
				nextInt = reader.read();
			} else {
				nextInt = reader.read();
			}
		}
		reader.close();
		return output;
	}
	
	public static boolean containChar(char[] array, char i) {
		for(char j : array) {
			if(i == j) return true;
		}
		return false;
	}
	
	@Override
	public boolean build(ArrayList<Object> elements) {
		int index = 0;
		if(((char) elements.get(index)) == '{' && ((char) elements.get(elements.size() - 1)) == '}') {
			index++;
			try {
				do {
					if(((String) elements.get(index)).equals("animation") && ((char) elements.get(index + 1)) == ':' && ((char) elements.get(index + 2)) == '{') {
						index = index + 3;
						do {
							if(((char) elements.get(index + 1)) == ':') {
								if(((String) elements.get(index)).equals("frametime")) {
									this.frameTime = (Integer) elements.get(index + 2);
									index = index + 4;
								} else if(((String) elements.get(index)).equals("interpolate")) {
									this.interpolate = (Boolean) elements.get(index + 2);
									index = index + 4;
								}
							}
						} while(((char) elements.get(index - 1)) == ',');
						if(!(((char) elements.get(index - 1)) == '}')) {
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("ANIMATION", "Fail to build animation properties - Insert '}' to complete", ConsoleLine.ERROR));
	                        throw new ArrayIndexOutOfBoundsException();
	                    }	
					} else {
						if(this.other == null)
							this.other = new HashMap<String, Object>();
						this.other.put((String) elements.get(index), elements.get(index + 2));
						index = index + 4;
					}
				} while(((char) elements.get(index - 1)) == ',');
				
			} catch(ArrayIndexOutOfBoundsException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("ANIMATION", "Fail to build animation - ArrayIndexOutOfBoundsException in animation.read", ConsoleLine.FATAL, exc));
				return false;
			} catch(ClassCastException exc) {
	        	ConsoleStage.defaultCns.printConsole(new ConsoleLine("ANIMATION", "Fail to build animation - ClassCastException in animation.read", ConsoleLine.FATAL, exc));
	        	return false;
	        }
		} else {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("ANIMATION", "Fail to build animation - it is not a animation", ConsoleLine.FATAL));
			return false;
		}
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("ANIMATION", "Build animation successfully", ConsoleLine.SUCCESS));
		return true;
	}
	
	public String toString() {
		String output = "{";
		if(this.other != null) {
			String otherList = "";
			for(String other : this.other.keySet()) {
				if(!otherList.equals(""))
					otherList = otherList + ",";
				if(this.other.get(other) instanceof String)
					otherList = otherList + "\n    \"" + other + "\": \"" + this.other.get(other) + "\"";
				else
					otherList = otherList + "\n    \"" + other + "\": " + this.other.get(other);
			}
			output = output + otherList;
		}
		
		output = output + "\n    \"animation\": {";
		
		output = output + "\n        \"interpolate\": " + this.interpolate;
		
		if(this.frameTime != 0) {
			if(!output.equals("{"))
				output = output + ",";
			output = output + "\n        \"frametime\": " + this.frameTime;
		}
		output = output + "\n    }";
		
		return output + "\n}";
	}

	public HashMap<String, Object> getOther() {
		return other;
	}

	public void setOther(HashMap<String, Object> other) {
		this.other = other;
	}

	public boolean isInterpolate() {
		return interpolate;
	}

	public void setInterpolate(boolean interpolate) {
		this.interpolate = interpolate;
	}

	public int getFrameTime() {
		return frameTime;
	}

	public void setFrameTime(int frameTime) {
		this.frameTime = frameTime;
	}
}
