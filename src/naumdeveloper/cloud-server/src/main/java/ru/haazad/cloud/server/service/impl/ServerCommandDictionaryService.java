import ru.haazad.cloud.server.service.CommandDictionaryService;
import ru.haazad.cloud.server.service.CommandService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerCommandDictionaryService implements CommandDictionaryService {
    private final Map<CommandName, CommandService> commandServiceMap;

    public ServerCommandDictionaryService() {
        this.commandServiceMap = Collections.unmodifiableMap(getCommandDictionary());
    }

    private Map<CommandName, CommandService> getCommandDictionary() {
        List<CommandService> commandServices = Factory.getCommandServices();

        Map<CommandName, CommandService> commandServiceMap = new HashMap<>();
        for (CommandService cs: commandServices) {
            commandServiceMap.put(cs.getCommand(), cs);
        }
        return commandServiceMap;
    }

    @Override
    public Command processCommand(Command command) {
        if (commandServiceMap.containsKey(command.getCommandName())) {
            return commandServiceMap.get(command.getCommandName()).processCommand(command);
        }
        throw new IllegalArgumentException("The command " + command.getCommandName() + " does not exist");
    }
}
