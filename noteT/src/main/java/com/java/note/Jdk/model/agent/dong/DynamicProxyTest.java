package com.java.note.Jdk.model.agent.dong;

import com.java.note.Jdk.model.agent.Operate;
import com.java.note.Jdk.model.agent.Operator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;

import com.java.note.Jdk.utils.MyProxyGenerator;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/1  下午 5:30
 * @Description 动态代理
 */
public class DynamicProxyTest {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        //1.首先原生接口的实现类准备好  或者占好坑位
        Operate operate = new Operator();
        //2.新建好自己的 加强类（传入原生的实现类 是为了调用原生实现类方法）
        InvocationHandlerImpl handler = new InvocationHandlerImpl(operate);
        //无传入方法
//        InvocationHandlerImpl handler1 = new InvocationHandlerImpl();
        //生成代理对象 　
        // 一个ClassLoader对象，定义了由哪个ClassLoader对象来对生成的代理对象进行加载
        // 一个Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了
        //一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个InvocationHandler对象上
        //那么我这个代理对象就会实现了这组接口
        //原生实现类的类加载器  将要实现的接口    完成实现的实体
        Operate operationProxy = (Operate) Proxy.newProxyInstance(operate.getClass().getClassLoader(), operate.getClass().getInterfaces(), handler);

        // Operate operationProxy = (Operate) Proxy.newProxyInstance(Operate.class.getClassLoader(), new Class<?>[]{Operate.class}, handler1);

        //生成代理类字节码
        String proxyName = "OperateService5";
        // byte[] bytes4 = ProxyGenerator.generateProxyClass(proxyName, new Class<?>[]{operaionProxy.getClass()});
        byte[] bytes4 = MyProxyGenerator.generateProxyClass(proxyName, new Class<?>[]{Operate.class});
        Path path4 = new File("F:\\" + proxyName + "$myproxy1.class").toPath();
        Files.write(path4, bytes4);

//        operate.doSomething("【operate】 deal with ");
        //增强完成
        //调用操作方法
        String deal_with = operationProxy.doSomething("【operationProxy】 deal with");
        System.out.println("deal_with ：" + deal_with);
        System.out.println("==============================");
        System.out.println(operate);
        System.out.println(operationProxy);//为什么不能打印  或者 为null？？？
        System.out.println("==============================");


        String s1 = "hello";
        //s所指向的内存地址是在堆内存中
        String s = new String("hello");
        //s1是在字符串常量池中
        // String s1 = "hello";
        //从常量池中获取
        String intern = s.intern();

        System.out.println(s == intern);//false
        System.out.println(s1 == intern);//true
        System.out.println(s == s1);//false
        System.out.println(s.equals(s1));//true


        MyUserDao myUserDao = new MyUserDao();
        OrderDao orderDao = new OrderDao();
        System.out.println(myUserDao.equals(orderDao));
        System.out.println(myUserDao.hashCode());
        System.out.println(orderDao.hashCode());


        Hero h = new Hero();
        h.setPrice("qq");
        System.out.println(h.getPrice());
        Field f = h.getClass().getDeclaredField("price"); //根据类对象获取该类的price属性
        f.set(h, "tt"); //通过获取的属性，给h对象修改这个属性的值
        System.out.println(h.getPrice());

    }
}
