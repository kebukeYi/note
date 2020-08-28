package com.java.note.Jdk.thread.join;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/9  15:55
 * @Description
 */
public class CreateThreadTest {

    /**
     * 在main主线程中调用threadA.join()方法，因为join() 方法是一个synchronized方法，
     * 所以主线程会首先持有thread线程对象的锁。接下来在join()方法里面调用wait()方法，
     * 主线程会释放thread线程对象的锁，进入等待状态。最后，threadA线程执行结束，JVM会调用lock.notify_all(thread);
     * 唤醒持有threadA这个对象锁的线程，也就是主线程，所以主线程会继续往下执行。
     * <p>
     * 链接：https://www.jianshu.com/p/5d88b122a050
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程执行开始");
        Thread threadA = new Thread(new RunnableTest(), "线程A");
        threadA.start();
        threadA.join();
        System.out.println("主线程执行结束");
    }

    static class RunnableTest implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "执行开始");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "执行结束");
        }
    }
}
