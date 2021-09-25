import ru.haazad.cloud.client.service.CommandService;
import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;

public class SuccessUploadCommand implements CommandService {
    @Override
    public void processCommand(Command command) {
        if (command.haveImportantArgs(2)) {
            Factory.getActiveController().showInfoAlert((String) command.getArgs()[0]);
            Factory.getNetworkService().sendCommand(new Command(CommandName.LS, new Object[]{command.getArgs()[1]}));
        }
    }

    @Override
    public CommandName getCommand() {
        return CommandName.UPLOAD_SUCCESS;
    }
}
