package com.rocketmq.producer.service.lmpl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.rocketmq.producer.config.TransactionProducer;
import com.rocketmq.producer.dto.OrderDTO;
import com.rocketmq.producer.dto.TransactionLogDTO;
import com.rocketmq.producer.mapper.OrderMapper;
import com.rocketmq.producer.mapper.TransactionLogMapper;
import com.rocketmq.producer.model.Order;
import com.rocketmq.producer.service.OrderService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-27 13:05
 * @Description :
 * @Version :  0.0.1
 */
@Service
public class OrderServicelmpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TransactionLogMapper transactionLogMapper;
    @Autowired
    private TransactionProducer producer;

    Snowflake snowflake = new Snowflake(1, 1);

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //执行本地事务时调用，将订单数据和事务日志写入本地数据库
    @Transactional
    @Override
    public Integer createOrder(OrderDTO orderDTO, String transactionId) {
        //1.创建订单
        orderMapper.createOrder(orderDTO);
        //2.写入事务日志
        TransactionLogDTO log = new TransactionLogDTO();
        log.setId(transactionId);
        log.setBusiness("order");
        log.setForeignKey(String.valueOf(orderDTO.getId()));
        transactionLogMapper.insert(log);
        logger.info("订单创建完成。{}", orderDTO);
        return 1;
    }

    //前端调用，只用于向RocketMQ发送事务消息
    @Override
    public void createOrder(OrderDTO order) throws MQClientException {
        //雪花算法生成事务id
        order.setTransactionId(snowflake.nextId());
        //雪花生成订单号码
        order.setOrderNo(snowflake.nextIdStr());
        //单价
        order.setAmount(23.4);
        TransactionSendResult sendResult = producer.send(JSON.toJSONString(order), "order");
    }
}
