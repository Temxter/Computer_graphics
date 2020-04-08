import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewController {
    @FXML
    private TextField brushSize;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private CheckBox eraser;
    @FXML
    ComboBox comboBoxFilter;
    @FXML
    private Canvas canvasPaint;
    @FXML
    private Canvas canvasFilter;


    // call with show view.fxml
    public void initialize() {
        GraphicsContext g = canvasPaint.getGraphicsContext2D();

        canvasPaint.setOnMouseDragged(e -> {
            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if (eraser.isSelected()){
                g.clearRect(x, y, size, size);
            } else {
                g.setFill(colorPicker.getValue());
                g.fillRect(x, y, size, size);
            }
        });

        //init comboBox
        comboBoxFilter.getItems().addAll(new String[]{noFilter, filterStaticMin, filterStaticMax, filterStaticMean});
        comboBoxFilter.setValue(noFilter);
    }

    private final String filterStaticMin = "Static Min",
    filterStaticMax = "Static Max",
    filterStaticMean = "Static Mean",
    noFilter = "Clear Filter";

    @FXML
    void setFilter(ActionEvent actionEvent){
        String selectedFilter = (String)comboBoxFilter.getValue();
        switch (selectedFilter){
            case filterStaticMin:
                break;
            case filterStaticMax:
                break;
            case filterStaticMean:
                break;

            default: clearCanvasFilter();
                break;
        }
    }

    void clearCanvasFilter(){
        GraphicsContext g = canvasFilter.getGraphicsContext2D();
        g.clearRect(0, 0, canvasFilter.getWidth(), canvasFilter.getHeight());
    }

    @FXML
    void onLoad(ActionEvent actionEvent) {
        try{
            GraphicsContext g = canvasPaint.getGraphicsContext2D();
            //TODO module window to open
            String filepath = "image.png";
            File file = new File(filepath);
            Image image = new Image("file:\\" + file.getAbsolutePath());
            g.drawImage(image, 0, 0, 300, 300);
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

    @FXML
    void onSave(ActionEvent actionEvent) {
        try{
            Image snapshot = canvasPaint.snapshot(null, null);
            //TODO module window to save
            String filename = "image.png";
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File(filename));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

    @FXML
    void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
