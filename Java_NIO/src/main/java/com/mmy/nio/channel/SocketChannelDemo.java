package com.mmy.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 18:17
 * @description:
 * @question:
 * @link:
 **/
public class SocketChannelDemo {

    public static void main(String[] args) throws IOException {
        //创建SocketChannel
//        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8888));

        //设置阻塞和非阻塞
        socketChannel.configureBlocking(false);

        //SocketChannel 还支持多路复用
        socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, Boolean.TRUE);
        socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, Boolean.TRUE);
        socketChannel.getOption(StandardSocketOptions.SO_KEEPALIVE);
        socketChannel.getOption(StandardSocketOptions.SO_RCVBUF);


        //读操作
        ByteBuffer byteBuffer = ByteBuffer.allocate(222);
        final int read = socketChannel.read(byteBuffer);
        System.out.println(read);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.out.println((char) byteBuffer.get());
        }
        socketChannel.close();
        System.out.println("read over");
    }


}
 
