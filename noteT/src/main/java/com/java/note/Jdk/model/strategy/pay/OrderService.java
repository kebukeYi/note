package com.java.note.Jdk.model.strategy.pay;

public interface OrderService {

    OrderMessage creatOrderMessage(long orderId, int type);

    OrderResult pay(OrderMessage orderMessage);


}
