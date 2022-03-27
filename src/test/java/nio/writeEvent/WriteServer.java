package nio.writeEvent;

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
                    SelectionKey selectionKey1 = channel.register(selector, SelectionKey.OP_READ);
                    // 向客户端发送大量数据
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 30000000; i++) {
                        sb.append("a");
                    }
                    ByteBuffer byteBuffer = Charset.defaultCharset().encode(sb.toString());
                    // 该方法不能保证一次把所有内容将数据写入客户端
                    // 该方法返回值，返回实际写入的字节数
                    int write = channel.write(byteBuffer);
                    System.out.println(write);
                    if (byteBuffer.hasRemaining()) {
                        selectionKey1.attach(byteBuffer);
                        // 把未写完的数据挂在selectionKey上
                        selectionKey1.interestOps(selectionKey1.interestOps() + SelectionKey.OP_WRITE);
                    }
                } else if (selectionKey.isWritable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    // 该方法不能保证一次把所有内容将数据写入客户端
                    // 该方法返回值，返回实际写入的字节数
                    int write = channel.write(byteBuffer);
                    System.out.println(write);
                    // 如果数据已经处理完毕
                    // 注销attach
                    // 清除SelectionKey.OP_WRITE事件，因为已经不需要通过该事件进行写入了
                    if (!byteBuffer.hasRemaining()) {
                        selectionKey.attach(null);
                        selectionKey.interestOps(selectionKey.interestOps() - SelectionKey.OP_WRITE);
                    }
                }

            }

        }
    }
}
