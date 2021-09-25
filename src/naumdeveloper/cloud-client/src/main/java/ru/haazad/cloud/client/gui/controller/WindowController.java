import javafx.stage.Stage;

public interface WindowController {

    void setStage(Stage stage);

    Stage getStage();

    void close();

    void showErrorAlert(String cause);

    void showInfoAlert(String cause);

    void processAction(Object[] args);
}
