import ru.haazad.cloud.client.service.CommandService;
import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;

public class ErrorCommand implements CommandService {
    @Override
    public void processCommand(Command command) {
        Factory.getActiveController().showErrorAlert((String) command.getArgs()[0]);
    }

    @Override
    public CommandName getCommand() {
        return CommandName.ERROR;
    }
}
