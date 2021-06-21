package com.java.note.Jdk.Annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 注解能作用于类上
@Retention(RetentionPolicy.RUNTIME) // 当前被描述的注解，会保留到class字节码文件中，并被JVM读取到
public @interface pro {

    public abstract Strings className();

    public abstract Strings methodName();
}
