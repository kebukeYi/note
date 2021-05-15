package com.java.note.netty.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author yhd
 * @createtime 2021/1/24 0:08
 */
@Slf4j
public class ReactorServer implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private DispatchHandler dispatchHandler;
    private static final String IP = "127.0.0.1";
    private static final Integer PORT = 8848;

    /**
     * 初始化，启动服务器
     */
    private ReactorServer(DispatchHandler dispatchHandler) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(IP, PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            this.dispatchHandler = dispatchHandler;
            log.info("服务器启动成功！");
        } catch (Exception e) {
            log.info("服务器启动错误：{}", e);
        }
    }

    public static ReactorServer start(DispatchHandler dispatchHandler) {
        return new ReactorServer(dispatchHandler);
    }

    @Override
    public void run() {
        dispatchHandler.dispatch(serverSocketChannel, selector);
    }
}

@Slf4j
class DispatchHandler {
    private ReadHandler readHandler;

    public void dispatch(ServerSocketChannel serverSocketChannel, Selector selector) {
        log.info("监听线程：{}", Thread.currentThread().getName());
        try {
            while (true) {
                if (selector.select() == 0) {
                    continue;
                }
                log.info("---------------");
                Set<SelectionKey> keys = selector.selectedKeys();
                keys.forEach(key -> {
                    try {
                        // 客户端请求连接事件
                        if (key.isAcceptable()) {
                            SocketChannel channel = serverSocketChannel.accept();
//                            ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
                            // 获得和客户端连接的通道
//                            SocketChannel channel = socketChannel.accept();
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                            log.info("上线");
                        }
                        if (key.isReadable()) {
                            readHandler = new ReadHandler();
                            readHandler.read(key, selector);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

@Slf4j
class ReadHandler {
    private ByteBuffer BUFFER;
    private WriteHandler writeHandler;

    public void read(SelectionKey key, Selector selector) {
        log.info("ReadHandler---read -- thread {}", Thread.currentThread().getName());
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            BUFFER = ByteBuffer.allocate(1024);
            int read = socketChannel.read(BUFFER);
            if (read > 0) {
                String message = new String(BUFFER.array());
                log.info("服务器接收到的信息是：{}", message);
                writeHandler = new WriteHandler();
                writeHandler.write(socketChannel, message, selector);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}

@Slf4j
class WriteHandler {
    public void write(SocketChannel channel, String message, Selector selector) {
        log.info("服务器转发消息中...");
        log.info("服务器转发数据给客户端线程: {}", Thread.currentThread().getName());

        //通过 key  取出对应的 SocketChannel
        selector.keys().stream().map(SelectionKey::channel).
                filter(targetChannel -> targetChannel instanceof SocketChannel && targetChannel != channel)
                .map(targetChannel -> (SocketChannel) targetChannel)
                .forEach(socketChannel -> {
                    try {
                        //将msg 存储到buffer 将buffer 的数据写入 通道
                        socketChannel.write(ByteBuffer.wrap(message.getBytes()));
                        log.info("消息发送成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}