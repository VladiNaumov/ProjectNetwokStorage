import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@ToString
public class FileInfo implements Serializable {
    private final String fileName;
    private final String path;
    private final String fileType;
    private final long size;

    public FileInfo(Path path) {
        try {
            this.path = path.toString();
            this.fileName = path.getFileName().toString();
            this.size = Files.size(path);
            this.fileType = Files.isDirectory(path) ? "DIR" : "FILE";
        } catch (IOException e) {
            throw new RuntimeException("Failed to collect information about the file along the path" + path.toString());
        }
    }
}
