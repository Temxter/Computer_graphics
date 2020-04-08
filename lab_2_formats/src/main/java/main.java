import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {
    /**
     * Main container
     */
    private Stage stage;
    /**
     * Root Layout
     */
    private Parent rootLayout;

    public static void main(String[] args) throws Exception {
//
//
//        File file = new File("C:\\Users\\anrgr\\IdeaProjects\\ComputerGraphics\\lab2_formats\\src\\main\\resources\\img.jpg");
//
//        BufferedImage bufferedImage = ImageIO.read(file);
//        bufferedImage.
//
//        Metadata metadata = null;
//        try {
//            metadata = ImageMetadataReader.readMetadata(file);
//        } catch (ImageProcessingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                System.out.println(tag);
//            }
//        }
        Application.launch(args);
    }


    public void start(Stage stage) throws Exception {
        this.stage = stage;

        initRootLayout();
    }

    public Stage getStage() {
        return stage;
    }

    private void initRootLayout() {
        try {
            // loading root layout from fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("scene.fxml"));
            rootLayout = loader.load();

            // Creating scene with root layout
            Scene scene = new Scene(rootLayout);

            stage.setScene(scene);

            stage.setTitle("Image Meta Information");

            stage.show();

            // Controller gets access to the main app.
            Controller controller = loader.getController();
            controller.setMainApp(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
