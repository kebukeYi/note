package cn.gof.template;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 17:22
 * @description: 即便我们只用到其中的一个模板方法，子类也必须实现所有的抽象方法
 * @question:
 * @link:
 **/
public class SubClass extends AbstractClass {

    @Override
    protected void method1() {

    }

    @Override
    protected void method4() {

    }

    @Override
    protected void method3() {

    }


    public static void main(String[] args) {
        final SubClass subClass = new SubClass();
        subClass.templateMethod1();
    }
}
 
