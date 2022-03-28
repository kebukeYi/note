package com.java.note.Jdk.thread.read_write_lock;

/**
 * @ClassName Main
 * @Author kebukeyi
 * @Date 2022/3/28 19:35
 * @Description 适合读取操作繁重时 、适合读取频率比写入频率高时
 * @Version 1.0.0
 */
public class Main {

    public static void main(String[] args) {
        Data data = new Data(10);
        new ReadThread(data, 1).start();
        new ReadThread(data, 2).start();
        new ReadThread(data, 3).start();
        new ReadThread(data, 4).start();
        new ReadThread(data, 5).start();
        new ReadThread(data, 6).start();
        System.out.println(data.getReadingReaders());
        System.out.println(data.readWriteLock.getWaitingReaders());
        new WriteThread(data, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1).start();
        System.out.println(data.readWriteLock.getWaitingReaders());
        new WriteThread(data, "abcdefghijklmnopqrstuvwxyz", 2).start();
        System.out.println(data.readWriteLock.getWaitingWriters());
    }
}
