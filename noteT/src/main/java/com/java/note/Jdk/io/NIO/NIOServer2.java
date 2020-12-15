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
 * @Description :
 * @Version :  0.0.1
 */
public class NIOServer2 {


    public static void main(String[] args) throws IOException {
        Selector serverSelector = Selector.open();
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            try {
                SocketChannel socketChannel = SocketChannel.open();
                socketChannel.socket().bind(new InetSocketAddress(8000));
                socketChannel.configureBlocking(false);
                socketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
                while (true) {
                    if (serverSelector.select(1) > 0) {
                        Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                        if (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            if (key.isAcceptable()) {
                                try {
                                    SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                    clientChannel.configureBlocking(false);
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
                    if (clientSelector.select(1) > 0) {
                        Set<SelectionKey> selectionKeys = clientSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                        if (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            if (key.isReadable()) {
                                try {
                                    SocketChannel clientChannel = (SocketChannel) key.channel();
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    clientChannel.read(byteBuffer);
                                    byteBuffer.flip();
                                    System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer).toString());
                                    String text = "服务器：我收到了";
                                    ByteBuffer outBuffer = ByteBuffer.wrap(text.getBytes());
                                    clientChannel.write(outBuffer);// 将消息回送给客户端
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
