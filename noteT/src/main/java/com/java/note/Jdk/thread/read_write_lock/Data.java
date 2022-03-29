package com.java.note.Jdk.thread.read_write_lock;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName Data
 * @Author kebukeyi
 * @Date 2022/3/28 18:19
 * @Description 简版读写锁样例
 * @Version 1.0.0
 */
public class Data {

    private final char[] buffer;
    //自定义实现
    public final ReadWriteLock readWriteLock = new ReadWriteLock();
    //原生实现
    private final java.util.concurrent.locks.ReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
    Lock readLock = reentrantReadWriteLock.readLock();
    Lock writeLock = reentrantReadWriteLock.writeLock();

    public Data(int cap) {
        this.buffer = new char[cap];
        Arrays.fill(buffer, '*');
    }

    public char[] read() throws InterruptedException {
        readWriteLock.readLock();
        //readLock.lock();
        try {
            return doRead();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //readLock.unlock();
            readWriteLock.unReadLock();
        }
        return null;
    }

    private char[] doRead() {
        char[] chars = new char[buffer.length];
        //System.arraycopy(buffer, 0, chars, 0, chars.length);
        for (int i = 0; i < buffer.length; i++) {
            chars[i] = buffer[i];
        }
        slowly();
        return chars;
    }

    public void write(char c) throws InterruptedException {
        readWriteLock.writeLock();
        //writeLock.lock();
        try {
            doWrite(c);
            System.out.println(Thread.currentThread().getName() + "  " + c);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //writeLock.unlock();
            readWriteLock.unWriteLock();
        }
    }

    private void doWrite(char c) {
        //Arrays.fill(buffer, c);
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = c;
            slowly();
        }
    }

    public void slowly() {
        try {
            //睡眠时间不同 效果不同
            //Thread.sleep(50);
            Thread.sleep(100);
            //Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getReadingReaders() {
        return readWriteLock.getReadingReaders();
    }



}
