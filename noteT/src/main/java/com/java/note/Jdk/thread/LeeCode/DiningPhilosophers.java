package com.java.note.Jdk.thread.LeeCode;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/20  12:04
 * @Description
 * 哲学家进餐 为的是防止死锁发生
 */
public class DiningPhilosophers {

    Semaphore semaphore = new Semaphore(4);
    ReentrantLock[] reentrantLocks = {new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()};

    public DiningPhilosophers() {

    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {

        semaphore.acquire();//进来一个人
        int leftForks = (philosopher + 1) % 5;//这个人的左边叉子编号
        int rightForks = philosopher;//右边的编号
        reentrantLocks[rightForks].lock();//锁叉
        reentrantLocks[leftForks].lock();//锁叉

        pickLeftFork.run();//干活
        pickRightFork.run();//干活
        eat.run();//吃饭
        putLeftFork.run();//放叉子
        putRightFork.run();//放叉子
        reentrantLocks[rightForks].unlock();//解锁
        reentrantLocks[leftForks].unlock();//解锁
        semaphore.release();//退场
    }
}
