package com.java.note.Jdk.thread.synchronizedd;

/**
 * @author : kebukeyi
 * @date :  2021-07-13 11:49
 * @description :
 * @question :
 * @usinglink :
 **/
public class Foo extends Thread {

    private String value;

    private final Object object = new Object();


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
 
