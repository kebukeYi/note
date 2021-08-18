package com.java.note.Jdk.lock.transfer.thread;


import com.java.note.Jdk.lock.transfer.entity.UserAccount;
import com.java.note.Jdk.lock.transfer.service.TransferAccountService;

import java.math.BigDecimal;

/**
 * 描述 : .
 *
 * @author : MoCha
 * @version : v1
 * @date : 2021-08-08 20:41
 */
public class TransferThread extends Thread {
    private String name;
    private UserAccount from;
    private UserAccount to;
    private volatile BigDecimal amount;
    private TransferAccountService transfer;

    public TransferThread(String name, UserAccount from, UserAccount to, BigDecimal amount, TransferAccountService transfer) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.transfer = transfer;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(name);
        try {
            transfer.transfer(from, to, amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
