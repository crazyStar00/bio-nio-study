package nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author star
 * @date 2022/3/27 8:59 AM
 */
public class ChatClient {
    SocketChannel socketChannel;
    Selector selector;

    public ChatClient() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
            socketChannel.configureBlocking(false);
            // 创建选择器
            selector = Selector.open();
            // 监听读事件
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("客户端准备完毕：" + Thread.currentThread().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessage() throws IOException {
        if (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    channel.read(byteBuffer);
                    System.out.println("接收到消息：" + new String(byteBuffer.array(), 0, byteBuffer.remaining()));
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        // 定义一个线程专门负责监听服务端发送过来读读消息事件
        new Thread(() -> {
            try {
                while (true) {
                    chatClient.readMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            chatClient.sendMessage(line);
        }
    }

    private void sendMessage(String line) throws IOException {
        socketChannel.write(ByteBuffer.wrap(("crazy说:" + line).getBytes()));

    }
}
