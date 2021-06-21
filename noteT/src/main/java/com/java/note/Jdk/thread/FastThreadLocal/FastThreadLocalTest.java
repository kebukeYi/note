package com.java.note.Jdk.thread.FastThreadLocal;

import io.netty.util.concurrent.FastThreadLocal;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 19:57
 * @description :
 * @question :
 * @usinglink : https://blog.csdn.net/lirenzuo/article/details/94495469
 **/
public class FastThreadLocalTest {

    private static FastThreadLocal<Integer> fastThreadLocal = new FastThreadLocal<>();


    public static void main(Strings[] args) {
        //if (thread instanceof FastThreadLocalThread) 使用FastThreadLocalThread更优，普通线程也可以
//        new FastThreadLocalThread(() -> {
//            for (int i = 0; i < 10; i++) {
//                //threadLocal1 中 set
//                fastThreadLocal.set(i);
//                InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
//                threadLocalMap.setIndexedVariable(3, "33");
//                //threadLocal1 中 get
//                System.out.println(Thread.currentThread().getName() + "====" + fastThreadLocal.get());
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "fastThreadLocal1").start();
//
//        new FastThreadLocalThread(() -> {
//            for (int i = 0; i < 10; i++) {
//                //在 fastThreadLocal2 中 获取 fastThreadLocal1 线程在threadLocal 中设置的值
//                //那必然是 获取不到的
//                System.out.println(Thread.currentThread().getName() + "====" + fastThreadLocal.get());
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "fastThreadLocal2").start();


        System.out.println(tableSizeFor(9));
        System.out.println(tableSizeFor2(9));
        System.out.println("---------------------");
        System.out.println(tableSizeFor(32));
        System.out.println(tableSizeFor2(32));

    }

    static final int tableSizeFor(int cap) {
        //容量减1，为了防止初始化容量已经是2的幂的情况，否则传入16返回32，-1的话 传入16返回16，最后有+1运算
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        //如果入参cap为小于或等于0的数，那么经过cap-1之后n为负数，n经过无符号右移和或操作后仍为负数,
        // 所以如果n<0,则返回1;如果n大于或等于最大容量，则返回最大容量;否则返回n+1
        return n + 1;
    }

    static final int tableSizeFor2(int n) {
        //容量减1，为了防止初始化容量已经是2的幂的情况，否则传入16返回32，-1的话 传入16返回16，最后有+1运算
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        //如果入参cap为小于或等于0的数，那么经过cap-1之后n为负数，n经过无符号右移和或操作后仍为负数,
        // 所以如果n<0,则返回1;如果n大于或等于最大容量，则返回最大容量;否则返回n+1
        return n + 1;
    }


}
 
