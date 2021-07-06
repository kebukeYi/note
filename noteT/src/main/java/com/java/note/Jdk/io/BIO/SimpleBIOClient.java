package com.java.note.Jdk.io.BIO;

import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author : kebukeyi
 * @date :  2021-05-18 12:30
 * @description :
 * @question :
 * @usinglink :
 **/
public class SimpleBIOClient {

    public static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        // 采用具有定时执行任务的线程池，来模拟客户端每隔一段时间来向服务端发送消息
        // 这里是每隔3秒钟向服务端发送一条消息
        executorService.scheduleAtFixedRate(() -> {
            try {
                OutputStream outputStream = socket.getOutputStream();
                // 向服务端发送消息（消息内容为：客户端的ip+端口+Hello World，示例：/127.0.0.1:999999 Hello World）
                String message = socket.getLocalSocketAddress().toString() + " Hello World";
                outputStream.write(message.getBytes());
                outputStream.flush();

                InputStream inputStream = socket.getInputStream();
                byte[] bytes = new byte[1024];
                //会阻塞
                val read = inputStream.read(bytes);
                System.out.println("read : " + new String(bytes, 0, read));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }
}
 
