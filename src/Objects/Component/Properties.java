package io.github.MillenniarSt.MillenniarTexture.Objects.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;

public class Properties extends Component {

	private static final long serialVersionUID = 1L;
	public static final byte id = 3;

	private HashMap<String, ArrayList<String>> properties;
	
	public Properties(File file, String name) {
		super(file, name);
		this.properties = new HashMap<>();
	}
	
	public Properties(File file, String name, HashMap<String, ArrayList<String>> properties) {
		super(file, name);
		this.properties = properties;
	}
	
	public static ArrayList<Object> readPropertiesFile(File properties) throws FileNotFoundException, IOException {
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("PROPERTIES", "Starting to read properties file \"" + properties.getPath() + "\"...",
				ConsoleLine.INFO));
		ArrayList<Object> output = new ArrayList<>();
		FileReader reader = new FileReader(properties);
			
		int nextInt = reader.read();
		char next;
		while(nextInt != -1) {
			next = (char) nextInt;
			if(next == '=' || nextInt == 13 || nextInt == 10) {
				output.add(next);
				nextInt = reader.read();
			} else if(next != ' ') {
				String ne = "";
				do {
					if(next != ' ' && next != '=' && nextInt != 13 && nextInt != 10 && nextInt != -1)
						ne = ne + next;
					nextInt = reader.read();
					next = (char) nextInt;
				} while(next != ' ' && next != '=' && nextInt != 13 && nextInt != 10 && nextInt != -1);
				output.add(ne);
			} else {
				nextInt = reader.read();
			}
		}
		reader.close();
		return output;
	}
	
	@Override
	public boolean build(ArrayList<Object> elements) {
		int index = 0;
		try {
			this.properties.clear();
			do {
				if(elements.get(index) instanceof String && ((char) elements.get(index + 1)) == '=') {
					String key = (String) elements.get(index);
					this.properties.put(key, new ArrayList<String>());
					index = index + 2;
					while(elements.get(index) instanceof String) {
						this.properties.get(key).add((String) elements.get(index));
						index++;
					}
				}
				index++;
			} while(((char) elements.get(index - 1)) == 13 || ((char) elements.get(index - 1)) == 10);
		} catch(IndexOutOfBoundsException exc) {
		} catch(ClassCastException exc) {
        	ConsoleStage.defaultCns.printConsole(new ConsoleLine("PROPERTIES", "Fail to build properties - ClassCastException in properties.read", ConsoleLine.FATAL, exc));
        	return false;
        }
		return true;
	}
	
	@Override
	public String toString() {
		String output = "";
		for(String key : properties.keySet()) {
			if(!output.equals(""))
				output = output + "\n";
			output = output + key + "=";
			String comps = "";
			for(String comp : properties.get(key)) {
				if(!comps.equals(""))
					comps = comps + " ";
				comps = comps + comp;
			}
			output = output + comps;
		}
		return output;
	}

	public HashMap<String, ArrayList<String>> getProperties() {
		return properties;
	}
	public void setProperties(HashMap<String, ArrayList<String>> properties) {
		this.properties = properties;
	}
}
