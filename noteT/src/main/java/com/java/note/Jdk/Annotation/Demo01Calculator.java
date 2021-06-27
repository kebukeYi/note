package com.java.note.Jdk.Annotation;

import java.io.*;
import java.lang.reflect.Method;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 17:46
 * @description : 利用反射、注解，写一个简单的测试框架，当主方法执行后，会自动加载被检测的所有方法，判断是否有异常，并记录到文件中。注解名称是@Check.
 **/
public class Demo01Calculator {

    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {

        Class<Calculator> calculatorClass = Calculator.class;
        Calculator calculator = calculatorClass.newInstance();
        Method[] declaredMethods = calculatorClass.getDeclaredMethods();

        int count = 0;

        //字符缓冲输出流
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("bug.txt", true));

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Check.class)) {
                try {
                    method.invoke(calculator, 1, 0);
                } catch (Exception e) {
                    count++;
                    bufferedWriter.newLine();
                    bufferedWriter.write("被测试的方法：" + method.getName() + "，该方法出现异常。");
                    bufferedWriter.newLine();
                    bufferedWriter.write("异常的名称：" + e.getCause().getClass().getSimpleName());
                    bufferedWriter.newLine();
                    bufferedWriter.write("异常的原因：" + e.getCause().getMessage());
                    bufferedWriter.newLine();
                }
            }
        }
        bufferedWriter.write("本次测试一共出现了" + count + "次异常。");
        bufferedWriter.newLine();
        bufferedWriter.write("====================================");

        // 8. 释放BufferedWriter资源
        bufferedWriter.flush();
        bufferedWriter.close();

        printByFileReaderChar("bug.txt");

        printByFileReaderLine("bug.txt");
    }

    //一个字符一个字符
    public static void printByFileReaderChar(String pathName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathName));
        if (!bufferedReader.ready()) {
            System.out.println("文件流暂时无法读取");
            return;
        }
        int result = 0;
        while ((result = bufferedReader.read()) != -1) {
            //因为读取到的是int类型的，所以要强制类型转换
            System.out.print((char) result);
        }
        bufferedReader.close();
    }

    //一行一行读取
    public static void printByFileReaderLine(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath)
        );
        if (!reader.ready()) {
            System.out.println("文件流暂时无法读取");
            return;
        }
        int size = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }
        reader.close();
    }
}
 
