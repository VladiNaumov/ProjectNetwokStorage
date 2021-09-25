import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;
import ru.haazad.cloud.client.factory.Factory;
import ru.haazad.cloud.client.service.NetworkService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController implements Initializable, WindowController {
    private static final Logger logger = LogManager.getLogger(LoginWindowController.class);

    private Stage stage;

    private NetworkService networkService;

    public TextField loginField;
    public PasswordField passwordField;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void login(ActionEvent event) {
        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Factory.getAlertService().showErrorAlert("Login/password cannot be empty");
            return;
        }
        if (!networkService.isConnected()) {
            networkService = Factory.initializeNetworkService();
        }
        networkService.sendCommand(new Command(CommandName.LOGIN, new Object[]{
                loginField.getText(),
                Factory.getEncryptService().encryptPassword(passwordField.getText())
        }));
        loginField.clear();
        passwordField.clear();
    }

    public void close() {
        networkService.closeConnection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        networkService = Factory.initializeNetworkService();
    }

    public void showErrorAlert(String cause) {
        Platform.runLater(() -> Factory.getAlertService().showErrorAlert(cause));
    }

    public void showInfoAlert(String cause) {
        Platform.runLater(() -> Factory.getAlertService().showInfoAlert(cause));
    }

    @Override
    public void processAction(Object[] args) {
        Factory.setUsername((String) args[0]);
        try {
            FXMLLoader secondary = new FXMLLoader(getClass().getClassLoader().getResource("view/applicationWindow.fxml"));
            Parent child = secondary.load();
            WindowController secondaryController = secondary.getController();
            WindowController loginController = Factory.getActiveController();
            Factory.setActiveController(secondaryController);
            Platform.runLater(() -> {
                loginController.getStage().close();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Cloud Client.");
                stage.setResizable(false);
                stage.setScene(new Scene(child));
                secondaryController.setStage(stage);
                stage.setOnCloseRequest((event) -> secondaryController.close());
                stage.show();
            });
        } catch (IOException e) {
            logger.throwing(Level.ERROR, e);
        }
    }

    public void callRegistrationForm(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/registrationWindow.fxml"));
            Parent child = loader.load();
            Stage stage = new Stage();
            WindowController controller = loader.getController();
            Factory.setSecondaryController(controller);
            controller.setStage(stage);
            stage.setTitle("Cloud Client. Register new user");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(child));
            stage.setOnCloseRequest((event) -> stage.close());
            stage.show();
        } catch (IOException e) {
            logger.throwing(Level.ERROR, e);
        }
    }
}
