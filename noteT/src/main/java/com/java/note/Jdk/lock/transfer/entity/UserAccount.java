package com.java.note.Jdk.lock.transfer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述 : 用户账户.
 *
 * @author : MoCha
 * @version : v1
 * @date : 2021-08-08 15:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

    /**
     * 用户账户名.
     */
    private String name;

    /**
     * 账户余额.
     */
    private BigDecimal balance;

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
        System.out.println(name + balance);
    }


    // /**
    //  * 可重入锁.
    //  *
    //  * <p>说明：</p>
    //  * 1. 解决互相转账时，出现死锁问题
    //  * 破坏不可剥夺条件的核心在于：让当前线程自己主动释放占用的资源
    //  * <p>
    //  * 2. 单机环境下，可以使用该方案，但在分布式环境下，选用分布式锁较为恰当
    //  */
    private final Lock lock = new ReentrantLock();
}
