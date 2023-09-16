package CreateTexture;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.ResourceBundle;

import Console.ConsoleLine;
import Console.ConsoleStage;
import Main.ManagmentFile;
import Main.ProgrammePath;
import Main.Support;
import ManagmentFile.CopyFile;
import Menu.ManagmentStage;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import Main.MSDialog;
import Main.Main;

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

public class CreateTexture implements Initializable, ProgrammePath {
	
	public static Scene createTexture;
	public static TexturePack modify = null;
	private static boolean newStageOpen = false;
	
	private static Stage stageOpen;
	
	private static final String ERROR = "text-field-error";
	
	public final ObservableList<String> versionList = FXCollections.observableArrayList(
			"1.6.1 - 1.8.9", "1.9 - 1.10.2", "1.11 - 1.12.2", "1.13 - 1.14.4", "1.15 - 1.16.1", "1.16.2 - 1.16.5", "1.17.x", "1.18.x", "1.19 - 1.19.2", "1.19.3", "1.19.4", "1.20.x");
	
	@FXML
	private TextField fieldName;
	@FXML
	private TextArea fieldDescription;
	@FXML
	private TextField fieldImage;
	@FXML
	private Button choserImage;
	@FXML
	private ImageView checkName;
	@FXML
	private ImageView checkDescription;
	@FXML
	private ImageView checkImage;
	@FXML
	private ComboBox<String> version;
	@FXML
	private Button finish;
	
	public static void loadNewTexturePack() {
		try {
			newStageOpen = false;
			Parent root = FXMLLoader.load(Main.getApl().getClass().getResource("/CreateTexture/CreateTexture.fxml"));
			createTexture = new Scene(root);
			createTexture.getStylesheets().add("resource/teme/teme" + Settings.getSetting(Settings.TEME) + ".css");
				
			Main.stageApl.setScene(createTexture);
		} catch(IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to open Create Texture stage - IOException in TexturePack.create", ConsoleLine.CRASH, exc));
		} catch(Exception exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to open Create Texture stage - Exception in TexturePack.create", ConsoleLine.CRASH, exc));
		}
	}
	
	public static void loadTexturePack(TexturePack texturePack, boolean newStage) {
		try {
			modify = texturePack;
			
			if(newStage && !newStageOpen) {
				newStageOpen = true;
				Parent root = FXMLLoader.load(Main.getApl().getClass().getResource("/CreateTexture/ModifyTexture.fxml"));
				createTexture = new Scene(root);
				createTexture.getStylesheets().add("resource/teme/teme" + Settings.getSetting(Settings.TEME) + ".css");
				stageOpen = new Stage();
				stageOpen.getIcons().add(new Image(Main.getApl().getClass().getResourceAsStream("/icon.png")));
				stageOpen.setTitle("Millenniar Texture - Modify");
				stageOpen.setScene(createTexture);
				stageOpen.show();
			} else {
				newStageOpen = false;
				Parent root = FXMLLoader.load(Main.getApl().getClass().getResource("/CreateTexture/CreateTexture.fxml"));
				createTexture = new Scene(root);
				createTexture.getStylesheets().add("resource/teme/teme" + Settings.getSetting(Settings.TEME) + ".css");
				Main.stageApl.setScene(createTexture);
			}
		} catch(NullPointerException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to load data Create Texture stage - NullPointerException in TexturePack.modify", ConsoleLine.CRASH, exc));
		} catch(IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to open Create Texture stage - IOException in TexturePack.modify", ConsoleLine.CRASH, exc));
		} catch(Exception exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to open Create Texture stage - Exception in TexturePack.modify", ConsoleLine.CRASH, exc));
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		checkName.setImage(new Image(getClass().getResourceAsStream("/resource/icon/void.png")));
		checkDescription.setImage(new Image(getClass().getResourceAsStream("/resource/icon/void.png")));
		checkImage.setImage(new Image(getClass().getResourceAsStream("/resource/icon/void.png")));
		choserImage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/search_icon.png"))));
		version.setItems(versionList);
		if(modify != null) {
			fieldName.setText(modify.getName());
			fieldDescription.setText(modify.getDescription());
			fieldImage.setText(modify.getImage());
			version.setValue(modify.getVersionString());
		} else {
			version.setValue("1.19.x");
			fieldImage.setText("-default");
		}
	}
	
	public void returnLobby() {
		if(!newStageOpen) {
			modify = null;
			Main.loadMenu();
		}
	}
	
	public void choseFile() {
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open Resource File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Images PNG", "*.png"),
		         new ExtensionFilter("Other images", "*.jpg", "*.gif"),
		         new ExtensionFilter("All Files", "*.*"));
		 File selectedFile = fileChooser.showOpenDialog(Main.stageApl);
		 if (selectedFile != null) {
			 fieldImage.setText(selectedFile.getPath());
			 ConsoleStage.defaultCns.printConsole(new ConsoleLine("CHOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.DEBUG));
		 }
		 checkImage();
	}
	
	public void checkName() {
		if(fieldName.getText().trim().equals("")) {
			checkName.setImage(new Image(getClass().getResourceAsStream("/resource/exception/null_text.png")));
			fieldName.getStyleClass().add(ERROR);
		} else {
			checkName.setImage(new Image(getClass().getResourceAsStream("/resource/icon/ok_icon.png")));
			fieldName.getStyleClass().remove(ERROR);
		}
	}
	
	public void checkImage() {
		if(fieldImage.getText().trim().equals("")) {
			checkImage.setImage(new Image(getClass().getResourceAsStream("resource/exception/null_text.png")));
			fieldImage.getStyleClass().add(ERROR);
		} else if(!new File(fieldImage.getText().trim()).exists() && !fieldImage.getText().trim().equals("-default") && !fieldImage.getText().trim().equals("-void")) {
			checkImage.setImage(new Image(getClass().getResourceAsStream("/resource/exception/file_not_found.png")));
			fieldImage.getStyleClass().add(ERROR);
		} else {
			checkImage.setImage(new Image(getClass().getResourceAsStream("/resource/icon/ok_icon.png")));
			fieldImage.getStyleClass().remove(ERROR);
		}
	}
	
	public void finish() {
		generate();
	}
	
	public void settings() {
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
	
	public void support() {
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
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open Support stage - IOException in Support.stage", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open Support stage - Exception in Support.stage", ConsoleLine.CRASH, exc));
			}
		}
	}
	
	public void excConsole() {
		Main.exceptionStage.getStage().show();
	}

	public void generate() {
		try {
			boolean check = true;
			if(this.fieldName.getText().trim().equals("") || this.fieldName.getText() == null) {
				this.checkName.setImage(new Image(resource + "exception/null_text.png"));
				this.fieldName.getStyleClass().add(ERROR);
				check = false;
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to get name of texture pack - VoidObject in TexturePack.getData.name", ConsoleLine.ERROR));
			}
			for(TexturePack ne : TexturePack.textures) {
				if(this.fieldName.getText().equalsIgnoreCase(ne.getName()) && modify != ne) {
					this.checkName.setImage(new Image(resource + "exception/same_text.png"));
					this.fieldName.getStyleClass().add(ERROR);
					check = false;
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to get name of texture pack - SameObject in TexturePack.getData.name", ConsoleLine.ERROR));
				}
			}
			File checkImage = new File(this.fieldImage.getText().trim());
			if((this.fieldImage.getText().trim().equals("") || !checkImage.exists()) && !this.fieldImage.getText().trim().equals("-default") && !fieldImage.getText().trim().equals("-void")) {
				this.checkImage.setImage(new Image(resource + "exception/file_not_found.png"));
				this.checkImage.getStyleClass().add(ERROR);
				check = false;
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to get image of texture pack - FileNotFound in TexturePack.getData.image", ConsoleLine.ERROR));
			}
			if(check || !Settings.verifyTexturePack) {
				TexturePack generated = new TexturePack(this.fieldName.getText().trim(), this.fieldDescription.getText().trim(), this.version.getSelectionModel().getSelectedItem(), buildTexture + this.fieldName.getText().trim() + "/pack.png");
				Exception exception = null;
				if(modify != null)
					exception = this.modifyTexture(generated);
				else
					exception = this.createTexture();
				if(exception == null) {
					Register.save();
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Create texture pack \"" + this.fieldName.getText().trim() + "\" succefly", ConsoleLine.SUCCESS));
					if(modify != null) {
						MSDialog.showInformation("Modify Texture pack", 
								"Modify texture pack \"" + modify.getName() + "\" to \"" + this.fieldName.getText() + "\" succefly\n"
								+ "	- Name: \"" + modify.getName() + "\" to \"" + this.fieldName.getText() + "\"\n"
								+ "	- Description: \"" + modify.getDescription() + "\" to \"" + this.fieldDescription.getText() + "\"\n"
								+ "	- Image: \"" + modify.getImage() + "\" to \"" + this.fieldImage.getText() + "\"\n"
								+ "	- Version: \"" + modify.getVersionString() + "\" to \"" + this.version.getSelectionModel().getSelectedItem() + "\"");
					} else {
						MSDialog.showInformation("Create Texture pack", 
								"Create" + this.fieldName.getText() + "\" succefly\n"
								+ "	- Name: \"" + this.fieldName.getText() + "\"\n"
								+ "	- Description: \"" + this.fieldDescription.getText() + "\"\n"
								+ "	- Image: \"" + this.fieldImage.getText() + "\"\n"
								+ "	- Version: \"" + this.version.getSelectionModel().getSelectedItem() + "\"");
					}
					if(newStageOpen) {
						ManagmentStage.getOn().setTexturePack(generated);
						stageOpen.close();
						ManagmentStage.getOn().refreshTreeFiles();
					} else {
						Main.loadMenu();
					}
					newStageOpen = false;
					modify = null;
				} else {
					ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to create texture pack \"" + this.fieldName.getText() + "\"", ConsoleLine.FATAL));
					MSDialog.showError("Create Texture pack", 
							"Fail to create" + this.fieldName.getText() + "\" - change the wrong fields\n"
							+ "	- Name: \"" + this.fieldName.getText() + "\"\n"
							+ "	- Description: \"" + this.fieldDescription.getText() + "\"\n"
							+ "	- Image: \"" + this.fieldImage.getText() + "\"\n"
							+ "	- Version: \"" + this.version.getSelectionModel().getSelectedItem() + "\"\n"
							+ "	- Exception: Exception:\n"
								+ "		- " + exception);
				}
			} else {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to create texture pack \"" + this.fieldName.getText() + "\"", ConsoleLine.FATAL));
				MSDialog.showError("Create Texture pack", 
						"Fail to create" + this.fieldName.getText() + "\" - change the wrong fields\n"
						+ "	- Name: \"" + this.fieldName.getText() + "\"\n"
						+ "	- Description: \"" + this.fieldDescription.getText() + "\"\n"
						+ "	- Image: \"" + this.fieldImage.getText() + "\"\n"
						+ "	- Version: \"" + this.version.getSelectionModel().getSelectedItem() + "\"\n");
			}
		} catch(NullPointerException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to create texture pack - NullPointerException in TexturePack.create",
					ConsoleLine.FATAL, exc));
			MSDialog.showError("Create Texture pack", 
					"Fail to create" + this.fieldName.getText() + "\"\n"
					+ "	- Name: \"" + this.fieldName.getText() + "\"\n"
					+ "	- Description: \"" + this.fieldDescription.getText() + "\"\n"
					+ "	- Image: \"" + this.fieldName.getText() + "\"\n"
					+ "	- Version: \"" + this.version.getEditor().getText() + "\"\n"
					+ "	- Exception: NullPointerException:\n"
						+ "		- " + exc);
		} catch(Exception exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to create texture pack - Exception in TexturePack.create",
					ConsoleLine.FATAL, exc));
			MSDialog.showError("Create Texture pack", 
					"Fail to create" + this.fieldName.getText() + "\"\n"
					+ "	- Name: \"" + this.fieldName.getText() + "\"\n"
					+ "	- Description: \"" + this.fieldDescription.getText() + "\"\n"
					+ "	- Image: \"" + this.fieldImage.getText() + "\"\n"
					+ "	- Version: \"" + this.version.getSelectionModel().getSelectedItem() + "\"\n"
					+ "	- Exception: Exception:\n"
						+ "		- " + exc);
		}
	}
	
	private Exception createTexture() {
		File textureMkdirs = new File(buildTexture + this.fieldName.getText());
		textureMkdirs.mkdirs();
		try {
			byte versionByte = (byte) (TexturePack.versionToInt(this.version.getSelectionModel().getSelectedItem()));
			FileWriter packMcmeta = new FileWriter(buildTexture + this.fieldName.getText().trim() + "/pack.mcmeta");
			packMcmeta.write("{\r\n"
					+ "  \"credit\": \"create whit Millenniar Texture\",\n"
					+ "  \"pack\": {\r\n"
					+ "    \"pack_format\": "
					+ versionByte
					+ ",\r\n"
					+ "    \"description\": \""
					+ this.fieldDescription.getText().trim()
					+ "\"\r\n"
					+ "  }\r\n"
					+ "}");
			try {
				packMcmeta.close();
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to close FileWriter - IOException in TexturePack.file.writer", ConsoleLine.WARN, exc));
			}
			
			if(this.fieldImage.getText().trim().equals("-default"))
				new CopyFile(defaultFile + "texture_pack_image.png", buildTexture + this.fieldName.getText().trim() + "/pack.png", true).execute(false);
			else if(!this.fieldImage.getText().trim().equals("-void"))
				new CopyFile(this.fieldImage.getText().trim(), buildTexture + this.fieldName.getText().trim() + "/pack.png", true).execute(false);
		
		} catch (NoSuchFileException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to create files of texture pack - NoSuchFileException in TexturePack.file", ConsoleLine.ERROR, exc));
			return exc;
		} catch (FileAlreadyExistsException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to create files of texture pack - FileAlreadyExistsException in TexturePack.file", ConsoleLine.ERROR, exc));
			return exc;
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to create files of texture pack - IOException in TexturePack.file", ConsoleLine.ERROR, exc));
			return exc;
		}
		return null;
	}
	
	private Exception modifyTexture(TexturePack generated) {
		File textureW = new File(buildTexture + modify.getName());
		textureW.renameTo(new File(buildTexture + this.fieldName.getText().trim()));
		
		File textureD = new File(data + "textureObjects/" + modify.getName() + ".dat");
		textureD.renameTo(new File(data + "textureObjects/" + this.fieldName.getText().trim() + ".dat"));
		
		byte versionByte = (byte) (TexturePack.versionToInt(this.version.getSelectionModel().getSelectedItem()));
		FileWriter packMcmeta;
		try {
			packMcmeta = new FileWriter(buildTexture + this.fieldName.getText().trim() + "/pack.mcmeta");
			packMcmeta.write("{\r\n"
					+ "  \"credit\": \"create whit Millenniar Texture\",\n"
					+ "  \"pack\": {\r\n"
					+ "    \"pack_format\": "
					+ versionByte
					+ ",\r\n"
					+ "    \"description\": \""
					+ this.fieldDescription.getText().trim()
					+ "\"\r\n"
					+ "  }\r\n"
					+ "}");
			try {
				packMcmeta.close();
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to close FileWriter - IOException in TexturePack.file.writer", ConsoleLine.WARN, exc));
			}
			
			if(this.fieldImage.getText().trim().equals("-default"))
				new CopyFile(defaultFile + "texture_pack_image.png", buildTexture + this.fieldName.getText().trim() + "/pack.png", true).execute(false);
			else if(!this.fieldImage.getText().trim().equals("-void"))
				new CopyFile(this.fieldImage.getText(), buildTexture + this.fieldName.getText().trim() + "/pack.png", true).execute(false);
			else {
				if(new File(modify.getImage()).exists()) {
					if(new File(modify.getImage()).delete())
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Delet texture pack's image file \"" + modify.getImage() + "\"", ConsoleLine.INFO));
					else
						ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to delet texture pack's image file \"" + modify.getImage() + "\"", ConsoleLine.ERROR));
			
				}
			}
			generated.setObjects(modify.getObjects());
			TexturePack.textures.remove(modify);
		
		} catch (NoSuchFileException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to modify files of texture pack - NoSuchFileException in TexturePack.file", ConsoleLine.ERROR, exc));
			return exc;
		} catch (FileAlreadyExistsException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to modify files of texture pack - FileAlreadyExistsException in TexturePack.file", ConsoleLine.ERROR, exc));
			return exc;
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("TEXTUREPACK", "Fail to modify files of texture pack - IOException in TexturePack.file", ConsoleLine.ERROR, exc));
			return exc;
		}
		return null;
	}
}
