package com.mmy.nio.lockfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 00:32
 * @description: 文件锁
 * @question:
 * @link:
 **/
public class FileLockDemo {

    public static void main(String[] args) throws Exception {
        String input = "atguigu";
        System.out.println("input:" + input);

        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());

        String filePath = "d:\\atguigu\\01.txt";
        Path path = Paths.get(filePath);

        FileChannel channel =
                FileChannel.open(path,
                        StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        channel.position(channel.size() - 1);

        // 获得锁方法一：lock()，阻塞方法，当文件锁不可用时，当前进程会被挂起
        //lock = channel.lock();// 无参 lock()为独占锁
        // lock = channel.lock(0L, Long.MAX_VALUE, true);//有参 lock()为共享锁，有写操作会报异常
        // 获得锁方法二：trylock()，非阻塞的方法，当文件锁不可用时，tryLock()会得到 null

        //加锁
        FileLock lock = channel.lock(0L, Long.MAX_VALUE, true);
        System.out.println("是否共享锁：" + lock.isShared());

        channel.write(buffer);
        channel.close();

        //读文件
        readFile(filePath);
    }

    private static void readFile(String filePath) throws Exception {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String tr = bufferedReader.readLine();
        System.out.println("读取出内容：");
        while (tr != null) {
            System.out.println(" " + tr);
            tr = bufferedReader.readLine();
        }
        fileReader.close();
        bufferedReader.close();
    }


}
 
