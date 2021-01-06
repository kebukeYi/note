

import lombok.SneakyThrows;

import java.time.Instant;
import java.util.concurrent.*;

public class Boot02ApplicationTests {

    //单线程计算累加
    public static void oneThread() {
        Instant start = Instant.now();
        long num = 0;
        for (long i = 0; i < 400000000; i++) {
            num++;
        }
        Instant end = Instant.now();
        System.out.println(num + "单线程耗时:" + (end.getNano() - start.getNano()));
    }

    //多线程Callable计算累加
    @SneakyThrows
    public static void doFuture() {
        Instant startThread = Instant.now();
        long num1 = 0;
        Callable<Long> add1 = () -> {
            long callableNum = 0;
            for (long i = 0; i < 200000000; i++) {
                callableNum++;
            }
            return callableNum;
        };
        Callable<Long> add2 = () -> {
            long callableNum = 0;
            for (long i = 0; i < 200000000; i++) {
                callableNum++;
            }
            return callableNum;
        };
        FutureTask<Long> integerFutureTask = new FutureTask<>(add1);
        new Thread(integerFutureTask).start();
        FutureTask<Long> integerFutureTask2 = new FutureTask<>(add2);
        new Thread(integerFutureTask2).start();

        Long integer = integerFutureTask.get();
        Long integer1 = integerFutureTask2.get();
        Instant endThread = Instant.now();
        System.out.println(integer + integer1 + "Future多线程耗时:" + (endThread.getNano() - startThread.getNano()));
    }

    //CompletableFuture 多线程计算累加
    @SneakyThrows
    public static void doCompletableFuture() {
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(2, 50));
        Instant startCompletableFuture = Instant.now();
        CompletableFuture<Long> longCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            long callableNum = 0;
            for (long i = 0; i < 200000000; i++) {
                callableNum++;
            }
            return callableNum;
        }, executorService);
        CompletableFuture<Long> longCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
            long callableNum = 0;
            for (long i = 0; i < 200000000; i++) {
                callableNum++;
            }
            return callableNum;
        }, executorService);
        Instant endCompletableFuture = Instant.now();
        CompletableFuture.allOf(longCompletableFuture1, longCompletableFuture2).join();
        System.out.println(longCompletableFuture1.get() + longCompletableFuture2.get() + "CompletableFuture多线程耗时:" + (endCompletableFuture.getNano() - startCompletableFuture.getNano()));
        executorService.shutdown();
    }

    public static void main(String[] args) {
        oneThread();
        doFuture();
        doCompletableFuture();
    }

}
