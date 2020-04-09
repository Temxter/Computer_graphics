import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFxml extends Application {

    /**
     * Main container
     */
    private Stage stage;
    /**
     * Root Layout
     */
    private Parent rootLayout;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        initRootLayout();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initRootLayout() {
        try {
            // loading root layout from fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
            rootLayout = loader.load();

            // Creating scene with root layout
            Scene scene = new Scene(rootLayout);

            stage.setScene(scene);

            stage.setTitle("Image Static Filters");

            stage.show();

            // Controller gets access to the main app.
            ViewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }
}
