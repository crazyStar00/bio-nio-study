package nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 目标： NIO非阻塞通信下的入门案例
 *
 * @author star
 * @date 2022/3/26 4:03 PM
 */
public class NioServer {
    public static void main(String[] args) {
        try {


            // 1. 获取通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 2. 切换为非阻塞式模式
            serverSocketChannel.configureBlocking(false);
            // 3. 绑定连接的端口
            serverSocketChannel.bind(new InetSocketAddress(9999));
            // 4. 获取选择器Selector
            Selector selector = Selector.open();
            // 5. 将通道注册到选择器，并且开始指定监听事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 6. 使用选择器轮训已经就绪好的事件
            while (selector.select() > 0) {
                // 7. 获取选择器中所有已经就绪好的事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                // 8. 开始遍历这里准备好的事件
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    // 9. 判断事件类型
                    if (selectionKey.isAcceptable()) {
                        // 10. 直接获取当前接入的客户端通道
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        if(socketChannel==null){
                             continue;
                        }
                        // 11. 切换成非阻塞模式
                        socketChannel.configureBlocking(false);
                        // 12. 将本客户端通道注册到选择器
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        // 13. 获取当前选择器的就绪事件
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        // 14. 读取数据
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = channel.read(byteBuffer)) > 0) {
                            byteBuffer.flip();
                            System.out.println(new String(byteBuffer.array(), 0, len));
                            // 清除缓冲区的数据
                            byteBuffer.clear();
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
