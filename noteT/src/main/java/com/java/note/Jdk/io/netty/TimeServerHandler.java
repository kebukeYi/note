package com.java.note.Jdk.io.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 16:02
 * @description :
 * @question :
 * @usinglink :
 **/
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String firstMessage = "nihao";
        ctx.writeAndFlush(firstMessage.getBytes());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String firstMessage = "nihao";
        ctx.writeAndFlush(firstMessage.getBytes());
    }
}
 
