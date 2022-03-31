package com.java.note.Jdk.thread.work_thread;

import java.util.Random;

/**
 * @ClassName Request
 * @Author kebukeyi
 * @Date 2022/3/31 14:10
 * @Description
 * @Version 1.0.0
 */
public class Request {

    private String name;
    private int num;
    private static Random random = new Random();

    public Request(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public void execute() {
        try {
            System.out.println(Thread.currentThread().getName() + " executes " + this);
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " is interrupted " + this);
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", num=" + num +
                '}';
    }
}
