package com.java.note.Jdk.io.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 16:10
 * @description :
 * @question :
 * @usinglink :
 **/
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("shoudaol");
        Strings firstMessage = "hello";
        ctx.writeAndFlush(firstMessage.getBytes());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
 
