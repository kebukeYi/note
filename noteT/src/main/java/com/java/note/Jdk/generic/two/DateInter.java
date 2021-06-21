package com.java.note.Jdk.generic.two;

import java.util.Date;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-07 12:22
 * @Description :
 * @Version :  0.0.1
 */
public class DateInter extends Pair<Date> {

    /*
     *1.原意：将父类的泛型类型限定为Date，那么父类里面的两个方法的参数都为Date类型。然后再子类中重写参数类型为Date的那两个方法，实现继承中的多态。
     * 2.我们在子类中重写这两个方法一点问题也没有，实际上，从他们的@Override标签中也可以看到，一点问题也没有，实际上是这样的吗？
     */

    @Override
    public void setValue(Date value) {
        super.setValue(value);
    }

    @Override
    public Date getValue() {
        return super.getValue();
    }

    //3.分析：实际上，类型擦除后，父类的的泛型类型全部变为了原始类型Object，所以父类编译之后会变成下面的样子：
    class Pair {
        //Date 变为了 Object
        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    //4.先来分析setValue方法，父类的类型是Object，而子类的类型是Date，参数类型不一样，这如果实在普通的继承关系中，根本就不会是重写，而是重载。
    //如果是重载，那么子类中两个setValue方法，一个是参数Object类型，一个是Date类型，可是我们发现，根本就没有这样的一个子类继承自父类的Object类型参数的方法。
    // 所以说，却是 是重写了，而不是重载了。
    public static void main(Strings[] args) throws ClassNotFoundException {
        DateInter dateInter = new DateInter();
        dateInter.setValue(new Date());
        //这是为何？
        //dateInter.setValue(new Object()); //编译错误
    }

    //5.思路 可是由于种种原因，虚拟机并不能将泛型类型变为Date，只能将类型擦除掉，变为原始类型Object。这样，我们的本意是进行重写，实现多态。
    // 可是类型擦除后，只能变为了重载。这样，类型擦除就和多态有了冲突。
    // JVM知道你的本意吗？知道！！！可是它能直接实现吗，不能！！！如果真的不能的话，那我们怎么去重写我们想要的Date类型参数的方法啊。
    //于是JVM采用了一个特殊的方法，来完成这项功能，那就是桥方法。

}
