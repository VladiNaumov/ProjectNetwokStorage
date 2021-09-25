import ru.haazad.cloud.server.service.CommandService;

public class LoginCommand implements CommandService {

    @Override
    public Command processCommand(Command command) {
        if (!command.haveImportantArgs(2)) {
            return new Command(CommandName.ERROR, new Object[]{"Not enough parameters for login"});
        }
        if (!Factory.getDatabaseService().tryLogin(command.getArgs())) {
            return new Command(CommandName.ERROR, new String[]{"Incorrect login or password."});
        }
        return new Command(CommandName.LOGIN_SUCCESS, new Object[]{command.getArgs()[0]});
    }

    @Override
    public CommandName getCommand() {
        return CommandName.LOGIN;
    }
}
