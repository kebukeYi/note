package com.mmy.nio.chat.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 19:18
 * @description:
 * @question:
 * @link:
 **/
public class ClientThread implements Runnable {

    private Selector selector;

    public ClientThread(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // System.out.println("等待连接中.....");
                int select = selector.select();
                if (select == 0) continue;
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        System.out.println("\n有新的数据来了.....");
                        readOperator(selector, key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //处理可读状态操作
    public void readOperator(Selector selector, SelectionKey selectionKey) {
        //1 从SelectionKey获取到已经就绪的通道
        final SocketChannel channel = (SocketChannel) selectionKey.channel();
        //2 创建buffer
        final ByteBuffer allocate = ByteBuffer.allocate(1024);
        try {
            //3 循环读取客户端消息
            int read = channel.read(allocate);
            String message = "";
            if (read > 0) {
                //切换读模式
                allocate.flip();
                //读取内容
                message = message + Charset.forName("UTF-8").decode(allocate);
            }
            //4 将channel再次注册到选择器上，监听可读状态
            channel.register(selector, SelectionKey.OP_READ);
            //5 把客户端发送消息，广播到其他客户端
            if (message.length() > 0) {
                System.out.println("\n收到 : " + message);
                castOtherClient(message, selector, channel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void castOtherClient(String message, Selector selector, SocketChannel socketChannel) throws IOException {
        //  final Set<SelectionKey> selectionKeys = selector.selectedKeys();
        //获取所有已经接入channel
        final Set<SelectionKey> selectionKeys = selector.keys();
        //循环想所有channel广播消息
        for (SelectionKey selectionKey : selectionKeys) {
            //获取每个channel
            SelectableChannel channel = selectionKey.channel();
            if (channel instanceof SocketChannel && socketChannel != channel) {
                ((SocketChannel) channel).write(Charset.forName("UTF-8").encode(message));
            }
        }
    }
}
 
