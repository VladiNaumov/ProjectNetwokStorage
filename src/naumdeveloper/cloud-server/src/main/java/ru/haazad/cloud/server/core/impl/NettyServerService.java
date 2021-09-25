import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.haazad.cloud.server.config.ConfigProperty;
import ru.haazad.cloud.server.core.DatabaseService;
import ru.haazad.cloud.server.core.ServerService;
import ru.haazad.cloud.server.core.handler.ChannelDisconnectHandler;
import ru.haazad.cloud.server.core.handler.CommandHandler;
import ru.haazad.cloud.server.core.handler.ChannelActivateHandler;
import ru.haazad.cloud.server.factory.Factory;

public class NettyServerService implements ServerService {
    private static final Logger logger = LogManager.getLogger(NettyServerService.class);
    private static DatabaseService databaseService;

    private static NettyServerService networkService;

    private NettyServerService(){}

    public static NettyServerService initializeServerService() {
        networkService = new NettyServerService();
        return networkService;
    }

    public static DatabaseService getDatabaseService() {return databaseService;}

    @Override
    public void startServer() {
        databaseService = Factory.initializeDbService();
        databaseService.connect();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new ChannelActivateHandler(),
                                    new CommandHandler(),
                                    new ChannelDisconnectHandler()
                                    );
                        }
                    });
            ChannelFuture future = b.bind(Integer.parseInt(ConfigProperty.getServerPort())).sync();
            logger.info("Server is running on port: " + ConfigProperty.getServerPort());
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.throwing(Level.ERROR, e);
        } finally {
            databaseService.disconnect();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
