package com.java.note.Jdk.model.single.testSingle;

import com.java.note.Jdk.model.single.Singleton1;
import com.java.note.Jdk.model.single.Singleton2;
import com.java.note.Jdk.model.single.Singleton3;
import com.java.note.Jdk.model.single.Singleton4;

import java.util.concurrent.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  16:51
 * @Description
 */
public class TestSingle {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Singleton1 singleton1 = Singleton1.SINGLETON1;
        System.out.println(singleton1);

        Singleton2 singleton2 = Singleton2.SINGLETON2;
        System.out.println(singleton2);

        Singleton3 singleton3 = Singleton3.SINGLETON3;
        System.out.println(singleton3);

//        Singleton4 singleton4 = Singleton4.getInstance();
//        Singleton4 singleton4_2 = Singleton4.getInstance();
//        System.out.println(singleton4 == singleton4_2);

        Callable<Singleton4> callable = new Callable<Singleton4>() {
            @Override
            public Singleton4 call() throws Exception {
                return Singleton4.getInstance();
            }
        };

        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Singleton4> future = service.submit(callable);
        Future<Singleton4> future_1 = service.submit(callable);

        Singleton4 singleton4 = future.get();
        Singleton4 singleton4_1 = future_1.get();

        System.out.println(singleton4 == singleton4_1);

        System.out.println(singleton4);
        System.out.println(singleton4_1);
        service.shutdown();
    }

}
