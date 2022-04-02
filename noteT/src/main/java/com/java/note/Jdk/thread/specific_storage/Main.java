package com.java.note.Jdk.thread.specific_storage;

/**
 * @ClassName Main
 * @Author kebukeyi
 * @Date 2022/4/2 14:28
 * @Description
 * @Version 1.0.0
 */
public class Main {


    public static void main(String[] args) throws InterruptedException {
        TSLog tsLog = new TSLog();
        Thread watcher = Log.startWatcher(tsLog);
        Thread.sleep(10 * 1000);
        //tsLog.setOk(true);
        watcher.interrupt();
        Thread.sleep(10 * 1000);

    }
}
