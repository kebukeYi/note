package com.java.note.Jdk.thread.CompletableFuture;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Author : fang.com
 * @CreatTime : 2020-12-24 09:02
 * @Description :
 * @Version :  0.0.1
 */
public class CompletableFutureTest {
    /*
    如果任务是计算密集型的，并且没有I/O操作的话，那么推荐你选择Stream的并行流，实现简单并行效率也是最高的
    如果任务是有频繁的I/O或者网络连接等操作，那么推荐使用CompletableFuture，采用自定义线程池的方式，根据服务器的情况设置线程池的大小，尽可能的让CPU忙碌起来
     */

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        runWithSync();
        runWithFuture();
//        runWithParalleStream();
//        runWithCompletableFuture();
//        runWithCompletableFuture2();
        runWithCompletableFuture3();
        runWithCompletableFuture4();
    }

    public static void runWithSync() {
        long start = System.currentTimeMillis();
        List<com.java.note.Jdk.thread.completableFuture.RemoteLoader> remoteLoaders = Arrays.asList(new CustomerInfoService(),
                new com.java.note.Jdk.thread.CompletableFuture.LearnRecordService());
        List<String> collect = remoteLoaders.stream().map(com.java.note.Jdk.thread.completableFuture.RemoteLoader::load).collect(Collectors.toList());
        System.out.println(collect);
        long end = System.currentTimeMillis();
        System.out.println("总共花费时间:" + (end - start));
    }

    public static void runWithFuture() {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<com.java.note.Jdk.thread.completableFuture.RemoteLoader> remoteLoaders = Arrays.asList(new CustomerInfoService(), new com.java.note.Jdk.thread.CompletableFuture.LearnRecordService());
        List<Future<String>> futures = remoteLoaders.stream().map(remoteLoader -> executorService.submit(remoteLoader::load)).collect(Collectors.toList());
        List<String> list = futures.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        System.out.println(list);
        long end = System.currentTimeMillis();
        executorService.shutdown();
        System.out.println("总共花费时间:" + (end - start));
    }

    public static void runWithParalleStream() {
        long start = System.currentTimeMillis();
        List<com.java.note.Jdk.thread.completableFuture.RemoteLoader> remoteLoaders = Arrays.asList(new CustomerInfoService(), new com.java.note.Jdk.thread.CompletableFuture.LearnRecordService(), new com.java.note.Jdk.thread.CompletableFuture.WatchRecordService(), new com.java.note.Jdk.thread.CompletableFuture.LabelService(), new com.java.note.Jdk.thread.CompletableFuture.LabelService());
        List<String> collect = remoteLoaders.parallelStream().map(com.java.note.Jdk.thread.completableFuture.RemoteLoader::load).collect(Collectors.toList());
        System.out.println(collect);
        long end = System.currentTimeMillis();
        System.out.println("总共花费时间:" + (end - start));
    }

    private static void doSomething() {
        System.out.println("doSomething...");
        throw new RuntimeException();
    }

    //这种用法还有个问题，就是任务出现了异常，主线程会无感知，任务线程不会把异常给抛出来；
    //这会导致主线程会一直等待，通常我们也需要知道出现了什么异常，做出对应的响应；
    public static void runWithCompletableFuture() {
        CompletableFuture future = new CompletableFuture();
        new Thread(() -> {
            try {
                doSomething();
                future.complete("Finish");
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }).start();
        System.out.println(future.join());
    }

    //Java8不仅提供允许任务返回结果的supplyAsync，还提供了没有返回值的runAsync；
    //让我们可以更加的关注业务的开发，不需要处理异常错误的管理
    public static void runWithCompletableFuture2() throws ExecutionException, InterruptedException {
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> {
            doSomething();
            return "Finish";
        }).exceptionally(throwable -> "Throwable exception message : " + throwable.getMessage());
        System.out.println(completableFuture.get());
    }

    //依然是采用的两个Stream来完成的,并行流的结果差不多，消耗时间
    public static void runWithCompletableFuture3() {
        long start = System.currentTimeMillis();
        List<com.java.note.Jdk.thread.completableFuture.RemoteLoader> remoteLoaders = Arrays.asList(new CustomerInfoService(), new com.java.note.Jdk.thread.CompletableFuture.LearnRecordService(), new com.java.note.Jdk.thread.CompletableFuture.LabelService(), new com.java.note.Jdk.thread.CompletableFuture.OrderService(), new com.java.note.Jdk.thread.CompletableFuture.WatchRecordService());
        List<CompletableFuture<String>> completableFutures = remoteLoaders.stream().map(loader -> CompletableFuture.supplyAsync(loader::load)).collect(Collectors.toList());
//        List<CompletableFuture<String>> completableFutures2 = remoteLoaders.parallelStream().map(loader -> CompletableFuture.supplyAsync(loader::load)).collect(Collectors.toList());
        List<String> customerDetail = completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
//        List<String> customerDetail2 = completableFutures2.parallelStream().map(CompletableFuture::join).collect(Collectors.toList());
        System.out.println(customerDetail);
//        System.out.println(customerDetail2);
        long end = System.currentTimeMillis();
        System.out.println("总共花费时间:" + (end - start));
    }

    //深入了解下并行流和CompletableFuture的实现原理
    //自定义线程池，优化CompletableFuture
    //使用并行流无法自定义线程池，但是CompletableFuture可以
    public static void runWithCompletableFuture4() {
        long start = System.currentTimeMillis();
        List<com.java.note.Jdk.thread.completableFuture.RemoteLoader> remoteLoaders = Arrays.asList(new CustomerInfoService(), new com.java.note.Jdk.thread.CompletableFuture.LearnRecordService(), new com.java.note.Jdk.thread.CompletableFuture.LabelService(), new com.java.note.Jdk.thread.CompletableFuture.OrderService(), new com.java.note.Jdk.thread.CompletableFuture.WatchRecordService());

        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(remoteLoaders.size(), 50));

        List<CompletableFuture<String>> completableFutures = remoteLoaders.parallelStream().map(loader -> CompletableFuture.supplyAsync(loader::load, executorService)).collect(Collectors.toList());

        List<String> customerDetail = completableFutures.parallelStream().map(CompletableFuture::join).collect(Collectors.toList());

        System.out.println(customerDetail);
        executorService.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("总共花费时间:" + (end - start));
    }
}
