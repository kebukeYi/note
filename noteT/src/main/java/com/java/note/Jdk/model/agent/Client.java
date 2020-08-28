package com.java.note.Jdk.model.agent;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  23:28
 * @Description
 */
public class Client {
    public static void main(String[] args) {
        Host host = new Host();
        host.rent();

        Proxy proxy = new Proxy(host);
        proxy.rent();
    }
}
