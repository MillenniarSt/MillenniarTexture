package io.github.MillenniarSt.MillenniarTexture.Settings;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Console.Console;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;
import io.github.MillenniarSt.MillenniarTexture.Dialog.Support;
import io.github.MillenniarSt.MillenniarTexture.Menu.ManagmentStage;
import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.CopyFile;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.DeleteFile;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.ZipFileProcess;
import io.github.MillenniarSt.MillenniarTexture.ManagmentFile.UnZip;
import io.github.MillenniarSt.MillenniarTexture.CreateTexture.CreateTexture;
import io.github.MillenniarSt.MillenniarTexture.CreateTexture.TexturePack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class ManagmentOption implements Initializable, ProgrammePath {

	public static Stage options = new Stage();
	public static Scene optScene;
	public static byte tab = 0;
	
	private final ObservableList<String> listTeme = FXCollections.observableArrayList("Light", "Dark", "Modena-Light", "Modena-Dark");
	private final ObservableList<Derivation> derivationsOblist = FXCollections.observableArrayList();
	private final ObservableList<Console> consolesOblist = FXCollections.observableArrayList();
	
	@FXML
	private TabPane tabPane;

	@FXML
	private TextField fieldMinecraftDir;
	@FXML
	private ImageView checkMinecraftDir;
	@FXML
	private Button searchMinecraftDir;
	@FXML
	private TextField fieldResource;
	@FXML
	private ImageView checkResource;
	@FXML
	private Button searchResource;
	@FXML
	private Button downloadResource;
	@FXML
	private Button loadResource;
	@FXML
	private ComboBox<String> comboTeme;

	@FXML
	private TableView<Derivation> listDerivation;
	@FXML
	private TableColumn<Derivation, String> versionDer;
	@FXML
	private TableColumn<Derivation, String> nameDer;
	@FXML
	private TableColumn<Derivation, String> codeDer;
	@FXML
	private Button addDer;
	@FXML
	private Button deleteDer;
	@FXML
	private Button modifyDer;
	@FXML
	private Button importDer;
	@FXML
	private Button exportDer;

	@FXML
	private ListView<Console> listConsoles;
	@FXML
	private Button addCns;
	@FXML
	private Button deleteCns;
	@FXML
	private Button openCns;
	@FXML
	private Button importCns;
	@FXML
	private Button exportCns;
	@FXML
	private CheckBox defaultCns;
	@FXML
	private CheckBox debugMes;
	@FXML
	private CheckBox successMes;
	@FXML
	private CheckBox infoMes;
	@FXML
	private CheckBox warnMes;
	@FXML
	private CheckBox errorMes;
	@FXML
	private CheckBox fatalMes;
	@FXML
	private TextField nameCns;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tabPane.getSelectionModel().select(tab);
		comboTeme.setItems(listTeme);

		if(Settings.getSetting(Settings.MINECRAFT_DIRECTORY) != null)
			fieldMinecraftDir.setText((String) Settings.getSetting(Settings.MINECRAFT_DIRECTORY));
		else
			fieldMinecraftDir.setText("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/.minecraft/");
		
		if(Settings.getSetting(Settings.RESOURCE) != null)
			fieldResource.setText((String) Settings.getSetting(Settings.RESOURCE));
		else
			fieldResource.setText("C:/Users/" + System.getProperty("user.name") + "/Downloads/resource mt-3.0.0.zip");
		comboTeme.setValue((String) Settings.getSetting(Settings.TEME));
		
		searchMinecraftDir.setGraphic(new ImageView(Main.getResource("button/search_icon")));
		searchResource.setGraphic(new ImageView(Main.getResource("button/search_icon")));
		downloadResource.setGraphic(new ImageView(Main.getResource("button/download_icon")));
		loadResource.setGraphic(new ImageView(Main.getResource("button/load_icon")));
		
		versionDer.setCellValueFactory(new PropertyValueFactory<Derivation, String>("version"));
		nameDer.setCellValueFactory(new PropertyValueFactory<Derivation, String>("name"));
		codeDer.setCellValueFactory(new PropertyValueFactory<Derivation, String>("code"));
		refreshDer();
		
		addCns.setGraphic(new ImageView(Main.getResource("button/add")));
		importCns.setGraphic(new ImageView(Main.getResource("button/import")));
		refreshCns();
		
		checkMinecraftDir();
		checkResource();
		checkDerivation();
		checkConsole();
		tab = 0;
	}
	
	public void checkMinecraftDir() {
		if(!new File(searchResource.getText().trim()).exists() || !new File(searchResource.getText().trim()).isFile()) {
			checkMinecraftDir.setImage(Main.getResource("exception/right"));
		} else {
			checkMinecraftDir.setImage(Main.getResource("exception/file_not_found"));
		}
	}
	public void checkResource() {
		if(!new File(searchResource.getText().trim()).exists() || !new File(searchResource.getText().trim()).isFile()) {
			checkResource.setImage(Main.getResource("exception/right"));
		} else {
			checkResource.setImage(Main.getResource("exception/file_not_found"));
		}
	}
	public void choseMinecraftDir() {
		DirectoryChooser DirectoryChooser = new DirectoryChooser();
		File selectedFile = DirectoryChooser.showDialog(options);
		if (selectedFile != null) {
			fieldMinecraftDir.setText(selectedFile.getPath());
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.INFO));
		}
	}
	public void choseResource() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("ZIP Archive", "*.zip"),
		        new ExtensionFilter("All io.github.MillenniarSt.MillenniarTexture.Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(options);
		if (selectedFile != null) {
			fieldResource.setText(selectedFile.getPath());
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.INFO));
		}
	}
	public void downloadResource() {
		try {
	        Desktop.getDesktop().browse(new URI("https://cdn.discordapp.com/attachments/1029131762162471014/1090313731138920559/resource_mt-3.0.0.zip"));
	        ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Open URL \"https://cdn.discordapp.com/attachments/1029131762162471014/1090313731138920559/resource_mt-3.0.0.zip\"", ConsoleLine.INFO));
	    } catch (URISyntaxException exc) {
	    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Fail to open URL \"https://cdn.discordapp.com/attachments/1029131762162471014/1090313731138920559/resource_mt-3.0.0.zip\" - URISyntaxException in io.github.MillenniarSt.MillenniarTexture.Settings.resource.download", ConsoleLine.FATAL, exc));
	    } catch (IOException exc) {
	    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("URL", "Fail to open URL \"https://cdn.discordapp.com/attachments/1029131762162471014/1090313731138920559/resource_mt-3.0.0.zip\" - IOException in io.github.MillenniarSt.MillenniarTexture.Settings.resource.download", ConsoleLine.ERROR, exc));
	    }
	}
	
	public void loadResource() {
		new UnZip(fieldResource.getText().trim(), resource, true).execute(true);
			MSDialog.showInformation("Millenniar Texture - Resource", "The programme resourece was installed successfully in:\n" + resource);
	}
	
	@SuppressWarnings("unchecked")
	public void refreshDer() {
		derivationsOblist.clear();
		derivationsOblist.addAll((ArrayList<Derivation>) Settings.getSetting(Settings.DERIVATION));
		listDerivation.setItems(derivationsOblist);
	}
	
	@SuppressWarnings("unchecked")
	public void addDer() {
		String chose = MSDialog.showConfirm("Type of derivation", "Select an option based on the type of file or directory\nfrom which the lineage will be extracted", "Minecraft", "Jar file", "Zip file", "Directory");
		File file = null;

        switch (chose) {
            case "Minecraft" -> {
                MSDialog.showWarning("Jar derivation Beta", "Derivations which are extracted from mods .jar archives may be extracted incorrectly, "
                        + "it is recommended to extract the files in zip archives or in directories with other programs (ex. WinRAR) before uploading them");
                file = new File((String) Settings.getSetting(Settings.MINECRAFT_DIRECTORY));
            }
            case "Jar file" -> {
                MSDialog.showWarning("Jar derivation Beta", "Derivations which are extracted from mods .jar archives may be extracted incorrectly, "
                        + "it is recommended to extract the files in zip archives or in directories with other programs (ex. WinRAR) before uploading them");
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("Jar io.github.MillenniarSt.MillenniarTexture.Files", "*.jar"),
                        new ExtensionFilter("All io.github.MillenniarSt.MillenniarTexture.Files", "*.*"));
                file = fileChooser.showOpenDialog(options);
            }
            case "Zip file" -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("Zip io.github.MillenniarSt.MillenniarTexture.Files", "*.zip"),
                        new ExtensionFilter("All io.github.MillenniarSt.MillenniarTexture.Files", "*.*"));
                file = fileChooser.showOpenDialog(options);
            }
            case "Directory" -> {
                DirectoryChooser DirectoryChooser = new DirectoryChooser();
                file = DirectoryChooser.showDialog(options);
            }
        }
		if(file != null) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOOSER", "Chose file path \"" + file.getPath() + "\"", ConsoleLine.DEBUG));
			String version = null;
			String versionCom = "";
			float versionFl = -1;
			byte versionFlext = -1;
			do {
				try {
					versionCom = MSDialog.showTextInput("Derivation's version", "Version: ", "1.20.1").trim();
					if(versionCom.lastIndexOf(".") == 2) {
						versionFl = Float.parseFloat(versionCom);
					} else {
						versionFl = Float.parseFloat(versionCom.substring(0, versionCom.lastIndexOf(".")));
						versionFlext = Byte.parseByte(versionCom.substring(versionCom.lastIndexOf(".") + 1));
					}
					version = TexturePack.versionToString(versionFl, versionFlext);
					if(version == null) {
						throw new NumberFormatException();
					}
				} catch(NumberFormatException | StringIndexOutOfBoundsException exc) {
					MSDialog.showError("Invalid version", "The input version is not valid:\nPlease use an input 1.x.x (ex. 1.20.1)");
				}
            } while(version == null);
			
			ArrayList<File> oldList = new ArrayList<>();
			if(new File(derivations + version).exists()) {
				File[] old = new File(derivations + version).listFiles();
                Collections.addAll(oldList, old);
			}

            switch (chose) {
                case "Minecraft" -> {
                    file = new File(Settings.getSetting(Settings.MINECRAFT_DIRECTORY) + "/versions/" + versionCom + "/" + versionCom + ".jar");
                    if (file.exists())
                        new UnZip(file.getPath(), version).execute(true);
                    else
                        ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to found minecraft file \"" + file.getPath() + "\"", ConsoleLine.FATAL));
                }
                case "Jar file" -> new UnZip(file.getPath(), version).execute(true);
                case "Zip file" -> new UnZip(file.getPath(), version).execute(true);
                case "Directory" ->
                        new CopyFile(file.getPath(), derivations + version + file.getName(), true).execute(true);
            }
			
			if(new File(derivations + version).exists()) {
				File[] now = new File(derivations + version).listFiles();
				for(File derivation : now) {
					if(derivation.isDirectory() && !oldList.contains(derivation)) {
						Derivation newDer;
						if(chose.equals("Minecraft")) {
							newDer = new Derivation("Minecraft", derivation.getName(), version, derivation);
						} else {
							try {
								newDer = new Derivation(file.getName().substring(0, file.getName().lastIndexOf(".")), derivation.getName(), version, derivation);
							} catch(StringIndexOutOfBoundsException exc) {
								newDer = new Derivation(file.getName(), derivation.getName(), version, derivation);
							}
						}
						derivationsOblist.add(newDer);
						((ArrayList<Derivation>) Settings.getSetting(Settings.DERIVATION)).add(newDer);
					}
				}
			} else {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "No new directory founds in \"" + file.getPath() + "\"", ConsoleLine.FATAL));
			}
			if(new File(derivations + version).exists()) {
				File[] dirs = new File(derivations + version).listFiles();
				for(File othFile : dirs) {
					if(!othFile.isDirectory()) {
						if(othFile.delete())
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete file \"" + othFile.getPath() + "\" successfully, it is not in a derivation", ConsoleLine.WARN));
						else
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete file \"" + othFile.getPath() + "\", it is not in a derivation", ConsoleLine.ERROR));
					}
				}
			}
			save();
		}
	}
	public void modifyDer() {
		if(listDerivation.getSelectionModel().getSelectedItem() != null) {
			Derivation subject = listDerivation.getSelectionModel().getSelectedItem();
			int i = derivationsOblist.indexOf(subject);
			
			String name = MSDialog.showTextInput("Derivation modify", "Name:", subject.getName()).trim();
			if(!name.isEmpty()) {
				subject.setName(name);
				derivationsOblist.set(i, subject);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void deleteDer() {
		if(listDerivation.getSelectionModel().getSelectedItem() != null) {
			Derivation subject = listDerivation.getSelectionModel().getSelectedItem();
			new DeleteFile(subject.getSource(), true).execute(true);
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete derivation \"" + subject.getName() + "\" successfully", ConsoleLine.SUCCESS));
				((ArrayList<Derivation>) Settings.getSetting(Settings.DERIVATION)).remove(subject);
				derivationsOblist.remove(subject);
				save();
		}
	}
	@SuppressWarnings("unchecked")
	public void importDer() {
		try {
			FileChooser fileChooser = new FileChooser();
			 fileChooser.setTitle("Open Resource File");
			 fileChooser.getExtensionFilters().addAll(
			         new ExtensionFilter("Zip Files", "*.zip"),
			         new ExtensionFilter("All Files", "*.*"));
			 File selectedFile = fileChooser.showOpenDialog(options);
			 if (selectedFile != null) {
				ConsoleStage.currentStage.printConsole(new ConsoleLine("CHOOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.DEBUG));
				String version = null;
				float versionFl = -1;
				byte versionFlext = -1;
				do {
					try {
						String temp = MSDialog.showTextInput("Derivation's version", "Version: ", "1.20.1").trim();
						if(temp.lastIndexOf(".") == 2) {
							versionFl = Float.parseFloat(temp);
						} else {
							versionFl = Float.parseFloat(temp.substring(0, temp.lastIndexOf(".")));
							versionFlext = Byte.parseByte(temp.substring(temp.lastIndexOf(".") + 1));
						}
						version = TexturePack.versionToString(versionFl, versionFlext);
						if(version == null) {
							throw new NumberFormatException();
						}
					} catch(NumberFormatException | StringIndexOutOfBoundsException exc) {
						MSDialog.showError("Invalid version", "The input version is not valid:\nPlease use an input 1.x.x (ex. 1.20.1)");
					}
                } while(version == null);
				new UnZip(selectedFile.getPath(), version).execute(true);
				
				ArrayList<File> oldList = new ArrayList<>();
				if(new File(derivations + version).exists()) {
					File[] old = new File(derivations + version).listFiles();
                    Collections.addAll(oldList, old);
				}
				if(new File(derivations + version).exists()) {
					File[] now = new File(derivations + version).listFiles();
					for(File derivation : now) {
						if(derivation.isDirectory() && !oldList.contains(derivation)) {
							Derivation newDer;
							try {
								newDer = new Derivation(selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf(".")), derivation.getName(), version, derivation);
							} catch(StringIndexOutOfBoundsException exc) {
								newDer = new Derivation(selectedFile.getName(), derivation.getName(), version, derivation);
							}
							derivationsOblist.add(newDer);
							((ArrayList<Derivation>) Settings.getSetting(Settings.DERIVATION)).add(newDer);
						}
					}
				} else {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "No new directory founds in \"" + selectedFile.getPath() + "\"", ConsoleLine.FATAL));
				}
				File[] dirs = new File(derivations + version).listFiles();
				for(File othFile : dirs) {
					if(!othFile.isDirectory()) {
						if(othFile.delete())
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Delete file \"" + othFile.getPath() + "\" successfully, it is not in a derivation", ConsoleLine.WARN));
						else
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("MANAGMENTFILE", "Fail to delete file \"" + othFile.getPath() + "\", it is not in a derivation", ConsoleLine.ERROR));
					}
				}
				save();
			 }
		} catch(NullPointerException exc) {
			ConsoleStage.currentStage.printConsole(new ConsoleLine("SETTING", "Fail import console - NullPointerException in io.github.MillenniarSt.MillenniarTexture.Console.import", ConsoleLine.FATAL));
		}
	}
	public void exportDer() {
		if(listDerivation.getSelectionModel().getSelectedItem() != null) {
			Derivation selected = listDerivation.getSelectionModel().getSelectedItem();
			FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Save");
		    fileChooser.getExtensionFilters().addAll(
		    		new ExtensionFilter("Zip Files", "*.zip"),
		    		new ExtensionFilter("All Files", "*.*"));
		    File file = fileChooser.showSaveDialog(options);
			if(file != null) {
				ConsoleStage.currentStage.printConsole(new ConsoleLine("CHOOSER", "Chose file path \"" + file.getPath() + "\"", ConsoleLine.DEBUG));
				new ZipFileProcess(selected.getSource(), file, true).execute(true);
				/*try {
					new CopyFile(selected.getSource().getPath(), canche + "derivation/" + selected.getName() + "/" + selected.getCode(), true).execute(true);
					new ZipFileProcess(canche + "derivation/" + selected.getName(), file.getPath().substring(0, file.getPath().lastIndexOf(".")) + ".zip", true).execute(true);
				} catch(StringIndexOutOfBoundsException exc) {
					new ZipFileProcess(canche + "derivation/" + selected.getName(), file.getPath() + ".zip", true).execute(true);
				}
				if(new DeleteFile(canche + "derivation/", true).execute(true))
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Delete file \"" + canche + "derivation\\" + "\"", ConsoleLine.INFO));
				else
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to delete file \"" + canche + "derivation\\" + "\"", ConsoleLine.ERROR));*/
			}
		}
	}
	
	public void checkDerivation() {
		if(listDerivation.getSelectionModel().getSelectedItem() != null) {
			addDer.setGraphic(new ImageView(Main.getResource("button/add")));
			deleteDer.setGraphic(new ImageView(Main.getResource("button/delete")));
			modifyDer.setGraphic(new ImageView(Main.getResource("button/modify")));
			importDer.setGraphic(new ImageView(Main.getResource("button/import")));
			exportDer.setGraphic(new ImageView(Main.getResource("button/export")));
		} else {
			addDer.setGraphic(new ImageView(Main.getResource("button/add")));
			deleteDer.setGraphic(new ImageView(Main.getResource("button/delete_grey")));
			modifyDer.setGraphic(new ImageView(Main.getResource("button/modify_grey")));
			importDer.setGraphic(new ImageView(Main.getResource("button/import")));
			exportDer.setGraphic(new ImageView(Main.getResource("button/export_grey")));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void refreshCns() {
		consolesOblist.clear();
		consolesOblist.addAll((ArrayList<Console>) Settings.getSetting(Settings.CONSOLES));
		listConsoles.setItems(consolesOblist);
	}
	
	public void checkConsole() {
		if(listConsoles.getSelectionModel().getSelectedItem() != null && listConsoles.getSelectionModel().getSelectedItem().id.equals(Main.exception.id)) {
			deleteCns.setGraphic(new ImageView(Main.getResource("button/delete_grey")));
			exportCns.setGraphic(new ImageView(Main.getResource("button/export")));
			openCns.setGraphic(new ImageView(Main.getResource("button/load")));
			
			Console selected = listConsoles.getSelectionModel().getSelectedItem();
			if(selected.equals(ConsoleStage.defaultCns.getSubject()))
				defaultCns.setSelected(true);
			else
				defaultCns.setSelected(false);
			nameCns.setText(selected.getTitle());
			debugMes.setSelected(selected.isMesDebug());
			successMes.setSelected(selected.isMesSuccess());
			infoMes.setSelected(selected.isMesInfo());
			warnMes.setSelected(selected.isMesWarn());
			errorMes.setSelected(selected.isMesError());
			fatalMes.setSelected(selected.isMesFatal());
		} else if(listConsoles.getSelectionModel().getSelectedItem() != null) {
			deleteCns.setGraphic(new ImageView(Main.getResource("button/delete")));
			exportCns.setGraphic(new ImageView(Main.getResource("button/export")));
			openCns.setGraphic(new ImageView(Main.getResource("button/load")));
			
			Console selected = listConsoles.getSelectionModel().getSelectedItem();
			if(selected.equals(ConsoleStage.defaultCns.getSubject()))
				defaultCns.setSelected(true);
			else
				defaultCns.setSelected(false);
			nameCns.setText(selected.getTitle());
			debugMes.setSelected(selected.isMesDebug());
			successMes.setSelected(selected.isMesSuccess());
			infoMes.setSelected(selected.isMesInfo());
			warnMes.setSelected(selected.isMesWarn());
			errorMes.setSelected(selected.isMesError());
			fatalMes.setSelected(selected.isMesFatal());
		} else {
			deleteCns.setGraphic(new ImageView(Main.getResource("button/delete_grey")));
			exportCns.setGraphic(new ImageView(Main.getResource("button/export_grey")));
			openCns.setGraphic(new ImageView(Main.getResource("button/load_grey")));
			
			defaultCns.setSelected(false);
			nameCns.setText("");
			debugMes.setSelected(false);
			successMes.setSelected(false);
			infoMes.setSelected(false);
			warnMes.setSelected(false);
			errorMes.setSelected(false);
			fatalMes.setSelected(false);
		}
	}
	
	public void addCns() {
		new Console("New console", null, new ArrayList<ConsoleLine>());
		
		refreshCns();
		checkConsole();
	}
	@SuppressWarnings("unchecked")
	public void deleteCns() {
		if(listConsoles.getSelectionModel().getSelectedItem() != null && !listConsoles.getSelectionModel().getSelectedItem().id.equals(Main.exception.id)) {
			Console selected = listConsoles.getSelectionModel().getSelectedItem();
			((ArrayList<Console>) Settings.getSetting(Settings.CONSOLES)).remove(selected);
			
			refreshCns();
			checkConsole();
		}
	}
	public void openCns() {
		if(listConsoles.getSelectionModel().getSelectedItem() != null) {
			Console selected = listConsoles.getSelectionModel().getSelectedItem();
			ConsoleStage consoleStg = selected.getConsoleStage();
			
			if(selected.equals(Main.exception))
				consoleStg = Main.exceptionStage;
			
			try {
				if(consoleStg == null) {
					consoleStg = new ConsoleStage();
					consoleStg.setStage(new Stage());
					ConsoleStage.current = selected;
					
					Parent consolePrt = FXMLLoader.load(Main.getLayout("Console/Console"));
					Scene consoleScn = new Scene(consolePrt);
					
					consoleStg.getStage().setScene(consoleScn);
					consoleStg.getStage().getIcons().add(Main.getResource("icon"));
					consoleStg.getStage().setTitle("Millenniar Texture - io.github.MillenniarSt.MillenniarTexture.Console");
					consoleScn.getStylesheets().add(Settings.getTheme());
					
					Stage backup = consoleStg.getStage();
					consoleStg = ConsoleStage.currentStage;
					consoleStg.setStage(backup);
					
					selected.addConsoleStage(consoleStg);
				}
				consoleStg.getStage().show();
			} catch (IOException e) {
				ConsoleStage.currentStage.printConsole(new ConsoleLine("SETTING", "Fail open io.github.MillenniarSt.MillenniarTexture.Console stage - IOException in io.github.MillenniarSt.MillenniarTexture.Settings.console.stage", ConsoleLine.ERROR));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open io.github.MillenniarSt.MillenniarTexture.Console stage - Exception in io.github.MillenniarSt.MillenniarTexture.Settings.console.stage", ConsoleLine.ERROR, exc));
			}
		}
	}
	public void importCns() {
		try {
			FileChooser fileChooser = new FileChooser();
			 fileChooser.setTitle("Open Resource File");
			 fileChooser.getExtensionFilters().addAll(
			         new ExtensionFilter("io.github.MillenniarSt.MillenniarTexture.Console files", "*.cns"),
			         new ExtensionFilter("Data files", "*.dat"),
			         new ExtensionFilter("All io.github.MillenniarSt.MillenniarTexture.Files", "*.*"));
			 File selectedFile = fileChooser.showOpenDialog(options);
			 if (selectedFile != null) {
				ConsoleStage.currentStage.printConsole(new ConsoleLine("CHOOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.DEBUG));
				Console imported = Console.load(selectedFile.getPath());
				new Console(imported.getTitle(), imported.getLastTime(), imported.getRegister());
				
				refreshCns();
				checkConsole();
			 }
		} catch(NullPointerException exc) {
			ConsoleStage.currentStage.printConsole(new ConsoleLine("SETTING", "Fail import console - NullPointerException in io.github.MillenniarSt.MillenniarTexture.Console.import", ConsoleLine.FATAL));
		}
	}
	
	public void exportCns() {
		if(listConsoles.getSelectionModel().getSelectedItem() != null) {
			Console selected = listConsoles.getSelectionModel().getSelectedItem();
			FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Save");
		    fileChooser.getExtensionFilters().addAll(
		    		new ExtensionFilter("Cns io.github.MillenniarSt.MillenniarTexture.Files", "*.cns"),
		    		new ExtensionFilter("All io.github.MillenniarSt.MillenniarTexture.Files", "*.*"));
		    File file = fileChooser.showSaveDialog(options);
			if(file != null) {
				ConsoleStage.currentStage.printConsole(new ConsoleLine("CHOOSER", "Chose file path \"" + file.getPath() + "\"", ConsoleLine.DEBUG));
				if(file.exists()) {
					try {
						file.delete();
						Console.save(file.getPath().substring(0, file.getPath().lastIndexOf(".")) + ".cns", selected);
					} catch(StringIndexOutOfBoundsException exc) {
						Console.save(file.getPath() + ".cns", selected);
					}
				} else {
					try {
						Console.save(file.getPath().substring(0, file.getPath().lastIndexOf(".")) + ".cns", selected);
					} catch(StringIndexOutOfBoundsException exc) {
						Console.save(file.getPath() + ".cns", selected);
					}
				}
			}
		}
	}
	
	public void applyCns() {
		if(listConsoles.getSelectionModel().getSelectedItem() != null) {
			Console selected = listConsoles.getSelectionModel().getSelectedItem();
			ConsoleStage consoleStg = selected.getConsoleStage();
			
			if(selected.equals(Main.exception))
				consoleStg = Main.exceptionStage;
			
			if(defaultCns.isSelected()) {
				if(consoleStg == null) {
					try {
						consoleStg = new ConsoleStage();
						consoleStg.setStage(new Stage());
						ConsoleStage.current = selected;
						
						Parent consolePrt = FXMLLoader.load(Main.getLayout("Console/Console"));
						Scene consoleScn = new Scene(consolePrt);
						
						consoleStg.getStage().setScene(consoleScn);
						consoleStg.getStage().getIcons().add(Main.getResource("icon"));
						consoleStg.getStage().setTitle("Millenniar Texture - io.github.MillenniarSt.MillenniarTexture.Console");
						consoleScn.getStylesheets().add(Settings.getTheme());
						
						Stage backup = consoleStg.getStage();
						consoleStg = ConsoleStage.currentStage;
						consoleStg.setStage(backup);
						
						selected.addConsoleStage(consoleStg);
					} catch (IOException e) {
						ConsoleStage.currentStage.printConsole(new ConsoleLine("SETTING", "Fail load default console - IOException in io.github.MillenniarSt.MillenniarTexture.Settings.console.load", ConsoleLine.ERROR));
					}
				}
				ConsoleStage.defaultCns = consoleStg;
			}
			
			selected.setTitle(nameCns.getText().trim());
			consoleStg.setTitle(selected.getTitle().trim());
			
			selected.setMesDebug(debugMes.isSelected());
			selected.setMesSuccess(successMes.isSelected());
			selected.setMesInfo(infoMes.isSelected());
			selected.setMesWarn(warnMes.isSelected());
			selected.setMesError(errorMes.isSelected());
			selected.setMesFatal(fatalMes.isSelected());
			
			refreshCns();
			checkConsole();
		}
	}
	
	public void close() {
		options.close();
	}
	public void saveAndClose() {
		save();
		options.close();
	}
	public void save() {
		Settings.setSetting(Settings.MINECRAFT_DIRECTORY, fieldMinecraftDir.getText().trim());
		Settings.setSetting(Settings.RESOURCE, fieldResource.getText().trim());
		Settings.setSetting(Settings.TEME, comboTeme.getSelectionModel().getSelectedItem());
		
		if(optScene != null) {
			optScene.getStylesheets().clear();
			optScene.getStylesheets().add(Settings.getTheme());
		}
		if(Main.menu != null) {
			Main.menu.getStylesheets().clear();
			Main.menu.getStylesheets().add(Settings.getTheme());
		}
		if(CreateTexture.createTexture != null) {
			CreateTexture.createTexture.getStylesheets().clear();
			CreateTexture.createTexture.getStylesheets().add(Settings.getTheme());
		}
		if(Support.supDialog != null) {
			Support.supDialog.getStylesheets().clear();
			Support.supDialog.getStylesheets().add(Settings.getTheme());
		}
		if(Main.exceptionStage.getStage().getScene() != null) {
			Main.exceptionStage.getStage().getScene().getStylesheets().clear();
			Main.exceptionStage.getStage().getScene().getStylesheets().add(Settings.getTheme());
		}
		if(ManagmentStage.getOn() != null) {
			ManagmentStage.getOn().getStage().getScene().getStylesheets().clear();
			ManagmentStage.getOn().getStage().getScene().getStylesheets().add(Settings.getTheme());
		}
		ConsoleStage.refresh();
		
		Settings.save();
	}
}
