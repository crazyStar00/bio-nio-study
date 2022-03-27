package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * 目标： 客户端案例实力实现-基于NIO非阻塞通信
 *
 * @author star
 * @date 2022/3/26 4:08 PM
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        // 1. 获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        // 2. 切换为非阻塞模式
        socketChannel.configureBlocking(false);
        // 3. 指定缓冲区大小
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 4. 发送数据到服务端
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请说：");
            String line = scanner.nextLine();
            byteBuffer.put(("star:"+line).getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

    }
}
