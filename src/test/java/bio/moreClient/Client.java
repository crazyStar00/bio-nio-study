package bio.moreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author star
 * @date 2022/3/25 8:37 AM
 */
public class Client {
    public static void main(String[] args) throws IOException {
        // 创建一个Socket对象请求服务端
        Socket socket = new Socket("127.0.0.1", 5555);
        // 从socket中获取字节输入输出流
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        // 把字节输出流包装成一个打印流
        PrintWriter printWriter = new PrintWriter(outputStream);
        Scanner scanner = new Scanner(System.in);
        String line;
        while (true) {
            if ((line = scanner.nextLine()) != null) {
                printWriter.println(line);
                printWriter.flush();
            }
        }
    }
}
