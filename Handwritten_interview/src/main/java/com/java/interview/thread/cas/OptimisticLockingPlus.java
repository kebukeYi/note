package com.java.interview.thread.cas;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : kebukeYi
 * @date :  2021-10-23 16:53
 * @description:
 * @question:
 * @link:
 **/
public class OptimisticLockingPlus {
    //静态常量：线程数
    private static final int THREAD_COUNT = 10;
    //成员属性：包装的值
    volatile private int value;
    //静态常量：JDK 不安全类的实例
    // private static final Unsafe unsafe = JvmUtil.getUnsafe();
    //静态常量：value 成员的相对偏移（相对于对象头）
    //private static final long valueOffset;
    //静态常量：CAS 的失败次数
    private static final AtomicLong failure = new AtomicLong(0);
}
 
