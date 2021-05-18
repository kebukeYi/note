package com.java.note.Jdk.io.NIO;

import lombok.val;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 13:20
 * @description : 单线程模型
 * @question :
 * @usinglink :
 **/
public class SimpleNioServer {


    public static void main(String[] args) throws IOException {
        oneNio();
    }

    public static void oneNio() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8080), 10);
        serverSocketChannel.configureBlocking(false);
        val selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            //单线程模型 只有一个线程 = 一边处理连接事件+ 一边处理业务流程
            selector.select(200);
            val iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                val selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("有新客户端来连接");
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    channel.configureBlocking(false);
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                    channel.read(byteBuffer);
                    //
                    byteBuffer.flip();
                    System.out.println(Charset.defaultCharset().decode(byteBuffer));
                    channel.register(selector, SelectionKey.OP_READ);
                }
                iterator.remove();
            }
        }
    }
}
 
