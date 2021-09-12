package com.mmy.nio.path;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 00:36
 * @description:
 * @question:
 * @link:
 **/
public class PathDemo {


    public static void main(String[] args) {
        //绝对路径是 d:\atguigu\001.txt
        //在 Java 字符串中， \ 是一个转义字符，需要编写\\，告诉 Java 编译器在字符串中写入一个\字符。
        Path path = Paths.get("d:\\atguigu\\001.txt");
        System.out.println(path);
        // Linux、MacOS 等操作字体上，上面的绝对路径可能如下:
        //Path path = Paths.get("/home/jakobjenkov/myfile.txt");


        //创建path实例
        Path path1 = Paths.get("d:\\atguigu\\01.txt");

        //创建相对路径
        //代码1
        Path projects = Paths.get("d:\\atguigu", "projects");

        //代码2
        Path file = Paths.get("d:\\atguigu", "projects\\002.txt");


        String originalPath = "d:\\atguigu\\projects\\..\\yygh-project";

        Path path2 = Paths.get(originalPath);
        System.out.println("path2 = " + path2);

        //Path 接口的 normalize()方法可以使路径标准化。标准化意味着它将移除所有在路径
        //字符串的中间的.和..代码，并解析路径字符串所引用的路径。
        Path path3 = path1.normalize();
        System.out.println("path3 = " + path3);
    }
}
 
