package com.java.note.Jdk.thread.joins;

/**
 * @author : kebukeyi
 * @date :  2021-05-12 17:10
 * @description :
 * join()方法的作用是：
 * 在当前线程A中调用另外一个线程B的join()方法后，会让当前线程A阻塞，直到线程B的逻辑执行完成，A线程才会解阻塞，
 * 然后继续执行自己的业务逻辑。
 * @question :
 * @usinglink : https://juejin.cn/post/6844903997472505864
 **/
public class JoinDemo {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " 肚子饿了，想吃饭饭");

        Thread b = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 开始做饭饭");
            try {
                // 让线程休眠4秒钟，模拟做饭的过程
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 饭饭做好了");
        });
        b.start();

        try {
            // 等待饭做好
            // 注意 : 此时是在main线程中调用的 其他 b 线程的 join 方法, 因此 就会把 main 线程 给挂到 b 线程尾巴上
            //重要 : 我们是在main线程中，调用了thread这个对象的join()方法，所以这里调用wait()方法时，调用的是thread这个对象的wait()方法，所以是main线程进入到等待状态中
            //既然调用了wait()方法，那么notify()或者notifyAll()方法是在哪儿被调用的？
            //wait()和notify()或者notifyAll()肯定是成对出现的，单独使用它们毫无意义，那么在join()方法的使用场景下，notify()或者notifyAll()方法是在哪儿被调用的呢？答案就是jvm
            b.join();
            //主线程 自己挂自己 ???
            //Thread.currentThread().join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 只有饭做好了，才能开始吃饭
        System.out.println(Thread.currentThread().getName() + " 开始吃饭饭");

    }
}
 
