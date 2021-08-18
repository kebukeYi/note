package com.java.note.Jdk.transmittable;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author : kebukeYi
 * @date :  2021-08-16 21:14
 * @description: 父线程给子线程传递值
 * @question:
 * @link:
 **/
public class One {


    public static void main(String[] args) {
        final TransmittableThreadLocal context = new TransmittableThreadLocal();
        // 在父线程中设置
        context.set("value-set-in-parent");

        // =====================================================

        // 在子线程中可以读取，值是"value-set-in-parent"
        new Thread(() -> {
            String value = (String) context.get();
            System.out.println(value);
        }).start();
    }


}
 
