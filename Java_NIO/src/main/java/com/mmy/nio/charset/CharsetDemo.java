package com.mmy.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.Set;

/**
 * @author : kebukeYi
 * @date :  2021-09-12 19:14
 * @description:
 * @question:
 * @link:
 **/
public class CharsetDemo {

    public static void main(String[] args) throws CharacterCodingException {
        //1 获取charset对象
        Charset charset = Charset.forName("UTF-8");

        //2 获得编码器对象
        CharsetEncoder charsetEncoder = charset.newEncoder();

        //3 创建缓冲区
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("atguigu尚硅谷");
        charBuffer.flip();

        //4 编码
        ByteBuffer byteBuffer = charsetEncoder.encode(charBuffer);
        System.out.println("编码之后结果：");
        for (int i = 0; i < byteBuffer.limit(); i++) {
            System.out.println(byteBuffer.get());
        }

        //5 获取解码器对象
        byteBuffer.flip();
        CharsetDecoder charsetDecoder = charset.newDecoder();

        //6 解码
        CharBuffer charBuffer1 = charsetDecoder.decode(byteBuffer);
        System.out.println("解码之后结果：");
        System.out.println(charBuffer1.toString());

        //7 使用GBK解码
        Charset charset1 = Charset.forName("GBK");
        byteBuffer.flip();
        CharBuffer charBuffer2 = charset1.decode(byteBuffer);
        System.out.println("使用其他编码进行解码：");
        System.out.println(charBuffer2.toString());

        //8.获取Charset所支持的字符编码
        Map<String, Charset> map = Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> set = map.entrySet();
        for (Map.Entry<String, Charset> entry : set) {
            System.out.println(entry.getKey() + "=" + entry.getValue().toString());
        }

    }


}
 
