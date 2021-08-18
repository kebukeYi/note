package com.java.note.Jdk.lock.transfer.service;


import com.java.note.Jdk.lock.transfer.entity.UserAccount;

import java.math.BigDecimal;

/**
 * 描述 : 转账 业务层接口.
 *
 * @author : MoCha
 * @version : v1
 * @date : 2021-08-08 15:01
 */
public interface TransferAccountService {
    /**
     * 转账接口.
     *
     * @param from   转出账户
     * @param to     转入账户
     * @param amount 转账金额
     * @throws InterruptedException 中断异常
     */
    void transfer(UserAccount from, UserAccount to, BigDecimal amount) throws InterruptedException;
}
