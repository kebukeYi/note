package com.java.note.Jdk.throwable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author : kebukeYi
 * @date :  2021-09-26 13:22
 * @description: 异常只是打印出错误 然后退出虚拟机
 * @question:
 * @link:
 **/
public class ExceptionTest {

    public static void main(String[] args) throws CompilerException {
        //String name = null;
        //运行时异常 NullPointerException extends RuntimeException extends Exception  不要进行预处理 假如进行了预处理呢? 那就直接不在控制台报错了
        //System.out.println(name.length());

        try {
            //编译时异常 FileNotFoundException extends IOException extends Exception
            //需要预处理方案: 1. 自己执行 try catch 方案  2.方法上进行 throw 异常 让调用者去处理异常
            FileInputStream fileInputStream = new FileInputStream("dsadsa.fdfs");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        testCompilerException();

        //并不需要 进行显式抛异常
        testRunTimeException();
    }

    //手动抛出 编译异常
    public static void testCompilerException() throws CompilerException {
        throw new CompilerException("testCompilerException", new Exception());
    }

    //手动抛出 运行时异常
    public static void testRunTimeException() {
        throw new RunTimeException("testCompilerException", new Exception());
    }
}
 
