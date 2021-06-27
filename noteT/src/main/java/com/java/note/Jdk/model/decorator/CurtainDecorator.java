package com.java.note.Jdk.model.decorator;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:04
 * @Description 窗帘装饰类
 */
public class CurtainDecorator extends BaseDecorator {

    public CurtainDecorator(IDecorator decorator) {
        super(decorator);
    }

    /**
     * 窗帘具体装饰方法
     */
    @Override
    public void decorate() {
        System.out.println(" 窗帘装饰。。。");
        super.decorate();
    }

    public static void main(String[] args) {
        IDecorator decorator = new Decorator();
        IDecorator curtainDecorator = new CurtainDecorator(decorator);
        curtainDecorator.decorate();
    }
}
