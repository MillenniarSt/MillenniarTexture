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

public class Model extends Component implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final byte id = 1;
	
	public static final String[] categories = {"block", "item"};
	
	private Model parent;
	private String category;
	private String name;
	private HashMap<String, Texture> textures;
	private ArrayList<ModelElement> elements;
	private HashMap<String, HashMap<String, ArrayList<Float>>> display;
	private HashMap<String, Object> other;
	
	public Model(File file, String path) {
		super(file, path);
		if(getPath().contains("\\")) {
			this.name = getPath().substring(getPath().indexOf("\\") + 1);
			this.category = getPath().substring(0, getPath().indexOf("\\"));
		} else if(getPath().contains("/")) {
			this.name = getPath().substring(getPath().indexOf("/") + 1);
			this.category = getPath().substring(0, getPath().indexOf("/"));
		} else {
			this.category = "";
			this.name = getPath();
		}
		this.textures = new HashMap<>();
		this.elements = new ArrayList<>();
		this.other = new HashMap<>(); 
	}
	public Model(File file, String path, String derivation) {
		super(file, derivation, path);
		if(getPath().contains("\\")) {
			this.name = getPath().substring(getPath().indexOf("\\") + 1);
			this.category = getPath().substring(0, getPath().indexOf("\\"));
		} else if(getPath().contains("/")) {
			this.name = getPath().substring(getPath().indexOf("/") + 1);
			this.category = getPath().substring(0, getPath().indexOf("/"));
		} else {
			this.category = "";
			this.name = getPath();
		}
		this.textures = new HashMap<>();
		this.elements = new ArrayList<>();
		this.other = new HashMap<>(); 
	}
	public Model(File file, String category, String name, String derivation) {
		super(file, derivation, category + "/" + name);
		this.category = category;
		this.name = name;
		this.textures = new HashMap<>();
		this.elements = new ArrayList<>();
		this.other = new HashMap<>(); 
	}
	
	public static ArrayList<Object> readJsonFile(File json) throws FileNotFoundException, IOException {
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Starting to read model file \"" + json.getPath() + "\"...",
				ConsoleLine.INFO));
		ArrayList<Object> output = new ArrayList<>();
		char[] noReadChar = {' ', '\n'};
		char[] specialReadChar = {':', ',', '{', '}', '[', ']'};
		char[] number = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '-'};
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
				output.add(Float.parseFloat(ne));
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
					if(((char) elements.get(index + 1)) == ':') {
						if(((String) elements.get(index)).equals("parent")) {
							this.parent = new Model(null, (String) elements.get(index + 2));
							index = index + 4;
						} else if(((String) elements.get(index)).equals("textures") && ((char) elements.get(index + 2)) == '{') {
							this.textures = new HashMap<String, Texture>();
							index = index + 3;
							do {
								if(((char) elements.get(index + 1)) == ':') {
									String texture = (String) elements.get(index + 2);
									this.textures.put((String) elements.get(index), new Texture(null, texture));
									index = index + 4;
								} else {
									ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model textures - Insert ':' to complete", ConsoleLine.ERROR));
				                    throw new ArrayIndexOutOfBoundsException();
								}
							} while(((char) elements.get(index - 1)) == ',');
							if(!(((char) elements.get(index - 1)) == '}')) {
								ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model textures - Insert '}' to complete", ConsoleLine.ERROR));
                                throw new ArrayIndexOutOfBoundsException();
                            }
							index++;
						} else if(((String) elements.get(index)).equals("elements") && ((char) elements.get(index + 2)) == '[') {
							this.elements = new ArrayList<ModelElement>();
							index = index + 3;
							int elementOn = -1;
							do {
								index++;
								if(((char) elements.get(index - 1)) == '{') {
									this.elements.add(new ModelElement(null, null, null, null, null));
									elementOn++;
									do {
										if(((char) elements.get(index + 1)) == ':') {
											if(((String) elements.get(index)).equals("from") && ((char) elements.get(index + 2)) == '[') {
												this.elements.get(elementOn).setPositionFrom(new ArrayList<Float>());
												index = index + 3;
												do {
													this.elements.get(elementOn).getPositionFrom().add((Float) elements.get(index));
													index = index + 2;
												} while(((char) elements.get(index - 1)) == ',');
												if(!(((char) elements.get(index - 1)) == ']')) {
													ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements numbers - Insert ']' to complete", ConsoleLine.ERROR));
					                                throw new ArrayIndexOutOfBoundsException();
					                            }
												index++;
											} else if(((String) elements.get(index)).equals("to") && ((char) elements.get(index + 2)) == '[') {
												this.elements.get(elementOn).setPositionTo(new ArrayList<Float>());
												index = index + 3;
												do {
													this.elements.get(elementOn).getPositionTo().add((Float) elements.get(index));
													index = index + 2;
												} while(((char) elements.get(index - 1)) == ',');
												if(!(((char) elements.get(index - 1)) == ']')) {
													ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements numbers - Insert ']' to complete", ConsoleLine.ERROR));
					                                throw new ArrayIndexOutOfBoundsException();
					                            }
												index++;
											} else if(((String) elements.get(index)).equals("rotation") && ((char) elements.get(index + 2)) == '{') {
												this.elements.get(elementOn).setRotation(new ModelRotation());
												index = index + 3;
												do {
													if(((char) elements.get(index + 1)) == ':' && elements.get(index + 2) instanceof String) {
														if(elements.get(index).equals("axis")) {
															this.elements.get(elementOn).getRotation().setAxis((String) elements.get(index + 2));
														} else {
															this.elements.get(elementOn).getRotation().getOther().put((String) elements.get(index), (String) elements.get(index + 2));
														}
														index = index + 4;
													} else if(((char) elements.get(index + 1)) == ':' && elements.get(index + 2) instanceof Float) {
														if(elements.get(index).equals("angle")) {
															this.elements.get(elementOn).getRotation().setAngle((Float) elements.get(index + 2));
														} else {
															this.elements.get(elementOn).getRotation().getOther().put((String) elements.get(index), (Float) elements.get(index + 2));
														}
														index = index + 4;
													} else if(((char) elements.get(index + 1)) == ':' && elements.get(index + 2) instanceof Boolean) {
														this.elements.get(elementOn).getRotation().getOther().put((String) elements.get(index), (Boolean) elements.get(index + 2));
														index = index + 4;
													} else if(((char) elements.get(index + 1)) == ':' && ((char) elements.get(index + 2)) == '[') {
														index = index + 3;
														do {
															this.elements.get(elementOn).getRotation().getOrigin().add((Float) elements.get(index));
															index = index + 2;
														} while(((char) elements.get(index - 1)) == ',');
														if(!(((char) elements.get(index - 1)) == ']')) {
															ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements properties numbers - Insert ']' to complete", ConsoleLine.ERROR));
							                                throw new ArrayIndexOutOfBoundsException();
							                            }
														index++;
													} else {
														ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements faces properties - Insert ':' and a String or ':' and '[' to complete", ConsoleLine.ERROR));
							                            throw new ArrayIndexOutOfBoundsException();
													}
												} while(((char) elements.get(index - 1)) == ',');
												if(!(((char) elements.get(index - 1)) == '}')) {
													ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements faces - Insert '}' to complete", ConsoleLine.ERROR));
					                                throw new ArrayIndexOutOfBoundsException();
					                            }
												index++;
											} else if(((String) elements.get(index)).equals("faces") && ((char) elements.get(index + 2)) == '{') {
												this.elements.get(elementOn).setFaces(new ArrayList<ModelFace>());
												index = index + 3;
												do {
													ModelFace faceProp = new ModelFace((String) elements.get(index));
													this.elements.get(elementOn).getFaces().add(faceProp);
													index = index + 3;
													do {
														if(((char) elements.get(index + 1)) == ':' && elements.get(index + 2) instanceof String) {
															if(elements.get(index).equals("texture")) {
																faceProp.setTexture((String) elements.get(index + 2));
															} else if(elements.get(index).equals("cullface")) {
																faceProp.setCullface((String) elements.get(index + 2));
															} else {
																faceProp.getOther().put((String) elements.get(index), (String) elements.get(index + 2));
															}
															index = index + 4;
														} else if(((char) elements.get(index + 1)) == ':' && elements.get(index + 2) instanceof Float) {
															if(elements.get(index).equals("rotation")) {
																faceProp.setRotation(((Float) elements.get(index + 2)).intValue());
															} else if(elements.get(index).equals("tintindex")) {
																faceProp.setTintindex(((Float) elements.get(index + 2)).intValue());
															} else {
																faceProp.getOther().put((String) elements.get(index), (Float) elements.get(index + 2));
															}
															index = index + 4;
														} else if(((char) elements.get(index + 1)) == ':' && ((char) elements.get(index + 2)) == '[') {
															index = index + 3;
															do {
																faceProp.getUv().add((Float) elements.get(index));
																index = index + 2;
															} while(((char) elements.get(index - 1)) == ',');
															if(!(((char) elements.get(index - 1)) == ']')) {
																ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements faces numbers - Insert ']' to complete", ConsoleLine.ERROR));
								                                throw new ArrayIndexOutOfBoundsException();
								                            }
															index++;
														} else {
															ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements faces properties - Insert ':' and a String or ':' and '[' to complete", ConsoleLine.ERROR));
								                            throw new ArrayIndexOutOfBoundsException();
														}
													} while(((char) elements.get(index - 1)) == ',');
													if(!(((char) elements.get(index - 1)) == '}')) {
														ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements faces - Insert '}' to complete", ConsoleLine.ERROR));
						                                throw new ArrayIndexOutOfBoundsException();
						                            }
													index++;
												} while(((char) elements.get(index - 1)) == ',');
												if(!(((char) elements.get(index - 1)) == '}')) {
													ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements faces - Insert '}' to complete", ConsoleLine.ERROR));
					                                throw new ArrayIndexOutOfBoundsException();
					                            }
												index++;
											} else if(elements.get(index + 2) instanceof String || elements.get(index + 2) instanceof Boolean) {
												if(this.elements.get(elementOn).getOther() == null)
													this.elements.get(elementOn).setOther(new HashMap<String, Object>());
												this.elements.get(elementOn).getOther().put((String) elements.get(index), elements.get(index + 2));
												index = index + 4;
											} else {
												ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elemets properties - Insert ':' to complete", ConsoleLine.ERROR));
							                    throw new ArrayIndexOutOfBoundsException();
											}
										}
									} while(((char) elements.get(index - 1)) == ',');
									if(!(((char) elements.get(index - 1)) == '}')) {
										ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements - Insert '}' to complete", ConsoleLine.ERROR));
		                                throw new ArrayIndexOutOfBoundsException();
		                            }
									index++;
								} else {
									ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements - Insert '{' to complete", ConsoleLine.ERROR));
				                    throw new ArrayIndexOutOfBoundsException();
								}
							} while(((char) elements.get(index - 1)) == ',');
							if(!(((char) elements.get(index - 1)) == ']')) {
								ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model elements - Insert ']' to complete", ConsoleLine.ERROR));
                                throw new ArrayIndexOutOfBoundsException();
                            }
							index++;
						} else if(((String) elements.get(index)).equals("display") && ((char) elements.get(index + 2)) == '{') {
							this.display = new HashMap<String, HashMap<String, ArrayList<Float>>>();
							index = index + 3;
							do {
								if(((char) elements.get(index + 1)) == ':' && ((char) elements.get(index + 2)) == '{') {
									this.display.put((String) elements.get(index), new HashMap<String, ArrayList<Float>>());
									String displayProperty = (String) elements.get(index);
									index = index + 3;
									do {
										this.display.get(displayProperty).put((String) elements.get(index), new ArrayList<Float>());
										String propertyNumbers = (String) elements.get(index);
										index = index + 3;
										do {
											this.display.get(displayProperty).get(propertyNumbers).add((Float) elements.get(index));
											index = index + 2;
										} while(((char) elements.get(index - 1)) == ',');
										if(!(((char) elements.get(index - 1)) == ']')) {
											ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model display properties numbers - Insert ']' to complete", ConsoleLine.ERROR));
			                                throw new ArrayIndexOutOfBoundsException();
			                            }
										index++;
									} while(((char) elements.get(index - 1)) == ',');
									if(!(((char) elements.get(index - 1)) == '}')) {
										ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model display properties - Insert '}' to complete", ConsoleLine.ERROR));
		                                throw new ArrayIndexOutOfBoundsException();
		                            }
									index++;
								} else {
									ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model display properties - Insert ':' and '{' to complete", ConsoleLine.ERROR));
	                                throw new ArrayIndexOutOfBoundsException();
								}
							} while(((char) elements.get(index - 1)) == ',');
							if(!(((char) elements.get(index - 1)) == '}')) {
								ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model display - Insert '}' to complete", ConsoleLine.ERROR));
                                throw new ArrayIndexOutOfBoundsException();
                            }
							index++;
						} else if(elements.get(index + 2) instanceof String || elements.get(index + 2) instanceof Boolean) {
							if(this.other == null)
								this.other = new HashMap<String, Object>();
							this.other.put((String) elements.get(index), elements.get(index + 2));
							index = index + 4;
						} else {
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model data - Insert ':' and a value to complete", ConsoleLine.ERROR));
		                    throw new ArrayIndexOutOfBoundsException();
						}
					} else {
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model data - Insert ':' to complete", ConsoleLine.ERROR));
	                    throw new ArrayIndexOutOfBoundsException();
					}
				} while(((char) elements.get(index - 1)) == ',');
			} catch(ArrayIndexOutOfBoundsException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model - ArrayIndexOutOfBoundsException in blockstate.read", ConsoleLine.FATAL, exc));
				return false;
			} catch(ClassCastException exc) {
	        	ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model - ClassCastException in blockstate.read", ConsoleLine.FATAL, exc));
	        	return false;
	        }
		} else {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model - it is not a model", ConsoleLine.FATAL));
			return false;
		}
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Build model successfully", ConsoleLine.SUCCESS));
		return true;
	}
	public boolean buildParentTextures(ArrayList<Object> elements) {
		int index = 0;
		if(((char) elements.get(index)) == '{' && ((char) elements.get(elements.size() - 1)) == '}') {
			index++;
			try {
				do {
					if(((char) elements.get(index + 1)) == ':') {
						if(((String) elements.get(index)).equals("parent")) {
							this.parent = new Model(null, (String) elements.get(index + 2));
							index = index + 4;
						} else if(((String) elements.get(index)).equals("textures") && ((char) elements.get(index + 2)) == '{') {
							this.textures = new HashMap<String, Texture>();
							index = index + 3;
							do {
								if(((char) elements.get(index + 1)) == ':') {
									String texture = (String) elements.get(index + 2);
									this.textures.put((String) elements.get(index), new Texture(null, texture));
									index = index + 4;
								} else {
									ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model textures - Insert ':' to complete", ConsoleLine.ERROR));
				                    throw new ArrayIndexOutOfBoundsException();
								}
							} while(((char) elements.get(index - 1)) == ',');
							if(!(((char) elements.get(index - 1)) == '}')) {
								ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model textures - Insert '}' to complete", ConsoleLine.ERROR));
                                throw new ArrayIndexOutOfBoundsException();
                            }
							index++;
						}
					} else {
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model data - Insert ':' to complete", ConsoleLine.ERROR));
	                    throw new ArrayIndexOutOfBoundsException();
					}
				} while(((char) elements.get(index - 1)) == ',');
			} catch(ArrayIndexOutOfBoundsException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model - ArrayIndexOutOfBoundsException in blockstate.read", ConsoleLine.FATAL, exc));
				return false;
			} catch(ClassCastException exc) {
	        	ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model - ClassCastException in blockstate.read", ConsoleLine.FATAL, exc));
	        	return false;
	        }
		} else {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to build model - it is not a model", ConsoleLine.FATAL));
			return false;
		}
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Build model successfully", ConsoleLine.SUCCESS));
		return true;
	}
	
	public String write() {
		String output = "{";
		if(!this.other.isEmpty()) {
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
		if(this.parent != null) {
			if(!output.equals("{"))
				output = output + ",";
			output = output + "\n    \"parent\": \"" + this.parent.getValue() + "\"";
		}
		if(!this.textures.isEmpty()) {
			if(!output.equals("{"))
				output = output + ",";
			output = output + "\n    \"textures\": {";
			String texturesList = "";
			for(String texture : this.textures.keySet()) {
				if(!texturesList.equals(""))
					texturesList = texturesList + ",";
				texturesList = texturesList + "\n        \"" + texture + "\": \"" + this.textures.get(texture).getValue() + "\"";
			}
			output = output + texturesList + "\n    }";
		}
		if(!this.elements.isEmpty()) {
			if(!output.equals("{"))
				output = output + ",";
			output = output + "\n    \"elements\": [";
			String elementsList = "";
			for(ModelElement element : this.elements) {
				if(!elementsList.equals(""))
					elementsList = elementsList + ",";
				elementsList = elementsList + "\n        {";
				String propertiesList = "";
				if(element.getOther() != null) {
					String otherList = "";
					for(String other : element.getOther().keySet()) {
						if(!otherList.equals(""))
							otherList = otherList + ",";
						if(element.getOther().get(other) instanceof String)
							otherList = otherList + "\n	    \"" + other + "\": \"" + element.getOther().get(other) + "\"";
						else
							otherList = otherList + "\n	    \"" + other + "\": " + element.getOther().get(other);
					}
					propertiesList = propertiesList + otherList;
				}
				if(element.getPositionFrom() != null) {
					if(!propertiesList.equals(""))
						propertiesList = propertiesList + ",";
					propertiesList = propertiesList + "\n            \"from\": [ ";
					String numbersList = "";
					for(Float number : element.getPositionFrom()) {
						if(!numbersList.equals(""))
							numbersList = numbersList + ", ";
						numbersList = numbersList + number;
					}
					propertiesList = propertiesList + numbersList + " ]";
				}
				if(element.getPositionTo() != null) {
					if(!propertiesList.equals(""))
						propertiesList = propertiesList + ",";
					propertiesList = propertiesList + "\n            \"to\": [ ";
					String numbersList = "";
					for(Float number : element.getPositionTo()) {
						if(!numbersList.equals(""))
							numbersList = numbersList + ", ";
						numbersList = numbersList + number;
					}
					propertiesList = propertiesList + numbersList + " ]";
				}
				if(element.getRotation() != null) {
					if(!propertiesList.equals(""))
						propertiesList = propertiesList + ",";
					propertiesList = propertiesList + "\n            \"rotation\": {"
							+ "\"angle\":" + element.getRotation().getAngle()
							+ ", \"axis\":" + element.getRotation().getAxis() + ", \"origin\":[";
					String origin = "";
					for(Float pos : element.getRotation().getOrigin()) {
						if(!origin.equals(""))
							origin = origin + ", ";
						origin = origin + pos;
					}
					propertiesList = propertiesList + origin + "]}";
				}
				if(element.getFaces() != null) {
					if(!propertiesList.equals(""))
						propertiesList = propertiesList + ",";
					propertiesList = propertiesList + "\n            \"faces\": {";
					String facesList = "";
					for(ModelFace face : element.getFaces()) {
						if(!facesList.equals(""))
							facesList = facesList + ",";
						facesList = facesList + "\n                \"" + face.getName() + "\": {";
						String facepropertiesList = "";
						if(face.getUv() != null) {
							facepropertiesList = facepropertiesList + "\"uv\":[";
							String uv = "";
							for(Float number : face.getUv()) {
								if(!uv.equals(""))
									uv = uv + ", ";
								uv = uv + number;
							}
							facepropertiesList = facepropertiesList + uv + "]";
						}
						if(face.getRotation() != 0) {
							if(!facepropertiesList.equals(""))
								facepropertiesList = facepropertiesList + ", ";
							facepropertiesList = facepropertiesList + "\"rotation\":" + face.getRotation();
						}
						if(face.getTexture() != null) {
							if(!facepropertiesList.equals(""))
								facepropertiesList = facepropertiesList + ", ";
							facepropertiesList = facepropertiesList + "\"texture\":\"" + face.getRotation() + "\"";
						}
						if(face.getTintindex() != 0) {
							if(!facepropertiesList.equals(""))
								facepropertiesList = facepropertiesList + ", ";
							facepropertiesList = facepropertiesList + "\"tintindex\":" + face.getTintindex();
						}
						if(face.getCullface() != null) {
							if(!facepropertiesList.equals(""))
								facepropertiesList = facepropertiesList + ", ";
							facepropertiesList = facepropertiesList + "\"cullface\":\"" + face.getCullface() + "\"";
						}
						facesList = facesList + facepropertiesList + "}";
					}
					propertiesList = propertiesList + facesList + "\n            }";
				}
				elementsList = elementsList + propertiesList + "\n        }";
			}
			output = output + elementsList + "\n    ]";
		}
		if(this.display != null) {
			if(!output.equals("{"))
				output = output + ",";
			output = output + "\n    \"display\": {";
			String displayList = "";
			for(String display : this.display.keySet()) {
				if(!displayList.equals(""))
					displayList = displayList + ",";
				displayList = displayList + "\n            \"" + display + "\": {";
				String displayPropertiesList = "";
				for(String displayProperty : this.display.get(display).keySet()) {
					if(!displayPropertiesList.equals(""))
						displayPropertiesList = displayPropertiesList + ",";
					displayPropertiesList = displayPropertiesList + "\n                \"" + displayProperty + "\": [ ";
					String numbersList = "";
					for(Float number : this.display.get(display).get(displayProperty)) {
						if(!numbersList.equals(""))
							numbersList = numbersList + ", ";
						numbersList = numbersList + number;
					}
					displayPropertiesList = displayPropertiesList + numbersList + "]";
				}
				displayList = displayList + displayPropertiesList + "\n            }";
			}
			output = output + displayList + "\n    }";
		}
		return output + "\n}";
	}

	public ArrayList<Texture> getTexturesUD() {
		ArrayList<Texture> output = new ArrayList<>();
		for(String key : textures.keySet()) {
			output.add(textures.get(key));
		}
		return output;
	}
	
	@Override
	public Model clone() {
		Model output = new Model(this.getFile(), this.getValue());
		if(parent != null)
			output.setParent(parent.clone());
		for(String key : other.keySet()) {
			output.other.put(key, other.get(key));
		}
		for(String key : textures.keySet()) {
			output.textures.put(key, new Texture(textures.get(key).getFile(), textures.get(key).getValue()));
		}
		for(ModelElement element : elements) {
			output.elements.add(element.clone());
		}
		return output;
	}
	
	public Model getParent() {
		return parent;
	}
	public void setParent(Model parent) {
		this.parent = parent;
	}
	public ArrayList<ModelElement> getElements() {
		return elements;
	}
	public void setElements(ArrayList<ModelElement> elements) {
		this.elements = elements;
	}
	public HashMap<String, Texture> getTextures() {
		return textures;
	}
	public void setTextures(HashMap<String, Texture> textures2) {
		this.textures = textures2;
	}
	public HashMap<String, Object> getOther() {
		return other;
	}
	public void setOther(HashMap<String, Object> other) {
		this.other = other;
	}
	public HashMap<String, HashMap<String, ArrayList<Float>>> getDisplay() {
		return display;
	}
	public void setDisplay(HashMap<String, HashMap<String, ArrayList<Float>>> display) {
		this.display = display;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
		if(category.equals(""))
			this.setPath(name);
		else
			this.setPath(category + "/" + name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		if(category.equals(""))
			this.setPath(name);
		else
			this.setPath(category + "/" + name);
	}
}
