package cn.gof.oo.Polymorphism;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 10:26
 * @description :
 * @question :
 * @usinglink :
 **/
public class IteratorDemo {

    private static void print(Iterator iterator) {
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    //我们通过传递不同类型的实现类（Array、LinkedList）到 print(Iterator iterator) 函数中，支持动态的调用不同的 next()、hasNext() 实现
    public static void main(String[] args) {
        Iterator arrayIterator = new Array();
        //调用哪个方法，由方法参数的编译时类型决定
        print(arrayIterator);
        Iterator linkedListIterator = new LinkedList();
        //调用哪个对象，由对象的运行时类型决定
        print(linkedListIterator);
    }
}
 
