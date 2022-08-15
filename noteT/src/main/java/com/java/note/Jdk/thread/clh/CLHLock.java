package com.java.note.Jdk.thread.clh;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName CLHLock
 * @Author kebukeyi
 * @Date 2022/7/29 18:09
 * @Description
 * @Version 1.0.0
 */
public class CLHLock implements Lock {

    private AtomicReference<CLHNode> tail;

    private ThreadLocal<CLHNode> threadLocal;

    public CLHLock() {
        this.tail = new AtomicReference<>();
        this.threadLocal = new ThreadLocal<>();
    }

    @Override
    public void lock() {
        CLHNode curNode = threadLocal.get();
        if (curNode == null) {
            curNode = new CLHNode();
            threadLocal.set(curNode);
        }
        // 通过同步方法获取尾节点，并将当前节点置为尾节点，此时获取到的尾节点为当前节点的前驱节点
        CLHNode predNode = tail.getAndSet(curNode);
        // 如果 predNode 不为空 说明存在 前置节点
        if (predNode != null) {
            //申请加锁的线程通过前驱节点的变量值进行自旋
            while (predNode.getLocked()) {
                //当前线程来 等待前置节点 释放锁
                if (Thread.currentThread().isInterrupted()){

                }
            }
        }
    }

    @Override
    public void unlock() {
        CLHNode curNode = threadLocal.get();
        threadLocal.remove();
        // 获取当前线程的锁节点，如果节点为空或者锁值（locked== false）则无需解锁，直接返回
        if (curNode == null || curNode.getLocked() == false) {
            return;
        }

        // 使用同步方法为尾节点赋空值，赋值不成功则表示当前节点不是尾节点，需要将当前节点的 locked == false 已保证解锁该节点
        if (!tail.compareAndSet(curNode, null)) {
            // 每个节点在解锁时更新自己的锁值（locked），在这一时刻，该节点的后置节点会结束自旋，并进行加锁
            curNode.setLocked(false);
        } else {
            //如果当前节点为尾节点，则无需设置该节点的锁值。因为该节点没有后置节点，即使设置了，也没有实际意义
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        return false;
    }


    @NotNull
    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) {
        final Lock clhLock = new CLHLock();
        for (int i = 0; i < 10; i++) {
            new Thread(new DemoTask(clhLock, i + "")).start();
        }
    }

    class CLHNode {

        //每一个节点的内部变量，这个变量是跨线程获取的，为了保证locked属性线程间可见，该属性被volatile修饰
        //locked == true 表示节点处于加锁状态或者等待加锁状态
        //locked == false 表示节点处于解锁状态/
        private volatile boolean locked = true;


        public boolean getLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }
    }
}

