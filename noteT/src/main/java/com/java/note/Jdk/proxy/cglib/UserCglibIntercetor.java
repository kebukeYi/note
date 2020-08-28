package com.java.note.Jdk.proxy.cglib;

import com.java.note.redis.bean.User;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/11  15:51
 * @Description
 */
public class UserCglibIntercetor implements MethodInterceptor {

    @Override
    public Object intercept(Object realObj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (args != null && args.length > 0 && args[0] instanceof User) {
            User user = (User) args[0];
            if (user.getName().trim().length() <= 1) {
                throw new RuntimeException("用户输入长度需要大于一");
            }
        }
        //调用 父类方法，开始调用 Service 方法
//        Object ret = method.invoke(realObj, args);
        Object ret = methodProxy.invokeSuper(realObj, args);
        return ret;
    }
}
