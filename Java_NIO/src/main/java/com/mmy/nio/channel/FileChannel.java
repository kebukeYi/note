package com.mmy.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 17:26
 * @description:
 * @question:
 * @link:
 **/
public class FileChannel {


    public static void main(String[] args) throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile("F:\\01.txt", "rw");
        final java.nio.channels.FileChannel fileChannel = randomAccessFile.getChannel();
        //在 FileChannel 的某个特定位置进行数据的读/写操作
        long pos = fileChannel.position();
        fileChannel.position(pos + 0);
        //文件大小
        long fileSize = fileChannel.size();
        System.out.println("文件大小 : " + fileSize);

        //截取文件的前 1024 个字节
        final java.nio.channels.FileChannel truncate = fileChannel.truncate(1024);
        final long size = truncate.size();
        System.out.println("文件大小 : " + size);

        //只会先读取100个字节
        final ByteBuffer buffer = ByteBuffer.allocate(100);
        //将数据写入缓冲区
        int read = fileChannel.read(buffer);
        while (read != -1) {
            System.out.println("读取了 : " + read);
            //调用 buffer.flip() 反转读写模式
            buffer.flip();
            //buffer 中是否还存在数据
            while (buffer.hasRemaining()) {
                //从 buffer 中拿取
                System.out.println((char) buffer.get());
            }
            // 调用 buffer.clear() 或 buffer.compact() 清除缓冲区内容
            buffer.clear();
            //读取下一组 10字节的数据
            read = fileChannel.read(buffer);
        }
        buffer.clear();
        //向文件中写数据 避免一次性 写入数据过大
        String newData = "\nNew String to write to file..." + System.currentTimeMillis();
        buffer.put(newData.getBytes());
        buffer.flip();
        //要保证这一点，需要调用 force()方法
        //有一个 boolean 类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上
        fileChannel.force(true);
        //buffer 中是否还存在数据
        while (buffer.hasRemaining()) {
            //处于性能的原因,操作系统会将数据缓存在内存中，所以无法保证写入到 FileChannel 里的数据一定会即时写到磁盘上
            fileChannel.write(buffer);
        }
        randomAccessFile.close();
        fileChannel.close();
        System.out.println("over");
    }
}
 
