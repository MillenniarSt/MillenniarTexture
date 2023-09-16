package Console;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import JavaObject.Block;
import JavaObject.TextureObject;
import Main.Main;
import Main.ProgrammePath;
import Main.Support;
import Settings.ManagmentOption;
import Settings.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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

public class ConsoleStage implements ProgrammePath, Initializable {
	
	private static ArrayList<ConsoleStage> stages = new ArrayList<>();
	
	public static Console corrent = null;
	public static ConsoleStage correntStage = null;
	public static ConsoleStage defaultCns = new ConsoleStage();
	
	private Console subject;
	private Stage stage;

	@FXML
	private Label labelName;
	@FXML
	private TextField comandPromp;
	@FXML
	private TextFlow display;
	@FXML
	private Button buttonImport;
	@FXML
	private Button buttonExport;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.subject = corrent;
		correntStage = this;
		
		labelName.setText(this.subject.getTitle() + " Console:");
		buttonImport.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/import_icon.png"))));
		buttonExport.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/resource/icon/export_icon.png"))));
		
		stages.add(this);
	}
	
	public void setTitle(String title) {
		this.labelName.setText(title + " Console:");
	}
	
	public static void refresh() {
		for(ConsoleStage cnss : stages) {
			cnss.stage.getScene().getStylesheets().clear();
			cnss.stage.getScene().getStylesheets().add("resource/teme/teme" + Settings.getSetting(Settings.TEME) + ".css");
		}
	}
	
	public void printConsole(ConsoleLine line) {
		if(this.subject != null) {
			try {
				print: {
					String stateString;
					this.subject.getRegister().add(line);
					if(line.getState().equals(ConsoleLine.DEBUG) && this.subject.isMesDebug())
						stateString = "DEBUG";
					else if(line.getState().equals(ConsoleLine.SUCCESS) && this.subject.isMesSuccess())
						stateString = "SUCCESS";
					else if(line.getState().equals(ConsoleLine.INFO) && this.subject.isMesInfo())
						stateString = "INFO";
					else if(line.getState().equals(ConsoleLine.WARN) && this.subject.isMesWarn())
						stateString = "WARN";
					else if(line.getState().equals(ConsoleLine.ERROR) && this.subject.isMesError())
						stateString = "ERROR";
					else if(line.getState().equals(ConsoleLine.FATAL) && this.subject.isMesFatal())
						stateString = "FATAL";
					else if(line.getState().equals(ConsoleLine.CRASH)) {
						stateString = "CRASH";
						Main.exceptionStage.getStage().show();
						this.subject = Main.exception;
						try {
							Parent support = FXMLLoader.load(Main.getApl().getClass().getResource("/Support.fxml"));
							Support.supScene = new Scene(support);
								
							Support.getSupport().setScene(Support.supScene);
							Support.getSupport().getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
							Support.getSupport().setTitle("Millenniar Texture - Support");
							Support.getSupport().setResizable(false);
							Support.supScene.getStylesheets().add("resource/teme/teme" + Settings.getSetting(Settings.TEME) + ".css");
							Support.getSupport().show();
						} catch (IOException exc) { }
					} else {
						break print;
					}
					if(this.subject.getLastTime() == null || !(this.subject.getLastTime().getDayOfYear() == LocalDateTime.now().getDayOfYear() && this.subject.getLastTime().getYear() == LocalDateTime.now().getYear())) {
						Text text = new Text("--------------------------------------------------------------------------------------------------------------------------------------------" + 
				        		LocalDateTime.now().getDayOfMonth() + "/" + LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getYear() +
				        		"----------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
						text.setFill(Color.web(ConsoleLine.DEBUG));
						this.display.getChildren().add(text);
					}
					if(this.subject.getLastTime() == null || !(this.subject.getLastTime().getHour() == LocalDateTime.now().getHour() && 
							this.subject.getLastTime().getMinute() == LocalDateTime.now().getMinute() && this.subject.getLastTime().getSecond() == LocalDateTime.now().getSecond())) {
						String hour = LocalDateTime.now().getHour() + "";
						if(LocalDateTime.now().getHour() < 10)
							hour = "0" + LocalDateTime.now().getHour();
						String minute = LocalDateTime.now().getMinute() + "";
						if(LocalDateTime.now().getMinute() < 10)
							minute = "0" + LocalDateTime.now().getMinute();
						String second = LocalDateTime.now().getSecond() + "";
						if(LocalDateTime.now().getSecond() < 10)
							second = "0" + LocalDateTime.now().getSecond();
						
						Text text = new Text(hour + ":" + minute + ":" + second + " | [" + line.getLocation() + "/" + stateString + "] " + line.getText() + "\n");
						text.setFill(Color.web(line.getState()));
						this.display.getChildren().add(text);
						
				        this.subject.setLastTime(LocalDateTime.now());
					} else {
						Text text = new Text("               | [" + line.getLocation() + "/" + stateString + "] " + line.getText() + "\n");
						text.setFill(Color.web(line.getState()));
						this.display.getChildren().add(text);
					}
					if(line.getException() != null && this.subject.isMesException()) {
						StringWriter sw = new StringWriter();
					    PrintWriter pw = new PrintWriter(sw);
						line.getException().printStackTrace(pw);
						Text text = new Text("               | " + sw.toString() + "\n");
						text.setFill(Color.web(line.getState()));
						this.display.getChildren().add(text);
					}
				}
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	
	public void println() {
		Text text = new Text("\n");
		this.display.getChildren().add(text);
	}
	
	public void comand() {
		ArrayList<String> commands = new ArrayList<>();
		
		char[] chars = new char[comandPromp.getText().trim().length()];
		for (int i = 0; i < comandPromp.getText().trim().length(); i++) {
			chars[i] = comandPromp.getText().trim().charAt(i);
		}
		
		String text = "";
		for(char character : chars) {
			if(character != ' ') {
				text = text + character;
			} else {
				if(!text.trim().equals("")) {
					commands.add(text);
				}
				text = "";
			}
		}
		commands.add(text);
		
		try {
			if(commands.get(0).equals("help")) {
				this.printConsole(new ConsoleLine("COMMAND", "commands:\n"
						+ "                         \"message (text)\" : send a message in this console\n"
						+ "                         \"variable [variable] {true,false}\" : change a local variable\n"
						+ "                         \"open [frame]\" : open a frame\n"
						+ "                         \"load [data]\" : load a programme's data", ConsoleLine.INFO));
			} else if(commands.get(0).equals("message")) {
				this.printConsole(new ConsoleLine("COMMAND", comandPromp.getText().substring(commands.get(0).length() + 1), ConsoleLine.INFO));
			} else if(commands.get(0).equals("variable")) {
				String variable = commands.get(1);
				String value = commands.get(2);
				if(variable.equals("verify_object"))
					Settings.verifyObject = Boolean.getBoolean(value);
				else if(variable.equals("verify_texture_pack"))
					Settings.verifyTexturePack = Boolean.getBoolean(value);
				else
					this.printConsole(new ConsoleLine("COMMAND", "Variable \"" + variable + "\" not exists", ConsoleLine.INFO));
				this.printConsole(new ConsoleLine("COMMAND", "Variable \"" + variable + "\" was set to \"" + value + "\"", ConsoleLine.INFO));
			} else if(commands.get(0).equals("open")) {
				if(text.equals("new_block")) {
						
				} else if(text.equals("settings")) {
					if(!ManagmentOption.options.isShowing()) {
						try {
							Parent settings = FXMLLoader.load(Main.getApl().getClass().getResource("/Settings.fxml"));
							ManagmentOption.optScene = new Scene(settings);
							
							ManagmentOption.options.setScene(ManagmentOption.optScene);
							ManagmentOption.options.getIcons().add(new Image(defaultFile + "icon.png"));
							ManagmentOption.options.setTitle("Millenniar Texture - Options");
							ManagmentOption.optScene.getStylesheets().add("teme" + Settings.getSetting(Settings.TEME) + ".css");
							ManagmentOption.options.show();
						} catch (IOException exc) {
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open Settings stage - IOException in Settings.stage", ConsoleLine.CRASH, exc));
						} catch (Exception exc) {
							ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open Settings stage - Exception in Settings.stage", ConsoleLine.CRASH, exc));
						}
					}
				} else if(text.equals("support")) {
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
				} else if(text.equals("exception_console")) {
					Main.exceptionStage.stage.show();
				} else {
					this.printConsole(new ConsoleLine("COMMAND", "Stage \"" + text + "\" not exists", ConsoleLine.WARN));
				}
			} else if(commands.get(0).equals("load")) {
					if(text.equals("settings")) {
						Settings.load();
					} else if(text.equals("texture_object")) {
						
					} else {
						this.printConsole(new ConsoleLine("COMMAND", "Data \"" + text + "\" not exists", ConsoleLine.WARN));
					}
			} else if(commands.get(0).equals("restart")) {
				if((Main.stageApl != null && Main.stageApl.isShowing())) {
					this.printConsole(new ConsoleLine("COMMAND", "Impossible to restart the programme because the programme has already restarted", ConsoleLine.WARN));
				} else {
					try {
						new Main().start(new Stage());
					} catch (Exception exc) {
						exc.printStackTrace();
					}
					this.printConsole(new ConsoleLine("COMMAND", "Restarting programme...", ConsoleLine.INFO));
				}
			} else if(commands.get(0).equals("see")) {
				String type = commands.get(1);
				String derivation = commands.get(2);
				String name = commands.get(3);
				Block subject = (Block) TextureObject.getTextureObject(name, derivation, type);
				if(subject != null) {
					this.printConsole(new ConsoleLine("COMMAND", "Block " + name + ":\n" + subject, ConsoleLine.INFO));
				} else {
					this.printConsole(new ConsoleLine("COMMAND", "Block " + name + " not found", ConsoleLine.WARN));
				}
			} else {
				this.printConsole(new ConsoleLine("COMMAND", "Command \"" + commands.get(0) + "\" not found, use \"help\" to see the list", ConsoleLine.WARN));
			}
		} catch(ArrayIndexOutOfBoundsException exc) {
			this.printConsole(new ConsoleLine("COMMAND", "Command \"" + comandPromp.getText().trim() + "\" not expeted -> \"\", use \"help\" to see the list", ConsoleLine.WARN));
		} catch(StringIndexOutOfBoundsException exc) {
			this.printConsole(new ConsoleLine("COMMAND", "Incorrect expression of String in the command \"" + comandPromp.getText().trim() + "\", use \"help\" to see the list", ConsoleLine.WARN));
		}
		comandPromp.setText("");
	}
	
	public void exportCns() {
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save");
	    fileChooser.getExtensionFilters().addAll(
	    		new ExtensionFilter("Cns Files", "*.cns"),
	    		new ExtensionFilter("All Files", "*.*"));
	    File file = fileChooser.showSaveDialog(this.stage);
		if(file != null) {
			if(file.exists()) {
				try {
					file.delete();
					Console.save(file.getPath().substring(0, file.getPath().lastIndexOf(".")) + ".cns", this.subject);
				} catch(StringIndexOutOfBoundsException exc) {
					Console.save(file.getPath() + ".cns", this.subject);
				}
			} else {
				try {
					Console.save(file.getPath().substring(0, file.getPath().lastIndexOf(".")) + ".cns", this.subject);
				} catch(StringIndexOutOfBoundsException exc) {
					Console.save(file.getPath() + ".cns", this.subject);
				}
			}
		}
	}
	
	public void importCns() {
		try {
			FileChooser fileChooser = new FileChooser();
			 fileChooser.setTitle("Open Resource File");
			 fileChooser.getExtensionFilters().addAll(
			         new ExtensionFilter("Console files", "*.cns"),
			         new ExtensionFilter("Data files", "*.dat"),
			         new ExtensionFilter("All Files", "*.*"));
			 File selectedFile = fileChooser.showOpenDialog(this.stage);
			 if (selectedFile != null) {
				this.printConsole(new ConsoleLine("CHOSER", "Chose file path \"" + selectedFile.getPath() + "\"", ConsoleLine.DEBUG));
				Console imported = Console.load(selectedFile.getPath());
				this.printConsole(new ConsoleLine("CONSOLE", "Execute import of Console \"" + imported.getTitle() + "\":", ConsoleLine.INFO));
				this.println();
				for(ConsoleLine line : imported.getRegister()) {
					this.printConsole(line);
				}
				this.println();
				this.printConsole(new ConsoleLine("CONSOLE", "Finish to read line of Console \"" + imported.getTitle() + "\"", ConsoleLine.INFO));
			 }
		} catch(NullPointerException exc) {
			this.printConsole(new ConsoleLine("SETTING", "Fail import console - FileNotFoundException in Console.import", ConsoleLine.FATAL));
		}
	}
	
	public TextField getComandPromp() {
		return comandPromp;
	}

	public void setComandPromp(TextField comandPromp) {
		this.comandPromp = comandPromp;
	}

	public TextFlow getDisplay() {
		return display;
	}

	public void setDisplay(TextFlow display) {
		this.display = display;
	}

	public Console getSubject() {
		return subject;
	}

	public void setSubject(Console subject) {
		this.subject = subject;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
