package com.java.note.Jvm.loadClass;

import lombok.SneakyThrows;

import java.io.InputStream;

/**
 * @author : kebukeyi
 * @date :  2021-06-21 11:58
 * @description :
 * @question :
 * @usinglink :
 **/
public class LoadClass {

    //不同的加载器 拥有不同的加载路径 这是最关键的一点！
    //Bootstrap ClassLoader  <JAVA_HOME>/lib
    //Extension ClassLoader  <JAVA_HOME>/lib/ext
    //Application ClassLoader classpath/java.class.path
    //什么叫做顶层加载器无法加载而下传到子加载器加载：就是在顶层加载器默认的加载路径中  无法找到提供全限名的类 从而无法加载 不得已下发加载

    //-XX:+TraceClassLoading 可以看加载了哪些类
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader classLoader = new ClassLoader() {
            @SneakyThrows
            @Override
            //false
            //对双亲委派模型的破坏
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                //读取字节码阶段
                //返回此字符最后出现的索引处 (从0开始计数)
                int lastIndexOf = name.lastIndexOf('.');
                String fileName = name.substring(lastIndexOf + 1) + ".class";
                InputStream resourceAsStream = getClass().getResourceAsStream(fileName);
                if (resourceAsStream == null) {
                    return super.loadClass(name);
                }
                byte[] bytes = new byte[resourceAsStream.available()];
                resourceAsStream.read(bytes);
                //加载阶段
                return defineClass(name, bytes, 0, bytes.length);
            }

            // @Override
            // Jvm的补救措施 重写findClass 而不是重写loadClass 以此来达到 同一个 全限类名 可以只加载一次 不会出现 两个加载器 加载两次
            // true
//            protected Class<?> findClass(String name) throws ClassNotFoundException {
//                //返回此字符最后出现的索引处 (从0开始计数)
//                int lastIndexOf = name.lastIndexOf('.');
//                String fileName = name.substring(lastIndexOf + 1) + ".class";
//                InputStream resourceAsStream = getClass().getResourceAsStream(fileName);
//                if (resourceAsStream == null) {
//                    return super.findClass(name);
//                }
//                byte[] bytes = new byte[resourceAsStream.available()];
//                resourceAsStream.read(bytes);
//                return defineClass(name, bytes, 0, bytes.length);
//            }
        };
        //全限类名:类名全称，带包路径的用点隔开，例如: java.java.lang.String
        // 非限定(non-qualified)类名也叫短名，就是我们平时说的类名，不带包的，例如：String
        Object instance = classLoader.loadClass("com.java.note.Jvm.loadClass.A").newInstance();
//        System.out.println(instance.getClass());
        //  System.out.println(instance instanceof com.java.note.Jvm.loadClass.A);
    }
}
 
