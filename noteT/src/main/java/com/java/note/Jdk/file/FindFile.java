package com.java.note.Jdk.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author : kebukeYi
 * @date :  2022-01-07 18:21
 * @description: txt 文本可以 合并的 但是 doc 文件就不行了，因为doc不是文本文件  你这种方式合成文本文件是可以的
 * @question: doc文件实际上是一个 zip文件 , 中间有一定的xml规则的 不能像二进制文件那样直接叠加就行了，有格式的
 * 一般的doc 首先创建一个doc对象，然后添加页面，添加内容，； 合并的话实际上就是拷贝页面 添加进去，； 最后保存 关闭对象
 * @link:
 **/
public class FindFile {

    public static final int BUFSIZE = 1024 * 8 * 8;

    private static ArrayList<String> arrayList = new ArrayList<>();

    //F:\\BaiduNetdiskDownload\\DFS
    public static void findFile(File file, FileChannel destFileName, String suffix) {
        if (file.isDirectory()) {
            //F:\\BaiduNetdiskDownload\\DFS\\001~021
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                if (subFile.isDirectory()) {
                    findFile(subFile, destFileName, suffix);
                } else {
                    final String subFileName = subFile.getName();
                    if (subFileName.endsWith(suffix)) {
                        final String name = file.getName();
                        //System.out.println(name);
                        try {
                            final String filePath = subFile.getPath();
                            //System.out.println(filePath);
                            arrayList.add(filePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

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

    public static void transferFromTo(String name, String srcPath, FileChannel toChannel) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile(srcPath, "rw");
        // 文件编码是utf8,需要用utf8解码
        Charset charset = Charset.forName("utf-8");
        FileChannel fromChannel = aFile.getChannel();
        //实现文件追加效果
        toChannel.position(toChannel.size());
        long position = 0;
        long count = fromChannel.size();
        fromChannel.force(true);
        toChannel.force(true);

        fromChannel.transferTo(position, count, toChannel);
        aFile.close();
        System.out.println("over!");
    }

    public static void transferFromTo(String srcPath, String destPath) throws Exception {
        File file1 = new File(srcPath);
        File file2 = new File(destPath);
        FileInputStream input = new FileInputStream(file1);
        FileOutputStream output = new FileOutputStream(file2);
        FileChannel fout = output.getChannel();    // 声明FileChannel对象
        FileChannel fin = input.getChannel();    // 定义输入的通道
        fout.position(fout.size());//追加模式
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int temp = 0;
        while ((temp = fin.read(buf)) != -1) {
            buf.flip();
            fout.write(buf);
            buf.clear();    // 清空缓冲区,所有的状态变量的位置恢复到原点
        }
        fout.force(true);
        fin.close();
        fout.close();
        input.close();
        output.close();
    }

    public static void transferFrom(String name, String srcPath, FileChannel fileChannel) throws Exception {
        // 文件编码是utf8,需要用utf8解码
        Charset charset = Charset.forName("GBK");
        File file1 = new File(srcPath);
        FileInputStream input = new FileInputStream(file1);
        FileChannel fout = fileChannel;    // 声明FileChannel对象
        FileChannel fin = input.getChannel();    // 定义输入的通道
        fout.position(fout.size());//追加模式
        ByteBuffer buf = ByteBuffer.allocate(1024);
        fout.write(charset.encode("标题：" + name));
        int temp = 0;
        while ((temp = fin.read(buf)) != -1) {
            buf.flip();
            fout.write(buf);
            buf.clear();    // 清空缓冲区,所有的状态变量的位置恢复到原点
        }
        fout.force(true);
        fin.close();

        input.close();
    }

    public static void mergeFiles(String outFile, ArrayList<String> files) {
        FileChannel outChannel = null;
        long count = 0L;
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for (String f : files) {
                //F:\BaiduNetdiskDownload\DFS\001~021\001_在大规模电商场景下为什么需要分布式文件系统？\笔记.doc
                final String[] split = f.split("\\\\");
                //final String name = "标题：" + split[4];
                Charset charset = Charset.forName("utf-8");
                CharsetDecoder chdecoder = charset.newDecoder();
                CharsetEncoder chencoder = charset.newEncoder();
                FileChannel fc = new FileInputStream(f).getChannel();
                count += fc.size();
                //System.out.println(name + "  " + fc.size());
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                //bb.put(name.getBytes("UTF-8"));
                //解码
                CharBuffer charBuffer = chdecoder.decode(bb);
                //编码
                ByteBuffer nbuBuffer = chencoder.encode(charBuffer);
                //outChannel.write(nbuBuffer);
                while (fc.read(nbuBuffer) != -1) {
                    bb.flip();
                    nbuBuffer.flip();
                    outChannel.write(nbuBuffer);
                    bb.clear();
                    nbuBuffer.clear();
                }
                fc.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
                System.out.println(count / 1024);
            } catch (IOException ignore) {
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String path = "F:\\BaiduNetdiskDownload\\DFS";
        String suffix = ".txt";
        String destFileName = "F:\\BaiduNetdiskDownload\\sum.doc";
        RandomAccessFile bFile = new RandomAccessFile(destFileName, "rw");
        FileChannel toChannel = bFile.getChannel();
        findFile(new File(path), toChannel, suffix);
        System.out.println(arrayList.size());
        mergeFiles(destFileName, arrayList);
    }
}
 
