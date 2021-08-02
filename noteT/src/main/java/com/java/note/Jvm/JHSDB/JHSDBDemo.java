package com.java.note.Jvm.JHSDB;

import org.omg.CORBA.ObjectHolder;

/**
 * @author : kebukeYi
 * @date :  2021-08-01 18:00
 * @description: 启动JHSDB  java -cp  sa-jdi.jar sun.jvm.hotspot.HSDB
 * @question: java -cp java -jar 的区别 ?
 *                      对于java -cp就不需要指定Main-Class来指定入口。因为第一个参数就指定了你的入口类，第二个参数就是你的jar包。
 *                      它会根据你的jar包找到第一个参数指定的Test类，来输出HelloWorld.
 * @link:
 **/
public class JHSDBDemo {

    static class Test {
        static ObjectHolder objectHolder = new ObjectHolder();
        ObjectHolder instanceObjectHolder = new ObjectHolder();

        void foo() {
            ObjectHolder localObject = new ObjectHolder();
            System.out.println("done");
        }
    }

    public static void main(String[] args) {
        final Test test = new Test();
        test.foo();
    }
}
 
