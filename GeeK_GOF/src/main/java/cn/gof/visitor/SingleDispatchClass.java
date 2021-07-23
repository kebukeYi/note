package cn.gof.visitor;

/**
 * @author : kebukeYi
 * @date :  2021-07-22 17:53
 * @description:
 * @question:
 * @link:
 **/
public class SingleDispatchClass {

    public void polymorphismFunction(ParentClass p) {
        p.f();
    }

    //重载的调用确实是根据参数的声明类型决定的，但是调用哪个对象的方法是运行时动态绑定的
    public void overloadFunction(ParentClass p) {
        p.f();//I am ChildClass's f().
        System.out.println(p.getClass().getName());//cn.gof.visitor.ChildClass
        System.out.println("I am overloadFunction(ParentClass p).");
    }

    public void overloadFunction(ChildClass c) {
        c.f();
        System.out.println(c.getClass().getName());
        System.out.println("I am overloadFunction(ChildClass c).");
    }
}
 
