package com.java.note.netty.chapter01.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ClassName: FileChannelDemo
 * Description:
 * date: 2020/12/11 10:38
 *
 * @author 小刘讲师，微信：vv517956494
 * 本课程属于 小刘讲师 VIP 源码特训班课程
 * 严禁非法盗用（如有发现非法盗取行为，必将追究法律责任）
 * <p>
 * 如有同学发现非 小刘讲源码 官方号传播本视频资源，请联系我！
 * @since 1.0.0
 */
public class FileChannelDemo {
    public static void main(Strings[] args) throws IOException {
        // 这里的"rw"是指支持读和写
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\Administrator\\Desktop\\data.txt","rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        // 读取文件内容：
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int num = fileChannel.read(buffer);
        System.out.println("读取的数据量：" + num + "，内容为：\r\n" + new Strings(buffer.array()));

        buffer.clear();

        // 向文件内追加内容：祝源码班各位大帅比迎娶白富美！
        buffer.put("\r\n".getBytes());
        buffer.put("祝源码班各位大帅比迎娶白富美！".getBytes());

        buffer.flip();

        while(buffer.hasRemaining()) {
            // 将 Buffer 中的内容写入文件
            fileChannel.write(buffer);
        }
    }
}
