package com.java.note.Jdk.generic.one;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-07 10:27
 * @Description : 泛型擦除
 * @Version :  0.0.1
 */
public class GenericTest {

    public static void one() {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("abc");

        ArrayList<Integer> list2 = new ArrayList<Integer>();
        list2.add(123);

        //最后发现结果为true,说明泛型类型String和Integer都被擦除掉了，只剩下原始类型。
        System.out.println(list1.getClass() == list2.getClass());
    }

    public static void two() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);  //这样调用 add 方法只能存储整形，因为泛型类型的实例为 Integer
        list.getClass().getMethod("add", Object.class).invoke(list, "asd");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    //这是一个简单的泛型方法
    public static <T> T add(T x, T y) {
        return y;
    }

    public static void three() {
        /**不指定泛型的时候*/
        int i = GenericTest.add(1, 2); //这两个参数都是Integer，所以T为Integer类型
        Number f = GenericTest.add(1, 1.2); //这两个参数一个是Integer，以风格是Float，所以取同一父类的最小级，为Number
        Object o = GenericTest.add(1, "asd"); //这两个参数一个是Integer，以风格是Float，所以取同一父类的最小级，为Object

        /**指定泛型的时候*/
        int a = GenericTest.<Integer>add(1, 2); //指定了Integer，所以只能为Integer类型或者其子类
        // int b = GenericTest.<Integer>add(1, 2.2); //编译错误，指定了Integer，不能为Float
        Number c = GenericTest.<Number>add(1, 2.2); //指定为Number，所以可以为Integer和Float
    }

    //因为类型检查就是编译时完成的，new ArrayList()只是在内存中开辟了一个存储空间，可以存储任何类型对象
    //而真正设计类型检查的是它的引用，因为我们是使用它引用list1来调用它的方法，比如说调用add方法，所以list1引用能完成泛型类型的检查。而引用list2没有使用泛型，所以不行
    public static void four() {
        ArrayList<String> list1 = new ArrayList();
        list1.add("1"); //编译通过
        // list1.add(1); //编译错误
        String str1 = list1.get(0); //返回类型就是String

        ArrayList list2 = new ArrayList<String>();
        list2.add("1"); //编译通过
        list2.add(1); //编译通过
        Object object = list2.get(0); //返回类型就是Object

        new ArrayList<String>().add("11"); //编译通过
        //new ArrayList<String>().add(22); //编译错误

        String str2 = new ArrayList<String>().get(0); //返回类型就是String
    }

    public static void five() {
        ArrayList arrayList = new ArrayList<String>();
        arrayList.add(22);
        //因为类型擦除之后(运行时), ArrayList也并不知道自己存储的是什么类型(ArrayList内部使用了一个Object数组存储的元素), 所以在取出元素时, 要进行强制类型转换.
        arrayList.get(3);
        if (arrayList.contains("sw")) {
            System.out.println("OK");
        }
    }

    public static void main(String[] args) {
        five();
    }
}
