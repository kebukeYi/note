package com.mmy.nio.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 18:44
 * @description: Scattering Reads 是指数据从一个 channel 读取到多个 buffer 中
 * @question:
 * @link:
 **/
public class ScatteringDemo {


    public static void main(String[] args) throws IOException {

        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);
        ByteBuffer[] bufferArray = {header, body};

        final SocketChannel channel = SocketChannel.open();

        //注意 buffer 首先被插入到数组，然后再将数组作为 channel.read() 的输入参数。
        //read()方法按照 buffer 在数组中的顺序将从 channel 中读取的数据写入到 buffer，当
        //一个 buffer 被写满后，channel 紧接着向另一个 buffer 中写。
        //Scattering Reads 在移动下一个 buffer 前，必须填满当前的 buffer，这也意味着它
        //不适用于动态消息(译者注：消息大小不固定)。换句话说，如果存在消息头和消息体，
        //消息头必须完成填充（例如 128byte），Scattering Reads 才能正常工作。
        channel.read(bufferArray);


    }
}
 
