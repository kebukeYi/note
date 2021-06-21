package com.java.note.netty.Http.server.handler;

import com.java.note.netty.Http.ByteBufToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/4  9:37
 * @Description Netty 客户端访问
 */
public class HttpServerHandler2 extends ChannelInboundHandlerAdapter {
    private ByteBufToBytes reader;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.println("messageType:" + request.headers().get("messageType"));
            System.out.println("businessType:" + request.headers().get("businessType"));
            System.out.println("method:" + request.method());
            System.out.println("getUri:" + request.uri());
            if (HttpUtil.isContentLengthSet(request)) {
                reader = new ByteBufToBytes((int) HttpUtil.getContentLength(request));
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            reader.reading(content);
            content.release();
            if (reader.isEnd()) {
                Strings resultStr = new Strings(reader.readFull());
                System.out.println("Client said:" + resultStr);
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("I am ok".getBytes()));
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.write(response);
                ctx.flush();
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
