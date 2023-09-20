package io.github.MillenniarSt.MillenniarTexture.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.CreateTexture.CreateTexture;
import io.github.MillenniarSt.MillenniarTexture.CreateTexture.Register;
import io.github.MillenniarSt.MillenniarTexture.CreateTexture.TexturePack;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
import io.github.MillenniarSt.MillenniarTexture.Dialog.Support;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.DeleteFile;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.ZipFileProcess;
import io.github.MillenniarSt.MillenniarTexture.Menu.ManagmentStage;
import io.github.MillenniarSt.MillenniarTexture.Settings.ManagmentOption;
import io.github.MillenniarSt.MillenniarTexture.Settings.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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

public class Controller implements Initializable, ProgrammePath {
	
	private final ObservableList<TexturePack> texturePacks = FXCollections.observableArrayList();
	
	@FXML
	private GridPane grid;
	@FXML
	private TableView<TexturePack> listTexturePacks;
	@FXML
	private TableColumn<TexturePack, String> versionTp;
	@FXML
	private TableColumn<TexturePack, String> nameTp;
	@FXML
	private TableColumn<TexturePack, String> descriptionTp;
	@FXML
	private Button load;
	@FXML
	private Button modify;
	@FXML
	private Button delete;
	@FXML
	private Button install;
	@FXML
	private Button uninstall;

	public void addTexture() {
		CreateTexture.loadNewTexturePack();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		grid.getStyleClass().add("pane");
		
		versionTp.setCellValueFactory(new PropertyValueFactory<TexturePack, String>("versionString"));
		nameTp.setCellValueFactory(new PropertyValueFactory<TexturePack, String>("name"));
		descriptionTp.setCellValueFactory(new PropertyValueFactory<TexturePack, String>("description"));
		checkList();

		texturePacks.addAll(TexturePack.textures);
		listTexturePacks.setItems(texturePacks);
	}
	
	public void settings() {
		if(!ManagmentOption.options.isShowing()) {
			try {
				Parent settings = FXMLLoader.load(Main.getLayout("Settings/Settings"));
				ManagmentOption.optScene = new Scene(settings);
				
				ManagmentOption.options.setScene(ManagmentOption.optScene);
				ManagmentOption.options.getIcons().add(Main.getResource("icon"));
				ManagmentOption.options.setTitle("Millenniar Texture - Options");
				ManagmentOption.optScene.getStylesheets().add(Settings.getTheme());
				ManagmentOption.options.show();
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open io.github.MillenniarSt.MillenniarTexture.Settings stage - IOException in io.github.MillenniarSt.MillenniarTexture.Settings.stage", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open io.github.MillenniarSt.MillenniarTexture.Settings stage - Exception in io.github.MillenniarSt.MillenniarTexture.Settings.stage", ConsoleLine.CRASH, exc));
			}
		}
	}
	
	public void support() {
		Support.show();
	}
	
	public void excConsole() {
		Main.exceptionStage.getStage().show();
	}
	
	public void load() {
		if(listTexturePacks.getSelectionModel().getSelectedItem() != null) {
			try {
				ManagmentStage.texturePackTemp = listTexturePacks.getSelectionModel().getSelectedItem();
				
				Parent parentTp = FXMLLoader.load(Main.getLayout("Menu/Menu"));
				Scene sceneTp = new Scene(parentTp);
				Stage stageTP = new Stage();
				
				ManagmentStage.getOn().setStage(stageTP);
				stageTP.setScene(sceneTp);
				stageTP.getIcons().add(Main.getResource("icon"));
				stageTP.setTitle("Millenniar Texture - " + listTexturePacks.getSelectionModel().getSelectedItem().getName());
				sceneTp.getStylesheets().add(Settings.getTheme());
				stageTP.setMaximized(true);
				Main.stageApl.close();
				ManagmentStage.getOn().getStage().show();
			} catch (RuntimeException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MAIN", "Fail to open io.github.MillenniarSt.MillenniarTexture.Menu of a Texture pack stage - RuntimeException in io.github.MillenniarSt.MillenniarTexture.Main.TexturePack.stage", ConsoleLine.CRASH, exc));
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MAIN", "Fail to open io.github.MillenniarSt.MillenniarTexture.Menu of a Texture pack stage - IOException in io.github.MillenniarSt.MillenniarTexture.Main.TexturePack.stage", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MAIN", "Fail to open io.github.MillenniarSt.MillenniarTexture.Menu of a Texture pack stage - Exception in io.github.MillenniarSt.MillenniarTexture.Main.TexturePack.stage", ConsoleLine.CRASH, exc));
			}
		}
	}
	
	public void modify() {
		if(listTexturePacks.getSelectionModel().getSelectedItem() != null) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Modify texture pack \"" + listTexturePacks.getSelectionModel().getSelectedItem().getName() + "\"", ConsoleLine.INFO));
			CreateTexture.loadTexturePack(listTexturePacks.getSelectionModel().getSelectedItem(), false);
		}
	}
	@SuppressWarnings("unlikely-arg-type")
	public void delete() {
		if(listTexturePacks.getSelectionModel().getSelectedItem() != null) {
			TexturePack subject = listTexturePacks.getSelectionModel().getSelectedItem();
			if(MSDialog.showConfirm("Are you sure you want to delete this texture pack?",
					"If you delete this texture pack you will lose all data of that:\n"
					+ "	- Texture pack's files\n"
					+ "	- Texture pack's other data\n"
					+ "	- Texture pack will be uninstall",
					MSDialog.YES, MSDialog.NO).equals(MSDialog.YES)) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Starting to delete \"" + subject.getName() + "\" texture pack", ConsoleLine.INFO));
				new DeleteFile(buildTexture + subject.getName(), true).execute(true);
				File textureData = new File(data + "textureObjects/" + subject.getName() + ".dat");
				if(textureData.exists()) {
					if(textureData.delete()) {
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Data file \"" + textureData.getPath() + "\" of \"" + subject.getName() + "\" delete successfully", ConsoleLine.INFO));
					} else {
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to delete data file \"" + textureData.getPath() + "\" of \"" + subject.getName() + "\"", ConsoleLine.ERROR));
					}
				} else {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Data file \"" + textureData.getPath() + "\" of \"" + subject.getName() + "\" not found", ConsoleLine.WARN));
				}
				TexturePack.textures.remove(subject);
				texturePacks.remove(subject);
				Register.save();
				
				if(new File(Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "resourcepacks/" + subject.getName() + ".zip").exists()) {
					uninstall(subject);
				}
				
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Delete texture pack successfully", ConsoleLine.SUCCESS));
				MSDialog.showInformation("Delete Texture pack", 
						"Delete texture pack \"" + subject.getName() + "\" successfully\n"
						+ "	- Workspace: \"" + buildTexture + subject.getName() + "\"\n"
						+ "	- Data: \"" + data + "textureObjects/" + subject.getName() + ".dat\"\n"
						+ "	- Minecraft: \"" + Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "resourcepacks/" + subject.getName() + ".zip\"");
				listTexturePacks.getItems().remove(subject);
			}
		}
	}
	public void install() {
		if(listTexturePacks.getSelectionModel().getSelectedItem() != null) {
			TexturePack subject = listTexturePacks.getSelectionModel().getSelectedItem();
			try {
				if(subject.isOptifine()) {
					MSDialog.showWarning("Texture Pack whit Optifine", "To work fully this texture pack require optifine\n"
							+ "Please, install in minecraft optifine - version " + subject.getVersionString() + " -");
				}
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Starting to install texture pack \"" + subject.getName() + "\"...",
						ConsoleLine.SUCCESS));
				new ZipFileProcess(buildTexture + subject.getName(), Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "resourcepacks/" + subject.getName() + ".zip", true).execute(true);
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Install texture pack \"" + subject.getName() + "\" successfully",
						ConsoleLine.SUCCESS));
				MSDialog.showInformation("Install Texture pack", 
						"Install texture pack \"" + subject.getName() + "\" in minecraft directory > resourcepacks successfully\n"
						+ "	- Path: \"" + Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "resourcepacks/" + subject.getName() + ".zip\"");
			} catch (NullPointerException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to install texture pack \"" + subject + "\" - NullPointerException in TexturePack.list.execute.install",
						ConsoleLine.FATAL, exc));
				MSDialog.showError("Install Texture pack", 
						"Fail to install texture pack \"" + subject + "\" in minecraft directory > resourcepacks\n"
						+ "	- Path: \"" + Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "resourcepacks/[" + subject + ".getName()].zip\"\n"
						+ "	- Object: \"" + subject + "\"\n"
						+ "	- Exception: NullPointerException:\n"
							+ "		" + exc);
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to install texture pack \"" + subject.getName() + "\" - Exception in TexturePack.list.execute.install",
						ConsoleLine.FATAL, exc));
				MSDialog.showError("Install Texture pack", 
						"Fail to install texture pack \"" + subject.getName() + "\" in minecraft directory > resourcepacks\"\n"
						+ "	- Path: \"" + Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "resourcepacks/" + subject.getName() + ".zip\n"
						+ "	- Object: \"" + subject + "\"\n"
						+ "	- Exception: Exception:\n"
							+ "		" + exc);
			}
			checkList();
		}
	}
	public void uninstall() {
		if(listTexturePacks.getSelectionModel().getSelectedItem() != null) {
			TexturePack subject = listTexturePacks.getSelectionModel().getSelectedItem();
			uninstall(subject);
			MSDialog.showInformation("Uninstall Texture pack", 
					"Uninstall texture pack \"" + subject.getName() + "\" from minecraft directory > resourcepacks successfully\n"
					+ "	- Path: \"" + Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "resourcepacks/" + subject.getName() + ".zip\"");
			checkList();
		}
	}
	public void uninstall(TexturePack texturePack) {
		ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Starting to uninstall texture pack \"" + texturePack.getName() + "\"...",
				ConsoleLine.INFO));
		File textureToDelete = new File(Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "resourcepacks/" + texturePack.getName() + ".zip");
		if(textureToDelete.delete()) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Uninstall texture pack \"" + texturePack.getName() + "\" successfully",
					ConsoleLine.SUCCESS));
		} else {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to uninstall texture pack \"" + texturePack.getName() + "\"",
					ConsoleLine.FATAL));
		}
		checkList();
	}
	
	public void checkList() {
		if(listTexturePacks.getSelectionModel().getSelectedItem() == null) {
			load.setContentDisplay(ContentDisplay.TOP);
			load.setGraphic(new ImageView(Main.getResource("button/load")));
			modify.setContentDisplay(ContentDisplay.TOP);
			modify.setGraphic(new ImageView(Main.getResource("button/modify_grey")));
			delete.setContentDisplay(ContentDisplay.TOP);
			delete.setGraphic(new ImageView(Main.getResource("button/delete_grey")));
			install.setContentDisplay(ContentDisplay.TOP);
			install.setGraphic(new ImageView(Main.getResource("button/install_grey")));
			uninstall.setContentDisplay(ContentDisplay.TOP);
			uninstall.setGraphic(new ImageView(Main.getResource("button/uninstall_grey")));
		} else if(!new File(Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "resourcepacks/" + listTexturePacks.getSelectionModel().getSelectedItem().getName() + ".zip").exists()) {
			load.setContentDisplay(ContentDisplay.TOP);
			load.setGraphic(new ImageView(Main.getResource("button/load")));
			modify.setContentDisplay(ContentDisplay.TOP);
			modify.setGraphic(new ImageView(Main.getResource("button/modify")));
			delete.setContentDisplay(ContentDisplay.TOP);
			delete.setGraphic(new ImageView(Main.getResource("button/delete")));
			install.setContentDisplay(ContentDisplay.TOP);
			install.setGraphic(new ImageView(Main.getResource("button/install")));
			uninstall.setContentDisplay(ContentDisplay.TOP);
			uninstall.setGraphic(new ImageView(Main.getResource("button/uninstall_grey")));
		} else {
			load.setContentDisplay(ContentDisplay.TOP);
			load.setGraphic(new ImageView(Main.getResource("button/load")));
			modify.setContentDisplay(ContentDisplay.TOP);
			modify.setGraphic(new ImageView(Main.getResource("button/modify")));
			delete.setContentDisplay(ContentDisplay.TOP);
			delete.setGraphic(new ImageView(Main.getResource("button/delete")));
			install.setContentDisplay(ContentDisplay.TOP);
			install.setGraphic(new ImageView(Main.getResource("button/install")));
			uninstall.setContentDisplay(ContentDisplay.TOP);
			uninstall.setGraphic(new ImageView(Main.getResource("button/uninstall")));
		}
	}
}
