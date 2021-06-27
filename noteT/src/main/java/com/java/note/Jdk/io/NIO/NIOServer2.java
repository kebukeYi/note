package com.java.note.Jdk.io.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author : fang.com
 * @CreatTime : 2020-12-15 14:28
 * @Description : 闪电侠  Nio 多线程模型
 * @Version :  0.0.1
 */
public class NIOServer2 {

    public static void main(String[] args) throws IOException {
        //处理连接 的 selector
        Selector serverSelector = Selector.open();
        //处理 read 的selector
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            try {
                //服务端启动
                ServerSocketChannel socketChannel = ServerSocketChannel.open();
                socketChannel.socket().bind(new InetSocketAddress(8080));
                socketChannel.configureBlocking(false);
                //连接注册
                socketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
                while (true) {
                    // 监测是否有新的连接，这里的1指的是阻塞的时间为 1ms
                    if (serverSelector.select(20) > 0) {
                        System.out.println("存在  serverSelector select 事件");
                        Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                        if (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            if (key.isAcceptable()) {
                                try {
                                    System.out.println("存在连接事件");
                                    // (1) 每来一个新连接，不需要创建一个线程，而是直接 注册到 clientSelector
                                    //完成了 TCP 三次握手 建立了物理链路
                                    SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                    clientChannel.configureBlocking(false);
                                    //
                                    clientChannel.register(clientSelector, SelectionKey.OP_READ);
                                } catch (Exception e) {
                                } finally {
                                    keyIterator.remove();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    // 监测是否有新的连接，这里的1指的是阻塞的时间为 1ms
                    if (clientSelector.select(20) > 0) {
                        System.out.println("存在  clientSelector select 事件");
                        Set<SelectionKey> selectionKeys = clientSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                        if (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            if (key.isReadable()) {
                                try {
                                    System.out.println("存在 read 事件");
                                    SocketChannel clientChannel = (SocketChannel) key.channel();
                                    // (3) 面向 Buffer
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    clientChannel.read(byteBuffer);
                                    byteBuffer.flip();
                                    System.out.println(Charset.defaultCharset().decode(byteBuffer));
                                    String text = "服务器：我收到了";
                                    ByteBuffer outBuffer = ByteBuffer.wrap(text.getBytes());
                                    // 将消息回送给客户端
                                    clientChannel.write(outBuffer);
                                } catch (Exception e) {
                                } finally {
                                    keyIterator.remove();
                                    key.interestOps(SelectionKey.OP_READ);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }).start();
    }
}
