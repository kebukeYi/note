package com.java.note.Jdk.thread.FastThreadLocal;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 20:16
 * @description :
 * @question :
 * @usinglink : https://mp.weixin.qq.com/s/8Ql-5kaUtxiCWyHR6uPPBw
 **/
public class ThreadLocalTest {

    //这里把ThreadLocal定义为static还有一个好处就是，由于ThreadLocal有强引用在，
    // 那么在ThreadLocalMap里对应的Entry的键会永远存在，那么执行remove的时候就可以正确进行定位到并且删除！！！
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 111;
        }
    };

    public static void main(Strings[] args) {
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    //默认是空
                    System.out.println(Thread.currentThread().getName() + "====" + threadLocal.get());
                    //threadLocal1 中 set
                    threadLocal.set(i);
                    //threadLocal1 中 get
                    System.out.println(Thread.currentThread().getName() + "====" + threadLocal.get());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                threadLocal.remove();
                System.out.println("threadLocal1 over");
            }
        }, "threadLocal1").start();

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    //在 threadLocal2 中 获取 threadLocal1 线程在threadLocal 中设置的值
                    //那必然是 获取不到的
                    //只能获取到 初始化的值 111
                    System.out.println(Thread.currentThread().getName() + "====" + threadLocal.get());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                threadLocal.remove();
                System.out.println("threadLocal2 over");
            }
        }, "threadLocal2").start();
    }
}
 
