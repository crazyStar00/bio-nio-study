package bio.portForward;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 目标： BIOmoist下的端口转发思想-服务端实现
 * 服务端实现的需求：
 * 1. 注册端口
 * 2. 接受客户端的连接，交给一个独立的线程处理
 * 3. 把当前连接的客户端socket存入到一个所谓的在线socket集合中保存
 * 4. 接受客户端的消息，然后推入给当前所有在线的socket接受不了
 *
 * @author star
 * @date 2022/3/25 8:37 AM
 */
public class Server {
    public static List<Socket> socketList = new ArrayList<>();

    public static void main(String[] args) {
        // 定义一一个ServerSocket对象进行服务端的端口注册
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7777);
            // 监听客户端的Socket连接请求
            while (true) {
                Socket socket = serverSocket.accept();
                socketList.add(socket);
                // 创建一个线程处理socket通信需求
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                // 从socket中获取当金钱客户端的输入流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = bufferedReader.readLine()) != null) {
                    // 服务端接受到客户端的消息后，将消息推送到当前所有的在线socket
                    sendMessageToAllClient(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                socketList.remove(socket);
            }
        }
    }

    public static void sendMessageToAllClient(String message) throws IOException {
        for (Socket socket : socketList) {
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println(message);
            printStream.flush();

        }
    }
}
