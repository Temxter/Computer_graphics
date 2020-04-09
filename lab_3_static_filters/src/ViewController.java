import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    @FXML
    private TextField textFieldFile;

    @FXML
    private Button buttonOpen;

    @FXML
    private Button buttonOk;

    @FXML
    private Text textOriginallyImage;

    @FXML
    private Text textFilter1;

    @FXML
    private ImageView filter1;

    @FXML
    private Text textFilter2;

    @FXML
    private ImageView filter2;

    @FXML
    private Text textFilter3;

    @FXML
    private ImageView filter3;

    @FXML
    private Text textInfo;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button buttonSave;

    @FXML
    private ImageView originallyImage;

    @FXML
    private Text textApplyFilters;

    private MainFxml mainApp;

    private ImageProcessing imageProcessing;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAllInvisible();
    }

    private void setAllInvisible(){
        progressBar.setVisible(false);
        buttonOk.setVisible(true);
        buttonSave.setVisible(false);
        originallyImage.setVisible(false);
        textOriginallyImage.setVisible(false);
        textApplyFilters.setVisible(false);
        buttonOk.setVisible(false);
        textFilter1.setVisible(false);
        textFilter2.setVisible(false);
        textFilter3.setVisible(false);
        filter1.setVisible(false);
        filter2.setVisible(false);
        filter3.setVisible(false);
    }

    @FXML
    private void onClickOpenButton(ActionEvent event) {
        setAllInvisible();

        // Окно для выбора папки
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        File selectedFile = fileChooser.showOpenDialog(mainApp.getStage());
        // Если объект null или не файл, то прерываем метод
        if (selectedFile == null || !selectedFile.isFile()) {
            return;
        }

        try {
            imageProcessing = new ImageProcessing(selectedFile.getAbsolutePath());
            textInfo.setText("Loaded " + selectedFile.getName() + ". Click ok to apply filters.");
            textOriginallyImage.setVisible(true);
            originallyImage.setVisible(true);
            originallyImage.setImage(SwingFXUtils.toFXImage(imageProcessing.getBufferedImage(), null));
            textApplyFilters.setVisible(true);
            buttonOk.setVisible(true);
        } catch (Exception e){
            textInfo.setText("Error image loading: " + e.getMessage());
        }

    }

    ImageProcessing imageWithFilterMin, imageWithFilterMax, imageWithFilterMean;

    @FXML
    private void onClickOkButton(){
        if (imageProcessing == null)
            return;
        try {
            textInfo.setText("Please, wait. Processing image!");

            //in this way not working
            //progressBar.setVisible(true);
            progressBar.setProgress(0.0);

            imageWithFilterMin = imageProcessing.applyStaticFilter(ImageProcessing.StaticFilters.Min,3);
            filter1.setImage(SwingFXUtils.toFXImage(imageWithFilterMin.getBufferedImage(), null));
            filter1.setVisible(true);
            textFilter1.setVisible(true);
            progressBar.setProgress(0.33);

            imageWithFilterMean = imageProcessing.applyStaticFilter(ImageProcessing.StaticFilters.Mean,3);
            filter2.setImage(SwingFXUtils.toFXImage(imageWithFilterMean.getBufferedImage(), null));
            filter2.setVisible(true);
            textFilter2.setVisible(true);
            progressBar.setProgress(0.67);

            imageWithFilterMax = imageProcessing.applyStaticFilter(ImageProcessing.StaticFilters.Max,3);
            filter3.setImage(SwingFXUtils.toFXImage(imageWithFilterMax.getBufferedImage(), null));
            filter3.setVisible(true);
            textFilter3.setVisible(true);
            progressBar.setProgress(1.0);

            textInfo.setText("Processing has completed!");
            buttonSave.setVisible(true);
        }  catch (Exception e){
            textInfo.setText("Error image processing: " + e.getMessage());
            buttonOk.setVisible(false);
            buttonSave.setVisible(false);
            filter1.setVisible(false);
            filter2.setVisible(false);
            filter3.setVisible(false);
        }
    }

    @FXML
    private void onClickSaveButton(){
        try {
            String pathFiles = "";

            pathFiles += imageWithFilterMin.saveImage(null) + "\n";
            pathFiles += imageWithFilterMax.saveImage(null) + "\n";
            pathFiles += imageWithFilterMean.saveImage(null);

            textInfo.setText("Successfully saved files in app folder.\n" + pathFiles);
        } catch (Exception e){
            textInfo.setText("Error image saving: " + e.getMessage());
            buttonOk.setVisible(false);
            buttonSave.setVisible(false);
        }

    }


    public void setMainApp(MainFxml mainFxml) {
        mainApp = mainFxml;
    }
}
