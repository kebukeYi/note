package com.java.note.Jdk.thread.synchronizedd;

import lombok.Data;

/**
 * @author : kebukeyi
 * @date :  2021-07-13 11:49
 * @description :
 * @question :
 * @usinglink :
 **/
@Data
public class Foo extends Thread {
    private String value;

    private Object object = null;


    public Foo(String value) {
        this.value = value;
    }

    public synchronized void printValue(String value) {
        while (true) {
            System.out.println(value);
        }
    }

    public void syncNull() {
        try {
            //null 肯定不能锁住的
            synchronized (object) {
                System.out.println(object);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        syncNull();
        printValue(value);
    }
}
 
