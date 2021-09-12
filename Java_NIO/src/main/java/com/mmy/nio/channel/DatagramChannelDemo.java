package com.mmy.nio.channel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;

/**
 * @author : kebukeYi
 * @date :  2021-09-11 18:26
 * @description:
 * @question:
 * @link:
 **/
public class DatagramChannelDemo {

    @Test
    public void sendDatagram() throws IOException, InterruptedException {
        DatagramChannel sendChannel = DatagramChannel.open();
        InetSocketAddress sendAddress = new InetSocketAddress("127.0.0.1", 9999);
        while (true) {
            sendChannel.send(ByteBuffer.wrap("发包".getBytes("UTF-8")),
                    sendAddress);
            System.out.println("发包端发包");
            Thread.sleep(2000);
        }
    }

    @Test
    public void receive() throws Exception {
        final DatagramChannel datagramChannel = DatagramChannel.open();
        //也可以设置成 非阻塞
        datagramChannel.configureBlocking(false);
        final InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
        datagramChannel.bind(inetSocketAddress);
        final DatagramSocket socket = datagramChannel.socket();
        final ByteBuffer byteBuffer = ByteBuffer.allocate(321);
        while (true) {
            byteBuffer.clear();
            //会阻塞 设置了非阻塞后就不阻塞了
            final SocketAddress receive = datagramChannel.receive(byteBuffer);
            byteBuffer.flip();
            if (receive == null) continue;
            System.out.println(receive.toString());
            System.out.println(Charset.forName("UTF-8").decode(byteBuffer));
        }
    }


    public void testConect1() throws IOException {
        DatagramChannel connChannel = DatagramChannel.open();
        connChannel.bind(new InetSocketAddress(9998));
        connChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
        connChannel.write(ByteBuffer.wrap("发包".getBytes("UTF-8")));
        ByteBuffer readBuffer = ByteBuffer.allocate(512);
        while (true) {
            try {
                readBuffer.clear();
                connChannel.read(readBuffer);
                readBuffer.flip();
                System.out.println(Charset.forName("UTF-8").decode(readBuffer));
            } catch (Exception e) {
            }
        }
    }
}



 
