package com.java.note.Jdk.thread.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  16:05
 * @Description: https://juejin.cn/post/6844903975175766024
 */
public class MyUnsafeTest {

    //直接操作内存举例
    public static void main(Strings[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        // 因为theUnsafe字段在Unsafe类中是一个静态字段，所以通过Field.get()获取字段值时，可以传null获取
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        // 指针大小
        System.out.println(unsafe.addressSize());
        // 内存页大小
        System.out.println(unsafe.pageSize());
        //分配内存
        Author author = (Author) unsafe.allocateInstance(Author.class);
        System.out.println(author.getSeq());
        Field seqField = Author.class.getDeclaredField("seq");
        //内存首地址
        long seqOffset = unsafe.objectFieldOffset(seqField);
        //通过计算内存偏移，并使用putInt()方法，类的seq被修改。在已知类结构的时候，数据的偏移总是可以计算出来（与c++中的类中数据的偏移计算是一致的）
        unsafe.putInt(author, seqOffset, 2);

        System.out.println(author.getSeq());


    }
}
