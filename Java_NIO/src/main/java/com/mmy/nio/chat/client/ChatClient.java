package com.mmy.nio.chat.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 19:18
 * @description:
 * @question:
 * @link:
 **/
public class ChatClient {

    public void startClient(String Ip, int port, String name) throws IOException, InterruptedException {
//        final SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(Ip, port));
        //下行代码有连接错误
        final SocketChannel socketChannel = SocketChannel.open();
        final boolean connect = socketChannel.connect(new InetSocketAddress(Ip, port));
        System.out.println(connect);
        final boolean finishConnect = socketChannel.finishConnect();
        System.out.println(finishConnect);

        socketChannel.configureBlocking(false);
        final Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);

        new Thread(new ClientThread(selector)).start();

        // 客户端 正常关闭  -> 服务器通过读事件 读的数据长度为-1 主动关闭channel 通道 没有异常
        //socketChannel.close();
        //客户端 非正常关闭-> 服务器通过读事件 存在异常

        System.out.print("\n请输入 : ");
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (line.length() > 0) {
                socketChannel.write(Charset.forName("UTf-8").encode(name + ":" + line));
            }
        }
    }
}
 
