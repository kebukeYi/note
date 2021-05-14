package com.java.note.Jdk.model.strategy.pay;

/**
 * @author : kebukeyi
 * @date :  2021-05-07 12:32
 * @description :
 * @question :
 * @usinglink :
 **/
public class Main {

    public static void main(String[] args) {
        int type = 2;
        PayType payType = PayType.getPayTypeByType(type);
        OrderMessage orderMessage = payType.getOrderService().creatOrderMessage(1, type);
        OrderResult orderResult = payType.getOrderService().pay(orderMessage);
        System.out.println(orderResult);
    }
}
 
