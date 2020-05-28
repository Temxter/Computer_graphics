import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.io.File;


public class ViewController {

    @FXML
    private TextField x0_cutbox;
    @FXML
    private TextField y0_cutbox;
    @FXML
    private TextField x1_cutbox;
    @FXML
    private TextField y1_cutbox;
    @FXML
    private Button clear_btn_cutbox;
    @FXML
    private TextField x0_line;
    @FXML
    private TextField y0_line;
    @FXML
    private TextField x1_line;
    @FXML
    private TextField y1_line;
    @FXML
    private TextField x_polygon;
    @FXML
    private TextField y_polygon;
    @FXML
    private Button add_btn_polygon;
    @FXML
    private Button finish_btn_polygon;
    @FXML
    private Button clear_btn_polygon;
    @FXML
    private Canvas canvas;

    private XYplane xyPlane;

    private LineSegment cutRect, line;

    private Polygon polygon;


    @FXML
    void onClearPolygon(ActionEvent event) {

    }

    @FXML
    void onAddPolygon(ActionEvent event) {

    }

    @FXML
    void onClearCutbox(ActionEvent event) {

    }

    @FXML
    void onClearLine(ActionEvent event) {

    }

    @FXML
    void onFinishPolygon(ActionEvent event) {

    }

    @FXML
    void onOkCutbox(ActionEvent event) {
        double x0 = Double.parseDouble(x0_cutbox.getText());
        double y0 = Double.parseDouble(y0_cutbox.getText());
        double x1 = Double.parseDouble(x1_cutbox.getText());
        double y1 = Double.parseDouble(y1_cutbox.getText());
        cutRect = new LineSegment(x0, y0, x1, y1);
        System.out.println("Rect: " + cutRect);

        xyPlane.paintRectangle(cutRect, Color.RED);
    }

    @FXML
    void onOkLine(ActionEvent event) {
        double x0 = Double.parseDouble(x0_line.getText());
        double y0 = Double.parseDouble(y0_line.getText());
        double x1 = Double.parseDouble(x1_line.getText());
        double y1 = Double.parseDouble(y1_line.getText());
        line = new LineSegment(x0, y0, x1, y1);
        System.out.println("Line: " + line);

        Algorithms.paintCutLine(cutRect, line, xyPlane);
    }

    @FXML
    void onClear(ActionEvent event) {
        canvas.getGraphicsContext2D().clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
        xyPlane.paintXY();
    }

    public void initialize() {
        xyPlane = new XYplane(canvas);
    }


    public void onSave() {
        try {
            Image snapshot = canvas.snapshot(null, null);

            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

    public void onExit() {
        Platform.exit();
    }

}