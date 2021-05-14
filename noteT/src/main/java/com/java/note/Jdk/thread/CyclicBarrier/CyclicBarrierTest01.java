package com.java.note.Jdk.thread.CyclicBarrier;

import java.util.Random;
import java.util.concurrent.*;

/**
 *
 */
public class CyclicBarrierTest01 {

    /**
     * 案例：
     * 模拟过气游戏 “王者荣耀” 游戏开始逻辑
     */
    public static void main(String[] args) throws InterruptedException {
        final int num = 5;

        //第一步：定义玩家，定义5个
        String[] heros = {"安琪拉", "亚瑟", "马超", "张飞", "刘备"};

        //第二步：创建固定线程数量的线程池，线程数量为5
        ExecutorService service = Executors.newFixedThreadPool(num);

        //第三步：创建 barrier，parties 设置为5
        // 这个数字 5  很重要
        CyclicBarrier barrier = new CyclicBarrier(num, () -> {
            System.out.println("开始战斗!!!");
        });

        CountDownLatch countDownLatch = new CountDownLatch(num);

        //第四步：通过for循环开启5任务，模拟开始游戏，传递给每个任务 英雄名称和barrier
        for (int i = 0; i < num; i++) {
            service.execute(new Player(heros[i], barrier, countDownLatch));
        }
//        countDownLatch.await();
//        System.out.println("发现所有英雄加载完成，开始战斗吧！");
        service.shutdown();
    }


    static class Player implements Runnable {
        private String hero;
        private CyclicBarrier barrier;
        private CountDownLatch countDownLatch;

        public Player(String hero, CyclicBarrier barrier, CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
            this.hero = hero;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                //每个玩家加载进度不一样，这里使用随机数来模拟！
                System.out.println(hero + "：加载中...");
                TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                System.out.println(hero + "：加载进度100%，等待其他玩家加载完成中...");
                //
                barrier.await();
                // countDownLatch.countDown();
                System.out.println(hero + "：发现所有英雄加载完成，开始战斗吧！");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
