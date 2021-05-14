package com.java.note.Jdk.model.strategy.pay.lmpl;

import com.java.note.Jdk.model.strategy.pay.OrderMessage;
import com.java.note.Jdk.model.strategy.pay.OrderResult;
import com.java.note.Jdk.model.strategy.pay.OrderService;

/**
 * @author : kebukeyi
 * @date :  2021-05-07 12:21
 * @description :
 * @question :
 * @usinglink :
 **/
public class AliPay implements OrderService {

    @Override
    public OrderMessage creatOrderMessage(long orderId, int type) {
        OrderMessage orderMessage = OrderMessage.builder().account(111 * 2).count(111).orderId(orderId).build();
        return orderMessage;
    }

    @Override
    public OrderResult pay(OrderMessage orderMessage) {
        System.out.println("----支付宝支付----");
        System.out.println("支付宝支付111元");
        return OrderResult.builder().code(1).message("支付宝-success").result(orderMessage.getType()).build();
    }
}
 
