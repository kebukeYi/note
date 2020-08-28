package com.java.note.Jdk.model.builders;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/28  8:48
 * @Description
 */
public class Worker extends Builder {

    private Product product;

    public Worker() {
        this.product = new Product();
    }

    @Override
    Builder buildA() {
        product.setBuildA("BuildA");
        System.out.println("BuildA");
        return this;
    }

    @Override
    Builder buildB() {
        product.setBuildB("BuildB");
        System.out.println("BuildB");
        return this;

    }

    @Override
    Builder buildC() {
        product.setBuildC("BuildC");
        System.out.println("BuildC");
        return this;

    }

    @Override
    Builder buildD() {
        product.setBuildD("BuildD");
        System.out.println("BuildD");
        return this;

    }

    @Override
    Product getProduct() {
        return product;
    }

}
