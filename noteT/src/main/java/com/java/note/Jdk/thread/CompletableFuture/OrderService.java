package com.java.note.Jdk.thread.CompletableFuture;


/**
 * @Author : fang.com
 * @CreatTime : 2020-12-24 10:13
 * @Description :
 * @Version :  0.0.1
 */
public class OrderService implements RemoteLoader {
    @Override
    public String load() {
        this.delay();
        return "订单服务";
    }
}
