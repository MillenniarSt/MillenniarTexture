package io.github.MillenniarSt.MillenniarTexture.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import io.github.MillenniarSt.MillenniarTexture.Console.Console;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.CreateTexture.Register;
import io.github.MillenniarSt.MillenniarTexture.Dialog.MSDialog;
import io.github.MillenniarSt.MillenniarTexture.RPC.Discord;
import io.github.MillenniarSt.MillenniarTexture.Settings.Settings;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*            //////////////////////////////////
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

public class Main extends Application implements ProgrammePath, Initializable {
	
	public static Console exception = new Console("Exception", null, new ArrayList<ConsoleLine>(), "0");
	public static ConsoleStage exceptionStage = new ConsoleStage();
	public static Stage stageApl;
	public static Scene menu;
	
	private static Application apl;
	
	private static final Stage start = new Stage();
	private MediaPlayer media;
	@FXML
	private MediaView mediaView;

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		apl = this;
		stageApl = stage;
		
		exception.setMesException(true);
		exceptionStage.setStage(new Stage());

		if(!new File(resource).exists()) {
			Stage askResource = new Stage();
			AskResource.tempStage = askResource;
			
			Parent startP = FXMLLoader.load(getLayout("Main/AskResource"));
			Scene startS = new Scene(startP);
			askResource.setScene(startS);
			askResource.getIcons().add(getResource("icon"));
			askResource.setTitle("Millenniar Texture - Resource");
			
			askResource.show();
		} else {
			run();
		}
	}
	
	@SuppressWarnings("unchecked")
	void run() {
		try {
			Parent startP = FXMLLoader.load(getLayout("Main/Start"));
			Scene startS = new Scene(startP);
			
			start.getIcons().add(getResource("icon"));
			start.setTitle("Millenniar Texture - Starting");
			start.setScene(startS);
			start.setResizable(false);
			start.initStyle(StageStyle.UNDECORATED);
			start.show();
			
			ConsoleStage.current = exception;
			Parent console = FXMLLoader.load(getLayout("Console/Console"));
			Scene menu = new Scene(console);
			exceptionStage.getStage().getIcons().add(getResource("icon"));
			exceptionStage.getStage().setTitle("Exception io.github.MillenniarSt.MillenniarTexture.Console");
			
			exceptionStage.getStage().setScene(menu);
				
			Stage backup = exceptionStage.getStage();
			exceptionStage = ConsoleStage.currentStage;
			exceptionStage.setStage(backup);
				
			ConsoleStage.defaultCns = exceptionStage;

			Discord.startRPC();
				
			Settings.load();

			menu.getStylesheets().add(Settings.getTheme());
			exception.copyOpt(((ArrayList<Console>) Settings.getSetting(Settings.CONSOLES)).get(0));
			Register.loadTp();
			loadMenu();
			stageApl.getIcons().add(getResource("icon"));
			stageApl.setTitle("Millenniar Texture - Start");
			stageApl.setResizable(false);

		}  catch(NullPointerException exc) {
			exc.printStackTrace();
			MSDialog.showException("Crash in Millenniar Texture Run", "Fail to start Millenniar Texture - NullPointerException in io.github.MillenniarSt.MillenniarTexture.Main.run", exc);
			start.close();
			exceptionStage.getStage().show();
			exceptionStage.printConsole(new ConsoleLine("RUN", "Fail to start the programme - NullPointerException in io.github.MillenniarSt.MillenniarTexture.Main.run", ConsoleLine.CRASH, exc));
		} catch(RuntimeException exc) {
			exc.printStackTrace();
			MSDialog.showException("Crash in Millenniar Texture Run", "Fail to start Millenniar Texture - RuntimeException in io.github.MillenniarSt.MillenniarTexture.Main.run", exc);
			start.close();
			exceptionStage.getStage().show();
			exceptionStage.printConsole(new ConsoleLine("RUN", "Fail to start the programme - RuntimeException in io.github.MillenniarSt.MillenniarTexture.Main.run", ConsoleLine.CRASH, exc));
		} catch(Exception exc) {
			exc.printStackTrace();
			MSDialog.showException("Crash in Millenniar Texture Run", "Fail to start Millenniar Texture - Exception in io.github.MillenniarSt.MillenniarTexture.Main.run", exc);
			start.close();
			exceptionStage.getStage().show();
			exceptionStage.printConsole(new ConsoleLine("RUN", "Fail to start the programme - Exception in io.github.MillenniarSt.MillenniarTexture.Main.run", ConsoleLine.CRASH, exc));
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(new File(resource + "theme_song.mp4").exists()) {
			media = new MediaPlayer(new Media(new File(resource + "theme_song.mp4").toURI().toString()));
			mediaView.setMediaPlayer(media);
			media.play();
		}
	}
	
	public static void loadMenu() {
		try {
			Parent main = FXMLLoader.load(getLayout("Main/Main"));
			menu = new Scene(main);
			menu.getStylesheets().add(Settings.getTheme());
				
			Main.stageApl.setScene(menu);
		} catch (RuntimeException exc) {
			exceptionStage.printConsole(new ConsoleLine("RUN", "Fail to open io.github.MillenniarSt.MillenniarTexture.Main stage - RuntimeException in io.github.MillenniarSt.MillenniarTexture.Main.run", ConsoleLine.CRASH, exc));
		} catch (IOException exc) {
			exceptionStage.printConsole(new ConsoleLine("RUN", "Fail to open io.github.MillenniarSt.MillenniarTexture.Main stage - IOException in io.github.MillenniarSt.MillenniarTexture.Main.run", ConsoleLine.CRASH, exc));
		} catch (Exception exc) {
			exceptionStage.printConsole(new ConsoleLine("RUN", "Fail to open io.github.MillenniarSt.MillenniarTexture.Main stage - Exception in io.github.MillenniarSt.MillenniarTexture.Main.run", ConsoleLine.CRASH, exc));
		}
	}

	public void show() {
		try {
			start.close();
			
			stageApl.show();
		}  catch(NullPointerException exc) {
			exceptionStage.getStage().show();
			exceptionStage.printConsole(new ConsoleLine("RUN", "Fail to start the programme - NullPointerException in io.github.MillenniarSt.MillenniarTexture.Main.run", ConsoleLine.CRASH, exc));
			stageApl.close();
		} catch(RuntimeException exc) {
			exceptionStage.getStage().show();
			exceptionStage.printConsole(new ConsoleLine("RUN", "Fail to start the programme - RuntimeException in io.github.MillenniarSt.MillenniarTexture.Main.run", ConsoleLine.CRASH, exc));
			stageApl.close();
			} catch(Exception exc) {
			exceptionStage.getStage().show();
			exceptionStage.printConsole(new ConsoleLine("RUN", "Fail to start the programme - Exception in io.github.MillenniarSt.MillenniarTexture.Main.run", ConsoleLine.CRASH, exc));
			stageApl.close();
		}
	}

	public static InputStream getResourceStream(String path) {
		return apl.getClass().getResourceAsStream("/resources/" + path + ".png");
	}
	public static Image getResource(String path) {
		return new Image(apl.getClass().getResourceAsStream("/resources/" + path + ".png"));
	}

	public static URL getLayout(String path) {
		return apl.getClass().getResource("/io/github/MillenniarSt/MillenniarTexture/" + path + ".fxml");
	}
	
	public static Application getApl() {
		return apl;
	}
}
