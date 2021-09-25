import ru.haazad.cloud.server.service.CommandDictionaryService;
import ru.haazad.cloud.server.service.CommandService;
import ru.haazad.cloud.server.service.impl.ServerCommandDictionaryService;
import ru.haazad.cloud.server.service.impl.command.DownloadCommand;
import ru.haazad.cloud.server.service.impl.command.LoginCommand;
import ru.haazad.cloud.server.service.impl.command.RegisterCommand;
import ru.haazad.cloud.server.service.impl.command.ViewFilesOnServerCommand;

import java.util.Arrays;
import java.util.List;

public class Factory {

    public static ServerService getServerService() {
        return NettyServerService.initializeServerService();
    }

    public static DatabaseService initializeDbService() {
        return PostgreDatabaseService.initializeDbConnection();
    }

    public static DatabaseService getDatabaseService() {
        return NettyServerService.getDatabaseService();
    }

    public static CommandDictionaryService getCommandDictionary() {
        return new ServerCommandDictionaryService();
    }

    public static List<CommandService> getCommandServices() {
        return Arrays.asList(new LoginCommand(),
                new RegisterCommand(),
                new ViewFilesOnServerCommand(),
                new DownloadCommand());
    }
}
