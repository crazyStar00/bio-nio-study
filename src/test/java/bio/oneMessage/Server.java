package bio.oneMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 目标： 客户端发送消息，服务端接受消息
 *
 * @author star
 * @date 2022/3/25 8:37 AM
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 定义一一个ServerSocket对象进行服务端的端口注册
        ServerSocket serverSocket = new ServerSocket(5555);
        // 监听客户端的Socket连接请求
        Socket socket = serverSocket.accept();
        // 从socket管道中得到一个字节输入流对象
        InputStream inputStream = socket.getInputStream();
        // 把字节输入流包装成一个缓冲字节输入流
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        // 把字节流包装成字符输入流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
        String line;
        if ((line = bufferedReader.readLine()) != null) {
            System.out.println("服务端接收到：" + line);
        }

    }
}
