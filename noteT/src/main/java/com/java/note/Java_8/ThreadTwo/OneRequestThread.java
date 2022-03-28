package ThreadTwo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class OneRequestThread  extends Thread{

    private static final ShareData shareData = ShareData.getInstantce();
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String id;
    private CountDownLatch latch;

    public OneRequestThread(String id, CountDownLatch latch) {
        super();
        this.id = id;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println(id + " is  waitting");
            latch.await(); // 一直阻塞当前线程，直到计时器的值为0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doPost();// 发送post 请求
    }

    private void doPost() {
        System.out.println("模拟用户： " + id + " 模拟请求开始  at " + sdf.format(new Date()));
        shareData.addX();//操作
        System.out.println("模拟用户： " + id + " 模拟请求结束  at " + sdf.format(new Date()));
    }





}
