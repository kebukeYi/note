package com.java.note.Jdk.lock.transfer.service.impl;


import com.java.note.Jdk.lock.transfer.entity.UserAccount;
import com.java.note.Jdk.lock.transfer.service.TransferAccountService;

import java.math.BigDecimal;

/**
 * 描述 : 转账 业务层接口实现.
 *
 * @author : MoCha
 * @version : v1
 * @date : 2021-08-08 15:04
 */
// @Slf4j
public class TransferAccountServiceImpl implements TransferAccountService {

    @Override
    public void transfer(UserAccount from, UserAccount to, BigDecimal amount) {
        while (true) {
            if (from.getLock().tryLock()) {
                try {
                    if (to.getLock().tryLock()) {
                        try {
                            from.setBalance(from.getBalance().subtract(amount));
                            to.setBalance(to.getBalance().add(amount));

                            break;
                        } finally {
                            to.getLock().unlock();
                        }
                    }
                } finally {
                    from.getLock().unlock();
                }
            }
        }
    }
    // @Override
    // public void transfer(UserAccount from, UserAccount to, int amount) throws InterruptedException {
    //     // 先锁转出
    //     synchronized (from) {
    //         // log.info("Thread: [{}] get [{}]", Thread.currentThread().getName(), from.getName());
    //         Thread.sleep(100);
    //         // 再锁转入
    //         synchronized (to) {
    //             // log.info("Thread: [{}] get [{}]", Thread.currentThread().getName(), from.getName());
    //             // TODO 业务代码
    //         }
    //     }
    // }
}
