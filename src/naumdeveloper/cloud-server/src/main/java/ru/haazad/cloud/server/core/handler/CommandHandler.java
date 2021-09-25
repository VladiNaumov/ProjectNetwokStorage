import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.server.config.ConfigProperty;
import ru.haazad.cloud.server.factory.Factory;
import ru.haazad.cloud.server.service.CommandDictionaryService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandHandler extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LogManager.getLogger(CommandHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command command) throws IOException {
        logger.debug("Input command " + command.toString());
        if (command.getCommandName() == CommandName.PREPARE_UPLOAD) {
            FileInfo info = (FileInfo) command.getArgs()[0];
            FileHandler.setFilename(info.getFileName());
            FileHandler.setDstDirectory((String) command.getArgs()[1]);
            FileHandler.setFileSize(info.getSize());
            ctx.writeAndFlush(new Command(CommandName.READY, command.getArgs()));
            SwitchPipelineUtil.switchToFileUpload(ctx);
        } else if (command.getCommandName() == CommandName.DOWNLOAD) {
            Path path = Paths.get((ConfigProperty.getStorage() + "\\" + command.getArgs()[0]));
            FileInfo info = new FileInfo(path);
            ctx.writeAndFlush(new Command(CommandName.PREPARE_DOWNLOAD, new Object[]{info, path.toString(), command.getArgs()[1]}));
        } else if (command.getCommandName() == CommandName.READY){
            String path = (String) command.getArgs()[1];
            SwitchPipelineUtil.switchToFileUpload(ctx);
            ChannelFuture future = ctx.channel().writeAndFlush(new ChunkedFile(new File(path)));
            future.addListener((ChannelFutureListener) listener -> {
                logger.debug("File transfer success");
                SwitchPipelineUtil.switchAfterUpload(ctx);
            });
        } else {
            CommandDictionaryService commandDictionary = Factory.getCommandDictionary();
            Command commandResult = commandDictionary.processCommand(command);
            logger.debug("Result is " + commandResult.toString());
            ctx.writeAndFlush(commandResult);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        logger.throwing(Level.ERROR, cause);
    }
}
