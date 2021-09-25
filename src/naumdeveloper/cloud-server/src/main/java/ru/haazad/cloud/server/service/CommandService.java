public interface CommandService {
    Command processCommand(Command command);

    CommandName getCommand();
}
