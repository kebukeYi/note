package cn.gof.observer.eventBus;

import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.Executor;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 16:00
 * @description:
 * @question:
 * @link:
 **/
public class EventBus {

    private Executor executor;
    private ObserverRegistry registry = new ObserverRegistry();

    public EventBus() {
        this(MoreExecutors.directExecutor());
    }

    protected EventBus(Executor executor) {
        this.executor = executor;
    }

    //注册口子  起码有个获取相关注解方法的口子
    public void register(Object object) {
        registry.register(object);
//        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz, providerClassLoader);
//        for (final T provider : serviceLoader) {
//            collection.add(provider);
//        }
    }

    public void post(Object event) {
        List<ObserverAction> observerActions = registry.getMatchedObserverActions(event);
        for (ObserverAction observerAction : observerActions) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    observerAction.execute(event);
                }
            });
        }
    }
}
 
