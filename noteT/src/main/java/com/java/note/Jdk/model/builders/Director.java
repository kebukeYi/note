package com.java.note.Jdk.model.builders;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/28  8:53
 * @Description 指挥构建工程
 * 隐藏创造细节
 */
public class Director {

    //指挥顺序构建房子
    public Product builderProduct(Builder builder) {
        builder.buildA();
        builder.buildB();
        builder.buildC();
        builder.buildD();

        return builder.getProduct();
    }


    public static void main(String[] args) {
        Director director = new Director();
        Product product = director.builderProduct(new Worker());
        System.out.println(product.toString());


        Worker worker = new Worker();
        Product workerProduct = worker.getProduct();
        System.out.println(workerProduct);

        Worker worker1 = new Worker();
        Product product1 = worker1.buildA().buildB().buildC().buildD().getProduct();
        System.out.println(product1);


    }
}
