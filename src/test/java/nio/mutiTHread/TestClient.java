package nio.mutiTHread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

/**
 * @author star
 * @date 2022/3/29 9:15 PM
 */
public class TestClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        socketChannel.configureBlocking(false);
        socketChannel.write(Charset.defaultCharset().encode("1234567890abcdef"));

        new CountDownLatch(1).await();
    }
}
