package nio.writeLotsMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author star
 * @date 2022/3/27 7:09 PM
 */
public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9999));
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    SocketChannel channel = serverSocketChannel.accept();
                    channel.configureBlocking(false);

                    // 向客户端发送大量数据
                    // 这里仍然是阻塞的写入，性能较差
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 30000000; i++) {
                        sb.append("a");
                    }
                    ByteBuffer byteBuffer = Charset.defaultCharset().encode(sb.toString());
                    while (byteBuffer.hasRemaining()) {
                        // 该方法不能保证一次把所有内容将数据写入客户端
                        // 该方法返回值，返回实际写入的字节数
                        int write = channel.write(byteBuffer);
                        System.out.println(write);
                    }
                    byteBuffer.clear();
                }

            }

        }
    }
}
