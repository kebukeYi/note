package com.java.note.Jdk.io.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * NIO客户端
 * *2020年4月18日
 *
 * @author
 */
public class NIOClient {

    //通道管理器
    private Selector selector;

    /**
     * 获得一个Socket通道，并对该通道做一些工作
     *
     * @throws IOException
     */
    public void initClient(String ip, int port) throws IOException {
        //获得一个Socket通道
        SocketChannel channel = SocketChannel.open();
        //设置为非阻塞
        channel.configureBlocking(false);
        //获得一个通道管理器
        this.selector = selector.open();
        //客户端连接服务器，其方法执行并没有实现连接，需要listen()方法中调用Channel.finshConection();才算完成连接
        channel.connect(new InetSocketAddress(ip, port));
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。
        channel.register(selector, SelectionKey.OP_CONNECT);
//        channel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的对象,如果有，则进行处理
     *
     * @throws IOException
     */
    public void connect() throws IOException {
        //轮询访问Selector
        while (true) {
            //选择一组可以进行I/O的操作的事件，放在selector中，客户端的该方法不会阻塞
            System.out.println("selector.select() : " + selector.select());
            //获得selector中选中的项的迭代器
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                //删除已经选择key,防止重复
                ite.remove();
                //连接事件的发生
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    //如果正在连接，则完成连接
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    //设置成非阻塞
                    channel.configureBlocking(false);
                    //给服务端发送信息；
                    System.out.println("客户端已连接成功！");
                    channel.write(ByteBuffer.wrap(new String("客户端已连接成功！").getBytes("UTF-8")));
                    //和服务器端连接成功后，为了可以接受到服务端的信息，需要给通道设置读的权限
                    channel.register(this.selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    read(key);
                }
            }
        }
    }


    /**
     * 处理读取服务端发来的信息 的事件
     * 和服务端的read方法一样
     *
     * @throws IOException
     */
    public void read(SelectionKey key) throws IOException {
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        // 创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(512);
        channel.read(buffer);
        byte[] data = buffer.array();
        String msg = new String(data).trim();
        System.out.println("客户端收到信息：" + msg);
        System.out.println("请输入信息：");
        Scanner scanne = new Scanner(System.in);
        String text = scanne.nextLine();
        ByteBuffer outBuffer = ByteBuffer.wrap(text.getBytes("utf-8"));
        channel.write(outBuffer);// 将消息回送给客户端
    }

    /**
     * 启动测试
     *
     * @throws IOException
     */

    public static void main(String args[]) throws IOException {
        NIOClient client = new NIOClient();
        client.initClient("127.0.0.1", 8000);
        client.connect();
    }
}