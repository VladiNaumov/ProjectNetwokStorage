import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.log4j.Log4j2;
import ru.haazad.cloud.client.service.impl.handler.CommandHandler;
import ru.haazad.cloud.client.service.impl.handler.FileHandler;

@Log4j2
public class SwitchPipelineUtil {

    public static void switchToTransferFile(ChannelHandlerContext context) {
        context.pipeline().addLast(new ChunkedWriteHandler(), new FileHandler());
        context.pipeline().remove(ObjectEncoder.class);
        context.pipeline().remove(ObjectDecoder.class);
        context.pipeline().remove(CommandHandler.class);
        log.debug("Switch pipeline to upload");
    }

    public static void switchAfterTransferFile(ChannelHandlerContext context) {
        context.pipeline().remove(ChunkedWriteHandler.class);
        context.pipeline().remove(FileHandler.class);
        context.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                new ObjectEncoder(),
                new CommandHandler());
        log.debug("Switch pipeline to command");
    }
}
