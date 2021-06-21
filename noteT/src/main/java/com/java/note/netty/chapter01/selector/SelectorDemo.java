package com.java.note.netty.chapter01.selector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * ClassName: SelectorDemo
 * Description:
 * date: 2020/12/12 10:31
 *
 * @author 小刘讲师，微信：vv517956494
 * 本课程属于 小刘讲师 VIP 源码特训班课程
 * 严禁非法盗用（如有发现非法盗取行为，必将追究法律责任）
 * <p>
 * 如有同学发现非 小刘讲源码 官方号传播本视频资源，请联系我！
 * @since 1.0.0
 */
public class SelectorDemo {
    public static void main(Strings[] args) throws IOException {
        // java 获取一个selector，selector 底层实现是什么？
        // 1.windows 默认是 select 系统函数
        // 2.linux 默认是 epoll
        Selector selector = Selector.open();

        // 如何将Channel注册到selector呢？
        // 1.需要将channel设置成为非阻塞模式，因为默认Channel都是阻塞模式。
        // 2.注册，并且设置感兴趣的事件
        // 伪代码...假设当前是服务端代码，通过clientChannel获取一个客户端channel
        SocketChannel socketChannel = clientChannel();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        // SelectionKey 中定义的常量
        // 1. OP_READ：        1 << 0 =  0000 0000 0000 0000 0000 0000 0000 0001
        // 2. OP_WRITE：      1 << 2 =  0000 0000 0000 0000 0000 0000 0000 0100
        // 3. OP_CONNECT： 1 << 3 =  0000 0000 0000 0000 0000 0000 0000 1000
        // 4. OP_ACCEPT：    1 << 4 =  0000 0000 0000 0000 0000 0000 0001 0000

        // 如果即想监听通道的 可读 且 又想监听通道的 可写，怎么做？
        //0000 0000 0000 0000 0000 0000 0000 0001
        // |
        //0000 0000 0000 0000 0000 0000 0000 0100
        //0000 0000 0000 0000 0000 0000 0000 0101
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);


        // selector 如何使用呢？ 假设selector上 已经注册了 n 个 Channel了。
        // 1.select() 这个接口 会阻塞当前线程，直到 selector 管理的 Channel ，有某个就绪之后，才会唤醒主线程
        //  int readyChannels = selector.select();

        // 2.select(long timeout) 这个接口会带超时时间 去 阻塞调用线程，如果在超时时间范围内有就绪的Channel 则提前返回，否则 一直等待超时时间为止。
        // int readyChannels = selector.select(2000);

        // 3.selectNow() 非阻塞select接口，调用之后会立马返回，如果底层有Channel就绪则返回就绪Channel数量，否则返回0.
        int readyChannels = selector.selectNow();

    }

    private static SocketChannel clientChannel() {
        return null;
    }
}
