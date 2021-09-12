package com.plan.time.timer;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 16:06
 * @description:
 * @question:
 * @link:
 **/
public class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        System.out.println("Timer task started at:" + new Date());
        completeTask();
        System.out.println("Timer task finished at:" + new Date());

    }

    private void completeTask() {
        try {
            //assuming it takes 20 secs to complete the task
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
 
