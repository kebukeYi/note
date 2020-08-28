package com.java.note.Jdk.io.BIO;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : mmy
 * @Creat Time : //  :
 * @Description
 */
public class BIOServer {

    static final String HOST_NAME = "127.0.0.1";
    static int PORT = 32222;


    public static void main(String[] args) throws Exception {
        ServerSocket socket = new ServerSocket(PORT);
        socket.setSoTimeout(1000);//超过次时间没有数据发来时  主线程可以做别的事
        System.out.println("服务端已经启动，监听端口为：" + PORT);
        boolean flag = true;
        Socket client = null;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (flag) {
            try {
                client = socket.accept();//底层硬件 阻塞
            } catch (SocketTimeoutException e) {
                System.out.println("做其他事 " + System.currentTimeMillis());
                continue;
            }
            System.out.println(client.hashCode() + " 已连接...");
            executorService.submit(new EchoClientHandler(client));
        }
        executorService.shutdown();
        socket.close();
    }

    private static class EchoClientHandler implements Runnable {
        private Socket client;
        private Scanner scanner;
        private PrintStream out;
        private boolean flag = true;

        public EchoClientHandler(Socket client) {
            this.client = client;
            try {
                this.scanner = new Scanner(this.client.getInputStream());
                this.scanner.useDelimiter("\n");
                this.out = new PrintStream(this.client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (this.flag) {
                if (this.scanner.hasNext()) {
                    String var = this.scanner.next().trim();
                    System.out.println("收到客户端发来的 ：" + var);
                    if ("byebye".equals(var)) {
                        this.out.print("");
                        this.flag = false;
                    } else {
                        out.println("我是Server " + var);
                    }
                }
            }
            try {
                this.scanner.close();
                this.out.close();
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
