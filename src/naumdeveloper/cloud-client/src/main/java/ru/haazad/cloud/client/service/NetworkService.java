import ru.haazad.cloud.command.Command;

public interface NetworkService {

    void sendCommand(Command command);

    void sendFile(String path);

    void closeConnection();

    boolean isConnected();

}
