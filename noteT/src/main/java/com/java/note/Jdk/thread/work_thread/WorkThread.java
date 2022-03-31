package com.java.note.Jdk.thread.work_thread;

/**
 * @ClassName WorkThread
 * @Author kebukeyi
 * @Date 2022/3/31 14:10
 * @Description
 * @Version 1.0.0
 */
public class WorkThread extends Thread {

    private Channel channel;
    private String name;
    private volatile boolean terminated = false;

    public WorkThread(String name, Channel channel) {
        super(name);
        this.name = name;
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            while (!terminated) {
                Request request = channel.getRequest();
                request.execute();
            }
        } catch (InterruptedException e) {
            terminated = true;
        } finally {
            System.out.println(Thread.currentThread().getName() + " is interrupted " + this);
        }
    }

    public void stopThread() {
        terminated = true;
        interrupt();
    }
}
