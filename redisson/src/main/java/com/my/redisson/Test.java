package com.my.redisson;

/**
 * @ClassName Test
 * @Author kebukeyi
 * @Date 2022/4/6 16:40
 * @Description 已在单核CPU的阿里云服务器上测试(/ usr / javaSrc / simpleCpuAdd10K)，30次结果都为正确结果；
 * 当在本地多核CPU测试时，结果不一致；
 * @Version 1.0.0
 */
public class Test {

    private static long count = 0;

    private void add10K() {
        int idx = 0;
        while (idx++ < 100) {
            count += 1;
        }
    }

    public static long calc() throws InterruptedException {
        final Test test = new Test();
        // 创建两个线程，执行add()操作
        Thread th1 = new Thread(() -> {
            test.add10K();
        });
        Thread th2 = new Thread(() -> {
            test.add10K();
        });
        // 启动两个线程
        th1.start();
        th2.start();
        // 等待两个线程执行结束
        th1.join();
        th2.join();
        return count;
    }

    public static void main(String[] args) {
        try {
            long calc = calc();
            System.out.println(calc);
            //main 中调用 main() 怎么样
            //main(null);
            //System.out.println("dff");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
