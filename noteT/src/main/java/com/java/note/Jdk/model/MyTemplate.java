package com.java.note.Jdk.model;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/27  23:08
 * @Description 设计模式之 模板建立
 */
public abstract class MyTemplate {

    //这是我们的模板方法
    public final void TemplateMethod() {
        PrimitiveOperation1();
        PrimitiveOperation2();
        PrimitiveOperation3();
    }

    protected void PrimitiveOperation1() {
        //当前类实现
    }

    //被子类实现的方法
    protected abstract void PrimitiveOperation2();

    protected abstract void PrimitiveOperation3();

}

class TemplateImpl extends MyTemplate {

    @Override
    public void PrimitiveOperation2() {
        //当前类实现
    }

    @Override
    public void PrimitiveOperation3() {
        //当前类实现
    }
}
