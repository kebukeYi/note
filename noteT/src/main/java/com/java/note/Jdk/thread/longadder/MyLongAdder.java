package com.java.note.Jdk.thread.longadder;

import java.io.Serializable;

/**
 * @author : kebukeYi
 * @date :  2021-11-22 14:35
 * @description:
 * @question:
 * @link:
 **/
public class MyLongAdder extends MyStriped64 implements Serializable {

    private static final long serialVersionUID = 7249069246863182397L;

    /**
     * Creates a new adder with initial sum of zero.
     */
    public MyLongAdder() {
    }

    /**
     * Adds the given value.
     *
     * @param x the value to add
     */
    public void add(long x) {
        //cells 的引用
        MyStriped64.Cell[] as;
        //b base   v 期望值
        long b, v;
        //表示 cells 的长度
        int m;
        //a 表示当前线程命中的单元格
        MyStriped64.Cell a;
        //条件一： 成立   表示cells已经初始化好了 当前线程应该将数据写入到对应的 cells 中 ； 直接进入if代码块
        //              false  表示 cells 未被初始化 ，当前所有线程应该将数据写到 base 中；       进入下一个判断条件
        //条件二：false  表示当前线程替换数据成功； 好的达到目的 直接退出代码块；
        //              true 竞争发生 casBase 失败 需要扩容 或者 重试  ； 进入if代码块
        if ((as = cells) != null || !casBase(b = base, b + x)) {
            //什么时候会进来？
            //1. 条件一  成立 cells 已经初始化好了   当前线程应该将数据写入到对应的 cells 中
            //2. 条件二  成立 cells 未被初始化   竞争发生失败 需要扩容 或者 重试

            //true 未发生竞争    false 发生了竞争
            boolean uncontended = true;

            //条件一：(as == null || (m = as.length - 1) < 0  成立：说明cell未被初始化  也就是多线程写base出现竞争了；直接接入if代码块
            //                                                                       不成立：说明cell已经被初始化了，当前线程应该是 找自己的cell的值； 进入下一个判断条件
            //条件二：getProbe()  获取当前线程的hash值      成立：(cells已经开好了) 当前线程对应的下标元素为 null，需要创建； 直接接入if代码块
            //                                                                      不成立：当前线程对应的下标元素不为 null，说明下一步需要将 x  追加到 此cell 中； 进入下一个判断条件
            //条件三：a.cas(v = a.value, v + x)  ：                 成立：说明cas赋值完成；取反 false 直接 退出代码块，完事儿了；
            //                                                                      不成立： 说明当前线程对应得 cell 存在竞争，一般uncontended都是false；进入if代码块
            if (as == null || (m = as.length - 1) < 0 ||
                    (a = as[getProbe() & m]) == null ||
                    !(uncontended = a.cas(v = a.value, v + x))) {
                //进来这里的情况？
                //1. 条件一  成立 说明 cell 未被初始化 也就是多线程写 base 出现竞争了 需要重试初始化 cells
                //2. 条件二  成立 说明当前线程对应的下标元素为 null，需要创建；
                //3. 条件三  成立 说明当前线程对应的 cell 存在竞争，需要重试；
                longAccumulate(x, null, uncontended);
            }
        }
    }

    /**
     * Equivalent to {@code add(1)}.
     */
    public void increment() {
        add(1L);
    }

    /**
     * Equivalent to {@code add(-1)}.
     */
    public void decrement() {
        add(-1L);
    }

    /**
     * Returns the current sum.  The returned value is <em>NOT</em> an atomic snapshot;
     * invocation in the absence of concurrent
     * updates returns an accurate result, but concurrent updates that
     * occur while the sum is being calculated might not be
     * incorporated.  不是准确的
     *
     * @return the sum
     */
    public long sum() {
        MyStriped64.Cell[] as = cells;
        MyStriped64.Cell a;
        long sum = base;
        if (as != null) {
            for (int i = 0; i < as.length; ++i) {
                if ((a = as[i]) != null) {
                    sum += a.value;
                }
            }
        }
        return sum;
    }

    /**
     * Resets variables maintaining the sum to zero.  This method may
     * be a useful alternative to creating a new adder, but is only
     * effective if there are no concurrent updates.  Because this
     * method is intrinsically racy, it should only be used when it is
     * known that no threads are concurrently updating.
     */
    public void reset() {
        MyStriped64.Cell[] as = cells;
        MyStriped64.Cell a;
        base = 0L;
        if (as != null) {
            for (int i = 0; i < as.length; ++i) {
                if ((a = as[i]) != null)
                    a.value = 0L;
            }
        }
    }

    /**
     * Equivalent in effect to {@link #sum} followed by {@link
     * #reset}. This method may apply for example during quiescent
     * points between multithreaded computations.  If there are
     * updates concurrent with this method, the returned value is
     * <em>not</em> guaranteed to be the final value occurring before
     * the reset.
     *
     * @return the sum
     */
    public long sumThenReset() {
        MyStriped64.Cell[] as = cells;
        MyStriped64.Cell a;
        long sum = base;
        base = 0L;
        if (as != null) {
            for (int i = 0; i < as.length; ++i) {
                if ((a = as[i]) != null) {
                    sum += a.value;
                    a.value = 0L;
                }
            }
        }
        return sum;
    }

    /**
     * Returns the String representation of the {@link #sum}.
     *
     * @return the String representation of the {@link #sum}
     */
    public String toString() {
        return Long.toString(sum());
    }

    /**
     * Equivalent to {@link #sum}.
     *
     * @return the sum
     */
    public long longValue() {
        return sum();
    }

    /**
     * Returns the {@link #sum} as an {@code int} after a narrowing
     * primitive conversion.
     */
    public int intValue() {
        return (int) sum();
    }

    /**
     * Returns the {@link #sum} as a {@code float}
     * after a widening primitive conversion.
     */
    public float floatValue() {
        return (float) sum();
    }

    /**
     * Returns the {@link #sum} as a {@code double} after a widening
     * primitive conversion.
     */
    public double doubleValue() {
        return (double) sum();
    }

    /**
     * Serialization proxy, used to avoid reference to the non-public
     * Striped64 superclass in serialized forms.
     *
     * @serial include
     */
    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 7249069246863182397L;

        /**
         * The current value returned by sum().
         *
         * @serial
         */
        private final long value;

        SerializationProxy(MyLongAdder a) {
            value = a.sum();
        }

        /**
         * Return a {@code LongAdder} object with initial state
         * held by this proxy.
         *
         * @return a {@code LongAdder} object with initial state
         * held by this proxy.
         */
        private Object readResolve() {
            MyLongAdder a = new MyLongAdder();
            a.base = value;
            return a;
        }
    }

    /**
     * Returns a
     * <a href="../../../../serialized-form.html#java.util.concurrent.atomic.LongAdder.SerializationProxy">
     * SerializationProxy</a>
     * representing the state of this instance.
     *
     * @return a {@link MyLongAdder.SerializationProxy}
     * representing the state of this instance
     */
    private Object writeReplace() {
        return new MyLongAdder.SerializationProxy(this);
    }

    /**
     * @param s the stream
     * @throws java.io.InvalidObjectException always
     */
    private void readObject(java.io.ObjectInputStream s) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }

}
 
