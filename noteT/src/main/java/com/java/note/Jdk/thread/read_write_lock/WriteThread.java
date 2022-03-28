package com.java.note.Jdk.thread.read_write_lock;

import java.util.Random;

/**
 * @ClassName WriteThread
 * @Author kebukeyi
 * @Date 2022/3/28 18:21
 * @Description
 * @Version 1.0.0
 */
public class WriteThread extends Thread {

    private static final Random random = new Random();
    private final Data data;
    private final String fillChars;
    private int index = 0;

    public WriteThread(Data data, String fillChars, int id) {
        super("WriteThread-" + id);
        this.data = data;
        this.fillChars = fillChars;
    }

    @Override
    public void run() {
        try {
            while (true) {
                char nextChar = nextChar();
                data.write(nextChar);
                Thread.sleep(random.nextInt(3000));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public char nextChar() {
        char charAt = fillChars.charAt(index);
        index++;
        if (index >= fillChars.length()) {
            index = 0;
        }
        return charAt;
    }
}
