package com.java.interview.thread.consumer_provider;

/**
 * @author : kebukeYi
 * @date :  2021-10-23 10:46
 * @description:
 * @question:
 * @link:
 **/
public class Print {


    public static void tcfo(String message) {
        System.out.println(message);
    }


    public static void cfo(String message) {
        System.out.println(message);
    }

    public static void sleep(int mils) {
        try {
            cfo("睡眠 " + mils);
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
 
