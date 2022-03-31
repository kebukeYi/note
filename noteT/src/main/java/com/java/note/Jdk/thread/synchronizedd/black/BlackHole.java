package com.java.note.Jdk.thread.synchronizedd.black;

/**
 * @ClassName blackhole
 * @Author kebukeyi
 * @Date 2022/3/29 23:43
 * @Description 目的： 假如子线程没有获得锁的话 主线程不返回 继续阻塞
 * @Version 1.0.0
 */
public class BlackHole {

    public static void enter(Object obj) {
        System.out.println("step 1");
        //主线程调用
        magic(obj);
        System.out.println("step 2");
        //主线程 可能  调用
        synchronized (obj) {
            System.out.println("step 3");
        }
    }

    public static void magic(final Object obj) {
        //主线程创建子线程 但是子线程未启动
        final Thread thread = new Thread() {
            @Override
            public void run() {
                //子线程 获得 入参 obj  锁
                synchronized (obj) {
                    //子线程 获得 thread 锁
                    synchronized (this) {
                        //设置跳出循环条件
                        this.setName("Locked");
                        // 唤醒在 thread 上线程 (这里是主线程) || 通知主线程 已经获得了 obj 锁
                        this.notifyAll();
                    }
                    while (true) {
                        //子线程 获得了 obj  锁 并 无限循环
                    }
                }
            }
        };
        //主线程 获得 thread 锁
        synchronized (thread) {
            thread.setName("");
            //子线程启动
            thread.start();
            //这里主线程进行判断
            while (thread.getName().equals("")) {
                try {
                    //主线程释放 thread 锁 并 进行 wait 等待唤醒
                    thread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
