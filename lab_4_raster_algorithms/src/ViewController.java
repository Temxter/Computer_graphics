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
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.io.File;


public class ViewController {
    @FXML
    private TextField xStart;
    @FXML
    private TextField xFinish;
    @FXML
    private TextField kText;
    @FXML
    private TextField bText;
    @FXML
    private ComboBox comboBox;
    @FXML
    private Button buttonOK;
    @FXML
    private Canvas canvas;

    @FXML
    private Text x0;
    @FXML
    private Text x1;
    @FXML
    private Text k;
    @FXML
    private Text b;

    private XYplane xyPlane;

    private ObservableList<String> comboBoxValues = FXCollections.observableArrayList(
            "Step-by-step algorithm",
            "CDA algorithm",
            "Bresenham algorithm",
            "Bresenham algorithm (circle)");

    public void setLinearInput(){
        x0.setText("x0");
        x1.setText("x1");
        k.setText("k");
        b.setVisible(true);
        bText.setVisible(true);
    }

    public void setCircleInput(){
        x0.setText("x0");
        x1.setText("y0");
        k.setText("R");
        b.setVisible(false);
        bText.setVisible(false);
    }

    public void initialize() {
        comboBox.setItems(comboBoxValues);
        comboBox.setValue(comboBoxValues.get(0));
        xyPlane = new XYplane(canvas);
    }

    public void onButtonOK(){
        double x0 = Double.parseDouble(xStart.getText());
        double x1 = Double.parseDouble(xFinish.getText());
        double k = Double.parseDouble(kText.getText());
        double b = Double.parseDouble(bText.getText());

        LinearEquation equation = new LinearEquation(x0, x1, k, b);

        String algorithm = (String)comboBox.getValue();
        switch (algorithm) {
            case "Step-by-step algorithm": Algorithms.paintStepByStep(equation, xyPlane); break;
            case "CDA algorithm": Algorithms.paintCDA(equation, xyPlane); break;
            case "Bresenham algorithm": Algorithms.paintBresenham(equation, xyPlane); break;
            case "Bresenham algorithm (circle)":
                CircleEquation circle = new CircleEquation(x0, x1, k);
                Algorithms.paintBresenhamCircle(circle, xyPlane);
                break;
        }
    }

    public void onComboBoxChanged(ActionEvent actionEvent) {
        if (comboBox.getValue().equals("Bresenham algorithm (circle)"))
            setCircleInput();
        else
            setLinearInput();
    }

    public void onButtonClear(ActionEvent actionEvent) {
        canvas.getGraphicsContext2D().clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
        xyPlane.paintXY();
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