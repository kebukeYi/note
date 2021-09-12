package com.mmy.nio.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 18:45
 * @description: Gathering Writes 是指数据从多个 buffer 写入到同一个 channel
 * @question:
 * @link:
 **/
public class GatheringDemo {
    public static void main(String[] args) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);
        //write data into buffers
        ByteBuffer[] bufferArray = {header, body};

        final SocketChannel channel = SocketChannel.open();
        //buffers 数组是 write()方法的入参，write()方法会按照 buffer 在数组中的顺序，将数
        //据写入到 channel，注意只有 position 和 limit 之间的数据才会被写入。因此，如果
        //一个 buffer 的容量为 128byte，但是仅仅包含 58byte 的数据，那么这 58byte 的数
        //据将被写入到 channel 中。因此与 Scattering Reads 相反，Gathering Writes 能较
        //好的处理动态消息
        channel.write(bufferArray);
    }
}
 
