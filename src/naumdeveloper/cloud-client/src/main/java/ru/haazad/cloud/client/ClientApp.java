import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {
    private static String username;

    private static WindowController activeController;
    private static WindowController secondaryController;

    public static WindowController getActiveController() {
        return activeController;
    }

    public static void setActiveController(WindowController controller) {
       activeController = controller;
    }

    public static WindowController getSecondaryController() {
        return secondaryController;
    }

    public static void setSecondaryController(WindowController secondaryController) {
        ClientApp.secondaryController = secondaryController;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String name) {
        username = name;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/loginWindow.fxml"));
        Parent parent = loader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("Cloud Client");
        primaryStage.setResizable(true);

        LoginWindowController controller = loader.getController();
        controller.setStage(primaryStage);
        setActiveController(controller);
        primaryStage.setOnCloseRequest((event) -> controller.close());
        primaryStage.show();
    }
}
