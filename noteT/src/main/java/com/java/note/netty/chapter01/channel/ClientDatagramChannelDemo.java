package com.java.note.netty.chapter01.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * ClassName: ClientDatagramChannelDemo
 * Description:
 * date: 2020/12/11 12:15
 *
 * @author 小刘讲师，微信：vv517956494
 * 本课程属于 小刘讲师 VIP 源码特训班课程
 * 严禁非法盗用（如有发现非法盗取行为，必将追究法律责任）
 * <p>
 * 如有同学发现非 小刘讲源码 官方号传播本视频资源，请联系我！
 * @since 1.0.0
 */
public class ClientDatagramChannelDemo {

    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        //绑定自己的数据的出口 因为不需要建立连接
        channel.socket().bind(new InetSocketAddress(8848));

        String newData = "New String to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(newData.getBytes());
        buf.flip();
        int bytesSent = channel.send(buf, new InetSocketAddress("127.0.0.1", 9090));
    }
}
