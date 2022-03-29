package com.java.note.Jdk.thread.read_write_lock;

/**
 * @ClassName ReadWriteLock
 * @Author kebukeyi
 * @Date 2022/3/28 18:22
 * @Description 简版读写锁 来自 《图解java多线程设计模式》
 * @Version 1.0.0
 */
public class ReadWriteLock {

    //正在读 线程数量
    private int readingReaders = 0;

    //等待读 线程数量
    private int waitingReaders = 0;

    //正在写 线程数量
    private int writingWriters = 0;

    //等待写 线程数量
    private int waitingWriters = 0;

    //是否写入优先
    //意义：读操作过后 写操作优先执行 ； 写操作过后 读操作优先执行；
    private boolean preferWriters = true;

    //只能一个线程通过 其余线程则加入等待队列中
    //读、写 线程都有可能 独自 唤醒访问此方法 因此需要进一步判断
    public synchronized void readLock() throws InterruptedException {
        //获得了锁 但是假如存在 正在写 或者 等待写 就得退出
        waitingReaders++;
        try {
            //是否存在正在写 || 等待写为优先级
            while (writingWriters > 0 || (preferWriters && waitingWriters > 0)) {
                //释放锁 等待队列中等待
                //一并加到 ReadWriteLock 实例的等待队列中
                wait();
            }
        } finally {
            //当从wait中醒过来时  并且重新获得锁时 执行 -- 操作
            waitingReaders--;
        }
        //正在读执行
        readingReaders++;
    }

    //读、写 线程都有可能 独自 唤醒访问此方法 因此需要进一步判断
    public synchronized void unReadLock() {
        readingReaders--;
        preferWriters = false;
        //唤醒所有：读 、写  线程重新争抢锁
        notifyAll();
    }

    //读、写 线程都有可能 独自 唤醒访问此方法 因此需要进一步判断
    public synchronized void writeLock() throws InterruptedException {
        waitingWriters++;
        try {
            while (readingReaders > 0 || writingWriters > 0) {
                //一并加到 ReadWriteLock 实例的等待队列中
                wait();
            }
        } finally {
            waitingWriters--;
        }
        writingWriters++;
    }

    //读、写 线程都有可能 独自 唤醒访问此方法 因此需要进一步判断
    public synchronized void unWriteLock() {
        writingWriters--;
        preferWriters = false;
        //唤醒所有：读 、写  线程重新争抢锁
        //因为不能指定线程唤醒
        notifyAll();
    }

    public int getReadingReaders() {
        return readingReaders;
    }

    public int getWaitingReaders() {
        return waitingReaders;
    }

    public int getWritingWriters() {
        return writingWriters;
    }

    public int getWaitingWriters() {
        return waitingWriters;
    }
}
