package com.java.note.Jdk.thread.read_write_lock;

import java.util.Arrays;

/**
 * @ClassName ReadThread
 * @Author kebukeyi
 * @Date 2022/3/28 18:22
 * @Description
 * @Version 1.0.0
 */
public class ReadThread extends Thread {

    private final Data data;

    public ReadThread(Data data, int id) {
        super("ReadThread-" + id);
        this.data = data;
    }

    @Override
    public void run() {
        try {
            //'while' statement cannot complete without throwing an exception
            while (true) {
                char[] read = data.read();
                System.out.println(Thread.currentThread().getName() + "  " + Arrays.toString(read));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
