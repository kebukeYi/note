package com.java.note.tomcat;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.web.ServerProperties;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/12  15:12
 * @Description
 */
public class MyTomcat {

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(20000);
        tomcat.addWebapp("/index", "src/main/resources/templates/index.html");
        tomcat.start();
    }


}
