package bio.fileTransfer;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 目标：可以实现接受客户端任意类型文件，并保存到服务端的磁盘
 *
 * @author star
 * @date 2022/3/25 10:40 AM
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            Socket socket = serverSocket.accept();
            new ServerThread(socket).start();
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
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                String fileName = dataInputStream.readUTF();
                System.out.println("fileName:" + fileName);
                int fileSize = dataInputStream.readInt();
                System.out.println("fileSize:" + fileSize);
                System.out.println();
                byte[] bytes = new byte[fileSize];
                dataInputStream.read(bytes);
                FileUtils.writeByteArrayToFile(new File("/tmp/server/"+fileName),bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
