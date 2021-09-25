import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.client.gui.util.AlertUtil;

public class ClientAlertUtil implements AlertUtil {
    private static final Logger logger = LogManager.getLogger(ClientAlertUtil.class);

    private ClientAlertUtil() {
    }

    public static ClientAlertUtil getAlertService() {
        return new ClientAlertUtil();
    }

    @Override
    public void showErrorAlert(Object cause) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        showAlert(alert, cause);
    }

    @Override
    public void showInfoAlert(Object cause) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        showAlert(alert, cause);
    }

    private void showAlert(Alert alert, Object cause) {
        alert.setTitle("Cloud Client");
        alert.setContentText((String) cause);
        alert.setHeaderText(null);
        alert.showAndWait();
        logger.error(cause);
    }
}
