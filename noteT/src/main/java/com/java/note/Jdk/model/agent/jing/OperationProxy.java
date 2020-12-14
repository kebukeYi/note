package com.java.note.Jdk.model.agent.jing;

import com.java.note.Jdk.model.agent.Operate;
import com.java.note.Jdk.model.agent.Operator;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/1  下午 5:25
 * @Description 静态代理
 */
public class OperationProxy implements Operate {

    //被代理对象
    private Operator operator = null;

    @Override
    public String doSomething(String name) {
        System.out.println("静态代理 开始");
        beforeDoSomething();
        if (operator == null) {
            operator = new Operator();
        }
        operator.doSomething(name);
        afterDoSomething();
        System.out.println("静态代理 结束");
        return "自己方法的返回值：success-4";
    }

    //加强方法前
    private void beforeDoSomething() {
        System.out.println("before doing something");
    }

    //加强方法后
    private void afterDoSomething() {
        System.out.println("after doing something");
    }

    public static void main(String[] args) {
        //使用OperationProxy代替Operator
        Operate operate = new OperationProxy();
        //代理者代替真实者做事情
        operate.doSomething("name");
    }
}
