package com.mmy.nio.chat.client;

import java.io.IOException;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 19:17
 * @description:
 * @question:
 * @link:
 **/
public class BClient {

    public static void main(String[] args) {
        try {
            new ChatClient().startClient("127.0.0.1", 8888, "mary");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
 
