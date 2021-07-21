package cn.gof.chain.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 13:01
 * @description:
 * @question:
 * @link:
 **/
public class HandlerChain {


    //链表方式
    Handler head;
    Handler tail;

    public void addHandler(Handler handler) {
        handler.setNext(null);
        if (head == null) {
            head = handler;
            tail = handler;
            return;
        }
        tail.setNext(handler);
        tail = handler;
    }

    //链表方式
    public void handle() {
        if (head != null) {
            head.handler();
        }
    }

    //数组方式
    List<Handler> handlers = new ArrayList<>();

    //数组方式
    public void handles() {
        for (Handler handler : handlers) {
            boolean handled = handler.doHandler();
            if (handled) {
                break;
            }
        }
    }


}
 
