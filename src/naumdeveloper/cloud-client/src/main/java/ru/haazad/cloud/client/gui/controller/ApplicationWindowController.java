import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;
import ru.haazad.cloud.client.factory.Factory;
import ru.haazad.cloud.client.service.NetworkService;
import ru.haazad.cloud.command.FileInfo;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

@Log4j2
public class ApplicationWindowController implements Initializable, WindowController {

    private Stage stage;

    public TextField clientPathFolder, serverPathFolder;
    public TableView<FileInfo> clientDirectoryView, serverDirectoryView;
    public Button uploadButton, downloadButton;

    private NetworkService network;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = Factory.getNetworkService();
        Factory.makeTableView(clientDirectoryView);
        Factory.makeTableView(serverDirectoryView);
        clientPathFolder.appendText(Factory.getView().getDirectoryPath(null).toString());
        listClientDirectory(Factory.getView().getDirectoryPath(null));
        network.sendCommand(new Command(CommandName.LS, new Object[]{Factory.getUsername()}));
    }

    private void listClientDirectory(Path path) {
        clientDirectoryView.getItems().clear();
        clientPathFolder.clear();
        clientPathFolder.appendText(path.toString());
        clientDirectoryView.getItems().addAll(Factory.getView().getFilesInDirectory(path));
        clientDirectoryView.sort();
    }

    public void moveToClientDirectory(ActionEvent event) {
        displayClientDirectory(clientPathFolder.getText());
    }

    public void moveToServerDirectory(ActionEvent event) {
        displayServerDirectory(serverPathFolder.getText());
    }

    public void moveToClientDirectoryByMouseEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            displayClientDirectory(clientPathFolder.getText() + "/" + getSelectedItem(clientDirectoryView));
        }
    }

    private void displayClientDirectory(String path) {
        clientDirectoryView.getItems().clear();
        listClientDirectory(Factory.getView().getDirectoryPath(path));
    }

    public void moveToServerDirectoryByMouseEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            displayServerDirectory(serverPathFolder.getText() + "/" + getSelectedItem(serverDirectoryView));
        }
    }

    private void displayServerDirectory(String path) {
        network.sendCommand(new Command(CommandName.LS, new Object[]{path}));
    }

    private String getSelectedItem(TableView<FileInfo> view) {
        return view.getSelectionModel().getSelectedItem().getFileName();
    }

    public void upToClientDirectory(ActionEvent event) {
        String path = clientPathFolder.getText();
        if (!path.equals("C:\\")) {
            listClientDirectory(Paths.get(path).getParent());
        } else {
            Factory.getAlertService().showInfoAlert(String.format("Directory %s is root directory", path));
        }
    }

    public void upToServerDirectory(ActionEvent event) {
        String path = serverPathFolder.getText();
        String[] arr;
        if (path.contains("/")) {
            arr = serverPathFolder.getText().replace("/", " ").split(" ");
        } else {
            arr = serverPathFolder.getText().replace("\\", " ").split(" ");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            sb.append(arr[i]).append("\\");
        }
        displayServerDirectory(sb.toString());
    }

    public void uploadFileOnServer(ActionEvent event) {
        String srcPath = clientPathFolder.getText() + "\\" + getSelectedItem(clientDirectoryView);
        String dstPath = serverPathFolder.getText();
        Object[] cmdArgs = {new FileInfo(Paths.get(srcPath)), dstPath};
        network.sendCommand(new Command(CommandName.PREPARE_UPLOAD, cmdArgs));
    }

    public void downloadFile(ActionEvent event) {
        String srcPath = serverPathFolder.getText() + "\\" + getSelectedItem(serverDirectoryView);
        String dstPath = clientPathFolder.getText();
        network.sendCommand(new Command(CommandName.DOWNLOAD, new Object[]{srcPath, dstPath}));
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void close() {
        stage.close();
        network.closeConnection();
    }

    @Override
    public void showErrorAlert(String cause) {
        Platform.runLater(() -> Factory.getAlertService().showErrorAlert(cause));
    }

    @Override
    public void showInfoAlert(String cause) {
        Platform.runLater(() -> Factory.getAlertService().showInfoAlert(cause));
    }

    @Override
    public void processAction(Object[] args) {
        String path = (String) args[0];
        List<FileInfo> listFiles = (List<FileInfo>) args[1];
        Platform.runLater(() -> {
                    serverPathFolder.clear();
                    serverPathFolder.appendText(path);
                    serverDirectoryView.getItems().clear();
                    serverDirectoryView.getItems().addAll(listFiles);
                    serverDirectoryView.sort();
                }
        );
    }
}
