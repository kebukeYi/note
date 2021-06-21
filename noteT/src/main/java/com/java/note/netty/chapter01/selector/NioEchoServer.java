package com.java.note.netty.chapter01.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * ClassName: NioEchoServer
 * Description:
 * date: 2020/12/12 11:16
 *
 * @author 小刘讲师，微信：vv517956494
 * 本课程属于 小刘讲师 VIP 源码特训班课程
 * 严禁非法盗用（如有发现非法盗取行为，必将追究法律责任）
 * <p>
 * 如有同学发现非 小刘讲源码 官方号传播本视频资源，请联系我！
 * @since 1.0.0
 */
public class NioEchoServer {

    public static void main(Strings[] args) throws IOException {
        // 1. 首先获取一个selector多路复用器
        Selector selector = Selector.open();

        // 2. 创建ServerSocketChannel 并且 监听8866端口..
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8866));
        // 设置当前
        serverSocketChannel.configureBlocking(false);

        // 3. 将serverSocketChannel注册到selector，并且设置感兴趣的事件为：accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 使用阻塞 select 方法，这个方法只有在 selector 上注册的 channel，有就绪的才会返回。
            selector.select();
            // 获取就绪列表
            Set<SelectionKey> keys = selector.selectedKeys();
            // 获取迭代器
            Iterator<SelectionKey> it = keys.iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();

                //条件成立：当前就绪key，代表有ACCEPT事件就绪，有客户端想要连接到当前 server
                if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    // 当前ServerSocketChannel已经处于ACCEPT就绪状态，所以当前accept方法会返回SocketChannel实例。
                    SocketChannel socketChannel = serverChannel.accept();
                    // 将客户端socket设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 客户端通道注册到当前selector
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    it.remove();
                } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    try {
                        // 客户端socket读缓冲区内已经有数据 等待 读取了，所以 read 方法 直接就会拿到数据。将数据写入到 byteBuffer内。
                        while (true) {
                            byteBuffer.clear();
                            //byteBuffer 写模式
                            int reads = socketChannel.read(byteBuffer);
                            if (reads == -1) break;
                            //byteBuffer 读模式
                            byteBuffer.flip();
                            //byteBuffer 读出来写到socketChannel中去
                            socketChannel.write(byteBuffer);
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                        // 对端关闭后，服务端一定要关闭，否则 selector 内会一直检查这个socketChannel。
                        socketChannel.close();
                    } finally {
                        it.remove();
                    }
                }
            }
        }
    }
}
