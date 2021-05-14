package com.java.note.Jdk.Annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
// 表示MyAnnotation注解能作用于类、方法、成员变量上

@Retention(RetentionPolicy.RUNTIME)
// 当前被描述的注解（MyAnnotation），会保留到class字节码文件中，并被JVM读取到

@Documented
// 描述注解会被抽取到api文档中

@Inherited
// 描述注解是否被子类继承
public @interface MyAnnotation {
}
