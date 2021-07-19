package com.java.note.Algorithm.xiaoxi;

import java.io.*;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 09:22
 * @description:
 * @question:
 * @link:
 **/
public class f {

    public static void main(String[] args) throws IOException {
        InputStream in = new FileInputStream("noteT/src/main/resources/test.txt");
        InputStream bin = new BufferedInputStream(in);
        DataInputStream din = new DataInputStream(bin);
        int data = din.readInt();
        System.out.println(data);
    }
}
 
