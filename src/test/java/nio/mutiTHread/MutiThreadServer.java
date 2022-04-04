package nio.mutiTHread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author star
 * @date 2022/3/27 7:09 PM
 */
public class MutiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boos");
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress("127.0.0.1", 9999));

        Selector boss = Selector.open();
        serverChannel.register(boss, SelectionKey.OP_ACCEPT);
        // 创建固定数量的worker并初始化
        Worker[] workers = new Worker[2];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);

        }

        AtomicInteger index = new AtomicInteger(0);
        while (true) {
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("connected ...." + socketChannel.getRemoteAddress());
                    // 唤醒selector，避免selector.select()时阻塞
                    int workIndex = index.getAndIncrement();
                    workers[workIndex % workers.length].register();
                    workers[workIndex % workers.length].selector.wakeup();
                }
            }
        }
    }

    static class Worker implements Runnable {
        private Thread thread;
        private Selector selector;
        private String name;
        private volatile boolean start = false;

        public Worker(String name) {
            this.name = name;
        }

        // 初始化线程和selector
        public void register() throws IOException {
            if (start) {
                return;
            }
            thread = new Thread(this, name);
            thread.start();
            selector = Selector.open();
            start = true;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // 通过传入阻塞的超市时间，避免无限等待，使不能注册新的SocketChannel
                    if (selector.select(100) > 0) {
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = iterator.next();
                            iterator.remove();
                            if (selectionKey.isWritable()) {

                            } else if (selectionKey.isReadable()) {
                                ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                                SocketChannel channel = (SocketChannel) selectionKey.channel();
                                System.out.println("read data ");
                                channel.read(byteBuffer);
                                byteBuffer.flip();
                                System.out.println("data:" + new String(byteBuffer.array(), 0, byteBuffer.remaining()));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
