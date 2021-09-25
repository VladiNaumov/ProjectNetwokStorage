import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;

public interface CommandService {
    void processCommand(Command command);

    CommandName getCommand();
}
