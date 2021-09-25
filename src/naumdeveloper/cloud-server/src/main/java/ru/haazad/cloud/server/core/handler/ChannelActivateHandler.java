import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChannelActivateHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(ChannelActivateHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        logger.info("Create new connection");
    }

}
