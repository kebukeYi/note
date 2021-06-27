package com.java.note.Jdk.model.agent.dong;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author : fang.com
 * @CreatTime : 2020-11-23 10:09
 * @Description :  动态代理： 代理工厂，给多个目标对象生成代理对象！
 * @Version :  0.0.1
 */
public class ProxyFactory {

    // 接收一个目标对象
    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    // 返回对目标对象(target)被代理后的对象(proxy)
    public Object getProxyInstance() {
        Object proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(), // 目标对象使用的类加载器
                target.getClass().getInterfaces(),   // 目标对象实现的所有接口
                new InvocationHandler() {  // 执行代理对象方法时候触发
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("开始动态代理");
                        // 获取当前执行的方法的方法名
                        String methodName = method.getName();
                        System.out.println(methodName);
                        System.out.println(proxy.getClass());
                        System.out.println(args[0]);
                        // 方法返回值
                        Object result = null;
                        if ("find".equals(methodName)) {
                            // 直接调用目标对象方法
                            result = method.invoke(target, args);
                        } else {
                            System.out.println("开启事务...");
                            // 执行目标对象方法
                            result = method.invoke(target, args);
                            System.out.println("提交事务...");
                        }
                        System.out.println("结束动态代理 " + result);
                        return result;
                    }
                });
        return proxy;
    }

    public static void main(String[] args) {
        IUserDao target = new UserDao();
        MyUserDao myUserDao = new MyUserDao();
        System.out.println("目标对象 " + target.getClass());
        new ProxyFactory(target).getProxyInstance();
        IUserDao proxy = (IUserDao) new ProxyFactory(target).getProxyInstance();
        System.out.println("代理对象 " + proxy.getClass());
        System.out.println(target == myUserDao);
        //执行代理对象方法
        proxy.save("Tony");
        proxy.find(2);

        IUserDao proxyInstance = (IUserDao) new ProxyFactory(myUserDao).getProxyInstance();
        proxyInstance.find(3);
        proxyInstance.save("Mike");
    }
}
