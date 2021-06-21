package com.java.note.Jdk.thread.Semaphore;

import java.util.concurrent.Semaphore;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/21  9:24
 * @Description 伸缩性
 * 根据Semaphore的特点，还可以用它来做简易版的限流器。当某一时刻系统的并发量较大的时候，可以简单的使用Semaphore来实现流量控制，
 * 只有从Semaphore中获取到许可证的连接，才让它继续访问系统，否则返回系统繁忙等提示。当然了，Semaphore的性能当然满足不了双十一这种高并发的场景，
 * 关于高性能的限流器，市面上有更好的解决方法，那就是Guava RateLimiter
 *
 * 链接：https://juejin.cn/post/6844903992363843592
 */
public class SemaphoreDemo {

    public static void main(Strings[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i <= 5; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "  抢到车位");
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "   离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }, Strings.valueOf(i)).start();
        }

    }
}
