import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class Controller {

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField pathTextField;

    @FXML
    private TableView<ImageMetaInfo> table;

    @FXML
    private TableColumn<ImageMetaInfo, String> filenameColumn;

    @FXML
    private TableColumn<ImageMetaInfo, String> extensionColumn;

    @FXML
    private TableColumn<ImageMetaInfo, String> resolutionColumn;

    @FXML
    private TableColumn<ImageMetaInfo, Long> sizeColumn;

    @FXML
    private TableColumn<ImageMetaInfo, Double> dpiColumn;

    @FXML
    private TableColumn<ImageMetaInfo, Integer> depthColumn;

    @FXML
    private TableColumn<ImageMetaInfo, String> compressedColumn;

    // Ссылка на главное приложение.
    private main mainApp;

    /**
     * Init method for controller class. It will be called after loading fxml file.
     *
     */
    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        //firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        //lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        filenameColumn.setCellValueFactory(new PropertyValueFactory<ImageMetaInfo, String>("name"));
        extensionColumn.setCellValueFactory(new PropertyValueFactory<ImageMetaInfo, String>("extension"));
        resolutionColumn.setCellValueFactory(new PropertyValueFactory<ImageMetaInfo, String>("resolution"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<ImageMetaInfo, Long>("size"));
        dpiColumn.setCellValueFactory(new PropertyValueFactory<ImageMetaInfo, Double>("dpi"));
        depthColumn.setCellValueFactory(new PropertyValueFactory<ImageMetaInfo, Integer>("bpi"));
        compressedColumn.setCellValueFactory(new PropertyValueFactory<ImageMetaInfo, String>("compressed"));
    }

    /**
     * Called by main class, which gives reference to itself
     *
     * @param mainApp - this переданное в main
     */
    public void setMainApp(main mainApp) {
        this.mainApp = mainApp;

        // Добавление в таблицу данных из наблюдаемого списка
        //sweetTable.setItems(mainApp.getPresentData());
        //reflashWeight();
    }

    /**
     * Event method for button "Open"
     *
     * @param event
     */
    @FXML
    private void onClickButton(ActionEvent event) {
        // Окно для выбора папки
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Folder with Images");
        File selectedFolder = directoryChooser.showDialog(mainApp.getStage());
        // Если объект null или не папка, то прерываем метод
        if (selectedFolder == null || !selectedFolder.isDirectory()) {
            return;
        }

        try {
            double height = Double.parseDouble(heightTextField.getText().replace(',', '.'));
            double width = Double.parseDouble(widthTextField.getText().replace(',', '.'));
            ImageMetaInfo.setPaperHeight(height);
            ImageMetaInfo.setPaperWidth(width);
        } catch (NumberFormatException e){
            e.printStackTrace();
        }

        pathTextField.setText(selectedFolder.getAbsolutePath());
        ObservableList<ImageMetaInfo> imageMetaInfoObservableList = FileProcessor.getImagesInfo(selectedFolder);
        table.setItems(imageMetaInfoObservableList);
    }




}
