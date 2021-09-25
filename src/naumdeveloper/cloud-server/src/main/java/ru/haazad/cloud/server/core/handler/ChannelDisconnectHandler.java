import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChannelDisconnectHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(ChannelDisconnectHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.trace("Disconnect connection");
    }
}
