package channel;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author star
 * @date 2022/3/26 2:07 PM
 */
public class ChannelTest {
    @Test
    public void write() throws IOException {
        // 1. 字节输出流通向目标文件
        FileOutputStream outputStream = new FileOutputStream("/tmp/aa.txt");
        // 2. 得到字节输出流对应的通道
        FileChannel fileChannel = outputStream.getChannel();
        // 3. 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello star".getBytes());
        // 4. 把缓冲区切换到写入模式
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileChannel.close();
    }

    @Test
    public void read() throws IOException {
        // 1。 定义文件字节输入流与源文件接通
        FileInputStream fileInputStream = new FileInputStream("/tmp/aa.txt");
        // 2。 获取字节输入流对应的通道
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);
        byteBuffer.flip();
        System.out.println(new String(byteBuffer.array(),0,byteBuffer.remaining()));
    }

    @Test
    public void copy() throws IOException {
        // 定义源文件
        File file = new File("/tmp/aa.txt");
        // 得到一个字节输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        // 得到一个字节输出流
        FileOutputStream fileOutputStream = new FileOutputStream(new File("/tmp/copy.txt"));
        // 得到文件的通道
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = fileOutputStream.getChannel();

        // 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            // 必须要情况缓冲区然后在写入数据到缓冲区
            byteBuffer.clear();
            // 开始读取数据
            int index = inChannel.read(byteBuffer);
            if (index == -1) {
                break;
            }
            // 已经读取了数据，把缓冲区的模式切换成可读模式
            byteBuffer.flip();
            // 把数据写出
            outChannel.write(byteBuffer);
        }
        inChannel.close();
        outChannel.close();
    }

    @Test
    public void test() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("/tmp/aa.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/1.txt");
    }
    @Test
    public void transfer() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/tmp/aa.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/1.txt");
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = fileOutputStream.getChannel();
        outChannel.transferFrom(inChannel, inChannel.position(), inChannel.size());
        inChannel.close();
        outChannel.close();
    }
}
