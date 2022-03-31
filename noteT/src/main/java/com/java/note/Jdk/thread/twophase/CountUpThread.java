package com.java.note.Jdk.thread.twophase;

/**
 * @ClassName CountUpThread
 * @Author kebukeyi
 * @Date 2022/3/31 17:59
 * @Description
 * @Version 1.0.0
 */
public class CountUpThread extends Thread {

    private long count = 0;
    private volatile boolean shutDownRequest = false;

    public void shutDownRequest() {
        shutDownRequest = true;
        interrupt();
    }

    public boolean isShutDownRequest() {
        return shutDownRequest;
    }

    @Override
    public void run() {
        try {
            while (!isShutDownRequest()) {
                doWork();
            }
        } catch (InterruptedException e) {
        } finally {
            doShutDown();
        }
    }

    private void doWork() throws InterruptedException {
        count++;
        System.out.println("doWork：counter：" + count);
        //会抛出异常 也就间接的完成通信
        Thread.sleep(500);
    }

    public void doShutDown() {
        System.out.println("doShutDown：counter：" + count);
    }
}
