package com.java.note.netty.chapter01.buffer;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.channels.SocketChannel;

/**
 * ClassName: CreateBufferDemo
 * Description:
 * date: 2020/12/10 12:14
 *
 * @author 小刘讲师，微信：vv517956494
 * 本课程属于 小刘讲师 VIP 源码特训班课程
 * 严禁非法盗用（如有发现非法盗取行为，必将追究法律责任）
 * <p>
 * 如有同学发现非 小刘讲源码 官方号传播本视频资源，请联系我！
 * @since 1.0.0
 */
public class BufferDemo {
    public static void main(String[] args) {
        compactBuffer();
    }

    public static void createBuffer() {
        //每个 Buffer 实现类都提供了一个静态方法 allocate(int capacity) 帮助我们快速实例化一个 Buffer。
        //如：
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        IntBuffer intBuf = IntBuffer.allocate(1024);
        LongBuffer longBuf = LongBuffer.allocate(1024);
        //.....类似的就不说了


        //还有另外一种 获取 buffer 的方式，使用wrap
        byte[] bytes = new byte[1024];
        // writeSomething(bytes);
        ByteBuffer.wrap(bytes);
    }


    public static void writeBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // byte 范围是多少？ byte 由8bit表示，2^8=256，范围[-128~127]
        byte b = 127;
        // 填充一个 byte 值
        byteBuffer.put(b);


        // 在指定位置填充一个 byte 值
        byteBuffer.put(20, b);


        // 将一个数组中的值填充进去
        byte[] byteArray = "something".getBytes();
        byteBuffer.put(byteArray);

        // 在指定位置填充一个 byte 数组
        //参数一：byte数组  参数二：src开始位置   参数三：有效数据长度
        byteBuffer.put(byteArray, 0, byteArray.length);
        //上述这些方法需要自己控制 Buffer 大小，不能超过 capacity，超过会抛 java.nio.BufferOverflowException 异常。


        // 对于 Buffer 来说，另一个常见的操作中就是，我们要将来自 Channel 的数据填充到 Buffer 中，
        // 在系统层面上，这个操作我们称为读操作，因为数据是从外部（文件或网络等）读到内存中。
        // ps：伪代码，这段代码无法正常运行..
        try {
            SocketChannel socketChannel = socketChannel();
            //read方法会返回读取的数据量 num，相对于buffer来说，channel 读取 对应 buffer 写操作。
            int num = socketChannel.read(byteBuffer);
        } catch (IOException e) {
        }
    }


    /**
     * 提取 Buffer 中的值
     * <p>
     * 写操作中，介绍了buffer每写一个值，position都会加一。所以position当前位置为有效数据后一位，
     * 如果buffer写满了，position的值就和buffer.capacity容量一致。
     * <p>
     * 如果要读取buffer内的数据，第一件事是切换buffer的模式，从“写模式”切换至“读模式”，使用flip()方法完成。
     * <p>
     * Buffer:
     * public final Buffer flip() {
     * limit = position; // 将 limit 设置为实际写入的数据数量
     * position = 0; // 重置 position 为 0
     * mark = -1; // mark 之后再说
     * return this;
     * }
     */
    public static void readBuffer(ByteBuffer byteBuffer) {
        // 1. 按照position顺序读取，每get一次，position + 1
        byte val1 = byteBuffer.get();

        // 2. 获取指定位置的数据
        byte val2 = byteBuffer.get(10);

        // 3. 将buffer中的数据，拷贝到一个指定的数组中
        byte[] bytes = new byte[1024];
        byteBuffer.get(bytes);
        ByteBuffer.allocateDirect(1024).flip();

        // 4. 另一个经常使用的方法，直接返回Buffer内部的byte[]数组
        String str = new String(byteBuffer.array());


        // 5.
        // 网络编程中，我们将数据发送至对端 或者 使用FileChannel将内存中的数据写入到硬盘
        // 这种操作，我们通常称之为 Channel 的写操作，与之对应的 Buffer 就是 读操作。
        // ps：伪代码，这段代码无法正常运行..
        try {
            SocketChannel socketChannel = socketChannel();
            // byteBuffer.flip();    一定要注意！
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
        }
    }

    /**
     * mark&reset:
     * mark 用于临时保存 position 的值，每次调用 mark() 方法都会将 mark 设值为当前的 position，便于后续需要的时候使用。
     * <p>
     * public final Buffer mark() {
     * mark = position;
     * return this;
     * }
     * <p>
     * 那mark有什么用呢？
     * 想要重复读取buffer内某段空间时，可以使用mark与reset组合。
     * 比如说：我们在position = 5 的位置开始读取Buffer，在读取之前先使用mark()方法保存一下当前读取的位点，
     * 假设读取到 position = 50 的时候，我想要重新读取 buffer [5,50] 这块区域，那么此时就可以使用reset()方法 重置 buffer 的position。
     * <p>
     * public final Buffer reset() {
     * int m = mark;
     * if (m < 0)
     * throw new InvalidMarkException();
     * position = m;
     * return this;
     * }
     */
    public static void markResetBuffer(ByteBuffer byteBuffer) {
    }


    /**
     * rewind() & clear() & compact()
     * <p>
     * rewind()：会重置 position 为 0，通常用于重新从头读 Buffer，源码中可以看出
     * 只修改了position = 0，并没有修改limit。
     * public final Buffer rewind() {
     * position = 0;
     * mark = -1;
     * return this;
     * }
     * <p>
     * clear()：有点重置 Buffer 的意思，相当于重新实例化了一样。
     * 通常，我们使用Buffer会先填充 Buffer，然后从 Buffer 读取数据，
     * 之后我们再重新往里填充新的数据，我们一般在重新填充之前先调用 clear()
     * <p>
     * public final Buffer clear() {
     * position = 0;
     * limit = capacity;
     * mark = -1;
     * return this;
     * }
     * <p>
     * compact()：调用这个方法以后，会先处理还没有读取的数据，也就是 position 到 limit 之间的
     * 数据（还没有读过的数据），先将这些数据移到左边，然后在这个基础上再开始写入，利用好每一个字节的位置；
     */
    public static void compactBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        byte[] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byteBuffer.put(bytes);

        // 读之前，应该怎么操作呢？
        byteBuffer.flip();

        for (int i = 0; i < 4; i++) {
            System.out.println(byteBuffer.get());
        }
        // 4
        System.out.println("compact操作之前buffer的position：" + byteBuffer.position());

        // 读取了一部分之后呢，又想继续向buffer中填充数据呢，为了提升可写空间，可以将已读过的数据的空间释放掉，对应的操作就是compact()
        byteBuffer.compact();
        // 6
        System.out.println("compact操作之后buffer的position：" + byteBuffer.position());
    }

    // 伪代码.. 假设这个方法返回一个客户端channel
    private static SocketChannel socketChannel() {
        return null;
    }
}
