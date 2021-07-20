package cn.gof.observer.eventBus;

import java.util.concurrent.Executor;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 16:00
 * @description:
 * @question:
 * @link:
 **/
public class AsyncEventBus extends EventBus {
    public AsyncEventBus(Executor executor) {
        super(executor);
    }
}
 
