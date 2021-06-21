package com.java.note.Jdk.thread.threadgroup;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 19:46
 * @description :
 * @question :
 * @usinglink :
 **/
public class ThreadGroupDemo {

    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void main(Strings[] args) {
        //Returns the thread group to which this thread belongs.
        // This method returns null if this thread has died
        // java.lang.ThreadGroup[name=main,maxpri=10]
        System.out.println(Thread.currentThread().getThreadGroup());

        // java.lang.ThreadGroup[name=main,maxpri=10]
        executorService.scheduleWithFixedDelay(() -> {
            System.out.println(Thread.currentThread().getThreadGroup());
        }, 0, 3, TimeUnit.SECONDS);

    }
}
 
