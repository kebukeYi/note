package com.java.note.Jdk.io.BIO;

import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 12:25
 * @description :
 * @question :
 * @usinglink : https://juejin.cn/post/6844904003889807368
 **/
public class SimpleBIOServer {


    public static void main(String[] args) throws IOException {
//        oneBio();
        twoBio();
    }

    public static void oneBio() throws IOException {
        //创建服务端的serverSocket : 是建立 "连接上的客户端socket通信的"  的父 socket 并且 还担任监听端口作用
        ServerSocket serverSocket = new ServerSocket(8080);
        //不断进行监听处理连接
        while (true) {
            // accept()方法是个阻塞方法，如果没有客户端来连接，线程就会一直阻塞在这儿
            //怎么个阻塞法?
            Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            //怎么释放 bytes 内存???
            // read()方法是一个阻塞方法，当没有数据可读时，线程会一直阻塞在read()方法上 ,也就是 从始之终只 有一个 main 主线程在干事
            try {
                //单线程模型
                while ((len = inputStream.read(bytes)) != -1) {
                    System.out.println("receive : " + new String(bytes, 0, len));
                    accept.getOutputStream().write(new String("我收到了").getBytes());
                    accept.getOutputStream().flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void twoBio() throws IOException {
        //创建服务端的serverSocket : 是建立 "连接上的客户端socket通信的"  的父 socket 并且 还担任监听端口作用
        ServerSocket serverSocket = new ServerSocket(8080);
        //不断进行监听处理连接
        while (true) {
            // accept()方法是个阻塞方法，如果没有客户端来连接，线程就会一直阻塞在这儿
            //怎么个阻塞法?
            Socket accept = serverSocket.accept();
            //多线程模型
            runSocket(accept);
        }

    }


    public static void runSocket(Socket socket) throws IOException {
        //会新创建一个线程来读取数据，这样就不会造成main线程阻塞在read()方法上了
        new Thread(() -> {
            InputStream inputStream = null;
            byte[] bytes = new byte[1024];
            int len;
            try {
                inputStream = socket.getInputStream();
                //怎么释放 bytes 内存???
                // read()方法是一个阻塞方法，当没有数据可读时，线程会一直阻塞在read()方法上 ,也就是 从始之终只 有一个 main 主线程在干事
                while ((len = inputStream.read(bytes)) != -1) {
                    val s = new String(bytes, 0, len);
                    System.out.println("receive : " + s);
                    socket.getOutputStream().write((s+ "  我收到了 ").getBytes());
                    socket.getOutputStream().flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }


}
 
