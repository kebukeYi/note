package com.java.note.Jdk.queue;

import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/17  上午 10:09
 * @Description
 */
public class MiNiArrayBlockingQueue implements BlockingQueue {

    //线程并发控制
    private Lock lock = new ReentrantLock();

    /**
     * 当生产者线程生产数据时，它会先检查当前queues是否已经满了，如果已经满了，需要将当前生产者线程 调用 notFull.await()
     * 进入到notFull条件队列挂起。等待消费者线程消费数据时唤醒。
     */
    private Condition notFull = lock.newCondition();

    /**
     * 当消费者线程消费数据时，它会先检查当前queues中是否有数据，如果没有数据,需要将当前消费者线程 调用 notEmpty.await()
     * 进入到notEmpty条件队列挂起。等待生产者线程生产数据时唤醒。
     */
    private Condition notEmpty = lock.newCondition();

    private Object[] queues;

    private int size;


    public MiNiArrayBlockingQueue(int size) {
        this.size = size;
        this.queues = new Object[size];
    }

    /**
     * count:当前队列中可以被消费的数据量
     * putptr:记录生产者存放数据的下一次位置。每个生产者生产完一个数据后，会将 putptr ++
     * takeptr:记录消费者消费数据的下一次的位置。每个消费者消费完一个数据后，将将takeptr ++
     */
    private int count, putstr, takeptr;

    @SneakyThrows
    @Override
    public boolean add(Object element) {
        lock.lock();
        try {
            //第一件事？ 判断一下当前queues是否已经满了...
            // 原理释放了锁，也是把线程封装成一个node节点，存在一个队列中；
            if (count == this.size) notFull.await();
            //执行到这里，说明队列未满，可以向队列中存放数据了..
            this.queues[putstr] = element;
            putstr++;
            //一边用 一边放 都是可以的
            if (putstr == this.size) putstr = 0;
            count++;

            //当向队列中成功放入一个元素之后，需要做什么呢？
            //需要给 notEmpty 一个唤醒信号
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
        return false;
    }


    @Override
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            //第一件事？判断一下当前队列是否有数据可以被消费...
            if (count == 0) {
                notEmpty.await();
            }
            //执行到这里，说明队列有数据可以被消费了..
            Object element = this.queues[takeptr];
            takeptr++;

            if (takeptr == this.size) takeptr = 0;
            //生产数据 自减count
            count--;
            //当向队列中成功消费一个元素之后，需要做什么呢？
            //需要给notFull一个唤醒信号
            notFull.signal();

            return element;
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {

        MiNiArrayBlockingQueue queue = new MiNiArrayBlockingQueue(10);

        Thread producer = new Thread(() -> {
            int i = 0;
            while (true) {
                i++;
                if (i == 10) i = 0;
                try {
                    System.out.println("生产数据：" + i);
                    queue.add(Integer.valueOf(i));
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producer.start();


        Thread cousumer = new Thread(() -> {
            while (true) {
                try {
                    Integer result = (Integer) queue.take();
                    System.out.println("消费者消费：" + result);
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        cousumer.start();
    }


    @Override
    public boolean offer(Object o) {
        return false;
    }

    @Override
    public Object remove() {
        return null;
    }

    @Override
    public Object poll() {
        return null;
    }

    @Override
    public Object element() {
        return null;
    }

    @Override
    public Object peek() {
        return null;
    }

    @Override
    public void put(Object o) throws InterruptedException {

    }

    @Override
    public boolean offer(Object o, long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }


    @Override
    public Object poll(long l, TimeUnit timeUnit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean retainAll(Collection collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection collection) {
        return false;
    }

    @Override
    public boolean containsAll(Collection collection) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] objects) {
        return new Object[0];
    }

    @Override
    public int drainTo(Collection collection) {
        return 0;
    }

    @Override
    public int drainTo(Collection collection, int i) {
        return 0;
    }
}
