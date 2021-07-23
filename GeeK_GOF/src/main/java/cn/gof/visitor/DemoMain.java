package cn.gof.visitor;

import cn.gof.visitor.ChildClass;
import cn.gof.visitor.ParentClass;
import cn.gof.visitor.SingleDispatchClass;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 17:53
 * @description:
 * @question:
 * @link:
 **/
public class DemoMain {

    public static void main(String[] args) {
        SingleDispatchClass demo = new SingleDispatchClass();
        //实际类型是 ChildClass
        //声明类型是 ParentClass
        ParentClass p = new ChildClass();
        //多态
        demo.polymorphismFunction(p);//执行哪个对象的方法，由对象的实际类型决定
        System.out.println("===========================");
        //重载
        demo.overloadFunction(p);//执行对象的哪个方法，由参数对象的声明类型决定
    }
}
 
