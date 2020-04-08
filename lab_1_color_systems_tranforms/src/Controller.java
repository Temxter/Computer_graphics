import com.sun.webkit.Timer;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import transforms.CMYKModel;
import transforms.HSLModel;
import transforms.RGBModel;
import transforms.Transformations;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Circle circle;

    @FXML
    private ColorPicker colorpicker;

    @FXML
    private TextField textfield_rgb_1;

    @FXML
    private TextField textfield_rgb_2;

    @FXML
    private TextField textfield_rgb_3;

    @FXML
    private Button button_apply_rgb;

    @FXML
    private TextField textfield_cmyk_1;

    @FXML
    private TextField textfield_cmyk_2;

    @FXML
    private TextField textfield_cmyk_3;

    @FXML
    private TextField textfield_cmyk_4;

    @FXML
    private Button button_apply_cmyk;

    @FXML
    private TextField textfield_hls_1;

    @FXML
    private TextField textfield_hls_2;

    @FXML
    private TextField textfield_hls_3;

    @FXML
    private Button button_apply_hls;

    @FXML
    private Slider slider_rgb_1;

    @FXML
    private Slider slider_rgb_2;

    @FXML
    private Slider slider_rgb_3;

    @FXML
    private Slider slider_cmyk_1;

    @FXML
    private Slider slider_cmyk_2;

    @FXML
    private Slider slider_cmyk_3;

    @FXML
    private Slider slider_cmyk_4;

    @FXML
    private Slider slider_hls_1;

    @FXML
    private Slider slider_hls_2;

    @FXML
    private Slider slider_hls_3;

    @FXML
    private Text text_info;

    private enum Models{
        RGB, CMYK, HSL;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindFieldSlider(textfield_rgb_1, slider_rgb_1, Models.RGB);
        bindFieldSlider(textfield_rgb_2, slider_rgb_2, Models.RGB);
        bindFieldSlider(textfield_rgb_3, slider_rgb_3, Models.RGB);

        bindFieldSlider(textfield_cmyk_1, slider_cmyk_1, Models.CMYK);
        bindFieldSlider(textfield_cmyk_2, slider_cmyk_2, Models.CMYK);
        bindFieldSlider(textfield_cmyk_3, slider_cmyk_3, Models.CMYK);
        bindFieldSlider(textfield_cmyk_4, slider_cmyk_4, Models.CMYK);

        bindFieldSlider(textfield_hls_1, slider_hls_1, Models.HSL);
        bindFieldSlider(textfield_hls_2, slider_hls_2, Models.HSL);
        bindFieldSlider(textfield_hls_3, slider_hls_3, Models.HSL);

        colorpicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Color color = colorpicker.getValue();
                transformations.setRGB(color.getRed(), color.getGreen(), color.getBlue());
                updateFields();
                circle.setFill(color);
            }
        });
    }

    void bindFieldSlider(TextField t, Slider s, Models model){

        t.textProperty().bindBidirectional(s.valueProperty(),
                NumberFormat.getNumberInstance(Locale.US));
//        s.valueProperty().addListener((observableValue, s1, t1) -> {
//            updateColorAndFields(model);
//        });

    }


    private Transformations transformations = new Transformations(0.0, 0.0, 0.0);

    private void log(String s){
        System.out.println(s);
    }

    @FXML
    void onKeyTyped(KeyEvent event) {
//        if (event.getSource().equals(textfield_rgb_1) || event.getSource().equals(textfield_rgb_2) ||
//                event.getSource().equals(textfield_rgb_3))
//            updateColorAndFields(Models.RGB);
    }

    @FXML
    void onApplyClick(ActionEvent actionEvent) {
        if (button_apply_rgb.equals(actionEvent.getSource())) {
            updateColorAndFields(Models.RGB);
        } else if (button_apply_hls.equals(actionEvent.getSource())) {
            updateColorAndFields(Models.HSL);
        } else if (button_apply_cmyk.equals(actionEvent.getSource())) {
            updateColorAndFields(Models.CMYK);
        }
    }

    void toCorrectTransformations(){
        String info;
        if (transformations.toCorrectValues()){
            info = "Correct transformations.";
        } else {
            info = "Inaccurate transformation!";
        }
        text_info.setText(info);
    }

    void updateColorAndFields(Models model) {
        try {
            if (model.equals(Models.RGB)) {
                readRgbFields();
                updateFields();
            } else if (model.equals(Models.HSL)) {
                readHlsFields();
                updateFields();
            } else if (model.equals(Models.CMYK)) {
                readCMYKFields();
                updateFields();
            }

            RGBModel rgbModel = transformations.getRgb();

            //set color to ColorPicker
            Color color = new Color(rgbModel.getRed(), rgbModel.getGreen(), rgbModel.getBlue(), 1.0);
            colorpicker.setValue(color);
            //set color to circle
            circle.setFill(color);
        } catch (Exception e){
            e.printStackTrace();
            //fillFieldsNull();
        }
    }

    void fillFieldsNull(){
        textfield_rgb_1.setText("0.0");
        textfield_rgb_2.setText("0.0");
        textfield_rgb_3.setText("0.0");

        textfield_cmyk_1.setText("0.0");
        textfield_cmyk_2.setText("0.0");
        textfield_cmyk_3.setText("0.0");
        textfield_cmyk_4.setText("0.0");

        textfield_hls_1.setText("0.0");
        textfield_hls_2.setText("0.0");
        textfield_hls_3.setText("0.0");
    }

    synchronized void updateFields() {
        toCorrectTransformations();

        RGBModel rgbModel = transformations.getRgb();
        updateField(textfield_rgb_1, rgbModel.getRed());
        updateField(textfield_rgb_2, rgbModel.getGreen());
        updateField(textfield_rgb_3, rgbModel.getBlue());

        HSLModel hslModel = transformations.getHsl();
        updateField(textfield_hls_1, hslModel.getHue());
        updateField(textfield_hls_2, hslModel.getSaturation());
        updateField(textfield_hls_3, hslModel.getLightness());

        CMYKModel cmykModel = transformations.getCmyk();
        updateField(textfield_cmyk_1, cmykModel.getCyan());
        updateField(textfield_cmyk_2, cmykModel.getMagenta());
        updateField(textfield_cmyk_3, cmykModel.getYellow());
        updateField(textfield_cmyk_4, cmykModel.getKey());
    }

    void updateField(TextField t, Double color){

        t.setText(String.format(Locale.US, "%.2f", color));
    }

    synchronized void readRgbFields() {
        double red = Double.parseDouble(textfield_rgb_1.getText());
        double green = Double.parseDouble(textfield_rgb_2.getText());
        double blue = Double.parseDouble(textfield_rgb_3.getText());
        transformations.setRGB(red, green, blue);
    }

    synchronized void readCMYKFields() {
        double c = Double.parseDouble(textfield_cmyk_1.getText());
        double m = Double.parseDouble(textfield_cmyk_2.getText());
        double y = Double.parseDouble(textfield_cmyk_3.getText());
        double k = Double.parseDouble(textfield_cmyk_4.getText());
        transformations.setCMYK(c, m, y, k);
    }

    synchronized void readHlsFields() {
        double h = Double.parseDouble(textfield_hls_1.getText());
        double l = Double.parseDouble(textfield_hls_2.getText());
        double s = Double.parseDouble(textfield_hls_3.getText());
        transformations.setHSL(h, l, s);
    }


}
