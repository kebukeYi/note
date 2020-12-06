package com.java.note.Jdk.OOM;

import java.nio.ByteBuffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  12:21
 * @Description
 */
public class DirectBufferMemoryDemo {

    public static void main(String[] args) {
//        System.out.println("maxDirectMemory is:" + sun.misc.VM.maxDirectMemory() / 1024 / 1024 + " MB");

        //Byte Buffer. allocate( capability)第一种方式是分配内存,属管结范,由于需要贝所以速度相对较慢
//        ByteBuffer buffer = ByteBuffer.allocate(4000 * 1024 * 1024);

        //Byte Buffer. alLocteDirect( capability)第一种方式是分OS本地内存,不属于GC管范国,由于不需要内存考所纵速度相对较快
        ByteBuffer buffer = ByteBuffer.allocateDirect(4000 * 1024 * 1024);

    }
}
