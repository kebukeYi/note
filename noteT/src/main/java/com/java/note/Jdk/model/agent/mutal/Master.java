package com.java.note.Jdk.model.agent.mutal;

import java.lang.reflect.Proxy;

/**
 * @author : kebukeyi
 * @date :  2021-04-23 18:16
 * @description :
 * @usinglink :
 **/
public class Master {

    public static void main(String[] args) {
        Duck duck = new Duck();
        JavaDynamicProxy javaDynamicProxy = new JavaDynamicProxy(duck);
        Cooking proxyInstance = (Cooking) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), Duck.class.getInterfaces(), javaDynamicProxy);
        proxyInstance.barbecue("田园鸭");
    }
}
 
