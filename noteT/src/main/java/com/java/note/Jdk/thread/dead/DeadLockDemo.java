package com.java.note.Jdk.thread.dead;

import lombok.SneakyThrows;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/26  10:34
 * @Description 死锁样例
 */
class HoldLockThread extends Thread {

    private String lockA;
    private String lockB;

    public HoldLockThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @SneakyThrows
    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println("1: " + Thread.currentThread().getName() + "\t" + "自己持有 " + lockA + "  尝试获得 " + lockB);
            Thread.sleep(200);
            synchronized (lockB) {
                System.out.println("2: " + Thread.currentThread().getName() + "\t" + "自己持有 " + lockB + " 尝试获得 " + lockA);
            }
        }
    }
}

public class DeadLockDemo {

    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";

        new Thread(new HoldLockThread(lockA, lockB), "AAA").start();

        new Thread(new HoldLockThread(lockB, lockA), "BBB").start();

    }

}
