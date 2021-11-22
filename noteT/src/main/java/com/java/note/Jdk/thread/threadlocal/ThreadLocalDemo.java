package com.java.note.Jdk.thread.threadlocal;

public class ThreadLocalDemo {

    //外部的 ThreadLocal_1  变量
    ThreadLocal<String> localVar_1 = new ThreadLocal<>();
    ThreadLocal<String> localVar_2 = new ThreadLocal<>();
    ThreadLocal<String> localVar_3 = new ThreadLocal<>();

    void print(String str) {
        //打印当前线程中本地内存中本地变量的值
        System.out.println(str + " :" + localVar_1.get());
        //清除本地内存中的本地变量
        localVar_1.remove();
    }

    public static void main(String[] args) throws InterruptedException {
        final ThreadLocalDemo threadLocalDemo = new ThreadLocalDemo();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //设置  当前线程1中的 ThreadLocalMap 的值 key: 外部 localVar_1  value :  自定义的值
                //方式二：
                threadLocalDemo.localVar_1.set("线程1 localVar1");
                threadLocalDemo.localVar_2.set("线程1 localVar2");
                threadLocalDemo.localVar_3.set("线程1 localVar3");

                //调用打印方法
                //打印当前线程中本地内存中本地变量的值
                System.out.println("thread1" + " :" + threadLocalDemo.localVar_1.get());
                //清除本地内存中的本地变量
                threadLocalDemo.localVar_1.remove();
                //打印本地变量
                System.out.println("after remove_1 : " + threadLocalDemo.localVar_1.get());
                System.out.println("after remove : " + threadLocalDemo.localVar_2.get());
                System.out.println("after remove : " + threadLocalDemo.localVar_3.get());
            }
        });

        //方式一：
        //t1.getThreadLocalMap().put(localVar_1, "localVar1");
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程1中本地变量的值
                threadLocalDemo.localVar_1.set("线程2 localVar1");
                threadLocalDemo.localVar_2.set("线程2 localVar2");
                threadLocalDemo.localVar_3.set("线程2 localVar3");
                //调用打印方法
                //打印当前线程中本地内存中本地变量的值
                System.out.println("thread2" + " :" + threadLocalDemo.localVar_1.get());
                //清除本地内存中的本地变量
                threadLocalDemo.localVar_1.remove();
                //打印本地变量
                System.out.println("after remove_1 : " + threadLocalDemo.localVar_1.get());
                System.out.println("after remove : " + threadLocalDemo.localVar_2.get());
                System.out.println("after remove : " + threadLocalDemo.localVar_3.get());
            }
        });
        //再次设值
        threadLocalDemo.localVar_1.set("线程1 localVar1_1");
        t1.start();
        Thread.sleep(2000);
        //覆盖操作
        threadLocalDemo.localVar_1.set("线程1 localVar1_2");
        //localVar_1 = null;
        t2.start();
        System.out.println("main : " + threadLocalDemo.localVar_1.get());
    }
}
