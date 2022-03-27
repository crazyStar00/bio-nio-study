package nio_chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 目标：服务端群聊系统实现。
 *
 * @author star
 * @date 2022/3/26 8:24 PM
 */
public class ChatServer {
    // 1. 定义成员属性：选择器、服务端通道、端口
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private static final int PORT = 9999;

    // 2. 定义初始化代码逻辑
    public ChatServer() throws IOException {
        try {
            // 接受选择器
            selector = Selector.open();
            // 获取通道
            serverChannel = ServerSocketChannel.open();
            // 绑定客户端的端口
            serverChannel.bind(new InetSocketAddress("127.0.0.1", PORT));
            // 设置非阻塞模式
            serverChannel.configureBlocking(false);
            // 把通道注册到选择器上，并且指定接受连接事件
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 开始监听
     */
    private void listen() {
        try {
            while (selector.select() > 0) {
                // 获取选择器中所有注册通道到就绪事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                // 开始遍历事件
                while (iterator.hasNext()) {
                    // 提取事件
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        // 获取当前客户端通道
                        SocketChannel socketChannel = serverChannel.accept();
                        // 注册成非阻塞模式
                        socketChannel.configureBlocking(false);
                        // 注册给选择器，监听读取数据到事件
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        // 处理客户端的消息，实现转发逻辑
                        readClientData(selectionKey);
                    } else if (selectionKey.isConnectable()) {

                    } else if (selectionKey.isWritable()) {

                    } else {

                    }
                    // 处理完毕，移除当前事件
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受当前客户端通道的信息，转发给其他全部客户端通道
     *
     * @param selectionKey
     */
    private void readClientData(SelectionKey selectionKey) {
        SocketChannel channel = null;
        try {
            // 得到当前客户端通道
            channel = (SocketChannel) selectionKey.channel();
            // 创建缓冲区对象，开始接受客户端的数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = channel.read(byteBuffer);
            if (read > 0) {
                byteBuffer.flip();
                String message = new String(byteBuffer.array(), 0, byteBuffer.remaining());
                System.out.println("接受到了客户端的消息：" + message);
                // 把消息推送给全部的客户端接受
                sendMsgToAllClient(message, channel);
            }

        } catch (Exception e) {
            // 当前客户端离线
            // 取消注册
            selectionKey.cancel();
            if (channel != null) {
                try {
                    // 关闭通道
                    channel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }

    }

    /**
     * 把当前客户端的消息推送给当前全部在线注册的channel
     *
     * @param message
     * @param channel
     */
    private void sendMsgToAllClient(String message, SocketChannel channel) throws IOException {
//        System.out.println("服务端开始转发这个消息：当前处理的线程" + Thread.currentThread().getName());
        for (SelectionKey selectionKey : selector.keys()) {
            // 排除服务端通道
            if (selectionKey.channel() instanceof SocketChannel && selectionKey.channel() != serverChannel) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                // 不要把数据发给了自己
                if (socketChannel == channel) {
                    continue;
                }
                ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
                socketChannel.write(byteBuffer);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.listen();

    }


}
