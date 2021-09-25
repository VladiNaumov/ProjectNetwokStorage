import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Command implements Serializable {
    private CommandName commandName;
    private Object[] args;

    public boolean haveImportantArgs(int importantArgs) {
        return args.length == importantArgs;
    }
}
