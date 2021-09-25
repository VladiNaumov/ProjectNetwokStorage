import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;
import ru.haazad.cloud.client.factory.Factory;
import ru.haazad.cloud.client.service.NetworkService;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationFormController implements Initializable,WindowController{
    private static final Logger logger = LogManager.getLogger(RegistrationFormController.class);

    private NetworkService network;

    private Stage stage;

    public TextField loginField, emailField;
    public PasswordField passwordField, confirmPasswordField;

    public Stage getStage() {
        return stage;
    }

    @Override
    public void close() {
        Platform.runLater(() -> stage.close());
    }

    @Override
    public void showErrorAlert(String cause) {
        Platform.runLater(() -> Factory.getAlertService().showErrorAlert(cause));
    }

    public void showInfoAlert(String cause) {
        Platform.runLater(() -> Factory.getAlertService().showInfoAlert(cause));
    }

    @Override
    public void processAction(Object[] args) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.trace("Registration form invoked");
        network = Factory.getNetworkService();
    }

    public void register(ActionEvent event) {
        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty() || emailField.getText().isEmpty()) {
            Factory.getAlertService().showInfoAlert("Not all required fields have been filled in");
            logger.info("Not all required fields have been filled in");
            return;
        }
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            Factory.getAlertService().showInfoAlert("The password was incorrectly verified");
            logger.info("The password was incorrectly verified");
            return;
        }
        if (!network.isConnected()) {
            network = Factory.initializeNetworkService();
        }
        network.sendCommand(new Command(CommandName.REGISTER, new Object[]{
                loginField.getText(),
                Factory.getEncryptService().encryptPassword(passwordField.getText()),
                emailField.getText()
        }));
    }

    public void closeForm(ActionEvent event) {
        close();
    }
}
