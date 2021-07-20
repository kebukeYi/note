package cn.gof.template;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 17:20
 * @description:
 * @question:
 * @link:
 **/
public abstract class AbstractClass {

    //模板方法一
    public final void templateMethod1() {
        method1();
        method2();
    }

    protected void method2() {
        throw new UnsupportedOperationException();
    }


    protected  void method1(){

    };

    //模板方法二
    public final void templateMethod2() {
        method3();
        method4();
    }


    protected abstract void method4();

    protected abstract void method3();

}
 
