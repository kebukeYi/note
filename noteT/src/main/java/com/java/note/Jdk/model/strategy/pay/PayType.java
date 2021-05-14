package com.java.note.Jdk.model.strategy.pay;

import com.java.note.Jdk.model.strategy.pay.lmpl.AliPay;
import com.java.note.Jdk.model.strategy.pay.lmpl.CardPay;
import com.java.note.Jdk.model.strategy.pay.lmpl.VxPay;

public enum PayType {

    ALi_PAY(1, new AliPay()),
    VX_PAY(2, new VxPay()),
    CARD_PAY(3, new CardPay());

    private int type;
    private OrderService orderService;


    PayType(int i, OrderService orderService) {
        this.type = i;
        this.orderService = orderService;
    }

    public OrderService getOrderService() {
        return this.orderService;
    }

    public static PayType getPayTypeByType(int type) {
        for (PayType payType : PayType.values()) {
            if (payType.type == type) {
                return payType;
            }
        }
        return null;
    }


}
