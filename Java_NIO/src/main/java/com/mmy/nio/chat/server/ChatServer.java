package com.mmy.nio.chat.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 19:17
 * @description:
 * @question: https://www.codercto.com/a/64945.html
 * @link:
 **/
public class ChatServer {

    public void startServer() throws IOException {
        int count = 0;
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8888), 1024);
        //优化 这里只有一个 selector 边处理连接事件 还有 读事件
        //是不是可以使用两个 selector 一个处理连接 一个处理 读写事件
        final Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            try {
                System.out.println("等待连接中.....");
                int select = 0;
                try {
                    select = selector.select();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (select == 0) continue;
                count += 1;
                if (count > 200) break;
                //获取可用的channel
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                //遍历集合
                final Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //移除set集合当前selectionKey
                    iterator.remove();
                    //6 根据就绪状态，调用对应方法实现具体业务操作
                    //6.1 如果accept状态
                    if (key.isAcceptable()) {
                        System.out.println("有了新链接了.....");
                        acceptOperator(serverSocketChannel, selector);
                    }
                    //6.2 如果可读状态
                    if (key.isReadable()) {
                        System.out.println("有新的数据来了.....");
                        try {
                            readOperator(selector, key);
                        } catch (Exception e) {
                            System.out.println("0关闭通道....");
                            key.cancel();
                            key.channel().close();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void acceptOperator(ServerSocketChannel serverSocketChannel, Selector selector) {
        try {
            final SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            socketChannel.write(Charset.forName("UTF-8").encode("欢迎进入聊天室，请注意隐私安全"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readOperator(Selector selector, SelectionKey selectionKey) throws IOException {
        final SocketChannel channel = (SocketChannel) selectionKey.channel();
        final ByteBuffer allocate = ByteBuffer.allocate(1024);
        try {
            //read() 时 客户端是非正常关闭，服务端这边会引发IOException 因此在 catch{} 中 要关闭 channel
            final int read = channel.read(allocate);
            String message = "";
            if (read > 0) {
                allocate.flip();
                message = message + Charset.forName("UTF-8").decode(allocate);

            } else if (read == -1) {
                //此处可能会是 客户端主动关闭 返回-1  服务器要手动关闭通道
                //马虎之处
                //java nio中，为什么客户端一方正常关闭了Socket，而服务端的isReadable()还总是返回true? https://www.codercto.com/a/64945.html
                channel.close();
                System.out.println("1关闭通道....");
                return;
            }
            channel.register(selector, SelectionKey.OP_READ);
            if (message.length() > 0) {
                System.out.println(message);
                castOtherClient(message, selector, channel);
            }
        } catch (IOException e) {
            selectionKey.cancel();
            channel.close();
            System.out.println("2关闭通道....");
            e.printStackTrace();
        }
    }

    public void castOtherClient(String message, Selector selector, SocketChannel socketChannel) throws IOException {
        // final Set<SelectionKey> selectionKeys = selector.selectedKeys();
        final Set<SelectionKey> selectionKeys = selector.keys();
        for (SelectionKey selectionKey : selectionKeys) {
            SelectableChannel channel = selectionKey.channel();
            if (channel instanceof SocketChannel) {
                ((SocketChannel) channel).write(Charset.forName("UTF-8").encode(message));
            }
        }
    }

    public static void main(String[] args) {
        try {
            final ChatServer chatServer = new ChatServer();
            chatServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
