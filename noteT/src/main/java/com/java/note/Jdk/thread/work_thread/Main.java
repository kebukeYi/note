package com.java.note.Jdk.thread.work_thread;

/**
 * @ClassName Main
 * @Author kebukeyi
 * @Date 2022/3/31 14:10
 * @Description 如果自己的工作可以交给其他人，那么自己就可以执行下一个工作。其他人可以先启动线程池来循环往复的获取任务。
 * @Version 1.0.0
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Channel(5);
        channel.startWorkers();
        new ClientThread("bobo", channel).start();
        new ClientThread("kaka", channel).start();
        new ClientThread("haha", channel).start();
        new ClientThread("papa", channel).start();
        Thread.sleep(2000);
        //手动停止线程：设置抛出异常tH 然后线程捕获异常 自动退出线程
        channel.stopAllWorkers();
    }
}
