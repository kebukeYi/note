package com.java.note.Jdk.model.strategy.pay;

/**
 * @author : kebukeyi
 * @date :  2021-05-07 12:47
 * @description :
 * @question :
 * @usinglink :
 **/
public abstract class AbstractPay implements OrderService {


    protected abstract boolean calibrationParameters(OrderMessage orderMessage);

    protected abstract boolean checkAuth(OrderMessage orderMessage);

    protected abstract OrderResult realPay(OrderMessage orderMessage);


    public OrderResult pay(OrderMessage orderMessage) {
        if (calibrationParameters(orderMessage)) {
            if (checkAuth(orderMessage)) {
                return realPay(orderMessage);
            }
        }
        return null;
    }

}
 
