import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.haazad.cloud.command.FileInfo;

public class CreateTableView {
    private static final int NAME_PREF_WIDTH = 340;
    private static final int TYPE_PREF_WIDTH = 60;
    private static final int SIZE_PREF_WIDTH = 155;

    public static TableView<FileInfo> makeTableView(TableView<FileInfo> view) {
        view.getColumns().addAll(createNameColumn(),
                createTypeColumn(),
                createSizeColumn());
        return view;
    }

    private static TableColumn<FileInfo,String> createNameColumn() {
        TableColumn<FileInfo, String> column = new TableColumn<>("Name");
        column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        column.setPrefWidth(NAME_PREF_WIDTH);
        return column;
    }

    private static TableColumn<FileInfo, String> createTypeColumn() {
        TableColumn<FileInfo, String> column = new TableColumn<>("Type");
        column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileType()));
        column.setPrefWidth(TYPE_PREF_WIDTH);
        return column;
    }

    private static TableColumn<FileInfo, Long> createSizeColumn() {
        TableColumn<FileInfo, Long> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        sizeColumn.setPrefWidth(SIZE_PREF_WIDTH);
        sizeColumn.setCellFactory(column -> new TableCell<FileInfo, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    String text = String.format("%,d bytes", item);
                    if (item == -1L) {
                        text = "DIR";
                    }
                    setText(text);
                }
            }
        });
        return sizeColumn;
    }


}

