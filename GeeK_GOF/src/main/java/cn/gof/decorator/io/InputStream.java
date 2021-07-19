package cn.gof.decorator.io;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 12:40
 * @description: 基类
 * @question:
 * @link:
 **/
public abstract class InputStream {


    int read() {
        System.out.println("InputStream 自己实现的read()方法");
        return -2;
    }

    int available() {
        System.out.println("InputStream 自己实现的available()方法");
        return -1;
    }

}
 
