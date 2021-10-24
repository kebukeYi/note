package com.java.interview.thread.consumer_provider;

/**
 * @author : kebukeYi
 * @date :  2021-10-21 23:07
 * @description:
 * @question:
 * @link:
 **/
public class IGoods {

    private String name;
    private int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public IGoods(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "IGoods{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
 
