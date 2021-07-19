package cn.gof.adapter.object;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 15:39
 * @description:
 * @question:
 * @link:
 **/
public class Adaptor implements ITarget {

    private Adaptee adaptee;

    public Adaptor(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void f1() {
        adaptee.fa();
    }

    @Override
    public void f2() {
        System.out.println("Adaptor.f2()");
    }

    @Override
    public void fc() {
        adaptee.fc();
    }
}
 
