package cn.gof.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 15:44
 * @description:
 * @question:
 * @link:
 **/
public class CacheProxy implements InvocationHandler,CacheService {

    Object target;

    public CacheProxy(Object target) {
        this.target = target;
    }

    public static Object getProxyObject(Object clazz) {
        return Proxy.newProxyInstance(clazz.getClass().getClassLoader(), clazz.getClass().getInterfaces(), new CacheProxy(clazz));
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before method " + method.getName());
        final Integer id = (Integer) args[0];
        if (id.equals(1)) {
            return "cache";
        }
        final Object invoke = method.invoke(target, args);
        return invoke;
    }

    @Override
    public String getUser(int id, int age) {
        return null;
    }
}
 
