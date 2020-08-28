package com.java.note.Jdk.model.single;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  16:36
 * @Description 懒汉式；
 * 存在线程安全问题； 需要加锁；
 * https://blog.csdn.net/Dongguabai/article/details/82828125?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
 */
public class Singleton4 {


    /* new Singleton4(); /
    memory=a1 locate();     //1:分配对象的内存空间
    ctorinstance( memory);   //2:初始化对象
    instance memory;         //3:设置 instance指向刚分配的内存地址
     */
    //    private static  Singleton4 singleton4；

    //改进方法
    private static volatile Singleton4 singleton4;

    //DCL 模式
    public static Singleton4 getInstance() throws InterruptedException {
        //性能优化
        //如果已经创建实例了 那么下一从线程不会在进行同步机制；直接返回；
        if (singleton4 == null) {
            //安全优化
            synchronized (Singleton4.class) {
                if (singleton4 == null) {
                    Thread.sleep(100);
                    singleton4 = new Singleton4(); //但是仍然会出现问题！！！ 当线程进行第一次检查的时候，代码读取到instance不为null时，instance引用的对象有可能还没有完成初始化。
                }
            }
        }
        return singleton4;
    }

    private Singleton4() {
    }
}
