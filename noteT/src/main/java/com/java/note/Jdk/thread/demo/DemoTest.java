package com.java.note.Jdk.thread.demo;

import org.apache.logging.log4j.util.ProcessIdUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoTest {

    private static int x = 0;
    private static volatile int y = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private final static int count = 100;
    private static Object lock = new Object();


    public static void test01() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                // 线程休眠 统一休眠 加大之后的并发+1操作
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //同步执行
//                synchronized (lock) {
//                    x++;
//                }
                //
                y++;
                //这个是保证原子性吗？
                cdl.countDown();
                //这个是原子性
                atomicInteger.incrementAndGet();
            }).start();
        }//for结束

        //主线程进行阻塞
        cdl.await();
        System.out.println("x " + x);
        System.out.println("y " + y);
        System.out.println("atomicInteger " + atomicInteger.get());
    }

    public static void main(String[] args) throws InterruptedException {
        //test01();
        testSleepThreadCPU();
    }

    //测试 sleep 占不占用 cpu
    public static void testSleepThreadCPU() {
        //获取进程id  其实  jps  也是可以的
        final String processId = ProcessIdUtil.getProcessId();
        System.out.println("进程id:" + processId);
        try {
            //main 线程 无限制等待
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //通过 jstack pid 获取对应的 main 线程状态
        //"main" #1 prio=5 os_prio=0 tid=0x0000000002ede800 nid=0x55d4 waiting on condition [0x0000000002e0f000]
        //   java.lang.Thread.State: TIMED_WAITING (sleeping)
        //        at java.lang.Thread.sleep(Native Method)
        //        at com.java.note.Jdk.thread.demo.DemoTest.testSleepThreadCPU(DemoTest.java:59)
        //        at com.java.note.Jdk.thread.demo.DemoTest.main(DemoTest.java:50)
    }
}

class ThreadDemo extends Thread {
    @Override
    public void run() {
        super.run();
    }
}

