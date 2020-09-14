package com.java.note.Jdk.model.agent.jing;

import com.java.note.Jdk.model.agent.Operate;
import com.java.note.Jdk.model.agent.Operator;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/1  下午 5:25
 * @Description 静态代理
 */
public class OperationProxy implements Operate {

    private Operator operator = null;

    @Override
    public void doSomething() {
        beforeDoSomething();
        if (operator == null) {
            operator = new Operator();
        }
        operator.doSomething();
        afterDoSomething();
    }

    private void beforeDoSomething() {
        System.out.println("before doing something");
    }

    private void afterDoSomething() {
        System.out.println("after doing something");
    }

    public static void main(String[] args) {
        Operate operate = new OperationProxy();//使用OperationProxy代替Operator
        operate.doSomething();  //代理者代替真实者做事情
    }
}
