package com.plan.time.timer;

import java.util.Timer;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 16:04
 * @description:
 * @question:
 * @link:
 **/
public class MyTimer {


    public static void main(String[] args) {
        final Timer timer = new Timer(true);
        timer.schedule(new MyTimerTask(), 0, 10 * 1000);

        System.out.println("TimerTask started");
        //cancel after sometime
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();
        System.out.println("TimerTask cancelled");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
 
