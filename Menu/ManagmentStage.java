package Menu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import Console.ConsoleLine;
import Console.ConsoleStage;
import CreateTexture.CreateTexture;
import CreateTexture.Register;
import CreateTexture.TexturePack;
import Files.DirectoryTx;
import Files.FileJSON;
import Files.SaveJSON;
import Files.FileMCMETA;
import Files.SaveMCMETA;
import Files.FilePNG;
import Files.SavePNG;
import Files.FilePROPERTIES;
import Files.SavePROPERTIES;
import Files.FileTXT;
import Files.SaveTXT;
import Files.FileTx;
import Files.SaveTx;
import JavaObject.Block;
import JavaObject.Blockstate;
import JavaObject.Model;
import JavaObject.Texture;
import JavaObject.TextureObject;
import Main.MSDialog;
import Main.Main;
import Main.ProgrammePath;
import Main.Support;
import ManagmentFile.CopyFile;
import ManagmentFile.DeleteFile;
import Objects.BlockTx;
import Objects.ObjectTx;
import Settings.ManagmentOption;
import Settings.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

public class ManagmentStage implements Initializable, ProgrammePath {

	private static ManagmentStage on = null;
	private static FileTx copy;
	
	public static TexturePack texturePackTemp = null;
	
	private TexturePack texturePack;
	private Stage stage;
	
	private ObservableList<FileTx> filesOblist = FXCollections.observableArrayList();
	
	@FXML
	private Button addObj;
	@FXML
	private Button duplicateObj;
	@FXML
	private Button modifyObj;
	@FXML
	private Button deletObj;
	@FXML
	private Button exportObj;
	@FXML
	private Button importObj;
	@FXML
	private TableView<ObjectTx> listObjects;
	@FXML
	private TableColumn<ObjectTx, String> derivationObj;
	@FXML
	private TableColumn<ObjectTx, String> nameObj;
	@FXML
	private TableColumn<ObjectTx, String> typeObj;
	@FXML
	private TableColumn<ObjectTx, Boolean> activatedObj;
	@FXML
	private TableColumn<ObjectTx, String> problemsObj;
	@FXML
	private TableView<FileTx> listFiles;
	@FXML
	private TableColumn<FileTx, String> nameFile;
	@FXML
	private TableColumn<FileTx, String> typeFile;
	@FXML
	private TableColumn<FileTx, Boolean> activatedFile;
	@FXML
	private TableColumn<FileTx, String> problemsFile;
	@FXML
	private Label directoryLabel;
	@FXML
	private TabPane filesTabPane;
	@FXML
	private TabPane objectsTabPane;
	@FXML
	private TreeView<ObjectTx> objectsTree;
	@FXML
	private TreeView<FileTx> filesTree;
	private TreeItem<ObjectTx> texturePackFolderObj;
	private TreeItem<FileTx> texturePackFolder;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.texturePack = texturePackTemp;
		this.texturePack.setObjects(Register.load(this.texturePack));
		
		on = this;
		
		addObj.setContentDisplay(ContentDisplay.TOP);
		addObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/add.png"))));
		importObj.setContentDisplay(ContentDisplay.TOP);
		importObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/import.png"))));
		
		nameFile.setCellValueFactory(new PropertyValueFactory<FileTx, String>("name"));
		typeFile.setCellValueFactory(new PropertyValueFactory<FileTx, String>("type"));
		activatedFile.setCellValueFactory(new PropertyValueFactory<FileTx, Boolean>("activated"));
		problemsFile.setCellValueFactory(new PropertyValueFactory<FileTx, String>("problems"));

		nameObj.setCellValueFactory(new PropertyValueFactory<ObjectTx, String>("name"));
		derivationObj.setCellValueFactory(new PropertyValueFactory<ObjectTx, String>("derivation"));
		typeObj.setCellValueFactory(new PropertyValueFactory<ObjectTx, String>("type"));
		activatedObj.setCellValueFactory(new PropertyValueFactory<ObjectTx, Boolean>("activated"));
		problemsObj.setCellValueFactory(new PropertyValueFactory<ObjectTx, String>("problems"));
		
		refreshTreeObj();
		refreshTreeFiles();
		
		listFiles.setRowFactory( tv -> {
		    TableRow<FileTx> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	row.getItem().open(this);
		        }
		    });
		    return row ;
		});
		listObjects.setRowFactory( tv -> {
		    TableRow<ObjectTx> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	row.getItem().open();
		        }
		    });
		    return row ;
		});
		
		checkList();
	}
	public void selectObj() {
		TreeItem<ObjectTx> dir = objectsTree.getSelectionModel().getSelectedItem();
		if(dir != null) {
			listObjects.getItems().clear();
			if(dir.getChildren().isEmpty() && dir != texturePackFolderObj)
				dir = dir.getParent();
			refreshObj(dir);
		}
	}
	public void refreshObj(TreeItem<ObjectTx> parent) {
		if(parent != null)
			for(TreeItem<ObjectTx> obj : parent.getChildren()) {
				if(obj.getChildren().isEmpty()) {
					listObjects.getItems().add(obj.getValue());
				} else {
					refreshObj(obj);
				}
			}
	}
	public void refreshTreeObj() {
		ObjectTx objectsFolder = new ObjectTx(this.texturePack.getName(), null, null, new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/directory.png"))));
		texturePackFolderObj = new TreeItem<>(objectsFolder, objectsFolder.getIcon());
		objectsTree.setRoot(texturePackFolderObj);
		
		texturePackFolderObj.getChildren().clear();
		for(TextureObject obj : this.texturePack.getObjects().getList()) {
			TreeItem<ObjectTx> derivation = getDer(obj.getDerivation());
			if(derivation == null) {
				TreeItem<ObjectTx> newDer = new TreeItem<>(new ObjectTx(obj.getDerivation(), obj.getDerivation(), null, null), new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/directory.png"))));
				texturePackFolderObj.getChildren().add(newDer);
				newDer.setExpanded(true);
				derivation = newDer;
			}
			TreeItem<ObjectTx> type = getType(derivation, obj.getType());
			if(type == null) {
				TreeItem<ObjectTx> newType = new TreeItem<>(new ObjectTx(obj.getType(), obj.getDerivation(), obj.getType(), null), new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/directory.png"))));
				derivation.getChildren().add(newType);
				type = newType;
			}
			if(obj.getType().equals("Block")) {
				BlockTx block = new BlockTx((Block) obj);
				type.getChildren().add(new TreeItem<ObjectTx>(block, block.getIcon()));
			}
		}
		texturePackFolderObj.setExpanded(true);
		MultipleSelectionModel<?> msm = this.objectsTree.getSelectionModel();
		int row = this.objectsTree.getRow(texturePackFolderObj);
		msm.select(row);
		refreshObj(texturePackFolderObj);
	}
	private TreeItem<ObjectTx> getDer(String derivation) {
		for(TreeItem<ObjectTx> der : texturePackFolderObj.getChildren()) {
			if(der.getValue().getDerivation().equals(derivation))
				return der;
		}
		return null;
	}
	private TreeItem<ObjectTx> getType(TreeItem<ObjectTx> derivation, String type) {
		for(TreeItem<ObjectTx> typeDer : derivation.getChildren()) {
			if(typeDer.getValue().getType().equals(type))
				return typeDer;
		}
		return null;
	}
	public void selectFile() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		if(selected != null) {
			if(selected.getChildren().isEmpty()) {
				refreshFiles(selected.getParent());
				this.listFiles.getSelectionModel().select(selected.getValue());
			} else {
				refreshFiles(selected);
			}
		} else {
			directoryLabel.setText("No directory");
		}
	}
	public void refreshFiles(TreeItem<FileTx> parent) {
		filesOblist.clear();
		for(TreeItem<FileTx> file : parent.getChildren()) {
			filesOblist.add(file.getValue());
		}
		listFiles.setItems(filesOblist);
		directoryLabel.setText(parent.getValue().getPath().substring(buildTexture.length()));
	}
	public void refreshTreeFiles() {
		DirectoryTx directorySource = new DirectoryTx(buildTexture + this.texturePack.getName(), true, null); 
		texturePackFolder = new TreeItem<>(directorySource, directorySource.getIcon());
		directorySource.setLocation(texturePackFolder);
		filesTree.setRoot(texturePackFolder);
		
		texturePackFolder.getChildren().clear();
		listFiles(texturePackFolder, (DirectoryTx) texturePackFolder.getValue(), 0);
		texturePackFolder.setExpanded(true);
		MultipleSelectionModel<?> msm = this.filesTree.getSelectionModel();
		int row = this.filesTree.getRow(texturePackFolder);
		msm.select(row);
		refreshFiles(texturePackFolder);
	}
	public void listFiles(TreeItem<FileTx> parent, DirectoryTx source, int level) {
		parent.getChildren().clear();
		for(File file : source.listFiles()) {
			try {
				TreeItem<FileTx> fileItem;
				SaveTx saved = null;
				if(this.texturePack.getObjects().getFiles() != null)
					saved = this.texturePack.getObjects().getFiles().get(file.getPath());
				if(file.isDirectory()) {
					DirectoryTx newDirectory;
					if(saved != null) {
						newDirectory = new DirectoryTx(file.getPath(), saved);
					} else {
						newDirectory = new DirectoryTx(file.getPath(), true, null);
					}
					
					fileItem = new TreeItem<>(newDirectory, newDirectory.getIcon());
					newDirectory.setLocation(fileItem);
					parent.getChildren().add(fileItem);
					listFiles(fileItem, newDirectory, level + 1);
					if(level <= 1) {
						fileItem.setExpanded(true);
					}
				} else {
					if(saved != null && saved instanceof SavePNG) {
						FilePNG newFile = new FilePNG(file.getPath(), (SavePNG) saved);
						fileItem = new TreeItem<>(newFile, newFile.getIcon());
					} else if(saved != null && saved instanceof SaveJSON) {
						FileJSON newFile = new FileJSON(file.getPath(), (SaveJSON) saved);
						fileItem = new TreeItem<>(newFile, newFile.getIcon());
					} else if(saved != null && saved instanceof SavePROPERTIES) {
						FilePROPERTIES newFile = new FilePROPERTIES(file.getPath(), (SavePROPERTIES) saved);
						fileItem = new TreeItem<>(newFile, newFile.getIcon());
					} else if(saved != null && saved instanceof SaveMCMETA) {
						FileMCMETA newFile = new FileMCMETA(file.getPath(), (SaveMCMETA) saved);
						fileItem = new TreeItem<>(newFile, newFile.getIcon());
					} else if(saved != null && saved instanceof SaveTXT) {
						FileTXT newFile = new FileTXT(file.getPath(), (SaveTXT) saved);
						fileItem = new TreeItem<>(newFile, newFile.getIcon());
					} else if(saved != null && saved instanceof SaveTx) {
						FileTx newFile = new FileTx(file.getPath(), (SaveTx) saved, null);
						fileItem = new TreeItem<>(newFile, newFile.getIcon());
					} else {
						String extension = file.getName().substring(file.getName().lastIndexOf("."));
						if(extension.equals(".png")) {
							FilePNG newFile = new FilePNG(file.getPath(), true);
							fileItem = new TreeItem<>(newFile, newFile.getIcon());
						} else if(extension.equals(".json")) {
							FileJSON newFile = new FileJSON(file.getPath(), true);
							fileItem = new TreeItem<>(newFile, newFile.getIcon());
						} else if(extension.equals(".properties")) {
							FilePROPERTIES newFile = new FilePROPERTIES(file.getPath(), true);
							fileItem = new TreeItem<>(newFile, newFile.getIcon());
						} else if(extension.equals(".mcmeta")) {
							FileMCMETA newFile = new FileMCMETA(file.getPath(), true);
							fileItem = new TreeItem<>(newFile, newFile.getIcon());
						} else if(extension.equals(".txt")) {
							FileTXT newFile = new FileTXT(file.getPath(), true);
							fileItem = new TreeItem<>(newFile, newFile.getIcon());
						} else {
							FileTx newFile = new FileTx(file.getPath(), extension, true, null);
							fileItem = new TreeItem<>(newFile, newFile.getIcon());
							fileItem.setExpanded(true);	
						}
					}
					parent.getChildren().add(fileItem);
				}
			} catch(NullPointerException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MENU", "Fail to load a file in texture pack - NullPointerException in Menu.Files.load", ConsoleLine.ERROR, exc));
			} catch(ClassCastException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MENU", "Fail to load a file in texture pack - ClassCastException in Menu.Files.load", ConsoleLine.ERROR, exc));
			} catch(Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MENU", "Fail to load a file in texture pack - Exception in Menu.Files.load", ConsoleLine.ERROR, exc));
			}
		}
	}
	
	public void addObj() {
		Blockstate blockstate = new Blockstate(new File(resource + "default_file\\block\\blockstate.json"), "block");
		try {
			blockstate.build(Blockstate.readJsonFile(blockstate.getFile()));
		} catch (FileNotFoundException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MENU", "Fail to load a default blockstate - FileNotFoundException in Menu.Object.Blockstate.load", ConsoleLine.ERROR, exc));
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("MENU", "Fail to load a default blockstate - IOException in Menu.Object.Blockstate.load", ConsoleLine.ERROR, exc));
		}
		ArrayList<Model> models = new ArrayList<>();
		for(Model model : blockstate.getModelsUD()) {
			boolean check = true;
			for(Model model2 : models) {
				if(model.equals(model2, "minecraft"))
					check = false;
			}
			if(check) {
				String derivation;
				if(model.getDerivation() == null)
					derivation = "minecraft";
				else
					derivation = model.getDerivation();
				try {
					model.setFile(new File(resource + "default_file\\block\\models\\" + derivation + "\\" + model.getPath() + ".json"));
					model.build(Model.readJsonFile(model.getFile()));
				} catch (FileNotFoundException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MENU", "Fail to load a default model \"" + model.getValue() + "\" - FileNotFoundException in Menu.Object.Model.load", ConsoleLine.ERROR, exc));
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("MENU", "Fail to load a default model \"" + model.getValue() + "\" - IOException in Menu.Object.Model.load", ConsoleLine.ERROR, exc));
				}
				models.add(model);
			}
		}
		ArrayList<Texture> textures = new ArrayList<>();
		for(Model model : models) {
			if(model.getTextures() != null)
				for(String key : model.getTextures().keySet()) {
					Texture texture = model.getTextures().get(key);
					boolean check = true;
					for(Texture texture2 : textures) {
						if(texture.equals(texture2, "minecraft"))
							check = false;
					}
					if(check) {
						String derivation;
						if(texture.getDerivation() == null)
							derivation = "minecraft";
						else
							derivation = model.getDerivation();
						texture.setFile(new File(resource + "default_file\\block\\textures\\" + derivation + "\\" + texture.getPath() + ".png"));
						textures.add(texture);
					}
				}
		}
		BlockTx block = new BlockTx(new Block("minecraft", "New block", blockstate, new Model(null, "item")));
		block.open();
	}
	
	public void modifyProject() {
		CreateTexture.loadTexturePack(ManagmentStage.getOn().getTexturePack(), true);
	}
	public void openExceptionCns() {
		Main.exceptionStage.getStage().show();
	}
	public void openConsoles() {
		ManagmentOption.tab = 2;
		openSettings();
	}
	public void openDerivations() {
		ManagmentOption.tab = 1;
		openSettings();
	}
	public void openSettings() {
		if(!ManagmentOption.options.isShowing()) {
			try {
				Parent settings = FXMLLoader.load(Main.getApl().getClass().getResource("/Settings/Settings.fxml"));
				ManagmentOption.optScene = new Scene(settings);
				ManagmentOption.options.setScene(ManagmentOption.optScene);
				ManagmentOption.options.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
				ManagmentOption.options.setTitle("Millenniar Texture - Options");
				ManagmentOption.optScene.getStylesheets().add("resource/teme/teme" + Settings.getSetting(Settings.TEME) + ".css");
				ManagmentOption.options.show();
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open Settings stage - IOException in Settings.stage", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open Settings stage - Exception in Settings.stage", ConsoleLine.CRASH, exc));
			}
		}
	}
	public void openSupport() {
		if(!Support.getSupport().isShowing()) {
			try {
				Parent support = FXMLLoader.load(Main.getApl().getClass().getResource("/Support.fxml"));
				Support.supScene = new Scene(support);
				Support.getSupport().setScene(Support.supScene);
				Support.getSupport().getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
				Support.getSupport().setTitle("Millenniar Texture - Support");
				Support.getSupport().setResizable(false);
				Support.supScene.getStylesheets().add("resource/teme/teme" + Settings.getSetting(Settings.TEME) + ".css");
				Support.getSupport().show();
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SUPPORT", "Fail to open Support stage - IOException in Support.stage", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SUPPORT", "Fail to open Support stage - Exception in Support.stage", ConsoleLine.CRASH, exc));
			}
		}
	}
	
	public void addDir() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		if(selected != null) {
			String path;
			if(selected.getValue() instanceof DirectoryTx) {
				path = selected.getValue().getPath();
			} else {
				path = selected.getParent().getValue().getPath();
				selected = selected.getParent();
			}
			String name = MSDialog.showTextInput("New Directory", "path: ", "new directory");
			if(name != null && !name.equals("")) {
				DirectoryTx newFile = new DirectoryTx(path + "\\" + name, true, selected);
				TreeItem<FileTx> fileItem = new TreeItem<>(newFile, newFile.getIcon());
				selected.getChildren().add(fileItem);
				
				newFile.mkdir();
				selectFile();
			}
		}
	}
	public void addPng() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		if(selected != null) {
			String path;
			if(selected.getValue() instanceof DirectoryTx) {
				path = selected.getValue().getPath();
			} else {
				path = selected.getParent().getValue().getPath();
				selected = selected.getParent();
			}
			String name = MSDialog.showTextInput("New File PNG", "path: ", "new image");
			if(name != null && !name.equals("")) {
				FilePNG newFile = new FilePNG(path + "\\" + name + ".png", true);
				TreeItem<FileTx> fileItem = new TreeItem<>(newFile, newFile.getIcon());
				selected.getChildren().add(fileItem);
				
				try {
					ImageIO.write(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), "png", newFile);
				} catch(FileNotFoundException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create PNG file \"" + newFile + "\" - FileNotFoundException in FilePNG.delete", ConsoleLine.FATAL, exc));
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create PNG file \"" + newFile + "\" - IOException in FilePNG.delete", ConsoleLine.FATAL, exc));
				}
				selectFile();
			}
		}
	}
	public void addTxt() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		if(selected != null) {
			String path;
			if(selected.getValue() instanceof DirectoryTx) {
				path = selected.getValue().getPath();
			} else {
				path = selected.getParent().getValue().getPath();
				selected = selected.getParent();
			}
			String name = MSDialog.showTextInput("New File TXT", "path: ", "new text");
			if(name != null && !name.equals("")) {
				FileTXT newFile = new FileTXT(path + "\\" + name + ".txt", true);
				TreeItem<FileTx> fileItem = new TreeItem<>(newFile, newFile.getIcon());
				selected.getChildren().add(fileItem);
				
				try {
					FileWriter writer = new FileWriter(newFile);
					writer.write("");
					writer.close();
				} catch(FileNotFoundException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create TXT file \"" + newFile + "\" - FileNotFoundException in FileTXT.delete", ConsoleLine.FATAL, exc));
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create TXT file \"" + newFile + "\" - IOException in FileTXT.delete", ConsoleLine.FATAL, exc));
				}
				selectFile();
			}
		}
	}
	public void addJson() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		if(selected != null) {
			String path;
			if(selected.getValue() instanceof DirectoryTx) {
				path = selected.getValue().getPath();
			} else {
				path = selected.getParent().getValue().getPath();
				selected = selected.getParent();
			}
			String name = MSDialog.showTextInput("New File JSON", "path: ", "new json");
			if(name != null && !name.equals("")) {
				FileJSON newFile = new FileJSON(path + "\\" + name + ".json", true);
				TreeItem<FileTx> fileItem = new TreeItem<>(newFile, newFile.getIcon());
				selected.getChildren().add(fileItem);
				
				try {
					FileWriter writer = new FileWriter(newFile);
					writer.write("{\n	\n}");
					writer.close();
				} catch(FileNotFoundException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create JSON file \"" + newFile + "\" - FileNotFoundException in FileJSON.delete", ConsoleLine.FATAL, exc));
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create JSON file \"" + newFile + "\" - IOException in FileJSON.delete", ConsoleLine.FATAL, exc));
				}
				selectFile();
			}
		}
	}
	public void addMcmeta() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		if(selected != null) {
			String path;
			if(selected.getValue() instanceof DirectoryTx) {
				path = selected.getValue().getPath();
			} else {
				path = selected.getParent().getValue().getPath();
				selected = selected.getParent();
			}
			String name = MSDialog.showTextInput("New File MCMETA", "path: ", "new mcmeta");
			if(name != null && !name.equals("")) {
				FileMCMETA newFile = new FileMCMETA(path + "\\" + name + ".mcmeta", true);
				TreeItem<FileTx> fileItem = new TreeItem<>(newFile, newFile.getIcon());
				selected.getChildren().add(fileItem);
				
				try {
					FileWriter writer = new FileWriter(newFile);
					writer.write("{\n	\n}");
					writer.close();
				} catch(FileNotFoundException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create MCMETA file \"" + newFile + "\" - FileNotFoundException in FileMCMETA.delete", ConsoleLine.FATAL, exc));
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create MCMETA file \"" + newFile + "\" - IOException in FileMCMETA.delete", ConsoleLine.FATAL, exc));
				}
				selectFile();
			}
		}
	}
	public void addProperties() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		if(selected != null) {
			String path;
			if(selected.getValue() instanceof DirectoryTx) {
				path = selected.getValue().getPath();
			} else {
				path = selected.getParent().getValue().getPath();
				selected = selected.getParent();
			}
			String name = MSDialog.showTextInput("New File PROPERTIES", "path: ", "new properties");
			if(name != null && !name.equals("")) {
				FilePROPERTIES newFile = new FilePROPERTIES(path + "\\" + name + ".properties", true);
				TreeItem<FileTx> fileItem = new TreeItem<>(newFile, newFile.getIcon());
				selected.getChildren().add(fileItem);
				
				try {
					FileWriter writer = new FileWriter(newFile);
					writer.write("");
					writer.close();
				} catch(FileNotFoundException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create PROPERTIES file \"" + newFile + "\" - FileNotFoundException in FilePROPERTIES.delete", ConsoleLine.FATAL, exc));
				} catch (IOException exc) {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to create PROPERTIES file \"" + newFile + "\" - IOException in FilePROPERTIES.delete", ConsoleLine.FATAL, exc));
				}
				selectFile();
			}
		}
	}
	public void copyFile() {
		if(filesTree.getSelectionModel().getSelectedItem() != null)
			copy = filesTree.getSelectionModel().getSelectedItem().getValue();
	}
	public void pasteFile() {
		TreeItem<FileTx> directory = filesTree.getSelectionModel().getSelectedItem();
		copy:
		if(directory != null) {
			FileTx pasteDir;
			if(!(directory.getValue() instanceof DirectoryTx)) {
				directory = directory.getParent();
			}
			pasteDir = directory.getValue();
			String path = pasteDir.getPath() + "\\" + copy.getName();
			boolean delet = false;
			if(new File(path).exists()) {
				if(MSDialog.showConfirm("Copy File", "File " + copy.getName() + " already exists here, do you want to replace the file", "Yes", "No").equals("Yes")) {
					delet = true;
				} else {
					break copy;
				}
			}
			TreeItem<FileTx> fileItem;
			if(copy instanceof DirectoryTx) {
				if(new CopyFile(copy.getPath(), path, true).execute(true)) {
					DirectoryTx newDir = new DirectoryTx(path, copy.isActivated(), null);
					fileItem = new TreeItem<>(newDir, newDir.getIcon());
					newDir.setLocation(fileItem);
					directory.getChildren().add(fileItem);
					if(delet) {
						listFiles(directory, (DirectoryTx) directory.getValue(), 2);
						refreshFiles(directory);
					}
				}
			} else {
				if(new CopyFile(copy.getPath(), path, true).execute(true)) {
					if(copy instanceof FilePNG) {
						FilePNG changeFile = new FilePNG(path, copy.isActivated());
						fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
					} else if(copy instanceof FileJSON) {
						FileJSON changeFile = new FileJSON(path, copy.isActivated());
						fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
					} else if(copy instanceof FilePROPERTIES) {
						FilePROPERTIES changeFile = new FilePROPERTIES(path, copy.isActivated());
						fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
					} else if(copy instanceof FileMCMETA) {
						FileMCMETA changeFile = new FileMCMETA(path, copy.isActivated());
						fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
					} else if(copy instanceof FileTXT) {
						FileTXT changeFile = new FileTXT(path, copy.isActivated());
						fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
					} else {
						FileTx changeFile = new FileTx(path, copy.getType(), copy.isActivated(), null);
						fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
					}
					directory.getChildren().add(fileItem);
					if(delet) {
						listFiles(directory, (DirectoryTx) directory.getValue(), 2);
						refreshFiles(directory);
					}
				}
			}
		}
	}
	public void modifyFile() {
		if(filesTree.getSelectionModel().getSelectedItem() != null)
			filesTree.getSelectionModel().getSelectedItem().getValue().open(this);
	}
	public void deletFile() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		if(selected != null) {
			if(MSDialog.showConfirm("Delete File", "Are you sure to delete this file, you can not get back the file", "Yes", "No").equals("Yes")) {
				if(selected.getValue().deleteTx()) {
					selected.getParent().getChildren().remove(selected);
					selectFile();
				} else {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to delete file \"" + selected.getParent() + "\"", ConsoleLine.FATAL));
				}
			}
		}
	}
	public void renameFile() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		if(selected != null) {
			TreeItem<FileTx> parent = selected.getParent();
			FileTx selectedFile = selected.getValue();
			String extension = selectedFile.getPath().substring(selectedFile.getPath().lastIndexOf(".") + 1);
			
			String newName = MSDialog.showTextInput("Rename File", "Name: ", selectedFile.getName());
			if(newName != null && !newName.equals("")) {
				File newFile = new File(selectedFile.getPath().substring(0, selectedFile.getPath().lastIndexOf("\\") + 1) + newName);
				if(selectedFile instanceof DirectoryTx) {
					selectedFile.renameTo(newFile);
					DirectoryTx newDir = new DirectoryTx(newFile.getPath(), selectedFile.isActivated(), selectedFile.getSources(), ((DirectoryTx) selectedFile).getLocation());
					TreeItem<FileTx> fileItem = new TreeItem<>(newDir, newDir.getIcon());
					parent.getChildren().remove(selected);
					parent.getChildren().add(fileItem);
					
					listFiles(fileItem, newDir, 2);
				} else {
					if(newName.contains(".") && newName.substring(newName.lastIndexOf(".") + 1).equals(extension)) {
						selectedFile.renameTo(newFile);
						FileTx file = new FileTx(newFile.getPath(), selectedFile.getType(), selectedFile.isActivated(), selectedFile.getIcon(), selectedFile.getSources());
						parent.getChildren().remove(selected);
						parent.getChildren().add(new TreeItem<>(file, file.getIcon()));
					} else {
						if(MSDialog.showConfirm("Change extension", "Are you sure to change the file extension?", "Yes", "No").equals("Yes")) {
							selectedFile.renameTo(newFile);
							String newExtension = newFile.getName().substring(newFile.getName().lastIndexOf("."));
							TreeItem<FileTx> fileItem;
							if(newExtension.equals(".png")) {
								FilePNG changeFile = new FilePNG(newFile.getPath(), selectedFile.isActivated(), selectedFile.getSources());
								fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
							} else if(newExtension.equals(".json")) {
								FileJSON changeFile = new FileJSON(newFile.getPath(), selectedFile.isActivated(), selectedFile.getSources());
								fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
							} else if(newExtension.equals(".properties")) {
								FilePROPERTIES changeFile = new FilePROPERTIES(newFile.getPath(), selectedFile.isActivated(), selectedFile.getSources());
								fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
							} else if(newExtension.equals(".mcmeta")) {
								FileMCMETA changeFile = new FileMCMETA(newFile.getPath(), selectedFile.isActivated(), selectedFile.getSources());
								fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
							} else if(newExtension.equals(".txt")) {
								FileTXT changeFile = new FileTXT(newFile.getPath(), selectedFile.isActivated(), selectedFile.getSources());
								fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
							} else {
								FileTx changeFile = new FileTx(newFile.getPath(), newExtension, selectedFile.isActivated(), null, selectedFile.getSources());
								fileItem = new TreeItem<>(changeFile, changeFile.getIcon());
								fileItem.setExpanded(true);	
							}
							parent.getChildren().remove(selected);
							parent.getChildren().add(fileItem);
						}
					}
				}
			}
		}
	}
	public void importFile() {
		TreeItem<FileTx> selected = filesTree.getSelectionModel().getSelectedItem();
		TreeItem<FileTx> imported;
		TreeItem<FileTx> source;
		String dest;
		if(selected == null) {
			dest = buildTexture + this.getTexturePack().getName() + "\\";
			source = texturePackFolder;
		} else if(selected.getValue() instanceof DirectoryTx) {
			dest = selected.getValue().getPath() + "\\";
			source = selected;
		} else {
			dest = selected.getParent().getValue().getPath() + "\\";
			source = selected.getParent();
		}
		if(MSDialog.showConfirm("File type", "Choose the type of the file to import", "File", "Directory").equals("Directory")) {
			DirectoryChooser DirectoryChooser = new DirectoryChooser();
			File selectedFile = DirectoryChooser.showDialog(this.stage);
			if (selectedFile != null) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.DEBUG));
				if(new CopyFile(selectedFile.getPath(), dest + selectedFile.getName(), true).execute(true)) {
					DirectoryTx newDirectory = new DirectoryTx(dest + selectedFile.getName(), true, source);
					imported = new TreeItem<>(newDirectory, newDirectory.getIcon());
					source.getChildren().add(imported);
					listFiles(imported, newDirectory, 5);
					source.setExpanded(true);
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOSER", "Import directory path \"" + selectedFile.getPath() + "\" in \"" + dest + selectedFile.getName() + "\"", ConsoleLine.INFO));
				}
			}
		} else {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open import File");
			fileChooser.getExtensionFilters().addAll(
			        new ExtensionFilter("All Files", "*.*"));
			File selectedFile = fileChooser.showOpenDialog(this.stage);
			if (selectedFile != null) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.DEBUG));
				if(new CopyFile(selectedFile.getPath(), dest + selectedFile.getName(), true).execute(true)) {
					String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
					if(extension.equals(".png")) {
						FilePNG newFile = new FilePNG(dest + selectedFile.getName(), true);
						imported = new TreeItem<>(newFile, newFile.getIcon());
					} else if(extension.equals(".json")) {
						FileJSON newFile = new FileJSON(dest + selectedFile.getName(), true);
						imported = new TreeItem<>(newFile, newFile.getIcon());
					} else if(extension.equals(".properties")) {
						FilePROPERTIES newFile = new FilePROPERTIES(dest + selectedFile.getName(), true);
						imported = new TreeItem<>(newFile, newFile.getIcon());
					} else if(extension.equals(".mcmeta")) {
						FileMCMETA newFile = new FileMCMETA(dest + selectedFile.getName(), true);
						imported = new TreeItem<>(newFile, newFile.getIcon());
					} else if(extension.equals(".txt")) {
						FileTXT newFile = new FileTXT(dest + selectedFile.getName(), true);
						imported = new TreeItem<>(newFile, newFile.getIcon());
					} else {
						FileTx newFile = new FileTx(dest + selectedFile.getName(), extension, true, null);
						imported = new TreeItem<>(newFile, newFile.getIcon());
					}
					source.getChildren().add(imported);
					source.setExpanded(true);
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOSER", "Import file path \"" + selectedFile.getPath() + "\" in \"" + dest + selectedFile.getName() + "\"", ConsoleLine.INFO));
				}
			}
		}
	}
	public void exportFile() {
		if(filesTree.getSelectionModel().getSelectedItem() != null) {
			FileTx choose = filesTree.getSelectionModel().getSelectedItem().getValue();
			String extension;
			try {
				extension = "*" + choose.getName().substring(choose.getName().lastIndexOf("."));
			} catch(StringIndexOutOfBoundsException exc) {
				extension = "*.*";
			}
			FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Save");
		    fileChooser.getExtensionFilters().addAll(
		    		new ExtensionFilter("Selected File", extension),
		    		new ExtensionFilter("All Files", "*.*"));
		    fileChooser.setInitialFileName(choose.getName());
		    File file = fileChooser.showSaveDialog(this.stage);
			if(file != null) {
				ConsoleStage.correntStage.printConsole(new ConsoleLine("CHOSER", "Chose file path \"" + file.getPath() + "\"", ConsoleLine.DEBUG));
				new CopyFile(choose.getPath(), file.getPath(), true).execute(true);
			}
		}
	}
	
	public void changeActivated(CellEditEvent<FileTx, Boolean> event) {
		FileTx filex = event.getRowValue();
		filex.setActivated(event.getNewValue());
	}
	
	public void checkList() {
		if(listObjects.getSelectionModel().getSelectedItem() == null) {
			duplicateObj.setContentDisplay(ContentDisplay.TOP);
			duplicateObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/modify_grey.png"))));
			modifyObj.setContentDisplay(ContentDisplay.TOP);
			modifyObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/modify_grey.png"))));
			deletObj.setContentDisplay(ContentDisplay.TOP);
			deletObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/delet_grey.png"))));
			exportObj.setContentDisplay(ContentDisplay.TOP);
			exportObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/export_grey.png"))));
		} else {
			duplicateObj.setContentDisplay(ContentDisplay.TOP);
			duplicateObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/add.png"))));
			modifyObj.setContentDisplay(ContentDisplay.TOP);
			modifyObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/modify.png"))));
			deletObj.setContentDisplay(ContentDisplay.TOP);
			deletObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/delet.png"))));
			exportObj.setContentDisplay(ContentDisplay.TOP);
			exportObj.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/export.png"))));
		}
	}
	
	public TexturePack getTexturePack() {
		return texturePack;
	}
	public void setTexturePack(TexturePack texturePack) {
		this.texturePack = texturePack;
	}
	public TreeView<FileTx> getFilesTree() {
		return filesTree;
	}
	public void setFilesTree(TreeView<FileTx> filesTree) {
		this.filesTree = filesTree;
	}
	public TabPane getFilesTabPane() {
		return filesTabPane;
	}
	public TabPane getObjectsTabPane() {
		return objectsTabPane;
	}
	public static ManagmentStage getOn() {
		return on;
	}
	protected static void setOn(ManagmentStage on) {
		if(ManagmentStage.on != null)
			ManagmentStage.on.stage.close();
		ManagmentStage.on = on;
	}
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
