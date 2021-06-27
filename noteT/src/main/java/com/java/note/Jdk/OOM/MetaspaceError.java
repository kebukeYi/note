package com.java.note.Jdk.OOM;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  16:19
 * @Description 元空间堆满
 * Java8及之后的版本使用 Metaspace来替代永久代。
 * Metaspace是法区在 Hotspot中的实现,它与持久代最大的区别在于: Metaspace并不在虚拟机内存中而是使用本地内存
 * 也即在Java8中 cLasse metadata( the virtual machines internal presentation of Java class),被存储在叫做
 * Metaspace Anative memory
 * 永久代(av8后被原空间 Metaspace取代了)存放了以下信息
 * 虚拟机加载的类信息
 * 静态变量
 * 即时编译后的代码
 * 模 etaspace空间溢出,我们不断生成类往元空间灌,类占据的空间总是会超过 dataspace指定的空间大小的
 */
public class MetaspaceError {

    static class OOMTest {
    }

    public static void main(String[] args) {
        int i = 0;
        try {
            while (true) {
                i++;
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOMTest.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(o, args);
                    }
                });
                enhancer.create();
            }
        } catch (Exception e) {
            System.out.println(i);
            e.printStackTrace();
        }
    }
}
