package cn.gof.oo.test;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 14:24
 * @description :
 * @question :
 * @usinglink :
 **/
public class TestDemo {

    public int index = 9;

    public void aVoid(final int ha) {
        int a = index;
        System.out.println(a);
    }

    public void bVoid() {
        int b = index;
        System.out.println(index);
        b += b;
    }

    public static void main(String[] args) {
        final TestDemo testDemo = new TestDemo();
        testDemo.aVoid(2);
        testDemo.bVoid();
    }


}
 
