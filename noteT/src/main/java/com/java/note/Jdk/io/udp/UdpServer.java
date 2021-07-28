package com.java.note.Jdk.io.udp;

import lombok.var;

import java.io.IOException;
import java.net.*;

/**
 * @author : kebukeYi
 * @date :  2021-07-26 20:00
 * @description:
 * @question:
 * @link:
 **/
public class UdpServer {

    public static void updServer() throws IOException {
        var socket = new DatagramSocket(8888);
        var buf = new byte[256];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            System.out.println("try receive...");
            socket.receive(packet);
            var address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength(),"UTF-8");
            System.out.println(received);
            socket.send(packet);
        }
    }


    public static void main(String[] args) {
        try {
            updServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
 
