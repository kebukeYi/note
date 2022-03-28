package com.java.note.Jdk.thread.synchronizedd;

/**
 * @Author : mmy
 * @CreatTime : 2020/6/9  11:10
 * @Description
 */
public class MySynchronized {

    private static String name;

    /**
     * 对于synchronized关键字而言，javac在编译时，会生成对应的monitorenter和monitorexit指令分别对应synchronized同步块的进入和退出，
     * 有两个monitorexit指令的原因是为了保证抛异常的情况下也能释放锁，
     * 所以javac为同步代码块添加了一个隐式的try-finally，
     * 在finally中会调用monitorexit命令释放锁。
     * <p>
     */
    public void syncBlock() {
        synchronized (this) {
            System.out.println("hello block");
        }
    }

    /*
     * 而对于synchronized方法而言，javac为其生成了一个ACC_SYNCHRONIZED关键字，在JVM进行方法调用时，
     * 发现调用的方法被ACC_SYNCHRONIZED修饰，则会先尝试获得锁。
     */
    public synchronized void syncMethod() {
        //检验是否获得了锁
        assert Thread.holdsLock(this);
        System.out.println("hello method");
    }

    /*
     * 静态方法加锁
     * 实例相关的锁 跟 静态方法的锁不一样
     * 所以互相不影响
     */
    static synchronized void syncStaticMethod() {

    }

    /*
     * 静态代码块加锁
     */
    static void syncStaticClass() {
        synchronized (MySynchronized.class) {
            System.out.println(name);
        }
    }


    /*
     * 由于此方法没有 synchronized 关键字
     * 所以不论是否有其他方法执行  syncMethod、syncBlock 等 都可以执行此方法
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        MySynchronized.name = name;
    }
}
