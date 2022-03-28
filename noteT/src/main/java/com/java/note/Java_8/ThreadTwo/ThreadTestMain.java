package ThreadTwo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class ThreadTestMain {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            OneRequestThread threadLog = new OneRequestThread("thread : " + i, latch);
            threadLog.start();
        }
        // 计数器減一 所有线程释放 并发访问。
        latch.countDown();
        System.out.println("所有模拟请求结束  at :  " + sdf.format(new Date()));
    }
}
