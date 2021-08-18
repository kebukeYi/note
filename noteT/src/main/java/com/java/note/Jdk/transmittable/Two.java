package com.java.note.Jdk.transmittable;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import lombok.val;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * @author : kebukeYi
 * @date :  2021-08-16 21:19
 * @description:
 * @question:
 * @link:
 **/
public class Two {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();
        val executorService = Executors.newCachedThreadPool();
        // =====================================================

        // 在父线程中设置
        context.set("value-set-in-parent");

        Runnable task = new Runnable() {
            @Override
            public void run() {
                final String s = context.get();
                System.out.println(s);
            }
        };
        // 额外的处理，生成修饰了的对象ttlRunnable
        Runnable ttlRunnable = TtlRunnable.get(task);
        System.out.println(executorService.submit(ttlRunnable).get());

        // =====================================================

        // Task中可以读取，值是"value-set-in-parent"
        String value = context.get();
        System.out.println(value);

        executorService.shutdown();
    }
}
 
