package com.java.note.netty.UDP.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/3  23:07
 * @Description
 */
public class UDPProverbClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        System.out.println("acceptInboundMessage 2" + atomicInteger.incrementAndGet());
        return super.acceptInboundMessage(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 1" + atomicInteger.incrementAndGet());
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        System.out.println("channelRead0 3" + atomicInteger.incrementAndGet());
        String response = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(response);
        //ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught " + atomicInteger.incrementAndGet());
        cause.printStackTrace();
        ctx.close();
    }

}
