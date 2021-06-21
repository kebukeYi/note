package com.java.note.netty.chapter01.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * ClassName: DatagramChannelDemo
 * Description:
 * date: 2020/12/11 12:12
 *
 * @author 小刘讲师，微信：vv517956494
 * 本课程属于 小刘讲师 VIP 源码特训班课程
 * 严禁非法盗用（如有发现非法盗取行为，必将追究法律责任）
 * <p>
 * 如有同学发现非 小刘讲源码 官方号传播本视频资源，请联系我！
 * @since 1.0.0
 */
public class DatagramChannelDemo {

    public static void main(Strings[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(9090));

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (true) {
            channel.receive(buf);
            System.out.println("服务器收到UDP报文，内容为：");
            System.out.println(new Strings(buf.array()));
            buf.clear();
        }

    }
}
