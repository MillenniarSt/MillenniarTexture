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

public class Blockstate extends Component implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final byte id = 2;
	
	private ArrayList<Variant> variants;
	private HashMap<String, Object> other;
	
	public Blockstate(File file, String name, ArrayList<Variant> variants, HashMap<String, Object> other) {
		super(file, name);
		this.variants = variants;
		this.other = other;
	}
	
	public Blockstate(File file, String value) {
		super(file, value);
		this.variants = new ArrayList<>();
		this.other = new HashMap<>();
	}
	
	public static ArrayList<Object> readJsonFile(File json) throws FileNotFoundException, IOException {
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Starting to read blockstate file \"" + json.getPath() + "\"...",
				ConsoleLine.INFO));
		ArrayList<Object> output = new ArrayList<>();
		char[] noReadChar = {' ', '\n'};
		char[] specialReadChar = {':', ',', '{', '}', '[', ']'};
		char[] number = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-'};
		FileReader reader = new FileReader(json);
			
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
	
	public boolean build(ArrayList<Object> elements) {
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Starting to build blockstate file...", ConsoleLine.INFO));
		this.variants = new ArrayList<Variant>();
	    int index = 0;
	    if(((char) elements.get(index)) == '{' && ((char) elements.get(elements.size() - 1)) == '}') {
	        index++;
	        try {
	            do {
	            	if(((char) elements.get(index + 1)) == ':' && (elements.get(index + 2) instanceof String || elements.get(index + 2) instanceof Boolean)) {
	                	if(this.other == null)
	                		this.other = new HashMap<String, Object>();
	                	this.other.put((String) elements.get(index), elements.get(index + 2));
	                	index = index + 4;
	            	} else if(((char) elements.get(index + 1)) == ':' && ((char) elements.get(index + 2)) == '{') {
	                    index = index + 3;
	                    int conditionOn = -1;
	                    do {
	                    	if(((char) elements.get(index + 1)) == ':') {
		                        ArrayList<String> conditions = new ArrayList<>();
		                        String condition = (String) elements.get(index);
		                        if(!condition.trim().equals("")) {
			                        try {
			                            while(condition.contains(",")) {
			                                conditions.add(condition.substring(0, condition.indexOf(",")));
			                                condition = condition.substring(condition.indexOf(",") + 1);
			                            }
			                            conditions.add(condition);
			                        } catch(StringIndexOutOfBoundsException exc) { }
		                        }
		                        this.variants.add(new Variant(conditions));
		                        conditionOn++;
		                        if(((char) elements.get(index + 2)) == '{') {
		                        	this.variants.get(conditionOn).getRandom().add(0, new RandomVariant());
                                    index = index + 3;
                                    do {
                                        if(elements.get(index) instanceof String && ((char) elements.get(index + 1)) == ':') {
                                        	if(elements.get(index + 2) instanceof String) {
                                        		if(elements.get(index).equals("model")) {
                                        			this.variants.get(conditionOn).getRandom().get(0).setModel(new Model(null, (String) elements.get(index + 2)));
                                        		} else {
                                        			this.variants.get(conditionOn).getRandom().get(0).getOther().put((String) elements.get(index), (String) elements.get(index + 2));
                                        		}
                                        	} else if(elements.get(index + 2) instanceof Integer) {
                                        		if(elements.get(index).equals("weight")) {
                                        			this.variants.get(conditionOn).getRandom().get(0).setWeigh((Integer) elements.get(index + 2));
                                        		} else if(elements.get(index).equals("x")) {
                                        			this.variants.get(conditionOn).getRandom().get(0).setX((Integer) elements.get(index + 2));
                                        		} else if(elements.get(index).equals("y")) {
                                        			this.variants.get(conditionOn).getRandom().get(0).setY((Integer) elements.get(index + 2));
                                        		} else if(elements.get(index).equals("z")) {
                                        			this.variants.get(conditionOn).getRandom().get(0).setZ((Integer) elements.get(index + 2));
                                        		} else {
                                        			this.variants.get(conditionOn).getRandom().get(0).getOther().put((String) elements.get(index), (Integer) elements.get(index + 2));
                                        		}
                                        	} else {
                                        		this.variants.get(conditionOn).getRandom().get(0).getOther().put((String) elements.get(index), elements.get(index + 2));
                                        	}
                                            index = index + 4;
                                        }
                                    } while(((char) elements.get(index - 1)) == ',');
                                    index++;
		                        } else if(((char) elements.get(index + 2)) == '[') {
		                            index = index + 3;
		                            int modelOn = -1;
		                            do {
		                                if(((char) elements.get(index)) == '{') {
		                                    index++;
		                                    modelOn++;
		                                    this.variants.get(conditionOn).getRandom().add(new RandomVariant());
		                                    do {
		                                    	if(elements.get(index) instanceof String && ((char) elements.get(index + 1)) == ':') {
		                                        	if(elements.get(index + 2) instanceof String) {
		                                        		if(elements.get(index).equals("model")) {
		                                        			this.variants.get(conditionOn).getRandom().get(modelOn).setModel(new Model(null, (String) elements.get(index + 2)));
		                                        		} else {
		                                        			this.variants.get(conditionOn).getRandom().get(modelOn).getOther().put((String) elements.get(index), (String) elements.get(index + 2));
		                                        		}
		                                        	} else if(elements.get(index + 2) instanceof Integer) {
		                                        		if(elements.get(index).equals("weight")) {
		                                        			this.variants.get(conditionOn).getRandom().get(modelOn).setWeigh((Integer) elements.get(index + 2));
		                                        		} else if(elements.get(index).equals("x")) {
		                                        			this.variants.get(conditionOn).getRandom().get(modelOn).setX((Integer) elements.get(index + 2));
		                                        		} else if(elements.get(index).equals("y")) {
		                                        			this.variants.get(conditionOn).getRandom().get(modelOn).setY((Integer) elements.get(index + 2));
		                                        		} else if(elements.get(index).equals("z")) {
		                                        			this.variants.get(conditionOn).getRandom().get(modelOn).setZ((Integer) elements.get(index + 2));
		                                        		} else {
		                                        			this.variants.get(conditionOn).getRandom().get(modelOn).getOther().put((String) elements.get(index), (Integer) elements.get(index + 2));
		                                        		}
		                                        	} else {
		                                        		this.variants.get(conditionOn).getRandom().get(modelOn).getOther().put((String) elements.get(index), elements.get(index + 2));
		                                        	}
		                                            index = index + 4;
		                                        }
		                                    } while(((char) elements.get(index - 1)) == ',');
		                                    if(!(((char) elements.get(index - 1)) == '}')) {
		                                    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Fail to build blockstate model - Insert '}' to complete", ConsoleLine.ERROR));
		                                        throw new ArrayIndexOutOfBoundsException();
		                                    }
		                            }
		                            index++;
		                        } while(((char) elements.get(index - 1)) == ',');
		                        if(!(((char) elements.get(index - 1)) == ']')) {
		                        	ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Fail to build blockstate condition - Insert ']' to complete", ConsoleLine.ERROR));
		                            throw new ArrayIndexOutOfBoundsException();
		                        }
		                        index++;
		                    }
	                    } else {
	                    	throw new ArrayIndexOutOfBoundsException();
	                    }
	                } while(((char) elements.get(index - 1)) == ',');
	                if(!(((char) elements.get(index - 1)) == '}')) {
	                	ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Fail to build blockstate variants - Insert '}' to complete", ConsoleLine.ERROR));
	                    throw new ArrayIndexOutOfBoundsException();
	                }
	                index++;
	                } else {
	                	ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Fail to build blockstate - Insert ':' and a value to complete", ConsoleLine.ERROR));
	                    throw new ArrayIndexOutOfBoundsException();
	                }
	            } while(((char) elements.get(index - 1)) == ',');
	        } catch(ArrayIndexOutOfBoundsException exc) {
	        	ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Fail to build blockstate - ArrayIndexOutOfBoundsException in blockstate.read", ConsoleLine.FATAL, exc));
	        	return false;
	        } catch(ClassCastException exc) {
	        	ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Fail to build blockstate - ClassCastException in blockstate.read", ConsoleLine.FATAL, exc));
	        	return false;
	        }
	    } else {
	    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Fail to build blockstate - it is not a blockstate", ConsoleLine.FATAL));
	    	return false;
	    }
	    ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Build blockstate successfully", ConsoleLine.SUCCESS));
	    return true;
	}
	
	public String write() {
		String output = "{";
		if(this.other != null) {
			for(String other : this.other.keySet()) {
				if(this.other.get(other) instanceof String)
					output = output + "\n  \"" + other + "\": \"" + this.other.get(other) + "\",";
				else 
					output = output + "\n  \"" + other + "\": " + this.other.get(other) + ",";
			}
		}
		String variantText = "";
		if(this.variants != null) {
			output = output + "\n  \"variants\": {";
			for(Variant variant : this.variants) {
				if(!variantText.equals(""))
					variantText = variantText + ",";
				variantText = variantText + "\n";
				String conditions = "";
				for(String basicCondition : variant.getConditions()) {
					if(!conditions.equals(""))
						conditions = conditions + "," + basicCondition;
					else
						conditions = conditions + basicCondition;
				}
				variantText = variantText + "    \"" + conditions + "\": ";
				if(variant.getRandom().size() == 1) {
					variantText = variantText + "{";
					RandomVariant random = variant.getRandom().get(0);
					String modelIntrs = "";
					if(random.getModel() != null) {
						modelIntrs = modelIntrs + "\n      \"model\": " + random.getModel().getValue();
					}
					if(random.getWeight() != 1) {
						if(!modelIntrs.equals(""))
							modelIntrs = modelIntrs + ",";
						modelIntrs = modelIntrs + "\n      \"weight\": " + random.getWeight();
					}
					if(random.getX() != 1) {
						if(!modelIntrs.equals(""))
							modelIntrs = modelIntrs + ",";
						modelIntrs = modelIntrs + "\n      \"x\": " + random.getX();
					}
					if(random.getY() != 1) {
						if(!modelIntrs.equals(""))
							modelIntrs = modelIntrs + ",";
						modelIntrs = modelIntrs + "\n      \"y\": " + random.getY();
					}
					if(random.getZ() != 1) {
						if(!modelIntrs.equals(""))
							modelIntrs = modelIntrs + ",";
						modelIntrs = modelIntrs + "\n      \"z\": " + random.getZ();
					}
					variantText = variantText + modelIntrs + "\n    }";
				} else {
					variantText = variantText + "[";
					String conditionText = "";
					for(RandomVariant random : variant.getRandom()) {
						if(!conditionText.equals(""))
							conditionText = conditionText + ",";
						conditionText = conditionText + "\n      {";
						String modelIntrs = "";
						if(random.getModel() != null) {
							modelIntrs = modelIntrs + "\n        \"model\": " + random.getModel().getValue();
						}
						if(random.getWeight() != 1) {
							if(!modelIntrs.equals(""))
								modelIntrs = modelIntrs + ",";
							modelIntrs = modelIntrs + "\n        \"weight\": " + random.getWeight();
						}
						if(random.getX() != 1) {
							if(!modelIntrs.equals(""))
									modelIntrs = modelIntrs + ",";
									modelIntrs = modelIntrs + "\n        \"x\": " + random.getX();
						}
						if(random.getY() != 1) {
							if(!modelIntrs.equals(""))
								modelIntrs = modelIntrs + ",";
							modelIntrs = modelIntrs + "\n        \"y\": " + random.getY();
						}
						if(random.getZ() != 1) {
							if(!modelIntrs.equals(""))
								modelIntrs = modelIntrs + ",";
							modelIntrs = modelIntrs + "\n        \"z\": " + random.getZ();
						}
						conditionText = conditionText + modelIntrs + "\n      }";
					}
					variantText = conditionText + "\n    ]";
				}
			}
		}
		return output + variantText + "\n  }\n}";
	}

	public ArrayList<Model> getModelsUD() {
		ArrayList<Model> output = new ArrayList<>();
		for(Variant variant : variants) {
			for(RandomVariant random : variant.getRandom()) {
				output.add(random.getModel());
			}
		}
		return output;
	}
	public ArrayList<Texture> getTexturesUD() {
		ArrayList<Texture> output = new ArrayList<>();
		for(Variant variant : variants) {
			for(RandomVariant random : variant.getRandom()) {
				output.addAll(random.getModel().getTexturesUD());
			}
		}
		return output;
	}
	
	@Override
	public Blockstate clone() {
		Blockstate output = new Blockstate(this.getFile(), this.getValue());
		for(String key : other.keySet()) {
			output.other.put(key, other.get(key));
		}
		for(Variant variant : variants) {
			output.variants.add(variant.clone());
		}
		return output;
	}
	
	public ArrayList<Variant> getVariants() {
		return variants;
	}
	public void setVariants(ArrayList<Variant> variants) {
		this.variants = variants;
	}
	public HashMap<String, Object> getOther() {
		return other;
	}
	public void setOther(HashMap<String, Object> other) {
		this.other = other;
	}
}
