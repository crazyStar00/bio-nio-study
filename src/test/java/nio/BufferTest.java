package nio;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author star
 * @date 2022/3/25 5:17 PM
 */
public class BufferTest {
    @Test
    public void test01(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("--------------");

        // put 往缓冲区添加数据
        byteBuffer.put("star".getBytes(StandardCharsets.UTF_8));
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("--------------");

        // Buffer flip()将缓冲区的界限值设置为当前位置，并将当前位置位置为0，可读模式
        byteBuffer.flip();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("--------------");

        // get 数据的读取
        System.out.println(byteBuffer.get());
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
    }

    @Test
    public void test02(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("--------------");

        // put 往缓冲区添加数据
        byteBuffer.put("star".getBytes(StandardCharsets.UTF_8));
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("--------------");

        byteBuffer.clear();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println(byteBuffer.get());
        System.out.println("--------------");

        // 定义一个新的缓冲区
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(10);
        byteBuffer1.put("star".getBytes());

        byteBuffer1.flip();
        byte[] dst = new byte[2];
        byteBuffer1.get(dst);
        System.out.println(new String(dst));

    }
    @Test
    public void test03(){
        // 1. 创建直接内存的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        System.out.println(byteBuffer.isDirect());
    }
}
