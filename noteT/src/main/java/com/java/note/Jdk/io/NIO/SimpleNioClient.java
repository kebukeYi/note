package com.java.note.Jdk.io.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 13:31
 * @description :
 * @question :
 * @usinglink :
 **/
public class SimpleNioClient {

    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void main(Strings[] args) throws IOException {
        oneNio();
    }

    public static void oneNio() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        if (!connect) {
            socketChannel.finishConnect();
        }
        executorService.scheduleWithFixedDelay(() -> {
            try {
                Strings message = socketChannel.getLocalAddress().toString() + " Hello World";
                // 使用ByteBuffer进行数据发送
                ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
                socketChannel.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }
}
 
