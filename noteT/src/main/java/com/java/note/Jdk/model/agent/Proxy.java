package com.java.note.Jdk.model.agent;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  23:28
 * @Description
 */
public class Proxy implements Rent {

    private Host host;

    public Proxy(Host host) {
        this.host = host;
    }


    @Override
    public void rent() {
        seeHouse();
        host.rent();
        wirte();
        pay();
    }

    public void seeHouse() {
        System.out.println("查看房屋");
    }

    public void pay() {
        System.out.println("支付房租");
    }

    public void wirte() {
        System.out.println("租赁房屋");
    }

}
