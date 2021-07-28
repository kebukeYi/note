package com.java.note.Jdk.io.udp;

import lombok.var;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author : kebukeYi
 * @date :  2021-07-26 20:03
 * @description:
 * @question:
 * @link:
 **/
public class UdpClient {
    public static void udpClient() throws IOException {
        var buf = "Hello".getBytes();
        var socket = new DatagramSocket();
        var address = InetAddress.getByName("127.0.0.1");
        var packet = new DatagramPacket(buf, buf.length, address, 8888);
        socket.send(packet);
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.format("Server echo : %s\n", received);
    }

    public static void main(String[] args) {
        try {
            udpClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
