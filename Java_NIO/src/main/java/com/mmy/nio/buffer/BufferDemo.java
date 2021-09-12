package com.mmy.nio.buffer;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 21:26
 * @description:
 * @question:
 * @link:
 **/
public class BufferDemo {


    @Test
    public void buffer01() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        CharBuffer charBuffer = CharBuffer.allocate(1024);

        //将 position 设回 0，所以你可以重读 Buffer 中的所有数据。limit 保
        //持不变，仍然表示能从 Buffer 中读取多少个元素（byte、char 等）
        byteBuffer.rewind();

        //如果 Buffer 中仍有未读的数据，且后续还需要这些数据，但是此时想要先先写些数
        //据，那么使用 compact()方法
        //compact()方法将所有未读的数据拷贝到 Buffer 起始处。然后将 position 设到最后一
        //个未读元素正后面。limit 属性依然像 clear()方法一样，设置成 capacity。现在
        //Buffer 准备好写数据了，但是不会覆盖未读的数据
        byteBuffer.compact();

        //clear()方法，position 将被设回 0，limit 被设置成 capacity 的值。换
        //句话说，Buffer 被清空了。Buffer 中的数据并未清除，只是这些标记告诉我们可以从
        //哪里开始往 Buffer 里写数据
        byteBuffer.clear();

        //可以标记 Buffer 中的一个特定 position。之后可以通
        //过调用 Buffer.reset()方法恢复到这个 position
        byteBuffer.mark();

        byteBuffer.reset();

    }

    @Test
    public void testConect3() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 缓冲区中的数据 0-9
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }
        // 创建子缓冲区
        //设置新的 目标位
        buffer.position(3);
        //设置新的 limit
        buffer.limit(7);
        //创建新的内存容量
        ByteBuffer slice = buffer.slice();
        // 改变子缓冲区的内容
        for (int i = 0; i < slice.capacity(); ++i) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i, b);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.remaining() > 0) {
            System.out.println(buffer.get());
        }
    }

    @Test
    public void testConect4() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 缓冲区中的数据 0-9
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }
        // 创建只读缓冲区
        ByteBuffer readonly = buffer.asReadOnlyBuffer();
        // 改变原缓冲区的内容
        for (int i = 0; i < buffer.capacity(); ++i) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }
        readonly.position(0);
        readonly.limit(buffer.capacity());
        // 只读缓冲区的内容也随之改变
        while (readonly.remaining() > 0) {
            System.out.println(readonly.get());
        }
    }

    @Test
    public void testConect5() throws IOException {
        //直接缓冲区
        //给定一个直接字节缓冲区，Java 虚拟机将尽最大努力直接对它执行本机
        //I/O 操作。也就是说，它会在每一次调用底层操作系统的本机 I/O 操作之前(或之后)，
        //尝试避免将缓冲区的内容拷贝到一个中间缓冲区中 或者从一个中间缓冲区中拷贝数
        //据。要分配直接缓冲区，需要调用 allocateDirect()方法，而不是 allocate()方法，使
        //用方式与普通缓冲区并无区别。
        String infile = "F:\\01.txt";
        FileInputStream fin = new FileInputStream(infile);
        FileChannel fcin = fin.getChannel();
        String outfile = String.format("F:\\02.txt");
        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel fcout = fout.getChannel();
        // 使用 allocateDirect，而不是 allocate
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            buffer.clear();
            int r = fcin.read(buffer);
            if (r == -1) {
                break;
            }
            buffer.flip();
            fcout.write(buffer);
        }
    }

    static private final int start = 0;
    static private final int size = 1024;


    //内存映射文件 I/O 是一种读和写文件数据的方法，它可以比常规的基于流或者基于通
    //道的 I/O 快的多。内存映射文件 I/O 是通过使文件中的数据出现为 内存数组的内容来
    //完成的，这其初听起来似乎不过就是将整个文件读到内存中，但是事实上并不是这
    //样。一般来说，只有文件中实际读取或者写入的部分才会映射到内存中
    static public void main(String args[]) throws Exception {
        RandomAccessFile raf = new RandomAccessFile("F:\\01.txt", "rw");
        FileChannel fc = raf.getChannel();
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);
        mbb.put(0, (byte) 97);
        mbb.put(1023, (byte) 122);
        raf.close();
    }


}
 
