import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedFile;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.command.Command;
import ru.haazad.cloud.client.service.NetworkService;

import java.io.File;
import java.io.IOException;


public class NettyNetworkService implements NetworkService {
    private static final Logger logger = LogManager.getLogger(NettyNetworkService.class);

    private static NettyNetworkService network;
    private static SocketChannel channel;

    private NettyNetworkService() {
    }

    public static NettyNetworkService initializeNetwork() {
        network = new NettyNetworkService();
        initializeNetworkService();
        return network;
    }

    public static NettyNetworkService getNetwork() {
        return network;
    }

    private static void initializeNetworkService() {
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                            @Override
                            protected void initChannel(io.netty.channel.socket.SocketChannel socketChannel) {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
                                        new CommandHandler());
                            }
                        });
                ChannelFuture future = b.connect(ConfigProperty.getServerHost(), ConfigProperty.getServerPort()).sync();
                logger.info("Connecting to server " + ConfigProperty.getServerHost() + " on port " + ConfigProperty.getServerPort());
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                logger.throwing(Level.ERROR, e);
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void sendCommand(Command command) {
        logger.debug("Command is " + command.toString());
        channel.writeAndFlush(command);
    }

    @Override
    public void sendFile(String path) {
        try {
            ChannelFuture future = channel.writeAndFlush(new ChunkedFile(new File(path)));
            logger.debug(String.format("Start send file %s", path));
            future.addListener((ChannelFutureListener) listener -> {
                logger.debug("Transfer success");
                SwitchPipelineUtil.switchAfterTransferFile(channel.pipeline().firstContext());
            });
        } catch (IOException e) {
            logger.throwing(Level.ERROR, e);
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (isConnected()) channel.close().sync();
        } catch (InterruptedException e) {
            logger.throwing(Level.ERROR, e);
        }
    }

    @Override
    public boolean isConnected() {
        return channel != null && !channel.isShutdown();
    }
}
