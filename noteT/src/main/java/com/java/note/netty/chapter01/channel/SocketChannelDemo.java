package com.java.note.netty.chapter01.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * ClassName: SocketChannelDemo
 * Description:
 * date: 2020/12/11 11:07
 *
 * @author 小刘讲师，微信：vv517956494
 * 本课程属于 小刘讲师 VIP 源码特训班课程
 * 严禁非法盗用（如有发现非法盗取行为，必将追究法律责任）
 * <p>
 * 如有同学发现非 小刘讲源码 官方号传播本视频资源，请联系我！
 * @since 1.0.0
 */
public class SocketChannelDemo {
    public static void main(String[] args) throws IOException {
        // 打开一个通道
        SocketChannel socketChannel = SocketChannel.open();
        // 发起连接，连接到本机的 EchoServer
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8007));

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("abc".getBytes());
        // 接下来需要将buffer中的数据读取到channel内，其实底层是 socket 缓冲区..
        // 所以需要将buffer从写模式切换到读模式
        byteBuffer.flip();

        socketChannel.write(byteBuffer);

        ByteBuffer echoBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(echoBuffer);

        System.out.println(new String(echoBuffer.array()));
    }
}
