package Objects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import Console.ConsoleLine;
import Console.ConsoleStage;
import JavaObject.Block;
import JavaObject.Blockstate;
import JavaObject.Model;
import JavaObject.ModelElement;
import JavaObject.ModelFace;
import JavaObject.ModelRotation;
import JavaObject.RandomVariant;
import JavaObject.Texture;
import JavaObject.Variant;
import Main.DialogComboBox;
import Main.MSDialog;
import Main.Main;
import Main.ProgrammePath;
import ManagmentFile.CopyFile;
import Menu.ManagmentStage;
import Settings.Derivation;
import Settings.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
public class BlockTx extends ObjectTx implements Initializable, ProgrammePath {

	private final ObservableList<String> rotation = FXCollections.observableArrayList("Default", "90", "180", "270");
	private final int[] rotationInt = {0, 90, 180, 270};
	private final ObservableList<String> axis = FXCollections.observableArrayList("X", "Y", "Z");
	private ObservableList<String> derivationsTx = FXCollections.observableArrayList();
	private final ObservableList<String> tint = FXCollections.observableArrayList("None", "Grass/Leaves [0]");
	private final ObservableList<String> cullface = FXCollections.observableArrayList("none", "north", "east", "south", "west", "up", "down");
	
	private HashMap<Model, HashMap<String, ModelTexture>> modelTexturesMemory = new HashMap<>();
	private HashMap<Texture, Image> images = new HashMap<>();
	private HashMap<Texture, HashMap<Model, ModelTexture>> textureChildren = new HashMap<>();
	
	private static Texture createTexture = new Texture(null, "Create new Texture");
	private static Model nullModel = new Model(null, "");
	private static Texture nullModelTexture = new Texture(null, "");
	
	private static final String COMBO_ERROR = "combo-box-error";
	private static final String COMBO_WARN = "combo-box-warn";
	private static final String ERROR = "text-field-error";
	private static final String WARN = "text-field-warn";
	
	private boolean enable = true;
	private HashMap<Node, String> errors = new HashMap<>();
	private HashMap<Node, String> warns = new HashMap<>();
	
	@FXML
	private Label nameLabel;
	@FXML
	private TextField nameField;
	@FXML
	private ComboBox<String> derivationCombo;
	@FXML
	private TextField blockstateField;
	@FXML
	private Button importBlockstate;
	@FXML
	private ListView<Variant> listVariants;
	@FXML
	private ListView<RandomVariant> listRandom;
	@FXML
	private TextField blockstateConditions;
	@FXML
	private ComboBox<Model> blockstateModel;
	@FXML
	private TextField blockstateWeight;
	@FXML
	private ComboBox<String> blockstateX;
	@FXML
	private ComboBox<String> blockstateY;
	@FXML
	private ComboBox<String> blockstateZ;
	@FXML
	private ListView<Model> listModels;
	@FXML
	private TextField modelPath;
	@FXML
	private ComboBox<String> modelCategory;
	@FXML
	private ComboBox<String> modelDerivation;
	@FXML
	private TextField modelParent;
	@FXML
	private ComboBox<String> modelParentCategory;
	@FXML
	private ComboBox<String> modelParentDerivation;
	@FXML
	private TableView<ModelTexture> modelTextures;
	@FXML
	private TableColumn<ModelTexture, ImageView> textureModelImage;
	@FXML
	private TableColumn<ModelTexture, String> textureModelId;
	@FXML
	private TableColumn<ModelTexture, String> textureModelPath;
	@FXML
	private ListView<ModelElement> listElements;
	@FXML
	private TextField from1;
	@FXML
	private TextField from2;
	@FXML
	private TextField from3;
	@FXML
	private TextField to1;
	@FXML
	private TextField to2;
	@FXML
	private TextField to3;
	@FXML
	private CheckBox elementRotation;
	@FXML
	private TextField rotAngle;
	@FXML
	private ComboBox<String> rotAxis;
	@FXML
	private TextField rotOrigin1;
	@FXML
	private TextField rotOrigin2;
	@FXML
	private TextField rotOrigin3;
	@FXML
	private ListView<ModelFace> listFaces;
	@FXML
	private TextField uv1;
	@FXML
	private TextField uv2;
	@FXML
	private TextField uv3;
	@FXML
	private TextField uv4;
	@FXML
	private ComboBox<Texture> faceTexture;
	@FXML
	private ComboBox<String> faceTint;
	@FXML
	private ComboBox<String> faceRotation;
	@FXML
	private ComboBox<String> faceCullface;
	@FXML
	private CheckBox faceTrans;
	@FXML
	private ListView<Texture> listTextures;
	@FXML
	private TextField texturePath;
	@FXML
	private ComboBox<String> textureCategory;
	@FXML
	private ComboBox<String> textureDerivation;
	@FXML
	private Label textureSize;
	@FXML
	private ImageView textureImage;

	private Block block;
	
	public BlockTx(Block block) {
		super(block, "Block", new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/block.png"))));
		this.block = block;
		images.put(createTexture, new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/add.png")));
	}
	
	@Override
	public void open() {
		if(this.getTab() == null) {
			try {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Starting to open Block object \"" + block.getName() + "\"...", ConsoleLine.DEBUG));
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/Objects/Block.fxml"));
				loader.setController(this);
				this.setTab(loader.load());
				this.getTab().setText(block.getName());
				this.getTab().setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/block.png"))));
				ManagmentStage.getOn().getObjectsTabPane().getTabs().add(this.getTab());
				ManagmentStage.getOn().getObjectsTabPane().getSelectionModel().select(this.getTab());
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Open Block object \"" + block.getName() + "\" successfully", ConsoleLine.INFO));
			} catch (FileNotFoundException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to open Block object \"" + this + "\" - FileNotFoundException in Block.open", ConsoleLine.CRASH, exc));
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to open Block object \"" + this + "\" - IOException in Block.open", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to open Block object \"" + this + "\" - Exception in Block.open", ConsoleLine.CRASH, exc));
			}
		} else {
			ManagmentStage.getOn().getObjectsTabPane().getSelectionModel().select(this.getTab());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		derivationCombo.setItems(derivationsTx);
		modelDerivation.setItems(derivationsTx);
		modelParentDerivation.setItems(derivationsTx);
		textureDerivation.setItems(derivationsTx);
		rotAxis.setItems(FXCollections.observableArrayList(""));
		importBlockstate.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/import_icon.png"))));
		faceTint.setItems(tint);
		faceRotation.setItems(rotation);
		faceCullface.setItems(cullface);
		faceTexture.setItems(listTextures.getItems());
		
		nameLabel.setText(block.getName());
		nameField.setText(block.getName());
		for(Derivation derivation : (ArrayList<Derivation>) Settings.getSetting(Settings.DERIVATION)) {
			if(derivation.getVersion().equals(ManagmentStage.getOn().getTexturePack().getVersionString())) {
				derivationsTx.add(derivation.getCode());
			}
		}
		derivationCombo.getSelectionModel().select(block.getDerivation());
		blockstateField.setText(block.getBlockstate().getPath());
		for(Variant variant : block.getBlockstate().getVariants()) {
			listVariants.getItems().add(variant);
		}
		if(!listVariants.getItems().isEmpty())
			listVariants.getSelectionModel().select(0);
		blockstateX.setItems(rotation);
		blockstateY.setItems(rotation);
		blockstateZ.setItems(rotation);
		
		modelCategory.setItems(FXCollections.observableArrayList(Model.categories));
		modelDerivation.setPromptText(block.getDerivation());
		modelParentCategory.setItems(FXCollections.observableArrayList(Model.categories));
		modelParentDerivation.setPromptText(block.getDerivation());
		textureModelImage.setCellValueFactory(new PropertyValueFactory<ModelTexture, ImageView>("image"));
		textureModelId.setCellValueFactory(new PropertyValueFactory<ModelTexture, String>("modelId"));
		textureModelId.setCellFactory(TextFieldTableCell.forTableColumn());
		textureModelId.setOnEditCommit(e -> {
			ModelTexture txm = e.getTableView().getItems().get(e.getTablePosition().getRow());
			Model model = listModels.getSelectionModel().getSelectedItem();
			if(modelTexturesMemory.get(model).get(e.getNewValue().trim()) == null) {
				e.getTableView().getItems().get(e.getTablePosition().getRow()).setModelId(e.getNewValue().trim());
				modelTexturesMemory.get(model).remove(txm.getModelId());
				modelTexturesMemory.get(model).put(e.getNewValue().trim(), txm);
			} else {
				MSDialog.showError("Invalid Texture Key", "Key \"" + e.getNewValue().trim() + "\" has already been used, use another");
			}
		});
		listElements.setCellFactory(param -> new ListCell<ModelElement>() {
            @Override
            public void updateItem(ModelElement element, boolean empty) {
                super.updateItem(element, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(element.toString().replace(".0", ""));
                }
                setGraphic(null);
            }
        });
		textureModelPath.setCellValueFactory(new PropertyValueFactory<ModelTexture, String>("path"));
		faceTexture.setCellFactory(param -> new ListCell<Texture>() {
        	@Override
            public void updateItem(Texture texture, boolean empty) {
            	super.updateItem(texture, empty);
            	Model model = listModels.getSelectionModel().getSelectedItem();
            	if (empty || model == null) {
            		setText(null);
            		setGraphic(null);
            	} else {
            		setText(texture.toString());
            		Image image = images.get(texture);
            		if(image != null) {
	            		ImageView imageView = new ImageView(image);
	            		imageView.setFitHeight(16);
	            		imageView.setFitWidth(16);
	            		setGraphic(imageView);
            		}
            	}
        	}
        });
		faceTexture.getSelectionModel().select(nullModelTexture);
		
		textureDerivation.setPromptText(block.getDerivation());
		textureCategory.setItems(FXCollections.observableArrayList(Texture.categories));
		listTextures.setCellFactory(param -> new ListCell<Texture>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(Texture texture, boolean empty) {
                super.updateItem(texture, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if(texture.getFile() == null) {
                	if(texture.getPath() != null) {
                		imageView.setImage(images.get(texture));
                		setText(texture.getPath());
		                setGraphic(imageView);
                	} else {
                		imageView.setImage(new Image(Main.getApl().getClass().getResourceAsStream("/resource/exception/file_not_found.png"), 32, 32, false, false));
                		setText("Texture file not found");
		                setGraphic(imageView);
                	}
                } else if (!texture.getFile().exists()) {
                	imageView.setImage(new Image(Main.getApl().getClass().getResourceAsStream("/resource/exception/file_not_found.png"), 32, 32, false, false));
	                setText("Texture path not found");
	                setGraphic(imageView);
                } else {
                	imageView.setImage(new Image(texture.getFile().getPath(), 32, 32, false, false));
                    setText(texture.getPath());
                    setGraphic(imageView);
                }
            }
        });
		
		for(Texture texture : block.getBlockstate().getTexturesUD()) {
			boolean check = true;
			for(Texture texture2 : listTextures.getItems()) {
				if(texture.equals(texture2, block.getDerivation())) {
					check = false;
					break;
				}
			}
			if(check) {
				textureChildren.put(texture, new HashMap<>());
				listTextures.getItems().add(texture);
				File textureFile;
				if(texture.getFile() != null) {
					textureFile = texture.getFile();
				} else if(new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "\\assets\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png").exists()) {
					textureFile = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "\\assets\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png");
				} else if(new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png").exists()) {
					textureFile = new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png");
				} else if(!new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim()).exists()) {
					textureFile = null;
				} else {
					textureFile = null;
				}
				if(textureFile != null) {
					try {
						images.put(texture, new Image(textureFile.toURI().toURL().toExternalForm(), 32, 32, false, false));
					} catch (IOException exc) {
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to read texture \"" + textureFile + "\" - IOException in Block.texture.read", ConsoleLine.ERROR, exc));
						images.put(texture, new Image(Main.getApl().getClass().getResourceAsStream("/resource/exception/wrong_text.png"), 32, 32, false, false));
					}
				} else {
					textureImage.setImage(new Image(Main.getApl().getClass().getResourceAsStream("/resource/exception/file_not_found.png")));
				}
			}
		}
		for(Model model : block.getBlockstate().getModelsUD()) {
			boolean check = true;
			for(Model model2 : listModels.getItems()) {
				if(model.equals(model2, block.getDerivation())) {
					check = false;
					break;
				}
			}
			if(check) {
				blockstateModel.getItems().add(model);
				modelTexturesMemory.put(model, new HashMap<>());
				listModels.getItems().add(model);
				for(String key : model.getTextures().keySet()) {
					String textureValue = (model.getTextures().get(key).getDerivation() == null ? block.getDerivation() : model.getTextures().get(key).getDerivation()) + ":" + model.getTextures().get(key).getPath();
					Texture texture = null;
					for(Texture tx : listTextures.getItems()) {
						String txValue = (tx.getDerivation() == null ? block.getDerivation() : tx.getDerivation()) + ":" + tx.getPath();
						if(txValue.equals(textureValue)) {
							texture = tx;
							break;
						}
					}
					if(texture != null) {
						ModelTexture txm = new ModelTexture(null, texture.getValue(), key, images.get(texture));
						modelTexturesMemory.get(model).put(txm.getModelId(), txm);
						textureChildren.get(texture).put(model, txm);
					}
				}
			}
		}
		
		selectVariant();
		selectModel();
	}
	
	@Override
	public void save() {
		if(!warns.isEmpty()) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Warn in Block \"" + block.getName() + "\"" + warnToString(), ConsoleLine.WARN));
			MSDialog.showWarning("Warn in Block", "Warn in save block: it contains warns. The block will save. Please look at the console and fix them");
		}
		if(errors.isEmpty()) {
			block.setName(nameLabel.getText().trim());
			if(derivationCombo.getSelectionModel().getSelectedItem().trim().equals(""))
				block.setDerivation("minecraft");
			else
				block.setDerivation(derivationCombo.getSelectionModel().getSelectedItem().trim());
			block.getBlockstate().setPath(blockstateField.getText().trim());
			block.getBlockstate().getVariants().clear();
			for(Variant variant : listVariants.getItems()) {
				block.getBlockstate().getVariants().add(variant);
			}
			for(Model model : modelTexturesMemory.keySet()) {
				model.getTextures().clear();
				for(String key : modelTexturesMemory.get(model).keySet()) {
					Texture texture = null;
					for(Texture tx : listTextures.getItems()) {
						if(modelTexturesMemory.get(model).get(key).equals(tx, modelParentDerivation.getSelectionModel().getSelectedItem().trim())) {
							texture = tx;
							break;
						}
					}
					if(texture != null)
						model.getTextures().put(key, texture);
				}
			}
			
			try {
				File directory;
				if(block.getBlockstate().getPath().contains("/"))
					directory = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + block.getDerivation() + "/blockstates/" + 
								block.getBlockstate().getPath().substring(0, block.getBlockstate().getPath().lastIndexOf("/")));
				else if(block.getBlockstate().getPath().contains("\\"))
					directory = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + block.getDerivation() + "/blockstates/" + 
							block.getBlockstate().getPath().substring(0, block.getBlockstate().getPath().lastIndexOf("\\")));
				else
					directory = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + block.getDerivation() + "/blockstates/");
				if(!directory.exists())
					directory.mkdirs();
				FileWriter writer = new FileWriter(new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + block.getDerivation() + "/blockstates/" + block.getBlockstate().getPath() + ".json"));
				writer.write(block.getBlockstate().write());
				writer.close();
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Save blockstate \"" + block.getBlockstate().getValue() + "\" succefly", ConsoleLine.DEBUG));
			} catch(FileNotFoundException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to save blockstate \"" + block.getBlockstate().getValue() + "\" - FileNotFoundException in Block.save.blockstate", ConsoleLine.ERROR, exc));
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to save blockstate \"" + block.getBlockstate().getValue() + "\" - IOException in Block.save.blockstate", ConsoleLine.ERROR, exc));
			}
			for(Model model : block.getBlockstate().getModelsUD()) {
				try {
					boolean check = true;
					for(Model model2 : listModels.getItems()) {
						if(model != model2 && model.equals(model2)) {
							check = false;
							break;
						}
					}
					if(check) {
						String derivation;
						if(model.getDerivation() != null)
							derivation = model.getDerivation();
						else
							derivation = block.getDerivation();
						File directory;
						if(model.getPath().contains("/"))
							directory = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivation + "/models/" + 
									model.getPath().substring(0, model.getPath().lastIndexOf("/")));
						else if(model.getPath().contains("\\"))
							directory = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivation + "/models/" + 
									model.getPath().substring(0, model.getPath().lastIndexOf("\\")));
						else
							directory = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivation + "/models/");
						if(!directory.exists())
							directory.mkdirs();
						FileWriter writer = new FileWriter(new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivation + "/models/" + model.getPath() + ".json"));
						writer.write(model.write());
						writer.close();
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Save model \"" + model.getValue() + "\" succefly", ConsoleLine.DEBUG));
					}
				} catch(FileNotFoundException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to save model \"" + model.getValue() + "\" - FileNotFoundException in Block.save.model", ConsoleLine.ERROR, exc));
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to save model \"" + model.getValue() + "\" - IOException in Block.save.model", ConsoleLine.ERROR, exc));
				}
			}
			for(Texture texture : block.getBlockstate().getTexturesUD()) {
				if(texture.getFile() != null) {
					boolean check = true;
					for(Texture texture2 : listTextures.getItems()) {
						if(texture != texture2 && texture.equals(texture2)) {
							check = false;
							break;
						}
					}
					if(check) {
						String derivation;
						if(texture.getDerivation() != null)
							derivation = texture.getDerivation();
						else
							derivation = block.getDerivation();
						File directory;
						if(texture.getPath().contains("/"))
							directory = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivation + "/textures/" + 
									texture.getPath().substring(0, texture.getPath().lastIndexOf("/")));
						else if(texture.getPath().contains("\\"))
							directory = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivation + "/textures/" + 
									texture.getPath().substring(0, texture.getPath().lastIndexOf("\\")));
						else
							directory = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivation + "/textures/");
						if(!directory.exists())
							directory.mkdirs();
						new CopyFile(texture.getFile().getPath(), buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivation + "/textures/" + texture.getPath() + ".png", true).execute(false);
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Copy texture \"" + block.getBlockstate().getValue() + "\" succefly", ConsoleLine.DEBUG));
					}
				}
			}
			
			if(!ManagmentStage.getOn().getTexturePack().getObjects().getList().contains(block)) {
				ManagmentStage.getOn().getTexturePack().getObjects().getList().add(block);
			}
			ManagmentStage.getOn().refreshTreeObj();
			ManagmentStage.getOn().refreshTreeFiles();
			ManagmentStage.getOn().selectObj();
			ManagmentStage.getOn().getTexturePack().getObjects().save(ManagmentStage.getOn().getTexturePack().getName());
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Save Block \"" + block.getName() + "\" succefly", ConsoleLine.INFO));
			this.setSave(true);
		} else {
			MSDialog.showError("Fail to Save Block", "Fail to save block: it contains errors. Please look at the console and fix them");
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to save Block \"" + block.getName() + "\"" + errorToString(), ConsoleLine.FATAL));
		}
	}
	
	public void importBlockstate() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("JSON files", "*.json"),
		        new ExtensionFilter("Text files", "*.txt", ".json", ".mcmeta", ".properties"),
		        new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(ManagmentStage.getOn().getStage());
		if (selectedFile != null && MSDialog.showConfirm("Import Blockstate", "Are you sure to import this blockstate?\nAll changes will be deleted", "Yes", "No").equals("Yes")) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.INFO));
			try {
				Blockstate blockstate = new Blockstate(selectedFile, selectedFile.getName().substring(0, selectedFile.getName().indexOf(".")));
				blockstate.build(Blockstate.readJsonFile(selectedFile));
				listVariants.getItems().clear();
				for(Variant variant : blockstate.getVariants()) {
					listVariants.getItems().add(variant);
				}
				if(!listVariants.getItems().isEmpty())
					listVariants.getSelectionModel().select(0);
				blockstateField.setText(selectedFile.getName().substring(0, selectedFile.getName().indexOf(".")));
				selectVariant();
			} catch(Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCKSTATE", "Fail to import blockstate \"" + selectedFile.getPath() + "\" - Exception in Block.blockstate.import", ConsoleLine.FATAL));
			}
		}
	}
	public void importModel() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("JSON files", "*.json"),
		        new ExtensionFilter("Text files", "*.txt", ".json", ".mcmeta", ".properties"),
		        new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(ManagmentStage.getOn().getStage());
		if (selectedFile != null) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.INFO));
			try {
				Model model = new Model(selectedFile, selectedFile.getName().substring(0, selectedFile.getName().indexOf(".")));
				model.build(Model.readJsonFile(selectedFile));
				listModels.getItems().add(model);
				blockstateModel.getItems().add(model);
				modelTexturesMemory.put(model, new HashMap<>());
				selectModel();
			} catch(Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to import model \"" + selectedFile.getPath() + "\" - Exception in Block.model.import", ConsoleLine.FATAL));
			}
		}
	}
	public void importTexture() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("PNG files", "*.png"),
		        new ExtensionFilter("Image files", "*.png", ".jpg", ".jpeg"),
		        new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(ManagmentStage.getOn().getStage());
		if (selectedFile != null) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.INFO));
			try {
				String texturePath = selectedFile.getName().substring(0, selectedFile.getName().indexOf("."));
				Texture texture = new Texture(selectedFile, "block", texturePath);
				images.put(texture, new Image(selectedFile.toURI().toURL().toExternalForm(), 32, 32, false, false));
				textureChildren.put(texture, new HashMap<>());
				listTextures.getItems().add(texture);
				selectTexture();
			} catch(Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MODEL", "Fail to import model \"" + selectedFile.getPath() + "\" - Exception in Block.model.import", ConsoleLine.FATAL));
			}
		}
	}
	public void addVariant() {
		ArrayList<RandomVariant> random = new ArrayList<>();
		random.add(new RandomVariant(new Model(null, "block"), 1, 0, 0, 0));
		listVariants.getItems().add(new Variant(new ArrayList<String>(), random));
	}
	public void removeVariant() {
		Variant variant = listVariants.getSelectionModel().getSelectedItem();
		if(variant != null) {
			if(MSDialog.showConfirm("Delet Variant", "Are you sure to delet this variant?", "Yes", "No").equals("Yes")) {
				listVariants.getItems().remove(variant);
				selectVariant();
			}
		}
	}
	public void selectVariant() {
		Variant variant = listVariants.getSelectionModel().getSelectedItem();
		if(variant != null) {
			blockstateConditions.setEditable(true);
			blockstateConditions.setText(variant.conditionsToString());
			listRandom.getItems().clear();
			for(RandomVariant random : variant.getRandom()) {
				listRandom.getItems().add(random);
			}
			if(!listRandom.getItems().isEmpty())
				listRandom.getSelectionModel().select(0);
		} else {
			blockstateConditions.setText("");
			blockstateConditions.setEditable(false);
			listRandom.getItems().clear();
		}
		selectRandom();
	}
	public void addRandom() {
		Variant variant = listVariants.getSelectionModel().getSelectedItem();
		if(variant != null) {
			RandomVariant random = new RandomVariant(new Model(null, "block"), 1, 0, 0, 0);
			variant.getRandom().add(random);
			listRandom.getItems().add(random);
		}
	}
	public void removeRandom() {
		Variant variant = listVariants.getSelectionModel().getSelectedItem();
		RandomVariant random = listRandom.getSelectionModel().getSelectedItem();
		if(variant != null && random != null) {
			if(MSDialog.showConfirm("Delet Variant", "Are you sure to delet this variant?", "Yes", "No").equals("Yes")) {
				listRandom.getItems().remove(random);
				variant.getRandom().remove(random);
				selectRandom();
			}
		}
	}
	public void selectRandom() {
		Variant variant = listVariants.getSelectionModel().getSelectedItem();
		RandomVariant random = listRandom.getSelectionModel().getSelectedItem();
		if(variant != null && random != null) {
			enable = false;
			blockstateModel.getSelectionModel().select(random.getModel());
			if(random.getWeight() != 1)
				blockstateWeight.setText(random.getWeight()+"");
			else
				blockstateWeight.setText("");
			if(random.getX() != 0)
				blockstateX.getSelectionModel().select(random.getX()+"");
			else
				blockstateX.getSelectionModel().select("Default");
			if(random.getY() != 0)
				blockstateY.getSelectionModel().select(random.getY()+"");
			else
				blockstateY.getSelectionModel().select("Default");
			if(random.getZ() != 0)
				blockstateZ.getSelectionModel().select(random.getZ()+"");
			else
				blockstateZ.getSelectionModel().select("Default");
			enable = true;
			checkRandomVariant();
		} else {
			blockstateModel.getSelectionModel().select(nullModel);
			blockstateWeight.setText("");
			blockstateX.getSelectionModel().select("Default");
			blockstateY.getSelectionModel().select("Default");
			blockstateZ.getSelectionModel().select("Default");
		}
	}
	public void addModel() {
		Model model = new Model(null, "block/block");
		model.setParent(new Model(null, "block", "block", "minecraft"));
		listModels.getItems().add(model);
		blockstateModel.getItems().add(model);
		modelTexturesMemory.put(model, new HashMap<>());
	}
	public void removeModel() {
		Model model = listModels.getSelectionModel().getSelectedItem();
		if(model != null) {
			if(MSDialog.showConfirm("Delet Model", "Are you sure to delet this model?", "Yes", "No").equals("Yes")) {
				blockstateModel.getItems().remove(model);
				listModels.getItems().remove(model);
				blockstateModel.getItems().remove(model);
				modelTexturesMemory.remove(model);
				selectModel();
			}
		}
	}
	public void selectModel() {
		enable = false;
		Model model = listModels.getSelectionModel().getSelectedItem();
		if(model != null) {
			modelPath.setEditable(true);
			modelParent.setEditable(true);
			modelPath.setText(model.getName());
			modelCategory.getSelectionModel().select(model.getCategory());
			modelDerivation.getSelectionModel().select(model.getDerivationNotNull());
			if(model.getParent() != null) {
				modelParentDerivation.getSelectionModel().select(model.getParent().getDerivationNotNull());
				modelParentCategory.getSelectionModel().select(model.getParent().getCategory());
				modelParent.setText(model.getParent().getName());
			} else {
				modelParentDerivation.getSelectionModel().select("");
				modelParentCategory.getSelectionModel().select("");
				modelParent.setText("");
			}
			listElements.getItems().clear();
			for(ModelElement element : model.getElements()) {
				listElements.getItems().add(element);
			}
			if(!listElements.getItems().isEmpty())
				listElements.getSelectionModel().select(0);
			modelTextures.getItems().clear();
			for(String key : modelTexturesMemory.get(model).keySet()) {
				modelTextures.getItems().add(modelTexturesMemory.get(model).get(key));
			}
		} else {
			modelPath.setText("");
			modelPath.setEditable(false);
			modelCategory.getSelectionModel().select("");
			modelDerivation.getSelectionModel().select("");
			modelParent.setText("");
			modelParent.setEditable(false);
			modelParentCategory.getSelectionModel().select("");
			modelParentDerivation.getSelectionModel().select("");
			listElements.getItems().clear();
		}
		enable = true;
		selectElement();
		checkModel();
	}
	public void addElement() {
		Model model = listModels.getSelectionModel().getSelectedItem();
		if(model != null) {
			ModelElement element = new ModelElement();
			model.getElements().add(element);
			listElements.getItems().add(element);
		}
	}
	public void removeElement() {
		Model model = listModels.getSelectionModel().getSelectedItem();
		ModelElement element = listElements.getSelectionModel().getSelectedItem();
		if(model != null && element != null) {
			if(MSDialog.showConfirm("Delet Element", "Are you sure to delet this model element?", "Yes", "No").equals("Yes")) {
				model.getElements().remove(element);
				listElements.getItems().remove(element);
				selectElement();
			}
		}
	}
	public void selectElement() {
		Model model = listModels.getSelectionModel().getSelectedItem();
		ModelElement element = listElements.getSelectionModel().getSelectedItem();
		if(model != null && element != null) {
			from1.setEditable(true);
			from2.setEditable(true);
			from3.setEditable(true);
			to1.setEditable(true);
			to2.setEditable(true);
			to3.setEditable(true);
			from1.setText(element.getPositionFrom().get(0).toString().replace(".0", ""));
			from2.setText(element.getPositionFrom().get(1).toString().replace(".0", ""));
			from3.setText(element.getPositionFrom().get(2).toString().replace(".0", ""));
			to1.setText(element.getPositionTo().get(0).toString().replace(".0", ""));
			to2.setText(element.getPositionTo().get(1).toString().replace(".0", ""));
			to3.setText(element.getPositionTo().get(2).toString().replace(".0", ""));
			if(element.getRotation() != null) {
				elementRotation.setSelected(true);
				rotAngle.setEditable(true);
				rotOrigin1.setEditable(true);
				rotOrigin2.setEditable(true);
				rotOrigin3.setEditable(true);
				rotAngle.setText(element.getRotation().getAngle()+"");
				rotAngle.setText(rotAngle.getText().replace(".0", ""));
				rotAxis.getSelectionModel().select(element.getRotation().getAxis().toUpperCase());
				rotOrigin1.setText(element.getRotation().getOrigin().get(0).toString().replace(".0", ""));
				rotOrigin2.setText(element.getRotation().getOrigin().get(1).toString().replace(".0", ""));
				rotOrigin3.setText(element.getRotation().getOrigin().get(2).toString().replace(".0", ""));
			} else {
				elementRotation.setSelected(false);
				rotAngle.setText("");
				rotAxis.getSelectionModel().select("");
				rotOrigin1.setText("");
				rotOrigin2.setText("");
				rotOrigin3.setText("");
				rotAngle.setEditable(false);
				rotOrigin1.setEditable(false);
				rotOrigin2.setEditable(false);
				rotOrigin3.setEditable(false);
				removeError(rotOrigin1);
				removeError(rotOrigin2);
				removeError(rotOrigin3);
				removeError(rotAngle);
			}
			listFaces.getItems().clear();
			for(String faceName : ModelFace.faces) {
				ModelFace face = null;
				for(ModelFace checkFace : element.getFaces()) {
					if(checkFace.getName().equals(faceName))
						face = checkFace;
				}
				if(face == null) {
					face = new ModelFace(faceName, faceName);
				}
				listFaces.getItems().add(face);
			}
			listFaces.getSelectionModel().select(0);
		} else {
			from1.setText("");
			from2.setText("");
			from3.setText("");
			to1.setText("");
			to2.setText("");
			to3.setText("");
			rotAngle.setText("");
			rotAxis.getSelectionModel().select("");
			rotOrigin1.setText("");
			rotOrigin2.setText("");
			rotOrigin3.setText("");
			from1.setEditable(false);
			from2.setEditable(false);
			from3.setEditable(false);
			to1.setEditable(false);
			to2.setEditable(false);
			to3.setEditable(false);
			elementRotation.setSelected(false);
			rotAngle.setEditable(false);
			rotOrigin1.setEditable(false);
			rotOrigin2.setEditable(false);
			rotOrigin3.setEditable(false);
		}
		selectFace();
	}
	public void selectFace() {
		enable = false;
		Model model = listModels.getSelectionModel().getSelectedItem();
		ModelElement element = listElements.getSelectionModel().getSelectedItem();
		ModelFace face = listFaces.getSelectionModel().getSelectedItem();
		if(model != null && element != null && face != null) {
			faceTrans.setSelected(face.isTransparent());
			if(face.isTransparent()) {
				uv1.setEditable(false);
				uv2.setEditable(false);
				uv3.setEditable(false);
				uv4.setEditable(false);
			} else {
				uv1.setEditable(true);
				uv2.setEditable(true);
				uv3.setEditable(true);
				uv4.setEditable(true);
				uv1.setText(face.getUv().get(0).toString().replace(".0", ""));
				uv2.setText(face.getUv().get(1).toString().replace(".0", ""));
				uv3.setText(face.getUv().get(2).toString().replace(".0", ""));
				uv4.setText(face.getUv().get(3).toString().replace(".0", ""));
				faceTexture.getSelectionModel().select(modelTexturesMemory.get(model).get(face.getTexture().substring(1)));
				if(face.getTintindex() > 0)
					faceTint.getSelectionModel().select(face.getTintindex()+"");
				else if(face.getTintindex() == 0)
					faceTint.getSelectionModel().select(1);
				else
					faceTint.getSelectionModel().select(0);
				if(face.getRotation() == 0)
					faceRotation.getSelectionModel().select(0);
				else
					faceRotation.getSelectionModel().select(face.getRotation()+"");
				if(face.getCullface() != null)
					faceCullface.getSelectionModel().select(face.getCullface());
				else
					faceCullface.getSelectionModel().select(0);
			}
		} else {
			faceTrans.setSelected(false);
			uv1.setText("");
			uv2.setText("");
			uv3.setText("");
			uv4.setText("");
			uv1.setEditable(false);
			uv2.setEditable(false);
			uv3.setEditable(false);
			uv4.setEditable(false);
			faceTexture.getSelectionModel().select(nullModelTexture);
			faceTint.getSelectionModel().select(0);
			faceRotation.getSelectionModel().select(0);
		}
		enable = true;
		checkFace();
	}
	public void addTextureModel() {
		Model model = listModels.getSelectionModel().getSelectedItem();
		if(model != null) {
			ObservableList<Texture> texturesOn = FXCollections.observableArrayList(listTextures.getItems());
			for(Texture tx : textureChildren.keySet()) {
				if(textureChildren.get(tx).get(model) != null)
					texturesOn.remove(tx);
			}
			texturesOn.add(createTexture);
			Texture texture = new DialogComboBox<Texture>(texturesOn, images).showComboInput("Choose a Texture", null);
			if(texture != null) {
				ModelTexture txm;
				int id = 0;
				while(modelTexturesMemory.get(model).get(String.valueOf(id)) != null) {
					id++;
				}
				if(texture == createTexture) {
					texture = new Texture(null, "block", "texture");
					textureChildren.put(texture, new HashMap<>());
					txm = new ModelTexture(null, texture.getValue(), String.valueOf(id), images.get(texture));
					listTextures.getItems().add(texture);
					listTextures.getSelectionModel().select(texture);
					selectTexture();
				} else {
					txm = new ModelTexture(null, texture.getValue(), String.valueOf(id), images.get(texture));
				}
				modelTextures.getItems().add(txm);
				modelTexturesMemory.get(model).put(txm.getModelId(), txm);
				textureChildren.get(texture).put(model, txm);
			}
		}
	}
	public void removeTextureModel() {
		Model model = listModels.getSelectionModel().getSelectedItem();
		ModelTexture txm = modelTextures.getSelectionModel().getSelectedItem();
		if(model != null && txm != null) {
			if(MSDialog.showConfirm("Remove Model's Texture", "Are you sure to remove this model's texture", MSDialog.YES, MSDialog.NO).equals(MSDialog.YES)) {
				modelTextures.getItems().remove(txm);
				modelTexturesMemory.get(model).remove(txm.getModelId());
			}
		}
	}
	public void addTexture() {
		Texture texture = new Texture(null, "block", "texture");
		textureChildren.put(texture, new HashMap<>());
		listTextures.getItems().add(texture);
		listTextures.getSelectionModel().select(texture);
		selectTexture();
	}
	public void removeTexture() {
		Texture texture = listTextures.getSelectionModel().getSelectedItem();
		if(texture != null) {
			if(MSDialog.showConfirm("Delet Texture", "Are you sure to delet this texture?", "Yes", "No").equals("Yes")) {
				listTextures.getItems().remove(texture);
				for(Model model : textureChildren.get(texture).keySet()) {
					modelTexturesMemory.get(model).remove(textureChildren.get(texture).get(model).getModelId());
				}
				textureChildren.remove(texture);
				selectTexture();
			}
		}
	}
	public void selectTexture() {
		enable = false;
		Texture texture = listTextures.getSelectionModel().getSelectedItem();
		if(texture != null) {
			texturePath.setEditable(true);
			textureCategory.getSelectionModel().select(texture.getCategory());
			textureDerivation.getSelectionModel().select(texture.getDerivationNotNull());
			texturePath.setText(texture.getName());
			File textureFile;
			if(new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "\\assets\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png").exists()) {
				textureFile = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "\\assets\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png");
			} else if(new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png").exists()) {
				textureFile = new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png");
			} else if(texture.getFile() != null) {
				textureFile = texture.getFile();
			} else {
				textureFile = null;
			}
			if(textureFile != null) {
				try {
					BufferedImage buf = ImageIO.read(textureFile);
					textureSize.setText("Size: " + buf.getWidth() + " x " + buf.getHeight() + " pixels");
					textureImage.setImage(new Image(textureFile.toURI().toURL().toExternalForm()));
					images.put(texture, new Image(textureFile.toURI().toURL().toExternalForm(), 32, 32, false, false));
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to read texture \"" + textureFile + "\" - IOException in Block.texture.read", ConsoleLine.ERROR, exc));
				}
			} else {
				textureSize.setText("Size: .. x .. pixels");
				textureImage.setImage(new Image(Main.getApl().getClass().getResourceAsStream("/resource/exception/file_not_found.png")));
				images.put(texture, new Image(Main.getApl().getClass().getResourceAsStream("/resource/exception/file_not_found.png"), 32, 32, false, false));
			}
		} else {
			textureCategory.getSelectionModel().select("");
			texturePath.setText("");
			textureSize.setText("Size: .. x .. pixels");
			textureImage.setImage(null);
			texturePath.setEditable(false);
		}
		enable = true;
		checkTexture();
	}
	public void checkGeneral() {
		if(nameField.getText().trim().equals("")) {
			addError(nameField, "Name null");
			nameLabel.setText("New Block");
		} else if(!new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + 
				"\\blockstates\\" + nameField.getText().trim() + ".json").exists()) {
			removeError(nameField);
			addWarn(nameField, "Name block not found");
			nameLabel.setText(nameField.getText().trim());
		} else {
			removeError(nameField);
			nameLabel.setText(nameField.getText().trim());
		}
		if(derivationCombo.getSelectionModel().getSelectedItem().trim().equals("")) {
			addError(derivationCombo, "Derivation null");
		} else if(!new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim()).exists()) {
			removeError(derivationCombo);
			addWarn(derivationCombo, "Derivation not found");
		} else {
			removeError(derivationCombo);
		}
		if(blockstateField.getText().trim().equals("")) {
			addError(blockstateField, "Blockstate null");
		} else {
			removeError(blockstateField);
		}
		modelDerivation.setPromptText(derivationCombo.getSelectionModel().getSelectedItem().trim());
		modelParentDerivation.setPromptText(derivationCombo.getSelectionModel().getSelectedItem().trim());
		textureDerivation.setPromptText(derivationCombo.getSelectionModel().getSelectedItem().trim());
		this.setSave(false);
	}
	public void checkCondition() {
		Variant variant = listVariants.getSelectionModel().getSelectedItem();
		if(variant != null) {
			for(Variant vr : listVariants.getItems()) {
				if(blockstateConditions.getText().trim().equals(vr.conditionsToString()))
					addError(blockstateConditions, "Equals conditions");
				else
					removeError(blockstateConditions);
			}
			variant.setConditions(blockstateConditions.getText().trim());
		}
		this.setSave(false);
	}
	public void checkRandomVariant() {
		Variant variant = listVariants.getSelectionModel().getSelectedItem();
		RandomVariant random = listRandom.getSelectionModel().getSelectedItem();
		if(variant != null && random != null && enable) {
			try {
				if(Integer.parseInt(blockstateWeight.getText().trim()) > 0) {
					removeError(blockstateWeight);
				} else {
					addError(blockstateWeight, "Weigh < 0");
				}
				random.setWeigh(Integer.parseInt(blockstateWeight.getText().trim()));
			} catch(NumberFormatException | NullPointerException exc) {
				if(blockstateWeight.getText().trim().equals(""))
					removeError(blockstateWeight);
				else
					addError(blockstateWeight, "Weigh not valid");
				random.setWeigh(1);
			}
			random.setModel(blockstateModel.getSelectionModel().getSelectedItem());
			random.setX(rotationInt[blockstateX.getSelectionModel().getSelectedIndex()]);
			random.setY(rotationInt[blockstateY.getSelectionModel().getSelectedIndex()]);
			random.setZ(rotationInt[blockstateZ.getSelectionModel().getSelectedIndex()]);
			this.setSave(false);
		}
	}
	public void checkModel() {
		Model model = listModels.getSelectionModel().getSelectedItem();
		if(model != null && enable) {
			if(block.getBlockstate().getModelsUD().contains(model)) {
				removeError(modelPath);
				removeError(modelCategory);
				removeError(modelDerivation);
			} else {
				addWarn(modelPath, "Model not used");
				addWarn(modelCategory);
				addWarn(modelDerivation);
			}
			String derivationParent;
			if(modelParentDerivation.getSelectionModel().getSelectedItem().trim().equals(""))
				derivationParent = derivationCombo.getSelectionModel().getSelectedItem().trim();
			else
				derivationParent = modelParentDerivation.getSelectionModel().getSelectedItem().trim();
			String pathParent;
			if(modelParentCategory.getSelectionModel().getSelectedItem().trim().equals(""))
				pathParent = modelParent.getText().trim();
			else
				pathParent = modelParentCategory.getSelectionModel().getSelectedItem().trim() + "/" + modelParent.getText().trim();
			boolean check = false;
			if(model.getParent() != null) {
				for(Model modelParent : listModels.getItems()) {
					if(modelParent.equals(model.getParent(), modelParentDerivation.getSelectionModel().getSelectedItem().trim())) {
						check = true;
						break;
					}
				}
			}
			if(check) {
				removeError(modelParent);
				removeError(modelParentCategory);
				removeError(modelParentDerivation);
				model.getParent().setFile(new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivationParent + "/models/" + pathParent + ".json"));
			} else if(new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivationParent + "/models/" + pathParent + ".json").exists()) {
				removeError(modelParent);
				removeError(modelParentCategory);
				removeError(modelParentDerivation);
				model.getParent().setFile(new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "/assets/" + derivationParent + "/models/" + pathParent + ".json"));
			} else if(new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "/" + derivationParent + "/models/" + pathParent + ".json").exists()) {
				removeError(modelParent);
				removeError(modelParentCategory);
				removeError(modelParentDerivation);
				model.getParent().setFile(new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "/" + derivationParent + "/models/" + pathParent + ".json"));
			} else if(!new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "/" + derivationParent).exists()) {
				removeError(modelParent);
				removeError(modelParentCategory);
				removeError(modelParentDerivation);
				addWarn(modelParentDerivation, "Model parent's derivation not found");
				model.getParent().setFile(null);
			} else {
				addError(modelParent, "Model parent not found");
				addError(modelParentCategory);
				addError(modelParentDerivation);
				model.getParent().setFile(null);
			}
			model.setPath(modelPath.getText().trim());
			model.setCategory(modelCategory.getSelectionModel().getSelectedItem().trim());
			model.setDerivation(modelDerivation.getSelectionModel().getSelectedItem().trim());
			model.getParent().setPath(modelParent.getText().trim());
			model.getParent().setCategory(modelParentCategory.getSelectionModel().getSelectedItem().trim());
			String derivationNull;
			if(modelDerivation.getSelectionModel().getSelectedItem().trim().equals(""))
				derivationNull = modelDerivation.getSelectionModel().getSelectedItem().trim();
			else
				derivationNull = null;
			model.getParent().setDerivation(derivationNull);
			for(Model md : listModels.getItems()) {
				if(model != md) {
					if(model.equals(md, modelParentDerivation.getSelectionModel().getSelectedItem().trim())) {
						addError(modelPath, "Same model path");
						addError(modelCategory);
						addError(modelDerivation);
					}
				}
			}
			this.setSave(false);
		}
	}
	public void checkElement() {
		Model model = listModels.getSelectionModel().getSelectedItem();
		ModelElement element = listElements.getSelectionModel().getSelectedItem();
		if(model != null && element != null) {
			try {
				if(Float.parseFloat(from1.getText().trim()) >= -16 && Float.parseFloat(from1.getText().trim()) <= 32)
					removeError(from1);
				else
					addError(from1, "From [1] value over size");
				if(Float.parseFloat(from2.getText().trim()) >= -16 && Float.parseFloat(from2.getText().trim()) <= 32)
					removeError(from2);
				else
					addError(from2, "From [2] value over size");
				if(Float.parseFloat(from3.getText().trim()) >= -16 && Float.parseFloat(from3.getText().trim()) <= 32)
					removeError(from3);
				else
					addError(from3, "From [3] value over size");
				element.getPositionFrom().set(0, Float.parseFloat(from1.getText().trim()));
				element.getPositionFrom().set(1, Float.parseFloat(from2.getText().trim()));
				element.getPositionFrom().set(2, Float.parseFloat(from3.getText().trim()));
			} catch(NumberFormatException | NullPointerException exc) {
				element.getPositionFrom().set(0, 0f);
				element.getPositionFrom().set(1, 0f);
				element.getPositionFrom().set(2, 0f);
				addError(from1, "From [1] value not valid");
				addError(from2, "From [2] value not valid");
				addError(from3, "From [3] value not valid");
			}
			try {
				if(Float.parseFloat(to1.getText().trim()) >= -16 && Float.parseFloat(to1.getText().trim()) <= 32)
					removeError(to1);
				else
					addError(to1, "To [1] value over size");
				if(Float.parseFloat(to2.getText().trim()) >= -16 && Float.parseFloat(to2.getText().trim()) <= 32)
					removeError(to2);
				else
					addError(to2, "To [2] value over size");
				if(Float.parseFloat(to3.getText().trim()) >= -16 && Float.parseFloat(to3.getText().trim()) <= 32)
					removeError(to3);
				else
					addError(to3, "To [3] value over size");
				element.getPositionTo().set(0, Float.parseFloat(to1.getText().trim()));
				element.getPositionTo().set(1, Float.parseFloat(to2.getText().trim()));
				element.getPositionTo().set(2, Float.parseFloat(to3.getText().trim()));
			} catch(NumberFormatException | NullPointerException exc) {
				element.getPositionTo().set(0, 16f);
				element.getPositionTo().set(1, 16f);
				element.getPositionTo().set(2, 16f);
				addError(to1, "To [1] value not valid");
				addError(to2, "To [2] value not valid");
				addError(to3, "To [3] value not valid");
			}
			if(elementRotation.isSelected()) {
				rotAngle.setEditable(true);
				rotOrigin1.setEditable(true);
				rotOrigin2.setEditable(true);
				rotOrigin3.setEditable(true);
				rotAxis.setItems(axis);
				if(rotAxis.getSelectionModel().getSelectedItem() == null)
					rotAxis.getSelectionModel().select(0);
				element.setRotation(new ModelRotation());
				try {
					element.getRotation().setAngle(Float.parseFloat(rotAngle.getText().trim()));
					removeError(rotAngle);
				} catch(NumberFormatException | NullPointerException exc) {
					element.getRotation().setAngle(0);
					addError(rotAngle, "Rotation angle not valid");
				}
				element.getRotation().setAxis(rotAxis.getSelectionModel().getSelectedItem());
				try {
					float origin1 = Float.parseFloat(rotOrigin1.getText().trim());
					float origin2 = Float.parseFloat(rotOrigin2.getText().trim());
					float origin3 = Float.parseFloat(rotOrigin3.getText().trim());
					element.getRotation().getOrigin().set(0, origin1);
					element.getRotation().getOrigin().set(1, origin2);
					element.getRotation().getOrigin().set(2, origin3);
					removeError(rotOrigin1);
					removeError(rotOrigin2);
					removeError(rotOrigin3);
				} catch(NumberFormatException | NullPointerException exc) {
					element.getRotation().getOrigin().set(0, 8f);
					element.getRotation().getOrigin().set(1, 8f);
					element.getRotation().getOrigin().set(2, 8f);
					addError(rotOrigin1, "Rotation [1] value not valid");
					addError(rotOrigin2, "Rotation [1] value not valid");
					addError(rotOrigin3, "Rotation [1] value not valid");
				}
			} else {
				rotAngle.setText("");
				rotAxis.setItems(FXCollections.observableArrayList());
				rotAxis.getSelectionModel().select("");
				rotOrigin1.setText("");
				rotOrigin2.setText("");
				rotOrigin3.setText("");
				rotAngle.setEditable(false);
				rotOrigin1.setEditable(false);
				rotOrigin2.setEditable(false);
				rotOrigin3.setEditable(false);
				element.setRotation(null);
				removeError(rotOrigin1);
				removeError(rotOrigin2);
				removeError(rotOrigin3);
				removeError(rotAngle);
			}
			this.setSave(false);
		}
	}
	public void checkFace() {
		Model model = listModels.getSelectionModel().getSelectedItem();
		ModelElement element = listElements.getSelectionModel().getSelectedItem();
		ModelFace face = listFaces.getSelectionModel().getSelectedItem();
		if(model != null && element != null && face != null && enable) {
			if(face.isTransparent()) {
				uv1.setEditable(false);
				uv2.setEditable(false);
				uv3.setEditable(false);
				uv4.setEditable(false);
			} else {
				uv1.setEditable(true);
				uv2.setEditable(true);
				uv3.setEditable(true);
				uv4.setEditable(true);
				try {
					float value1 = Float.parseFloat(uv1.getText());
					float value2 = Float.parseFloat(uv2.getText());
					float value3 = Float.parseFloat(uv3.getText());
					float value4 = Float.parseFloat(uv4.getText());
					if(value1 < 0 || value1 > 16)
						addError(uv1, "UV [1] value over size");
					else
						removeError(uv1);
					if(value2 < 0 || value2 > 16)
						addError(uv2, "UV [2] value over size");
					else
						removeError(uv2);
					if(value3 < 0 || value3 > 16)
						addError(uv3, "UV [3] value over size");
					else
						removeError(uv3);
					if(value4 < 0 || value4 > 16)
						addError(uv4, "UV [4] value over size");
					else
						removeError(uv4);
					face.getUv().set(0, value1);
					face.getUv().set(1, value2);
					face.getUv().set(2, value3);
					face.getUv().set(3, value4);
				} catch(NumberFormatException exc) {
					addError(uv1, "UV [1] value not valid");
					addError(uv2, "UV [2] value not valid");
					addError(uv3, "UV [3] value not valid");
					addError(uv4, "UV [4] value not valid");
					face.getUv().set(0, 0f);
					face.getUv().set(1, 0f);
					face.getUv().set(2, 16f);
					face.getUv().set(3, 16f);
				}
				try {
					if(faceTint.getSelectionModel().getSelectedItem().equals("None")) {
						face.setTintindex(-1);
						removeError(faceTint);
					} else if(faceTint.getSelectionModel().getSelectedItem().equals("Grass/Leaves [0]")) {
						face.setTintindex(0);
						removeError(faceTint);
					} else if(Integer.parseInt(faceTint.getSelectionModel().getSelectedItem().trim()) >= 0) {
						face.setTintindex(Integer.parseInt(faceTint.getSelectionModel().getSelectedItem().trim()));
						removeError(faceTint);
					} else {
						addError(faceTint, "Texture tint < 0");
						face.setTintindex(-1);
					}
				} catch(NumberFormatException exc) {
					addError(faceTint, "Texture tint value not valid");
					face.setTintindex(-1);
				}
				if(textureChildren.get(faceTexture.getSelectionModel().getSelectedItem()) != null) {
					ModelTexture txm = textureChildren.get(faceTexture.getSelectionModel().getSelectedItem()).get(model);
					if(txm != null) {
						face.setTexture("#" + txm.getModelId());
					} else {
						Texture texture = faceTexture.getSelectionModel().getSelectedItem();
						int id = 0;
						while(modelTexturesMemory.get(model).get(String.valueOf(id)) != null) {
							id++;
						}
						txm = new ModelTexture(null, texture.getValue(), String.valueOf(id), images.get(texture));
						modelTextures.getItems().add(txm);
						modelTexturesMemory.get(model).put(txm.getModelId(), txm);
						textureChildren.get(texture).put(model, txm);
						face.setTexture("#" + txm.getModelId());
					}
				}
				if(faceRotation.getSelectionModel().getSelectedIndex() != 0) 
					face.setRotation(Integer.parseInt(faceRotation.getSelectionModel().getSelectedItem()));
				if(faceCullface.getSelectionModel().getSelectedIndex() != 0) 
					face.setCullface(faceCullface.getSelectionModel().getSelectedItem());
				else
					face.setCullface(null);
			}
			this.setSave(false);
		}
	}
	public void checkTexture() {
		Texture texture = listTextures.getSelectionModel().getSelectedItem();
		if(texture != null && enable) {
			texture.setDerivation(textureDerivation.getSelectionModel().getSelectedItem().trim());
			texture.setCategory(textureCategory.getSelectionModel().getSelectedItem().trim());
			texture.setName(texturePath.getText().trim());
			File textureFile;
			if(texture.getFile() != null) {
				textureFile = texture.getFile();
				removeError(textureDerivation);
				removeError(textureCategory);
				removeError(texturePath);
			} else if(new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "\\assets\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png").exists()) {
				textureFile = new File(buildTexture + ManagmentStage.getOn().getTexturePack().getName() + "\\assets\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png");
				removeError(textureDerivation);
				removeError(textureCategory);
				removeError(texturePath);
			} else if(new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png").exists()) {
				textureFile = new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim() + "\\textures\\" + texture.toString() + ".png");
				removeError(textureDerivation);
				removeError(textureCategory);
				removeError(texturePath);
			} else if(!new File(derivations + ManagmentStage.getOn().getTexturePack().getVersionString() + "\\" + derivationCombo.getSelectionModel().getSelectedItem().trim()).exists()) {
				textureFile = null;
				removeError(textureDerivation);
				removeError(textureCategory);
				removeError(texturePath);
				addWarn(textureDerivation, "Texture derivation not found");
			} else {
				textureFile = null;
				addError(textureDerivation);
				addError(textureCategory);
				addError(texturePath, "Texture path not found");
			}
			if(textureFile != null) {
				try {
					BufferedImage buf = ImageIO.read(textureFile);
					textureSize.setText("Size: " + buf.getWidth() + " x " + buf.getHeight() + " pixels");
					textureImage.setImage(new Image(textureFile.toURI().toURL().toExternalForm()));
					images.put(texture, new Image(textureFile.toURI().toURL().toExternalForm(), 32, 32, false, false));
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("BLOCK", "Fail to read texture \"" + textureFile + "\" - IOException in Block.texture.read", ConsoleLine.ERROR, exc));
					textureImage.setImage(new Image(Main.getApl().getClass().getResourceAsStream("/resource/exception/wrong_text.png")));
					images.put(texture, new Image(Main.getApl().getClass().getResourceAsStream("/resource/exception/wrong_text.png"), 32, 32, false, false));
				}
			} else {
				textureSize.setText("Size: .. x .. pixels");
				textureImage.setImage(new Image(Main.getApl().getClass().getResourceAsStream("/resource/exception/file_not_found.png")));
			}
			for(Model model : textureChildren.get(texture).keySet()) {
				ModelTexture txm = textureChildren.get(texture).get(model);
				if(modelTextures.getItems().contains(txm)) {
					modelTextures.getItems().remove(txm);
					ModelTexture newtxm = new ModelTexture(txm.getFile(), texture.getPath(), txm.getModelId(), images.get(texture));
					modelTextures.getItems().add(newtxm);
					textureChildren.get(texture).put(model, newtxm);
					modelTexturesMemory.get(listModels.getSelectionModel().getSelectedItem()).put(txm.getModelId(), newtxm);
				}
				txm.setPath(texture.getPath());
				txm.setImage(images.get(texture));
			}
			for(Texture tx : listTextures.getItems()) {
				if(tx != texture) {
					if(texture.equals(tx, modelParentDerivation.getSelectionModel().getSelectedItem().trim())) {
						addError(texturePath, "Same texture path");
					}
				}
			}
			this.setSave(false);
		}
	}
	
	private void addError(Node node) {
		if(node instanceof TextField) {
			if(!node.getStyleClass().contains(ERROR)) {
				node.getStyleClass().add(ERROR);
				node.getStyleClass().remove(WARN);
				warns.remove((TextField) node);
			}
		} else if(node instanceof ComboBox) {
			if(!node.getStyleClass().contains(COMBO_ERROR)) {
				node.getStyleClass().add(COMBO_ERROR);
				warns.remove((ComboBox<?>) node);
			}
		}
	}
	private void addWarn(Node node) {
		if(node instanceof TextField) {
			if(!node.getStyleClass().contains(WARN) && !node.getStyleClass().contains(ERROR))
				node.getStyleClass().add(WARN);
		} else if(node instanceof ComboBox) {
			if(!node.getStyleClass().contains(COMBO_WARN) && !node.getStyleClass().contains(COMBO_WARN))
				node.getStyleClass().add(COMBO_WARN);
		}
	}
	private void addError(Node node, String view) {
		if(node instanceof TextField) {
			if(!node.getStyleClass().contains(ERROR)) {
				node.getStyleClass().add(ERROR);
				node.getStyleClass().remove(WARN);
				errors.put((TextField) node, view);
				warns.remove((TextField) node);
			}
		} else if(node instanceof ComboBox) {
			if(!node.getStyleClass().contains(COMBO_ERROR)) {
				node.getStyleClass().add(COMBO_ERROR);
				errors.put((ComboBox<?>) node, view);
				warns.remove((ComboBox<?>) node);
			}
		}
	}
	private void addWarn(Node node, String view) {
		if(node instanceof TextField) {
			if(!node.getStyleClass().contains(WARN) && !node.getStyleClass().contains(ERROR)) {
				node.getStyleClass().add(WARN);
				warns.put((TextField) node, view);
			}
		} else if(node instanceof ComboBox) {
			if(!node.getStyleClass().contains(COMBO_WARN) && !node.getStyleClass().contains(COMBO_WARN)) {
				node.getStyleClass().add(COMBO_WARN);
				warns.put((ComboBox<?>) node, view);
			}
		}
	}
	private void removeError(Node node) {
		if(node instanceof TextField) {
			node.getStyleClass().remove(ERROR);
			node.getStyleClass().remove(WARN);
			errors.remove((TextField) node);
			warns.remove((TextField) node);
		} else if(node instanceof ComboBox) {
			node.getStyleClass().remove(COMBO_ERROR);
			node.getStyleClass().remove(COMBO_WARN);
			errors.remove((ComboBox<?>) node);
			warns.remove((ComboBox<?>) node);
		}
	}
	
	private String errorToString() {
		String output = "";
		for(Node error : errors.keySet()) {
			output = output + "\n                    ";
			output = output + errors.get(error);
		}
		return output;
	}
	private String warnToString() {
		String output = "";
		for(Node warn : warns.keySet()) {
			output = output + "\n                    ";
			output = output + warns.get(warn);
		}
		return output;
	}
	
	public Block getBlock() {
		return block;
	}
	public void setBlock(Block block) {
		this.block = block;
	}
}
