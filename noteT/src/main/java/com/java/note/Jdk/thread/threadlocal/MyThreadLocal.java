package com.java.note.Jdk.thread.threadlocal;


import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/9  16:07
 * @Description
 */
public class MyThreadLocal {


    /**
     * 链接：https://zhuanlan.zhihu.com/p/163852829
     * Spring采用ThreadLocal的方式，来保证单个线程中的数据库操作使用的是同一个数据库连接，同时，采用这种方式可以使业务层使用事务时不需要感知并管理connection对象，
     * 通过传播级别，巧妙地管理多个事务配置之间的切换，挂起和恢复。Spring框架里面就是用的ThreadLocal来实现这种隔离，主要是在TransactionSynchronizationManager这个类里面
     * <p>
     * 为什么需要数组呢？没有了链表怎么解决Hash冲突呢？
     * 用数组是因为，我们开发过程中可以一个线程可以有多个TreadLocal来存放不同类型的对象的，但是他们都将放到你当前线程的ThreadLocalMap里，所以肯定要数组来存。
     * ThreadLocal的不足，我觉得可以通过看看Netty的fastThreadLocal来弥补，大家有兴趣可以康康。
     */


    // 第一次get()方法调用时会进行初始化(如果set方法没有调用)，每个线程会调用一次
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    private static final ThreadLocal<String> TIME_THREADLOCAL_2 = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return System.currentTimeMillis() + "over";
        }
    };

    /**
     * 父子线程共享数据
     */
    private void test() {

        final ThreadLocal threadLocal = new InheritableThreadLocal();
        threadLocal.set("帅得一匹");

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                System.out.println("张三帅么 =" + threadLocal.get());
            }
        };
        t.start();
    }


    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
        TIME_THREADLOCAL_2.set("第二个");
    }

    public static final long end() {
        System.out.println(TIME_THREADLOCAL_2.get());
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        MyThreadLocal.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: " + MyThreadLocal.end() + " mills");
    }
}
