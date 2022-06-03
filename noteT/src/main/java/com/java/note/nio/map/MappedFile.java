package com.java.note.nio.map;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName MappedFile
 * @Author kebukeyi
 * @Date 2022/6/3 18:50
 * @Description
 * @Version 1.0.0
 */
public class MappedFile {

    public static void mappedFileForce() throws IOException, InterruptedException {
        String srcFolder = "F:\\a.txt";
        File file1 = new File(srcFolder);
        File tempFile = File.createTempFile("mmaptest", null);

        RandomAccessFile file = new RandomAccessFile(file1, "rw");
        FileChannel channel = file.getChannel();
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        mappedByteBuffer.put(new byte[1020]);
        mappedByteBuffer.putInt(1);
        System.out.println(mappedByteBuffer.position());
        System.out.println(mappedByteBuffer.limit());
        //此时 数据还在 内存中
        mappedByteBuffer.force();
        Thread.sleep(3000);
        System.out.println(mappedByteBuffer.position());
        System.out.println(mappedByteBuffer.limit());
    }

    public static void main(String[] args) {
        try {
            mappedFileForce();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
