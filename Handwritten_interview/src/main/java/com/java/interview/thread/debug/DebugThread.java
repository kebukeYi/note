package com.java.interview.thread.debug;

import java.math.BigInteger;

/**
 * @author : kebukeYi
 * @date :  2021-10-26 20:02
 * @description: 学习多线程调试
 * @question:
 * @link: https://www.bilibili.com/video/BV1Hf4y1t77J?from=search&seid=13766791624826663955&spm_id_from=333.337.0.0
 **/
public class DebugThread {

    public static void main(String[] args) {
        final FactorialCalculating calculating = new FactorialCalculating(100);
        final FactorialCalculating factorialCalculating = new FactorialCalculating(200);
        calculating.setName("Thread-1");
        factorialCalculating.setName("Thread-2");
        calculating.start();
        factorialCalculating.start();
        try {
            calculating.join();
            factorialCalculating.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final BigInteger bigInteger = calculating.getResult().add(factorialCalculating.getResult());
        System.out.println("两个线程相加结果：" + bigInteger);
    }

    private static class FactorialCalculating extends Thread {
        private BigInteger result = BigInteger.ONE;
        private long num;

        public FactorialCalculating(long num) {
            this.num = num;
        }

        @Override
        public void run() {
            //在此断点上 右键 设置 继承Thread : condition currentThread().getName().equals("Thread-1")
            // 或者  实现 Runnbale 接口 Thread.currentThread().getName().equals("Thread-1")
            System.out.println(Thread.currentThread().getName() + " 开始执行阶乘的计算：" + num);
            add(num);
            //或者 选择 SupThread 中的 Thread 选项
            System.out.println(Thread.currentThread().getName() + " 执行完成");
        }


        public void add(long num) {
            BigInteger sum = BigInteger.ONE;
            if (num < 0) {//判断传入数是否为负数
                throw new IllegalArgumentException("必须为正整数!");//抛出不合理参数异常
            }
            for (long i = 1; i <= num; i++) {//循环num
                sum = sum.multiply(new BigInteger(String.valueOf(i)));//每循环一次进行乘法运算
            }
            result = sum;
        }

        public BigInteger getResult() {
            return result;
        }
    }

}
 
