package com.java.note.Jdk.reflect.two;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 17:12
 * @description : 修改配置文件，不用修改代码。实现了创建任意类的对象，并且执行其中任意方法。
 **/
public class DemoReflection {

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 1. 在程序中加载读取配置文件
        // 1.1 创建Properties对象
        Properties properties = new Properties();
        // 1.2 加载配置文件，转换为一个集合
        // 1.2.1 获取类的类加载器
        ClassLoader classLoader = DemoReflection.class.getClassLoader();
         InputStream inputStream = classLoader.getResourceAsStream("pro.properties");
//        InputStream inputStream = new FileInputStream("pro.properties");
        properties.load(inputStream);
        // 2. 获取配置文件中定义的数据
        // 2.1 获取类名称
        String className = properties.getProperty("className");
        String methodName = properties.getProperty("methodName");
        // 3. 加载该类进内存
        Class<?> aClass = Class.forName(className);
        // 4. 创建对象
        Object newInstance = aClass.newInstance();
        // 5. 获取方法对象
        Method declaredMethod = aClass.getDeclaredMethod(methodName);
        // 6. 执行方法
        declaredMethod.invoke(newInstance);
    }
}
 
