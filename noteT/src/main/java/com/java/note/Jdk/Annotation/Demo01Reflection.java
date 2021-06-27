package com.java.note.Jdk.Annotation;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 17:32
 * @description :
 **/

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 写一个"框架"，不能改变该类的任何代码的前提下，可以帮我们创建任意类的对象，并且执行其中任意方法
 * 利用：反射、注解实现
 */
//@pro(className = "com.java.note.Jdk.Annotation.Person", methodName = "personMethod")
@pro(className = "com.java.note.Jdk.Annotation.Student", methodName = "studentMethod")
public class Demo01Reflection {

    public static void main(String[] args) {
        // 1.1 获取Demo01Reflection类的字节码文件对象
        Class<Demo01Reflection> demo01ReflectionClass = Demo01Reflection.class;
        pro annotation = demo01ReflectionClass.getAnnotation(pro.class);
        if (Objects.nonNull(annotation)) {
            try {
                String className = annotation.className();
                String methodName = annotation.methodName();
                Class<?> aClass = Class.forName(className);
                Object instance = aClass.newInstance();
                Method method = aClass.getMethod(methodName);
                method.invoke(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
 
