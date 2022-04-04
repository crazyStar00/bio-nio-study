package aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

/**
 * @author star
 * @date 2022/4/4 2:46 PM
 */
public class AioFIleChannel {
    public static void main(String[] args) throws InterruptedException {
        try  {
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("/tmp/aa.txt"), StandardOpenOption.READ);
            ByteBuffer buffer = ByteBuffer.allocate(16);
            System.out.println("read begin...");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println("read competed ..." + result);
                    attachment.flip();
                    System.out.println(new String(buffer.array(), 0, buffer.remaining()));
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println("failed...");
                    exc.printStackTrace();
                    attachment.flip();
                    System.out.println(new String(buffer.array(), 0, buffer.remaining()));
                }
            });
            System.out.println("read end ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.sleep(1111);
        new CountDownLatch(1).await();
    }
}
