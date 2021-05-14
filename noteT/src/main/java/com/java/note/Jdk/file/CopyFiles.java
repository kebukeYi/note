package com.java.note.Jdk.file;

import java.io.*;

/**
 * @author : kebukeyi
 * @date :  2021-04-28 19:01
 * @description :
 * @usinglink :
 **/
public class CopyFiles {

    public void copyFile() {
        File srcFile = new File("E://atest//atest.txt");
        File dstFile = new File("E://btest//btest.txt");
        BufferedReader in = null;
        BufferedWriter out = null;
        try {

            in = new BufferedReader(new FileReader(srcFile));
            out = new BufferedWriter(new FileWriter(dstFile));

            String line = null;
            while ((line = in.readLine()) != null) {
                out.write(line + "/r/n");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }
    }
}
 
