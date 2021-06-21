package com.java.note.netty.chapter01.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: ServerSocketChannel
 * Description:
 * date: 2020/12/11 11:39
 *
 * @author 小刘讲师，微信：vv517956494
 * 本课程属于 小刘讲师 VIP 源码特训班课程
 * 严禁非法盗用（如有发现非法盗取行为，必将追究法律责任）
 * <p>
 * 如有同学发现非 小刘讲源码 官方号传播本视频资源，请联系我！
 * @since 1.0.0
 */
public class ServerSocketChannelDemo {
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(Strings[] args) throws IOException {

        // 实例化
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 监听 8007 端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8007));
        serverSocketChannel.configureBlocking(false);
        while (true) {
            final SocketChannel socketChannel = serverSocketChannel.accept();

            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        // 此处会阻塞线程..
                        while (true) {
                            socketChannel.read(byteBuffer);
                            byteBuffer.flip();
                            socketChannel.write(byteBuffer);
                            byteBuffer.clear();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (socketChannel.isOpen() || socketChannel.isConnected()) {
                            try {
                                socketChannel.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            });
        }
    }
}
