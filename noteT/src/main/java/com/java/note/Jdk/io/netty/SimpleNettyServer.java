package com.java.note.Jdk.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 15:56
 * @description :
 * @question :
 * @usinglink :
 **/
public class SimpleNettyServer {


    public static void main(String[] args) {
        int port = 8080;
        oneNetty(port);
    }


    public static void oneNetty(int port) {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup(2);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(boss, work)
                    .option(ChannelOption.SO_BACKLOG, 20)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new TimeServerHandler());

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("Server start listen at " + port);
            //closeFuture()是开启了一个子线程server channel的监听器，负责监听channel是否关闭的状态
            channelFuture.channel().closeFuture().sync();
            System.out.println("执行到这里 " + port);
           // channel.close()才是主动关闭通道的方法
            // channelFuture.channel().close().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
 
