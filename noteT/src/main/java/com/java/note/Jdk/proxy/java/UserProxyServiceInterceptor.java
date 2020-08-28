package com.java.note.Jdk.proxy.java;

import com.java.note.redis.bean.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/11  15:21
 * @Description
 */
public class UserProxyServiceInterceptor implements InvocationHandler {

    private Object realObj;

    public Object getRealObj() {
        return realObj;
    }

    public void setRealObj(Object realObj) {
        this.realObj = realObj;
    }

    public UserProxyServiceInterceptor(Object realObj) {
        super();
        this.realObj = realObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args != null && args.length > 0 && args[0] instanceof User) {
            User user = (User) args[0];
            if (user.getName().trim().length() <= 1) {
                throw new RuntimeException("用户输入长度需要大于一");
            }
        }
        //调用反射  ，开始调用 Service 方法
        Object ret = method.invoke(realObj, args);
        return ret;
    }
}
