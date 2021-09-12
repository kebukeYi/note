package com.mmy.nio.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 18:08
 * @description:
 * @question:
 * @link:
 **/
public class ServerSocketChannelDemo {

    public static final String GREETING = "Hello java nio.\r\n";

    public static void main(String[] argv) throws Exception {
        int port = 8888; // default
        if (argv.length > 0) {
            port = Integer.parseInt(argv[0]);
        }
        //跟  ByteBuffer.allocate(100); 有什么区别吗?
        ByteBuffer buffer = ByteBuffer.wrap(GREETING.getBytes());
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //由于 ServerSocketChannel 没有 bind()方法，因此有必要取出对等的 socket 并使用
        //它来绑定到一个端口以开始监听连接
        ssc.socket().bind(new InetSocketAddress(port));
        //非阻塞
        ssc.configureBlocking(false);
        // ssc.configureBlocking(true);
        while (true) {
            System.out.println("Waiting for connections");
            SocketChannel sc = ssc.accept();
            if (sc == null) {
                System.out.println("null");
                Thread.sleep(2000);
            } else {
                System.out.println("Incoming connection from: " + sc.socket().getRemoteSocketAddress());
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }
}
 
