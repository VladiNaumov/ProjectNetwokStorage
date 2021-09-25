import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;
import ru.haazad.cloud.client.service.CommandService;


public class SuccessLogin implements CommandService {
    private static final Logger logger = LogManager.getLogger(SuccessLogin.class);

    @Override
    public void processCommand(Command command) {
        if (command.haveImportantArgs(1)) {
            Factory.getActiveController().processAction(command.getArgs());
            return;
        }
        logger.error("Problem to activate application window form");
        Factory.getActiveController().showErrorAlert("Problem to activate application window form");
    }

    @Override
    public CommandName getCommand() {
        return CommandName.LOGIN_SUCCESS;
    }
}
