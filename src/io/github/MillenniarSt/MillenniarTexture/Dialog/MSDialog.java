package io.github.MillenniarSt.MillenniarTexture.Dialog;

import io.github.MillenniarSt.MillenniarTexture.Main.Main;
import io.github.MillenniarSt.MillenniarTexture.Main.ProgrammePath;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Robot;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleLine;
import io.github.MillenniarSt.MillenniarTexture.Console.ConsoleStage;
import io.github.MillenniarSt.MillenniarTexture.Settings.Settings;

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

public class MSDialog implements ProgrammePath {

	public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle("Millenniar Texture - Information");
        alert.setHeaderText(title);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    	stage.getIcons().add(Main.getResource("icon"));
    	stage.getScene().getStylesheets().add(Settings.getTheme());
        
    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("MAIN", message, ConsoleLine.INFO));
        alert.showAndWait();
    }

    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle("Millenniar Texture - Warning");
        alert.setHeaderText(title);
        alert.setContentText(message);
        
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    	stage.getIcons().add(Main.getResource("icon"));
    	stage.getScene().getStylesheets().add(Settings.getTheme());

    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("MAIN", message, ConsoleLine.WARN));
        alert.showAndWait();
    }

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle("Millenniar Texture - Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    	stage.getIcons().add(Main.getResource("icon"));
    	stage.getScene().getStylesheets().add(Settings.getTheme());

    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("MAIN", message, ConsoleLine.ERROR));
        alert.showAndWait();
    }

    public static void showException(String title, String message, Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle("Millenniar Texture - Exception");
        alert.setHeaderText(title);
        alert.setContentText(message);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Details:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    	stage.getIcons().add(Main.getResource("icon"));
    	stage.getScene().getStylesheets().add(Settings.getTheme());
        
    	ConsoleStage.defaultCns.printConsole(new ConsoleLine("MAIN", message, ConsoleLine.FATAL, exception));
        alert.showAndWait();
    }

    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";

    public static String showConfirm(String title, String message, String... options) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle("Millenniar Texture - Choose an option");
        alert.setHeaderText(title);
        alert.setContentText(message);

        alert.getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                event.consume();
                try {
                    Robot r = new Robot();
                    r.keyPress(java.awt.event.KeyEvent.VK_SPACE);
                    r.keyRelease(java.awt.event.KeyEvent.VK_SPACE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (options == null || options.length == 0) {
            options = new String[]{OK, CANCEL};
        }

        List<ButtonType> buttons = new ArrayList<>();
        for (String option : options) {
            buttons.add(new ButtonType(option));
        }

        alert.getButtonTypes().setAll(buttons);

    	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    	stage.getIcons().add(Main.getResource("icon"));
    	stage.getScene().getStylesheets().add(Settings.getTheme());
    	
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
            return CANCEL;
        } else {
            return result.get().getText();
        }
    }

    public static String showTextInput(String title, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.setTitle("Input");
        dialog.setHeaderText(title);
        dialog.setContentText(message);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
    	stage.getIcons().add(Main.getResource("icon"));
    	stage.getScene().getStylesheets().add(Settings.getTheme());
        
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
