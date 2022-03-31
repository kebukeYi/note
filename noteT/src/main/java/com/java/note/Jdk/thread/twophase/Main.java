package com.java.note.Jdk.thread.twophase;

/**
 * @ClassName Main
 * @Author kebukeyi
 * @Date 2022/3/31 17:59
 * @Description 二阶段终止：一阶段：发出终止请求 ；二阶段：进行终止处理代码
 * 安全性、生存性、响应性
 * @Version 1.0.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        //CountUpThread countUpThread = new CountUpThread();
        HanoiThread countUpThread = new HanoiThread();
        countUpThread.start();
        Thread.sleep(10000);
        //终止请求
        countUpThread.shutDownRequest();
        //主线程等待 countUpThread 终止结束
        countUpThread.join();
    }

}
