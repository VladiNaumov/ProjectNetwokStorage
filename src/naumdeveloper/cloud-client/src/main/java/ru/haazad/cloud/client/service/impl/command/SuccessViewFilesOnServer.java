import lombok.extern.log4j.Log4j2;
import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;
import ru.haazad.cloud.client.service.CommandService;

@Log4j2
public class SuccessViewFilesOnServer implements CommandService {
    @Override
    public void processCommand(Command command) {
        if (command.haveImportantArgs(2)){
            Factory.getActiveController().processAction(command.getArgs());
            return;
        }
        log.error("Not enough parameters");
    }

    @Override
    public CommandName getCommand() {
        return CommandName.LS;
    }
}
