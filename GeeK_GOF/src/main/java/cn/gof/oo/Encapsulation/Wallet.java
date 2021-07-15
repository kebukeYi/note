package cn.gof.oo.Encapsulation;

import org.springframework.util.SimpleIdGenerator;

import java.math.BigDecimal;

/**
 * @author : kebukeyi
 * @date :  2021-07-13 20:02
 * @description :  封装特性
 * @question :
 * @usinglink :
 **/
public class Wallet {

    private String id;
    private long createTime;
    private BigDecimal balance;
    private long balanceLastModifiedTime;


    public Wallet() {
        this.id = new SimpleIdGenerator().generateId().toString();
        this.createTime = System.currentTimeMillis();
        this.balance = BigDecimal.ZERO;
        this.balanceLastModifiedTime = System.currentTimeMillis();
    }


    //并没有提供很多的 getter setter 方法
    //很好的封装性
    public void increaseBigDecimal(BigDecimal increasedAmount) throws Exception {
        if (increasedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("message");
        }
        this.balance.add(increasedAmount);
        this.balanceLastModifiedTime = System.currentTimeMillis();
    }

    public void decreaseBalance(BigDecimal decreasedAmount) throws Exception {
        if (decreasedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("message");
        }
        if (decreasedAmount.compareTo(this.balance) > 0) {
            throw new Exception("message");
        }
        this.balance.subtract(decreasedAmount);
        this.balanceLastModifiedTime = System.currentTimeMillis();
    }


}
 
