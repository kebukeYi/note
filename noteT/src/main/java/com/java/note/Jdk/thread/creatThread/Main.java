package com.java.note.Jdk.thread.creatThread;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * @ClassName Main
 * @Author kebukeyi
 * @Date 2022/3/31 13:23
 * @Description  主线程麻溜的执行完 子线程再慢慢的执行
 * @Version 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        testPrint();
    }

    public static void testPrint() {
        Thread.currentThread().setName("Main-Thread-begin");
        new Executor() {
            @Override
            public void execute(Runnable r) {
                System.out.println("execute-begin");
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        System.out.println("newThread-begin");
                        Thread thread = new Thread(r, "QuizThread");
                        System.out.println("newThread-end");
                        return thread;
                    }
                }.newThread(r).start();
                System.out.println("execute-end");
            }
        }.execute(new Runnable() {
                      @Override
                      public void run() {
                          System.out.println("run-begin");
                          System.out.println("Hello");
                          System.out.println("run-end");
                      }
                  }
        );
        System.out.println("Main-Thread-end");
    }
}
