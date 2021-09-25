import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.command.CommandName;
import ru.haazad.cloud.client.service.CommandService;
import ru.haazad.cloud.client.service.CommandDictionaryService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientCommandDictionaryService implements CommandDictionaryService {
    private static final Logger logger = LogManager.getLogger(ClientCommandDictionaryService.class);

    private final Map<CommandName, CommandService> dictionaryService;

    public ClientCommandDictionaryService(){
        this.dictionaryService = Collections.unmodifiableMap(getCommandDictionary());
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
    public void processCommand(Command command) {
        if (dictionaryService.containsKey(command.getCommandName())) {
            dictionaryService.get(command.getCommandName()).processCommand(command);
            return;
        }
        throw new IllegalArgumentException("The command " + command.getCommandName() + " does not exist");
    }


}
