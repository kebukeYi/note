package com.rocketmq.producer.service;

import com.rocketmq.producer.dto.OrderDTO;
import org.apache.rocketmq.client.exception.MQClientException;

public interface OrderService {

    Integer createOrder(OrderDTO order, Long transactionId);

    void createOrder(OrderDTO order) throws MQClientException;
}
