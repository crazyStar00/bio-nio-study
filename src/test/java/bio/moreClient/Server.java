package bio.moreClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 目标： 实现服务端可以同时接受多个客户端的socket的通信需求
 * 思路： 服务端每接受到一个客户端socket请求后，交给一个独立的线程处理客户端数据交互需求
 *
 * @author star
 * @date 2022/3/25 8:37 AM
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 定义一一个ServerSocket对象进行服务端的端口注册
        ServerSocket serverSocket = new ServerSocket(5555);
        // 监听客户端的Socket连接请求
        while (true) {
            Socket socket = serverSocket.accept();
            // 创建一个线程处理socket通信需求
            ServerThread serverThread = new ServerThread(socket);
            serverThread.start();
        }
    }

    static class ServerThread extends Thread {
        Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 从socket管道中得到一个字节输入流对象
                InputStream inputStream = socket.getInputStream();
                // 把字节输入流包装成一个缓冲字节输入流
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                // 把字节流包装成字符输入流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("服务端接收到：" + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
