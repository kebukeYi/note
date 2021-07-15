package cn.gof.oo.Polymorphism;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 10:15
 * @description : 多态样例 : 编程语言 支持父类对象可以引用子类对象
 * @question :
 * @usinglink :
 **/
public class DynamicArrayExample {

    public static void test(DynamicArray dynamicArray) {
        dynamicArray.add(5);
        dynamicArray.add(1);
        dynamicArray.add(3);
        for (int i = 0; i < dynamicArray.size(); ++i) {
            System.out.println(dynamicArray.get(i));
        }
    }

    public static void main(String args[]) {
        //子类 SortedDyamicArray 替换父类 DynamicArray，执行子类 SortedDyamicArray 的 add() 方法，也就是实现了多态特性
        DynamicArray dynamicArray = new SortedDynamicArray();
        test(dynamicArray); // 打印结果：1、3、5 }
    }
}
 
