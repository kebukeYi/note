package cn.gof.oo.Polymorphism;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 10:09
 * @description : 编程语言要支持继承; 编程语言要支持子类可以重写（override）父类中的方法;
 * @question :
 * @usinglink :
 **/
public class SortedDynamicArray extends DynamicArray {

    @Override
    public void add(Integer e) {
        ensureCapacity();
        int i;
        for (i = size - 1; i >= 0; --i) { //保证数组中的数据有序
            if (elements[i] > e) {
                elements[i + 1] = elements[i];
            } else {
                break;
            }
        }
        elements[i + 1] = e;
        ++size;
    }
}
 
