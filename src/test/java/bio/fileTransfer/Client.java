package bio.fileTransfer;

import org.apache.commons.io.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 目标：实现客户端上传任意类型的文件数据给服务端保存起来
 *
 * @author star
 * @date 2022/3/25 10:41 AM
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 6666);
        // 把字节输入流包装成数据输出流
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        // 把文件数据发送到服务端进行接受
        String path = "/tmp/aa.txt";
        prepareFile(path);
        File file = new File(path);
        // 先发送上传文件的名称发送给服务端
        dataOutputStream.writeUTF(file.getName());
        // 发送文件大小给服务端
        dataOutputStream.writeInt((int) file.length());
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];

        fileInputStream.read(bytes);
        dataOutputStream.write(bytes);
    }
    public static void prepareFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            FileUtils.writeByteArrayToFile(file,"hwllo fileTransfer".getBytes());
        }
    }
}
