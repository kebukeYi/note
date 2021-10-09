package com.java.note.Jdk.thread.pool;

import java.util.concurrent.*;

import static com.java.note.Jdk.thread.pool.ThreadUtil.shutdownThreadPoolGracefully;

/**
 * @author : kebukeYi
 * @date :  2021-10-01 22:59
 * @description:  IO类型
 * @question:
 * @link:
 **/
public class IOThreadPoolUtil {

    //CPU 核数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //IO 处理线程数
    private static final int IO_MAX = Math.max(2, CPU_COUNT * 2);
    /**
     * 空闲保活时限，单位秒
     */
    private static final int KEEP_ALIVE_SECONDS = 30;
    /**
     * 有界队列 size
     */
    private static final int QUEUE_SIZE = 128;

    //使用懒汉式单例模式创建线程池，如果代码没有用到此线程池，也不会立即创建
    //懒汉式单例创建线程池：用于 IO 密集型任务
    private static class IoIntenseTargetThreadPoolLazyHolder {
        //线程池： 用于 IO 密集型任务
        private static final ThreadPoolExecutor EXECUTOR =
                new ThreadPoolExecutor(
                        //保持一致
                        IO_MAX, //CPU 核数*2
                        IO_MAX, //CPU 核数*2
                        KEEP_ALIVE_SECONDS,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue(QUEUE_SIZE),
                        new CustomThreadFactory("io"));

        static {
            //设置的 Idle 超时策略也将被应用于核心线程，当池中的线程长时间空闲时，可以自行销毁
            EXECUTOR.allowCoreThreadTimeOut(true);
            //JVM 关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(
                    new ShutdownHookThread("IO 密集型任务线程池",
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
            public CustomThreadFactory(String io) {
            }

            @Override
            public Thread newThread(Runnable r) {
                return null;
            }
        }
    }
// ...省略不相干代码
}

