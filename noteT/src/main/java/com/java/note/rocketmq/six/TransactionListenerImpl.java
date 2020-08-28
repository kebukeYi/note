package com.java.note.rocketmq.six;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/3  17:15
 * @Description
 */
public class TransactionListenerImpl implements TransactionListener {

    private AtomicInteger transactionIndex = new AtomicInteger(0);

    //存储当前事务的状态
    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    //执行本地事务
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        int value = transactionIndex.getAndIncrement();
        //0：执行中；1：本地事务执行成功；2：本地事务执行失败；
        localTrans.put(message.getTransactionId(), 0);
        try {
            System.out.println("正在执行本地事务...");
            Thread.sleep(3000);
            System.out.println("正在执行本地事务 - 成功");
            localTrans.put(message.getTransactionId(), 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            localTrans.put(message.getTransactionId(), 2);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    //消息回查
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        //获取对应事务状态ID
        Integer status = localTrans.get(messageExt.getTransactionId());
        System.out.println("消息回查--" + messageExt.getTransactionId() + " ,  状态 ：" + status);
        if (null != status) {
            switch (status) {
                case 0:
                    return LocalTransactionState.UNKNOW;
                case 1:
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}

