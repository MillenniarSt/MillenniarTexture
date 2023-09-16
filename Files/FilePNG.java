package Files;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import Console.ConsoleLine;
import Console.ConsoleStage;
import JavaObject.TextureObject;
import Main.MSDialog;
import Main.Main;
import Main.ProgrammePath;
import Menu.ManagmentStage;
import Settings.Settings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

public class FilePNG extends FileTx implements EventHandler<MouseEvent>, Initializable, ProgrammePath, PixelWriter {

	private static final long serialVersionUID = 1L;
	private static Background background = new Background(new BackgroundImage(new Image(Main.getApl().getClass().getResourceAsStream("/resource/image/void.png")), 
			BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(0, 0, true, true, true, false)));
	
	@FXML
	private AnchorPane parent;
	@FXML
	private Label pathLabel;
	@FXML
	private ScrollPane container;
	@FXML
	private Label sizeLabel;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private ToggleButton pen;
	@FXML
	private ToggleButton rubber;
	@FXML
	private ToggleButton picker;
	@FXML
	private Slider zoom;
	
	private Pixel[] [] pixels;
	private HBox grid;
	private SavePNG savedPNG;
	
	public FilePNG(String path, SavePNG savedPNG) {
		super(path, savedPNG, new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/file_png.png"))));
		this.pixels = savedPNG.getPixels();
		this.savedPNG = savedPNG;
	}
	
	public FilePNG(String path, boolean activated) {
		super(path, "image", activated, new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/file_png.png"))));
		this.savedPNG = new SavePNG(this);
	}
	
	public FilePNG(String path, boolean activated, ArrayList<TextureObject> sources) {
		super(path, "image", activated, new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/file_png.png"))), sources);
		this.savedPNG = new SavePNG(this);
	}
	
	@Override
	public void open(ManagmentStage stage) {
		if(this.getTab() == null) {
			try {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Starting to open png file \"" + this.getPath() + "\"...", ConsoleLine.INFO));
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/Files/TabPNG.fxml"));
				loader.setController(this);
				this.setTab(loader.load());
				this.getTab().setText(this.getName());
				this.getTab().setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/tree_item/file_png.png"))));
				stage.getFilesTabPane().getTabs().add(this.getTab());
				stage.getFilesTabPane().getSelectionModel().select(this.getTab());
			} catch (FileNotFoundException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open PNG file \"" + this + "\" - FileNotFoundException in FileTXT.open", ConsoleLine.CRASH, exc));
			} catch (IOException exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open PNG file \"" + this + "\" - IOException in FileTXT.open", ConsoleLine.CRASH, exc));
			} catch (Exception exc) {
				ConsoleStage.defaultCns.printConsole(new ConsoleLine("FILES", "Fail to open PNG file \"" + this + "\" - Exception in FileTXT.open", ConsoleLine.CRASH, exc));
			}
		} else {
			stage.getFilesTabPane().getSelectionModel().select(this.getTab());
		}
	}
	
	@Override
	public void read() {
		try {
			BufferedImage image = ImageIO.read(this);
			pixels = new Pixel[image.getWidth()][image.getHeight()];
			for(int i = 0; i < image.getWidth(); i++) {
				for(int j = 0; j < image.getHeight(); j++) {
					Pixel pixel;
					int rgb = image.getRGB(i, j);
					if(rgb == 0) {
						pixel = new Pixel(i, j);
					} else {
						pixel = new Pixel(rgb, i, j);
					}
					pixel.setOnMousePressed(this);
					pixels[i] [j] = pixel;
				}
			}
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	@Override
	public void handle(MouseEvent arg0) {
		if(arg0.getButton() == MouseButton.PRIMARY) {
			Pixel subject = (Pixel) arg0.getSource();
			subject.setColor(this.colorPicker.getValue(), this);
			this.setSave(false);
		}
	}
	
	@Override
	public boolean save() {
		try {
			savedPNG.setPixels(this.pixels);
			BufferedImage image = new BufferedImage(this.pixels.length, this.pixels[0].length, BufferedImage.TYPE_INT_ARGB);
			int i = 0;
			for(Pixel[] pixelRow : this.pixels) {
				int j = 0;
				for(Pixel pixel : pixelRow) {
					image.setRGB(i, j, pixel.getColor());
					j++;
				}
				i++;
			}
			ImageIO.write(image, "png", this);
			this.register();
			return true;
		} catch (IOException exc) {
			exc.printStackTrace();
			return false;
		}
	}
	@Override
	public void register() {
		ManagmentStage.getOn().getTexturePack().getObjects().putFile(this.getPath(), this.savedPNG);
	}
	
	public void close() {
		if(!this.isSave()) {
			String confirm = MSDialog.showConfirm("Save changes", "Do you want to save the changes", "Yes", "No", "Cancel");
			if(confirm.equals("Yes")) {
				this.save();
				this.getTab().getTabPane().getTabs().remove(this.getTab());
				this.setSave(true);
				this.setTab(null);
			} else if(confirm.equals("No")) {
				this.getTab().getTabPane().getTabs().remove(this.getTab());
				this.setSave(true);
				this.setTab(null);
			}
		} else {
			this.getTab().getTabPane().getTabs().remove(this.getTab());
			this.setSave(true);
			this.setTab(null);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pathLabel.setText(this.getPath().substring(buildTexture.length()));
		ToggleGroup quills = new ToggleGroup();
		pen.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/pen.png"))));
		rubber.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/rubber.png"))));
		picker.setGraphic(new ImageView(new Image(Main.getApl().getClass().getResourceAsStream("/resource/icon/picker.png"))));
		pen.setToggleGroup(quills);
		rubber.setToggleGroup(quills);
		picker.setToggleGroup(quills);
		pen.setSelected(true);
		grid = new HBox();
		this.read();
		refreshPixels();
		container.setContent(grid);
		this.colorPicker.setValue(Color.BEIGE);
		changeZoom();
	}
	protected void refreshPixels() {
		sizeLabel.setText("width x height: " + pixels.length + " x " + pixels[0].length);
		grid.getChildren().clear();
		for(Pixel[] row : pixels) {
			VBox gridV = new VBox();
			for(Pixel pixel : row) {
				gridV.getChildren().add(pixel);
			}
			grid.getChildren().add(gridV);
			gridV.setBackground(background);
		}
	}

	public void resize() {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Files/ResizePNG.fxml"));
			loader.setController(new ResizePNG(this, stage));
			Scene scene = new Scene(loader.load());
			stage.setScene(scene);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
			stage.setTitle("Resize image - " + this.getName());
			scene.getStylesheets().add("resource/teme/teme" + Settings.getSetting(Settings.TEME) + ".css");
			stage.setResizable(false);
			stage.show();
		} catch (IOException exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open Settings stage - IOException in Settings.stage", ConsoleLine.CRASH, exc));
		} catch (Exception exc) {
			ConsoleStage.defaultCns.printConsole(new ConsoleLine("SETTINGS", "Fail to open Settings stage - Exception in Settings.stage", ConsoleLine.CRASH, exc));
		}
	}
	
	public void changeZoom() {
		for(ImageView[] row : pixels) {
			for(ImageView image : row) {
				image.setFitHeight((int) zoom.getValue());
				image.setFitWidth((int) zoom.getValue());
			}
		}	
	}
	
	public static class Pixel extends ImageView implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public final int posX;
		public final int posY;
		private int color;
		
		public Pixel(int posX, int posY) {
			super();
			this.posX = posX;
			this.posY = posY;
			this.color = 0;
			WritableImage wr = new WritableImage(1, 1);
			PixelWriter pw = wr.getPixelWriter();
			pw.setColor(0, 0, new Color(0, 0, 0, 0.01));
			this.setImage(wr);
		}
		public Pixel(int color, int posX, int posY) {
			super();
			this.posX = posX;
			this.posY = posY;
			this.color = color;
			WritableImage wr = new WritableImage(1, 1);
			PixelWriter pw = wr.getPixelWriter();
			pw.setArgb(0, 0, color);
			this.setImage(wr);
		}
		
		public void setColor(Color color, FilePNG file) {
			if(file.getPen().isSelected()) {
				WritableImage wrn = new WritableImage(1, 1);
				PixelWriter pwn = wrn.getPixelWriter();
				if(color.getOpacity() == 0) {
					pwn.setColor(0, 0, new Color(0, 0, 0, 0.01));
				} else {
					pwn.setColor(0, 0, color);
				}
				this.setImage(wrn);
				if(color.getOpacity() == 0) {
					this.color = 0;
				} else {
					this.color = this.getImage().getPixelReader().getArgb(0, 0);
				}
			} else if(file.getRubber().isSelected()) {
				WritableImage wrn = new WritableImage(1, 1);
				PixelWriter pwn = wrn.getPixelWriter();
				pwn.setColor(0, 0, new Color(0, 0, 0, 0.01));
				this.setImage(wrn);
				this.color = 0;
			} else if(file.getPicker().isSelected()) {
				if(this.color == 0) {
					file.getColorPicker().setValue(new Color(0, 0, 0, 0));
				} else {
					file.getColorPicker().setValue(this.getImage().getPixelReader().getColor(0, 0));
				}
			}
		}
		public int getColor() {
			return this.color;
		}
	}
	
	public ToggleButton getPen() {
		return pen;
	}
	public ToggleButton getRubber() {
		return rubber;
	}
	public Pixel[][] getPixels() {
		return pixels;
	}
	public void setPixels(Pixel[][] pixels) {
		this.pixels = pixels;
	}
	public ToggleButton getPicker() {
		return picker;
	}
	public ColorPicker getColorPicker() {
		return colorPicker;
	}
	@Override
	public PixelFormat<?> getPixelFormat() {
		return null;
	}
	@Override
	public void setArgb(int arg0, int arg1, int arg2) {
	}
	@Override
	public void setColor(int arg0, int arg1, Color arg2) {
	}
	@Override
	public <T extends Buffer> void setPixels(int arg0, int arg1, int arg2, int arg3, PixelFormat<T> arg4, T arg5,
			int arg6) {
	}
	@Override
	public void setPixels(int arg0, int arg1, int arg2, int arg3, PixelReader arg4, int arg5, int arg6) {
	}
	@Override
	public void setPixels(int arg0, int arg1, int arg2, int arg3, PixelFormat<ByteBuffer> arg4, byte[] arg5, int arg6,
			int arg7) {
	}
	@Override
	public void setPixels(int arg0, int arg1, int arg2, int arg3, PixelFormat<IntBuffer> arg4, int[] arg5, int arg6,
			int arg7) {
	}
}
