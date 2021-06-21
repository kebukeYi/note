package com.java.note.netty.bybuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @Author : fang.com
 * @CreatTime : 2020-12-15 17:30
 * @Description :
 * @Version :  0.0.1
 */
public class ByteBufTest {

    public static void main(Strings[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
        print("allocate ByteBuf(9, 100)", buffer);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写, 写 完 int 类型之后，写指针增加4
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print("writeBytes(1,2,3,4)", buffer);

        buffer.writeInt(12);
        // write 方法改变写指针, 写完之后写指针等于 capacity 的时候，buffer 不可写
        print("writeInt(12)", buffer);

        buffer.writeBytes(new byte[]{5});
        // write 方法改变写指针，写的时候发现 buffer 不可写则开始扩容，扩容之后 capacity 随即改变
        print("writeBytes(5)", buffer);

        buffer.writeBytes(new byte[]{6});
        // get 方法不改变读写指针
        print("writeBytes(6)", buffer);

        System.out.println("getByte(3) return: " + buffer.getByte(3));
        System.out.println("getShort(3) return: " + buffer.getShort(3));
        System.out.println("getInt(3) return: " + buffer.getInt(3));

        // set 方法不改变读写指针
        print("getByte()", buffer);
        buffer.setByte(buffer.readableBytes() + 1, 0);

        // read 方法改变读指针
        print("setByte()", buffer);
        byte[] dst = new byte[buffer.readableBytes()];
        buffer.readBytes(dst);
        print("readBytes(" + dst.length + ")", buffer);

    }

    private static void print(Strings action, ByteBuf buffer) {
        System.out.println("after ===========" + action + "============");
        System.out.println("capacity(): " + buffer.capacity());
        System.out.println("maxCapacity(): " + buffer.maxCapacity());
        System.out.println("readerIndex(): " + buffer.readerIndex());
        System.out.println("readableBytes(): " + buffer.readableBytes());
        System.out.println("isReadable(): " + buffer.isReadable());
        System.out.println("writerIndex(): " + buffer.writerIndex());
        System.out.println("writableBytes(): " + buffer.writableBytes());
        System.out.println("isWritable(): " + buffer.isWritable());
        System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes());
        System.out.println();
    }
}
