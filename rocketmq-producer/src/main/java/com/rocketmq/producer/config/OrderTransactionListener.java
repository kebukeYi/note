package com.rocketmq.producer.config;

import com.alibaba.fastjson.JSONObject;
import com.rocketmq.producer.dto.OrderDTO;
import com.rocketmq.producer.service.OrderService;
import com.rocketmq.producer.service.TransactionLogService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 12:59
 * @Description :
 * @Version :  0.0.1
 */
@Component
public class OrderTransactionListener implements TransactionListener {

    @Autowired
    OrderService orderService;

    @Autowired
    TransactionLogService transactionLogService;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    //如果 half 消息发送成功了，也就是mq回复 ok，那么就会调用此监听器
    //就开始执行本地事务
    //但是这步监听 由于网络原因mq的消息没有返回回来没有执行怎么办？？？
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        logger.info("开始执行本地事务....");
        LocalTransactionState state;
        try {
            String body = new String(message.getBody());
            OrderDTO order = JSONObject.parseObject(body, OrderDTO.class);
            orderService.createOrder(order, (message.getTransactionId()));
            //返回commit
            state = LocalTransactionState.COMMIT_MESSAGE;
            logger.info("本地事务已提交。{}", message.getTransactionId());
        } catch (Exception e) {
            logger.info("执行本地事务失败。{}", e);
            //返回rollback
            state = LocalTransactionState.ROLLBACK_MESSAGE;
        }
        //这步挂了 服务器进行宕机了 怎么办？？？
        return state;
    }

    //因为各种原因没有进行commit和rollback；那么mq就会检查自身的half消息进行逐一回访
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        logger.info("开始回调查寻本地事务状态。{}", messageExt.getTransactionId());
        LocalTransactionState state;
        String transactionId = messageExt.getTransactionId();
        //根据本地事务表查询消息的状态
        if (transactionLogService.get(transactionId) > 0) {
            state = LocalTransactionState.COMMIT_MESSAGE;
        } else {
            state = LocalTransactionState.UNKNOW;
        }
        logger.info("结束本地事务状态查询：{}", state);
        return state;
    }
}

