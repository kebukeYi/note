package com.thread.affinity;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * @author : kebukeYi
 * @date :  2021-12-31 15:57
 * @description:
 * @question:
 * @link:
 **/
public class NettyThreadAffinity {


    public void NettyThreadAffinity() {
        final int acceptorThreads = 1;
        final int workerThreads = 10;
        EventLoopGroup acceptorGroup = new NioEventLoopGroup(acceptorThreads);
        ThreadFactory threadFactory = new AffinityThreadFactory("atf_wrk", AffinityStrategies.DIFFERENT_CORE);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerThreads, threadFactory);
        ServerBootstrap serverBootstrap = new ServerBootstrap().group(acceptorGroup, workerGroup);
        try {
            final ChannelFuture sync = serverBootstrap.bind(10001).sync();
            sync.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("ok");
                    } else {
                        System.out.println("error");
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
 
