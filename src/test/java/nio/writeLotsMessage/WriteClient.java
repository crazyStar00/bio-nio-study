package nio.writeLotsMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author star
 * @date 2022/3/27 7:17 PM
 */
public class WriteClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        socketChannel.configureBlocking(false);
        int count = 0;
        int sum = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
        while (true) {
            if ((count = socketChannel.read(byteBuffer)) > 0) {
                sum += count;
                System.out.println(sum);
                byteBuffer.clear();
            }

        }

    }
}
