import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.server.service.CommandService;

public class RegisterCommand implements CommandService {
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

    @Override
    public Command processCommand(Command command) {
        if (!command.haveImportantArgs(3)) {
            return new Command(CommandName.ERROR, new Object[]{"Not enough parameters for register"});
        }
        if (!Factory.getDatabaseService().checkLogin(command.getArgs())) {
            if (Factory.getDatabaseService().tryRegister(command.getArgs())) {
                return new Command(CommandName.INFO, new Object[]{"Registration was successful. Please log in."});
            }
        }
        logger.trace("Registration new user is failed");
        return new Command(CommandName.ERROR, new Object[]{"Register new user is failed"});
    }

    @Override
    public CommandName getCommand() {
        return CommandName.REGISTER;
    }
}
