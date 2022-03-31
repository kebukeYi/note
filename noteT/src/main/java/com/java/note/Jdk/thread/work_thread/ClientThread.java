package com.java.note.Jdk.thread.work_thread;

import java.util.Random;

/**
 * @ClassName ClientThread
 * @Author kebukeyi
 * @Date 2022/3/31 14:10
 * @Description
 * @Version 1.0.0
 */
public class ClientThread extends Thread {

    private Channel channel;
    private Random random = new Random();
    private Integer index = 0;

    public ClientThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                channel.putRequest(new Request(getName(), index++));
                Thread.sleep(random.nextInt(3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
