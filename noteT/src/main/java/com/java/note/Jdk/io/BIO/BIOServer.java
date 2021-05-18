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
 * @Creat Time :
 * @Description
 */
public class BIOServer {

    static final String HOST_NAME = "127.0.0.1";
    static int PORT = 32222;

    public static void main(String[] args) throws Exception {
        //服务端的 channel
        ServerSocket socket = new ServerSocket(PORT);
        socket.setSoTimeout(1000);//超过次时间没有数据发来时  主线程可以做别的事
        System.out.println("服务端已经启动，监听端口为：" + PORT);
        boolean flag = true;
        //
        Socket client = null;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (flag) {
            try {
                //底层硬件 阻塞
                //客户端的 channel 链接
                client = socket.accept();
            } catch (SocketTimeoutException e) {
                System.out.println("做其他事 " + System.currentTimeMillis());
                continue;
            }
            System.out.println(client.hashCode() + " 已连接...");
            //多线程模型
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
                        //也会由于socket写缓冲区满了而阻塞
                        out.println("我是Server " + var);
                        //out 在Java堆中也是会有一个缓冲区 等待满了就会复制到 内核态的socket缓冲区去
                        //out.write();
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
