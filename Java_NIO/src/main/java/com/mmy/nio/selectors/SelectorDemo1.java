package com.mmy.nio.selectors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 00:17
 * @description:
 * @question:
 * @link:
 **/
public class SelectorDemo1 {

    public static void main(String[] args) throws IOException {
        //创建selecotr
        Selector selector = Selector.open();

        //通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //非阻塞
        serverSocketChannel.configureBlocking(false);

        //绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9999));

        //将通道注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //查询已经就绪通道操作
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        //遍历集合
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            //判断key就绪状态操作
            if (key.isAcceptable()) {
                // a connection was accepted by a ServerSocketChannel.

            } else if (key.isConnectable()) {

                // a connection was established with a remote server.

            } else if (key.isReadable()) {

                // a channel is ready for reading

            } else if (key.isWritable()) {

                // a channel is ready for writing

            }
            iterator.remove();
        }


    }


}
 
