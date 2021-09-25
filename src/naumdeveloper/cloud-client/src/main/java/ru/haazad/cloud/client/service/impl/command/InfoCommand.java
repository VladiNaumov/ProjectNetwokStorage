import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;
import ru.haazad.cloud.client.service.CommandService;

public class InfoCommand implements CommandService {

    @Override
    public void processCommand(Command command) {
        if (Factory.getSecondaryController() != null) {
            Factory.getSecondaryController().close();
        }
        Factory.getActiveController().showInfoAlert((String) command.getArgs()[0]);
    }

    @Override
    public CommandName getCommand() {
        return CommandName.INFO;
    }
}
