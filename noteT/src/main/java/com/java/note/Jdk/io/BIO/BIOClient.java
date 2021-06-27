package com.java.note.Jdk.io.BIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/30  19:02
 * @Description
 */
public class BIOClient {

    static final String HOST_NAME = "127.0.0.1";
    static int PORT = 32222;


    public static void main(String[] args) throws Exception {
        Socket client = new Socket(HOST_NAME, PORT);
        Scanner scan = new Scanner(client.getInputStream());//流中的数据流
        scan.useDelimiter("\n");
        PrintStream out = new PrintStream(client.getOutputStream());
        boolean flag = true;
        while (flag) {
            String inputData = InputUtil.getString("请输入要发送的内容：").trim();
            out.println(inputData);
            if (scan.hasNext()) {
                String str = scan.next();
                System.out.println("服务器应答 ： " + str);
            }
            if ("byebye".equalsIgnoreCase(inputData)) {
                flag = false;
            }
        }
        client.close();
    }

}


class InputUtil {

    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));

    public static String getString(String prompt) {
        boolean flag = true;    //数据接受标记
        String str = null;
        while (flag) {
            System.out.println(prompt);
            try {
                str = KEYBOARD_INPUT.readLine();    // 读取一行数据
                if (str == null || "".equals(str)) {
                    System.out.println("数据输入错误，不允许为空！");
                } else {
                    flag = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }


}


