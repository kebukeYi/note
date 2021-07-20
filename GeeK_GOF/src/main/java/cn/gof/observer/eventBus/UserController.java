package cn.gof.observer.eventBus;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 16:01
 * @description:
 * @question:
 * @link:
 **/

public class UserController {

    private UserService userService; // 依赖注入

    private EventBus eventBus;
    private static final int DEFAULT_EVENTBUS_THREAD_POOL_SIZE = 20;

    public UserController() {
        //eventBus = new EventBus(); // 同步阻塞模式
        eventBus = new AsyncEventBus(Executors.newFixedThreadPool(DEFAULT_EVENTBUS_THREAD_POOL_SIZE)); // 异步非阻塞模式
    }

    //所有的 订阅者 或者 观察者 注册一下自己的类 或者 函数
    public void setRegObservers(List<Object> observers) {
        for (Object observer : observers) {
            eventBus.register(observer);
        }
    }

    public Long register(String telephone, String password) {
        //省略输入参数的校验代码
        //省略userService.register()异常的try-catch代码
        long userId = userService.register(telephone, password);

        //主动通知
        eventBus.post(userId);

        return userId;
    }
}

//礼金赠送
class RegPromotionObserver {
    private PromotionService promotionService; // 依赖注入

    @Subscribe
    public void handleRegSuccess(Long userId) {
        promotionService.issueNewUserExperienceCash(userId);
    }
}

//注册通知
class RegNotificationObserver {
    private NotificationService notificationService;

    @Subscribe
    public void handleRegSuccess(Long userId) {
        notificationService.sendInboxMessage(userId, "...");
    }
}
 
