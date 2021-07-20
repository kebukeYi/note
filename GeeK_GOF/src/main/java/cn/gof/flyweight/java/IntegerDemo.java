package cn.gof.flyweight.java;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 19:30
 * @description:
 * @question:
 * @link:
 **/
public class IntegerDemo {

//方法一：
//-Djava.lang.Integer.IntegerCache.high=255
//方法二
// -XX:AutoBoxCacheMax=255


    public static void main(String[] args) {

        Integer i1 = 56; //底层执行了：Integer i = Integer.valueOf(59);
        Integer i = Integer.valueOf(59);
        int j = i; //底层执行了：int j = i.intValue();
        Integer i2 = 56;
        Integer i3 = 129;
        Integer i4 = 129;
        System.out.println(i1 == i2);
        System.out.println(i3 == i4);

        System.out.println("-----------------------");

        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d);//true
        System.out.println(e == f);//false
        System.out.println(c == (a + b));//true
        System.out.println(c.equals(a + b));//true
        System.out.println(g == (a + b));//true
        System.out.println(g.equals(a + b));//false ==比较会让Integer对象强转成Long对象，然后就是用的LongCache了

    }
}
 
