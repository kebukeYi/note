package com.java.note.spring.Transaction;

import com.java.note.Jdk.proxy.main.Autowried;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/15  17:42
 * @Description 编程式事务管理
 */
public class testTransaction {

    @Autowried
    TransactionTemplate transactionTemplate;

    @Autowried
    PlatformTransactionManager platformTransactionManager;

    /**
     * 使用TransactionTemplate 进行编程式事务管理
     */
    public void testTransactionTemplate() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //业务代码
                try {

                } catch (Exception e) {
                    //回滚
                    status.setRollbackOnly();
                }
            }
        });
    }


    /**
     * 使用 TransactionManager 进行编程式事务管理
     */
    public void testPlatformTransactionManager() {
        TransactionStatus transaction = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            //业务代码
            platformTransactionManager.commit(transaction);
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
        }
    }

    /**
     * 声明式事务管理
     * 推荐使用（代码侵入性最小），实际是通过 AOP 实现（基于@Transactional 的全注解方式使用最多）。
     */
    @Transactional
    public void testTransactional() {
        //业务代码
    }


}
