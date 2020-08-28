package com.java.note.Jdk.thread.synchronizedd;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/9  11:10
 * @Description
 */
public class MySynchronized {
    /**
     * 对于synchronized关键字而言，javac在编译时，会生成对应的monitorenter和monitorexit指令分别对应synchronized同步块的进入和退出，
     * 有两个monitorexit指令的原因是为了保证抛异常的情况下也能释放锁，所以javac为同步代码块添加了一个隐式的try-finally，
     * 在finally中会调用monitorexit命令释放锁。
     * <p>
     * 而对于synchronized方法而言，javac为其生成了一个ACC_SYNCHRONIZED关键字，在JVM进行方法调用时，
     * 发现调用的方法被ACC_SYNCHRONIZED修饰，则会先尝试获得锁。
     */
    public void syncBlock() {
        synchronized (this) {
            System.out.println("hello block");
        }
    }

    public synchronized void syncMethod() {
        System.out.println("hello method");
    }
}
