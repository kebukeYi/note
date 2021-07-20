package cn.gof.observer.eventBus;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author : kebukeyi
 * @date :  2021-07-20 15:55
 * @description: ObserverAction 类用来表示 @Subscribe 注解的方法，它主要用在 ObserverRegistry 观察者注册表中
 * @question:
 * @link:
 **/
@Data
@NoArgsConstructor
public class ObserverAction {

    //arget 表示观察者类
    Object target;
    //method 表示目标方法
    Method method;

    public ObserverAction(Object target, Method method) {
        this.target = Preconditions.checkNotNull(target);
        this.method = method;
        this.method.setAccessible(true);
    }

    // event是method方法的参数
    public Object execute(Object object) {
        try {
            final Object invoke = method.invoke(target, object);
            return invoke;
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
 
