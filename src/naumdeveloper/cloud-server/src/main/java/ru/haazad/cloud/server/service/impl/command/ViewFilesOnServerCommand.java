import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.haazad.cloud.server.service.CommandService;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ViewFilesOnServerCommand implements CommandService {

    @Override
    public Command processCommand(Command command) {
        String srcDirectory = (String) command.getArgs()[0];
        List<FileInfo> listFiles = getFilesInDirectory(getUserDirectory(srcDirectory));
        return new Command(CommandName.LS, new Object[]{srcDirectory, listFiles});
    }

    private List<FileInfo> getFilesInDirectory(Path path) {
        List<FileInfo> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path p : stream) {
                list.add(new FileInfo(p));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Path getUserDirectory(String directory) {
        String url = ConfigProperty.getStorage() + "/" + directory;
        Path path = Paths.get(url);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                log.throwing(Level.ERROR, e);
            }
        }
        return path;
    }

    @Override
    public CommandName getCommand() {
        return CommandName.LS;
    }
}
