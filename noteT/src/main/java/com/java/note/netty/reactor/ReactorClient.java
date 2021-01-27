package com.java.note.netty.reactor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yhd
 * @createtime 2021/1/24 0:08
 */
@Slf4j
public class ReactorClient {

    private static final String IP = "127.0.0.1";
    private static final Integer PORT = 8848;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;
    public static ThreadPoolExecutor POOL = new ThreadPoolExecutor(5, 6, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
    private ClientReadHandler clientReadHandler;
    private ClientWriteHandler clientWriteHandler;

    //构造器, 完成初始化工作
    private ReactorClient(ClientWriteHandler clientWriteHandler, ClientReadHandler clientReadHandler) {
        try {
            selector = Selector.open();
            //连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(IP, PORT));
            //设置非阻塞
            socketChannel.configureBlocking(false);
            //将channel 注册到selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            //得到username
            username = socketChannel.getLocalAddress().toString().substring(1);
            log.info(username + " is ok...");
            this.clientWriteHandler = clientWriteHandler;
            this.clientReadHandler = clientReadHandler;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ReactorClient start(ClientWriteHandler clientWriteHandler, ClientReadHandler clientReadHandler) {
        return new ReactorClient(clientWriteHandler, clientReadHandler);
    }

    public void read() {
        POOL.submit(() -> clientReadHandler.read(selector));
    }

    public void write(String message) {
        POOL.submit(() -> clientWriteHandler.write(username, socketChannel, message));
    }
}

@Slf4j
@Data
class ClientWriteHandler {
    public void write(String username, SocketChannel socketChannel, String message) {
        message = username + " 说：" + message;
        try {
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

@Slf4j
class ClientReadHandler {
    public void read(Selector selector) {
        try {
            while (true) {
                if (selector.select(1000) == 0) {
                    continue;
                }
                //有可以用的通道
                Set<SelectionKey> keys = selector.selectedKeys();
                keys.forEach(key -> {
                    if (key.isReadable()) {
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        //得到一个Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取
                        try {
                            sc.read(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //把读到的缓冲区的数据转成字符串
                        String msg = new String(buffer.array());
                        log.info(msg.trim());
                    }
                    keys.remove(key);
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}