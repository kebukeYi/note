package com.java.note.netty.UDP.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/3  23:03
 * @Description
 */
public class UDPProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final String[] DICTIONARY = {"信号强", "信号很强", "信号弱"};

    private String nextQuote() {
        //线程安全岁基类，避免多线程环境发生错误
        int quote = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quote];
    }

    //接收Netty封装的DatagramPacket对象，然后构造响应消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        //利用ByteBuf的toString()方法获取请求消息
        String req = packet.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);
        //创建新的DatagramPacket对象，传入返回消息和目的地址（IP和端口）
        ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("信号检测结果：" + nextQuote(), CharsetUtil.UTF_8), packet.sender()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

}
