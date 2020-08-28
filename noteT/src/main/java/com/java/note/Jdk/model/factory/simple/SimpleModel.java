package com.java.note.Jdk.model.factory.simple;

import java.io.Serializable;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/6  11:58
 * @Description 单例模式
 * 正确并且可以做到延迟加载的写法其实就是三种：
 * <p>
 * 1.使用volatile修饰变量并且双重校验的写法。
 * <p>
 * 2.使用静态内部类来实现（类A有一个静态内部类B，类B有一个静态变量instance，类A的getInstance()方法会返回类B的静态变量instance，因为只有调用getInstance()方法时才会加载静态内部类B，这种写法缺点是不能传参。）
 * <p>
 * 3. 使用枚举来实现（）
 */
public class SimpleModel implements Serializable {


    private static SimpleModel simpleModel_1;

    /**
     * 懒汉模式
     * 多线程执行时，可能会在instance完成初始化之前，其他线性线程判断instance为null，从而也执行第二步的代码，导致初始化覆盖。
     * 初始化完成以后，每次调用getInstance()方法都需要获取同步锁，导致不必要的开销
     *
     * @return
     */
    public synchronized SimpleModel getSimpleModel_1() {
        if (simpleModel_1 == null) {
            simpleModel_1 = new SimpleModel();
        }
        return simpleModel_1;
    }

    //饿汉模式
    //这种方法是缺点在于不能做到延时加载，在第一次调用getInstance()方法之前，如果Singleton类被使用到，那么就会对instance变量初始化。
    private static SimpleModel instance = new SimpleModel();

    public static SimpleModel getInstance() {
        return instance;
    }


    private static SimpleModel simpleModel;

    //双重检查模式
    public static SimpleModel getSimpleModel() {
        if (simpleModel == null) {
            synchronized (SimpleModel.class) {
                //双重检查存在的意义在于可能会有多个线程进入第一个判断，然后竞争同步锁，线程A得到了同步锁，创建了一个Singleton实例，
                // 赋值给instance，然后释放同步锁，此时线程B获得同步锁，又会创建一个Singleton实例，造成初始化覆盖。
                if (simpleModel == null) {
                    /**
                     1.为对象分配内存空间。
                     2.执行初始化的代码。
                     3.将分配好的内存地址设置给instance引用。
                     编译器会对指令进行重排序，只能保证单线程执行时结果不会变化，也就是可能第3步会在第2步之前执行,
                     某个线程A刚好执行完第3步，正在执行第2步时，此时如果有其他线程B进入if (instance == null)判断，
                     会发现instance不为null，然后将instance返回，但是实际上instance还没有完成初始化，线程B会访问到一个未初始化完成的instance对象。
                     */
                    simpleModel = new SimpleModel();
                }
            }
        }
        return simpleModel;
    }


    /**
     * 基于 volatile 的双重检查锁定的解决方案。
     * volatile可以保证变量的内存可见性及防止指令重排。
     * <p>
     * volatile修饰的变量在编译后，会多出一个lock前缀指令，lock前缀指令相当于一个内存屏障（内存栅栏），有三个作用：
     * <p>
     * 1.确保指令重排序时，内存屏障前的指令不会排到后面去，内存屏障后的指令不会排到前面去。
     * 2.强制对变量在线程工作内存中的修改操作立即写入到物理内存。
     * 3. 如果是写操作，会导致其他CPU中对这个变量的缓存失效，强制其他CPU中的线程在获取变量时从物理内存中获取更新后的值。
     */
    private static volatile SimpleModel vo_simpleModel;

    public static SimpleModel getVoSimpleModel() {
        if (instance == null) {
            synchronized (SimpleModel.class) {
                //双重检查存在的意义在于可能会有多个线程进入第一个判断，然后竞争同步锁，线程A得到了同步锁，
                // 创建了一个Singleton实例，赋值给instance，然后释放同步锁，此时线程B获得同步锁，又会创建一个Singleton实例，造成初始化覆盖。
                if (instance == null) {
                    instance = new SimpleModel();
                }
            }
        }
        return instance;
    }

    /**
     * 使用静态内部类来实现
     * 因为JVM底层通过加锁实现，保证一个类只会被加载一次，多个线程在对类进行初始化时，只有一个线程会获得锁，然后对类进行初始化，
     * 其他线程会阻塞等待。所以可以使用上面的代码来保证instance只会被初始化一次，这种写法的问题在于创建单例时不能传参。
     * <p>
     * private static class Signleton {
     * private static Signleton instance = new Signleton();
     * }
     * <p>
     * public static Signleton getInstance() {
     * return Signleton.instance ;  // 这里将导致 Signleton 类被初始化
     * }
     */

    /**
     * 如何解决序列化时可以创建出单例对象的问题?
     * 如果将单例对象序列化成字节序列后，然后再反序列成对象，那么就可以创建出一个新的单例对象，从而导致单例不唯一，
     * 避免发生这种情况的解决方案是在单例类中实现readResolve()方法。
     * <p>
     * 通过实现readResolve方法，ObjectInputStream实例对象在调用readObject()方法进行反序列化时，就会判断相应的类是否实现了readResolve()方法，
     * 如果实现了，就会调用readResolve()方法返回一个对象作为反序列化的结果，而不是去创建一个新的对象。
     *
     * @return 浅复制与深复制的区别
     */
    private Object readResolve() {
        System.out.println("read resolve");
        return instance;
    }

    /**
     * 当线程进行一个volatile变量的写操作时，JIT编译器生成的汇编指令会在写操作的指令后面加上一个“lock”指令。 Java代码如下:
     *
     * instance = new Singleton(); // instance是volatile变量
     * 转变成汇编代码，如下 :
     * 0x01a3de1d: movb $0×0,0×1104800(%esi);0x01a3de24: lock addl $0×0,(%esp);
     *
     * “lock”有三个作用：
     *
     * 1.将当前CPU缓存行的数据会写回到系统内存。
     * 2.这个写回内存的操作会使得其他CPU里缓存了该内存地址的数据无效。
     * 3.确保指令重排序时，内存屏障前的指令不会排到后面去，内存屏障后的指令不会排到前面去。
     * 可见性可以理解为一个线程的写操作可以立即被其他线程得知。为了提高CPU处理速度，CPU一般不直接与内存进行通信，而是将系统内存的数据读到内部缓存，
     * 再进行操作，对于普通的变量，修改完不知道何时会更新到系统内存。但是如果是对volatile修饰的变量进行写操作，JVM就会向处理器发送一条Lock前缀的指令，
     * 将这个变量所在的缓存行的数据立即写回到系统内存。但是即便写回到系统内存，其他CPU中的缓存行数据还是旧的，为了保证数据一致性，
     * 其他CPU会嗅探在总线上传播的数据来检查自己的缓存行的值是否过期，当CPU发现缓存行对应的内存地址被修改，那么就会将当前缓存行设置为无效，
     * 下次当CPU对这个缓存行上 的数据进行修改时，会重新从系统内存中把数据读到处理器缓存 里。
     */


}
