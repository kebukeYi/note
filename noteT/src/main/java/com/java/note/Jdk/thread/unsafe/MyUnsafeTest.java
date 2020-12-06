package com.java.note.Jdk.thread.unsafe;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  16:05
 * @Description
 */
public class MyUnsafeTest {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
//        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
//        theUnsafe.setAccessible(true);
//        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
//
//        Author author = (Author) unsafe.allocateInstance(Author.class);
//        System.out.println(author.getSeq());
//        Field seqField = Author.class.getDeclaredField("seq");
//        long seqOffset = unsafe.objectFieldOffset(seqField);
//        //通过计算内存偏移，并使用putInt()方法，类的seq被修改。在已知类结构的时候，数据的偏移总是可以计算出来（与c++中的类中数据的偏移计算是一致的）。
//        unsafe.putInt(author, seqOffset, 2);
//
//        System.out.println(author.getSeq());


    }
}
