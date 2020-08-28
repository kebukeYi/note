package com.java.note.jvm;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  10:13
 * @Description
 */
public class JVMCase {

    /**
     * 举例：String a="b"和 String c = new String(“b”)，请问这两个对象会分别创建在 JVM 内存模型中的哪块区域呢？
     * String a="b"可能创建一个对象或者不创建对象,如果"b"这个字符串在常量池里不存在会在常量池创建一个String对象"b",如果已经存在则a直接reference to这个常量池里的对象;
     * String c= new String("b")至少创建一个对象,也可能两个,因为用到new关键字,会在堆内在创建一个的String对象,它的值是"b"。同时,如果"b"这个字符串在常量池里不存在,会在常量池创建这个一个String对象"b"。
     */

    //准备阶段会为类的静态变量分配内存，初始化为系统的初始值

    // 常量  方法区
    public final static String MAN_SEX_TYPE = "man";

    // 静态变量  方法区
    public static String WOMAN_SEX_TYPE = "woman";

    /**
     * 方法区
     * JVMCase.class
     * 静态区：
     * main()
     * print()
     * <p>
     * Student.class
     * 成员变量 字段以及描述符
     * 成员方法 名称以及描述符
     */

    public static void main(String[] args) {
        //堆中是实例，对象引用 student 就存放在栈中
        Student stu = new Student();
        stu.setName("nick");
        stu.setSexType(MAN_SEX_TYPE);
        stu.setAge(20);
        //堆中
        JVMCase jvmcase = new JVMCase();

        // 此时 sayHello 方法入栈，并通过栈中的 student 引用调用堆中的 Student 对象；
        jvmcase.sayHello(stu);

        // 之后，调用静态方法 print，print 静态方法属于 JVMCase 类，是从静态方法中获取，之后放入到栈中，也是通过 student 引用调用堆中的 student 对象。
        print(stu);

    }


    // 常规静态方法
    public static void print(Student stu) {
        System.out.println("name: " + stu.getName() + "; sex:" + stu.getSexType() + "; age:" + stu.getAge());
    }


    // 非静态方法
    public void sayHello(Student stu) {
        System.out.println(stu.getName() + "say: hello");
    }
}

class Student {
    String name;
    String sexType;
    int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSexType() {
        return sexType;
    }

    public void setSexType(String sexType) {
        this.sexType = sexType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
