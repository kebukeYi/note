package com.java.note.Jdk.thread.aba;

import lombok.Data;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 12:22
 * @description: 用原子变量实现的并发栈
 * @question:
 * @link:
 **/
@Data
public class Node {
    public final String item;
    public Node next;

    public Node(String item) {
        this.item = item;
    }
}
 
