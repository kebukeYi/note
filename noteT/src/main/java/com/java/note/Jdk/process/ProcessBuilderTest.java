package com.java.note.Jdk.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author : kebukeYi
 * @date :  2021-08-02 23:01
 * @description:
 * @question:
 * @link:
 **/
public class ProcessBuilderTest {

    public void testProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cd F:\\BaiduNetdiskDownload");
        processBuilder.command("cp --parents `find a -name *.html` copy_a");
        //将标准输入流和错误输入流合并，通过标准输入流读取信息
        processBuilder.redirectErrorStream(true);
        try {
            //启动进程
            Process start = processBuilder.start();
            //获取输入流
            InputStream inputStream = start.getInputStream();
            //转成字符输入流
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
            int len = -1;
            char[] c = new char[1024];
            StringBuffer outputString = new StringBuffer();
            //读取进程输入流中的内容
            while ((len = inputStreamReader.read(c)) != -1) {
                String s = new String(c, 0, len);
                outputString.append(s);
                System.out.print(s);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final ProcessBuilderTest builderTest = new ProcessBuilderTest();
        builderTest.testProcessBuilder();
    }

}
 
