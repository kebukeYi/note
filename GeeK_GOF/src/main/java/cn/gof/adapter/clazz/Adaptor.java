package cn.gof.adapter.clazz;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 15:35
 * @description:
 * @question:
 * @link:
 **/
public class Adaptor extends Adaptee implements ITarget {

    @Override
    public void f1() {
        super.fa();
    }

    @Override
    public void f2() {
        System.out.println("Adaptor.f2()");
    }

    // 这里fc()不需要实现，直接继承自Adaptee，这是跟对象适配器最大的不同点

}
 
