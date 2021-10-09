package com.java.note.Jdk.thread.pool;

import java.util.concurrent.*;

import static com.java.note.Jdk.thread.pool.ThreadUtil.shutdownThreadPoolGracefully;

/**
 * @author : kebukeYi
 * @date :  2021-10-01 23:25
 * @description: 计算密集型
 * @question:
 * @link:
 **/
public class ComputThreadPoolUtil {
    //CPU 核数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT;

    /**
     * 空闲保活时限，单位秒
     */
    private static final int KEEP_ALIVE_SECONDS = 30;

    /**
     * 有界队列 size
     */
    private static final int QUEUE_SIZE = 128;

    //懒汉式单例创建线程池：用于 CPU 密集型任务
    private static class CpuIntenseTargetThreadPoolLazyHolder {
        //线程池： 用于 CPU 密集型任务
        private static final ThreadPoolExecutor EXECUTOR =
                new ThreadPoolExecutor(
                        MAXIMUM_POOL_SIZE,
                        MAXIMUM_POOL_SIZE,
                        KEEP_ALIVE_SECONDS,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue(QUEUE_SIZE),
                        new CustomThreadFactory("cpu"));

        static {
            EXECUTOR.allowCoreThreadTimeOut(true);
            //JVM 关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(
                    new ShutdownHookThread("CPU 密集型任务线程池",
                            new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    //优雅关闭线程池
                                    shutdownThreadPoolGracefully(EXECUTOR);
                                    return null;
                                }
                            }));
        }

        private static class CustomThreadFactory implements ThreadFactory {
            public CustomThreadFactory(String cpu) {
            }

            @Override
            public Thread newThread(Runnable r) {
                return null;
            }
        }
    }
    //...省略不相干代码

}
 
