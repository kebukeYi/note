package com.mmy.nio.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 17:49
 * @description:
 * @question:
 * @link:
 **/
public class FileChannelWrite {

    public static void transferFrom(String srcPath, String destPath) throws IOException {
        final RandomAccessFile rw = new RandomAccessFile(srcPath, "rw");
        final FileChannel fromChannel = rw.getChannel();

        final RandomAccessFile rw1 = new RandomAccessFile(destPath, "rw");
        final FileChannel toChannel = rw1.getChannel();

        long position = fromChannel.position();
        long size = fromChannel.size();
        toChannel.transferFrom(fromChannel, position, size);

        rw.close();
        rw1.close();
        System.out.println("over");
    }

    public static void transferFromTo(String srcPath, String destPath) throws IOException {
        RandomAccessFile aFile = new
                RandomAccessFile(srcPath, "rw");
        FileChannel fromChannel = aFile.getChannel();
        RandomAccessFile bFile = new
                RandomAccessFile(destPath, "rw");
        FileChannel toChannel = bFile.getChannel();
        long position = 0;
        long count = fromChannel.size();
        fromChannel.transferTo(position, count, toChannel);
        aFile.close();
        bFile.close();
        System.out.println("over!");
    }

    public static void main(String[] args) throws IOException {
        String srcPath = "F:\\01.txt";
        String destPath = "F:\\02.txt";
        transferFrom(srcPath, destPath);

        srcPath = "F:\\02.txt";
        destPath = "F:\\03.txt";
        transferFromTo(srcPath, destPath);
    }


}
 
