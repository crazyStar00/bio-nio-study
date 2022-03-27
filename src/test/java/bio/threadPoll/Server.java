package bio.threadPoll;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 目标： 开发实现伪异步架构
 *
 * @author star
 * @date 2022/3/25 8:37 AM
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 定义一一个ServerSocket对象进行服务端的端口注册
        ServerSocket serverSocket = new ServerSocket(5555);
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        // 监听客户端的Socket连接请求
        while (true) {
            Socket socket = serverSocket.accept();
            // 创建一个线程处理socket通信需求
            ServerThread serverThread = new ServerThread(socket);
            executorService.submit(serverThread);
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
