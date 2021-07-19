package cn.gof.adapter.object;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 15:39
 * @description:
 * @question:
 * @link:
 **/
public class Adaptee {

    public void fa() { //...
        System.out.println("Adaptee.fa()");
    }

    public void fb() { //...
        System.out.println("Adaptee.fb()");
    }

    //竟然默认实现了 接口 ITarget 的方法
    public void fc() { //...
        System.out.println("Adaptee.fc()");
    }

}
 
