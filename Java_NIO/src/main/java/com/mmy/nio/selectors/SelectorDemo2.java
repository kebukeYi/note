package com.mmy.nio.selectors;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 00:22
 * @description:
 * @question:
 * @link:
 **/
public class SelectorDemo2 {

    //服务端代码
    @Test
    public void serverDemo() throws Exception {
        //1 获取服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //2 切换非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //3 创建buffer
        ByteBuffer serverByteBuffer = ByteBuffer.allocate(1024);

        //4 绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(8080));

        //5 获取selector选择器
        Selector selector = Selector.open();

        //6 通道注册到选择器，进行监听
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //7 选择器进行轮询，进行后续操作
        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                //获取就绪操作
                SelectionKey next = selectionKeyIterator.next();
                //判断什么操作
                if (next.isAcceptable()) {
                    //获取连接
                    SocketChannel accept = serverSocketChannel.accept();

                    //切换非阻塞模式
                    accept.configureBlocking(false);

                    //注册
                    accept.register(selector, SelectionKey.OP_READ);

                } else if (next.isReadable()) {
                    SocketChannel channel = (SocketChannel) next.channel();

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    //读取数据
                    int length = 0;
                    while ((length = channel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(), 0, length));
                        byteBuffer.clear();
                    }
                }
                selectionKeyIterator.remove();
            }
        }
    }

    //客户端代码
    @Test
    public void clientDemo() throws Exception {
        //1 获取通道，绑定主机和端口号
        SocketChannel socketChannel =
                SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));

        //2 切换到非阻塞模式
        socketChannel.configureBlocking(false);

        //3 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //4 写入buffer数据
        byteBuffer.put(new Date().toString().getBytes());

        //5 模式切换
        byteBuffer.flip();

        //6 写入通道
        socketChannel.write(byteBuffer);

        //7 关闭
        byteBuffer.clear();
    }

    public static void main(String[] args) throws IOException {
        //1 获取通道，绑定主机和端口号
        SocketChannel socketChannel =
                SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));

        //2 切换到非阻塞模式
        socketChannel.configureBlocking(false);

        //3 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.next();

            //4 写入buffer数据
            byteBuffer.put((new Date().toString() + "--->" + str).getBytes());

            //5 模式切换
            byteBuffer.flip();

            //6 写入通道
            socketChannel.write(byteBuffer);

            //7 关闭
            byteBuffer.clear();
        }

    }
}
 
