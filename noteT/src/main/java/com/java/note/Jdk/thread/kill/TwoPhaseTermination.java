package com.java.note.Jdk.thread.kill;


public class TwoPhaseTermination {

    //二阶段退出
    public static void v1() {
        Thread thread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            while (true) {
                if (currentThread.isInterrupted()) {
                    System.out.println("料理后事");
                    break;
                }
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    currentThread.interrupt();
                }
            }
            System.out.println("后事之外的外事");
        });

        thread.start();

        thread.interrupt();
    }

    private static volatile boolean stop = false;

    public static void v2() {
        Thread thread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            while (true) {
                if (stop) {
                    System.out.println("料理后事");
                    break;
                }
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    currentThread.interrupt();
                }
            }
            System.out.println("后事之外的外事");
        });
        thread.start();
        stop = true;
    }

    //中断退出
    public static void v3() {
        Thread thread = new Thread(() -> {
            try {
                Thread currentThread = Thread.currentThread();
                for (int i = 0; i < 1000; i++) {
                    if (currentThread.isInterrupted()) {
                        System.out.println("料理后事");
                        throw new InterruptedException();
                    }
                }
                System.out.println("后事之外的外事");
            } catch (Exception e) {
                System.out.println("over");
            }
        });
        thread.start();
        thread.interrupt();
    }

    public static void main(Strings[] args) {
//        v1();
//        v2();
        v3();

    }
}
