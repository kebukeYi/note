package com.java.note.spring.imports;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/12  21:41
 * @Description
 */
public class MyImportSelector implements ImportSelector {

    //返回值,就是到导入到容器中的组件全类名
    // AnnotationMetadata:当前标注@ Import注解的类的所有注解信息
    @Override
    public Strings[] selectImports(AnnotationMetadata importingClassMetadata) {
        //  importingClassMetadata 能够获取所有的类的信息 或者注解的属性
        //根据全类名 向容器中注入bean
        return new Strings[]{"com.java.note.spring.imports.NewClassTest", "com.java.note.spring.bean.People"};
    }


}
