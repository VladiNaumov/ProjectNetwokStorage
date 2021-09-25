import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import ru.haazad.cloud.server.config.ConfigProperty;

import java.io.*;

@Log4j2
public class FileHandler extends ChannelInboundHandlerAdapter {
    private static String filename;
    private static String dstDirectory;
    private static long fileSize;

    public static void setFilename(String filename) {
        FileHandler.filename = filename;
    }

    public static void setDstDirectory(String dstDirectory) {
        FileHandler.dstDirectory = dstDirectory;
    }

    public static void setFileSize(long fileSize) {
        FileHandler.fileSize = fileSize;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        log.debug("Prepare upload file");
        String dst = ConfigProperty.getStorage() + "/" + dstDirectory + "/" + filename;
        File file = new File(dst);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            ByteBuf byteBuf = (ByteBuf) obj;
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(dst, true))){
                while (byteBuf.isReadable()) {
                    out.write(byteBuf.readByte());
                }
            } finally {
                byteBuf.release();
            }
        } catch (IOException e) {
            log.throwing(Level.ERROR, e);
        }
        if (file.length() == fileSize) {
            SwitchPipelineUtil.switchAfterUpload(ctx);
            log.debug("Upload is finished");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.throwing(cause);
    }
}
