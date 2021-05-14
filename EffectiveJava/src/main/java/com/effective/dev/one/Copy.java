package com.effective.dev.one;

import java.io.*;

/**
 * @author : kebukeyi
 * @date :  2021-04-14 18:17
 * @description :
 * @usinglink : https://github.com/jbloch/effective-java-3e-source-code/blob/master/src/effectivejava/chapter2/item9/tryfinally/Copy.java
 **/
public class Copy {

    private static final int BUFFER_SIZE = 8 * 1024;

    //非常糟糕的写法
    //异常B会覆盖异常A的堆栈轨迹
    static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                //倘若这里发生了异常A
                while ((n = in.read(buf)) >= 0) {
                    out.write(buf, 0, n);
                }
            } finally {
                //这里发生了异常B
                out.close();
            }
        } finally {
            in.close();
        }
    }

    static void copy2(String src, String dst) throws IOException {
        try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            //倘若这里发生了异常A
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        }
    }

    // try-with-resources with a catch clause
    static String firstLineOfFile(String path, String defaultVal) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            return defaultVal;
        }
    }


    public static void main(String[] args) throws IOException {
        String src = args[0];
        String dst = args[1];
        copy(src, dst);
    }

}
 
