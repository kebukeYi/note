package com.java.note.netty.UDP.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Random;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/3  23:07
 * @Description
 */
public class UDPProverbClient {

    public void run(int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UDPProverbClientHandler());

            Channel ch = b.bind(0).sync().channel();
            //向网段内的所有机器广播
            ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("汽车人上报的信号值：" + new Random().nextInt(100), CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", port))).sync();

            //客户端等待15s用于接收服务端的应答消息，然后退出并释放资源
            if (!ch.closeFuture().await(15000)) {
                System.out.println("查询超时！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(Strings[] args) throws Exception {
        int port = 18888;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new UDPProverbClient().run(port);
    }

}
