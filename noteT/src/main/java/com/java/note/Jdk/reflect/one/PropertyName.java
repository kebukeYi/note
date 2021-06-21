package com.java.note.Jdk.reflect.one;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyName {

    //name代表的是属性的中文注释，isDict代表该属性是否为字典项，默认为false；如果设置为true，则代表该属性存储的是字典代码，需要将字典项代码翻译成对应的字典项名称

    // 字段属性中文注释
    Strings name() default "";

    // 字段属性是否为字典项代码
    boolean isDict() default false;
}
